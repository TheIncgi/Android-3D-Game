package com.theincgi.gles_game_fixed.render;

import com.theincgi.gles_game_fixed.utils.Color;

public class LightSource {
    float[] pos;
    float[] color;
    float brightness = 1;

    public LightSource(float x, float y, float z){
        this(x,y,z, 1,1,1);
    }
    public LightSource(float x, float y, float z, float r, float g, float b) {
        pos = new float[]{x, y, z};
        color = new float[]{r, g, b};
    }
    public float[] getPos() {
        return pos;
    }
    public float[] getColor(){
        return color;
    }

    public float getBrightness() {
        return brightness;
    }
}
