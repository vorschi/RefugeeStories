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

    /*public static List<Story> getStories(int content) {
        List<Story> stories = new ArrayList<>();
        stories.add(createDummyStory());
        stories.add(createDummyStory1());
        stories.add(createDummyStory2());
        stories.add(createDummyStory3());
        stories.add(createDummyStory4());
        stories.add(createDummyStory5());
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
        s.setAuthor("Some ordinary user");
        s.setTitle("Fancy title");
        s.setText(Consts.STORY_TEXT);
        s.setDate(DateTime.now());
        s.setLocation("Wien");
        s.setImgResId(R.drawable.wien);
        return s;
    }

    private static Story createDummyStory1() {
        Story s = new Story();
        s.setAuthor("Halil Topru");
        s.setTitle("The story of my life");
        s.setText(Consts.STORY_TEXT);
        s.setDate(DateTime.now());
        s.setLocation("Salzburg");
        s.setImgResId(R.drawable.running);
        return s;
    }
    private static Story createDummyStory2() {
        Story s = new Story();
        s.setAuthor("Tutanchamun");
        s.setTitle("Ich Pharao");
        s.setText(Consts.STORY_TEXT);
        s.setDate(DateTime.now());
        s.setLocation("Unterpremstätten");
        s.setImgResId(R.drawable.tutanchamun);
        return s;
    }
    private static Story createDummyStory3() {
        Story s = new Story();
        s.setAuthor("Lilly Abraham");
        s.setTitle("Interssante Dinge aus meinem Leben");
        s.setText(Consts.STORY_TEXT);
        s.setDate(DateTime.now());
        s.setLocation("Freistadt");
        s.setImgResId(R.drawable.woman);
        return s;
    }
    private static Story createDummyStory4() {
        Story s = new Story();
        s.setAuthor("Some creepy user");
        s.setTitle("Creatures in my garden");
        s.setText(Consts.STORY_TEXT);
        s.setDate(DateTime.now());
        s.setLocation("Wiener Neustadt");
        s.setImgResId(R.drawable.dog);
        return s;
    }
    private static Story createDummyStory5() {
        Story s = new Story();
        s.setAuthor("Markus Hartl");
        s.setTitle("Der Weg nach Salzburg");
        s.setText(Consts.STORY_TEXT);
        s.setDate(DateTime.now());
        s.setLocation("Böheimkirchen");
        s.setImgResId(R.drawable.woman2);
        return s;
    }*/
}
