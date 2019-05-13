package com.theincgi.gles_game_fixed;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import static com.theincgi.gles_game_fixed.musicButton.LOAD_METHOD_CODE;
import static com.theincgi.gles_game_fixed.musicButton.LOAD_METHOD_ID;


public class Screen1 extends AppCompatActivity implements View.OnClickListener {
    private MazeDatabase db;
    MediaPlayer mySong;
    Button button;
    EditText editText;
    TextView welcome;
    TextView textView;
    int id;

    //this is the first screen the player sees
    //it will ask for a username/password and stores it into the database
    //it will also comeback to this screen to display scores (W.I.P.)
    //music player
    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen1);
        Configuration config = getResources().getConfiguration();
        this.doBindService();
        Intent music = new Intent(this,MusicService.class);
        startService(music);

        welcome = (TextView)findViewById(R.id.Welcome);
        textView = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.Button);
        button.setOnClickListener(Screen1.this);
        mySong=MediaPlayer.create(this,R.raw.coin_grab_3);
        db = new MazeDatabase(this);

        int code = getIntent().getIntExtra(LOAD_METHOD_ID, 0);
        if (code == LOAD_METHOD_CODE) {
            this.scores();
        }

    }




    @Override
    public void onClick(View view){

        mySong.start();
        EditText userGet = (EditText) findViewById(R.id.enter_user);
        String username = userGet.getText().toString();
        EditText passGet = (EditText) findViewById(R.id.password);
        String password = passGet.getText().toString();


        for (int i = 0; i < 100; i++){
            id = i;
        }
        Player userPlayer = new Player(id, username, "0", password);
        Log.d("Inserting", "Inserting information into the database");

        db.addUser(userPlayer);

        Toast.makeText(getApplicationContext(), "Adding " +" " + username + " " + "to the DB", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, musicButton.class);
        intent.putExtra("Username", username);
        intent.putExtra("id", id);
        startActivity(intent);
        this.finish();
    }

    public void scores(){
        HashMap<String,String> data = db.getData();
        String level1Score = data.get("LEVEL1");
        String level2Score = data.get("LEVEL2");
        Toast.makeText(getApplicationContext(), "level 1 score is" + level1Score, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "level 2 score is" + level2Score, Toast.LENGTH_LONG).show();
    }

}

