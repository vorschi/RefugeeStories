package at.ac.tuwien.inso.refugeestories.domain;

/**
 * Created by mtraxler on 14.12.2015.
 */
public class Image {

    private int id;
    private String img;
    private Story story;

    public Image() { }

    public Image(String img, Story story) {
        this.img = img;
        this.story = story;
    }

    public Image(int id, String img, Story story) {
        this.id = id;
        this.img = img;
        this.story = story;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getImg() { return img; }

    public void setImg(String img) { this.img = img; }

    public Story getStory() { return story; }

    public void setStory(Story story) { this.story = story; }

    @Override
    public String toString() {
        return "Image { id= " + id + ", img= '" + img + '\'' + '}';
    }
}
