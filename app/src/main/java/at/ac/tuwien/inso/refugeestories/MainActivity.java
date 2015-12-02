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
        setTitle("Explore");

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            FragmentExplore firstFragment = new FragmentExplore();
            firstFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }
    }

    public void openStories(View v){
        this.fragmentTransacton(new FragmentStories());
    }
    public void openExplore(View v){
        this.fragmentTransacton(new FragmentExplore());
    }
    public void openNotifications(View v){
        this.fragmentTransacton(new FragmentNotifications());
    }

    private void fragmentTransacton(Fragment newFragment){
        Bundle args = new Bundle();
        //args.putInt(newFragment.ARG_POSITION, position);
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
