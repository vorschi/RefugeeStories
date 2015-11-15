package at.ac.tuwien.inso.refugeestories;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import at.ac.tuwien.inso.refugeestories.fragments.FragmentPeople;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStories;
import at.ac.tuwien.inso.refugeestories.utils.Consts;
import at.ac.tuwien.inso.refugeestories.utils.MockFactory;
import at.ac.tuwien.inso.refugeestories.utils.adapters.ViewPagerAdapter;


public class MainActivity extends FragmentActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Locate the viewpager in activity_main.xml
        viewPager = (ViewPager) findViewById(R.id.pager);

        // Set the ViewPagerAdapter into ViewPager
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

    }

    private void doMySearch(String query) {
        int position = viewPager.getCurrentItem();
        if(position == Consts.STORIES) {
            FragmentStories fragmentStories = (FragmentStories) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + position);
            fragmentStories.updateStoriesList(MockFactory.getPeople(6));
        } else if(position == Consts.PEOPLE) {
            FragmentPeople fragmentPeople = (FragmentPeople) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + position);
            fragmentPeople.updateStoriesList(MockFactory.getPeople(7));
        } else {
            throw new IllegalArgumentException("Unknown Fragment");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                onSearchRequested();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }
}
