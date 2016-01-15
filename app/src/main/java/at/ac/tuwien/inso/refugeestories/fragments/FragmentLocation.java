package at.ac.tuwien.inso.refugeestories.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.persistence.ImageControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.MyDatabaseHelper;
import at.ac.tuwien.inso.refugeestories.persistence.StoryControllerImpl;
import at.ac.tuwien.inso.refugeestories.utils.Consts;

/**
 * Created by Amer Salkovic on 13.1.2016.
 */
public class FragmentLocation extends Fragment implements OnMapReadyCallback {

    private RelativeLayout containerView;
    private Story story;

    private final LatLng VIENNA = new LatLng(48.209272, 16.372801);

    private GoogleMap gMap;

    private StoryControllerImpl storyControllerInstance;
    private MyDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        containerView = (RelativeLayout) inflater.inflate(R.layout.fragment_location, container, false);

        //init db
        dbHelper = new MyDatabaseHelper(getActivity());
        StoryControllerImpl.initializeInstance(dbHelper);
        storyControllerInstance = StoryControllerImpl.getInstance();

        SupportMapFragment smf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        smf.getMapAsync(this);
        return containerView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;

        List<Story> allStories = storyControllerInstance.getAllStoriesByUserId(story.getAuthor().getId(), Consts.DESC);
        if (allStories != null && !allStories.isEmpty()) {
            for (Story story : allStories) {
                gMap.addMarker(new MarkerOptions()
                        .position(story.getLatLng())
                        .title(story.getLocation()));
            }
        }

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(story.getLatLng(), 15));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public static FragmentLocation getInstance() {
        return new FragmentLocation();
    }
}
