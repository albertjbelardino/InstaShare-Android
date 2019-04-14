package instashare.instashare;

import android.content.ClipData;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BatchGalleryActivity extends AppCompatActivity {

    ViewPager galleryViewPager;
    GalleryCardAdapter galleryCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_gallery);

        ArrayList<ClipData> imagePaths = getIntent().getParcelableArrayListExtra("galleryImagePaths");

        galleryCardAdapter = new GalleryCardAdapter(this, imagePaths);
        galleryViewPager = (ViewPager) findViewById(R.id.batchGalleryViewPager);
        galleryViewPager.setAdapter(galleryCardAdapter);
    }
}
