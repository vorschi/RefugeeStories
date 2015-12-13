package at.ac.tuwien.inso.refugeestories;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
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
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStory.OnStorySelectedListener;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentTimeline;
import at.ac.tuwien.inso.refugeestories.utils.Consts;

public class MainActivity extends FragmentActivity implements OnStorySelectedListener {

    private final String TAG = this.getClass().getSimpleName();

    public static final String M_CURRENT_TAB = "M_CURRENT_TAB";
    private TabHost mTabHost;
    private String mCurrentTab;

    private TextView label;

    private FragmentManager manager;

    private LayoutInflater inflater;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);
        this.setTitle(R.string.explore);

        manager = getSupportFragmentManager();
        inflater = getLayoutInflater();

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
            mTabHost.setCurrentTabByTag(Consts.TAB_EXPLORE);
        }


        //showActionBar();
    }

    private View createTabView(final int id, final String text) {
        View view = LayoutInflater.from(this).inflate(R.layout.tabs_icon, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
        imageView.setImageDrawable(getResources().getDrawable(id));
        TextView textView = (TextView) view.findViewById(R.id.tab_text);
        textView.setText(text);
        return view;
    }

    private void showActionBar() {
        View v = inflater.inflate(R.layout.custom_bar, null);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
    }

    /*
    create 3 tabs with name and image
    and add it to TabHost
     */
    public void initializeTabs() {

        TabHost.TabSpec spec;

        spec = mTabHost.newTabSpec(Consts.TAB_MYSTORIES);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.mystories_selector, getString(R.string.tab_tags)));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec(Consts.TAB_EXPLORE);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.explore_selector, getString(R.string.tab_map)));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec(Consts.TAB_NOTIFICATIONS);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(R.id.realtabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.notifications_selector, getString(R.string.tab_settings)));
        mTabHost.addTab(spec);

    }

    TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
        public void onTabChanged(String tabId) {

            mCurrentTab = tabId;
            label = (TextView) findViewById(R.id.label);

            //Use FragmentStory for both stories and explore
            if ( tabId.equals(Consts.TAB_MYSTORIES) || tabId.equals(Consts.TAB_EXPLORE) ) {
                pushFragments(FragmentStory.getInstance(), false, false, null);
                getWindow().setTitle(getResources().getString(R.string.mystories));
            } else {
                pushFragments(new FragmentNotification(), false, false, null);
                getWindow().setTitle(getResources().getString(R.string.notifications));
            }

            /**
             * mario's old code
             *
            if (tabId.equals(Consts.TAB_MYSTORIES)) {
                pushFragments(FragmentStory.getInstance(), false, false, null);
                getWindow().setTitle(getResources().getString(R.string.mystories));
            } else if (tabId.equals(Consts.TAB_EXPLORE)) {
                pushFragments(FragmentExplore.getInstance(), false, false, null);
                getWindow().setTitle(getResources().getString(R.string.explore));
            } else if (tabId.equals(Consts.TAB_NOTIFICATIONS)) {
                pushFragments(new FragmentNotification(), false, false, null);
                getWindow().setTitle(getResources().getString(R.string.notifications));
            }
             */
        }
    };

    public void pushFragments(Fragment fragment,
                              boolean shouldAnimate, boolean shouldAdd, String tag) {
        FragmentTransaction ft = manager.beginTransaction();

        ft.replace(R.id.realtabcontent, fragment, tag);

        if (shouldAnimate) {
            /*
            here you can implement animations for fragment changes
             */
        }

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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(M_CURRENT_TAB, mCurrentTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                getActionBar().setDisplayHomeAsUpEnabled(false);
            case R.id.user_btn:
                //left button, do something
                return true;
            case R.id.filter_btn:
                // right button
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStorySelected(int position) {
        FragmentTimeline timeline = new FragmentTimeline();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.realtabcontent, timeline, FragmentTimeline.class.getSimpleName());
        ft.addToBackStack(null);
        ft.commit();
        manager.executePendingTransactions();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public String getCurrentTabId() {
        return mCurrentTab;
    }

}
