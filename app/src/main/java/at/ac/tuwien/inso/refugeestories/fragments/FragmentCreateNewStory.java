package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.Toast;

import org.joda.time.DateTime;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.utils.Utils;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class FragmentCreateNewStory extends Fragment implements OnDateSetListener {

    private Context context;

    private TextView storyTitle;
    private TextView storyLocation;
    private TextView storyDate;
    private TextView storyText;

    private Button btnAddStory;

    private ScrollView mContentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = (ScrollView) inflater.inflate(R.layout.fragment_create_new_story, container, false);

        storyTitle = (TextView) mContentView.findViewById(R.id.new_story_title);

        storyLocation = (TextView) mContentView.findViewById(R.id.new_story_location);

        storyDate = (TextView) mContentView.findViewById(R.id.new_story_date);
        storyDate.setText(Utils.dtf.print(DateTime.now()));
        storyDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment newFragment = new DatePickerDialogFragment(FragmentCreateNewStory.this);
                newFragment.show(ft, "date_dialog");
            }
        });

        storyText = (TextView) mContentView.findViewById(R.id.new_story_text);

        btnAddStory = (Button) mContentView.findViewById(R.id.btn_add_story);
        btnAddStory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    Toast.makeText(context, "story added!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return mContentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    public static FragmentCreateNewStory getInstance() {
        FragmentCreateNewStory instance = new FragmentCreateNewStory();
        return instance;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        if(storyDate != null) {
            storyDate.setText(dayOfMonth + "." + monthOfYear + "." + year);
        }
    }

    // TODO improve this with the loop
    public boolean validate() {

        String title = storyTitle.getText().toString();
        String location = storyLocation.getText().toString();
        String text = storyText.getText().toString();

        if (title.trim().isEmpty()) {
            storyTitle.setError("title is required");
            return false;
        }

        if (location.trim().isEmpty()) {
            storyLocation.setError("location is required");
            return false;
        }

        if (text.trim().isEmpty()) {
            storyText.setError("story is required");
            return false;
        }

        return true;
    }
}
