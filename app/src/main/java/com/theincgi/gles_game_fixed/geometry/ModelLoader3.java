package com.theincgi.gles_game_fixed.geometry;

import android.content.Context;
import android.content.res.AssetManager;
import android.nfc.FormatException;
import android.opengl.GLES20;
import android.util.Log;

import com.theincgi.gles_game_fixed.render.Camera;
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
        instance().loadedModels.clear();
        instance().context = context;
    }
    public static ModelLoader3 instance(){
        return instance==null? instance=new ModelLoader3():instance;
    }

    public static Model get(String name){
        ModelLoader3 inst = instance();
        if(!inst.loadedModels.containsKey( name ))
            try {
                inst.load(name);
            }catch(IOException e){
                throw new RuntimeException( e );
            }
        return inst.loadedModels.get( name );
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void load(String objName) throws IOException {
        long start = System.currentTimeMillis();
        AssetManager m = context.getAssets();
        InputStream in =m.open( objName + ".mdl" );
        Model model = new Model();
        model.modelName = objName;
        FloatBuffer vCoords, tCoords, nCoords;

        ModelObject currentObject = null;
        MaterialGroup currentMaterialGroup = null;
        Integer vertCount = null;
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
                    model.materialLib = MaterialManager.get(readUtf(in));
                    break;
                }
                case "OBJ_DEF":{
                    model.objects.add( currentObject = new ModelObject() );
                    currentObject.name = readUtf( in );
                    break;
                }
                case "VERTEX":{
                    vertCount = readInt( in );
                    model.vCoords = Utils.toBuffer( readFloatArray(in, vertCount*3) ); //TODO check me
                    break;
                }
                case "NORMAL":{
                    vertCount = readInt( in );//if(vertCount==null) throw new RuntimeException("File format is invalid, vertex count expected before normals");
                    model.nCoords = Utils.toBuffer( readFloatArray(in, vertCount*3) );
                    break;
                }
                case "UV_COORD":{
                    vertCount = readInt( in );//if(vertCount==null) throw new RuntimeException("File format is invalid, vertex count expected before normals");
                    model.tCoords = Utils.toBuffer( readFloatArray(in, vertCount*2) );
                    break;
                }
                case "USE_MAT":{
                    if(currentObject==null){
                        Log.w("#ModelLoader","Material set before object, assuming animated model");
                        model.objects.add( currentObject = new ModelObject() );
                        currentObject.name = "Frame 0?";
                    }
                    currentObject.materials.add( currentMaterialGroup = new MaterialGroup() );
                    currentMaterialGroup.material = model.materialLib.get( readUtf(in) );
                    break;
                }
                case "FACE":{
                    int numFaces = readInt( in );
                    int[] vertIndex = readIntArray( in ,numFaces*3 ); //3 verts per face
                    int[] txtrIndex = readIntArray( in, numFaces*3 ); //triangles
                    int[] normalIndx= readIntArray( in, numFaces*3 ); //trinagles everywhere!
                    currentMaterialGroup.iv = Utils.toBuffer( vertIndex );
                    if(txtrIndex[0]!=-1)
                        currentMaterialGroup.it = Utils.toBuffer( txtrIndex );
                    if(normalIndx[0]!=-1)
                        currentMaterialGroup.in = Utils.toBuffer( normalIndx );
                    break;
                }
                case "SMOOTH_SHADING":{
                    currentMaterialGroup.smoothShading = in.read()!=0;
                    break;
                }
                default:
                    throw new RuntimeException("Missing case: " + chunk);
            }
        }
        loadedModels.put(objName, model);
        long end = System.currentTimeMillis();
        Log.i("#ModelLoader", String.format("Took %6.3f seconds to load '%s'", (end-start)/1000f, objName));
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
    private float readFloat(InputStream in) throws IOException{
        int bits = readInt( in );
        return Float.intBitsToFloat(bits);
    }
    private float[] readFloatArray( InputStream in, int size ) throws IOException{
        float[] floats = new float[ size ];
        for (int i = 0; i < floats.length; i++) {
            floats[i] = readFloat( in );
        }
        return floats;
    }
    private int[] readIntArray( InputStream in, int size ) throws IOException {
        int[] ints = new int[ size ];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = readInt( in );
        }
        return ints;
    }




    //~~~~~~~~~~~~~~~
    //data containers

    public static class Model {
        ArrayList<ModelObject> objects = new ArrayList<>();
        String modelName;
        GLProgram program = GLPrograms.get("fancy");
        MaterialManager.MaterialLib materialLib;
        FloatBuffer vCoords, tCoords, nCoords;

        public void drawAll(Location at){
            setup(at);
            for (int i = 0; i < objects.size(); i++) {
                _drawObj(i);
                GLErrorLogger.check();
            }
            cleanup();
        }
        public void drawObj(Location at, int objNum){
            setup(at);
            _drawObj(objNum);
            cleanup();
        }

        private void setup(Location at){
            program.use();
            Utils.matrixStack.pushMatrix();
            at.applyToStack();

            program.trySetVertexAttribArray("position", vCoords);
           // program.trySetVertexAttribArray("normal", nCoords);
            if(tCoords!=null)
                program.trySetVertexAttribArray("uv", tCoords);

            program.trySetMatrix("modelMatrix", Utils.matrixStack.get());

        }

        private void cleanup(){
            program.tryDisableVertexAttribArray("position");
            //program.tryDisableVertexAttribArray("normal");
            GLErrorLogger.check();
            Utils.matrixStack.popMatrix();
        }

        private void _drawObj(int objNum){
            //Utils.matrixStack.pushMatrix();
            objects.get(objNum).draw(program);
            GLErrorLogger.check();
            //Utils.matrixStack.popMatrix();
        }
        public int numObjs() {
            return objects.size();
        }

        public ArrayList<ModelObject> getObjects() {
            return objects;
        }
    }

    public static class ModelObject {
        ArrayList<MaterialGroup> materials = new ArrayList<>();
        String name;

        public void draw(GLProgram program){

            //GLES20.glUniformMatrix4fv(modelH, 1, false, model,0 );
            GLErrorLogger.check();
            for(MaterialGroup materialGroup : materials){
                materialGroup.draw(program);
            }
        }

        public ArrayList<MaterialGroup> getMaterials() {
            return materials;
        }
    }

    public static class MaterialGroup {
        MaterialManager.Material material;
        IntBuffer iv, it, in;
        boolean smoothShading = false;


        public void draw(GLProgram program){
            program.trySetUniform("diffuseColor", material.diffuse);
            program.trySetUniform("ambientColor", material.ambient);
            program.trySetUniform("specularColor", material.specularColor);
            program.trySetUniform("specularExponent", material.specularExponent);
            program.trySetUniform("opacity", material.opacity);


            GLES20.glDrawElements(GLES20.GL_TRIANGLES, iv.limit(), GLES20.GL_UNSIGNED_INT, iv);

        }

        public MaterialManager.Material getMaterial() {
            return material;
        }
    }
    public interface DrawableModel {
        public void draw(Camera camera);
    }
}
