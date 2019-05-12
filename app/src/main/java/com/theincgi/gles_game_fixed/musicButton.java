package com.theincgi.gles_game_fixed;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MusicButton extends AppCompatActivity {
MediaPlayer mySong;
private Button playButton;
private Button selectLevelButton;
private Button highScoreButton;
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
            //TODO create an intent to launch the selected level
            //this little chunk of code would be run in the level select activity
            //intent = new Intent(this, SomeActivityNameHere.class);
            //intent.getExtras().putInt(MainActivity.LEVEL_KEY, /**Level number here (starts at 1)*/);
        }else if( view.equals( highScoreButton )){
            intent = new Intent(this, Screen1.class);
        }

        if(intent != null){ //set by a button
            startActivity(intent); //start the activity
        }
    }
}
