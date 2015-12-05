package at.ac.tuwien.inso.refugeestories.domain;

import org.joda.time.DateTime;

/**
 * Created by nn on 2.12.2015.
 */
public class Story {

    private int id;
    private int authorId;
    private int imgResId;
    private String author;
    private String title;
    private String text;
    private DateTime date;
    private String location;

    public Story() { }

    public Story(int id, int authorId, int imgResId, String author,
                 String title, String text, DateTime date, String location) {
        this.id = id;
        this.authorId = authorId;
        this.imgResId = imgResId;
        this.author = author;
        this.title = title;
        this.text = text;
        this.date = date;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
