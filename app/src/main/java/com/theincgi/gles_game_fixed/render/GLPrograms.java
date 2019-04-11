package com.theincgi.gles_game_fixed.render;

import android.content.Context;


import com.theincgi.gles_game_fixed.utils.Pair;

import java.util.HashMap;
import java.util.WeakHashMap;
import com.theincgi.gles_game_fixed.R;


public class GLPrograms {
    private static WeakHashMap<String, GLProgram> materialLookup = new WeakHashMap<>();
    private static HashMap<String, Pair<Integer, Integer>> sourceLookup = new HashMap<>();
    private static Context context;
    static boolean init = false;
    private static GLProgram DEFAULT_PROGRAM;
    private GLPrograms(){}

    public static void setContext(Context context){
        GLPrograms.context = context;
    }

    public static void init(){
        if(init)return;
        register("DEFAULT", R.raw.default_vertex, R.raw.default_fragment);
        register( "fancy", R.raw.fancy_vertex, R.raw.fancy_fragment);
        DEFAULT_PROGRAM = get("DEFAULT");
        init = true;
    }

    public static void register(String name, int vertexShader_src_id, int fragmentShader_src_id){
        sourceLookup.put(name, new Pair<Integer, Integer>(vertexShader_src_id, fragmentShader_src_id));
    }


    /**
     * Call setContext before using this if it has not already been done
     * may return the default material if the requested material could not load
     * An error will be logged if it can not load
     * */
    public static GLProgram get(String key){
        if(!materialLookup.containsKey(key)) { //compute if absent requires function which is api level 28
            GLProgram tmp = null;
            Pair<Integer, Integer> shaders = sourceLookup.get( key );
            if(shaders == null) return DEFAULT_PROGRAM;
            try {
                tmp = new GLProgram(context, shaders.first, shaders.second);
                materialLookup.put(key, tmp);
            }catch (Throwable e){
                throw new RuntimeException( e );
            }
            return tmp==null?DEFAULT_PROGRAM:tmp;
        }
        return materialLookup.get(key);
    }

    public static GLProgram getDefault(){
        if (DEFAULT_PROGRAM == null) {
            throw new RuntimeException("Default Program is null!");
        }
        return DEFAULT_PROGRAM;
    }

}
