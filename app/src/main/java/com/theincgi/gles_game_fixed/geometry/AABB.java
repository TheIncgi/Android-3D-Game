package com.theincgi.gles_game_fixed.geometry;

import com.theincgi.gles_game_fixed.utils.Location;

//axis aligned bounding box
public class AABB {
    Location location;
    float xLen, yLen, zLen;

    public AABB(Location location, float xLen, float yLen, float zLen) {
        this.location = location;
        this.xLen = xLen/2;
        this.yLen = yLen/2;
        this.zLen = zLen/2;
    }

    public boolean contains(Location location){
        return inRange(location.getX(), lowX(), highX()) &&
               inRange(location.getY(), lowY(), highY()) &&
               inRange(location.getZ(), lowZ(), highZ());
    }

    public boolean intersects(AABB other){
        boolean flag = true;
        flag &= rangeOverlaps( lowX(), highX(), other.lowX(), other.highX() );
        flag &= rangeOverlaps( lowY(), highY(), other.lowY(), other.highY() );
        flag &= rangeOverlaps( lowZ(), highZ(), other.lowZ(), other.highZ() );
        return flag;
    }

    public float lowX() { return this.location.getX()-xLen; }
    public float highX(){ return this.location.getX()+xLen; }
    public float lowY() { return this.location.getY()-yLen; }
    public float highY(){ return this.location.getY()+yLen; }
    public float lowZ() { return this.location.getZ()-zLen; }
    public float highZ(){ return this.location.getZ()+zLen; }

    private boolean inRange(float x, float a, float b){
        return a<=x && x<b;
    }

    private boolean rangeOverlaps( float a, float b, float x, float y ){
        return inRange(x, a, b) || inRange(y, a, b) || inRange(a, x, y) || inRange(b, x, y);
    }

    public float getXLen() {
        return xLen*2;
    }

    public float getYLen() {
        return yLen*2;
    }

    public float getZLen() {
        return zLen*2;
    }

    public void setXLen(float xLen) {
        this.xLen = xLen/2;
    }

    public void setYLen(float yLen) {
        this.yLen = yLen/2;
    }

    public void setZLen(float zLen) {
        this.zLen = zLen/2;
    }
}
