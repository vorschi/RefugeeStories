package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

import java.io.FileReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.MainActivity;
import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Person;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.persistence.ImageControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.MyDatabaseHelper;
import at.ac.tuwien.inso.refugeestories.persistence.StoryControllerImpl;
import at.ac.tuwien.inso.refugeestories.utils.Consts;
import at.ac.tuwien.inso.refugeestories.utils.SharedPreferencesHandler;
import at.ac.tuwien.inso.refugeestories.utils.adapters.TimelineAdapter;
import at.ac.tuwien.inso.refugeestories.utils.tasks.LoaderTask;
import at.ac.tuwien.inso.refugeestories.utils.tasks.TimelineLoaderTask;
import at.ac.tuwien.inso.refugeestories.utils.tasks.TimelineInitTask;

/**
 * Created by Amer Salkovic on 3.12.2015.
 */
public class FragmentTimeline extends Fragment implements FragmentStory.OnStorySelectedListener {

    private MainActivity mainActivity;
    private static FragmentTimeline instance;
    private Context context;

    private Person currentPerson;

    private RelativeLayout mFragmentLayout;

    private ListView timeline;
    private List<Story> stories;
    private TimelineAdapter timelineAdapter;
    private ProgressBar footer;

    private LoaderTask task;
    private SparseArray params;

    private int selectedStoryId;

    private StoryControllerImpl storyControllerInstance;
    private ImageControllerImpl imageControllerInstance;
    private MyDatabaseHelper dbHelper;

    private SharedPreferencesHandler sharedPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //db
        dbHelper = new MyDatabaseHelper(context);
        StoryControllerImpl.initializeInstance(dbHelper);
        storyControllerInstance = StoryControllerImpl.getInstance();
        ImageControllerImpl.initializeInstance(dbHelper);
        imageControllerInstance = ImageControllerImpl.getInstance();

        //sp
        sharedPrefs = new SharedPreferencesHandler(context);

        //prepare layouts
        mFragmentLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_timeline, container, false);
        timeline = (ListView) mFragmentLayout.findViewById(R.id.lst_timeline);
        footer = (ProgressBar) inflater.inflate(R.layout.footer, null);

        //init components
        stories = new ArrayList<>();
        timelineAdapter = new TimelineAdapter(instance);

        timeline.setAdapter(timelineAdapter);
        timeline.addFooterView(footer);

        //if MY_STORIES tab is currently active, then load host from the shared prefs.
        if (Consts.TAB_MYSTORIES.equals(mainActivity.getCurrentTabId())) {
            currentPerson = sharedPrefs.getUser();
        }

        /*
        in any other case currentPerson should not be null. It will be checked by throwing an exception in case it is null.
        currentPerson should be passed from the MainActivity, using onStorySelected()
        */
        if (currentPerson != null) {
            init(currentPerson.getId(), selectedStoryId);
        } else {
            throw new InvalidParameterException(FragmentTimeline.class.getSimpleName() + ": invalid params");
        }

        /* LISTENERS */


        return mFragmentLayout;
    }

    /**
     * This method is used for adding the new stories provided by the LoaderTask
     * @param newStories new stories retrieved from the db
     */
    public void addTimelineStories(List<Story> newStories) {
        stories.addAll(newStories);
        timelineAdapter.updateStories(stories);

        timeline.removeFooterView(footer);
    }

    private View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }

    public Context getContext() {
        return context;
    }

    public static FragmentTimeline getInstance() {
        if (instance == null)
            instance = new FragmentTimeline();
        return instance;
    }

    public static FragmentTimeline getNewInstance(){
        instance = new FragmentTimeline();
        return instance;
    }

    public String getName() {
        return (currentPerson.getId() == sharedPrefs.getUser().getId()) ?
                Consts.TAB_MYSTORIES : (currentPerson.getFirstname() + " " + currentPerson.getLastname());
    }

    public Person getPerson(){
        return currentPerson;
    }

    /**
     * This method executes the task used for the initialization of the timeline.
     *
     * @param authorId id of the timeline author
     * @param storyId  id of the targeted story
     * @return last loaded story id
     */
    private void init(int authorId, int storyId) {

        //init task
        task = new TimelineInitTask(instance);
        task.setStoryControllerInstance(storyControllerInstance);
        task.setImageControllerInstance(imageControllerInstance);

        //init params
        params = new SparseArray();
        params.append(TimelineInitTask.AUTHOR_ID, authorId);
        params.append(TimelineInitTask.STORY_ID, storyId);
        params.append(TimelineInitTask.ORDER, Consts.DESC);

        //execute task
        task.execute(params);
    }

    public boolean isMe() {
        return currentPerson.getId() == sharedPrefs.getUser().getId();
    }

    public void moveToPosition(int position) {
        smoothScrollToPositionFromTop(timeline, position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        mainActivity = ((MainActivity) activity);
        instance = this;
    }

    private boolean onDeleteStory(Story story) {
        return storyControllerInstance.deleteRecord(story);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // TODO ASA cache loaded stories, userId and current position in the list
    }

    @Override
    public void onStorySelected(Story story) {
        currentPerson = story.getAuthor();
        selectedStoryId = story.getId();
    }

    private void smoothScrollToPositionFromTop(final AbsListView view, final int position) {
        View child = getChildAtPosition(view, position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            return;
        }

        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);

                    // Fix for scrolling bug
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                        }
                    });
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) { }
        });

        // Perform scrolling to position
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPositionFromTop(position, 0);
            }
        });
    }

}
