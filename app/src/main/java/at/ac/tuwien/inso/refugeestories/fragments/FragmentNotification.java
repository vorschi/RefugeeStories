package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import at.ac.tuwien.inso.refugeestories.MainActivity;
import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Image;
import at.ac.tuwien.inso.refugeestories.domain.Person;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.persistence.ImageControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.MyDatabaseHelper;
import at.ac.tuwien.inso.refugeestories.persistence.StoryControllerImpl;
import at.ac.tuwien.inso.refugeestories.utils.Consts;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class FragmentNotification extends Fragment {

    private Context context;
    RelativeLayout but1;
    RelativeLayout but2;
    RelativeLayout but3;

    private StoryControllerImpl storyControllerInstance;
    private ImageControllerImpl imageControllerInstance;
    private MyDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_notifications, container, false);

        //db
        dbHelper = new MyDatabaseHelper(context);
        StoryControllerImpl.initializeInstance(dbHelper);
        storyControllerInstance = StoryControllerImpl.getInstance();
        ImageControllerImpl.initializeInstance(dbHelper);
        imageControllerInstance = ImageControllerImpl.getInstance();

        but1 = (RelativeLayout)contentView.findViewById(R.id.not_but1);
        but1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //redirect to the latest story of Sami Turku
                FragmentTimeline timeline = FragmentTimeline.getInstance();
                Story s = getStory(15);
                timeline.onStorySelected(s);
                ((MainActivity) getActivity()).pushFragments(timeline, true, Consts.TAB_TIMELINE);
            }
        });
        but2 = (RelativeLayout)contentView.findViewById(R.id.not_but2);
        but2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //redirect to latest story of Stefan Mayr
                FragmentTimeline timeline = FragmentTimeline.getInstance();
                Story s = getStory(16);
                timeline.onStorySelected(s);
                ((MainActivity) getActivity()).pushFragments(timeline, true, Consts.TAB_TIMELINE);
            }
        });
        but3 = (RelativeLayout)contentView.findViewById(R.id.not_but3);
        but3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //redirect to your own timeline
                FragmentTimeline timeline = FragmentTimeline.getInstance();
                ((MainActivity) getActivity()).pushFragments(timeline, false, Consts.TAB_MYSTORIES);
            }
        });

        return contentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public static FragmentNotification getInstance() {
        FragmentNotification f = new FragmentNotification();
        return f;
    }

    private Story getStory(int storyId) {
        Story s = storyControllerInstance.getSingleStory(storyId);
        List<Image> imageList = imageControllerInstance.getImagesByStoryId(storyId);
        if(imageList != null && !imageList.isEmpty()) {
            s.setImages(imageList);
        }
        return s;
    }

    public String getName(){
        return Consts.TAB_NOTIFICATIONS;
    }
}
