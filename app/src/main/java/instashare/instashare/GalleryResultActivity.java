package instashare.instashare;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GalleryResultActivity extends AppCompatActivity {

    String galleryImagePath;
    Button sendImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_result);

        setBackgroundImage();
        setSendImageButton();
    }

    private void setBackgroundImage() {
        galleryImagePath = getIntent().getStringExtra("galleryImagePath");
        ((ImageView) findViewById(R.id.gallery_picture))
                .setImageBitmap(BitmapFactory.decodeFile(galleryImagePath));
    }

    private void setSendImageButton() {
        sendImageButton = (Button) findViewById(R.id.sendImageButton);
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Map<String, String> data = new HashMap<String, String>();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] b = baos.toByteArray();
                try {
                    BitmapFactory.decodeFile(galleryImagePath)
                            .compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    b = baos.toByteArray();
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final String sfString = Base64.encodeToString(b, Base64.DEFAULT);

                data.put("base_64", sfString);
                JSONObject jsonob = new JSONObject(data);
                VolleyFactory.sendJsonObjRequest(jsonob, getApplicationContext(), ApiContract.sendPicture());
                //sendVolleyRequest(jsonob);
            }
        });
    }

    private void sendVolleyRequest(JSONObject jsonob) {

        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, ApiContract.sendPicture(), jsonob, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " +
                        LoginService.jwt_token);
                return headers;
            }
        };
        jor.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jor);
    }
}