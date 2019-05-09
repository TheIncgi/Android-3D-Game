package com.theincgi.gles_game_fixed.game.entities;

import android.opengl.Matrix;
import android.util.Log;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entity.Entity;
import com.theincgi.gles_game_fixed.game.obstacles.BaseObstacle;
import com.theincgi.gles_game_fixed.render.Camera;
import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

import java.util.Iterator;

public class BasicBall extends Entity implements ISphere {
    float[] orient = new float[16];

    public BasicBall() {
        super("sphere");
        Matrix.setIdentityM(orient, 0);
        location = new Location() {
            @Override
            public void applyToMatrix(float[] matrix) {
                //FIXME rolling needs fixing
                //Matrix.multiplyMM(Utils.matrixStack.get(), 0, orient, 0, Utils.matrixStack.get(), 0);
                //Matrix.multiplyMM(Utils.matrixStack.get(), 0, Utils.matrixStack.get(), 0, orient, 0);
                Matrix.translateM(matrix, 0, getX(),getY(),getZ());
                //Matrix.multiplyMM(matrix, 0, orient, 0, matrix, 0);
                Matrix.multiplyMM(matrix, 0,matrix,0,orient,0);
                //Matrix.translateM(matrix, 0, getX(), getY(), getZ());
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
        while (iterator.hasNext()) {
            BaseObstacle obst = iterator.next();
            float[] near = obst.intersectsSurface(this);
            if (obst.pointOver(location)) {
                velocityY = 0;
                float yOffset = obst.yOffset(this.getLocation());
                this.getLocation().setY(yOffset + getRadius());
                Log.i("####", "onTick: "+yOffset);
                float[] force = obst.force(this.location);
                velocityX+=force[0];
                velocityZ+=force[2];
                break;
            }
        }
        location.move(velocityX, velocityY, velocityZ);


        //ROLLING CODE
        float speed = Utils.distance(0,0,0, velocityX, 0, velocityZ);
        float volume = Utils.clamp(Utils.map(speed, 0, 2/**speed for max vol*/, 0, 1), 0, 1);
        if (velocityX != 0 || velocityZ != 0) {
            float rollAngle = (float) Math.toDegrees(Math.atan2(velocityZ, velocityX));
            rollAngle += 90; //axis of rotation
            rollAngle = (float) Math.toRadians(rollAngle);
            float ballCirc = (float) (2 * Math.PI * getRadius());
            float rollAmount = 360 * Utils.magnitude(new float[]{velocityX, 0, velocityZ}) / ballCirc;
            Matrix.rotateM(orient, 0, rollAmount, (float) Math.cos(rollAngle), 0, (float) -Math.sin(rollAmount));
        }
        //Matrix.rotateM(rotationMatrix, 0, 3, 1,0,1);
        //location.setPitch(location.getPitch()+1);


    }

    @Override
    public float getRadius() {
        return 1;
    }

}
