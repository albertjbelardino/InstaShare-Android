package instashare.instashare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textUsername = (EditText) findViewById(R.id.textUsername);
                EditText textEmailAddress = (EditText) findViewById(R.id.textEmailAddress);
                EditText textPassword = (EditText)  findViewById(R.id.textPassword);
                EditText textPasswordConfirm = (EditText)  findViewById(R.id.textPasswordConfirm);
                EditText textPhone = (EditText) findViewById(R.id.textPhone);
                EditText textFirst = (EditText) findViewById(R.id.TexTFirst);
                EditText textLast = (EditText) findViewById(R.id.TextLast);
                boolean test1 = textUsername.getText().toString().trim().length()==0;
                boolean test2 = textEmailAddress.getText().toString().trim().length()==0;
                //asndasndjkla
                boolean test3 = textPassword.getText().toString().trim().length()==0;
                boolean test4 = textPasswordConfirm.getText().toString().trim().length()==0;
                boolean test5 = textPhone.getText().toString().trim().length()==0;
                boolean test6 = (textFirst.getText().toString().trim().length()==0||textLast.getText().toString().trim().length()==0);
                boolean test7 = (!textPassword.getText().toString().equals(textPasswordConfirm.getText().toString()));

                System.out.println(textPassword.getText().toString());
                if (test1||test2||test3||test4||test5||test6){
                    Toast.makeText(SignUpActivity.this, "All elements must be filled.", Toast.LENGTH_SHORT).show();
                }
                else if(test7){
                    Toast.makeText(SignUpActivity.this, "Password and password confirmation must match.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Hello "+textUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
