package at.ac.tuwien.inso.refugeestories.utils.tasks;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStory;
import at.ac.tuwien.inso.refugeestories.persistence.ImageControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.StoryControllerImpl;

/**
 * Created by Amer Salkovic on 17.12.2015.
 */
public class PersonalStoriesLoaderTask extends AsyncTask<Integer, Void, List<Story>> {

    private final int AUTHOR_ID = 0;
    private final int OFFSET = 1;

    private List<Story> stories;

    private Fragment fragment;

    private StoryControllerImpl storyControllerInstance;
    private ImageControllerImpl imageControllerInstance;

    public PersonalStoriesLoaderTask(Fragment fragment, StoryControllerImpl storyControllerInstance,
                                     ImageControllerImpl imageControllerInstance) {
        this.fragment = fragment;
        this.storyControllerInstance = storyControllerInstance;
        this.imageControllerInstance = imageControllerInstance;
    }

    @Override
    protected List<Story> doInBackground(Integer... integers) {
        stories = storyControllerInstance.getStoriesByUserId(integers[AUTHOR_ID], integers[OFFSET]);
        for(Story story : stories) {
            story.setImages(imageControllerInstance.getImagesByStoryId(story.getId()));
        }
        return stories;
    }

    @Override
    protected void onPostExecute(List<Story> stories) {
        super.onPostExecute(stories);
        ((FragmentStory) fragment).addPersonalStories(stories);
    }
}
