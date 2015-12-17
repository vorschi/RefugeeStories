package at.ac.tuwien.inso.refugeestories.persistence;

import at.ac.tuwien.inso.refugeestories.domain.Person;

/**
 * Created by mtraxler on 14.12.2015.
 */
public interface IUserController {

    int createRecord(Person person);
    Person getSingleRecord(int id);
    boolean updateRecord(Person person);
    boolean deleteRecord(Person person);

}
