package com.theincgi.gles_game_fixed;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.sql.SQLInput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MazeDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "maze.db";
    private static final int VERSION = 2;
    private static MazeDatabase sInstance;

//the database covers the username, password, score, and scores for level1/level2
    //the database stores information given by the other classes
    private static final class MazeTable {
        private static final String TABLE = "mazes";
        private static final String COL_ID = "_id";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
        private static final String COL_SCORE = "score";
        private static final String LEVEL1 = "LEVEL1";
        private static final String LEVEL2 = "LEVEL2";
        private static final String[] COLUMNS = { COL_ID, COL_USERNAME, COL_SCORE, COL_PASSWORD, LEVEL1, LEVEL2};
    }

    public static synchronized MazeDatabase getInstance(Context context){
        if (sInstance == null){
            sInstance = new MazeDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    public MazeDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MazeTable.TABLE + " (" +
                MazeTable.COL_ID + " integer primary key autoincrement, " +
                MazeTable.COL_USERNAME + "username " +
                MazeTable.COL_PASSWORD + "password," +
                MazeTable.COL_SCORE + "score," +
                MazeTable.LEVEL1 + "LEVEL1`, " +
                MazeTable.LEVEL2 + "LEVEL2`)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("drop table if exists " + MazeTable.TABLE);
        onCreate(db);
    }


    public long addUser(Player player) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MazeTable.COL_USERNAME, player.getName());
        values.put(MazeTable.COL_SCORE, 0);
        values.put(MazeTable.COL_PASSWORD, player.getPassword());

        long mazeId = db.insert(MazeTable.TABLE, null, values);
        db.close();
        return mazeId;
    }

    public List<Player> getAllScores(){
        List<Player> playerList = new ArrayList<>();

        String selectQuery = "SELECT * FROM "+ MazeTable.TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Player player = new Player();
                player.setId(Integer.parseInt(cursor.getString(0)));
                player.setUsername(cursor.getString(1));
                //player.setScore(cursor.getInt(2));
                player.setPassword(cursor.getString(3));

                playerList.add(player);
            }while (cursor.moveToNext());
        }
        return playerList;
    }

    public void addScore(Player player){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MazeTable.COL_SCORE, Player.getScore());

        db.insert(MazeTable.TABLE, null, values);
        db.close();
    }
public void saveScore(int level, String score){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues data = new ContentValues();
    switch(level){
        case 1:
            data.put(MazeTable.LEVEL1, score);
            break;
        case 2:
            data.put(MazeTable.LEVEL2,score);
            break;
    }
    db.insert(MazeTable.TABLE,null,data);
    db.close();
}
    public String getUser(String username) {

        SQLiteDatabase db = getReadableDatabase();

        String rv = "Not found";
        String sql = "select * from " + MazeTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                //long id = cursor.getLong(0);
                rv = cursor.getString(1);
                //String score = cursor.getString((2));
                Log.d(TAG, "Username = " + rv + ", " + username);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rv;
    }

    public boolean updateMaze(Player player){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MazeTable.COL_USERNAME, player.getName());
        values.put(MazeTable.COL_SCORE, player.getScore());
        values.put(MazeTable.COL_PASSWORD, player.getPassword());

        int rowsUpdated = db.update(MazeTable.TABLE, values, "_id = ?",
                new String[] { String.valueOf(player.getId())});

        db.close();
        return rowsUpdated > 0;
    }

    public boolean deleteUser(long id){
        SQLiteDatabase db = getWritableDatabase();
        int rowsDeleted = db.delete(MazeTable.TABLE, MazeTable.COL_ID + " =?",
                new String[] { Long.toString(id)});
        db.close();
        return rowsDeleted > 0;
    }

    public HashMap<String,String> getData(){
        HashMap<String,String> scoreMap = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + MazeTable.TABLE ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                scoreMap.put(MazeTable.LEVEL1,cursor.getString(cursor.getColumnIndex(MazeTable.LEVEL1)));
                scoreMap.put(MazeTable.LEVEL2,cursor.getString(cursor.getColumnIndex(MazeTable.LEVEL2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return scoreMap;

    }
}



