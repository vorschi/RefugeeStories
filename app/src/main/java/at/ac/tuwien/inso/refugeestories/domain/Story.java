package at.ac.tuwien.inso.refugeestories.domain;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Amer Salkovic on 2.12.2015.
 */
public class Story {

    private int id;
    private String title;
    private String text;
    private DateTime date;
    private String location;
    private List<Image> images;
    private Person author;

    //these attributes are going to be removed after db introduction
    private int imgResId;
    //

    public Story() { }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) { this.imgResId = imgResId; }
}
