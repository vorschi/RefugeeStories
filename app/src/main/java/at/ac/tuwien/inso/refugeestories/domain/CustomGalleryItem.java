package at.ac.tuwien.inso.refugeestories.domain;

/**
 * Created by Amer Salkovic on 15.12.2015.
 */
public class CustomGalleryItem {

    public String sdcardPath;
    public boolean isSelected = false;

    public CustomGalleryItem() { }

    public CustomGalleryItem(String sdcardPath) {
        this.sdcardPath = sdcardPath;
    }
}
