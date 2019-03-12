package com.theincgi.gles_game_fixed.geometry;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;


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
            ArrayList<ModelObject> mObjs = new ArrayList<>();


            while(scanner.hasNext()){
                String instruction = scanner.next();
                switch (instruction){
                    case COMMENT:
                        scanner.nextLine(); //comment ignored
                        break;
                    case LOAD_MAT_LIB:
                        materialLib = MaterialManager.get(scanner.nextLine());
                        break;
                    case SET_OBJ:
                        while(scanner.hasNextLine()) { //all remaining contents are objects
                           ModelObject obj = parseModelObject(scanner, materialLib);
                           mObjs.add(obj);
                        }
                    default:
                        Log.w("#MODELLOADER", "Unexpected instruction \""+instruction+"\"");
                }
            }
            ModelObject[] objs = new ModelObject[mObjs.size()];
            for(int i = 0; i<objs.length; i++){
                objs[i] = mObjs.get(i);
            }
            Model model = new Model(materialLib, objs);
            return null; //TODO

        }catch (IOException e){
            Log.e("#MODELLOADER", "Unable to load model \""+objName+"\"", e);
            return null;
        }
    }
    private ModelObject parseModelObject(Scanner scanner, MaterialManager.MaterialLib lib){
        ArrayList<float[]> vert = new ArrayList<>();
        ArrayList<float[]> uv   = new ArrayList<>();
        ArrayList<float[]> norm = new ArrayList<>();

        String name = scanner.nextLine();
        MaterialManager.Material currentMaterial = null;
        HashMap<MaterialManager.Material, ArrayList<int[]>> tempGroups = new HashMap<>();
        MaterialGroup.Type faceType = MaterialGroup.Type.VERTEX_ONLY;

        while(scanner.hasNextLine()){
            String instruction = scanner.next();
            switch (instruction){
                case USE_MATERIAL:
                    currentMaterial = lib.get(scanner.next());
                    break;
                case ADD_VERTEX:
                    vert.add(new float[]{scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat()});
                    break;
                case ADD_UV:
                    uv.add(new float[]{scanner.nextFloat(), scanner.nextFloat()});
                    break;
                case ADD_NORMAL:
                    norm.add(new float[]{scanner.nextFloat(), scanner.nextFloat(), scanner.nextFloat()});
                    break;
                case ADD_FACE: {
                    String data = scanner.nextLine();
                    int[] face = null;
                    switch (Utils.countChar(data,'/')){ // v/vt/vn
                        case 0: //vertex only
                            faceType = MaterialGroup.Type.VERTEX_ONLY;
                            face = new int[]{scanner.nextInt()};
                            break;
                        case 1: //
                            if(data.indexOf("//")>=0){
                                faceType = MaterialGroup.Type.VERTEX_AND_NORMAL;
                            }else{
                                faceType = MaterialGroup.Type.VERTEX_AND_TEXTURE;
                            }
                            face = new int[]{scanner.nextInt(), scanner.nextInt()};
                            break;
                        case 2:
                            faceType = MaterialGroup.Type.ALL;
                            face = new int[]{scanner.nextInt(), scanner.nextInt(), scanner.nextInt()};
                            break;
                        default:
                            throw new RuntimeException("Too many points in face index");
                    }
                    if(!tempGroups.containsKey(currentMaterial))
                        tempGroups.put(currentMaterial, new ArrayList<int[]>());
                    tempGroups.get(currentMaterial).add(face);
                }
            }
        }
        ModelObject obj = new ModelObject();
        obj.groups = new MaterialGroup[tempGroups.keySet().size()];
        int groupNum = 0;
        for (Map.Entry<MaterialManager.Material, ArrayList<int[]>> entry: tempGroups.entrySet() ) {
            MaterialGroup mg = obj.groups[groupNum++];
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
        }
        return obj;
    }


    public static class Model {
        MaterialManager.MaterialLib materialLib;
        Location location;
        ModelObject[] modelObjects;


        public Model(MaterialManager.MaterialLib materialLib, ModelObject... modelObjects) {
            this.materialLib = materialLib;
            this.modelObjects = modelObjects;
            location = new Location();
        }

        public void draw(){}
        /*
        glVertexPointer(3, GL_FLOAT, 0, vertices);
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, indices);*/
    }
    public static class ModelObject {
        String name;
        FloatBuffer vCoords, tCoords, nCoords;
        MaterialGroup[]  groups;
        boolean smoothShading = false;
    }

    /**Collection of faces using the same material*/
    public static class MaterialGroup {
        MaterialManager.Material material;
        IntBuffer   iv, it, in;
        Type type;

        public enum Type {
            VERTEX_ONLY,
            VERTEX_AND_TEXTURE,
            VERTEX_AND_NORMAL,
            ALL;
        }
    }
}
