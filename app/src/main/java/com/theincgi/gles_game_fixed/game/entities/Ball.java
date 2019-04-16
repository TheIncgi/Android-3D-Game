package com.theincgi.gles_game_fixed.game.entities;

import com.theincgi.gles_game_fixed.game.entity.Entity;

public class Ball extends Entity {
    public Ball() {
        super( "sphere" );
    }

    @Override
    public void onTick(long time) {
        getLocation().setY( (float)Math.sin(time/500.0) );
    }
}
