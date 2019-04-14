package instashare.instashare;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GalleryCardAdapter extends PagerAdapter {

    private ArrayList<Uri> imagePaths;
    private LayoutInflater layoutInflater;
    private Context context;

    public GalleryCardAdapter(Context context, List<ClipData> clipDataList) {
        this.imagePaths = new ArrayList<>();
        ClipData clipData = clipDataList.get(0);
        for(int i = 0; i < clipData.getItemCount(); i++) {
            imagePaths.add(clipData.getItemAt(i).getUri());
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.batch_gallery_card_layout, container, false);

        ImageView imageView = view.findViewById(R.id.cardImageFromGallery);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imagePaths.get(position));
            imageView.setImageBitmap(bitmap);
            container.addView(view, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }
}
