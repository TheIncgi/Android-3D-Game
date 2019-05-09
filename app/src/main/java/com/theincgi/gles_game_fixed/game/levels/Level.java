package com.theincgi.gles_game_fixed.game.levels;

import android.content.Context;

import com.theincgi.gles_game_fixed.game.Engine;

public abstract class Level {
    int ticks = 0;
    private static int idCounter = 0;
    protected Context context;
    public static final int ID = idCounter++;
    abstract public void load();
    abstract public void onTick();

    public float getTime(){
        return ticks/(float)Engine.ticksPerSecond();
    }
}
