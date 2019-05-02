package com.theincgi.gles_game_fixed.game.entities;

import android.opengl.Matrix;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entity.Entity;
import com.theincgi.gles_game_fixed.game.obstacles.BaseObstacle;
import com.theincgi.gles_game_fixed.render.Camera;
import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

import java.util.Iterator;

public class BasicBall extends Entity implements ISphere{
    float[] orient = new float[16];
    public BasicBall(){
        super("sphere");
        Matrix.setIdentityM(orient, 0);
        location = new Location(){
            @Override
            public void applyToMatrix(float[] matrix) {
                //FIXME rolling needs fixing
                super.applyToMatrix(matrix);
                //Matrix.multiplyMM(Utils.matrixStack.get(), 0, orient, 0, Utils.matrixStack.get(), 0);
            }
        };
    }


    @Override
    public void onTick(Engine e, long time) {
        super.onTick(e, time);
        float[] g = Engine.instance().gravity;
        //g = Utils.scalar( .4f, g);
        velocityX += Engine.instance().gravity[0] * .02;
        velocityZ += Engine.instance().gravity[2] * .02;
        velocityX *= .9;
        velocityZ *= .9;
        velocityY += -.2f;
        Iterator<BaseObstacle> iterator = e.getObstacleItterator();
        while(iterator.hasNext()){
            BaseObstacle obst = iterator.next();
            float[] near = obst.intersectsSurface(this);
            if( obst.pointOver( location ) ){
                velocityY = 0;
                break;
            }
        }
        location.move(velocityX, velocityY, velocityZ);


        if(velocityX != 0 || velocityZ != 0) {
            float rollAngle = (float) Math.toDegrees(Math.atan2(-velocityZ, velocityX));
            rollAngle += 90; //axis of rotation
            rollAngle = (float)Math.toRadians(rollAngle);
            float ballCirc = (float)(2*Math.PI*getRadius());
            float rollAmount = 360 * Utils.magnitude(new float[]{velocityX,velocityY,velocityZ}) / ballCirc;
            Matrix.rotateM(orient,0, rollAmount, (float)Math.cos(rollAngle), 0, (float)Math.sin(rollAmount));
        }
        //Matrix.rotateM(rotationMatrix, 0, 3, 1,0,1);
        //location.setPitch(location.getPitch()+1);



    }

    @Override
    public float getRadius() {
        return 1;
    }

    @Override
    public void draw(float[] mvpm, Camera camera) {
        Utils.matrixStack.pushMatrix();
//
        super.draw(mvpm, camera);
        Utils.matrixStack.popMatrix();
    }
}
