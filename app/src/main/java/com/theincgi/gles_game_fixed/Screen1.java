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


public class Screen1 extends AppCompatActivity implements View.OnClickListener {
    private MazeDatabase db;
    MediaPlayer mySong;
    Button button;
    EditText editText;
    TextView welcome;
    TextView textView;
    int id;

    //music player, this comes from MusicService.java
    //doBindService starts the music while doUnbindService will end it
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

    //Once the user enters their info and clicks the login button, they will move onto the second screen
    //The music also starts playing on this screen
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
        db = new MazeDatabase(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.commit();

     /*   if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            LM_fragment ls_fragment = new LM_fragment();
            fragmentTransaction.replace(android.R.id.content, ls_fragment);}else{
            PM_fragment pm_fragment = new PM_fragment();
            fragmentTransaction.replace(android.R.id.content, pm_fragment);
        }
        fragmentTransaction.commit();*/
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

        Toast.makeText(getApplicationContext(), "Adding" + username, Toast.LENGTH_LONG);
        Intent intent = new Intent(this, musicButton.class);
        startActivity(intent);
        this.finish();
    }

}

