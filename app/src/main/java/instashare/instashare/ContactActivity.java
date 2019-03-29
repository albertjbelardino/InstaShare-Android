package instashare.instashare;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        //getContactList();
        ContactUploadService.uploadAllContacts(getContentResolver(), this, getApplicationContext());
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
            if(cur!=null){
                cur.close();
            }
            setUpRecyclerView(contacts);
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

}

