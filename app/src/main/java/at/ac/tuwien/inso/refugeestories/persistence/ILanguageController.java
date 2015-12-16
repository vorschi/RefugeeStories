package at.ac.tuwien.inso.refugeestories.persistence;

import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Language;
import at.ac.tuwien.inso.refugeestories.domain.Person;

/**
 * Created by mtraxler on 14.12.2015.
 */
public interface ILanguageController {

    boolean createRecord(Person person, Language language);
    Language getSingleLanguage(int id);
    List<Language> getLanguagesByUserId(int userId);
    List<Language> getAllLanguages();
    boolean deleteRecord(Person person, Language language);
    boolean deleteRecordsByUser(Person person);
}
