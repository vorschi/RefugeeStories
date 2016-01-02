package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import android.widget.AbsListView.OnScrollListener;

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

    private final int LIMIT = 2;
    private int offset;
    private LoaderTask task;
    private SparseArray params;

    private int selectedStoryId;
    private int lastLoadedStoryId;

    private boolean loading;
    private boolean allStoriesLoaded;

    private StoryControllerImpl storyControllerInstance;
    private ImageControllerImpl imageControllerInstance;
    private MyDatabaseHelper dbHelper;

    private SharedPreferencesHandler sharedPrefs;

    //dialogs
    AlertDialog.Builder builder;
    AlertDialog deleteDialog;
    AlertDialog optionDialog;

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
        offset = 0;
        allStoriesLoaded = false;
        stories = new ArrayList<>();
        timelineAdapter = new TimelineAdapter(context);

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
        // TODO implement onLongClickListener either here or in the adapter ...

        timeline.setOnScrollListener(new OnScrollListener() {

            private boolean userScrolled;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    userScrolled = true;
                } else {
                    userScrolled = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ( userScrolled &&
                        ((firstVisibleItem + visibleItemCount) == totalItemCount) && !loading && !allStoriesLoaded) {
                    timeline.addFooterView(footer);
                    load(currentPerson.getId(), lastLoadedStoryId);
                }
            }
        });

//        openUser = (Button) mFragmentLayout.findViewById(R.id.open_user_btn);
//        openUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MainActivity activity = (MainActivity) getActivity();
//                activity.pushFragments(FragmentUser.getInstance(), true, Consts.TAB_USER);
//                //TODO send user object
//                FragmentUser.getInstance().setData(null, false);
//            }
//        });

        return mFragmentLayout;
    }

    /**
     * This method is used for adding the new stories provided by the LoaderTask
     *
     * @param newStories new stories retrieved from the db
     */
    public void addTimelineStories(List<Story> newStories) {
        if (newStories.isEmpty()) {
            allStoriesLoaded = true;
        } else {
            lastLoadedStoryId = newStories.get(newStories.size() - 1).getId();
        }

        stories.addAll(newStories);
        timelineAdapter.updateStories(stories);

        timeline.removeFooterView(footer);
        loading = false;
    }

    private void createDeleteDialog(final int position) {
        builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.delete_check)
                .setPositiveButton(R.string.delete_true, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stories.remove(position);
                        timelineAdapter.updateStories(stories);
                        //TODO remove from the db also
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteDialog.cancel();
                    }
                });
        deleteDialog = builder.create();
    }

    private void createOptionsDialog(final int position) {
        builder = new AlertDialog.Builder(context);
        builder.setItems(R.array.story_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                if (id == Consts.EDIT) {
                    Story targetStory = timelineAdapter.getItem(position);
                    //TODO: pass the targetStory to the MainActivity, To the CreateNewStoryFragment
                } else if (id == Consts.DELETE) {
                    createDeleteDialog(position);
                    optionDialog.show();
                } else { /*ignore*/ }
            }
        });
        optionDialog = builder.create();
    }

    public static FragmentTimeline getInstance() {
            if (instance == null)
                instance = new FragmentTimeline();
            return instance;
    }

    public String getName() {
        return (currentPerson.getId() == sharedPrefs.getUser().getId()) ?
                Consts.TAB_MYSTORIES : (currentPerson.getFistname() + " " + currentPerson.getLastname());
    }

    /**
     * This method executes the task used for the initialization of the timeline.
     *
     * @param authorId id of the timeline author
     * @param storyId  id of the targeted story
     * @return last loaded story id
     */
    private void init(int authorId, int storyId) {
        loading = true;

        //init task
        task = new TimelineInitTask(instance);
        task.setStoryControllerInstance(storyControllerInstance);
        task.setImageControllerInstance(imageControllerInstance);

        //init params
        params = new SparseArray();
        params.append(TimelineInitTask.AUTHOR_ID, authorId);
        params.append(TimelineInitTask.STORY_ID, storyId);
        params.append(TimelineInitTask.LIMIT, LIMIT);

        //execute task with authorId, storyId and limit
        task.execute(params);
    }

    /**
     * This method executes the task used for loading of the new stories after the user scrolls down to the bottom of the timeline.
     *
     * @param authorId          id of the timeline author
     * @param lastLoadedStoryId id of the last story in the timeline
     * @return last loaded story id
     */
    private void load(int authorId, int lastLoadedStoryId) {
        loading = true;

        //init task
        task = new TimelineLoaderTask(instance);
        task.setStoryControllerInstance(storyControllerInstance);
        task.setImageControllerInstance(imageControllerInstance);

        //init params
        params = new SparseArray();
        params.append(TimelineLoaderTask.AUTHOR_ID, authorId);
        params.append(TimelineLoaderTask.LAST_LOADED_STORY_ID, lastLoadedStoryId);
        params.append(TimelineLoaderTask.LIMIT, LIMIT);

        //execute task with authorId, lastLoadedStoryId and limit
        task.execute(params);
    }

    public void moveToPosition(int position) {
        timeline.smoothScrollToPosition(position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        mainActivity = ((MainActivity) activity);
        instance = this;
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

    private boolean showMyStories() {
        return currentPerson.getId() == sharedPrefs.getUser().getId();
    }

}
