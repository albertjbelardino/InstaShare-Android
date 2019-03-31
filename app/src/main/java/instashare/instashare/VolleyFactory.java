package instashare.instashare;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
}

