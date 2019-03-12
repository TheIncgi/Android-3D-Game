package com.theincgi.gles_game_fixed.geometry;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

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

    private static MaterialLib load(String name){
        try{
            //TODO load material!
            AssetManager m = context.getAssets();
            Scanner s = new Scanner(m.open(name+".mtl"));

            throw new IOException("Definitely tried to open a file. totes");
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
                specular;
        float opacity;
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
    }

}
