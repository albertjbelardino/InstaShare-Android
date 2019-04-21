package instashare.instashare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.content.ContextCompat.startActivity;

public class VolleyFactory {

    public static JSONObject sendJsonObjRequest(JSONObject jsonob,
                                                Context applicationContext, String apiUrl) {

        final JSONObject[] responseHolder = new JSONObject[1];
        responseHolder[0] = new JSONObject();
        RequestQueue rq = Volley.newRequestQueue(applicationContext);
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, apiUrl, jsonob, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                responseHolder[0] = response;
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.toString());
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
        return responseHolder[0];
    }

    public static void sendJsonArrayRequestWithJsonObject(JSONObject jsonob,
                                                          Context applicationContext, String apiUrl,
                                                          final Context callingContext, final Uri imagepath, Activity a) {

        final ProgressDialog dialog = new ProgressDialog(a); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        final JSONArray[] responseHolder = new JSONArray[1];
        final Intent[] intentHolder = new Intent[1];
        responseHolder[0] = new JSONArray();
        RequestQueue rq = Volley.newRequestQueue(applicationContext);
        dialog.show();
        final Activity finala = a;
        JsonArrayRequest jor = new JsonArrayRequest(Request.Method.POST, ApiContract.sendPicture(), jsonob, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length() == 0)
                {
                    Log.d("response_len", "response len is 0");
                }
                else {
                    Log.d("response", response.toString());
                    String[] numbers = new String[response.length()];
                    String[] names = new String[response.length()];
                    for (int x = 0; x < response.length(); x++) {
                        try {
                            numbers[x] = response.getJSONObject(x).getString("phone_number");
                            names[x] = response.getJSONObject(x).getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent i = new Intent(callingContext, GallerySendActivity.class);
                    i.putExtra("contact_names", names);
                    i.putExtra("contact_numbers", numbers);
                    i.putExtra("myimagepath", imagepath);
                    dialog.dismiss();
                    finala.finish();
                    callingContext.startActivity(i);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Log.i("Error", error.toString());
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

    public static void sendJsonArrayRequestWithJsonObject(JSONObject jsonob,
                                                          Context applicationContext, String apiUrl,
                                                          final Context callingContext, List<Uri> imagePaths) {

        final JSONArray[] responseHolder = new JSONArray[1];
        final Intent[] intentHolder = new Intent[1];
        responseHolder[0] = new JSONArray();
        RequestQueue rq = Volley.newRequestQueue(applicationContext);

        JsonArrayRequest jor = new JsonArrayRequest(Request.Method.POST, apiUrl, jsonob, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length() == 0)
                {
                    Log.d("response_len", "response len is 0");
                }
                else {
                    Log.d("response", response.toString());
                    String[] numbers = new String[response.length()];
                    String[] names = new String[response.length()];
                    for (int x = 0; x < response.length(); x++) {
                        try {
                            Log.i("phone_number_response", response.getJSONObject(x).getString("phone_number"));
                            Log.i("name_response", response.getJSONObject(x).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    /*
                    Intent i = new Intent(callingContext, ChooseSendActivity.class);
                    i.putExtra("contact_names", names);
                    i.putExtra("contact_numbers", numbers);
                    i.putExtra("myimagepath", imagepath);
                    dialog.dismiss();
                    finala.finish();
                    callingContext.startActivity(i);*/
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.toString());
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

