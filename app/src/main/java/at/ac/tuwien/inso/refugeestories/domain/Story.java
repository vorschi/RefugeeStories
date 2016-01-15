package at.ac.tuwien.inso.refugeestories.domain;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

/**
 * Created by Amer Salkovic on 2.12.2015.
 */
public class Story {

    private int id;
    private String title;
    private String text;
    private Date date;
    private String location;
    private double lat;
    private double lng;
    private List<Image> images;
    private Person author;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() { return lng; }

    public void setLng(double lng) { this.lng = lng; }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }
}
