package com.theincgi.gles_game_fixed.geometry;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.theincgi.gles_game_fixed.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.WeakHashMap;

public class MaterialManager {
    private static WeakHashMap<String, MaterialLib> loadedMaterials = new WeakHashMap<>();
    private static Context context;

    private MaterialManager(){}
    public static void init(Context context) {
        MaterialManager.context = context;
    }

    public static MaterialLib get(String name){
        if(!loadedMaterials.containsKey(name)){
            loadedMaterials.put(name, load(name));
        }
        if(!loadedMaterials.containsKey(name))
            throw new RuntimeException("Missing material"+name);
        else
            return loadedMaterials.get(name);
    }

    public static final String
        NEW_MATERIAL = "newmtl",
        AMBIENT      = "Ka",
        DIFFUSE      = "Kd",
        SPECULAR_COLOR="Ks",
        EMMISIVE     = "Ke",
        SPECULAR_EXPONTENT = "Ns", //0 to 1000
        DISOLVE = "d",
        TRANSPARENCY = "Tr", //TR = 1-d
        ILLUM_MODE = "illum",
        MAP = "map_";

    private static MaterialLib load(String name){
        if(context==null) throw new RuntimeException("Context must be set first!");
        try{
            //TODO load material!
            AssetManager m = context.getAssets();
            Scanner s = new Scanner(m.open(name));
            MaterialLib lib = new MaterialLib();
            Material current = null;
            while(s.hasNext()){
                String instr = s.next();
                switch (instr){
                    case "#":
                        s.nextLine();
                        break;
                    case NEW_MATERIAL:
                        current = new Material();
                        lib.materials.put(s.next(), current);
                        break;
                    case AMBIENT:
                        current.ambient = new float[]{s.nextFloat(),s.nextFloat(),s.nextFloat()};
                        break;
                    case DIFFUSE:
                        current.diffuse = new float[]{s.nextFloat(),s.nextFloat(),s.nextFloat(), 1};
                        break;
                    case SPECULAR_COLOR:
                        current.specularColor = new float[]{s.nextFloat(),s.nextFloat(),s.nextFloat()};
                        break;
                    case EMMISIVE:
                        current.emmissive = new float[]{s.nextFloat(),s.nextFloat(),s.nextFloat()};
                        break;
                    case SPECULAR_EXPONTENT:
                        current.specularExponent = s.nextFloat();
                        break;
                    case DISOLVE:
                        current.opacity = s.nextFloat();
                        break;
                    case TRANSPARENCY:
                        current.opacity = 1-s.nextFloat();
                    case ILLUM_MODE:
                        current.illumMode = s.nextInt();
                        break;
                    case MAP+DIFFUSE:
                        current.diffuseMap = Utils.loadTexture(context, s.next());
                    default:
                        Log.w("#MATERIAL", new RuntimeException("Unhandled instruction \""+instr+"\""));
                }
            }
            return lib;

        }catch (IOException e) {
            Log.e("ModelManager", "IOException while loading", e);
            return null;
        }
    }

    public static class MaterialLib {
        HashMap<String, Material> materials = new HashMap<>();
        public Material get(String name){
            return  materials.get(name);
        }
    }

    public static class Material {
        float[] ambient,
                diffuse,
                specularColor,
                emmissive;
        float opacity = 1,specularExponent =1;
        int illumMode;
//0. Color on and Ambient off
//1. Color on and Ambient on
//2. Highlight on                  //common
//3. Reflection on and Ray trace on
//4. Transparency: Glass on, Reflection: Ray trace on
//5. Reflection: Fresnel on and Ray trace on
//6. Transparency: Refraction on, Reflection: Fresnel off and Ray trace on
//7. Transparency: Refraction on, Reflection: Fresnel on and Ray trace on
//8. Reflection on and Ray trace off
//9. Transparency: Glass on, Reflection: Ray trace off
//10. Casts shadows onto invisible surfaces
        Integer diffuseMap = null;
        Integer normalMap  = null;
        Integer specularMap = null;
    }

}
