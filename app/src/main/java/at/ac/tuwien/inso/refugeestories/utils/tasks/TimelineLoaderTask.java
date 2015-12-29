package at.ac.tuwien.inso.refugeestories.utils.tasks;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.security.InvalidParameterException;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentStory;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentTimeline;
import at.ac.tuwien.inso.refugeestories.utils.Consts;

/**
 * Created by Amer Salkovic on 17.12.2015.
 */
public class TimelineLoaderTask extends LoaderTask {

    public static final int LAST_LOADED_STORY_ID = 1;

    public TimelineLoaderTask(Fragment fragment) {
        super(fragment);
    }

    @Override
    protected List<Story> doInBackground(SparseArray... sparseArrays) {
        if (sparseArrays.length <= 0 || sparseArrays[0].size() != 3) {
            throw new InvalidParameterException("Invalid or missing parameters");
        }
        SparseArray params = sparseArrays[0];
        stories = storyControllerInstance.getStoriesByUserIdFromStoryId(
                (int) params.get(AUTHOR_ID),
                (int) params.get(LAST_LOADED_STORY_ID),
                (int) params.get(LIMIT)
        );
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
