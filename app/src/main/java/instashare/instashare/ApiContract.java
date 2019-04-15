package instashare.instashare;

public class ApiContract {

    public static final String baseUrl = "http://django-env.mzkdgeh5tz.us-east-1.elasticbeanstalk.com:80/";

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
}
