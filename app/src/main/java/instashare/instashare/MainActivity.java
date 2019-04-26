package instashare.instashare;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
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
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button takepicbutton, galleryButton,singleUploadButton;
    SurfaceView sv;
    CameraHandler ch;
    final String LOGGED_IN = "alkdhksadfadfsdfhst";
    final int GALLERY_REQUEST_CODE = 112;
    private DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    boolean paused = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermissions();
        setUpPictureTaking();
        Log.d("are logged?", Boolean.toString(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(LOGGED_IN, false)));

        if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(LOGGED_IN, false)) {
            Intent i = new Intent(this, LogInActivity.class);
            startActivity(i);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer,R.string.drawer_open,R.string.drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);



        //ContactUploadService.uploadAllContacts(getContentResolver(), this, getApplicationContext());
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                TextView username = drawer.findViewById(R.id.user_name);
                username.setText(LoginService.my_username);
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    private void initGalleryButton() {
//        galleryButton = (Button) findViewById(R.id.gallery_button);
//
//        galleryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pickFromGallery();
//            }
//        });
//    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent();
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
    }

    public void onActivityResult(int requestCode,int resultCode, Intent data){
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case 1:
                    if(data.getClipData() == null) {
                        //make intent for pic taken activity
                        Intent intent = new Intent(this, GalleryResultActivity.class);
                        intent.putExtra("galleryImagePath", data.getData());
                        startActivity(intent);
                    }
                    else if(data.getClipData().getItemCount() > 1) {
                        ClipData selectedImages = data.getClipData();
                        ArrayList<ClipData> galleryImagePaths = new ArrayList<ClipData>();
                        galleryImagePaths.add(selectedImages);

                        //make intent for pic taken activity
                        Intent intent = new Intent(this, BatchGalleryActivity.class);
                        intent.putParcelableArrayListExtra("galleryImagePaths", galleryImagePaths);
                        startActivity(intent);
                    }
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
            ch = new CameraHandler(sv, (Activity)this, 1);
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
        //Intent intent = new Intent(this, ContactActivity.class);
        //startActivity(intent);
        ContactUploadService.uploadAllContacts(getContentResolver(), this, getApplicationContext(), this);
    }

    public void openSettingsPage(View v)
    {
//        Intent intent = new Intent(this, SettingsActivity.class);
//        startActivity(intent);
        //not opening settings page now
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(LOGGED_IN, false).commit();
        //ch.endCapture();
        System.exit(0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ch.endCapture();

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_analyze:
                pickFromGallery();
                break;
            case R.id.nav_contacts:
                ContactUploadService.uploadAllContacts(getContentResolver(), this, getApplicationContext(), this);
                break;
            case R.id.nav_logout:
                PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(LOGGED_IN, false).commit();
                System.exit(0);
                break;
        }

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

            if(ch.captureSetUp) {
                paused = true;
                try {
                    ch.pauseCapture();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    protected void onResume() {
            if(ch.captureSetUp && paused == true) {

                paused = false;
            }
        super.onResume();
    }


}
