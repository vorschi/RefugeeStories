package at.ac.tuwien.inso.refugeestories.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
