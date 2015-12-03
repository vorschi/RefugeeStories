package at.ac.tuwien.inso.refugeestories.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import at.ac.tuwien.inso.refugeestories.R;

/**
 * Created by Amer Salkovic on 3.12.2015.
 */
public class FragmentTimeline extends Fragment {

    public static FragmentTimeline instance;

    private RelativeLayout mFragmentLayout;

    private TextView label;

    private String text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_timeline, container, false);

        label = (TextView) mFragmentLayout.findViewById(R.id.lbl_timeline);
        text = label.getText().toString();

        return mFragmentLayout;
    }

    public void setTargetStory(int storyId) {
        label.setText("some text");
    }

    public static FragmentTimeline getInstance() {
        if(instance == null) {
            instance = new FragmentTimeline();
        }
        return instance;
    }
}
