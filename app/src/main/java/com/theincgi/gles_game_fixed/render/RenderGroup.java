package com.theincgi.gles_game_fixed.render;


import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

import java.util.ArrayList;

public class RenderGroup extends ArrayList<IRenderable> implements IRenderable {
    ArrayList<IRenderable> children;
    Location offset = new Location(0,0,0);
    private float[] matrix = new float[16];
    public RenderGroup() {
        super();
    }
    public RenderGroup(IRenderable... renderables) {
        super(renderables.length);
        for(int i = 0; i<renderables.length; i++)
            add(renderables[i]);
    }



    @Override
    public void draw(float[] mvpm) {
        Utils.matrixStack.pushMatrix();
        //TODO offset.applyToStack();
        //Matrix.multiplyMM(matrix, 0, mvpm, 0, offset.getMatrix(), 0);
        synchronized (children){
            for (IRenderable r:children) {
                r.draw(matrix);
            }
        }
        Utils.matrixStack.popMatrix();
    }

    @Override
    public void drawAsColor(float[] mvpm, float[] color) {

    }
}
