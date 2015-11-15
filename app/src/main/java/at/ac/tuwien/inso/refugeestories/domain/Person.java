package at.ac.tuwien.inso.refugeestories.domain;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class Person {

    private String username;
    private String password;
    private String email;
    private boolean isTeller;

    private String name;
    private String nationality;
    private List<String> tags;
    private List<String> spokenLanguages;
    private String story;
    private DateTime dateTime;
    private long lat;
    private long lng;
    private boolean geoLocation;
    private int distance;

    public Person() { }

    public Person(String story) {
        this.story = story;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(boolean geoLocation) {
        this.geoLocation = geoLocation;
    }

    public long getLng() {
        return lng;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public List<String> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(List<String> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTeller() {
        return isTeller;
    }

    public void setIsTeller(boolean isTeller) {
        this.isTeller = isTeller;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
