package com.theincgi.gles_game_fixed.game.obstacles;

import android.opengl.Matrix;
import android.util.Log;

import com.theincgi.gles_game_fixed.game.entities.BasicBall;
import com.theincgi.gles_game_fixed.game.entity.Entity;
import com.theincgi.gles_game_fixed.geometry.ModelLoader3;
import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

public class Hill extends BaseObstacle {

    public Hill(float x, float y, float z){
        super(x, y, z);
        model = ModelLoader3.get("hill");
    }

    @Override
    public boolean pointOver(Location l) {
        return Utils.inRange(l.getX(), location.getX()-2, location.getX()+2) &&
               Utils.inRange(l.getZ(), location.getZ()-1, location.getZ()+1) &&
               Utils.inRange(l.getY(), location.getY()-.5f, location.getY()+1.5f);
    }

    @Override
    public float yOffset(Location l) {
        float x = l.getX()-this.location.getX();

        return (float)(-.2*Math.cos( (x/2-1)*Math.PI )+.2);
    }

    @Override
    public float[] force(Location l) {
        float x = l.getX()-this.location.getX();
        return new float[]{ ((x/2)*(x/2))/80, 0, 0 };
    }
}
