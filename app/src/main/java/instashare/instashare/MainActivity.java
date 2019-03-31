package instashare.instashare;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.camera2.CameraAccessException;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button takepicbutton, galleryButton;
    SurfaceView sv;
    CameraHandler ch;
    final String LOGGED_IN = "alkdhksadfadfsdfhst";
    final int GALLERY_REQUEST_CODE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissions();
        setUpPictureTaking();
        initGalleryButton();
        Log.d("are logged?", Boolean.toString(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(LOGGED_IN, false)));

        if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(LOGGED_IN, false)) {
            Intent i = new Intent(this, LogInActivity.class);
            startActivity(i);
        }

        //ContactUploadService.uploadAllContacts(getContentResolver(), this, getApplicationContext());
    }

    private void initGalleryButton() {
        galleryButton = (Button) findViewById(R.id.gallery_button);

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });
    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData return the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    // Set the Image in ImageView after decoding the String
                    //imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

                    //make intent for pic taken activity
                    Intent intent = new Intent(this, PictureTakenActivity.class);
                    intent.putExtra("myimage", imgDecodableString);
                    startActivity(intent);


                    break;

            }
    }


    public void setUpPictureTaking()
    {
        takepicbutton = findViewById(R.id.pictureButton);
        sv = (SurfaceView)findViewById(R.id.backgroundCamera);

        try {

            sv.setVisibility(SurfaceView.VISIBLE);
            sv.draw(new Canvas());
            ch = new CameraHandler(sv, (Activity)this);
            takepicbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ch.takePictureNow();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


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

    public void openContactList(View v)
    {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    public void openSettingsPage(View v)
    {
//        Intent intent = new Intent(this, SettingsActivity.class);
//        startActivity(intent);
        //not opening settings page now
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(LOGGED_IN, false).commit();
        System.exit(0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ch.endCapture();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
