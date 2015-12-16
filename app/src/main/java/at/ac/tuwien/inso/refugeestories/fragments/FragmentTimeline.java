package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;

import java.util.List;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.persistence.ImageControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.MyDatabaseHelper;
import at.ac.tuwien.inso.refugeestories.persistence.StoryControllerImpl;
import at.ac.tuwien.inso.refugeestories.utils.MockFactory;
import at.ac.tuwien.inso.refugeestories.utils.adapters.TimelineAdapter;

/**
 * Created by Amer Salkovic on 3.12.2015.
 */
public class FragmentTimeline extends Fragment {

    public static final String TAG = FragmentTimeline.class.getSimpleName();

    public static FragmentTimeline instance;

    private static Context context;

    private RelativeLayout mFragmentLayout;

    private ListView timeline;
    private TimelineAdapter timelineAdapter;

    private boolean loading = false;
    private boolean allStoriesLoaded = false;

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

        mFragmentLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_timeline, container, false);
        timeline = (ListView) mFragmentLayout.findViewById(R.id.lst_timeline);
        timelineAdapter = new TimelineAdapter(context);
        List<Story> stories = storyControllerInstance.getAllStories();
        for(Story story : stories) {
            story.setImages(imageControllerInstance.getImagesByStoryId(story.getId()));
        }
        timelineAdapter.updateStories(stories);
        //timelineAdapter.updateStories(MockFactory.getStories(6));
        timeline.setAdapter(timelineAdapter);


        timeline.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) { /* ignore */ }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //if the bottom of the list is reached
                if ( (firstVisibleItem + visibleItemCount) == totalItemCount ) {
                    //TODO load items
                }
            }
        });

        return mFragmentLayout;
    }

    public static FragmentTimeline getInstance() {
        //instance = (FragmentTimeline) ((MainActivity)context).getSupportFragmentManager().findFragmentByTag(TAG);
        if (instance == null) {
            instance = new FragmentTimeline();
        }
        return instance;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
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
}
