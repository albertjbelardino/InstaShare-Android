package instashare.instashare;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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

                    RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
                    final Map<String, String> data = new HashMap<String, String>();
                        data.put("username", textUsername.getText().toString());
                        data.put("password", textPassword.getText().toString());
                        data.put("email", textEmailAddress.getText().toString());
                        data.put("first_name", textFirst.getText().toString());
                        data.put("last_name", textLast.getText().toString());
                        data.put("phone_number", textPhone.getText().toString());


                    Log.d("INFO", data.toString());


                    JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST,"http://10.110.32.66:8000/api/register/", new JSONObject(data), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("response", response.toString());
                            try {
                                response.getString("id");
                                finish();
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "BAD SIGNUP", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ERROR", error.toString());
                        }
                    });
                    //{
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//
//                            return data;
//                        }
//                    };
                    rq.add(jor);

                }
            }
        });

    }
}
