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
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String MAIL = "mail";
        public static final String NATIONALITY = "nationality";
        public static final String IMG = "img";
        public static final String DOB = "dob";
        public static final String GENDER = "gender";
        public static final String LOCATION = "location";
        public static final String INTERESTS = "interests";
    }

    public static abstract class TableEntryFollower {
        public static final String AUTHORID1 = "authorid1";
        public static final String AUTHORID2 = "authorid2";
    }

    @Override
    public int createRecord(Person person) {
        ContentValues values = new ContentValues();

        values.put(TableEntry.FIRSTNAME, person.getFirstname());
        values.put(TableEntry.LASTNAME, person.getLastname());
        values.put(TableEntry.USERNAME, person.getUsername());
        values.put(TableEntry.PASSWORD, person.getPassword());
        values.put(TableEntry.MAIL, person.getEmail());
        values.put(TableEntry.NATIONALITY, person.getNationality());
        values.put(TableEntry.IMG, person.getImg());
        values.put(TableEntry.DOB, Utils.dateFormat.format(person.getDob()));
        values.put(TableEntry.GENDER, person.getGender());
        values.put(TableEntry.LOCATION, person.getLocation());
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
            person.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.USERNAME)));
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
        values.put(TableEntry.USERNAME, person.getUsername());
        values.put(TableEntry.PASSWORD, person.getPassword());
        values.put(TableEntry.MAIL, person.getEmail());
        values.put(TableEntry.NATIONALITY, person.getNationality());
        values.put(TableEntry.IMG, person.getImg());
        values.put(TableEntry.DOB, Utils.dateFormat.format(person.getDob()));
        values.put(TableEntry.GENDER, person.getGender());
        values.put(TableEntry.LOCATION, person.getLocation());
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
}
