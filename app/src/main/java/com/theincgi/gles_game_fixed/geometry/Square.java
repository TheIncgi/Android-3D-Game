package com.theincgi.gles_game_fixed.geometry;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;


import com.theincgi.gles_game_fixed.render.GLProgram;
import com.theincgi.gles_game_fixed.render.GLPrograms;
import com.theincgi.gles_game_fixed.render.IRenderable;
import com.theincgi.gles_game_fixed.utils.Color;
import com.theincgi.gles_game_fixed.utils.GLErrorLogger;
import com.theincgi.gles_game_fixed.utils.Utils;

import java.nio.FloatBuffer;

import java.nio.ShortBuffer;


public class Square implements IRenderable {

    private FloatBuffer vertexBuffer;
    private ShortBuffer indiceBuffer;
    GLProgram prgm = GLPrograms.getDefault();
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    float[] vertices = {
            0.5f,  0.5f, 0.0f,  // top right
            0.5f, -0.5f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f,  // bottom left
            -0.5f,  0.5f, 0.0f   // top left
    };
    short[] indices = {  // note that we start from 0!
            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle
    };

    // Set color with red, green, blue and alpha (opacity) values
   // float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
    public Color color = new Color(0.63671875f, 0.76953125f, 0.22265625f);
    int attrib_location, attrib_texture, attrib_normal;
    public Square() {
        vertexBuffer = Utils.toBuffer(vertices);
        indiceBuffer = Utils.toBuffer(indices);

        attrib_location = prgm.getAttribLocation("vPosition");

    }

    private final int vertexCount = vertices.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    public void draw(float[] mvpm) {

        prgm.use();
        GLErrorLogger.check();

    }

    @Override
    public void drawAsColor(float[] mvpm, float[] color) {

    }
}
