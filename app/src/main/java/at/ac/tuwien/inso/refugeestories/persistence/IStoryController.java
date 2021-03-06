package at.ac.tuwien.inso.refugeestories.persistence;

import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Story;

/**
 * Created by mtraxler on 14.12.2015.
 */
public interface IStoryController {

    int createRecord(Story story);
    Story getSingleStory(int id);
    List<Story> getStoriesByUserId(int limit, int offset, int userId);
    List<Story> getAllStories();
    List<Story> getRandomStories(int count);
    boolean updateRecord(Story story);
    boolean deleteRecord(Story story);

}
