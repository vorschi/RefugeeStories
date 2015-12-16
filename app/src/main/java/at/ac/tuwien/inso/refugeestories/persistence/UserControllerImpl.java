package at.ac.tuwien.inso.refugeestories.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import at.ac.tuwien.inso.refugeestories.domain.Person;
import at.ac.tuwien.inso.refugeestories.domain.Story;

/**
 * Created by mtraxler on 14.12.2015.
 */
public class UserControllerImpl implements IUserController {
    private static MyDatabaseHelper myDbHelper;
    private static UserControllerImpl instance;

    public static final String TABLE_NAME = "user";

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
    }

    @Override
    public boolean createRecord(Person person) {
        ContentValues values = new ContentValues();

        values.put(TableEntry.FIRSTNAME, person.getFistname());
        values.put(TableEntry.LASTNAME, person.getLastname());
        values.put(TableEntry.USERNAME, person.getUsername());
        values.put(TableEntry.PASSWORD, person.getPassword());
        values.put(TableEntry.MAIL, person.getEmail());
        values.put(TableEntry.NATIONALITY, person.getNationality());
        values.put(TableEntry.IMG, person.getImg());
        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        boolean createSuccessful = db.insert(TABLE_NAME, null, values) > 0;
        db.close();

        return createSuccessful;
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
            person.setFistname(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.FIRSTNAME)));
            person.setLastname(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.LASTNAME)));
            person.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.USERNAME)));
            person.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.PASSWORD)));
            person.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.MAIL)));
            person.setNationality(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.NATIONALITY)));
            person.setImg(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.IMG)));
            person.setLanguages(LanguageControllerImpl.getInstance().getLanguagesByUserId(person.getId()));
        }
        cursor.close();
        db.close();

        return person;
    }

    @Override
    public boolean updateRecord(Person person) {
        ContentValues values = new ContentValues();

        values.put(TableEntry.FIRSTNAME, person.getFistname());
        values.put(TableEntry.LASTNAME, person.getLastname());
        values.put(TableEntry.USERNAME, person.getUsername());
        values.put(TableEntry.PASSWORD, person.getPassword());
        values.put(TableEntry.MAIL, person.getEmail());
        values.put(TableEntry.NATIONALITY, person.getNationality());
        values.put(TableEntry.IMG, person.getImg());
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

        String where = TableEntry.ID + " = ?";
        String[] whereArgs = { Integer.toString(person.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }
}
