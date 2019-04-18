package instashare.instashare;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.camera2.CameraAccessException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SingleContactUploadActivity extends AppCompatActivity {

    Button uploadContactButton;
    String contactImagePath;
    Activity thisactivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact_upload);
        getPermissions();
        setContactImage();
        initUploadContactButton();
    }

    public void setContactImage() {
        contactImagePath = getIntent().getStringExtra("imagepath");
        Log.d("imagepath", contactImagePath);
        ((ImageView) findViewById(R.id.contact_image_view))
                .setImageBitmap(BitmapFactory.decodeFile(contactImagePath));
    }

    public void initUploadContactButton() {
        uploadContactButton = (Button) findViewById(R.id.upload_contact_button);
        uploadContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
                String number = ((EditText) findViewById(R.id.numberEditText)).getText().toString();
                ContactUploadService.uploadSingleContact(name, number, BitmapFactory.decodeFile(contactImagePath), getApplicationContext(), thisactivity, false);
            }
        });
    }

    public void getPermissions()
    {
        while (true) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS}, 0);
                //do something
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
            } else {
                break;
            }
        }
    }
}
