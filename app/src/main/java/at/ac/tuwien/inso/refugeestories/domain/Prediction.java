package at.ac.tuwien.inso.refugeestories.domain;

/**
 * Created by Amer Salkovic on 14.1.2016.
 */
public class Prediction {

    private String description;
    private String placeId;

    public Prediction() { }

    public Prediction(String description, String placeId) {
        this.description = description;
        this.placeId = placeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Override
    public String toString() {
        return "description: " + description + ", placeId: " + placeId;
    }
}
