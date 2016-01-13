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

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.utils.Consts;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class FragmentNotification extends Fragment {

    private Context context;
    RelativeLayout but1;
    RelativeLayout but2;
    RelativeLayout but3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_notifications, container, false);

        but1 = (RelativeLayout)contentView.findViewById(R.id.not_but1);
        but1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO redirect to the latest story of Sami Turku
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "JAAAAAAAAAAAAAAAAAAAAAAAAAAAA", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        but2 = (RelativeLayout)contentView.findViewById(R.id.not_but2);
        but2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO redirect to latest story of Stefan Mayr
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "JAAAAAAAAAAAAAAAAAAAAAAAAAAAA", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        but3 = (RelativeLayout)contentView.findViewById(R.id.not_but3);
        but3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO redirect to your own timeline
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "JAAAAAAAAAAAAAAAAAAAAAAAAAAAA", Toast.LENGTH_SHORT);
                toast.show();
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

    public void tastk1(View view){

    }

    public String getName(){
        return Consts.TAB_NOTIFICATIONS;
    }
}
