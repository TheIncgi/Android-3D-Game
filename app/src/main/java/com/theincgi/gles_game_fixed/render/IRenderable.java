package com.theincgi.gles_game_fixed.render;

public interface IRenderable {
    public void draw(float[] mvpm);
    public void drawAsColor(float[] mvpm, float[] color);
}
