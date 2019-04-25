package instashare.instashare;

public class TwilioSingleton {

    private static TwilioSingleton instance;

    private TwilioSingleton() {

    }

    public static TwilioSingleton getInstance() {
        if(instance == null) {
            instance = new TwilioSingleton();
        }
        return instance;
    }
}
