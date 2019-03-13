package com.theincgi.gles_game_fixed.geometry;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.util.Log;


import com.theincgi.gles_game_fixed.render.GLProgram;
import com.theincgi.gles_game_fixed.render.GLPrograms;
import com.theincgi.gles_game_fixed.utils.Color;
import com.theincgi.gles_game_fixed.utils.GLErrorLogger;
import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ModelLoader2 {
    private Context context;
    public ModelLoader2(Context context) {
        this.context = context;
    }
    private static final String
            LOAD_MAT_LIB = "mtllib",
            SET_OBJ      = "o",
            ADD_VERTEX   = "v",
            ADD_UV       = "vt",
            ADD_NORMAL   = "vn",
            USE_MATERIAL = "usemtl",
            SMOOTH_SHADING = "s",
            ADD_FACE     = "f",
            COMMENT      = "#";
    public Model load(String objName){
        try {
            AssetManager m = context.getAssets();
            Scanner scanner = new Scanner(m.open(objName + ".obj"));
            MaterialManager.MaterialLib materialLib = null;
            try {
                materialLib = MaterialManager.get(objName);
            }catch (Exception e){
                Log.e("#MODELLOADER", "No material lib for " +objName+".mtl" , e);
            }
            ArrayList<ModelObject> mObjs = new ArrayList<>();


            while(scanner.hasNext()){
                String instruction = scanner.next();
                switch (instruction){
                    case COMMENT:
                        scanner.nextLine(); //comment ignored
                        break;
                    case LOAD_MAT_LIB:
                        scanner.nextLine();
                        break;
                    case SET_OBJ:
                        while(scanner.hasNextLine()) { //all remaining contents are objects
                           ModelObject obj = parseModelObject(scanner, materialLib);
                           mObjs.add(obj);
                        }
                        break;
                    default:
                        Log.w("#MODELLOADER", "Unexpected instruction \""+instruction+"\"", new RuntimeException());
                }
            }
            ModelObject[] objs = new ModelObject[mObjs.size()];
            for(int i = 0; i<objs.length; i++){
                objs[i] = mObjs.get(i);
            }
            Model model = new Model(materialLib, objs);
            return model;

        }catch (IOException e){
            Log.e("#MODELLOADER", "Unable to load model \""+objName+"\"", e);
            return null;
        }
    }
    private ModelObject parseModelObject(Scanner scanner, MaterialManager.MaterialLib lib){
        ArrayList<Float> vert = new ArrayList<>();
        ArrayList<Float> uv   = new ArrayList<>();
        ArrayList<Float> norm = new ArrayList<>();

        String name = scanner.nextLine();
        MaterialManager.Material currentMaterial = null;
        HashMap<MaterialManager.Material, ArrayList<int[]>> tempGroups = new HashMap<>();
        MaterialGroup.Type faceType = MaterialGroup.Type.VERTEX_ONLY;

        while(scanner.hasNext()){
            String instruction = scanner.next();
            switch (instruction){
                case USE_MATERIAL: {
                    String matName = scanner.next();
                    currentMaterial = lib.get(matName);
                    break;
                }
                case ADD_VERTEX:
                    vert.add(scanner.nextFloat());
                    vert.add(scanner.nextFloat());
                    vert.add(scanner.nextFloat());
                    break;
                case ADD_UV:
                    uv.add(scanner.nextFloat());
                    uv.add(scanner.nextFloat());
                    break;
                case ADD_NORMAL:
                    norm.add(scanner.nextFloat());
                    norm.add(scanner.nextFloat());
                    norm.add(scanner.nextFloat());
                    break;
                case ADD_FACE: {
                    for (int i = 0; i < 3; i++) {
                        String data = scanner.next();
                        Scanner tmp = new Scanner(data.replaceAll("/"," "));
                        int[] face = null;
                        switch (Utils.countChar(data, '/')) { // v/vt/vn
                            case 0: //vertex only
                                faceType = MaterialGroup.Type.VERTEX_ONLY;
                                face = new int[]{tmp.nextInt()};
                                break;
                            case 1: //

                                    faceType = MaterialGroup.Type.VERTEX_AND_TEXTURE;


                                face = new int[]{tmp.nextInt(), tmp.nextInt()};
                                break;
                            case 2:
                                if (data.indexOf("//") >= 0) {
                                    faceType = MaterialGroup.Type.VERTEX_AND_NORMAL;
                                    face = new int[] {tmp.nextInt(), tmp.nextInt()};
                                }else {
                                    faceType = MaterialGroup.Type.ALL;
                                    face = new int[]{tmp.nextInt(), tmp.nextInt(), tmp.nextInt()};
                                }
                                break;
                            default:
                                throw new RuntimeException("Too many points in face index");
                        }
                        if (!tempGroups.containsKey(currentMaterial))
                            tempGroups.put(currentMaterial, new ArrayList<int[]>());
                        tempGroups.get(currentMaterial).add(face);
                    }
                }
            }
        }
        ModelObject obj = new ModelObject();
        obj.groups = new MaterialGroup[tempGroups.keySet().size()];
        obj.vCoords = Utils.toBuffer(Utils.unpackFloatList(vert));
        obj.nCoords = Utils.toBuffer(Utils.unpackFloatList(norm));
        obj.tCoords = Utils.toBuffer(Utils.unpackFloatList( uv ));

        int groupNum = 0;
        for (Map.Entry<MaterialManager.Material, ArrayList<int[]>> entry: tempGroups.entrySet() ) {
            MaterialGroup mg = (obj.groups[groupNum++] = new MaterialGroup());
            mg.type = faceType;
            mg.iv = Utils.toBuffer( Utils.unpackIntList(entry.getValue(),0) );
            if(faceType.equals(MaterialGroup.Type.ALL)) {
                mg.in = Utils.toBuffer(Utils.unpackIntList(entry.getValue(), 2));
                mg.it = Utils.toBuffer(Utils.unpackIntList(entry.getValue(), 1));
            }else if(faceType.equals(MaterialGroup.Type.VERTEX_AND_TEXTURE)){
                mg.it = Utils.toBuffer(Utils.unpackIntList(entry.getValue(), 1));
            }else if(faceType.equals(MaterialGroup.Type.VERTEX_AND_NORMAL)){
                mg.in = Utils.toBuffer(Utils.unpackIntList(entry.getValue(), 1));
            }
            mg.material = entry.getKey();
            mg.points = entry.getValue().size()/3;
        }
        return obj;
    }


    public static class Model {
        MaterialManager.MaterialLib materialLib;
        Location location;
        ModelObject[] modelObjects;
        GLProgram program = GLPrograms.getDefault();

        public Model(MaterialManager.MaterialLib materialLib, ModelObject... modelObjects) {
            this.materialLib = materialLib;
            this.modelObjects = modelObjects;
            location = new Location();
        }

        public Location getLocation() {
            return location;
        }

        public GLProgram getProgram() {
            return program;
        }

        public void setProgram(GLProgram program) {
            this.program = program;
        }

        public void draw(float[] mvpm){
            Utils.matrixStack.pushMatrix();
            location.applyToStack();
            program.use();
            for (ModelObject obj : modelObjects) {
                obj.draw(mvpm, program);
            }
            Utils.matrixStack.popMatrix();
        }
        /*
        glVertexPointer(3, GL_FLOAT, 0, vertices);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, indices);*/
    }
    public static class ModelObject {
        String name;
        FloatBuffer vCoords, tCoords, nCoords;
        MaterialGroup[]  groups;

        boolean smoothShading = false;

        public void draw(float[] mvpm, GLProgram program){
            int posH   = program.getAttribLocation("vPosition"); //positionHandle

            int mvpmH  = program.getUniformLocation("mvpm");

            GLES20.glEnableVertexAttribArray(posH);
            GLES20.glVertexAttribPointer(posH, 3, GLES20.GL_FLOAT,
                        false, 3*Float.SIZE, vCoords);
            GLES20.glUniformMatrix4fv(mvpmH, 1, false, mvpm,0 );
            GLErrorLogger.check();
            for(MaterialGroup materialGroup : groups){

                materialGroup.draw(program);
            }

            GLES20.glDisableVertexAttribArray(posH);
        }
    }

    /**Collection of faces using the same material*/
    public static class MaterialGroup {
        MaterialManager.Material material;
        IntBuffer   iv, it, in;
        Type type;
        int points;

        public void draw(GLProgram program){
            GLErrorLogger.check();
            int colorH = program.getUniformAttribLocation("vColor");
            GLErrorLogger.check();
            GLES20.glUniform4fv(colorH, 1, material.diffuse, 0);
            GLErrorLogger.check();
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, points, GLES20.GL_UNSIGNED_SHORT, iv);
            GLErrorLogger.check();
        }

        public enum Type {
            VERTEX_ONLY,
            VERTEX_AND_TEXTURE,
            VERTEX_AND_NORMAL,
            ALL;
        }
    }
}
