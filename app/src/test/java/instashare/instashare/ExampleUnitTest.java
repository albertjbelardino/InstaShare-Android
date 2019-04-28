package instashare.instashare;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void checkAPIContract()
    {
        assertEquals("http://django-env.mzkdgeh5tz.us-east-1.elasticbeanstalk.com:80/api/singlephotoMobile/", ApiContract.sendPicture());
    }

    @Test
    public void checkJwtTokenSave()
    {
        LoginService.jwt_token = "customtoken";
        assertEquals("customtoken", LoginService.jwt_token);
    }





}