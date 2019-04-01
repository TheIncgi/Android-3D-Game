package com.theincgi.gles_game_fixed.render;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.theincgi.gles_game_fixed.geometry.MaterialManager;
import com.theincgi.gles_game_fixed.geometry.ModelLoader2;
import com.theincgi.gles_game_fixed.geometry.Square;
import com.theincgi.gles_game_fixed.geometry.Triangle;
import com.theincgi.gles_game_fixed.utils.Task;

import java.util.LinkedList;
import java.util.Queue;


import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
//import static android.opengl.GLES20.*;
public class CustomGLSurfaceViewRenderer implements GLSurfaceView.Renderer {
    private Context context;
    private final float[] projectionMatrix = new float[16];
    /**Model View Projection matrix*/
    private final float[] mvpm = new float[16];
    private float fov = 100f;
    private float near = .1f;
    private float far  = 100f;
    Camera camera;
    private ModelLoader2 modelLoader;

    private Queue<Task> tasks = new LinkedList<>();
    private LinkedList<IRenderable> renderables = new LinkedList<>();

    public CustomGLSurfaceViewRenderer(Context context) {
        this.context = context;
        camera = new Camera(0,-3,-3, 0, 45, 0);
        modelLoader = new ModelLoader2(context );
        MaterialManager.init(context);
    }

    Triangle triangle;
    Square square;
    //ModelLoader2.Model cube;
    ModelLoader2.Model model;
    /*
     * Use this method to perform actions that need to happen only once, such as
     * setting OpenGL environment parameters or initializing OpenGL graphic objects.
     * */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (((EGL10) EGLContext.getEGL()).eglGetCurrentContext().equals(EGL10.EGL_NO_CONTEXT)) {
            // no current context.
            Log.w("#GLES", "No GLES CONTEXT!");
        }
        Log.d("#GLES", "registering GLES programs");
        GLPrograms.setContext(context);
        GLPrograms.init();

        Log.d("#GLES", "programs have been registered");

        GLES20.glClearColor( 0.4f, 0.5f, 0.75f, 1.0f); //Sky blue
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        float aspect = width/(float)height;
        Matrix.perspectiveM(projectionMatrix, 0, fov, aspect, near, far);
        Matrix.translateM(projectionMatrix, 0, 0,0,0);
        triangle = new Triangle();
        square   = new Square();
        //cube = modelLoader.load("cube");
        model = modelLoader.load("colorcube");
        model.setProgram(GLPrograms.getDefault());
//        // make adjustments for screen ratio
//        float ratio = (float) width / height;
//        gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
//        gl.glLoadIdentity();                        // reset the matrix to its default state
//        gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);  // apply the projection matrix
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        long time = SystemClock.uptimeMillis();

        synchronized (tasks) {
            while (!tasks.isEmpty())
                tasks.poll().run();
        }

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);

        triangle.color.setFromHSV(time/10f%360, 1, 1, 1);
        //camera.location.setRoll(time/10f%360);



        Matrix.multiplyMM(mvpm, 0,
                projectionMatrix, 0,
                camera.getMatrix(), 0);

        synchronized (renderables){
            for (IRenderable renderable : renderables) {
                renderable.draw(mvpm);
            }
        }
        triangle.draw(mvpm);
        //square.draw(mvpm);
       // cube.getLocation().rotate( System.currentTimeMillis()/1000, 0 ,0);
        model.draw(mvpm);

    }

    public void addTask( Task task){
        synchronized (tasks){
            tasks.offer(task);
        }
    }
    public void addRenderable(IRenderable renderable){
        synchronized (renderables){
            renderables.addLast(renderable);
        }
    }



}
