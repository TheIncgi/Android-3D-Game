package com.theincgi.gles_game_fixed;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.theincgi.gles_game_fixed.utils.Location;
import com.theincgi.gles_game_fixed.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.theincgi.gles_game_fixed", appContext.getPackageName());
    }

    @Test
    public void sphereCheck(){
        assertTrue(Utils.CollisionTests.sphereContains(new float[]{0,0,0}, 0,0,0, 1));
        assertTrue(Utils.CollisionTests.sphereContains(new float[]{0.28f,0.54f,0.28f}, 0,0,0, 1));
        assertFalse(Utils.CollisionTests.sphereContains(new float[]{0.28f,2.54f,0.28f}, 0,0,0, 1));
    }

    @Test
    public void cylCheck(){
        Location t = new Location(0, 1, 0);
        assertTrue( Utils.CollisionTests.cylinderContains(new float[]{0, .5f, 0, 1}, t, 1, 0, -1, 0) );
        t.setPos(0, 4, 0);
        assertTrue( Utils.CollisionTests.cylinderContains(new float[]{0, 4, .8f, 1}, t, 1, 0, 0, 1) );
    }
}
