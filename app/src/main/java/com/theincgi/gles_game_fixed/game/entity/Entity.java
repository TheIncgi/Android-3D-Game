package com.theincgi.gles_game_fixed.game.entity;

import com.theincgi.gles_game_fixed.geometry.AABB;
import com.theincgi.gles_game_fixed.geometry.ModelLoader2;
import com.theincgi.gles_game_fixed.geometry.ModelLoader3;
import com.theincgi.gles_game_fixed.utils.Location;

public class Entity implements ModelLoader2.DrawableModel {
    Location location;
    ModelLoader3.Model model;
    AABB aabb;

    float baseMovementSpeed;

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

    public void onTick( long time ){}



    @Override
    public void draw(float[] mvpm) {
        model.drawAll(mvpm, location);
    }


}
