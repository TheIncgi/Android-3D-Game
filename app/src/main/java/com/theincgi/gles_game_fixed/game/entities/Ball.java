package com.theincgi.gles_game_fixed.game.entities;

import android.opengl.Matrix;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entity.Entity;
import com.theincgi.gles_game_fixed.game.obstacles.BaseObstacle;
import com.theincgi.gles_game_fixed.render.Camera;
import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;

public class Ball extends Entity {
    float radius = 1;
    //used so rotation can accumulate instead of using just plain yawPitchRoll
    float[] rotationMatrix = new float[16];
    public Ball() {
        super( "sphere" );
        location = new Location(0,15,0);
        Matrix.setIdentityM(rotationMatrix,0);
    }

    @Override
    public void onTick(Engine e, long time) {


        ArrayList<BaseObstacle> colliding = new ArrayList();
        Iterator<BaseObstacle> iterator = e.getObstacleItterator();
        float[] avgReflect = new float[4]; avgReflect[3] = 1;
        while(iterator.hasNext()){
            BaseObstacle obst = iterator.next();
            boolean intersects = obst.intersectsSurface( this );
            if( intersects ) {
                colliding.add(obst);
                float[] reflection = Utils.reflect(new float[]{velocityX, velocityY, velocityZ}, obst.getNormal(this));
                for(int i = 0; i<3; i++)
                    avgReflect[i] += reflection[i];  //TODO handle reversed normals
            }
        }
        Utils.scalar(1f/colliding.size(), avgReflect);
        float speed = Utils.distance(0,0,0,velocityX, velocityY,velocityZ);
        if(colliding.size()>0){

            if(speed > Math.abs(gravity)){
                //TODO bounce ball in reflection angles
                //TODO move ball to height based on 'progress' passing past intersecting point
                Utils.scalar(speed * .33f, avgReflect);
                velocityX = avgReflect[0];
                velocityY = avgReflect[1];
                velocityZ = avgReflect[2];

                onGround = false;

            }else{
                onGround = true;
            }
        }else{
            onGround = false;
        }
        if(!onGround){
            velocityY += gravity;
        }else{
            //rolling code needed

            velocityY = 0;
        }

        location.move(velocityX,velocityY,velocityZ);
        if(velocityX != 0 || velocityZ != 0) {
//            float rollAngle = (float) Math.toDegrees(Math.atan2(-velocityZ, velocityX));
//            rollAngle += 90; //axis of rotation
//            rollAngle = (float)Math.toRadians(rollAngle);
//            float ballCirc = (float)(2*Math.PI*radius);
//            float rollAmount = 360 * speed / ballCirc;
//            Matrix.rotateM(rotationMatrix,0, rollAmount, (float)Math.cos(rollAngle), 0, (float)Math.sin(rollAmount));
        }
        //Matrix.rotateM(rotationMatrix, 0, 3, 1,0,1);
        //location.setPitch(location.getPitch()+1);
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void draw(float[] mvpm, Camera camera) {
        Utils.matrixStack.pushMatrix();
        //Matrix.multiplyMM(Utils.matrixStack.get(), 0, Utils.matrixStack.get(), 0, rotationMatrix, 0);
        Matrix.rotateM(Utils.matrixStack.get(), 0, 45, 1,1,1);
        super.draw(mvpm, camera);
        Utils.matrixStack.popMatrix();
    }
}
