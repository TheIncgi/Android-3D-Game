package com.theincgi.gles_game_fixed.game.obstacles;

import com.theincgi.gles_game_fixed.game.entities.BasicBall;
import com.theincgi.gles_game_fixed.geometry.ModelLoader3;

public class Goal extends Floor {
    public Goal(float x, float y, float z){
        super(x, y, z);
        model = ModelLoader3.get("goal");
    }


}
