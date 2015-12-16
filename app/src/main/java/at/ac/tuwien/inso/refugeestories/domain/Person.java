package at.ac.tuwien.inso.refugeestories.domain;

import java.util.List;

/**
 * Created by mtraxler on 14.12.2015.
 */
public class Person {

    private int id;
    private String fistname;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private String nationality;
    private String img;
    private List<Language> languages;
    private List<Story> stories;

    public Person() { }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getFistname() { return fistname; }

    public void setFistname(String fistname) { this.fistname = fistname; }

    public String getLastname() { return lastname; }

    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getNationality() { return nationality; }

    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getImg() { return img; }

    public void setImg(String img) { this.img = img; }

    public List<Language> getLanguages() { return languages; }

    public void setLanguages(List<Language> languages) { this.languages = languages; }

    public List<Story> getStories() { return stories; }

    public void setStories(List<Story> stories) { this.stories = stories; }
}
