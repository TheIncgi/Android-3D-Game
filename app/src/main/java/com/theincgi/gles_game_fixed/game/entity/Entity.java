package com.theincgi.gles_game_fixed.game.entity;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.geometry.AABB;
import com.theincgi.gles_game_fixed.geometry.ModelLoader2;
import com.theincgi.gles_game_fixed.geometry.ModelLoader3;
import com.theincgi.gles_game_fixed.render.Camera;
import com.theincgi.gles_game_fixed.utils.Location;

public class Entity implements ModelLoader3.DrawableModel {
    protected Location location;
    protected ModelLoader3.Model model;
    AABB aabb;

    protected float velocityX, velocityY, velocityZ;
    protected boolean onGround = false;
    //float baseMovementSpeed;

    public Entity(String modelName) {
        this.location = new Location();
        model = ModelLoader3.get(modelName);
    }
    public Entity(Location location, String modelName) {
        this.location = location;
        model = ModelLoader3.get(modelName);
    }

    public Location getLocation() {
        return location;
    }

    public void onTick(Engine e, long time ){

    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isOnGround() {
        return onGround;
    }

    @Override
    public void draw(float[] mvpm, Camera camera) {
        model.drawAll(mvpm, location);
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public float getVelocityZ() {
        return velocityZ;
    }

    public void setVelocity(float x, float y, float z) {
        velocityY = y;
        velocityX = x;
        velocityZ = z;
    }
    public void resetVelicty(){
        setVelocity(0,0,0);
    }
}
