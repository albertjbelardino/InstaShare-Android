package instashare.instashare;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.util.List;

public class ApiContract {

    public static final String baseUrl = "http://django-env.mzkdgeh5tz.us-east-1.elasticbeanstalk.com:80/";
    public static final String twilioInstasharePhoneNumber = "+12672140841";
    public static final String twilioSID  = "";
    public static final String twilioAuth = "";

    public static String loginUrl() {
        return baseUrl + "api/token/";
    }

    public static void sendTwilioMessage(String recipientPhoneNumber, List<URI> imageUrls) {
        Twilio.init("", "");
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(twilioInstasharePhoneNumber),
                new com.twilio.type.PhoneNumber("+1" + recipientPhoneNumber),
                imageUrls
        ).create();
    }

    public static String contactUploadUrl() {
        return baseUrl + "api/uploadContactMobile/";
    }

    public static String registerUrl() {
        return baseUrl + "api/register/";
    }

    public static String sendPicture(){return baseUrl + "api/singlephotoMobile/";}

    public static String batchUpload(){return baseUrl + "api/batchuploadMobile/";}
}
