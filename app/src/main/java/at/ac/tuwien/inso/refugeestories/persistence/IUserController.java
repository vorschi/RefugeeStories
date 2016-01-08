package at.ac.tuwien.inso.refugeestories.persistence;

import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Person;

/**
 * Created by mtraxler on 14.12.2015.
 */
public interface IUserController {

    int createRecord(Person person);
    Person getSingleRecord(int id);
    boolean updateRecord(Person person);
    boolean deleteRecord(Person person);

    int createFollowerRecord(Person toFollow, Person follower);
    List<Person> getFollowerByUserId(int userId);
    List<Person> getFollowingByUserId(int userId);
    boolean deleteFollowerRecord(Person toFollow, Person follower);
    boolean deleteFollowerRecordsByUser(Person toFollow);
    boolean deleteFollowerRecordsByFollower(Person follower);

    int createLikeRecord(Person toLike, Person liker);
    List<Person> getLikerByUserId(int userId);
    List<Person> getLikedUsersByUserId(int userId);
    boolean deleteLikerRecord(Person toLike, Person liker);
    boolean deleteLikerRecordsByUser(Person toLike);
    boolean deleteLikerRecordsByLiker(Person liker);
}
