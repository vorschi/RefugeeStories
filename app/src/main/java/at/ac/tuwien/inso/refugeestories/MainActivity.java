package at.ac.tuwien.inso.refugeestories;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import at.ac.tuwien.inso.refugeestories.domain.Person;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentCreateNewStory;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentLocation;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentNotification;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStory;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStory.OnStorySelectedListener;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentTimeline;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentUser;
import at.ac.tuwien.inso.refugeestories.persistence.MyDatabaseHelper;
import at.ac.tuwien.inso.refugeestories.persistence.StoryControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.UserControllerImpl;
import at.ac.tuwien.inso.refugeestories.utils.Consts;
import at.ac.tuwien.inso.refugeestories.utils.SharedPreferencesHandler;

public class MainActivity extends FragmentActivity implements OnStorySelectedListener {

    private final String TAG = this.getClass().getSimpleName();

    public static final String M_CURRENT_TAB = "M_CURRENT_TAB";
    public TabHost mTabHost;
    private String mCurrentTab;

    private TextView label;
    private MenuItem follow_pic;
    private MenuItem search_pic;
    private Menu menu;
    private AlertDialog optionDialog;

    private FragmentManager manager;

    private LayoutInflater inflater;

    private SharedPreferencesHandler sharedPrefs;

    private UserControllerImpl userControllerInstance;
    private MyDatabaseHelper dbHelper;

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

        sharedPrefs = new SharedPreferencesHandler(this.getBaseContext());

        dbHelper = new MyDatabaseHelper(this.getBaseContext());
        UserControllerImpl.initializeInstance(dbHelper);
        userControllerInstance = UserControllerImpl.getInstance();

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
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
            if (tabId.equals(Consts.TAB_MYSTORIES)) {
               pushFragments(FragmentTimeline.getNewInstance(), false, tabId);
            }
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
        } else {
            /*
            and remove named backstack:
            manager.popBackStack("name of your backstack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            remove whole:*/
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        mCurrentTab = tabId;
        ft.commit();
        manager.executePendingTransactions();
        updateAppBar(tabId);
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
        else if (getCurrentTabId().equals(Consts.TAB_NOTIFICATIONS)) {
        }
        else if (getCurrentTabId().equals(Consts.TAB_MYSTORIES))
            inflater.inflate(R.menu.mystories_menu, menu);
        else if (getCurrentTabId().equals(Consts.TAB_TIMELINE))
            inflater.inflate(R.menu.timeline_menu, menu);
        else if (getCurrentTabId().equals(Consts.TAB_USER))
            inflater.inflate(R.menu.user_menu, menu);
        else if (getCurrentTabId().equals(Consts.TAB_MYPROFILE))
            inflater.inflate(R.menu.profile_menu, menu);

        follow_pic = menu.findItem(R.id.follow_btn);
        FragmentTimeline timeline = FragmentTimeline.getInstance();
        if (follow_pic != null){
            if(sharedPrefs.getUser().isSubscribed(timeline.getPerson())) {
                follow_pic.setIcon(R.drawable.ic_star_white_24dp);
            } else {
                follow_pic.setIcon(R.drawable.ic_star_border_white_24dp);
            }
        }

        // Associate searchable configuration with the SearchView
        search_pic = menu.findItem(R.id.search_btn);
        if(search_pic != null) {
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) search_pic.getActionView();
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Fragment currentFragment;
        Person user = sharedPrefs.getUser();

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.user_btn:
                if (getCurrentTabId().equals(Consts.TAB_MYSTORIES)) {
                    pushFragments(FragmentUser.getInstance(), true, Consts.TAB_MYPROFILE);
                    FragmentTimeline timeline = FragmentTimeline.getInstance();
                    FragmentUser.getInstance().setData(timeline.getPerson(), true);
                }
                else{
                    pushFragments(FragmentUser.getInstance(), true, Consts.TAB_USER);
                    FragmentTimeline timeline = FragmentTimeline.getInstance();
                    FragmentUser.getInstance().setData(timeline.getPerson(), false);
                }
                return true;

            case R.id.btn_add_story:
                pushFragments(FragmentCreateNewStory.getInstance(), true, Consts.TAB_NEWSTORY);
                return true;

            case R.id.follow_btn:
                Person timelineUser = getTimelineUser();
                if(user.isSubscribed(timelineUser)) {
                    userControllerInstance.deleteFollowerRecord(timelineUser, user);
                    user.removeFollowingUser(timelineUser);
                    item.setIcon(R.drawable.ic_star_border_white_24dp);
                    writeToast(getString(R.string.unfollow));
                } else {
                    userControllerInstance.createFollowerRecord(timelineUser, user);
                    user.getFollowingUsers().add(timelineUser);
                    item.setIcon(R.drawable.ic_star_white_24dp);
                    writeToast(getString(R.string.follow));
                }
                sharedPrefs.putUser(user);
                return true;

            case R.id.meeting_btn:
                if(user.isMeetingRequested(getTimelineUser())) {
                    writeToast(getString(R.string.meeting_request_sent_already));
                } else {
                    createMessageDialog(R.string.meeting_check, R.string.meeting_true, item);
                    optionDialog.show();
                }
                return true;

            case R.id.report_btn:
                createOptionsDialog(R.string.report_check, R.string.report_true, item);
                optionDialog.show();
                return true;
            case R.id.menuSortNewest:
                currentFragment = getSupportFragmentManager().findFragmentById(R.id.realtabcontent);
                if(currentFragment instanceof FragmentStory) {
                    ((FragmentStory) currentFragment).onSortOptionSelected(StoryControllerImpl.TableEntry.DATE, Consts.DESC);
                    return true;
                } else {
                    return false;
                }
            case R.id.menuSortOldest:
                currentFragment = getSupportFragmentManager().findFragmentById(R.id.realtabcontent);
                if(currentFragment instanceof FragmentStory) {
                    ((FragmentStory) currentFragment).onSortOptionSelected(StoryControllerImpl.TableEntry.DATE, Consts.ASC);
                    return true;
                } else {
                    return false;
                }
//            case R.id.menuSortNearest:
//                return true;
//            case R.id.menuSortFarthest:
//                return true;
            default:
                return super.onOptionsItemSelected(item);

            case R.id.settings_btn:
                writeToast("This feature is currently not available!");
                return true;
        }
    }

    @Override

    public void onStorySelected(Story story) {
        FragmentTimeline timeline = FragmentTimeline.getInstance();
        timeline.onStorySelected(story);
        pushFragments(timeline, true, Consts.TAB_TIMELINE);
    }

    public String getCurrentTabId() {
        return mCurrentTab;
    }

    private void writeToast(String text) {
        Toast toast = Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void updateAppBar(String tabId) {

        if (tabId.equals(Consts.TAB_TIMELINE)||tabId.equals(Consts.TAB_USER))
            tabId=getName();

        setTitle(tabId);

        if (tabId != Consts.TAB_EXPLORE && tabId != Consts.TAB_MYSTORIES && tabId != Consts.TAB_NOTIFICATIONS)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        else
            getActionBar().setDisplayHomeAsUpEnabled(false);

        invalidateOptionsMenu();
    }

    private String getName() {
        FragmentTimeline timeline = FragmentTimeline.getInstance();
        return timeline.getName();
    }

    private Person getTimelineUser() {
        FragmentTimeline timeline = FragmentTimeline.getInstance();
        return timeline.getPerson();
    }

    private void createOptionsDialog(final int message, final int check, final MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setPositiveButton(check, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(check == R.string.meeting_true) {
                            FragmentTimeline timeline = FragmentTimeline.getInstance();
                            Person user = sharedPrefs.getUser();
                            if(!user.isMeetingRequested(timeline.getPerson())) {
                                userControllerInstance.createMeetingRecord(timeline.getPerson(), user);
                                user.getRequestedMeetingUsers().add(timeline.getPerson());
                                writeToast(getString(R.string.meeting_request_sent));
                                sharedPrefs.putUser(user);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        optionDialog = builder.create();
    }

    private void createMessageDialog(final int message, final int check, final MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(check, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(check == R.string.meeting_true) {
                    FragmentTimeline timeline = FragmentTimeline.getInstance();
                    Person user = sharedPrefs.getUser();
                    if(!user.isMeetingRequested(timeline.getPerson())) {
                        userControllerInstance.createMeetingRecord(timeline.getPerson(), user);
                        user.getRequestedMeetingUsers().add(timeline.getPerson());
                        writeToast(getString(R.string.meeting_request_sent));
                        sharedPrefs.putUser(user);
                    }
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        optionDialog = builder.create();
    }

    private void showResults(String query) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.realtabcontent);
        if(currentFragment instanceof FragmentStory) {
            ((FragmentStory) currentFragment).onSearchStringEntered(query);
        }
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        }
    }

    public void myStories(){
        mTabHost.setCurrentTab(0);
    }

}
