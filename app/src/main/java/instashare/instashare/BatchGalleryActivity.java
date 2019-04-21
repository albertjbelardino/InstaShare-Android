package instashare.instashare;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class BatchGalleryActivity extends AppCompatActivity {

    ViewPager galleryViewPager;
    GalleryCardAdapter galleryCardAdapter;
    Button removeButton;
    final Context context = this;

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

                for(Uri galleryImagePath: galleryCardAdapter.getImagePaths()) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] b = baos.toByteArray();
                    try {
                        MediaStore.Images.Media.getBitmap(context.getContentResolver(), galleryImagePath)
                                .compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                        b = baos.toByteArray();
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final String sfString = Base64.encodeToString(b, Base64.DEFAULT);

                    try {
                        data.put("batch_photo", sfString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("json_object_batch", data.toString());
                VolleyFactory.sendJsonArrayRequestWithJsonObject(data, getApplicationContext(),
                        ApiContract.batchUpload(), context, galleryCardAdapter.getImagePaths());
            }
        });
    }
}
