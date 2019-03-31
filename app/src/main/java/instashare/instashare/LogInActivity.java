package instashare.instashare;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        LoginService.logout();
        Log.i("HEY_THERE", LoginService.jwt_token);

        final Button loginButton = findViewById(R.id.logbutton);
        final Activity a = this;
        loginButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   final EditText usernameEditText = (EditText) findViewById(R.id.usernameLog);
                   final EditText passwordEditText = (EditText) findViewById(R.id.passwordLog);
                   CountDownLatch countDownLatch = new CountDownLatch(1);
                   LoginThread loginThread = new LoginThread(usernameEditText.getText().toString(),
                           passwordEditText.getText().toString(), "Login Thread", countDownLatch);

                   loginThread.start();
                   try {
                       countDownLatch.await();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }

                   if (!LoginService.jwt_token.equals("")) {
                       Intent i = new Intent(a, MainActivity.class);
                       startActivity(i);
                       finish();
                   } else {
                       //Looper.prepare();
                       Toast.makeText(a, "INCORRECT LOGIN INFO", Toast.LENGTH_SHORT).show();
                   }
               }
           }
        );

        Button signButton = findViewById(R.id.signButton);

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed(){
        //just do nothing right now
    }
}
