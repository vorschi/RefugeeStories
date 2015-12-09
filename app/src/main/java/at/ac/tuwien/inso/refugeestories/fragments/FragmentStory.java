package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;

import at.ac.tuwien.inso.refugeestories.R;

/**
 * Created by Amer Salkovic, Mario Vorstandlechner on 14.11.2015.
 */
public class FragmentStory extends Fragment {

    private Context context;
    private FloatingActionButton fab;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_stories, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();

        fab = (FloatingActionButton) contentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                    .replace(R.id.realtabcontent, FragmentCreateNewStory.getInstance())
                    .addToBackStack(null)
                    .commit();
                fragmentManager.executePendingTransactions();

            }
        });

        return contentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public static FragmentStory getInstance() {
        FragmentStory instance = new FragmentStory();
        return instance;
    }
}
