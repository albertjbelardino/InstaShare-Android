package instashare.instashare;

import android.content.ClipData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class BatchGalleryActivity extends AppCompatActivity {

    ViewPager galleryViewPager;
    GalleryCardAdapter galleryCardAdapter;
    Button removeButton;

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
                galleryViewPager.removeView(galleryViewPager.getChildAt(galleryViewPager.getCurrentItem()));
            }
        });
    }
}
