package at.ac.tuwien.inso.refugeestories;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Language;
import at.ac.tuwien.inso.refugeestories.domain.Person;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.persistence.MyDatabaseHelper;
import at.ac.tuwien.inso.refugeestories.persistence.UserControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.StoryControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.ImageControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.LanguageControllerImpl;
import at.ac.tuwien.inso.refugeestories.utils.SharedPreferencesHandler;

/**
 * Created by amsalk on 15.5.2015.
 */
public class InitActivity extends Activity {

    private static final String TAG = InitActivity.class.getSimpleName();
    private Handler myHandler;
    private Runnable runnable;
    private SharedPreferencesHandler sharedPrefs;

    private UserControllerImpl userControlleInstance;
    private StoryControllerImpl storyControllerInstance;
    private ImageControllerImpl imageControllerInstance;
    private LanguageControllerImpl languageControllerInstance;
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myHandler = new Handler();
        sharedPrefs = new SharedPreferencesHandler(this);

        dbHelper = new MyDatabaseHelper(this.getBaseContext());
        UserControllerImpl.initializeInstance(dbHelper);
        userControlleInstance = UserControllerImpl.getInstance();
        StoryControllerImpl.initializeInstance(dbHelper);
        storyControllerInstance = StoryControllerImpl.getInstance();
        ImageControllerImpl.initializeInstance(dbHelper);
        imageControllerInstance = ImageControllerImpl.getInstance();
        LanguageControllerImpl.initializeInstance(dbHelper);
        languageControllerInstance = LanguageControllerImpl.getInstance();

        start();
    }

    private void start() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if(!sharedPrefs.isUserLoggedIn()) {
                    Person user = userControlleInstance.getSingleRecord(2);
                    List<Story> stories = storyControllerInstance.getStoriesByUserId(user.getId());
                    user.setStories(stories);
                    for(Story story : stories) {
                        story.setImages(imageControllerInstance.getImagesByStoryId(story.getId()));
                    }
                    List<Language> languages = languageControllerInstance.getLanguagesByUserId(user.getId());
                    user.setLanguages(languages);
                    sharedPrefs.putUser(user);
                }
                Intent intent = new Intent(InitActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        myHandler.postDelayed(runnable, 2500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacks(runnable);
    }
}
