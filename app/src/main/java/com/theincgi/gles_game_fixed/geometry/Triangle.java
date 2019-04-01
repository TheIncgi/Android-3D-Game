package com.theincgi.gles_game_fixed.geometry;

import android.opengl.GLES20;

import com.theincgi.gles_game_fixed.render.GLProgram;
import com.theincgi.gles_game_fixed.render.GLPrograms;
import com.theincgi.gles_game_fixed.render.IRenderable;
import com.theincgi.gles_game_fixed.utils.Color;
import com.theincgi.gles_game_fixed.utils.Utils;

import java.nio.FloatBuffer;

public class Triangle implements IRenderable {

    private FloatBuffer vertexBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {   // in counterclockwise order:
            0.0f,  0.622008459f, 0.0f, // top
           -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };

    // Set color with red, green, blue and alpha (opacity) values
   // float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
    public Color color = new Color(0.63671875f, 0.76953125f, 0.22265625f);

    public Triangle() {
        vertexBuffer = Utils.toBuffer(triangleCoords);
    }

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    public void draw(float[] mvpm) {
        GLProgram prgm = GLPrograms.getDefault();
        prgm.use();
        int posH = prgm.getAttribLocation("vPosition"); //positionHandle
        GLES20.glEnableVertexAttribArray(posH);
        GLES20.glVertexAttribPointer(
                posH,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer);
        int colorH = prgm.getUniformAttribLocation("vColor");
        int mvpmH  = prgm.getUniformLocation("mvpm");
        GLES20.glUniform4fv(colorH, 1, color.array(), 0);
        GLES20.glUniformMatrix4fv(mvpmH, 1, false, mvpm, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(posH);
    }

    @Override
    public void drawAsColor(float[] mvpm, float[] color) {

    }
}
