package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import at.ac.tuwien.inso.refugeestories.utils.RecyclerItemClickListener;
import at.ac.tuwien.inso.refugeestories.utils.SharedPreferencesHandler;
import at.ac.tuwien.inso.refugeestories.utils.tasks.LoaderTask;
import at.ac.tuwien.inso.refugeestories.utils.tasks.PersonalStoriesLoaderTask;
import at.ac.tuwien.inso.refugeestories.utils.tasks.StoriesLoaderTask;
import at.ac.tuwien.inso.refugeestories.utils.adapters.StoryAdapter;

/**
 * Created by Amer Salkovic, Mario Vorstandlechner on 14.11.2015.
 */
public class FragmentStory extends Fragment {

    private final String TAG = FragmentStory.class.getSimpleName();

    private String CURRENT_TAB;
    private final int LIMIT = 5;
    private int offset;

    private Context context;
    private FragmentManager fragmentManager;

    private RecyclerView myStoriesView;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Story> stories;
    private StoryAdapter storyAdapter;
    private LoaderTask task;

    private TextView noStoriesMsg;
    private FloatingActionButton fab;

    //options dialog
    AlertDialog.Builder builder;
    AlertDialog optionDialog;

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
        dbHelper = new MyDatabaseHelper(getActivity().getBaseContext());
        StoryControllerImpl.initializeInstance(dbHelper);
        storyControllerInstance = StoryControllerImpl.getInstance();
        ImageControllerImpl.initializeInstance(dbHelper);
        imageControllerInstance = ImageControllerImpl.getInstance();

        //sp
        sharedPrefs = new SharedPreferencesHandler(getActivity());

        //add touch listener to the recyclerView
        myStoriesView.addOnItemTouchListener(new RecyclerItemClickListener(context,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View childView, int position) {
                        mStoryCallback.onStorySelected(position);
                    }

                    @Override
                    public void onItemLongPress(View childView, int position) {
                        if (Consts.TAB_MYSTORIES.equals(CURRENT_TAB)) {
                            createOptionsDialog(position);
                            optionDialog.show();
                        }
                    }
                }));

        //add data to the recyclerView
        if (Consts.TAB_EXPLORE.equals(CURRENT_TAB)) {

            //init task
            task = new StoriesLoaderTask(this);
            task.setStoryControllerInstance(storyControllerInstance);
            task.setImageControllerInstance(imageControllerInstance);

            //execute task with limit and offset params and finally increase offset
            task.execute(LIMIT, offset);
            offset += Consts.EXPLORE_STORY_INC;

        } else if (Consts.TAB_MYSTORIES.equals(CURRENT_TAB)) {

            noStoriesMsg = (TextView) contentView.findViewById(R.id.no_stories_msg);

            //init task
            task = new PersonalStoriesLoaderTask(this);
            task.setStoryControllerInstance(storyControllerInstance);
            task.setImageControllerInstance(imageControllerInstance);

            //execute task with limit, offset and userId params and finally increase offset
            task.execute(LIMIT, offset, sharedPrefs.getUser().getId());
            offset += Consts.EXPLORE_STORY_INC;

            fragmentManager = getFragmentManager();
            fab = (FloatingActionButton) contentView.findViewById(R.id.fab);
            fab.setVisibility(FloatingActionButton.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.pushFragments(FragmentCreateNewStory.getInstance(), true, Consts.TAB_NEWSTORY);
                }
            });
        }

        return contentView;
    }

    private void createOptionsDialog(final int position) {
        builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.story_options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                if (id == Consts.EDIT) {
                    Story targetStory = storyAdapter.getItem(position);
                    //TODO: pass the targetStory to the MainActivity, To the CreateNewStoryFragment
                } else if (id == Consts.DELETE) {

                    createDeleteDialog(position);
                    optionDialog.show();

                } else {
                    //ignore
                }
            }
        });
        optionDialog = builder.create();
    }

    private void createDeleteDialog(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_check)
                .setPositiveButton(R.string.delete_true, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stories.remove(position);
                        storyAdapter.updateStories(stories);
                        //TODO remove from the db also
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        optionDialog = builder.create();
    }

    public static FragmentStory getInstance() {
        FragmentStory instance = new FragmentStory();
        return instance;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
        CURRENT_TAB = ((MainActivity) activity).getCurrentTabId();

        try {
            mStoryCallback = (OnStorySelectedListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must implement OnStorySelectedListener");
        }
    }

    public interface OnStorySelectedListener {
        void onStorySelected(int position);
    }

    public String getName() {
        if(CURRENT_TAB.equals(Consts.TAB_EXPLORE)) {
            return Consts.TAB_EXPLORE;
        } else if (CURRENT_TAB.equals(Consts.TAB_MYSTORIES)) {
            return Consts.TAB_MYSTORIES;
        } else {
            return "";
        }
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

    /**
     * adds new stories for myStories fragment
     * @param newStories
     */
    public void addPersonalStories(List<Story> newStories) {
        if (storyAdapter == null) {
            return;
        }

        if (stories.isEmpty() && newStories.isEmpty()) {
            noStoriesMsg.setVisibility(TextView.VISIBLE);
            return;
        }

        stories.addAll(newStories);
        storyAdapter.updateStories(stories);
    }

}
