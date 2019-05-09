package com.theincgi.gles_game_fixed.game.obstacles;

import com.theincgi.gles_game_fixed.game.entity.Entity;
import com.theincgi.gles_game_fixed.geometry.ModelLoader3;
import com.theincgi.gles_game_fixed.render.Camera;
import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

public class BaseObstacle implements ModelLoader3.DrawableModel{
    ModelLoader3.Model model;
    protected Location location;
    private static int obstID = 0;
    public static final int OBST_ID = obstID++;
    protected float[] normal = {0,1,0,  1};
    public BaseObstacle() {
        location = new Location(0,0,0);
    }
    public BaseObstacle(float x, float y, float z){
        location = new Location(x,y,z);
    }

    public float[] intersectsSurface( Entity e ){
        return null;
    }

    public boolean pointOver(Location l){
        return Utils.inRange(l.getX(), this.location.getX()-1,this.location.getX()+1)&&
                Utils.inRange(l.getZ(), this.location.getZ()-1, this.location.getZ()+1) &&
                Utils.inRange(l.getY(), 0, 1.5f);
    }
    @Override
    public void draw(Camera camera) {
        model.drawAll(location);
    }

    public float[] getNormal( Entity e ){
        return normal;
    }

    public float yOffset(Location location){
        return this.location.getY();
    }
    public float[] force(Location l){
        return new float[] {0,0,0,1};
    }

}
