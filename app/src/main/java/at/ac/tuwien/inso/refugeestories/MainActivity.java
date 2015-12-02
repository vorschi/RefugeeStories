package at.ac.tuwien.inso.refugeestories;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import at.ac.tuwien.inso.refugeestories.fragments.FragmentExplore;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentNotifications;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStories;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            FragmentExplore firstFragment = new FragmentExplore();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

    }

    public void openStories(View v){
        FragmentStories newFragment = new FragmentStories();
        Bundle args = new Bundle();
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
    public void openExplore(View v){
        FragmentExplore newFragment = new FragmentExplore();
        Bundle args = new Bundle();
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }
    public void openNotifications(View v){
        FragmentNotifications newFragment = new FragmentNotifications();
        Bundle args = new Bundle();
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    /*
    private void doMySearch(String query) {
        int position = viewPager.getCurrentItem();
        if(position == Consts.STORIES) {
            FragmentStories fragmentStories = (FragmentStories) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + position);
            fragmentStories.updateStoriesList(MockFactory.getPeople(6));
        } else if(position == Consts.PEOPLE) {
            FragmentNotifications fragmentPeople = (FragmentNotifications) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + position);
            fragmentPeople.updateStoriesList(MockFactory.getPeople(7));
        } else {
            throw new IllegalArgumentException("Unknown Fragment");
        }
    }
    */

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
/*
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
    */


}
