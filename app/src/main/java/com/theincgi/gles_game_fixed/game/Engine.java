package com.theincgi.gles_game_fixed.game;

import com.theincgi.gles_game_fixed.game.entity.Entity;
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
    LinkedList<Entity> toAdd = new LinkedList<>(), toRemove = new LinkedList<>();
    private boolean running = false;
    ScheduledFuture sortTimer;

    private Engine(){

    }
    public static Engine instance() {
        return INSTANCE;
    }

    public void onTick(){
        long time = System.currentTimeMillis();

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
            eIter.next().onTick( time );
        }
    }

    public void addEntity(Entity entity){
        synchronized (toAdd) {
            toAdd.addLast(entity);
        }
    }
    public void removeEntity(Entity entity){
        synchronized (toRemove) {
            toRemove.addLast(entity);
        }
    }
    public void clearEntities() {
        synchronized (toRemove) {
            toRemove.addAll(entities);
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

    public void drawAll(float[] mvpm){
        synchronized (entities){
            for(Entity e : entities){
                e.draw(mvpm);
            }
        }
    }
}
