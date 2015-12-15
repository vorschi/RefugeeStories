package at.ac.tuwien.inso.refugeestories.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.utils.BitmapWorkerTask;

/**
 * Created by Amer Salkovic on 15.12.2015.
 */
public class FragmentImage extends Fragment {

    private static final String IMAGE_DATA_EXTRA = "imgPath";
    private String mImagePath;
    private ImageView mImageView;

    public static FragmentImage newInstance(String imgPath) {
        final FragmentImage instance = new FragmentImage();
        final Bundle args = new Bundle();
        args.putString(IMAGE_DATA_EXTRA, imgPath);
        instance.setArguments(args);
        return instance;
    }

    public FragmentImage() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImagePath = getActivity() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_image, container, false);
        mImageView = (ImageView) v.findViewById(R.id.imageView);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mImagePath != null && !"".equals(mImagePath)) {
            BitmapWorkerTask task = new BitmapWorkerTask(mImageView);
            task.execute(mImagePath);
        }
    }

}
