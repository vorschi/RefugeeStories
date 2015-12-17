package at.ac.tuwien.inso.refugeestories.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Language;
import at.ac.tuwien.inso.refugeestories.domain.Person;

/**
 * Created by mtraxler on 14.12.2015.
 */
public class LanguageControllerImpl implements ILanguageController {
    private static MyDatabaseHelper myDbHelper;
    private static LanguageControllerImpl instance;

    public static final String TABLE_NAME_LANGUAGE = "language";
    public static final String TABLE_NAME_USER_LANGUAGE = "user_language";

    public LanguageControllerImpl() {
    }

    public static synchronized void initializeInstance(MyDatabaseHelper helper) {
        if (instance == null) {
            instance = new LanguageControllerImpl();
            myDbHelper = helper;
        }
    }

    public static synchronized LanguageControllerImpl getInstance() {
        if (instance == null) {
            throw new IllegalStateException(LanguageControllerImpl.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public static abstract class TableEntryLanguage {
        public static final String ID = "id";
        public static final String LANGUAGE = "language";
    }

    public static abstract class TableEntryUserLanguage {
        public static final String AUTHORID = "authorid";
        public static final String LANGUAGEID = "languageid";
    }

    @Override
    public int createRecord(Person person, Language language) {
        ContentValues values = new ContentValues();

        values.put(TableEntryUserLanguage.AUTHORID, person.getId());
        values.put(TableEntryUserLanguage.LANGUAGEID, language.getId());

        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        int id = MyDatabaseHelper.safeLongToInt(db.insert(TABLE_NAME_USER_LANGUAGE, null, values));
        db.close();

        return id;
    }

    @Override
    public Language getSingleLanguage(int id) {
        String selection = TableEntryLanguage.ID + " = " + id;

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_LANGUAGE, null, selection, null, null, null, null);

        Language language = null;
        if(cursor.moveToNext()) {
            language = new Language();
            language.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntryLanguage.ID)));
            language.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(TableEntryLanguage.LANGUAGE)));
        }
        cursor.close();
        db.close();

        return language;
    }

    @Override
    public List<Language> getLanguagesByUserId(int userId) {
        String selection = TableEntryUserLanguage.AUTHORID + " = " + userId;

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_USER_LANGUAGE, null, selection, null, null, null, null);

        List<Language> languages = new ArrayList<Language>();
        Language language = null;
        while(cursor.moveToNext()) {
            language = getSingleLanguage(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntryUserLanguage.LANGUAGEID)));
            languages.add(language);
        }
        cursor.close();
        db.close();

        return languages;
    }

    @Override
    public List<Language> getAllLanguages() {

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_LANGUAGE, null, null, null, null, null, null);

        List<Language> languages = new ArrayList<Language>();
        Language language = null;
        while(cursor.moveToNext()) {
            language = new Language();
            language.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntryLanguage.ID)));
            language.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow(TableEntryLanguage.LANGUAGE)));
            languages.add(language);
        }
        cursor.close();
        db.close();

        return languages;
    }

    @Override
    public boolean deleteRecord(Person person, Language language) {
        String where = TableEntryUserLanguage.AUTHORID + " = ? AND " +
                TableEntryUserLanguage.LANGUAGEID + " = ?";
        String[] whereArgs = { Integer.toString(person.getId()), Integer.toString(language.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME_USER_LANGUAGE, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }

    @Override
    public boolean deleteRecordsByUser(Person person) {
        String where = TableEntryUserLanguage.AUTHORID + " = ?";
        String[] whereArgs = { Integer.toString(person.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME_USER_LANGUAGE, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }
}
