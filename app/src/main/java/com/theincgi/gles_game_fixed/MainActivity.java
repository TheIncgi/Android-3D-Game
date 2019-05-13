package com.theincgi.gles_game_fixed;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.levels.Level;
import com.theincgi.gles_game_fixed.game.levels.Level1;
import com.theincgi.gles_game_fixed.game.levels.Level2;
import com.theincgi.gles_game_fixed.game.levels.TestLevel2;
import com.theincgi.gles_game_fixed.render.CustomGLSurfaceView;
import com.theincgi.gles_game_fixed.render.CustomGLSurfaceViewRenderer;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "##MAINACTIVITY";
    //ConstraintLayout constraintLayout;
    CustomGLSurfaceView glSurfaceView;
    CustomGLSurfaceViewRenderer glSurfaceViewRenderer;
    FrameLayout frameLayout;

    private static MainActivity instance;

    public static final String LEVEL_KEY = "LEVEL";

    public static void onFinish() {
        instance.finish();
        Engine.instance().stop();
    }

    @Override
    public void onBackPressed() {
        Engine.instance().stop();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        Log.i(TAG, "onCreate: MainActivity");
        //public static final int FLAG_KEEP_SCREEN_ON = 0x00000080
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        boolean useXML = false;

        getIntent().getStringExtra(LEVEL_KEY);
        Level.MakeLevel levelMake = new Level.MakeLevel() {
            @Override
            public Level make() {
                switch(getIntent().getIntExtra(LEVEL_KEY, 2)){
                    case 1:
                        return new Level1(MainActivity.this);
                    case 2:
                        return new Level2( MainActivity.this);
                }
                return new TestLevel2(MainActivity.this);
            }
        };

        glSurfaceViewRenderer = new CustomGLSurfaceViewRenderer(this, levelMake);



        if( useXML ) {
            setContentView(R.layout.activity_main);

            frameLayout = findViewById(R.id.frameLayout);
            if(frameLayout==null)
                throw new NullPointerException("Could not load frame layout!");
            glSurfaceView = findViewById(R.id.theGLSurfaceView);
            if (glSurfaceView == null)
                throw new NullPointerException("could not load gl surface view!");

            glSurfaceView.init(glSurfaceViewRenderer);
            glSurfaceView.setRenderer(glSurfaceViewRenderer);
            glSurfaceView.setDrawWhenDirty(false);


        }else {
            glSurfaceView = new CustomGLSurfaceView(this);
            glSurfaceView.init( glSurfaceViewRenderer );
            glSurfaceView.setRenderer(glSurfaceViewRenderer);
            glSurfaceView.setDrawWhenDirty(false);
            setContentView(glSurfaceView);
        }


        //Engine.instance().start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Engine.instance().stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Engine.instance().start();
    }
}
