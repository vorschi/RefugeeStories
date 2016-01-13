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

import at.ac.tuwien.inso.refugeestories.R;

/**
 * Created by Amer Salkovic on 13.1.2016.
 */
public class FragmentLocation extends Fragment implements OnMapReadyCallback {

    RelativeLayout containerView;

    private final LatLng VIENNA = new LatLng(48.209272, 16.372801);

    private GoogleMap gMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        containerView = (RelativeLayout) inflater.inflate(R.layout.fragment_location, container, false);

        SupportMapFragment smf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        smf.getMapAsync(this);
        return containerView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(VIENNA, 15));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }
}
