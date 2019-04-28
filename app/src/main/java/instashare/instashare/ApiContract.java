package instashare.instashare;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.util.List;

public class ApiContract {

    public static final String baseUrl = "http://django-env.mzkdgeh5tz.us-east-1.elasticbeanstalk.com:80/";
    public static final String twilioInstasharePhoneNumber = "+12672140841";

    public static String loginUrl() {
        return baseUrl + "api/token/";
    }

    public static String contactUploadUrl() {
        return baseUrl + "api/uploadContactMobile/";
    }

    public static String registerUrl() {
        return baseUrl + "api/register/";
    }

    public static String sendPicture(){return baseUrl + "api/singlephotoMobile/";}

    public static String batchUpload(){return baseUrl + "api/batchuploadAndroid/";}

    public static String ngrokUrl() { return "http://0c6acb83.ngrok.io/mms";}
}
