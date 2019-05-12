package com.theincgi.gles_game_fixed;

public class Player {

    private int id;
    private String username;
    private static String score;
    private String password;

    public Player(){

    }

    public Player(int in, String username, String score, String password){
        this.id = id;
        this.username = username;
        this.score = score;
        this.password = password;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return username;
    }

    public String getPassword() {return password;}

    public void setUsername(String username){
        this.username = username;
    }

    public static String getScore(){
        return score;
    }

    public void setScore(String score){
        this.score = score;
    }

    public void setPassword(String password) {this.password = password; }


    @Override
    public String toString(){
        return username + " " + score + " " + password;
    }
}
