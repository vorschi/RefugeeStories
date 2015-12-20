package at.ac.tuwien.inso.refugeestories.utils.tasks;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.security.InvalidParameterException;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.persistence.ImageControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.StoryControllerImpl;

/**
 * Created by Amer Salkovic on 20.12.2015.
 */
public abstract class LoaderTask extends AsyncTask<Integer, Void, List<Story>> {

    protected List<Story> stories;

    protected Fragment fragment;

    protected StoryControllerImpl storyControllerInstance;
    protected ImageControllerImpl imageControllerInstance;

    public LoaderTask(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        if(storyControllerInstance == null || imageControllerInstance == null) {
            throw new InvalidParameterException("Controllers must be set first!");
        }
    }

    public void setStoryControllerInstance(StoryControllerImpl storyControllerInstance) {
        this.storyControllerInstance = storyControllerInstance;
    }

    public void setImageControllerInstance(ImageControllerImpl imageControllerInstance) {
        this.imageControllerInstance = imageControllerInstance;
    }
}
