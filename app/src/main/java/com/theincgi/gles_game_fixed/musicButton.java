package com.theincgi.gles_game_fixed;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MusicButton extends AppCompatActivity {
MediaPlayer mySong;
private Button button;
private Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_button);
        mySong=MediaPlayer.create(this,R.raw.coin_grab_3);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.levelButt);
    }
    public void playIT(View view) {
        mySong.start();
    }
}
