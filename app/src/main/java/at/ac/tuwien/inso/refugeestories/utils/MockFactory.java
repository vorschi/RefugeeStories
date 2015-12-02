package at.ac.tuwien.inso.refugeestories.utils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.Person;
import at.ac.tuwien.inso.refugeestories.domain.Story;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class MockFactory {

    public static List<Story> getStories(int content) {
        List<Story> stories = new ArrayList<>();
        for(int i = 0; i < content; i++) {
            stories.add(createDummyStory());
        }
        return stories;
    }

    public static List<Person> getPeople(int limit) {
        int count = getRandomNumber(limit);
        List<Person> people = new ArrayList<>();
        while (count > 0) {
            people.add(createDummyPerson());
            count--;
        }
        return people;
    }

    private static int getRandomNumber(int limit) {
        Random rand = new Random();
        return rand.nextInt(limit);
    }

    private static Person createDummyPerson() {
        Person p = new Person();
        p.setName("John Doe");
        p.setTags(Arrays.asList(new String[]{"Krieg", "Flucht", "Gefahr", "Not"}));
        p.setDistance(2);
        p.setDateTime(DateTime.now());
        p.setStory(Consts.STORY_TEXT);
        return p;
    }

    private static Story createDummyStory() {
        Story s = new Story();
        s.setAuthor("Amer Kamakawiwoʻole");
        s.setTitle("Some title");
        s.setText(Consts.STORY_TEXT);
        s.setDate(DateTime.now());
        s.setLocation("Rudolfsheim-Fünfhaus");
        s.setImgResId(R.drawable.wien);
        return s;
    }
}
