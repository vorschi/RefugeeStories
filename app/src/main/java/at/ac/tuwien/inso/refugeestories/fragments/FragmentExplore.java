package at.ac.tuwien.inso.refugeestories.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.ac.tuwien.inso.refugeestories.R;

/**
 * Created by Vorschi on 02.12.2015.
 */
public class FragmentExplore extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }
}

