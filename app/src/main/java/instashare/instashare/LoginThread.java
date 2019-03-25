package instashare.instashare;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class LoginThread extends Thread {

    private String username;
    private String password;
    private CountDownLatch latch;

    public LoginThread(String username, String password, String name, CountDownLatch latch) {
        super(name);
        this.username = username;
        this.password = password;
        this.latch = latch;
    }

    @Override
    public void run(){
        try {
            LoginService.login(username, password);
            latch.countDown();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
