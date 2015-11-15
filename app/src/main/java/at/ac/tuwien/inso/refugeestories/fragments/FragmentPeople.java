package at.ac.tuwien.inso.refugeestories.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.ac.tuwien.inso.refugeestories.R;

/**
 * Created by nn on 14.11.2015.
 */
public class FragmentPeople extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        return view;
    }
}
