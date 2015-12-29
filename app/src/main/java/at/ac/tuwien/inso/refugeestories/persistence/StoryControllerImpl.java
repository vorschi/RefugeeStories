package at.ac.tuwien.inso.refugeestories.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Image;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.utils.Consts;
import at.ac.tuwien.inso.refugeestories.utils.Utils;

/**
 * Created by mtraxler, Amer Salkovic on 14.12.2015.
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

    @Override
    public int createRecord(Story story) {
        ContentValues values = new ContentValues();

        values.put(TableEntry.TITLE, story.getTitle());
        values.put(TableEntry.TEXT, story.getText());
        values.put(TableEntry.DATE, Utils.dateFormat.format(story.getDate()));
        values.put(TableEntry.LOCATION, story.getLocation());
        values.put(TableEntry.AUTHORID, story.getAuthor().getId());

        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        int id = MyDatabaseHelper.safeLongToInt(db.insert(TABLE_NAME, null, values));
        db.close();

        return id;
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
            try {
                story.setDate(Utils.dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            story.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.LOCATION)));
            story.setAuthor(UserControllerImpl.getInstance().getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.AUTHORID))));
        }
        cursor.close();
        db.close();

        return story;
    }

    /**
     * This method return stories for the FragmentExplore
     * @param limit - number of stories that should be loaded
     * @param offset - starting point
     * @param userId - id of a current user, in order to avoid fetching of his own stories
     * @return list of stories for explore view
     */
    public List<Story> getOrderedStories(int limit, int offset, int userId, String column, String order) {
        SQLiteDatabase db = myDbHelper.getReadableDatabase();

        String query = "SELECT " + TableEntry.ID + ", " + TableEntry.TITLE + ", " + TableEntry.TEXT + ", " +
                       "substr(date, 7, 4)||\"-\"||substr(date, 4, 2)||\"-\"||substr(date,0,3) as date, " +
                       TableEntry.LOCATION + ", " + TableEntry.AUTHORID +
                       " FROM " + TABLE_NAME +
                       " WHERE " + TableEntry.AUTHORID + " <> " + userId +
                       " ORDER BY " + column + " " + order +
                       " LIMIT " + limit + " OFFSET " + offset;

        Log.i(StoryControllerImpl.class.getSimpleName(), query);
        Cursor cursor = db.rawQuery(query, null);

        List<Story> stories = new ArrayList<>();
        Story story = null;
        UserControllerImpl.initializeInstance(myDbHelper);
        while(cursor.moveToNext()) {
            story = new Story();
            story.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.ID)));
            story.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TITLE)));
            story.setText(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TEXT)));
            try {
                story.setDate(Utils.customDateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            story.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.LOCATION)));
            story.setAuthor(UserControllerImpl.getInstance().getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.AUTHORID))));
            stories.add(story);
        }
        cursor.close();
        db.close();

        return stories;
    }

    @Override
    public List<Story> getStoriesByUserId(int limit, int offset, int userId) {

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        String query = "SELECT * FROM STORY WHERE authorid = " + userId + " ORDER BY ID DESC LIMIT " + limit + " OFFSET " + offset;
        Log.i(StoryControllerImpl.class.getSimpleName(), query);
        Cursor cursor = db.rawQuery(query, null);

        List<Story> stories = new ArrayList<Story>();
        Story story = null;
        UserControllerImpl.initializeInstance(myDbHelper);
        while(cursor.moveToNext()) {
            story = new Story();
            story.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.ID)));
            story.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TITLE)));
            story.setText(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TEXT)));
            try {
                story.setDate(Utils.dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            story.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.LOCATION)));
            story.setAuthor(UserControllerImpl.getInstance().getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.AUTHORID))));
            stories.add(story);
        }
        cursor.close();
        db.close();

        return stories;
    }

    public List<Story> getStoriesByUserIdFromStoryId(int authorId, int lastLoadedStoryId, int limit) {
        SQLiteDatabase db = myDbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TableEntry.AUTHORID + " = " + authorId + " AND " + TableEntry.ID + " > " + lastLoadedStoryId + " LIMIT " + limit;

        Log.i(StoryControllerImpl.class.getSimpleName(), query);
        Cursor cursor = db.rawQuery(query, null);

        List<Story> stories = new ArrayList<Story>();
        Story story = null;
        UserControllerImpl.initializeInstance(myDbHelper);
        while(cursor.moveToNext()) {
            story = new Story();
            story.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.ID)));
            story.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TITLE)));
            story.setText(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TEXT)));
            try {
                story.setDate(Utils.dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            story.setLocation(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.LOCATION)));
            story.setAuthor(UserControllerImpl.getInstance().getSingleRecord(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.AUTHORID))));
            stories.add(story);
        }
        cursor.close();
        db.close();

        return stories;
    }

    public List<Story> getStoryPredecessorsAndTwoSuccessors(int authorId, int storyId, int limit) {
        SQLiteDatabase db = myDbHelper.getReadableDatabase();

        String query = "SELECT * FROM " +
                       "(SELECT * FROM " + TABLE_NAME + " WHERE " + TableEntry.AUTHORID + " = " + authorId + " AND " + TableEntry.ID + " > " + storyId + " LIMIT " + limit + ") " +
                       "UNION " +
                       "SELECT * FROM " + TABLE_NAME + " WHERE " + TableEntry.AUTHORID + " = " + authorId + " AND " + TableEntry.ID + " <= " + storyId;

        Log.i(StoryControllerImpl.class.getSimpleName(), query);
        Cursor cursor = db.rawQuery(query, null);

        List<Story> stories = new ArrayList<Story>();
        Story story = null;
        UserControllerImpl.initializeInstance(myDbHelper);
        while(cursor.moveToNext()) {
            story = new Story();
            story.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.ID)));
            story.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TITLE)));
            story.setText(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.TEXT)));
            try {
                story.setDate(Utils.dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
            try {
                story.setDate(Utils.dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
            try {
                story.setDate(Utils.dateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.DATE))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
        values.put(TableEntry.DATE, Utils.dateFormat.format(story.getDate()));
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
