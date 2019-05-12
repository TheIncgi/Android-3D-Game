package com.theincgi.gles_game_fixed;

import android.content.res.Configuration;
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

import com.example.mazedatabase.R;


public class Screen1 extends AppCompatActivity implements View.OnClickListener {
    private MazeDatabase db;

    Button button;
    EditText editText;
    TextView welcome;
    TextView textView;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuration config = getResources().getConfiguration();

        welcome = (TextView)findViewById(R.id.Welcome);
        textView = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.Button);
        button.setOnClickListener(Screen1.this);

        db = new MazeDatabase(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.commit();

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            LM_fragment ls_fragment = new LM_fragment();
            fragmentTransaction.replace(android.R.id.content, ls_fragment);}else{
            PM_fragment pm_fragment = new PM_fragment();
            fragmentTransaction.replace(android.R.id.content, pm_fragment);
        }
        fragmentTransaction.commit();
    }
    @Override
    public void onClick(View view){
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
        this.finish();

        /*
        Intent intent = new Intent(this, Screen2.class);
        startActivity(intent); */
    }

}

