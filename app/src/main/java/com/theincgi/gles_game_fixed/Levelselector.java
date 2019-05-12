package com.example.finalprojectsetting;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Levelselector extends AppCompatActivity {

    private Button buttonOne;
    private Button buttonTwo;
    private Button buttonThree;
    MediaPlayer mySong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_sel);

        buttonOne = findViewById(R.id.level1);
        buttonTwo = findViewById(R.id.level2);
        mySong = MediaPlayer.create(this, R.raw.coin_grab_3);


    }

    public void onClickLevelOne(View view){

        mySong.start();
        Intent intent = new Intent(this, Level1.class);
        intent.getExtras().putInt(MainActivity.LEVEL_KEY, 1);
        startActivity(intent);

    }

    public void onClickLevelTwo(View view){

        mySong.start();
        Intent intent = new Intent(this, Level2.class);
        intent.getExtras().putInt(MainActivity.LEVEL_KEY, 2);
        startActivity(intent);

    }
    //public void onClickLevelThree(View view){

       // Intent intent = new Intent(this, Level3.class);
       // startActivity(intent);

   // }
}
