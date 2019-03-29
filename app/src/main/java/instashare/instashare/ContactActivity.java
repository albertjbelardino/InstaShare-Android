package instashare.instashare;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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

import static android.content.ContentValues.TAG;

public class ContactActivity extends AppCompatActivity {

    final String MY_TOKEN = "sljdgbnrnkjsdfbgkjgnxfbnjkdgnjk";
    RequestQueue rq;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        rq = Volley.newRequestQueue(getApplicationContext());

        getContactList();
    }

    private void getContactList() {
        int contactnum = 0;
        ContentResolver cr = getContentResolver();
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

                    Bitmap photo = BitmapFactory.decodeResource(this.getResources(),
                            R.drawable.ic_launcher_background);

                    try {
                        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(this.getContentResolver(),
                                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                        if (inputStream != null) {
                            photo = BitmapFactory.decodeStream(inputStream);
                            inputStream.close();
                            sendContactToServer(name, "(A person)", phoneNo, photo);

                        }




                    } catch (IOException e) {
                        contacts[contactnum] = new Contact(null, name, phoneNo);
                        e.printStackTrace();
                    }
                    contacts[contactnum] = new Contact(photo, name, phoneNo);
                        Log.i(TAG, "Name: " + name);
                        Log.i(TAG, "Phone Number: " + phoneNo);


                    pCur.close();
                }
                contactnum += 1;
            }

            setUpRecyclerView(contacts);
        }
        if(cur!=null){
            cur.close();
        }


    }

    public void setUpRecyclerView(Contact[] contactlist)
    {
        RecyclerView rv = findViewById(R.id.contactRecycle);
        ContactListAdapter cla = new ContactListAdapter(contactlist);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setAdapter(cla);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

    }

    public void sendContactToServer(String firstname, String lastname, String number, Bitmap photo)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String sfString = Base64.encodeToString(b, Base64.DEFAULT);
        final Map<String, String> data = new HashMap<String, String>();

        number = number.replace("+", "");
        number = number.replace("(", "");
        number = number.replace(")", "");
        number = number.replace(" ", "");
        number = number.replace("-", "");





        data.put("base_64", sfString);
        data.put("first_name", firstname);
        data.put("last_name", lastname);
        data.put("phone_number", number);


        Log.d("INFO", data.get("first_name"));
        Log.d("MY TOKEN", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(MY_TOKEN, "this is not a token"));

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST,"http://10.110.32.66:8000/api/uploadContact64/", new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());

            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " +
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(MY_TOKEN, "this is not a token"));
                return headers;
            }
        };
        //{
//                        @Override
//                        protected Map<String, String> getParams() throws AuthFailureError {
//
//                            return data;
//                        }
//                    };
        jor.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(jor);

    }

}

