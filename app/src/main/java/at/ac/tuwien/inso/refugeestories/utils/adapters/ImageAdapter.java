package at.ac.tuwien.inso.refugeestories.utils.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Amer Salkovic on 5.12.2015.
 */
public class ImageAdapter extends PagerAdapter {

    private final Context context;
    List<Integer> images = Collections.<Integer>emptyList();

    public ImageAdapter(Context context) {
        this.context = context;
    }

    public void updateImages(List<Integer> images) {
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

//        int padding = context.getResources().getDimensionPixelSize(
//                R.dimen.padding_medium);
//        imageView.setPadding(padding, padding, padding, padding);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(images.get(position));
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

}
