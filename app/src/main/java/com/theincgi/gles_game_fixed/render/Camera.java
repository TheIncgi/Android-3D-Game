package com.theincgi.gles_game_fixed.render;

import android.opengl.Matrix;

import com.theincgi.gles_game_fixed.utils.Location;


public class Camera {
    Location location;
    float[] matrix = new float[16];
    float[] pos = new float[3];
    public Camera() {
        this(0,0,0);
    }
    public Camera(float x, float y, float z) {
        this(x, y, z, 0, 0,0 );
    }
    public Camera(float x, float y, float z, float yaw, float pitch, float roll) {
        location = new Location(x, y, z, yaw, pitch, roll);

    }


    public float[] getMatrix(){
//        Matrix.setIdentityM(matrix,  0);
////        location.applyToMatrix(matrix);
////        return matrix;
        return location.getMatrix();
    }

    public float[] getPos() {
        location.putPos( pos );
        return pos;
    }
}
