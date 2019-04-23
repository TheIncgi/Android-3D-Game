package com.theincgi.gles_game_fixed.game.obstacles;

import com.theincgi.gles_game_fixed.game.entity.Entity;
import com.theincgi.gles_game_fixed.geometry.ModelLoader3;
import com.theincgi.gles_game_fixed.render.Camera;
import com.theincgi.gles_game_fixed.utils.Location;

public class BaseObstacle implements ModelLoader3.DrawableModel{
    ModelLoader3.Model model;
    protected Location location;
    protected float[] normal = {0,1,0,  1};
    public BaseObstacle() {
        location = new Location(0,0,0);
    }
    public BaseObstacle(float x, float y, float z){
        location = new Location(x,y,z);
    }

    public boolean intersectsSurface( Entity e ){
        return false;
    }

    @Override
    public void draw(float[] mvpm, Camera camera) {
        model.drawAll(mvpm, location);
    }

    public float[] getNormal( Entity e ){
        return normal;
    }

    public float yLimit(){
        return location.getY();
    }
}
