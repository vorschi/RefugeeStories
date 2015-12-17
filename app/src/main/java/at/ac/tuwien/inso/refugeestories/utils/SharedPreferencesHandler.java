package at.ac.tuwien.inso.refugeestories.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import at.ac.tuwien.inso.refugeestories.domain.Person;
import at.ac.tuwien.inso.refugeestories.R;

/**
 * Created by mtraxler on 17.12.2015.
 */
public class SharedPreferencesHandler {

    private static final String TAG = SharedPreferencesHandler.class.getSimpleName();

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private final String USER = "user";

    public SharedPreferencesHandler(Context context) {
        sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.sharedPref), context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void clearSharedPreferences() {
        editor.clear();
        editor.commit();
        Log.i(TAG, "sharedPreferences cleared!");
    }

    public void putUser(Person person) {
        editor.putString(USER, MyJsonParser.toJson(person));
        editor.commit();
    }

    public Person getUser() {
        if("".equals(sharedPref.getString(USER, ""))) {
            return null;
        }
        return MyJsonParser.parseJson(sharedPref.getString(USER, ""), Person.class);
    }

    public void removeUser() {
        editor.remove(USER);
        editor.commit();
    }

    public boolean isUserLoggedIn() {
        return getUser() != null;
    }
}
