package com.theincgi.gles_game_fixed.geometry;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;

import com.theincgi.gles_game_fixed.render.GLProgram;
import com.theincgi.gles_game_fixed.render.GLPrograms;
import com.theincgi.gles_game_fixed.utils.GLErrorLogger;
import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.WeakHashMap;

public class ModelLoader3 {
    WeakHashMap<String, Model> loadedModels = new WeakHashMap<>();
    private static ModelLoader3 instance;
    private Context context;

    public static void init(Context context) {
        instance().context = context;
    }
    public static ModelLoader3 instance(){
        return instance==null? instance=new ModelLoader3():instance;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void load(String objName) throws IOException {
        AssetManager m = context.getAssets();
        InputStream in =m.open( objName + ".mdl" );
        MaterialManager.MaterialLib materialLib;
        ModelObject currentObject;


        /*
        MATERIAL_LIB,
		VERTEX,
		OBJ_DEF,
		NORMAL,
		FACE,
		USE_MAT,
		SMOOTH_SHADING,
		UV_COORD;
        * */
        while( in.available() > 0 ){
            String chunk = readUtf( in );
            switch (chunk){
                case "MATERIAL_LIB":{
                    materialLib = MaterialManager.get(readUtf(in));
                    break;
                }
                case "OBJ_DEF":{
                    currentObject = new ModelObject();
                    currentObject.name = readUtf( in );

                    break;
                }
                case "VERTEX":{

                    break;
                }
                case "NORMAL":{

                    break;
                }
                case "UV_COORD":{

                }
            }
        }
    }

    private int readShort(InputStream in) throws IOException{
        int out = in.read() << 8;
        out |= in.read();
        return out;
    }
    private String readUtf( InputStream in )throws IOException{
        int bytes = readShort( in );
        byte[] data = new byte[ bytes ];
        in.read( data );
        return new String(data);
    }
    private int readInt( InputStream in ) throws IOException{
        return
                in.read() << 24 |
                in.read() << 16 |
                in.read() << 8  |
                in.read();
    }
    private double readFloat(InputStream in) throws IOException{
        int bits = readInt( in );
        return Float.intBitsToFloat(bits);
    }






    //~~~~~~~~~~~~~~~
    //data containers

    public static class Model {
        ArrayList<ModelObject> objects;
        GLProgram program = GLPrograms.getDefault();
        public void drawAll(float[] mvpm, Location at){
            program.use();
            for (int i = 0; i < objects.size(); i++) {
                drawObj(mvpm, at, i);
                GLErrorLogger.check();

            }
        }
        public void drawObj(float[] mvpm, Location at, int objNum){
            Utils.matrixStack.pushMatrix();
            objects.get(objNum).draw(mvpm, at, program);
            GLErrorLogger.check();
            Utils.matrixStack.popMatrix();
        }
        public int numObjs() {
            return objects.size();
        }
    }

    private static class ModelObject {
        MaterialGroup[] materials;
        String name;
        boolean smoothShading = false;

        FloatBuffer vCoords, tCoords, nCoords;
        public void draw(float[] mvpm, Location at, GLProgram program){
            at.applyToStack();
            int posH = program.getAttribLocation("vPosition");
            int projectionH  = program.getUniformLocation("projectionMatrix");
            int modelH       = program.getUniformLocation("modelMatrix");


            GLES20.glEnableVertexAttribArray(posH);
            GLES20.glVertexAttribPointer(
                    posH,
                    3,//COORDS_PER_VERTEX,
                    GLES20.GL_FLOAT,
                    false,
                    3*Float.BYTES,//COORDS_PER_VERTEX*Float.BYTES,
                    vCoords);

            GLErrorLogger.check();
            GLES20.glUniformMatrix4fv(projectionH, 1, false, mvpm,0 );
            GLErrorLogger.check();
            GLES20.glUniformMatrix4fv(modelH, 1, false, model,0 );
            GLErrorLogger.check();
            for(MaterialGroup materialGroup : materials){
                materialGroup.draw(program);
            }

            GLES20.glDisableVertexAttribArray(posH);
            GLErrorLogger.check();

        }
    }

    private static class MaterialGroup {
        MaterialManager.Material material;
        IntBuffer iv, it, in;
        int points;


        public void draw(GLProgram program ){}

    }
}
