package com.theincgi.gles_game_fixed.game.levels;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entities.Ball;
import com.theincgi.gles_game_fixed.game.obstacles.AngledFloor;

public class TestLevel extends Level{
    @Override
    public void load() {
        Engine.instance().addEntity( new Ball() );
        Engine.instance().addObstacale(
                new AngledFloor(0f, 0f, 0f).setAngle(0,0,3)
        );Engine.instance().addObstacale(
                new AngledFloor(8f, 0f, 0f).setAngle(0,0,0)
        );Engine.instance().addObstacale(
                new AngledFloor(0f, 0f, 8f).setAngle(0,0,0)
        );Engine.instance().addObstacale(
                new AngledFloor(-8f, 0f, 0f).setAngle(0,0,0)
        );Engine.instance().addObstacale(
                new AngledFloor(0f, 0f, -8f).setAngle(0,0,0)
        );

        Engine.instance().addObstacale(
                new AngledFloor(-2f, -.5f, 0f).setAngle(0,10,0)
        );Engine.instance().addObstacale(
                new AngledFloor(-2f, -.5f, 1f).setAngle(0,-10,0)
        );
    }
}
