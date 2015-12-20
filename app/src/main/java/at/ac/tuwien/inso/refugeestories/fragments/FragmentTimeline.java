package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.MainActivity;
import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.persistence.ImageControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.MyDatabaseHelper;
import at.ac.tuwien.inso.refugeestories.persistence.StoryControllerImpl;
import at.ac.tuwien.inso.refugeestories.utils.Consts;
import at.ac.tuwien.inso.refugeestories.utils.MockFactory;
import at.ac.tuwien.inso.refugeestories.utils.adapters.StoryAdapter;
import at.ac.tuwien.inso.refugeestories.utils.adapters.TimelineAdapter;
import at.ac.tuwien.inso.refugeestories.utils.tasks.LoaderTask;
import at.ac.tuwien.inso.refugeestories.utils.tasks.PersonalStoriesLoaderTask;

/**
 * Created by Amer Salkovic on 3.12.2015.
 */
public class FragmentTimeline extends Fragment {

    public static FragmentTimeline instance;
    private Button openUser;

    private Context context;

    private RelativeLayout mFragmentLayout;

    private ListView timeline;
    private List<Story> stories;
    private TimelineAdapter timelineAdapter;
    private ProgressBar footer;

    private final int LIMIT = 2;
    private int offset;
    private LoaderTask task;
    private boolean loading;
    private boolean allStoriesLoaded;

    private StoryControllerImpl storyControllerInstance;
    private ImageControllerImpl imageControllerInstance;
    private MyDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dbHelper = new MyDatabaseHelper(getActivity().getBaseContext());
        StoryControllerImpl.initializeInstance(dbHelper);
        storyControllerInstance = StoryControllerImpl.getInstance();
        ImageControllerImpl.initializeInstance(dbHelper);
        imageControllerInstance = ImageControllerImpl.getInstance();

        //prepare layouts
        mFragmentLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_timeline, container, false);
        timeline = (ListView) mFragmentLayout.findViewById(R.id.lst_timeline);
        footer = (ProgressBar) inflater.inflate(R.layout.footer, null);

        //init components
        offset = 0;
        allStoriesLoaded = false;
        stories = new ArrayList<>();
        timelineAdapter = new TimelineAdapter(context, getFragmentManager());
        task = new PersonalStoriesLoaderTask(this);
        task.setStoryControllerInstance(storyControllerInstance);
        task.setImageControllerInstance(imageControllerInstance);

        timeline.setAdapter(timelineAdapter);
        timeline.addFooterView(footer);

        //execute task with limit, offset and userId params. Finally increase offset
        loading = true;
        task.execute(LIMIT, offset, 1);
        offset += Consts.TIMELINE_STORY_INC;

        timeline.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) { /* ignore */ }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ( ((firstVisibleItem + visibleItemCount) == totalItemCount) && !loading && !allStoriesLoaded) {
                    timeline.addFooterView(footer);
                    task = new PersonalStoriesLoaderTask(instance);
                    task.setStoryControllerInstance(storyControllerInstance);
                    task.setImageControllerInstance(imageControllerInstance);
                    task.execute(LIMIT, offset, 1);
                    offset += Consts.TIMELINE_STORY_INC;
                }
            }
        });

        openUser = (Button) mFragmentLayout.findViewById(R.id.open_user_btn);
        openUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.pushFragments(FragmentUser.getInstance(), true, Consts.TAB_USER);
                //TODO send user object
                FragmentUser.getInstance().setData(null, false);
            }
        });

        return mFragmentLayout;
    }

    public static FragmentTimeline getInstance() {
        if (instance == null) {
            instance = new FragmentTimeline();
        }
        return instance;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        instance = this;
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.i(TAG, "PAUSE");
    }

    @Override
    public void onStop() {
        super.onStop();
        //Log.i(TAG, "STOPPED");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.i(TAG, "DESTROYED");
    }


    public String getName() {
        return Consts.TAB_TIMELINE;
    }

    public void addTimelineStories(List<Story> newStories) {
        if(newStories.isEmpty()) {
            allStoriesLoaded = true;
        }
        stories.addAll(newStories);
        timelineAdapter.updateStories(stories);
        timeline.removeFooterView(footer);
        loading = false;
    }

}
