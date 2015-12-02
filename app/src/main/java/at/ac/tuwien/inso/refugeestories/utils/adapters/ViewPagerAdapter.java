package at.ac.tuwien.inso.refugeestories.utils.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import at.ac.tuwien.inso.refugeestories.fragments.FragmentNotifications;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStories;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {


    final int PAGE_COUNT = 2;
    // Tab Titles
    private String titles[] = new String[]{"Stories", "People"};
    Context context;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // Open Stories
            case 0:
                FragmentStories fragmentStories = new FragmentStories();
                return fragmentStories;

            // Open People
            case 1:
                FragmentNotifications fragmentNotifications = new FragmentNotifications();
                return fragmentNotifications;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }


    public <T> void updateFragment(int position, List<T> content) {
        // XXX @ ASA TODO MAYBE
    }

}
