package at.ac.tuwien.inso.refugeestories.utils.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by Amer Salkovic on 15.12.2015.
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

    public static final int AUTHOR_ID = 0;
    public static final int OFFSET = 1;

    private final WeakReference<ImageView> imageViewReference;

    public BitmapWorkerTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return BitmapFactory.decodeFile(params[0]);
    }

    /*@Override
    protected Bitmap doInBackground(String... params) {
        Bitmap original = BitmapFactory.decodeFile(params[0]);
        if (original != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 10, out);
            Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            return decoded;
        } else {
            return original;
        }
    }*/


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
