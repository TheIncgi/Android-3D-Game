package com.theincgi.gles_game_fixed.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

public class Utils {
    public static boolean inRange(float x, float low, float high){
        return low <= x && x < high;
    }
    
    public static float max(float... values){
        float max = values[0];
        for(int i = 1; i<values.length-1; i++)
            max = Math.max(max, values[i]);
        return max;
    }
    public static float min(float... values){
        float min = values[0];
        for(int i = 1; i<values.length-1; i++)
            min = Math.min(min, values[i]);
        return min;
    }
    public static float distance(Location a, Location b){ return distance(a, b.getX(), b.getY(), b.getZ());}
    public static float distance(Location l, float x, float y, float z){ return distance(l.getX(), l.getY(), l.getZ(), x, y, z); }
    public static float distance( float a, float b, float c, float x, float y, float z ){
        float dx = x-a, dy = y-b, dz = z-c;
        return (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
    public static float clamp(float x, float low, float high){
        return Math.max(low, Math.min(x, high));
    }
    public static float[] clone(float[] matrix){
        float[] output = new float[matrix.length];
        System.arraycopy(matrix,0, output, 0, matrix.length);
        return output;
    }

    public static int countChar(String s, char c){
        int out = 0;
        int cursor = 0;
        while((cursor = (s.indexOf(c, cursor+1))) != -1)
            out++;
        return out;
    }


    public static int[] unpackIntList(ArrayList<int[]> a, int pos){
        int[] out = new int[a.size()];
        for(int i = 0; i<a.size(); i++){
            out[i] = a.get(i)[pos];
        }
        return out;
    }
    public static float[] unpackFloatList(ArrayList<float[]> a, int pos){
        float[] out = new float[a.size()];
        for(int i = 0; i<a.size(); i++){
            out[i] = a.get(i)[pos];
        }
        return out;
    }

    public static float[] unpackFloatList(ArrayList<Float> a){
        float[] out = new float[a.size()];
        for(int i = 0; i<a.size(); i++)
            out[i] = a.get(i);
        return out;
    }


    public static final MatrixStack matrixStack = new MatrixStack();

    public static class MatrixStack {
        private ArrayList<float[]> matrixStack = new ArrayList<>();
        private int active = 0;
        public static final int STACK_LIMIT = 64;

        public MatrixStack() {
            matrixStack.add(new float[16]);
            Matrix.setIdentityM(get(), 0);
        }

        public float[] get(){return matrixStack.get(active);}

        public void pushMatrix() {
            active ++;
            if(active > STACK_LIMIT) throw new StackOverflowError("Attempt to push many matricies! Did you forget to pop from the stack?");
            if(matrixStack.size() <= active){ //create new matrix if get() would be null
                matrixStack.add(new float[16]);
            }
            System.arraycopy(matrixStack.get(active-1),0, get(), 0,16);
        }

        public void popMatrix() {
            active--;
        }


    }

    public static FloatBuffer toBuffer(float[] data){
        FloatBuffer dataBuffer;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                data.length * Float.BYTES);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        dataBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        dataBuffer.put(data);
        // set the buffer to read the first coordinate
        dataBuffer.position(0);
        return dataBuffer;
    }
    public static IntBuffer toBuffer(int[] data){
        IntBuffer dataBuffer;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                data.length * Integer.BYTES);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        dataBuffer = bb.asIntBuffer();
        // add the coordinates to the FloatBuffer
        dataBuffer.put(data);
        // set the buffer to read the first coordinate
        dataBuffer.position(0);
        return dataBuffer;
    }
    public static ShortBuffer toBuffer(short[] data){
        ShortBuffer dataBuffer;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                data.length * Integer.BYTES);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        dataBuffer = bb.asShortBuffer();
        // add the coordinates to the FloatBuffer
        dataBuffer.put(data);
        // set the buffer to read the first coordinate
        dataBuffer.position(0);
        return dataBuffer;
    }

    public static int loadTexture(Context context, int resID){
        Bitmap img = BitmapFactory.decodeResource(context.getResources(), resID);
        int[] texId = new int[1];
        GLES20.glGenTextures(1, texId, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img, 0);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        return texId[0];
    }
    public static int loadTexture(Context context, String assetName) throws IOException {
        AssetManager m = context.getAssets();
        InputStream in = m.open(assetName);
        Bitmap img = BitmapFactory.decodeStream(in);
        int[] texId = new int[1];
        GLES20.glGenTextures(1, texId, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img, 0);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        return texId[0];
    }

    public static class CollisionTests {
        public static boolean sphereContains(Location point, Location center, float radius){
            return distance(point, center) <= radius;
        }
        public static boolean cylinderContains(Location point, Location topCenter, float radius, float x, float y, float z, float dx, float dy, float dz){
            float[] transform = new float[16];
            float[] p = new float[]{dx, dy, dz, 1};
            Matrix.setIdentityM(transform, 0);

            float rotXY = (float)Math.atan2(dy, dx);
            Matrix.rotateM(transform, 0, -rotXY, 0,0,1); //rotate about z axis
            Matrix.multiplyMM(p, 0, transform, 0, p, 0); //p = T*p
            //changes point to be rotated

            float rotZY = (float)Math.atan2(p[1],p[2]); //atan2( z, y ) after rotation 1
            Matrix.rotateM(transform, 0, -rotZY, 1, 0, 0); //rotate about x axis

            float cylHeight = distance(0,0,0, dx, dy, dz);

            point.putPos( p ); //p now contains test point
            Matrix.multiplyMM(p, 0, transform, 0, p, 0); //rotate to test space

            //distance testPoint's xz to cyl's xz is < radius
            boolean inCircle = distance( p[0], 0, p[2], x, 0, z  ) <= radius;
            if(!inCircle) return false;
            return inRange(p[1], y, y+cylHeight); //inside height of cylinder
        }
    }
}
