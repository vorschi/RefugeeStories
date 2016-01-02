package at.ac.tuwien.inso.refugeestories.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.domain.Image;
import at.ac.tuwien.inso.refugeestories.domain.Story;

/**
 * Created by mtraxler on 14.12.2015.
 */
public class ImageControllerImpl implements IImageController {

    private static MyDatabaseHelper myDbHelper;
    private static ImageControllerImpl instance;

    public static final String TABLE_NAME = "image";

    public ImageControllerImpl() {
    }

    public static synchronized void initializeInstance(MyDatabaseHelper helper) {
        if (instance == null) {
            instance = new ImageControllerImpl();
            myDbHelper = helper;
        }
    }

    public static synchronized ImageControllerImpl getInstance() {
        if (instance == null) {
            throw new IllegalStateException(ImageControllerImpl.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public static abstract class TableEntry {
        public static final String ID = "id";
        public static final String IMG = "img";
        public static final String STORYID = "storyid";
    }

    @Override
    public int createRecord(Image image) {
        ContentValues values = new ContentValues();

        values.put(TableEntry.IMG, image.getImg());
        values.put(TableEntry.STORYID, image.getStory().getId());

        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        int id = MyDatabaseHelper.safeLongToInt(db.insert(TABLE_NAME, null, values));
        db.close();

        return id;
    }

    @Override
    public Image getSingleImage(int id) {
        String selection = TableEntry.ID + " = " + id;

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, selection, null, null, null, null);

        Image image = null;
        StoryControllerImpl.initializeInstance(myDbHelper);
        if(cursor.moveToNext()) {
            image = new Image();
            image.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.ID)));
            image.setImg(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.IMG)));
            image.setStory(StoryControllerImpl.getInstance().getSingleStory(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.STORYID))));
        }
        cursor.close();
        db.close();

        return image;
    }

    @Override
    public List<Image> getImagesByStoryId(int storyId) {
        String selection = TableEntry.STORYID + " = " + storyId;

        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, selection, null, null, null, null);

        List<Image> images = new ArrayList<Image>();
        Image image = null;
        StoryControllerImpl.initializeInstance(myDbHelper);
        while(cursor.moveToNext()) {
            image = new Image();
            image.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.ID)));
            image.setImg(cursor.getString(cursor.getColumnIndexOrThrow(TableEntry.IMG)));
            image.setStory(StoryControllerImpl.getInstance().getSingleStory(cursor.getInt(cursor.getColumnIndexOrThrow(TableEntry.STORYID))));
            images.add(image);
        }
        cursor.close();
        db.close();

        return images;
    }

    @Override
    public boolean updateRecord(Image image) {
        ContentValues values = new ContentValues();

        values.put(TableEntry.IMG, image.getImg());
        values.put(TableEntry.STORYID, image.getStory().getId());

        String where = TableEntry.ID + " = ?";
        String[] whereArgs = { Integer.toString(image.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();

        boolean updateSuccessful = db.update(TABLE_NAME, values, where, whereArgs) > 0;
        db.close();

        return updateSuccessful;
    }

    @Override
    public boolean deleteAllRecords(Story story) {
        String where = TableEntry.STORYID + " = ?";
        String[] whereArgs = { Integer.toString(story.getId()) };
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean success = db.delete(TABLE_NAME, where, whereArgs) > 0;
        db.close();
        return success;
    }

    @Override
    public boolean deleteSingleRecord(Image image) {
        String where = TableEntry.ID + " = ?";
        String[] whereArgs = { Integer.toString(image.getId()) };

        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        boolean deleteSuccessful = db.delete(TABLE_NAME, where, whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }
}
