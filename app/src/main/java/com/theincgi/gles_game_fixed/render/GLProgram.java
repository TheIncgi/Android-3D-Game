package com.theincgi.gles_game_fixed.render;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.theincgi.gles_game_fixed.utils.GLErrorLogger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;

public class GLProgram{
    private static final String TAG = "#GLProgram";
    /*
    * Vertex shader transforms vertex coords*/

    //make things quieter
    private static boolean doWarnings = false;

    private int program;

    public GLProgram(Context context, int vertex_id, int fragment_id) {
        Log.i("Shaders", "Building shader...");
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
        Log.i("Shader", "Finished building shader");
    }

    public int getAttribLocation(String name){
        return GLES20.glGetAttribLocation(program, name);
    }
//    public int getUniformAttribLocation(String name){
//        return GLES20.glGetAttribLocation(program, name);
//    }
    public int getUniformLocation(String name){
        return GLES20.glGetUniformLocation(program, name);
    }

    public boolean trySetVertexAttribArray(String key, FloatBuffer vCoords){
        int posH = getAttribLocation( key );
        if(posH==-1){warnMissingKey(key, "attribute vertex array"); return false;}
        GLES20.glEnableVertexAttribArray(posH);
        GLES20.glVertexAttribPointer( posH,
                3,//COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                3*Float.BYTES,//COORDS_PER_VERTEX*Float.BYTES,
                vCoords);
        GLErrorLogger.check();
        return true;
    }
    public boolean tryDisableVertexAttribArray(String key){
        int posH = getAttribLocation( key );
        if(posH==-1) {warnMissingKey(key, "attribute vertex array"); return false;}
        GLES20.glDisableVertexAttribArray( posH );
        GLErrorLogger.check();
        return true;
    }
    public boolean trySetUniform( String name, int i ){
        int handle = getUniformLocation( name );
        if(handle == -1){warnMissingKey(name, "integer"); return false;}
        GLES20.glUniform1i( handle, i );
        GLErrorLogger.check();
        return true;
    }
    public boolean trySetUniform( String name, float f) {
        int handle = getUniformLocation( name );
        if(handle == -1){warnMissingKey(name, "float"); return false;}
        GLES20.glUniform1f( handle, f );
        GLErrorLogger.check();
        return true;
    }
    public boolean trySetUniform( String name, float[] f){
        int handle = getUniformLocation( name );
        if(handle == -1){warnMissingKey(name, "vec"+f.length); return false;}
        switch (f.length){
            case 0:
                throw new RuntimeException("Empty float array!");
            case 1:
                GLES20.glUniform1fv(handle, 1, f, 0);
                break;
            case 2:
                GLES20.glUniform2fv(handle, 1, f, 0);
                break;
            case 3:
                GLES20.glUniform3fv(handle, 1, f, 0);
                break;
            case 4:
                GLES20.glUniform4fv(handle, 1, f, 0);
                break;
            default:
                throw new RuntimeException("Not a valid vector size ("+f.length+")");

        }
        GLErrorLogger.check();
        return true;
    }
    public boolean trySetMatrix(String name, float[] m){
        int handle = getUniformLocation( name );
        if(handle == -1){warnMissingKey(name, "Matrix"); return false;}
        switch (m.length){
            case 4:
                GLES20.glUniformMatrix2fv(handle, 1, false, m, 0);
                break;
            case 9:
                GLES20.glUniformMatrix3fv(handle, 1, false, m, 0);
                break;
            case 16:
                GLES20.glUniformMatrix4fv(handle, 1, false, m, 0);
                break;
            default:
                throw new RuntimeException("Not a valid matrix size");
        }
        GLErrorLogger.check();
        return true;
    }
    public boolean trySetUniformTexture( String name ){
        throw new RuntimeException("Unimplemented!");
    }

//    public boolean trySetVertexAttribArray(FloatBuffer vCoords, String... possibleKeys){
//        for (String key:possibleKeys) {
//            if(trySetVertexAttribArray(key, vCoords)) return true;
//        }
//        return false;
//    }
//    public boolean tryDisableVertexAttribArray(String... possibleKeys){
//        for(String key : possibleKeys)
//            if(tryDisableVertexAttribArray(key)) return true;
//        return false;
//    }
//    public boolean trySetUniform(int value, String... possibleKeys){
//        for(String key:possibleKeys)
//            if(trySetUniform( key, value )) return true;
//        return false;
//    }
//    public boolean trySetUniform(float value, String... possibleKeys){
//        for(String key:possibleKeys)
//            if(trySetUniform( key, value )) return true;
//        return false;
//    }
//    public boolean trySetUniform(float[] value, String... possibleKeys){
//        for(String key:possibleKeys)
//            if(trySetUniform( key, value )) return true;
//        return false;
//    }
//    public boolean trySetMatrix(float[] m, String... possibleKeys){
//        for(String key : possibleKeys)
//            if(trySetMatrix(key, m)) return true;
//        return false;
//    }
//    public boolean trySetUniformTexture(String... possibleKeys){
//        for(String key : possibleKeys)
//            if(trySetUniformTexture(key)) return true;
//        return false;
//    }


    private void warnMissingKey(String key, String type){
        if(doWarnings)
        Log.w(TAG, String.format("warnMissingKey: \"%s\" for type '%s' in shader [%s]",key, type, GLPrograms.getNameOf(program)));
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