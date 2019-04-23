package com.theincgi.gles_game_fixed.game.entities;

import com.theincgi.gles_game_fixed.game.Engine;
import com.theincgi.gles_game_fixed.game.entity.Entity;
import com.theincgi.gles_game_fixed.game.obstacles.BaseObstacle;
import com.theincgi.gles_game_fixed.utils.Location;

import java.util.Iterator;

public class Ball extends Entity {
    float radius = 1;

    public Ball() {
        super( "sphere" );
        location = new Location(0,15,0);
    }

    @Override
    public void onTick(Engine e, long time) {
        BaseObstacle obst = null;
        if(!onGround){
            Iterator<BaseObstacle> iterator = e.getObstacleItterator();
            while(iterator.hasNext()){
                obst = iterator.next();
                boolean intersects = obst.intersectsSurface( this );
                if(intersects){

                    velocityY = Math.abs(velocityY * .3f);
                    break;
                }
            }
        }
        setOnGround( obst!=null );
        if(!onGround){
            velocityY+=gravity;
        }//else{
            //float[] normal = obst.getNormal( this );

        //}
        if( obst == null || obst!=null && obst.yLimit() + radius + gravity*2 < location.getY())
            location.move(velocityX,velocityY,velocityZ);
        if(obst!=null)
            location.setY( Math.max(location.getY(), obst.yLimit() + radius) );
    }

    public float getRadius() {
        return radius;
    }
}
