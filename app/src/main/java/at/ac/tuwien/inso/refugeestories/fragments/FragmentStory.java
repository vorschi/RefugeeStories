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

import java.util.Collections;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.MainActivity;
import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.persistence.ImageControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.MyDatabaseHelper;
import at.ac.tuwien.inso.refugeestories.persistence.StoryControllerImpl;
import at.ac.tuwien.inso.refugeestories.utils.Consts;
import at.ac.tuwien.inso.refugeestories.utils.MockFactory;
import at.ac.tuwien.inso.refugeestories.utils.RecyclerItemClickListener;
import at.ac.tuwien.inso.refugeestories.utils.adapters.StoryAdapter;

/**
 * Created by Amer Salkovic, Mario Vorstandlechner on 14.11.2015.
 */
public class FragmentStory extends Fragment {

    private final String TAG = FragmentStory.class.getSimpleName();
    private String CURRENT_TAB;

    private Context context;
    private FragmentManager fragmentManager;

    private RecyclerView myStoriesView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Story> stories = Collections.<Story>emptyList();
    private StoryAdapter storyAdapter;

    private TextView noStoriesMsg;
    private FloatingActionButton fab;

    //options dialog
    AlertDialog.Builder builder;
    AlertDialog optionDialog;

    OnStorySelectedListener mStoryCallback;

    private StoryControllerImpl storyControllerInstance;
    private ImageControllerImpl imageControllerInstance;
    private MyDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_stories, container, false);

        //initialize recyclerView and other components
        myStoriesView = (RecyclerView) contentView.findViewById(R.id.my_stories_view);
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        storyAdapter = new StoryAdapter();

        //set default properties
        myStoriesView.setHasFixedSize(true);
        myStoriesView.setLayoutManager(mLayoutManager);
        myStoriesView.setAdapter(storyAdapter);

        dbHelper = new MyDatabaseHelper(getActivity().getBaseContext());
        StoryControllerImpl.initializeInstance(dbHelper);
        storyControllerInstance = StoryControllerImpl.getInstance();
        ImageControllerImpl.initializeInstance(dbHelper);
        imageControllerInstance = ImageControllerImpl.getInstance();

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

            stories = storyControllerInstance.getRandomStories(10);
            for(Story story : stories) {
                story.setImages(imageControllerInstance.getImagesByStoryId(story.getId()));
            }
            //stories = MockFactory.getStories(6);
            storyAdapter.updateStories(stories);

        } else if (Consts.TAB_MYSTORIES.equals(CURRENT_TAB)) {

            //TODO get personal stories from the db.
            stories = storyControllerInstance.getStoriesByUserId(1);
            for(Story story : stories) {
                story.setImages(imageControllerInstance.getImagesByStoryId(story.getId()));
            }
            //stories = MockFactory.getStories(6);
            if (stories.isEmpty()) {
                noStoriesMsg = (TextView) contentView.findViewById(R.id.no_stories_msg);
                noStoriesMsg.setVisibility(TextView.VISIBLE);
            } else {
                storyAdapter.updateStories(stories);
            }

            fragmentManager = getFragmentManager();
            fab = (FloatingActionButton) contentView.findViewById(R.id.fab);
            fab.setVisibility(FloatingActionButton.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.realtabcontent, FragmentCreateNewStory.getInstance())
                            .addToBackStack(null)
                            .commit();
                    fragmentManager.executePendingTransactions();
                    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
                    getActivity().setTitle(R.string.newstory);
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
                if(id == Consts.EDIT) {
                    Story targetStory = storyAdapter.getItem(position);
                    //TODO: pass the targetStory to the MainActivity, To the CreateNewStoryFragment
                } else if(id == Consts.DELETE) {
                    //TODO: security check if story really should be deleted

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
                    }
                })
                .setNegativeButton(R.string.delete_false, new DialogInterface.OnClickListener() {
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

}
