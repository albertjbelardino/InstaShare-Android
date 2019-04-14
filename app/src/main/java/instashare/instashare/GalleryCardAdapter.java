package instashare.instashare;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class GalleryCardAdapter extends PagerAdapter {

    private List<String> imagePaths;
    private LayoutInflater layoutInflater;
    private Context context;

    public GalleryCardAdapter(Context context, List<String> imagePaths) {
        this.imagePaths = imagePaths;
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
        imageView.setImageBitmap(BitmapFactory.decodeFile(imagePaths.get(position)));
        container.addView(view, 0);
        return view;
    }
}
