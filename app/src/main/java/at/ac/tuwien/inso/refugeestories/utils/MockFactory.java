package at.ac.tuwien.inso.refugeestories.utils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import at.ac.tuwien.inso.refugeestories.domain.Person;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class MockFactory {

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
}
