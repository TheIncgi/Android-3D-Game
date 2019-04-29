package com.theincgi.gles_game_fixed.game.obstacles;

import android.opengl.Matrix;

public class AngledFloor extends Floor {

    public AngledFloor(float x, float y, float z) {
        super(x, y, z);
    }
    public AngledFloor setAngle( float yaw, float pitch, float roll ){
        location.setRotation(yaw, pitch, roll); //also affects rendering angles
        float[] transform = new float[16];
        Matrix.setIdentityM(transform, 0);
        Matrix.rotateM(transform, 0, roll,  0, 0, 1);
        Matrix.rotateM(transform, 0, pitch, 1, 0, 0);
        Matrix.rotateM(transform, 0, yaw,   0, 1, 0);

        float[] normal = {0,1,0,1}; //up + 1 for translations
        Matrix.multiplyMV(this.normal, 0, transform,0, normal, 0);
        return this;
    }


}
