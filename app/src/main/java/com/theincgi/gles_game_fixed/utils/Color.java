package com.theincgi.gles_game_fixed.utils;

import static com.theincgi.gles_game_fixed.utils.Utils.clamp;
import static com.theincgi.gles_game_fixed.utils.Utils.inRange;

public class Color {

    /**RGBA*/
    public float[] color;
    public Color(float r, float g, float b){
        this(r,g,b,1.0f);
    }
    public Color(float r, float g, float b, float a){
        color = new float[]{r,g,b,a};
    }

    public void set(float r, float g, float b){
        set(r,g,b,1);
    }
    public void set(float r, float g, float b, float a){
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }

    public void setFromHSV(float hue, float sat, float val, float alpha){
        hue = hue<0?  360+(hue%360) : hue%360;
        float c = val*sat;
        float x = c*(1-Math.abs((hue/60)%2-1));
        float m = val-c;
        if(inRange(hue, 0, 60)){
            set(c +m, x+m, 0+m, alpha);
        }else if(inRange(hue, 60, 120)){
            set(x+m, c+m, 0+m, alpha);
        }else if(inRange(hue, 120, 180)){
            set(0+m, c+m, x+m, alpha);
        }else if(inRange(hue, 180, 240)){
            set(0+m, x+m, c+m, alpha);
        }else if(inRange(hue, 240, 300)){
            set(x+m, 0+m, c+m, alpha);
        }else if(inRange(hue, 300, 360)){
            set(c+m, 0+m, x+m, alpha);
        }else
            throw new RuntimeException(String.format("Color HSV<%.3f, %.3f, %.3f> had an unsupported case", hue,sat, val));

    }
    public float[] toHSV(){
        float r=color[0], g=color[1], b=color[2];
        float max = Utils.max(r,g,b);
        float min = Utils.min(r,g,b);
        float delta = max-min;
        float hue=0, sat=0, val=0;
        if(delta == 0){
            hue = 0;
        }else if(max==r){
            hue = 60*(((g-b)/delta)%6);
        }else if(max==g){
            hue = 60*((b-r)/delta +2);
        }else{
            hue = 60*((r-g)/delta +4);
        }

        sat = max==0? 0 : delta/max;
        val = max;
        return new float[]{hue, sat, val, color[3]};
    }
    public void adjustHSV(float hueDelta, float saturationDelta, float valueDelta){ adjustHSV(hueDelta, saturationDelta, valueDelta, 0);}
    public void adjustHSV(float hueDelta, float saturationDelta,  float valueDelta, float alphaDelta){
        float r=color[0], g=color[1], b=color[2], alpha=color[3];
        float max = Utils.max(r,g,b);
        float min = Utils.min(r,g,b);
        float delta = max-min;
        float hue=0, sat=0, val=0;
        if(delta == 0){
            hue = 0;
        }else if(max==r){
            hue = 60*(((g-b)/delta)%6);
        }else if(max==g){
            hue = 60*((b-r)/delta +2);
        }else{
            hue = 60*((r-g)/delta +4);
        }

        sat = max==0? 0 : delta/max;
        val = max;


        hue += hueDelta;
        sat += saturationDelta;
        val += valueDelta;
        alpha += alphaDelta;
        sat = clamp(sat, 0,1);
        val = clamp(val, 0,1);
        alpha = clamp(alpha, 0, 1);
        color[0] = hue;
        color[1] = sat;
        setFromHSV(hue, sat, val, alpha);
    }
    public float[] array(){
        return color;
    }
}
