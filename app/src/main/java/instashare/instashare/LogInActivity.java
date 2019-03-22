package instashare.instashare;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class LogInActivity extends AppCompatActivity {
    final String LOGGED_IN = "alkdhksadfadfsdfhst";
    final String MY_TOKEN = "sljdgbnrnkjsdfbgkjgnxfbnjkdgnjk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        Button logButton = findViewById(R.id.logbutton);
        final Activity a = this;
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText textLogUsername = (EditText) findViewById(R.id.usernameLog);
                final EditText textLogPassword = (EditText) findViewById(R.id.passwordLog);
                boolean test1 = textLogUsername.getText().toString().trim().length()==0;
                boolean test2 = textLogPassword.getText().toString().trim().length()==0;
                if(test1||test2){
                    Toast.makeText(LogInActivity.this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                }
                else{

                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                String token = LoginService.login(textLogUsername.getText().toString(), textLogPassword.getText().toString());
                                if(!token.equals(""))
                                {
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putBoolean(LOGGED_IN, true).commit();
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString(MY_TOKEN, token).commit();
                                    Intent i = new Intent(a, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else
                                {
                                    Looper.prepare();
                                    Toast.makeText(a, "INCORRECT LOGIN INFO", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    //Toast.makeText(LogInActivity.this, "Hello "+textLogUsername.getText().toString(), Toast.LENGTH_SHORT).show();



                }
            }
        });

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
