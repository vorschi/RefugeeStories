package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Person;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class FragmentUser extends Fragment {

    private Context context;
    private static FragmentUser instance;
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

        //dummie-entries
        user_forename.setText("Mario");
        user_surname.setText("Holzhauser");
        user_age.setText("27");
        user_gender.setText("Male");
        user_location.setText("Vienna");
        user_country.setText("Austria");
        user_languages.setText("German, English");
        user_interests.setText("Football, Running, Whisky, Music");
        user_email.setText("mario.me@abc.at");
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

    public void setData(Person user){
        //TODO:write data in textfields of current profile
    }
}
