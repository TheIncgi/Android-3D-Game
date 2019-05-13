package com.theincgi.gles_game_fixed;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class musicButton extends AppCompatActivity {
    MediaPlayer mySong;
    private Button playButton;
    private Button selectLevelButton;
    private Button highScoreButton;
    private Button goBack;
    public static final String LOAD_METHOD_ID = "load_method_id";
    public static final int LOAD_METHOD_CODE = 92840;


  //this screen acts as the main menu
    //it will give the choice of proceeding to level 1 or choosing a harder level
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_button);
        mySong=MediaPlayer.create(this,R.raw.coin_grab_3);
        playButton = findViewById(R.id.button);
        selectLevelButton = findViewById(R.id.levelButt);
        highScoreButton = findViewById(R.id.highScoreButton);
        //String username = getIntent().getStringExtra("Username");

        //this.doBindService();
        Intent music = new Intent(this,MusicService.class);
        startService(music);
        //displayName();
       // EditText usernameget = (EditText)findViewById(R.id.edittext);
        String username = getIntent().getStringExtra("Username");

    }

  /*  public void displayName(){
        String username = getIntent().getStringExtra("Username");
        Toast.makeText(this, "Hello" + username, Toast.LENGTH_SHORT).show();
        EditText usernameget = (EditText)findViewById(R.id.edittext);
        usernameget.setText(username);
        to launch
                    <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        to not launch
                    <!--<intent-filter>-->
            <!--<action android:name="android.intent.action.MAIN" />-->

            <!--<category android:name="android.intent.category.LAUNCHER" />-->
            <!--</intent-filter>-->

    }*/

    public void playIT(View view) {
        mySong.start();
        Intent intent = null;
        if(view .equals( playButton ) ){

            //example
            intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.LEVEL_KEY, 1);//select level 1
            //String username = getIntent().getStringExtra("Username");
            //intent.putExtra("Username", username);
        }else if ( view .equals( selectLevelButton ) ){
            intent = new Intent(this, Levelselector.class);
        }else if( view.equals( highScoreButton )){
            intent = new Intent(this, Screen1.class);
            intent.putExtra(LOAD_METHOD_ID, LOAD_METHOD_CODE);
            startActivity(intent);
        }
        else if( view.equals( goBack )){
            intent = new Intent(this,Screen1.class);
        }

        if(intent != null){ //set by a button
            startActivity(intent); //start the activity
        }
    }
}