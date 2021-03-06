package instashare.instashare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class ContactUploadService {


    public static String uploadSingleContact(String name, String number, Bitmap image, Context appContext, String id, Activity a, final boolean sendingMulitple)
    {
        final String idfinal = id;
        final Activity tempa = a;

        final ProgressDialog dialog = setUpDialog(a); // this = YourActivity


        number = number.replace(" ", "");
        number = number.replace("-", "");

        Log.i("NUMBER_JAWN", number);
        final HashMap<String, String> postJSON = new HashMap<String, String>();
        postJSON.put("name", name);
        postJSON.put("phone_number", number);
        postJSON.put("base_64", getFileToByte(image));

        RequestQueue requestQueue = Volley.newRequestQueue(appContext);
        if(!sendingMulitple) {
            dialog.show();
        }

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, ApiContract.contactUploadUrl(),
                new JSONObject(postJSON), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("UPLOAD_CONTACT_RESPONSE", response.toString());
                try {
                    tempa.getSharedPreferences(LoginService.my_username, Context.MODE_PRIVATE).edit().putString(idfinal, response.getString("id")).commit();
                    //PreferenceManager.getDefaultSharedPreferences(tempa).edit().putString(idfinal, response.getString("id")).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!sendingMulitple) {
                    dialog.dismiss();
                    Toast.makeText(tempa, "CONTACT UPLOAD SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    tempa.finish();

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(!sendingMulitple) {
                            dialog.dismiss();
                            Toast.makeText(tempa, "ERROR WITH CONTACT UPLOAD; TRY AGAIN", Toast.LENGTH_SHORT).show();
                            Log.i("ERROR", error.toString() + "\n\n\n\n\n\n\n\n" + postJSON.toString());
                            tempa.finish();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                Log.i("jwt_token", LoginService.jwt_token);
                headers.put("Authorization", "Bearer " + LoginService.jwt_token);
                return headers;
            }
        };
        jor.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jor);

        return "";
    }


    public static boolean updateSingleContact(String name, String number, Bitmap image, Context appContext, Activity a, String id, final boolean sendingMulitple)
    {

        final Activity tempa = a;

        final ProgressDialog dialog = setUpDialog(a); // this = YourActivity


        number = number.replace(" ", "");
        number = number.replace("-", "");

        Log.i("NUMBER_JAWN", number);
        final HashMap<String, String> postJSON = new HashMap<String, String>();
        postJSON.put("name", name);
        postJSON.put("phone_number", number);
        postJSON.put("base_64", getFileToByte(image));

        RequestQueue requestQueue = Volley.newRequestQueue(appContext);
        if(!sendingMulitple) {
            dialog.show();
        }

        String sendid = tempa.getSharedPreferences(LoginService.my_username, Context.MODE_PRIVATE).getString(id, "ERROR");

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.PUT, ApiContract.contactUploadUrl() + sendid + "/",
                new JSONObject(postJSON), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("UPDATE_CONTACT_RESPONSE", response.toString());
                if(!sendingMulitple) {
                    dialog.dismiss();
                    Toast.makeText(tempa, "CONTACT UPLOAD SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    tempa.finish();

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(!sendingMulitple) {
                            dialog.dismiss();
                            Toast.makeText(tempa, "ERROR WITH CONTACT UPLOAD; TRY AGAIN", Toast.LENGTH_SHORT).show();
                            Log.i("ERROR", error.toString() + "\n\n\n\n\n\n\n\n" + postJSON.toString());
                            tempa.finish();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                Log.i("jwt_token", LoginService.jwt_token);
                headers.put("Authorization", "Bearer " + LoginService.jwt_token);
                return headers;
            }
        };
        jor.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jor);

        return false;
    }

    public static boolean uploadAllContacts(ContentResolver cr, Context context, Context appContext, Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences(LoginService.my_username, Context.MODE_PRIVATE);


        Contact[] contacts = getContactList(cr, context);
        for(Contact contact : contacts) {
            if(contact != null) {
                if (contact.image != null) {
                    if(sp.getString(contact.id, "NOT UPLOADED").equals("NOT UPLOADED") && !contact.name.equals("")) {
                        Log.d("TEST", "UPLOADING NEW CONTACT");
                        uploadSingleContact(contact.name, contact.number, contact.image, appContext, contact.id, activity, true);
                    }
                    else
                    {
                        Log.d("TEST", "UPDATING OLD CONTACT - " + contact.name);
                        updateSingleContact(contact.name, contact.number, contact.image, appContext, activity, contact.id, true);
                    }
                }
            }
        }
        Toast.makeText(activity, "CONTACT UPLOAD SUCCESSFUL", Toast.LENGTH_SHORT).show();

        return true;
    }

    public static Contact[] getContactList(ContentResolver cr, Context context) {
        int contactnum = 0;
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            Contact[] contacts = new Contact[cur.getCount()];
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    pCur.moveToNext();
                    String phoneNo = pCur.getString(pCur.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));

                    Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_launcher_background);

                    try {
                        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                        if (inputStream != null) {
                            photo = BitmapFactory.decodeStream(inputStream);
                            inputStream.close();
                        }




                    } catch (IOException e) {
                        contacts[contactnum] = new Contact(null, name, phoneNo, id);
                        e.printStackTrace();
                    }
                    contacts[contactnum] = new Contact(photo, name, phoneNo, id);
                    Log.i(TAG, "Name: " + name);
                    Log.i(TAG, "Phone Number: " + phoneNo);


                    pCur.close();
                }
                contactnum += 1;
            }
            if(cur!=null){
                cur.close();
            }
            return contacts;
        }
        return new Contact[]{};
    }

    public static String getFileToByte(Bitmap bmp){
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.i("upload_pic_string", encodeString);
        return encodeString;
    }

    public static ProgressDialog setUpDialog(Activity activity)
    {
        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Loading");
        dialog.setMessage("Loading. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
