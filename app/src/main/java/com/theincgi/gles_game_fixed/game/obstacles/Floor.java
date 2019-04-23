package com.theincgi.gles_game_fixed.game.obstacles;

import android.opengl.Matrix;

import com.theincgi.gles_game_fixed.game.entities.Ball;
import com.theincgi.gles_game_fixed.game.entity.Entity;
import com.theincgi.gles_game_fixed.geometry.ModelLoader3;
import com.theincgi.gles_game_fixed.render.Camera;
import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

public class Floor extends BaseObstacle{
    public Floor() {
        this(0,0,0);
    }

    public Floor(float x, float y, float z) {
        super(x, y, z);
        model = ModelLoader3.get("floor");
    }

    @Override
    public boolean intersectsSurface( Entity e ) {
        Location l = e.getLocation();
        if(e instanceof Ball) {
            Ball ball = ((Ball) e);
            if (Utils.inRange(location.getY(), l.getY() - ball.getRadius(), l.getY() + ball.getRadius())){
                return true;
            }
        }
        return false;
    }


    @Override
    public float[] getNormal(Entity e) {
        float[] tmp = new float[4];
        Matrix.multiplyMM(tmp, 0, location.getMatrix(), 0, normal, 0);
        return tmp;
    }
}
