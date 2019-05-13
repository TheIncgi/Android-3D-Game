package com.theincgi.gles_game_fixed;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class musicButton extends AppCompatActivity {
MediaPlayer mySong;
private Button playButton;
private Button selectLevelButton;
private Button highScoreButton;
private Button goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_button);
        mySong=MediaPlayer.create(this,R.raw.coin_grab_3);
        playButton = findViewById(R.id.button);
        selectLevelButton = findViewById(R.id.levelButt);
        highScoreButton = findViewById(R.id.highScoreButton);
    }
    public void playIT(View view) {
        mySong.start();
        Intent intent = null;
        if(view .equals( playButton ) ){

            //example
            intent = new Intent(this, MainActivity.class);
            intent.putExtras(new Bundle());
            intent.getExtras().putInt(MainActivity.LEVEL_KEY, 1); //select level 1

        }else if ( view .equals( selectLevelButton ) ){
            intent = new Intent(this, Levelselector.class);
        }else if( view.equals( highScoreButton )){
            intent = new Intent(this, Screen1.class);
        }
        else if( view.equals( goBack )){
            intent = new Intent(this,Screen1.class);
        }

        if(intent != null){ //set by a button
            startActivity(intent); //start the activity
        }
    }
}
