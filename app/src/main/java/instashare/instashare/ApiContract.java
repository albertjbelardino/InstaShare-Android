package instashare.instashare;

public class ApiContract {

    public static final String baseUrl = "http://10.108.40.48:8000/";

    public static String loginUrl() {
        return baseUrl + "api/token/";
    }

    public static String contactUploadUrl() {
        return baseUrl + "api/uploadContact64/";
    }

    public static String registerUrl() {
        return baseUrl + "api/register/";
    }

    public static String sendPicture(){return baseUrl + "api/demo64/";}
}
