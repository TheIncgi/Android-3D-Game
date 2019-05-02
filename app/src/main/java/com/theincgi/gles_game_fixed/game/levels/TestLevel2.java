package com.theincgi.gles_game_fixed.game.levels;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entities.BasicBall;
import com.theincgi.gles_game_fixed.game.obstacles.Floor;

public class TestLevel2 extends Level{
    BasicBall theBall = new BasicBall();
    @Override
    public void load() {
        int r = 1;
        Engine.instance().addEntity(theBall);
        theBall.getLocation().setPos(0,3,0);
        for(int i = -r; i<=r; i++){
            for(int j = -r; j<=r; j++){
                Engine.instance().addObstacale(new Floor(i*2, 0, j*2));
            }
        }
    }

    @Override
    public void onTick() {
        if(theBall.getLocation().getY() < -3){
            theBall.getLocation().setPos(0,2,0);
            theBall.resetVelicty();
        }
    }
}
