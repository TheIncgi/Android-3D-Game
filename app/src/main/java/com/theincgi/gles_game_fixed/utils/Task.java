package com.theincgi.gles_game_fixed.utils;

public abstract class Task<T> {
    private boolean hasRun = false;
    private Throwable error = null;
    private T result = null;

    abstract public T run();

    public void doTask(){
        if(hasRun) throw new IllegalStateException("Task has already been run");
        try{
            result = run();
        }catch (Throwable throwable){
            error = throwable;
        }finally {
            hasRun = true;
        }
    }
    public boolean hasRun(){
        return  hasRun;
    }
    public Throwable getError(){
        return error;
    }
}
