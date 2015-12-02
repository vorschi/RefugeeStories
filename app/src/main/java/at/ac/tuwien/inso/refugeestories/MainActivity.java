package at.ac.tuwien.inso.refugeestories;

import android.app.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import at.ac.tuwien.inso.refugeestories.fragments.FragmentExplore;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentNotification;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStory;

public class MainActivity extends FragmentActivity {

    public static final String M_CURRENT_TAB = "M_CURRENT_TAB";
    private TabHost mTabHost;
    private String mCurrentTab;

    public static final String TAB_MYSTORIES = "MY_STORIES";
    public static final String TAB_EXPLORE = "EXPLORE";
    public static final String TAB_NOTIFICATIONS = "NOTIFICATIONS";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);

        mTabHost.setup();

        if (savedInstanceState != null) {
            mCurrentTab = savedInstanceState.getString(M_CURRENT_TAB);
            initializeTabs();
            mTabHost.setCurrentTabByTag(mCurrentTab);
            /*
            when resume state it's important to set listener after initializeTabs
            */
            mTabHost.setOnTabChangedListener(listener);
        } else {
            mTabHost.setOnTabChangedListener(listener);
            initializeTabs();
        }
    }

    private View createTabView(final int id, final String text) {
        View view = LayoutInflater.from(this).inflate(R.layout.tabs_icon, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
        imageView.setImageDrawable(getResources().getDrawable(id));
        TextView textView = (TextView) view.findViewById(R.id.tab_text);
        textView.setText(text);
        return view;
    }

    /*
    create 3 tabs with name and image
    and add it to TabHost
     */
    public void initializeTabs() {

        TabHost.TabSpec spec;

        spec = mTabHost.newTabSpec(TAB_MYSTORIES);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });

        //spec.setIndicator(getString(R.string.tab_tags, getResources().getDrawable(R.drawable.mystories_selector)));
        spec.setIndicator(createTabView(R.drawable.mystories_selector, getString(R.string.tab_tags)));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec(TAB_EXPLORE);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        //spec.setIndicator(getString(R.string.tab_map, getResources().getDrawable(R.drawable.explore_selector)));
        spec.setIndicator(createTabView(R.drawable.explore_selector, getString(R.string.tab_map)));
        mTabHost.addTab(spec);


        spec = mTabHost.newTabSpec(TAB_NOTIFICATIONS);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        //spec.setIndicator(getString(R.string.tab_settings, getResources().getDrawable(R.drawable.notifications_selector)));
        spec.setIndicator(createTabView(R.drawable.notifications_selector, getString(R.string.tab_settings)));
        mTabHost.addTab(spec);

    }

    /*
    first time listener will be trigered immediatelly after first: mTabHost.addTab(spec);
    for set correct Tab in setmTabHost.setCurrentTabByTag ignore first call of listener
    */
    TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
        public void onTabChanged(String tabId) {

            mCurrentTab = tabId;

            if (tabId.equals(TAB_MYSTORIES)) {
                pushFragments(FragmentStory.getInstance(), false,
                        false, null);
            } else if (tabId.equals(TAB_EXPLORE)) {
                pushFragments(FragmentExplore.getInstance(), false,
                        false, null);
            } else if (tabId.equals(TAB_NOTIFICATIONS)) {
                pushFragments(new FragmentNotification(), false,
                        false, null);
            }

        }
    };

    /*
    Example of starting nested fragment from another fragment:

    Fragment newFragment = ManagerTagFragment.newInstance(tag.getMac());
                    TagsActivity tAct = (TagsActivity)getActivity();
                    tAct.pushFragments(newFragment, true, true, null);
     */
    public void pushFragments(Fragment fragment,
                              boolean shouldAnimate, boolean shouldAdd, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        ft.replace(R.id.realtabcontent, fragment, tag);


        if (shouldAdd) {
            /*
            here you can create named backstack for realize another logic.
            ft.addToBackStack("name of your backstack");
             */
            ft.addToBackStack(null);
        } else {
            /*
            and remove named backstack:
            manager.popBackStack("name of your backstack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            or remove whole:
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
             */
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        ft.commit();
    }

    /*
    If you want to start this activity from another
     */
    public static void startUrself(Activity context) {
        Intent newActivity = new Intent(context, MainActivity.class);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newActivity);
        context.finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(M_CURRENT_TAB, mCurrentTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }


    /*
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
        this.fragmentTransacton(new FragmentStory());
    }
    public void openExplore(View v){
        this.fragmentTransacton(new FragmentExplore());
    }
    public void openNotifications(View v){
        this.fragmentTransacton(new FragmentNotification());
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
            FragmentStory fragmentStories = (FragmentStory) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + position);
            fragmentStories.updateStoriesList(MockFactory.getPeople(6));
        } else if(position == Consts.PEOPLE) {
            FragmentNotification fragmentPeople = (FragmentNotification) getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + position);
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
