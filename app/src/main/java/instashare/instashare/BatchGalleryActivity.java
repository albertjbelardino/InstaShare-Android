package instashare.instashare;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class BatchGalleryActivity extends AppCompatActivity {

    ViewPager galleryViewPager;
    GalleryCardAdapter galleryCardAdapter;
    Button removeButton;
    final Activity context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_gallery);

        ArrayList<ClipData> imagePaths = getIntent().getParcelableArrayListExtra("galleryImagePaths");

        galleryCardAdapter = new GalleryCardAdapter(this, imagePaths);
        galleryViewPager = (ViewPager) findViewById(R.id.batchGalleryViewPager);
        galleryViewPager.setAdapter(galleryCardAdapter);

        removeButton = (Button) findViewById(R.id.removeGalleryPictureButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject data = new JSONObject();
                JSONArray  array = new JSONArray();
                int count = 0;

                for(Uri galleryImagePath: galleryCardAdapter.getImagePaths()) {
                    try {
                        final InputStream imageStream = getContentResolver().openInputStream(galleryImagePath);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
                        byte[] b = baos.toByteArray();
                        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

                        array.put(encImage);
                        count = count + 1;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    data.put("group_photo", array);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("json_object_batch", data.toString());
                VolleyFactory.sendJsonArrayRequestWithJsonObject(data, getApplicationContext(),
                        ApiContract.batchUpload(), context, galleryCardAdapter.getImagePaths());
            }
        });
    }
}
