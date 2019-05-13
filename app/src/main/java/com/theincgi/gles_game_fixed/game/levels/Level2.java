package com.theincgi.gles_game_fixed.game.levels;

import android.content.Context;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entities.BasicBall;
import com.theincgi.gles_game_fixed.game.obstacles.Floor;
import com.theincgi.gles_game_fixed.game.obstacles.Goal;
import com.theincgi.gles_game_fixed.game.obstacles.Hill;

public class Level2 extends  Level1 {
    public Level2(Context context) {
        super(2, context);
    }

    @Override
    public void load() {
        theBall = new BasicBall();
        Engine.instance().addObstacale( new Floor(0, 0, 0) );
        Engine.instance().addObstacale( new Floor(0, 0, -2) );
        Engine.instance().addObstacale( new Floor(0, 0, -4) );
        Engine.instance().addObstacale( new Floor(2, 0, -4) );
        Engine.instance().addObstacale( new Hill(5, 0, -4));
        Engine.instance().addObstacale( new Floor(8, 0, -4));
        Engine.instance().addObstacale( new Floor(10, 0, -4) );
        Engine.instance().addObstacale( new Floor(10,0,-6));
        Engine.instance().addObstacale( new Floor(10,0,-8));
        Engine.instance().addObstacale( new Floor(8,0,-8));
        Engine.instance().addObstacale( new Floor(6,0,-8));
        Engine.instance().addObstacale( new Floor(4,0,-8));
        Engine.instance().addObstacale( new Floor(2,0,-8));
        Engine.instance().addObstacale( new Floor(0,0,-8));
        Engine.instance().addObstacale( new Floor(-2,0,-8));
        Engine.instance().addObstacale( new Floor(-4,0,-8));
        Engine.instance().addObstacale( new Floor(-6,0,-8));
        Engine.instance().addObstacale( new Floor(-6,0,-6));
        Engine.instance().addObstacale( new Floor(-6,0,-4));
        Engine.instance().addObstacale(goal = new Goal(-8, 0, -4));
        Engine.instance().addEntity(theBall);
    }
}
