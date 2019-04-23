package com.theincgi.gles_game_fixed.game;

import com.theincgi.gles_game_fixed.game.entity.Entity;
import com.theincgi.gles_game_fixed.game.obstacles.BaseObstacle;
import com.theincgi.gles_game_fixed.render.Camera;
import com.theincgi.gles_game_fixed.render.GLProgram;
import com.theincgi.gles_game_fixed.render.GLPrograms;
import com.theincgi.gles_game_fixed.render.LightSource;
import com.theincgi.gles_game_fixed.utils.Location;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Engine {
    private static final Engine INSTANCE = new Engine();
    LinkedList<Entity> entities = new LinkedList<>();
    LinkedList<BaseObstacle> obstacales = new LinkedList<>();
    LinkedList<Entity> toAdd = new LinkedList<>(), toRemove = new LinkedList<>();
    private boolean running = false;
    ScheduledFuture sortTimer;

    LinkedList<BaseObstacle> obstToAdd = new LinkedList(), obstToRemove = new LinkedList();

    LightSource lightSource = new LightSource(0, 100, 0);
    float globalIlluminationBrightness = .3f;

    private Engine(){

    }
    public static Engine instance() {
        return INSTANCE;
    }

    public void onTick(){
        long time = System.currentTimeMillis();

        synchronized (obstacales){
            synchronized (obstToAdd){
                obstacales.addAll(obstToAdd);
                obstToAdd.clear();
            }
            synchronized (obstToRemove){
                obstacales.removeAll(obstToRemove);
                obstToRemove.clear();
            }
        }
        synchronized (entities){
            synchronized (toAdd) {
                entities.addAll( toAdd );
                toAdd.clear();
            }
            synchronized (toRemove){
                entities.removeAll( toRemove );
                toRemove.clear();
            }
        }


        Iterator<Entity> eIter = entities.iterator();
        while(eIter.hasNext()) {
            eIter.next().onTick( this, time );
        }
    }

    public Iterator<BaseObstacle> getObstacleItterator(){
        return obstacales.iterator();
    }

    public void addEntity(Entity entity){
        synchronized (toAdd) {
            toAdd.addLast(entity);
        }
    }
    public void addObstacale(BaseObstacle obst){
        synchronized (obstToAdd){
            obstToAdd.addLast(obst);
        }
    }
    public void removeEntity(Entity entity){
        synchronized (toRemove) {
            toRemove.addLast(entity);
        }
    }
    public void removeObstacle(BaseObstacle obst){
        synchronized (obstToRemove) {
            obstToRemove.addLast(obst);
        }
    }
    public void clearEntities() {
        synchronized (toRemove) {
            toRemove.addAll(entities);
        }
    }
    public void clearObstacles() {
        synchronized (obstToRemove) {
            obstToRemove.addAll(obstacales);
        }
    }

    public void start(){
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        sortTimer = scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                onTick();
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }
    public void stop(){
        if(sortTimer!=null)
            sortTimer.cancel(false);
    }

    public LinkedList<Entity> getEntities() {
        return entities;
    }

    public void drawAll(float[] mvpm, Camera camera){
        for(GLProgram g : GLPrograms.getLoadedPrograms()) {
            g.use(); //important! call use before passing info to shader
            g.trySetUniform("lightPosition", lightSource.getPos());
            g.trySetUniform("lightColor", lightSource.getColor());
            g.trySetUniform("lightBrightness", lightSource.getBrightness());
            g.trySetUniform("time", (float)System.currentTimeMillis());
            g.trySetUniform("cameraPosition", camera.getPos());
            g.trySetUniform("globalIlluminationBrightness", globalIlluminationBrightness);
            g.trySetUniform("textureMode", 0); //textureless
            g.trySetMatrix("projectionMatrix", mvpm);
        }
        synchronized (entities){
            for(Entity e : entities){
                e.draw(mvpm, camera);
            }
        }
        synchronized (obstacales){
            for(BaseObstacle o : obstacales){
                o.draw(mvpm, camera);
            }
        }
    }
}
