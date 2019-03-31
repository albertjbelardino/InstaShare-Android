package instashare.instashare;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Telephony;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.klinker.android.send_message.Message;
import com.klinker.android.send_message.Settings;
import com.klinker.android.send_message.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PictureTakenActivity extends AppCompatActivity {

    String imagepath;
    ImageView iv;
    Button sendimagebutton;
    final String MY_TOKEN = "sljdgbnrnkjsdfbgkjgnxfbnjkdgnjk";
    PopupWindow popupWindow;
    Bitmap myimage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_taken);
        imagepath = getIntent().getStringExtra("myimage");
        iv = findViewById(R.id.myPicture);
        final Bitmap bm = BitmapFactory.decodeFile(imagepath);
        myimage = bm;
        popupWindow  = new PopupWindow(this);


        iv.setImageBitmap(bm);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String sfString = Base64.encodeToString(b, Base64.DEFAULT);

        sendimagebutton = findViewById(R.id.InstashareButton);
        final ProgressDialog dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        sendimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImage(new String[]{"8566026807", "2159419203"});
                dialog.show();

                final Map<String, String> data = new HashMap<String, String>();
                data.put("base_64", sfString);
                Log.d("INFO", data.toString());
                Log.d("MY TOKEN", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(MY_TOKEN, "this is not a token"));
                JSONObject jsonob = new JSONObject(data);

                RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
                    JsonArrayRequest jor = new JsonArrayRequest(Request.Method.POST, ApiContract.sendPicture(), new JSONObject(data), new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("response", response.toString());
//                        TextView tv = new TextView(getApplicationContext());
//                        try {
//                            tv.setText("Match: " + response.getString("first_name") + " " + response.getString("last_name"));
//                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                                    LinearLayout.LayoutParams.WRAP_CONTENT);
//
//                            tv.setTextSize(30);
//                            tv.setBackgroundColor(Color.WHITE);
//                            LinearLayout ll = new LinearLayout(getApplicationContext());
//                            ll.setOrientation(LinearLayout.VERTICAL);
//                            ll.addView(tv, lp);
//                            PopupWindow puw = new PopupWindow(getApplicationContext());
//                            puw.setAnimationStyle(-1);
//                            puw.setContentView(ll);
//                            puw.setFocusable(true);
//                            dialog.dismiss();
//                            puw.showAtLocation(iv, Gravity.CENTER, 0, 0);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            displayErrorMessage();
                            Log.d("ERROR", error.toString());
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {

                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", "Bearer " +
                                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(MY_TOKEN, "this is not a token"));
                            return headers;
                        }
                    };
                    jor.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    rq.add(jor);
                }
                //{
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//
//                            return data;
//                        }
//                    };



        });
    }


    public void displayErrorMessage()
    {
        TextView tv = new TextView(getApplicationContext());
            tv.setText("NO MATCH FOUND");
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            tv.setTextSize(30);
            tv.setBackgroundColor(Color.WHITE);
            LinearLayout ll = new LinearLayout(getApplicationContext());
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(tv, lp);
            PopupWindow puw = new PopupWindow(getApplicationContext());
            puw.setAnimationStyle(-1);
            puw.setContentView(ll);
            puw.setFocusable(true);
            puw.showAtLocation(iv, Gravity.CENTER, 0, 0);
    }

    public void sendImage(String[] phonenumbers)
    {
        String sendout = "";
        for(String s: phonenumbers)
        {
            sendout = sendout + s + ",";
        }
        sendout = sendout.substring(0, sendout.length() - 1);
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.putExtra("sms_body", "Sent from Instashare.");
        sendIntent.putExtra("address", sendout);
        sendIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(imagepath)));
        sendIntent.setType("image/jpeg");
        startActivity(sendIntent);
    }
}
