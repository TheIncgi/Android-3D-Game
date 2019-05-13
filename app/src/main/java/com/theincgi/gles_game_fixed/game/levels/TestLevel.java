package com.theincgi.gles_game_fixed.game.levels;

import android.content.Context;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entities.Ball;
import com.theincgi.gles_game_fixed.game.entities.BasicBall;
import com.theincgi.gles_game_fixed.game.entities.RigidBodyBall;
import com.theincgi.gles_game_fixed.game.obstacles.AngledFloor;

public class TestLevel extends Level{
    //RigidBodyBall theBall = new RigidBodyBall(1);
    BasicBall theBall = new BasicBall();

    public TestLevel(Context context) {
        super(1);
        this.context = context;
    }

    @Override
    public void load() {
        Engine.instance().addEntity( theBall );
        theBall.getLocation().setPos(0, 1.2f, 0);
        Engine.instance().addObstacale(
                new AngledFloor(0f, 0f, 0f).setAngle(0,0,45)
        );Engine.instance().addObstacale(
                new AngledFloor(9f, 0f, 0f).setAngle(0,0,0)
        );Engine.instance().addObstacale(
                new AngledFloor(0f, 0f, 9f).setAngle(0,0,0)
        );Engine.instance().addObstacale(
                new AngledFloor(-9f, 0f, 0f).setAngle(0,0,0)
        );Engine.instance().addObstacale(
                new AngledFloor(0f, 0f, -9f).setAngle(0,0,0)
        );

        Engine.instance().addObstacale(
                new AngledFloor(-2f, -4f, -1f).setAngle(0,10,0)
        );Engine.instance().addObstacale(
                new AngledFloor(-2f, -8f, 1f).setAngle(0,-10,0)
        );
    }


    @Override
    public void onTick() {
        if(theBall.getLocation().getY() < -10){
            theBall.getLocation().setPos(0,1.2f,0);
            theBall.setVelocity(0,0,0);
        }
    }
}
