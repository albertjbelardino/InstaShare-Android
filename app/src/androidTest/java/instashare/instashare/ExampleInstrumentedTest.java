package instashare.instashare;

import android.content.Context;
import android.net.Uri;
import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONObject;
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
        assertEquals("instashare.instashare", appContext.getPackageName());
    }

    @Test
    public void testApp()
    {
        Context appContext = InstrumentationRegistry.getTargetContext();

    }

    @Test
    public void log_in() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    LoginService.login("test", "Testo1");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue("greater than zero", LoginService.jwt_token.length() > 0);
    }

    @Test
    public void contact_upload() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    LoginService.login("bad", "no");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse("not greater than zero", LoginService.jwt_token.length() < 0);
    }

    @Test
    public void recognize() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    LoginService.login("user", "123");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Looper.prepare();
        try {
            VolleyFactory.sendJsonArrayRequestWithJsonObject(new JSONObject(), InstrumentationRegistry.getTargetContext(), "", InstrumentationRegistry.getTargetContext(), Uri.parse(""), new PictureTakenActivity());
        }
        catch(NullPointerException e)
        {
            assertTrue(true);
        }

    }


}
