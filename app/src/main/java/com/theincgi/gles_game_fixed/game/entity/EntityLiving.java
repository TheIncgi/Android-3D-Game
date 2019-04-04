package com.theincgi.gles_game_fixed.game.entity;

import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

public class EntityLiving extends Entity {
    float maxHealth = 100;
    float health = maxHealth;

    public EntityLiving(String modelName) {
        super(modelName);
    }

    public EntityLiving(Location location, String modelName) {
        super(location, modelName);
    }

    public void setMaxHealth(float maxHealth){
        float delta = maxHealth / this.maxHealth;
        this.maxHealth = Math.max(0, maxHealth);
        setHealth( health * delta );
    }

    public void setHealth( float health ){
        health = Utils.clamp(health, 0, maxHealth);
        if(health == 0)
            onDeath();
    }

    public void onDeath(){

    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getHealth() {
        return health;
    }
}
