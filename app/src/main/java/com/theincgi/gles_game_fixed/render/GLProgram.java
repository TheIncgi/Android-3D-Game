package com.theincgi.gles_game_fixed.render;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.theincgi.gles_game_fixed.utils.GLErrorLogger;

import java.io.IOException;
import java.io.InputStream;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;

public class GLProgram{
    private static final String TAG = "#GLProgram";
    /*
    * Vertex shader transforms vertex coords*/

    private int program;

    public GLProgram(Context context, int vertex_id, int fragment_id) {
        int vShader = loadShader( context, GL_VERTEX_SHADER, vertex_id);
        int fShader = loadShader( context, GL_FRAGMENT_SHADER, fragment_id );
        GLErrorLogger.check();

        program = GLES20.glCreateProgram();
        GLErrorLogger.check();

        GLES20.glAttachShader(program, vShader);
        GLES20.glAttachShader(program, fShader);
        GLErrorLogger.check();

        GLES20.glLinkProgram(program);

        int[] link = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, link, 0);
        if (link[0] == GLES20.GL_FALSE) {
            throw new RuntimeException("Program couldn't be loaded ("+link[0]+"): "+
                    GLES20.glGetProgramInfoLog(program));
        }
        GLES20.glDeleteShader(vShader);
        GLES20.glDeleteShader(fShader);
        GLErrorLogger.check();

    }

    public int getAttribLocation(String name){
        return GLES20.glGetAttribLocation(program, name);
    }
    public int getUniformAttribLocation(String name){
        return GLES20.glGetUniformLocation(program, name);
    }
    public int getUniformLocation(String name){
        return GLES20.glGetUniformLocation(program, name);
    }

    /**
     * called on finalization
     * */
    private void delete(){
        GLES20.glDeleteProgram(program);
    }

    /**
     * Called when no object referes to this material anymore
     * */
    @Override
    protected void finalize() throws Throwable {
        if(GLES20.glIsProgram(program))
            delete();
        super.finalize();
    }

    //    void initialize(){
//        // do loadShader and attachShader here
//        R.v
//    }

    public int getProgram() {
        return program;
    }
    public void use(){
        GLES20.glUseProgram(program);
    }

    public static int loadShader( Context context, int type, int resID ){
        try(InputStream is = context.getResources().openRawResource( resID )) {

            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String src = new String(buffer);
            return loadShader( type, src);

        } catch (IOException e) {
            Log.e(TAG, "Error loading vertex shader source: ", e);
            throw new RuntimeException(e); //TODO try for some default from materials if available
        }
    }
    //35633 is vertex type
    public static int loadShader( int type, String source ){
        int shader = GLES20.glCreateShader( type );
        GLErrorLogger.check();
        GLES20.glShaderSource( shader, source );
        GLErrorLogger.check();
        GLES20.glCompileShader(shader);
        int[] status = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, status, 0);
        if(status[0] == GLES20.GL_FALSE){
            Log.d(TAG, "src: \n"+source);
            throw new RuntimeException("Error compiling shader ("+status[0]+"): "+GLES20.glGetShaderInfoLog(shader));
        }
        return shader;
    }
}
/*
Vertex shader :
precision mediump float;
uniform mat4 uMVPMatrix;
attribute vec4 vPosition;
attribute vec4 vTextureCoordinate;
varying vec2 position;
void main() {
  gl_Position = uMVPMatrix * vPosition;
  position = vTextureCoordinate.xy;
}

Fragment shader :
precision mediump float;
uniform sampler2D uTexture;
varying vec2 position;
void main() {
  gl_FragColor = texture2D(uTexture, position);
}
 */