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


//        setContentView(R.layout.activity_main);
//
//        constraintLayout = findViewById(R.id.constraintLayout);
        Log.d("#GLES", "Main: setting context ("+Thread.currentThread().getId()+")");


        glSurfaceView = new CustomGLSurfaceView(this, glSurfaceViewRenderer = new CustomGLSurfaceViewRenderer(this));

        //glSurfaceView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        glSurfaceView.setRenderer(glSurfaceViewRenderer);
        glSurfaceView.setDrawWhenDirty( false );
        setContentView(glSurfaceView);


    }
}
