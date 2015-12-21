package at.ac.tuwien.inso.refugeestories.utils.tasks;

import android.support.v4.app.Fragment;

import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStory;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentTimeline;
import at.ac.tuwien.inso.refugeestories.utils.Consts;

/**
 * Created by Amer Salkovic on 17.12.2015.
 */
public class PersonalStoriesLoaderTask extends LoaderTask {

    public PersonalStoriesLoaderTask(Fragment fragment) {
        super(fragment);
    }

    @Override
    protected List<Story> doInBackground(Integer... integers) {
        stories = storyControllerInstance.getStoriesByUserId(integers[Consts.LIMIT], integers[Consts.OFFSET], integers[Consts.AUTHOR_ID]);
        for (Story story : stories) {
            story.setImages(imageControllerInstance.getImagesByStoryId(story.getId()));
        }
        return stories;
    }

    @Override
    protected void onPostExecute(List<Story> stories) {
        ((FragmentTimeline) fragment).addTimelineStories(stories);
    }

}
