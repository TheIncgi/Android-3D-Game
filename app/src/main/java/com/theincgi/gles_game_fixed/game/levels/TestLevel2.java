package com.theincgi.gles_game_fixed.game.levels;

import android.content.Context;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entities.BasicBall;
import com.theincgi.gles_game_fixed.game.obstacles.Floor;
import com.theincgi.gles_game_fixed.game.obstacles.Hill;

public class TestLevel2 extends Level{
    BasicBall theBall = new BasicBall();

    public TestLevel2(Context context) {
        super(2);
        this.context = context;
    }

    @Override
    public void load() {
        int r = 1; //emulator cant handel higher numbers without gpu accel
        Engine.instance().addEntity(theBall);
        Engine.instance().setTheBall(theBall);
        theBall.getLocation().setPos(0,3,0);
        for(int i = -r; i<=r; i++){
            for(int j = -r; j<=r; j++){
                Engine.instance().addObstacale(new Floor(i*2, 0, j*2));
            }
        }
        Engine.instance().addObstacale(new Hill(5, 0, 0));
    }

    @Override
    public void onTick() {
        if(theBall.getLocation().getY() < -3){
            theBall.getLocation().setPos(0,2,0);
            theBall.resetVelicty();
        }
    }
}
