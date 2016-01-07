package at.ac.tuwien.inso.refugeestories.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Amer Salkovic on 15.11.2015.
 */
public class Utils {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static SimpleDateFormat customDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        //currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar date1 = getCalendar(first);
        Calendar date2 = getCalendar(last);
        int diff = date2.get(Calendar.YEAR) - date1.get(Calendar.YEAR);
        if (date1.get(Calendar.MONTH) > date2.get(Calendar.MONTH) ||
                (date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH) && date1.get(Calendar.DAY_OF_MONTH) > date2.get(Calendar.DAY_OF_MONTH))) {
            diff--;
        }
        return diff;
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }
}
