package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.utils.MockFactory;
import at.ac.tuwien.inso.refugeestories.utils.adapters.StoriesAdapter;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class FragmentStories extends Fragment {

    private ListView lvStories;
    private StoriesAdapter storiesAdapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_stories, container, false);

        lvStories  = (ListView) contentView.findViewById(R.id.lv_stories);
        updateStoriesList(MockFactory.getStories(6));

        return contentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public void updateStoriesList(List<Story> stories) {
        storiesAdapter = new StoriesAdapter(context, stories);
        lvStories.setAdapter(storiesAdapter);
    }
}
