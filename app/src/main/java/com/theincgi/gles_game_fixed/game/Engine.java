package com.theincgi.gles_game_fixed.game;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Matrix;
import android.widget.Toast;

import com.theincgi.gles_game_fixed.game.entities.Ball;
import com.theincgi.gles_game_fixed.game.entities.BasicBall;
import com.theincgi.gles_game_fixed.game.entity.Entity;
import com.theincgi.gles_game_fixed.game.levels.Level;
import com.theincgi.gles_game_fixed.game.levels.TestLevel;
import com.theincgi.gles_game_fixed.game.obstacles.BaseObstacle;
import com.theincgi.gles_game_fixed.render.Camera;
import com.theincgi.gles_game_fixed.render.GLProgram;
import com.theincgi.gles_game_fixed.render.GLPrograms;
import com.theincgi.gles_game_fixed.render.LightSource;
import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Engine {
    private static Engine INSTANCE;
    LinkedList<Entity> entities = new LinkedList<>();
    LinkedList<BaseObstacle> obstacales = new LinkedList<>();
    LinkedList<Entity> toAdd = new LinkedList<>(), toRemove = new LinkedList<>();
    private boolean running = false;
    ScheduledFuture sortTimer;
    private static float[] defaultGravity = {0,-.05f, 0};
    public float[] gravity = new float[4];
    LinkedList<BaseObstacle> obstToAdd = new LinkedList(), obstToRemove = new LinkedList();

    LightSource lightSource = new LightSource(0, 100, 0);
    float globalIlluminationBrightness = .3f;
    private Sensor rotationSensor;
    public Level level;
    private Camera camera;

    BasicBall theBall;

    public static void init(Context context){
        INSTANCE = INSTANCE==null? new Engine( context ) : INSTANCE;
    }


    private Engine(Context context){
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            Toast.makeText(context, "Gravity AVAILABLE", Toast.LENGTH_SHORT).show();
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            sensorManager.registerListener(
                    onSensorEvent,
                    rotationSensor,
                    1_000_000/Engine.ticksPerSecond());
        } else if(sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR) != null){
            Toast.makeText(context, "GAME_ROTATION_VECTOR AVAILABLE", Toast.LENGTH_SHORT).show();
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
            sensorManager.registerListener(
                    onSensorEvent,
                    rotationSensor,
                    1_000_000/Engine.ticksPerSecond());
        } else if(sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) != null){
            Toast.makeText(context, "ROTATION_VECTOR AVAILABLE", Toast.LENGTH_SHORT).show();
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            sensorManager.registerListener(
                    onSensorEvent,
                    rotationSensor,
                    1_000_000/Engine.ticksPerSecond());
        } else if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            Toast.makeText(context, "ACCELEROMETER AVAILABLE", Toast.LENGTH_SHORT).show();
            rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(
                    onSensorEvent,
                    rotationSensor,
                    1_000_000/Engine.ticksPerSecond());
        }else{
            Toast.makeText(context, "Failure! No useable sensors", Toast.LENGTH_SHORT).show();
        }


    }

    public static int ticksPerSecond() {
        return 20;
    }

    private static float[] graviRot = new float[16];
    static{

    }
    private SensorEventListener onSensorEvent = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor .equals( rotationSensor )) {
                float[] temp = Utils.scalar(-0.81f, Utils.normalize(event.values));
                gravity[0] = temp[0];
                gravity[1] = temp[1];
                gravity[2] = temp[2];
                gravity[3] = 1;

                Matrix.setIdentityM(graviRot,0);
                Matrix.rotateM(graviRot, 0, 90, 0, 0, 1);



                Matrix.rotateM(graviRot, 0, 45, 0, 1, 0);

               // Matrix.rotateM(graviRot, 0, camera.getLocation().getYaw(), 0,  0, 1);
                Matrix.scaleM(graviRot, 0, 8,8,8);

                Matrix.multiplyMV(gravity, 0, graviRot,0, gravity, 0);


            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    public static Engine instance() {
        return INSTANCE;
    }

    public void setLevel(Level level) {
        this.level = level;
        boolean wasRunning = running;
        if(running)
            stop();
        synchronized (obstToAdd){
            obstToAdd.clear();
        }
        synchronized (obstToRemove){
            obstToRemove.clear();
        }
        synchronized (obstacales){
            obstacales.clear();
        }
        synchronized (toAdd){
            toAdd.clear();
        }
        synchronized (toRemove){
            toRemove.clear();
        }
        synchronized (entities){
            entities.clear();
        }
        level.load();
        if(wasRunning)
            start();
    }

    public void onTick() {
        long time = System.currentTimeMillis();

//        {
//        float[] m = Utils.matrixStack.get();
//        Matrix.setIdentityM(m, 0);
//        Matrix.rotateM(m, 0, rotationSensor., 0, 0, 1);
//        Matrix.rotateM(m, 0, getPitch(), 0, 1, 0);
//        Matrix.rotateM(m, 0, getYaw(), 1, 0, 0);
//        Matrix.multiplyMM(gravity, 0, );
//        Utils.matrixStack.popMatrix();
//        }
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

        if(level!=null)
            level.onTick();

        Iterator<Entity> eIter = entities.iterator();
        while(eIter.hasNext()) {
            Entity e = eIter.next();
            if(e instanceof Ball){
                if(e.getLocation().getY() < -10){
                    e.getLocation().setPos(0,2,0);
                    e.setVelocity(0,0,0);
                }
            }
            e.onTick( this, time );
        }
        camera.getLocation().setPos(
              0,//  theBall.getLocation().getX(),
                -2,//
             0//   theBall.getLocation().getZ() + 4
        );
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
        }, 0, 1000/ticksPerSecond(), TimeUnit.MILLISECONDS);
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
                e.draw(camera);
            }
        }
        synchronized (obstacales){
            for(BaseObstacle o : obstacales){
                o.draw(camera);
            }
        }
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setTheBall(BasicBall theBall) {
        this.theBall = theBall;
    }
}
