package com.theincgi.gles_game_fixed.render;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL;

public class CustomGLSurfaceView extends GLSurfaceView {
    CustomGLSurfaceViewRenderer renderer;

    public CustomGLSurfaceView(Context context, CustomGLSurfaceViewRenderer renderer) {
        super(context);
        Log.d("#GLES", "CustomGLSurfaceView: Creating context ("+Thread.currentThread().getId()+")");
        setEGLContextClientVersion(2); //Creates OpenGL ES 2.0 context
        Log.d("#GLES", "Version has been set to 2.0");


    }



    public void setDrawWhenDirty( boolean b ){
        setRenderMode(b? GLSurfaceView.RENDERMODE_WHEN_DIRTY : GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
    public void markDirty(){
        requestRender();
    }
}
