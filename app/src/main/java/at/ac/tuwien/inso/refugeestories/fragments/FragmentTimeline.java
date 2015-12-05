package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import at.ac.tuwien.inso.refugeestories.MainActivity;
import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.utils.MockFactory;
import at.ac.tuwien.inso.refugeestories.utils.adapters.TimelineAdapter;

/**
 * Created by Amer Salkovic on 3.12.2015.
 */
public class FragmentTimeline extends Fragment {

    public static final String TAG = FragmentTimeline.class.getSimpleName();

    public static FragmentTimeline instance;

    private static Context context;

    private RelativeLayout mFragmentLayout;

    private ListView timeline;
    private TimelineAdapter timelineAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_timeline, container, false);
        timeline = (ListView) mFragmentLayout.findViewById(R.id.lst_timeline);
        timelineAdapter = new TimelineAdapter(context);
        timelineAdapter.updateStories(MockFactory.getStories(6));
        timeline.setAdapter(timelineAdapter);
        return mFragmentLayout;
    }

    public static FragmentTimeline getInstance() {
        //instance = (FragmentTimeline) ((MainActivity)context).getSupportFragmentManager().findFragmentByTag(TAG);
        if(instance == null) {
            instance = new FragmentTimeline();
        }
        return instance;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

}
