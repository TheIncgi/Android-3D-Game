package com.theincgi.gles_game_fixed.render;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL;

public class CustomGLSurfaceView extends GLSurfaceView {
    CustomGLSurfaceViewRenderer renderer;

    public CustomGLSurfaceView(Context context, AttributeSet attrs) {
        this(context);
    }

    public CustomGLSurfaceView(Context context) {
        super(context);

    }



    public void init(CustomGLSurfaceViewRenderer renderer){
        this.renderer = renderer;
        Log.d("#GLES", "CustomGLSurfaceView: Creating context ("+Thread.currentThread().getId()+")");
        setEGLContextClientVersion(2); //Creates OpenGL ES 2.0 context
        Log.d("#GLES", "Version has been set to 2.0");
    }



    private final float TOUCH_SCALE_FACTOR = 1.0f / 5;
    private float previousX, previousY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        float x = event.getX(), y = event.getY();
//
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                previousX = x;
//                previousY = y;
//
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float dx = x - previousX;
//                float dy = y - previousY;
//                previousX = x; previousY = y;
//                if(dx==0 && dy==0) return true;
////                // reverse direction of rotation above the mid-line
////                if (y > getHeight() / 2) {
////                    dx = dx * -1 ;
////                }
////
////                // reverse direction of rotation to left of the mid-line
////                if (x < getWidth() / 2) {
////                    dy = dy * -1 ;
////                }
//                dx*= TOUCH_SCALE_FACTOR;
//                dy*=TOUCH_SCALE_FACTOR;
//
//                float yaw, pitch;
//                Location l = renderer.camera.location;
//                yaw = l.getYaw();
//                pitch = l.getPitch();
//
//                yaw += dx;
//                pitch += dy;
//                pitch = Utils.clamp(pitch, -90, 90);
//
//                l.setRotation(yaw, pitch, 0);
//
//                yaw = (float)Math.toRadians(yaw-90);
//
//                pitch = (float)Math.toRadians(pitch);
//
//                //float xzDist = Utils.distance( 0,0,0, l.getX(), 0, l.getZ() );
//
//                l.setPos(
//                        (float)(5 * Math.cos( yaw ) * Math.cos(pitch) ),
//                        (float)(5 * Math.sin( -pitch )),//l.getY(),
//                        (float)(5 * Math.sin( yaw ) * Math.cos(pitch) )
//                );
//
//        }
//        return true;
        return false;
    }

    public void setDrawWhenDirty(boolean b ){
        setRenderMode(b? GLSurfaceView.RENDERMODE_WHEN_DIRTY : GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
    public void markDirty(){
        requestRender();
    }
}
