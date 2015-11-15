package at.ac.tuwien.inso.refugeestories.domain;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class Person {

    private String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
