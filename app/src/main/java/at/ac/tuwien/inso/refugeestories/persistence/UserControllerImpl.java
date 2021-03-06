package at.ac.tuwien.inso.refugeestories.persistence;

import android.app.ActionBar;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Person;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.utils.Utils;

/**
 * Created by mtraxler on 14.12.2015.
 */
public class UserControllerImpl implements IUserController {
    private static MyDatabaseHelper myDbHelper;
    private static UserControllerImpl instance;

    public static final String TABLE_NAME = "user";
    public static final String TABLE_NAME_FOLLOWER = "user_follower";
    public static final String TABLE_NAME_LIKER = "user_liker";
    public static final String TABLE_NAME_MEETING = "user_meeting";

    public UserControllerImpl() {
    }

    public static synchronized void initializeInstance(MyDatabaseHelper helper) {
        if (instance == null) {
            instance = new UserControllerImpl();
            myDbHelper = helper;
        }
    }

    public static synchronized UserControllerImpl getInstance() {
        if (instance == null) {
            throw new IllegalStateException(UserControllerImpl.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public static abstract class TableEntry {
        public static final String ID = "id";
        public static final String FIRSTNAME = "firstname";
        public static final String LASTNAME = "lastname";
        public static final String PASSWORD = "password";
        public static final String MAIL = "mail";
        public static final String NATIONALITY = "nationality";
        public static final String IMG = "img";
        public static final String DOB = "dob";
        public static final String GENDER = "gender";
        public static final String LOCATION = "location";
        public static final String LAT = "lat";
        public static final String LNG = "lng";
        public static final String INTERESTS = "interests";
    }

    public static abstract class TableEntryFollower {
        public static final String AUTHORID1 = "authorid1";
        public static final String AUTHORID2 = "authorid2";
    }

    public static abstract class TableEntryLiker {
        public static final String AUTHORID1 = "authorid1";
        public static final String AUTHORID2 = "authorid2";
    }

    public static abstract class TableEntryMeeting {
        public static final String AUTHORID1 = "authorid1";
        public static final String AUTHORID2 = "authorid2";
    }

    @Override
    public int createRecord(Person person) {
        ContentValues values = new ContentValues();

        values.put(TableEntry.FIRSTNAME, person.getFirstname());
        values.put(TableEntry.LASTNAME, person.getLastname());
        values.put(TableEntry.PASSWORD, person.getPassword());
        values.put(TableEntry.MAIL, person.getEmail());
        values.put(TableEntry.NATIONALITY, person.getNationality());
        values.put(TableEntry.IMG, person.getImg());
        values.put(TableEntry.DOB, Utils.dateFormat.format(person.getDob()));
        values.put(TableEntry.GENDER, person.getGender());
        values.put(TableEntry.LOCATION, person.getLocation());
        values.put(TableEntry.LAT, person.getLat());
        values.put(TableEntry.LNG, person.getLng());
        values.put(TableEntry.INTERESTS, person.getInterests());
        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        int id = MyDatabaseHelper.safeLongToInt(db.insert(TABLE_NAME, null, values));
        db.close();

        return id;
    }

    @Override
    public Person getSingleRecord(int id) {

        String selection = TableEntry.ID + " = " + id;

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, selection, null, null, null, null);

        Person person = null;
        LanguageControllerImpl.initializeInstance(myDbHelper);
        if(cursor.moveToNext()) {
            person = new Person();
            person.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.ID)));
            person.setFirstname(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.FIRSTNAME)));
            person.setLastname(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.LASTNAME)));
            person.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.PASSWORD)));
            person.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.MAIL)));
            person.setNationality(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.NATIONALITY)));
            person.setImg(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.IMG)));
            try {
                person.setDob(Utils.dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.DOB))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            person.setGender(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.GENDER)));
            person.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.LOCATION)));
            person.setLat(cursor.getDouble(cursor.getColumnIndexOrThrow(TableEntry.LAT)));
            person.setLng(cursor.getDouble(cursor.getColumnIndexOrThrow(TableEntry.LNG)));
            person.setInterests(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.INTERESTS)));
            person.setLanguages(LanguageControllerImpl.getInstance().getLanguagesByUserId(person.getId()));
        }
        cursor.close();
        db.close();

        return person;
    }

    @Override
    public boolean updateRecord(Person person) {
        ContentValues values = new ContentValues();

        values.put(TableEntry.FIRSTNAME, person.getFirstname());
        values.put(TableEntry.LASTNAME, person.getLastname());
        values.put(TableEntry.PASSWORD, person.getPassword());
        values.put(TableEntry.MAIL, person.getEmail());
        values.put(TableEntry.NATIONALITY, person.getNationality());
        values.put(TableEntry.IMG, person.getImg());
        values.put(TableEntry.DOB, Utils.dateFormat.format(person.getDob()));
        values.put(TableEntry.GENDER, person.getGender());
        values.put(TableEntry.LOCATION, person.getLocation());
        values.put(TableEntry.LAT, person.getLat());
        values.put(TableEntry.LNG, person.getLng());
        values.put(TableEntry.INTERESTS, person.getInterests());
        String where = TableEntry.ID + " = ?";
        String[] whereArgs = { Integer.toString(person.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        boolean updateSuccessful = db.update(TABLE_NAME, values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;
    }

    @Override
    public boolean deleteRecord(Person person) {

        //delete all related stories
        StoryControllerImpl.initializeInstance(myDbHelper);
        for(Story story : person.getStories()) {
            StoryControllerImpl.getInstance().deleteRecord(story);
        }

        //delete all related language assignments
        LanguageControllerImpl.initializeInstance(myDbHelper);
        LanguageControllerImpl.getInstance().deleteRecordsByUser(person);

        //delete all related follow assignments
        deleteFollowerRecordsByUser(person);
        deleteFollowerRecordsByFollower(person);

        //delete all related like assignments
        deleteLikerRecordsByUser(person);
        deleteLikerRecordsByLiker(person);

        String where = TableEntry.ID + " = ?";
        String[] whereArgs = { Integer.toString(person.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }

    @Override
    public int createFollowerRecord(Person toFollow, Person follower) {
        ContentValues values = new ContentValues();

        values.put(TableEntryFollower.AUTHORID1, toFollow.getId());
        values.put(TableEntryFollower.AUTHORID2, follower.getId());

        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        int id = MyDatabaseHelper.safeLongToInt(db.insert(TABLE_NAME_FOLLOWER, null, values));
        db.close();

        return id;
    }

    @Override
    public List<Person> getFollowerByUserId(int userId) {
        String selection = TableEntryFollower.AUTHORID1 + " = " + userId;
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_FOLLOWER, null, selection, null, null, null, null);

        List<Person> followers = new ArrayList<Person>();
        Person follower = null;
        while(cursor.moveToNext()) {
            follower = getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntryFollower.AUTHORID2)));
            followers.add(follower);
        }
        cursor.close();
        db.close();

        return followers;
    }

    @Override
    public List<Person> getFollowingByUserId(int userId) {
        String selection = TableEntryFollower.AUTHORID2 + " = " + userId;
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_FOLLOWER, null, selection, null, null, null, null);

        List<Person> followingUsers = new ArrayList<Person>();
        Person following = null;
        while(cursor.moveToNext()) {
            following = getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntryFollower.AUTHORID1)));
            followingUsers.add(following);
        }
        cursor.close();
        db.close();

        return followingUsers;
    }

    @Override
    public boolean deleteFollowerRecord(Person toFollow, Person follower) {
        String where = TableEntryFollower.AUTHORID1 + " = ? AND " +
                TableEntryFollower.AUTHORID2 + " = ?";
        String[] whereArgs = { Integer.toString(toFollow.getId()), Integer.toString(follower.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME_FOLLOWER, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }

    @Override
    public boolean deleteFollowerRecordsByUser(Person toFollow) {
        String where = TableEntryFollower.AUTHORID1 + " = ?";
        String[] whereArgs = { Integer.toString(toFollow.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME_FOLLOWER, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }

    @Override
    public boolean deleteFollowerRecordsByFollower(Person follower) {
        String where = TableEntryFollower.AUTHORID2 + " = ?";
        String[] whereArgs = { Integer.toString(follower.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME_FOLLOWER, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }

    @Override
    public int createLikeRecord(Person toLike, Person liker) {
        ContentValues values = new ContentValues();

        values.put(TableEntryLiker.AUTHORID1, toLike.getId());
        values.put(TableEntryLiker.AUTHORID2, liker.getId());

        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        int id = MyDatabaseHelper.safeLongToInt(db.insert(TABLE_NAME_LIKER, null, values));
        db.close();

        return id;
    }

    @Override
    public List<Person> getLikerByUserId(int userId) {
        String selection = TableEntryLiker.AUTHORID1 + " = " + userId;
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_LIKER, null, selection, null, null, null, null);

        List<Person> likers = new ArrayList<Person>();
        Person liker = null;
        while(cursor.moveToNext()) {
            liker = getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntryLiker.AUTHORID2)));
            likers.add(liker);
        }
        cursor.close();
        db.close();

        return likers;
    }

    @Override
    public List<Person> getLikedUsersByUserId(int userId) {
        String selection = TableEntryLiker.AUTHORID2 + " = " + userId;
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_LIKER, null, selection, null, null, null, null);

        List<Person> likedUsers = new ArrayList<Person>();
        Person likedUser = null;
        while(cursor.moveToNext()) {
            likedUser = getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntryLiker.AUTHORID1)));
            likedUsers.add(likedUser);
        }
        cursor.close();
        db.close();

        return likedUsers;
    }

    @Override
    public boolean deleteLikerRecord(Person toLike, Person liker) {
        String where = TableEntryLiker.AUTHORID1 + " = ? AND " +
                TableEntryLiker.AUTHORID2 + " = ?";
        String[] whereArgs = { Integer.toString(toLike.getId()), Integer.toString(liker.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME_LIKER, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }

    @Override
    public boolean deleteLikerRecordsByUser(Person toLike) {
        String where = TableEntryLiker.AUTHORID1 + " = ?";
        String[] whereArgs = { Integer.toString(toLike.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME_LIKER, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }

    @Override
    public boolean deleteLikerRecordsByLiker(Person liker) {
        String where = TableEntryLiker.AUTHORID2 + " = ?";
        String[] whereArgs = { Integer.toString(liker.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME_LIKER, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }

    @Override
    public int createMeetingRecord(Person toMeet, Person meeter) {
        ContentValues values = new ContentValues();

        values.put(TableEntryMeeting.AUTHORID1, toMeet.getId());
        values.put(TableEntryMeeting.AUTHORID2, meeter.getId());

        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        int id = MyDatabaseHelper.safeLongToInt(db.insert(TABLE_NAME_MEETING, null, values));
        db.close();

        return id;
    }

    @Override
    public List<Person> getMeeterByUserId(int userId) {
        String selection = TableEntryMeeting.AUTHORID1 + " = " + userId;
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MEETING, null, selection, null, null, null, null);

        List<Person> meeters = new ArrayList<Person>();
        Person meeter = null;
        while(cursor.moveToNext()) {
            meeter = getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntryMeeting.AUTHORID2)));
            meeters.add(meeter);
        }
        cursor.close();
        db.close();

        return meeters;
    }

    @Override
    public List<Person> getRequestedMeetingUsersByUserId(int userId) {
        String selection = TableEntryMeeting.AUTHORID2 + " = " + userId;
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_MEETING, null, selection, null, null, null, null);

        List<Person> requestedMeetingUsers = new ArrayList<Person>();
        Person requestedMeetingUser = null;
        while(cursor.moveToNext()) {
            requestedMeetingUser = getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntryMeeting.AUTHORID1)));
            requestedMeetingUsers.add(requestedMeetingUser);
        }
        cursor.close();
        db.close();

        return requestedMeetingUsers;
    }

    @Override
    public boolean deleteMeetingRecord(Person toMeet, Person meeter) {
        String where = TableEntryMeeting.AUTHORID1 + " = ? AND " +
                TableEntryMeeting.AUTHORID2 + " = ?";
        String[] whereArgs = { Integer.toString(toMeet.getId()), Integer.toString(meeter.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME_MEETING, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }

    @Override
    public boolean deleteMeetingRecordsByUser(Person toMeet) {
        String where = TableEntryMeeting.AUTHORID1 + " = ?";
        String[] whereArgs = { Integer.toString(toMeet.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME_MEETING, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }

    @Override
    public boolean deleteMeetingRecordsByMeeter(Person meeter) {
        String where = TableEntryMeeting.AUTHORID2 + " = ?";
        String[] whereArgs = { Integer.toString(meeter.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME_MEETING, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }
}
