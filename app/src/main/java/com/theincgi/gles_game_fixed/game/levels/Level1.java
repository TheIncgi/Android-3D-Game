package com.theincgi.gles_game_fixed.game.levels;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.theincgi.gles_game_fixed.Levelselector;
import com.theincgi.gles_game_fixed.MainActivity;
import com.theincgi.gles_game_fixed.MazeDatabase;
import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entities.BasicBall;
import com.theincgi.gles_game_fixed.game.obstacles.Floor;
import com.theincgi.gles_game_fixed.game.obstacles.Goal;

public class Level1 extends Level {
    BasicBall theBall = new BasicBall();
    Goal goal;
    private MazeDatabase db;

    public Level1(Context context) {
        this.context = context;
    }

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
        Engine.instance().addObstacale(goal = new Goal(0,0,-14));

        Engine.instance().addEntity(theBall);
        Engine.instance().setTheBall(theBall);

        theBall.getLocation().setPos(0,3,0);
    }

    @Override
    public void onTick() {
        super.onTick();
        if(theBall != null && theBall.getLocation().getY() < -3){
            theBall.getLocation().setPos(0,2,0);
            theBall.resetVelicty();

        }
        if(goal!=null && theBall!=null && goal.pointOver(theBall.getLocation())){
            //TODO on level done
            MazeDatabase db = new MazeDatabase(context);
            //level1 vs level2?
            db.saveScore(1, String.valueOf(getTime()));
            MainActivity.onFinish();
            Levelselector.toast("Time: "+ticks);
        }
    }
}
