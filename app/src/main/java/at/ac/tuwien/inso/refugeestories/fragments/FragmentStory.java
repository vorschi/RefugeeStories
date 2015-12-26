package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.persistence.ImageControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.MyDatabaseHelper;
import at.ac.tuwien.inso.refugeestories.persistence.StoryControllerImpl;
import at.ac.tuwien.inso.refugeestories.utils.Consts;
import at.ac.tuwien.inso.refugeestories.utils.EndlessRecyclerOnScrollListener;
import at.ac.tuwien.inso.refugeestories.utils.RecyclerItemClickListener;
import at.ac.tuwien.inso.refugeestories.utils.SharedPreferencesHandler;
import at.ac.tuwien.inso.refugeestories.utils.tasks.LoaderTask;
import at.ac.tuwien.inso.refugeestories.utils.tasks.StoriesLoaderTask;
import at.ac.tuwien.inso.refugeestories.utils.adapters.StoryAdapter;

/**
 * This Fragment represents explore view
 * Created by Amer Salkovic, Mario Vorstandlechner on 14.11.2015.
 */
public class FragmentStory extends Fragment {

    private final String TAG = FragmentStory.class.getSimpleName();
    private FragmentStory instance;

    private final int LIMIT = 5;
    private int offset;
    private String column;
    private String order;

    private Context context;

    private RecyclerView myStoriesView;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Story> stories;
    private StoryAdapter storyAdapter;
    private LoaderTask task;
    private SparseArray params;

    OnStorySelectedListener mStoryCallback;
    private SharedPreferencesHandler sharedPrefs;

    private StoryControllerImpl storyControllerInstance;
    private ImageControllerImpl imageControllerInstance;
    private MyDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_stories, container, false);

        //init offset
        offset = 0;
        column = StoryControllerImpl.TableEntry.DATE;
        order = Consts.DESC;

        //initialize recyclerView and other components
        myStoriesView = (RecyclerView) contentView.findViewById(R.id.my_stories_view);
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        stories = new ArrayList<>();
        storyAdapter = new StoryAdapter();

        //set default properties
        myStoriesView.setHasFixedSize(true);
        myStoriesView.setLayoutManager(mLayoutManager);
        myStoriesView.setAdapter(storyAdapter);

        //db
        dbHelper = new MyDatabaseHelper(context);
        StoryControllerImpl.initializeInstance(dbHelper);
        storyControllerInstance = StoryControllerImpl.getInstance();
        ImageControllerImpl.initializeInstance(dbHelper);
        imageControllerInstance = ImageControllerImpl.getInstance();

        //sp
        sharedPrefs = new SharedPreferencesHandler(context);

        //add touch listener to the recyclerView
        myStoriesView.addOnItemTouchListener(new RecyclerItemClickListener(context,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View childView, int position) {
                        mStoryCallback.onStorySelected(storyAdapter.getItem(position));
                    }

                    @Override
                    public void onItemLongPress(View childView, int position) { /*ignore*/ }
                }));


        myStoriesView.setOnScrollListener(new EndlessRecyclerOnScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                executeLoaderTask();
            }
        });

        executeLoaderTask();

        return contentView;
    }

    /**
     * Adds new stories for explore fragment
     * @param newStories
     */
    public void addStories(List<Story> newStories) {
        if (storyAdapter == null) {
            return;
        }

        stories.addAll(newStories);
        storyAdapter.updateStories(stories);
    }

    private void executeLoaderTask() {
        //init task
        task = new StoriesLoaderTask(this);
        task.setStoryControllerInstance(storyControllerInstance);
        task.setImageControllerInstance(imageControllerInstance);

        //init params
        params = new SparseArray();
        params.append(Consts.LIMIT, LIMIT);
        params.append(Consts.OFFSET, offset);
        params.append(Consts.AUTHOR_ID, sharedPrefs.getUser().getId());
        params.append(Consts.COLUMN, column);
        params.append(Consts.ORDER, order);

        /*
        execute task with limit and offset, userId, (sort) column and order params
        personal stories are excluded
        increase offset
        */
        task.execute(params);
        offset += Consts.EXPLORE_STORY_INC;
    }

    public static FragmentStory getInstance() {
        FragmentStory instance = new FragmentStory();
        return instance;
    }

    public String getName() {
        return Consts.TAB_EXPLORE;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
        instance = this;

        try {
            mStoryCallback = (OnStorySelectedListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must implement OnStorySelectedListener");
        }
    }

    public void onSortOptionSelected(String column, String order) {
        stories.clear();
        storyAdapter.updateStories(stories);
        offset = 0;
        this.column = column;
        this.order = order;
        executeLoaderTask();
    }

    public interface OnStorySelectedListener {
        void onStorySelected(Story story);
    }

}
