package at.ac.tuwien.inso.refugeestories.utils.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Image;
import at.ac.tuwien.inso.refugeestories.utils.tasks.BitmapWorkerTask;

/**
 * Created by Amer Salkovic on 5.12.2015.
 */
public class ImageAdapter extends PagerAdapter {

    private final Context context;
    List<Image> images;

    public ImageAdapter(Context context) {
        this.context = context;
        images = new ArrayList<>();
    }

    public void updateImages(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(images.get(position).getImg());

        ((ViewPager) container).addView(imageView, 0);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

}
