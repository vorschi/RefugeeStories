package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.ac.tuwien.inso.refugeestories.R;

/**
 * Created by Mario Vorstandlechner, Amer Salkovic on 14.11.2015.
 */
public class FragmentCreateNewStory extends Fragment {

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_create_new_story, container, false);

        return contentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public static FragmentCreateNewStory getInstance() {
        FragmentCreateNewStory f = new FragmentCreateNewStory();
        return f;
    }
}
