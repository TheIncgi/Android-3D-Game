package com.theincgi.gles_game_fixed.game.entities;

import android.opengl.Matrix;
import android.util.Log;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entity.Entity;
import com.theincgi.gles_game_fixed.game.obstacles.BaseObstacle;
import com.theincgi.gles_game_fixed.render.Camera;
import com.theincgi.gles_game_fixed.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class RigidBodyBall extends Entity implements ISphere {
    float mass;
    final float momentOfInert;
    final float radius = 1;
    float[] angularVelocity = new float[]{0,0,0};
    float[] orientation = new float[16]; //rotation matrix
    ArrayList<float[]> collisions = new ArrayList<>();
    public RigidBodyBall( float mass){
        super("sphere");
        this.mass = mass;
        momentOfInert = (2f/5)*mass * radius*radius;
        Matrix.setIdentityM(orientation,0);
    }

    //https://www.toptal.com/game/video-game-physics-part-i-an-introduction-to-rigid-body-dynamics
    @Override
    public void onTick(Engine e, long time) {
        //phase 1: Apply forces (Calculate)
        float dt = 1f/e.ticksPerSecond();
        float[] force = computeForce( e );
        float[] accel = new float[]{
                force[0] / mass,
                force[1] / mass,
                force[2] / mass
        };
        float[] torque = computeTorque();

        //Phase 2: Update object
        if(collisions.size()>0){
            velocityX=0;
            velocityY=0;
            velocityZ=0;
        }
        velocityX += accel[0] * dt;
        velocityY += accel[1] * dt;
        velocityZ += accel[2] * dt;

        angularVelocity[0]+=torque[0];  //TODO check me
        angularVelocity[1]+=torque[1];
        angularVelocity[2]+=torque[2];

        Matrix.rotateM(orientation, 0, Utils.magnitude(angularVelocity), angularVelocity[0],angularVelocity[1],angularVelocity[2]);
        location.move(velocityX, velocityY, velocityZ);

        //Phase 3: update collisions
        updateCollisions(e);
        //Phase 4: solve constraints
        //TODO
    }



    private void updateCollisions(Engine e){
        collisions.clear();
        Iterator<BaseObstacle> itter = e.getObstacleItterator();
        while(itter.hasNext()){
            BaseObstacle ob = itter.next();
            float[] intersection = ob.intersectsSurface( this );
            //Log.i("INTERSECT", "updateCollisions: "+ Arrays.toString(intersection));
            if(intersection!=null)
                collisions.add(Utils.subv(intersection, location));
        }
    }

    private float[] force = new float[3];
    private float[] computeForce(Engine e){
       force[0] = mass * e.gravity[0];
       force[1] = mass * e.gravity[1];
       force[2] = mass * e.gravity[2];
       float[] mg = force.clone();

        //Log.i("PHYSICS", "computeForce: "+collisions.size());
       for(float[] fVec : collisions){
           float[] normalForce = Utils.scalar(Utils.dotProduct(e.gravity, fVec), mg);
            Utils.add(force,  force, normalForce);

       }
       return force;
    }

    private float[] computeTorque(){
        return new float[]{0,0,0}; //need to get forces from collisions
    }
    private float[] computeTorque(float[] force, float[] position){
        float[] r = Utils.subv( location, force );
        Utils.crossProduct(r,  r, force);
        return r;
    }


    @Override
    public void draw(float[] mvpm, Camera camera) {
        Utils.matrixStack.pushMatrix();

        super.draw(mvpm, camera);
        Utils.matrixStack.popMatrix();
    }

    public float getRadius() {
        return radius;
    }
}
