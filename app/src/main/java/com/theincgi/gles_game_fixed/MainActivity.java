package com.theincgi.gles_game_fixed;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.theincgi.gles_game_fixed.render.CustomGLSurfaceView;
import com.theincgi.gles_game_fixed.render.CustomGLSurfaceViewRenderer;


public class MainActivity extends AppCompatActivity {
    //ConstraintLayout constraintLayout;
    CustomGLSurfaceView glSurfaceView;
    CustomGLSurfaceViewRenderer glSurfaceViewRenderer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean useXML = false;
        if( useXML ) {

            glSurfaceView = findViewById(R.id.customGLSurfaceView);
            if (glSurfaceView == null)
                throw new NullPointerException("could not load gl surface view!");
            glSurfaceView.init(glSurfaceViewRenderer = new CustomGLSurfaceViewRenderer(this));


            glSurfaceView.setRenderer(glSurfaceViewRenderer);
            glSurfaceView.setDrawWhenDirty(false);


        }else {
            glSurfaceView = new CustomGLSurfaceView(this);
            glSurfaceView.init( glSurfaceViewRenderer = new CustomGLSurfaceViewRenderer(this));
            glSurfaceView.setRenderer(glSurfaceViewRenderer);
            glSurfaceView.setDrawWhenDirty(false);
            setContentView(glSurfaceView);
        }
    }
}
