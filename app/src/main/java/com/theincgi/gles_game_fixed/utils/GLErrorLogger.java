package com.theincgi.gles_game_fixed.utils;

import android.opengl.GLES20;

public class GLErrorLogger {
    private  GLErrorLogger(){}

    public static void check(){
        int code = GLES20.glGetError();
        if(code!=GLES20.GL_NO_ERROR)
            throw new GL_Exception(code);
    }

    public static class GL_Exception extends RuntimeException {
        public GL_Exception(int code){
            super(pickMessage(code));
        }
        private static String pickMessage(int code){
            switch (code){
                case GLES20.GL_INVALID_ENUM: return "GL Invalid Enum";
                case GLES20.GL_INVALID_VALUE: return "GL Invalid Value";
                case GLES20.GL_INVALID_OPERATION: return "GL Invalid Operation";
                case GLES20.GL_INVALID_FRAMEBUFFER_OPERATION: return "GL Invalid Framebuffer Operation";
                case GLES20.GL_OUT_OF_MEMORY: return "GL Out of Memory";
                default: return "GL Undefined Error ("+code+")";
            }
        }
    }
}
