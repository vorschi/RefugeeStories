package at.ac.tuwien.inso.refugeestories;

import android.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import at.ac.tuwien.inso.refugeestories.fragments.FragmentCreateNewStory;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentNotification;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStory;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStory.OnStorySelectedListener;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentTimeline;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentUser;
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
            //Use FragmentStory for both stories and explore
            if (tabId.equals(Consts.TAB_MYSTORIES))
                pushFragments(FragmentStory.getInstance(), false, tabId);
            else if (tabId.equals(Consts.TAB_EXPLORE))
                pushFragments(FragmentStory.getInstance(), false, tabId);
            else
                pushFragments(FragmentNotification.getInstance(), false, tabId);
        }
    };

    public void pushFragments(Fragment fragment, boolean shouldAdd, String tabId) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.realtabcontent, fragment);
        if (shouldAdd) {
            //add to stack for back navigation
            ft.addToBackStack(mCurrentTab);
            updateAppBar(tabId);
        } else {
            /*
            and remove named backstack:
            manager.popBackStack("name of your backstack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            remove whole:*/
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            updateAppBar(tabId);
        }
        mCurrentTab = tabId;
        ft.commit();
        manager.executePendingTransactions();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(M_CURRENT_TAB, mCurrentTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (manager.getBackStackEntryCount() == 0)
            this.finish();

        else {
            mCurrentTab = manager.getBackStackEntryAt((manager.getBackStackEntryCount()) - 1).getName();
            updateAppBar(mCurrentTab);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (getCurrentTabId().equals(Consts.TAB_EXPLORE))
            inflater.inflate(R.menu.explore_menu, menu);
        else
            inflater.inflate(R.menu.others_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.user_btn:
                if (getCurrentTabId() != Consts.TAB_MYPROFILE) {
                    pushFragments(FragmentUser.getInstance(), true, Consts.TAB_MYPROFILE);
                    FragmentUser.getInstance().setData(null, true);
                }
                return true;

            //TODO: implement filter-options
            case R.id.menuSortNewest:
                writeToast("sort by newest");
                return true;
            case R.id.menuSortOldest:
                // right button
                return true;
            case R.id.menuSortNearest:
                // right button
                return true;
            case R.id.menuSortFarthest:
                // right button
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStorySelected(int position) {
        pushFragments(FragmentTimeline.getInstance(), true, Consts.TAB_TIMELINE);
    }

    public String getCurrentTabId() {
        return mCurrentTab;
    }

    private void writeToast(String text) {
        Toast toast = Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void updateAppBar(String tabId) {
        if (tabId.equals(Consts.TAB_MYPROFILE))
            setTitle(R.string.myprofile);

        else if (tabId.equals(Consts.TAB_USER))
            setTitle(R.string.user);

        else if (tabId.equals(Consts.TAB_NEWSTORY))
            setTitle(R.string.newstory);

        else if (tabId.equals(Consts.TAB_TIMELINE))
            setTitle(R.string.timeline);

        else if (tabId.equals(Consts.TAB_MYSTORIES))
            setTitle(R.string.mystories);

        else if (tabId.equals(Consts.TAB_EXPLORE))
            setTitle(R.string.explore);

        else if (tabId.equals(Consts.TAB_NOTIFICATIONS))
            setTitle(R.string.notifications);

        if (tabId != Consts.TAB_EXPLORE && tabId != Consts.TAB_MYSTORIES && tabId != Consts.TAB_NOTIFICATIONS)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        else
            getActionBar().setDisplayHomeAsUpEnabled(false);

        invalidateOptionsMenu();
    }
}
