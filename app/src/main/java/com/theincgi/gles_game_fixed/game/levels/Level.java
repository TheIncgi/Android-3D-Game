package com.theincgi.gles_game_fixed.game.levels;

import com.theincgi.gles_game_fixed.game.Engine;

public abstract class Level {
    private static int idCounter = 0;
    public static final int ID = idCounter++;
    abstract public void load();
    abstract public void onTick();
}
