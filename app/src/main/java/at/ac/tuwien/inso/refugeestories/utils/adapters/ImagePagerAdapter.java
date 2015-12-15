package at.ac.tuwien.inso.refugeestories.utils.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import at.ac.tuwien.inso.refugeestories.fragments.FragmentImage;

/**
 * Created by Amer Salkovic on 15.12.2015.
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> images;

    public ImagePagerAdapter(FragmentManager fm, List<String> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentImage.newInstance(images.get(position));
    }
}
