package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Person;
import at.ac.tuwien.inso.refugeestories.utils.Utils;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class FragmentUser extends Fragment {

    private Context context;
    private static FragmentUser instance;
    private static boolean isMyUser = false;
    private ImageView userImage;
    private TextView user_forename;
    private TextView user_surname;
    private TextView user_age;
    private TextView user_gender;
    private TextView user_location;
    private TextView user_country;
    private TextView user_languages;
    private TextView user_interests;
    private TextView user_email;
    private TextView lbl_email;
    private String temp="";

    private ImageButton reportButton;
    private ImageButton requestMeetingButton;
    private Button showFriendsButton;

    AlertDialog optionDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_user, container, false);
        user_forename = (TextView) contentView.findViewById(R.id.user_forename);
        user_surname = (TextView) contentView.findViewById(R.id.user_surname);
        user_age = (TextView) contentView.findViewById(R.id.user_age);
        user_gender = (TextView) contentView.findViewById(R.id.user_gender);
        user_location = (TextView) contentView.findViewById(R.id.user_location);
        user_country = (TextView) contentView.findViewById(R.id.user_country);
        user_languages = (TextView) contentView.findViewById(R.id.user_language);
        user_interests = (TextView) contentView.findViewById(R.id.user_interests);
        user_email = (TextView) contentView.findViewById(R.id.user_email);
        lbl_email = (TextView) contentView.findViewById(R.id.lbl_email);

        reportButton = (ImageButton) contentView.findViewById(R.id.btn_report);
        requestMeetingButton = (ImageButton) contentView.findViewById(R.id.btn_request_meeting);
        showFriendsButton = (Button) contentView.findViewById(R.id.btn_see_friendlist);

        reportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createOptionsDialog(R.string.report_check, R.string.report_true);
                optionDialog.show();
            }
        });

        requestMeetingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createOptionsDialog(R.string.meeting_check, R.string.meeting_true);
                optionDialog.show();
            }
        });

        showFriendsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "OPENING LIST OF FRIENDS", Toast.LENGTH_LONG);
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

    public static FragmentUser getInstance() {
        if (instance == null) {
            instance = new FragmentUser();
        }
        return instance;
    }

    public void setData(Person user, boolean isMyUser) {
        this.isMyUser = isMyUser;
        //TODO:write data in textfields of current profile
        //dummie-entries
        user_forename.setText(user.getFirstname());
        user_surname.setText(user.getLastname());
        user_country.setText(user.getNationality());
        user_email.setText(user.getEmail());
        temp="";
        for (int i = 0; i < user.getLanguages().size(); i++){
            if (i==user.getLanguages().size()-1)
                temp+=""+user.getLanguages().get(i).getLanguage();
            else
                temp+=""+user.getLanguages().get(i).getLanguage()+", ";
        }
        user_languages.setText(temp);
        int age = Utils.getDiffYears(user.getDob(), new Date());
        user_age.setText(Integer.toString(age));
        user_gender.setText(user.getGender());
        user_location.setText(user.getLocation());
        user_interests.setText(user.getInterests());

        if (isMyUser) {
            reportButton.setVisibility(View.GONE);
            requestMeetingButton.setVisibility(View.GONE);
            showFriendsButton.setVisibility(View.VISIBLE);
            user_email.setVisibility(View.VISIBLE);
            lbl_email.setVisibility(View.VISIBLE);
        } else {
            reportButton.setVisibility(View.VISIBLE);
            requestMeetingButton.setVisibility(View.VISIBLE);
            showFriendsButton.setVisibility(View.GONE);
            user_email.setVisibility(View.GONE);
            lbl_email.setVisibility(View.GONE);
        }
    }

    private void createOptionsDialog(final int message, final int check) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(check, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        optionDialog = builder.create();
    }
}
