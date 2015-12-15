package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.app.DatePickerDialog.OnDateSetListener;

import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.utils.Consts;
import at.ac.tuwien.inso.refugeestories.utils.Utils;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class FragmentCreateNewStory extends Fragment implements OnDateSetListener {

    private final String TAG = FragmentCreateNewStory.class.getSimpleName();

    private Context context;

    private TextView storyTitle;
    private TextView storyLocation;
    private TextView storyDate;
    private TextView storyText;

    private Button btnAddStory;
    private Button btnAddPictures;

    String[] selectedImages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_create_new_story, container, false);

        storyTitle = (TextView) contentView.findViewById(R.id.new_story_title);

        storyLocation = (TextView) contentView.findViewById(R.id.new_story_location);

        storyDate = (TextView) contentView.findViewById(R.id.new_story_date);
        storyDate.setText(Utils.dtf.print(DateTime.now()));
        storyDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment newFragment = new DatePickerDialogFragment(FragmentCreateNewStory.this);
                newFragment.show(ft, "date_dialog");
            }
        });

        storyText = (TextView) contentView.findViewById(R.id.new_story_text);

        btnAddPictures = (Button) contentView.findViewById(R.id.btn_add_pictures);
        btnAddPictures.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Consts.ACTION_MULTIPLE_PICK);
                startActivityForResult(intent, Consts.SELECT_MULTIPLE_IMAGES);
            }
        });

        btnAddStory = (Button) contentView.findViewById(R.id.btn_add_story);
        btnAddStory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    return;
                }

                //Story
                // TODO get Author name and id
                Story newStory = new Story();
                newStory.setTitle(storyTitle.getText().toString());
                newStory.setLocation(storyLocation.getText().toString());
                newStory.setDateAsString(storyDate.getText().toString());
                newStory.setStory(storyText.getText().toString());
                //TODO save story -> should return storyId

                //Images
                List<String> images = getSelectedImages();
                //TODO save images using storyId
            }
        });

        return contentView;
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

    public List<String> getSelectedImages() {
        if (selectedImages != null && selectedImages.length > 0) {
            return Arrays.<String>asList(selectedImages);
        }
        return Collections.<String>emptyList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Consts.SELECT_MULTIPLE_IMAGES && resultCode == Activity.RESULT_OK) {
            selectedImages = data.getStringArrayExtra("all_path");
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        if (storyDate != null) {
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
