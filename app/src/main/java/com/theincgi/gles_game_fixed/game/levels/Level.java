package com.theincgi.gles_game_fixed.game.levels;

import android.content.Context;

import com.theincgi.gles_game_fixed.game.Engine;

public abstract class Level {
    int ticks = 0;
    public final int LELEL_NUMBER;
    private static int idCounter = 0;
    protected Context context;
    public static final int ID = idCounter++;
    abstract public void load();

    public Level(int lvlNum) {
        this.LELEL_NUMBER = lvlNum;
    }

    public void onTick(){
        ticks++;
    }

    public float getTime(){
        return ticks/(float)Engine.ticksPerSecond();
    }


    public interface MakeLevel{
        public Level make();
    }
}
