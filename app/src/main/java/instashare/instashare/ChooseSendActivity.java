package instashare.instashare;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChooseSendActivity extends AppCompatActivity {

    String[] names;
    String[] numbers;
    Button sendbutton;
    RecyclerView rv;
    ContactListAdapter cla;
    Contact[] contacts;
    String imagepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_send);

        sendbutton = findViewById(R.id.sendToContactsButton);
        rv = findViewById(R.id.sendContactRecycler);
        //Set<String> ss = new HashSet<>();
        names = getIntent().getStringArrayExtra("contact_names");
        numbers = getIntent().getStringArrayExtra("contact_numbers");
        imagepath = getIntent().getStringExtra("myimagepath");
        contacts = new Contact[names.length];
        Log.d("test", numbers.toString());
        for(int x = 0; x < names.length; x++)
        {
                contacts[x] = new Contact(null, names[x], numbers[x]);
        }
        Log.d("test", contacts.toString());
        cla = new ContactListAdapter(contacts, true);
        setUpRecyclerView();



    }

    public void onSendClick(View v)
    {
        Set<String> phonetosend = new HashSet<String>();
        for(int y = 0; y < contacts.length; y++)
        {
            if(cla.getCheckedList()[y] == true)
            {
                phonetosend.add(numbers[y]);
            }
        }
        sendImage(phonetosend.toArray(new String[0]));
        finish();

    }

    public void setUpRecyclerView()
    {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setAdapter(cla);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
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
