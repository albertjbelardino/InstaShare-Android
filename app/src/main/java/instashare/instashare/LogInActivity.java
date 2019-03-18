package instashare.instashare;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {
    final String LOGGED_IN = "alkdhksadfadfsdfhst";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        Button logButton = findViewById(R.id.logbutton);
        final Activity a = this;
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textLogUsername = (EditText) findViewById(R.id.usernameLog);
                EditText textLogPassword = (EditText) findViewById(R.id.passwordLog);
                boolean test1 = textLogUsername.getText().toString().trim().length()==0;
                boolean test2 = textLogPassword.getText().toString().trim().length()==0;
                if(test1||test2){
                    Toast.makeText(LogInActivity.this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(LogInActivity.this, "Hello "+textLogUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                    PreferenceManager.getDefaultSharedPreferences(a).edit().putBoolean(LOGGED_IN, true).commit();
                    Intent i = new Intent(a, MainActivity.class);
                    startActivity(i);
                    finish();


                }
            }
        });
    }

    public void onBackPressed(){
        //just do nothing right now
    }
}
