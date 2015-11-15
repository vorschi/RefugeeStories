package at.ac.tuwien.inso.refugeestories.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import at.ac.tuwien.inso.refugeestories.domain.Story;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class MockFactory {

    public static List<Story> getStories(int limit) {
        int count = getRandomNumber(limit);
        List<Story> stories = new ArrayList<>();
        while (count > 0) {
            stories.add(new Story(Consts.STORY_TEXT));
            count--;
        }
        return stories;
    }

    private static int getRandomNumber(int limit) {
        Random rand = new Random();
        return rand.nextInt(limit);
    }
}
