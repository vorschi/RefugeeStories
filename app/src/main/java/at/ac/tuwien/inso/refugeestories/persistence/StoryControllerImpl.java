package at.ac.tuwien.inso.refugeestories.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Image;
import at.ac.tuwien.inso.refugeestories.domain.Story;

/**
 * Created by mtraxler on 14.12.2015.
 */
public class StoryControllerImpl implements IStoryController {

    private static MyDatabaseHelper myDbHelper;
    private static StoryControllerImpl instance;

    public static final String TABLE_NAME = "story";

    public StoryControllerImpl() {
    }

    public static synchronized void initializeInstance(MyDatabaseHelper helper) {
        if (instance == null) {
            instance = new StoryControllerImpl();
            myDbHelper = helper;
        }
    }

    public static synchronized StoryControllerImpl getInstance() {
        if (instance == null) {
            throw new IllegalStateException(StoryControllerImpl.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public static abstract class TableEntry {
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String TEXT = "text";
        public static final String DATE = "date";
        public static final String LOCATION = "location";
        public static final String AUTHORID = "authorid";
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("dd.MM.yyyy");

    @Override
    public boolean createRecord(Story story) {
        ContentValues values = new ContentValues();

        values.put(TableEntry.TITLE, story.getTitle());
        values.put(TableEntry.TEXT, story.getText());
        values.put(TableEntry.DATE, dateFormat.format(story.getDate()));
        values.put(TableEntry.LOCATION, story.getLocation());
        values.put(TableEntry.AUTHORID, story.getAuthor().getId());

        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        boolean createSuccessful = db.insert(TABLE_NAME, null, values) > 0;
        db.close();

        return createSuccessful;
    }

    @Override
    public Story getSingleStory(int id) {
        String selection = TableEntry.ID + " = " + id;

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, selection, null, null, null, null);

        Story story = null;
        UserControllerImpl.initializeInstance(myDbHelper);
        if(cursor.moveToNext()) {
            story = new Story();
            story.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.ID)));
            story.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TITLE)));
            story.setText(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TEXT)));
            story.setDate(dateFormatter.parseDateTime(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.DATE))));
            story.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.LOCATION)));
            story.setAuthor(UserControllerImpl.getInstance().getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.AUTHORID))));
        }
        cursor.close();
        db.close();

        return story;
    }

    @Override
    public List<Story> getStoriesByUserId(int userId) {
        String selection = TableEntry.AUTHORID + " = " + userId;

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, selection, null, null, null, null);

        List<Story> stories = new ArrayList<Story>();
        Story story = null;
        UserControllerImpl.initializeInstance(myDbHelper);
        while(cursor.moveToNext()) {
            story = new Story();
            story.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.ID)));
            story.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TITLE)));
            story.setText(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TEXT)));
            story.setDate(dateFormatter.parseDateTime(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.DATE))));
            story.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.LOCATION)));
            story.setAuthor(UserControllerImpl.getInstance().getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.AUTHORID))));
            stories.add(story);
        }
        cursor.close();
        db.close();

        return stories;
    }

    @Override
    public List<Story> getAllStories() {

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        List<Story> stories = new ArrayList<Story>();
        Story story = null;
        UserControllerImpl.initializeInstance(myDbHelper);
        while(cursor.moveToNext()) {
            story = new Story();
            story.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.ID)));
            story.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TITLE)));
            story.setText(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TEXT)));
            story.setDate(dateFormatter.parseDateTime(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.DATE))));
            story.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.LOCATION)));
            story.setAuthor(UserControllerImpl.getInstance().getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.AUTHORID))));
            stories.add(story);
        }
        cursor.close();
        db.close();

        return stories;
    }

    @Override
    public List<Story> getRandomStories(int count) {
        String orderBy = " RANDOM() LIMIT " + count;

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, orderBy);

        List<Story> stories = new ArrayList<Story>();
        Story story = null;
        UserControllerImpl.initializeInstance(myDbHelper);
        while(cursor.moveToNext()) {
            story = new Story();
            story.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.ID)));
            story.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TITLE)));
            story.setText(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TEXT)));
            story.setDate(dateFormatter.parseDateTime(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.DATE))));
            story.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.LOCATION)));
            story.setAuthor(UserControllerImpl.getInstance().getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.AUTHORID))));
            stories.add(story);
        }
        cursor.close();
        db.close();

        return stories;
    }

    @Override
    public boolean updateRecord(Story story) {
        ContentValues values = new ContentValues();

        values.put(TableEntry.TITLE, story.getTitle());
        values.put(TableEntry.TEXT, story.getText());
        values.put(TableEntry.DATE, dateFormat.format(story.getDate()));
        values.put(TableEntry.LOCATION, story.getLocation());
        values.put(TableEntry.AUTHORID, story.getAuthor().getId());

        String where = TableEntry.ID + " = ?";
        String[] whereArgs = { Integer.toString(story.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        boolean updateSuccessful = db.update(TABLE_NAME, values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;
    }

    @Override
    public boolean deleteRecord(Story story) {
        //delete all related images
        ImageControllerImpl.initializeInstance(myDbHelper);
        for(Image image : story.getImages()) {
            ImageControllerImpl.getInstance().deleteRecord(image);
        }

        String where = TableEntry.ID + " = ?";
        String[] whereArgs = { Integer.toString(story.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }
}
