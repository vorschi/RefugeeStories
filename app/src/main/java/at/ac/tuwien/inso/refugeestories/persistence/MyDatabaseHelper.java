package at.ac.tuwien.inso.refugeestories.persistence;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


/**
 * Created by mtraxler on 14.12.2015.
 */
public class MyDatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "refugeediaries.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
