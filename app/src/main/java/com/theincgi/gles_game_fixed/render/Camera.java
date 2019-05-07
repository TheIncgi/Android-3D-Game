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
        this(x, y, z, 0, -45, 0 );
    }
    public Camera(float x, float y, float z, float yaw, float pitch, float roll) {
        location = new Location(x, y, z, yaw, pitch, roll);

    }


    public float[] getMatrix(){
        Matrix.setIdentityM(matrix, 0);

        Matrix.rotateM(matrix, 0, location.getRoll(),  0, 0, 1);
        Matrix.rotateM(matrix, 0, location.getPitch(), 1, 0, 0);
        Matrix.rotateM(matrix, 0, location.getYaw(),   0, 1, 0);

        Matrix.translateM(matrix, 0, location.getX(),location.getY(),location.getZ());
        return matrix;
    }

    public float[] getPos() {
        location.putPos( pos );
        return pos;
    }

    public Location getLocation() {
        return location;
    }
}
