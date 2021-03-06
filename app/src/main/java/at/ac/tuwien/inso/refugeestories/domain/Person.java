package at.ac.tuwien.inso.refugeestories.domain;

import java.util.Date;
import java.util.List;

/**
 * Created by mtraxler on 14.12.2015.
 */
public class Person {

    private int id;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private String nationality;
    private String img;
    private Date dob;
    private String gender;
    private String location;
    private double lat;
    private double lng;
    private String interests;
    private List<Language> languages;
    private List<Story> stories;
    private List<Person> followers;
    private List<Person> followingUsers;
    private List<Person> likers;
    private List<Person> likedUsers;
    private List<Person> RequestedMeetingUsers;

    public Person() { }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getFirstname() { return firstname; }

    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }

    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getNationality() { return nationality; }

    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getImg() { return img; }

    public void setImg(String img) { this.img = img; }

    public Date getDob() { return dob; }

    public void setDob(Date dob) { this.dob = dob; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() { return lng; }

    public void setLng(double lng) { this.lng = lng; }

    public String getInterests() { return interests; }

    public void setInterests(String interests) { this.interests = interests; }

    public List<Language> getLanguages() { return languages; }

    public void setLanguages(List<Language> languages) { this.languages = languages; }

    public List<Story> getStories() { return stories; }

    public void setStories(List<Story> stories) { this.stories = stories; }

    public List<Person> getFollowers() { return followers; }

    public void setFollowers(List<Person> followers) { this.followers = followers; }

    public List<Person> getFollowingUsers() { return followingUsers; }

    public void setFollowingUsers(List<Person> followingUsers) { this.followingUsers = followingUsers; }

    public boolean isSubscribed (Person person) {
        for(Person subscriber : getFollowingUsers()) {
            if(subscriber.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    public void removeFollowingUser(Person person) {
        for(Person subscriber : getFollowingUsers()) {
            if(subscriber.getId() == person.getId()) {
                getFollowingUsers().remove(subscriber);
                return;
            }
        }
    }

    public List<Person> getLikers() { return likers; }

    public void setLikers(List<Person> likers) { this.likers = likers; }

    public List<Person> getLikedUsers() { return likedUsers; }

    public void setLikedUsers(List<Person> likedUsers) { this.likedUsers = likedUsers; }

    public boolean isLiked (Person person) {
        for(Person likedUser : getLikedUsers()) {
            if(likedUser.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }

    public void removeLikedUser(Person person) {
        for(Person likedUser : getLikedUsers()) {
            if(likedUser.getId() == person.getId()) {
                getLikedUsers().remove(likedUser);
                return;
            }
        }
    }

    public void removeLiker(Person person) {
        for(Person liker : getLikers()) {
            if(liker.getId() == person.getId()) {
                getLikers().remove(liker);
                return;
            }
        }
    }

    public int countLikers() {
        List<Person> likers = getLikers();
        if(likers == null) {
            return 0;
        }
        return getLikers().size();
    }

    public List<Person> getRequestedMeetingUsers() { return RequestedMeetingUsers; }

    public void setRequestedMeetingUsers(List<Person> requestedMeetingUsers) { RequestedMeetingUsers = requestedMeetingUsers; }

    public boolean isMeetingRequested (Person person) {
        for(Person requestedMeetingUser : getRequestedMeetingUsers()) {
            if(requestedMeetingUser.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }
}
