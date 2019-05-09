package com.theincgi.gles_game_fixed.game.levels;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entities.BasicBall;
import com.theincgi.gles_game_fixed.game.obstacles.Floor;
import com.theincgi.gles_game_fixed.game.obstacles.Goal;

public class Level1 extends Level {
    BasicBall theBall = new BasicBall();
    @Override
    public void load() {
        Engine.instance().addObstacale(new Floor(0,0,0));
        Engine.instance().addObstacale(new Floor(0,0,-2));
        Engine.instance().addObstacale(new Floor(0,0,-4));
        Engine.instance().addObstacale(new Floor(-2,0,-4));
        Engine.instance().addObstacale(new Floor(-4,0,-4));
        Engine.instance().addObstacale(new Floor(-4,0,-6));
        Engine.instance().addObstacale(new Floor(-4,0,-8));
        Engine.instance().addObstacale(new Floor(-4,0,-10));
        Engine.instance().addObstacale(new Floor(-2,0,-10));
        Engine.instance().addObstacale(new Floor(-0,0,-10));
        Engine.instance().addObstacale(new Floor(-0,0,-12));
        Engine.instance().addObstacale(new Goal(0,0,-14));

        Engine.instance().addEntity(theBall);
        Engine.instance().setTheBall(theBall);

        theBall.getLocation().setPos(0,3,0);
    }

    @Override
    public void onTick() {
        if(theBall.getLocation().getY() < -3){
            theBall.getLocation().setPos(0,2,0);
            theBall.resetVelicty();
        }
    }
}
