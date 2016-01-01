package at.ac.tuwien.inso.refugeestories.persistence;

import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Image;
import at.ac.tuwien.inso.refugeestories.domain.Story;

/**
 * Created by mtraxler on 14.12.2015.
 */
public interface IImageController {

    int createRecord(Image image);
    Image getSingleImage(int id);
    List<Image> getImagesByStoryId(int storyId);
    boolean updateRecord(Image image);
    boolean deleteAllRecords(Story story);
    boolean deleteSingleRecord(Image image);
}
