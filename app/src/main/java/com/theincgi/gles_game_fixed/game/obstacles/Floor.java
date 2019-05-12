package com.theincgi.gles_game_fixed.game.obstacles;

import com.theincgi.gles_game_fixed.game.entities.BasicBall;
import com.theincgi.gles_game_fixed.game.entities.RigidBodyBall;
import com.theincgi.gles_game_fixed.game.entity.Entity;
import com.theincgi.gles_game_fixed.geometry.ModelLoader3;
import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

public class Floor extends BaseObstacle{
   // private float[] transform = new float[16];
    public Floor() {
        this(0,0,0);
    }

    public Floor(float x, float y, float z) {
        super(x, y, z);
        model = ModelLoader3.get("floor");
        //Matrix.setIdentityM(transform, 0);
    }


    @Override
    public float[] intersectsSurface( Entity e ) {
        Location l = e.getLocation();
        if(e instanceof BasicBall){

        }
        if(e instanceof RigidBodyBall) {
            RigidBodyBall ball = ((RigidBodyBall) e);
//            if (Utils.inRange(location.getY(), l.getY() - ball.getRadius(), l.getY() + ball.getRadius())){
//                return true;
//            }
            float[] near = Utils.CollisionTests.nearestPointToPlane(e.getLocation(), location, normal);
            if(Utils.CollisionTests.sphereContains(near, e.getLocation(), ball.getRadius()) ||
                   Utils.CollisionTests.sphereContains(
                           near,
                       e.getLocation().getX() + e.getVelocityX(),
                        e.getLocation().getY() + e.getVelocityY(),
                        e.getLocation().getZ() + e.getVelocityZ(),
                           ball.getRadius()) ||
                    Utils.CollisionTests.cylinderContains(near, e.getLocation(), ball.getRadius(),
                            e.getVelocityX(), e.getVelocityY(), e.getVelocityZ()))
            return near;
        }
        return null;
    }


    @Override
    public float[] getNormal(Entity e) {
        return normal;
    }
}
