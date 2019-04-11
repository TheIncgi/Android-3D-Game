package com.theincgi.gles_game_fixed;


import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entities.Ball;
import com.theincgi.gles_game_fixed.render.CustomGLSurfaceView;
import com.theincgi.gles_game_fixed.render.CustomGLSurfaceViewRenderer;


public class MainActivity extends AppCompatActivity {
    //ConstraintLayout constraintLayout;
    CustomGLSurfaceView glSurfaceView;
    CustomGLSurfaceViewRenderer glSurfaceViewRenderer;
    FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        boolean useXML = false;
        if( useXML ) {
            setContentView(R.layout.activity_main);

            frameLayout = findViewById(R.id.frameLayout);
            if(frameLayout==null)
                throw new NullPointerException("Could not load frame layout!");
            glSurfaceView = findViewById(R.id.theGLSurfaceView);
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

        //Engine.instance().start();

    }
}
