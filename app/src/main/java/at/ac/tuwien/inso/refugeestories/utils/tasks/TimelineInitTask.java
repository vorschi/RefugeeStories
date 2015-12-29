package at.ac.tuwien.inso.refugeestories.utils.tasks;

import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.security.InvalidParameterException;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.fragments.FragmentTimeline;

/**
 * Created by Amer Salkovic on 29.12.2015.
 *
 * This task is used for loading all the stories contained prior to the selected story including two succeeding stories if there are any.
 */
public class TimelineInitTask extends LoaderTask {

    public static final int STORY_ID = 1;

    private int selectedStoryPosition;
    private boolean found;

    public TimelineInitTask(Fragment fragment) {
        super(fragment);
        found = false;
    }

    @Override
    protected List<Story> doInBackground(SparseArray... sparseArrays) {
        if (sparseArrays.length <= 0 || sparseArrays[0].size() != 3) {
            throw new InvalidParameterException("Invalid or missing parameters");
        }

        SparseArray params = sparseArrays[0];
        stories = storyControllerInstance.getStoryPredecessorsAndTwoSuccessors(
                (int) params.get(AUTHOR_ID),
                (int) params.get(STORY_ID),
                (int) params.get(LIMIT)
        );

        for(int i = 0; i < stories.size(); i++) {
            if( ! found && stories.get(i).getId() == params.get(STORY_ID) ) {
                selectedStoryPosition = i;
                found = true;
            }
            stories.get(i).setImages(imageControllerInstance.getImagesByStoryId(stories.get(i).getId()));
        }

        return stories;
    }

    @Override
    protected void onPostExecute(List<Story> stories) {
        ((FragmentTimeline) fragment).addTimelineStories(stories);
        if(found) {
            ((FragmentTimeline) fragment).moveToPosition(selectedStoryPosition);
        }
    }
}
