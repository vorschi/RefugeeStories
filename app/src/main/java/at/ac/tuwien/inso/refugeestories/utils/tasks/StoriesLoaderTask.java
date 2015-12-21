package at.ac.tuwien.inso.refugeestories.utils.tasks;

import android.support.v4.app.Fragment;

import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStory;
import at.ac.tuwien.inso.refugeestories.utils.Consts;

/**
 * Created by Amer Salkovic on 16.12.2015.
 */
public class StoriesLoaderTask extends LoaderTask {

    public StoriesLoaderTask(Fragment fragment) {
        super(fragment);
    }

    @Override
    protected List<Story> doInBackground(Integer... integers) {
        stories = storyControllerInstance.getStories(integers[Consts.LIMIT], integers[Consts.OFFSET], integers[Consts.AUTHOR_ID]);
        for (Story story : stories) {
            story.setImages(imageControllerInstance.getImagesByStoryId(story.getId()));
        }
        return stories;
    }

    @Override
    protected void onPostExecute(List<Story> stories) {
        ((FragmentStory) fragment).addStories(stories);
    }

}
