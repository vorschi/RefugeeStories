package at.ac.tuwien.inso.refugeestories.domain;

/**
 * Created by mtraxler on 14.12.2015.
 */
public class Language {

    private int id;
    private String language;

    public Language() {
    }

    public Language(int id, String language) {
        this.id = id;
        this.language = language;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getLanguage() { return language; }

    public void setLanguage(String language) { this.language = language; }
}
