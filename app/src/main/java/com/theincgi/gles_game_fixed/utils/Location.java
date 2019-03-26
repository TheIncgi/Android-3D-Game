package com.theincgi.gles_game_fixed.utils;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.renderscript.Matrix4f;

public class Location {
    protected float x,y,z,yaw,pitch,roll;
    float[] matrix;
    public Location() {
        this(0,0,0);
    }

    public Location(float x, float y, float z) {
        this(x,y,z,0,0,0);
    }

    public Location(float x, float y, float z, float yaw, float pitch, float roll) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        matrix = new float[16];
    }

    public void move(float dx, float dy, float dz){
        this.x += dx;
        this.y += dy;
        this.z += dz;
    }
    public void rotate(float dYaw, float dPitch, float dRoll){
        this.yaw +=dYaw;
        this.pitch += dPitch;
        this.roll += dRoll;
    }

    public void setPos(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;

    }
    public void setRotation(float yaw, float pitch, float roll){
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getRoll() {
        return roll;
    }

    public float[] getMatrix(){
        Matrix.setIdentityM(matrix, 0);

        Matrix.rotateM(matrix, 0, getRoll(),  0, 0, 1);
        Matrix.rotateM(matrix, 0, getPitch(), 1, 0, 0);
        Matrix.rotateM(matrix, 0, getYaw(),   0, 1, 0);


        Matrix.translateM(matrix, 0, getX(),getY(),getZ());

        return matrix;
    }

    public void applyToStack(){
        float[] matrix = Utils.matrixStack.get();
        applyToMatrix(matrix);
    }
    public void applyToMatrix(float[] matrix){

        Matrix.rotateM(matrix, 0, getRoll(),  0, 0, 1);
        Matrix.rotateM(matrix, 0, getPitch(), 0, 1, 0);
        Matrix.rotateM(matrix, 0, getYaw(),   1, 0, 0);


        Matrix.translateM(matrix, 0, getX(),getY(),getZ());
    }

    private float interpolate(float a, float b, float f){
        return a*(1-f) + b*f;
    }
    public void interpolate(Location result, Location a, Location b, float factor){
        result.x = interpolate(a.x, b.x, factor);
        result.y = interpolate(a.y, b.y, factor);
        result.z = interpolate(a.z, b.z, factor);
        result.yaw = interpolate(a.yaw, b.yaw, factor);
        result.pitch = interpolate(a.pitch, b.pitch, factor);
        result.roll = interpolate(a.roll, b.roll, factor);
    }

    @Override
    public Location clone(){
        return new Location(x, y, z, yaw, pitch, roll);
    }
    public void cloneTo(Location location){
        location.x = x;
        location.y = y;
        location.z = z;
        location.yaw = yaw;
        location.pitch = pitch;
        location.roll = roll;
    }
}
