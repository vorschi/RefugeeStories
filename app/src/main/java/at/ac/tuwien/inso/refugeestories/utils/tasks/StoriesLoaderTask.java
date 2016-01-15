package at.ac.tuwien.inso.refugeestories.utils.tasks;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.security.InvalidParameterException;
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
    protected List<Story> doInBackground(SparseArray... sparseArrays) {
        if (sparseArrays.length <= 0 || sparseArrays[0].size() != 6) {
            throw new InvalidParameterException("Invalid or missing parameters");
        }
        SparseArray params = sparseArrays[0];
        stories = storyControllerInstance.getOrderedStories(
                (int) params.get(Consts.LIMIT),
                (int) params.get(Consts.OFFSET),
                (int) params.get(Consts.AUTHOR_ID),
                (String) params.get(Consts.COLUMN),
                (String) params.get(Consts.ORDER),
                (String) params.get(Consts.SEARCH)
        );
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
