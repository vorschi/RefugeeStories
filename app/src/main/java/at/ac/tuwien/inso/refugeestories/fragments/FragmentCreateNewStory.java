package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.ac.tuwien.inso.refugeestories.MainActivity;
import at.ac.tuwien.inso.refugeestories.R;
import at.ac.tuwien.inso.refugeestories.domain.CustomGalleryItem;
import at.ac.tuwien.inso.refugeestories.domain.Image;
import at.ac.tuwien.inso.refugeestories.domain.Story;
import at.ac.tuwien.inso.refugeestories.persistence.ImageControllerImpl;
import at.ac.tuwien.inso.refugeestories.persistence.MyDatabaseHelper;
import at.ac.tuwien.inso.refugeestories.persistence.StoryControllerImpl;
import at.ac.tuwien.inso.refugeestories.utils.Consts;
import at.ac.tuwien.inso.refugeestories.utils.components.ExpandableGridView;
import at.ac.tuwien.inso.refugeestories.utils.SharedPreferencesHandler;
import at.ac.tuwien.inso.refugeestories.utils.Utils;
import at.ac.tuwien.inso.refugeestories.utils.adapters.GalleryAdapter;

/**
 * Created by Amer Salkovic on 14.11.2015.
 */
public class FragmentCreateNewStory extends Fragment implements OnDateSetListener {

    private final String TAG = FragmentCreateNewStory.class.getSimpleName();

    private Context context;

    private TextView storyTitle;
    private TextView storyLocation;
    private TextView storyDate;
    private TextView storyText;

    private Button btnPublishStory;
    private Button btnAddPhotos;
    private Button btnClearAllPhotos;

    //private String[] selectedImages;
    private List<String> selectedImages;

    private ExpandableGridView gridGallery;
    private GalleryAdapter adapter;

    private ImageLoader imageLoader;

    //TODO prepare everything for for story editing...
    private Story story;

    private SharedPreferencesHandler sharedPrefs;

    private StoryControllerImpl storyControllerInstance;
    private ImageControllerImpl imageControllerInstance;
    private MyDatabaseHelper dbHelper;

    //dialog
    AlertDialog.Builder builder;
    AlertDialog selectPhotosDialog;
    private String currentPhotoPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_create_new_story, container, false);

        //init db, shared preferences and imageLoader
        dbHelper = new MyDatabaseHelper(getActivity().getBaseContext());
        StoryControllerImpl.initializeInstance(dbHelper);
        storyControllerInstance = StoryControllerImpl.getInstance();
        ImageControllerImpl.initializeInstance(dbHelper);
        imageControllerInstance = ImageControllerImpl.getInstance();

        sharedPrefs = new SharedPreferencesHandler(getActivity());

        selectedImages = new ArrayList<>();
        initImageLoader();

        //init other components
        storyTitle = (TextView) contentView.findViewById(R.id.new_story_title);

        storyLocation = (TextView) contentView.findViewById(R.id.new_story_location);

        storyDate = (TextView) contentView.findViewById(R.id.new_story_date);
        storyDate.setText(Utils.dateFormat.format(new Date()));
        storyDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DialogFragment newFragment = new DatePickerDialogFragment(FragmentCreateNewStory.this);
                newFragment.show(ft, "date_dialog");
            }
        });

        storyText = (TextView) contentView.findViewById(R.id.new_story_text);

        //init gallery
        gridGallery = (ExpandableGridView) contentView.findViewById(R.id.gridGallery);
        gridGallery.setExpanded(true);
        adapter = new GalleryAdapter(context, imageLoader);
        gridGallery.setAdapter(adapter);

        //controls
        btnAddPhotos = (Button) contentView.findViewById(R.id.btn_add_photos);
        btnAddPhotos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createSelectPhotosDialog();
                selectPhotosDialog.show();
            }
        });

        btnClearAllPhotos = (Button) contentView.findViewById(R.id.btn_clear_all_photos);
        btnClearAllPhotos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImages.clear();
                adapter.clear();
                btnClearAllPhotos.setVisibility(Button.GONE);
            }
        });

        btnPublishStory = (Button) contentView.findViewById(R.id.btn_publish_story);
        btnPublishStory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validate()) {
                    return;
                }

                if (!createNewStory()) {
                    Toast.makeText(context, "Story could not be published!", Toast.LENGTH_SHORT).show();
                }

                ((MainActivity) getActivity()).pushFragments(FragmentTimeline.getInstance(), true, Consts.TAB_MYSTORIES);
                clearBackStack();
            }
        });

        return contentView;
    }

    private void clearBackStack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private boolean createNewStory() {
        //Story
        Story newStory = new Story();

        newStory.setAuthor(sharedPrefs.getUser());
        newStory.setTitle(storyTitle.getText().toString());
        newStory.setLocation(storyLocation.getText().toString());

        try {
            newStory.setDate(Utils.dateFormat.parse(storyDate.getText().toString()));
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        newStory.setText(storyText.getText().toString());
        int storyId = storyControllerInstance.createRecord(newStory);

        if (storyId <= 0) {
            return false;
        }
        newStory.setId(storyId);

        //Images
        if (selectedImages.isEmpty()) {
            return true; //Ok, images are not required
        }

        for (String imgPath : selectedImages) {
            imageControllerInstance.createRecord(new Image(imgPath, newStory));
        }
        return true;
    }

    private void createSelectPhotosDialog() {
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_select_photos)
                .setItems(R.array.select_photos_options_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        if (id == Consts.FROM_CAMERA) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                    Log.e(TAG, ex.getMessage());
                                }
                                if (photoFile != null) {
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                    startActivityForResult(takePictureIntent, Consts.REQUEST_TAKE_PHOTO);
                                }
                            }
                        } else if (id == Consts.FROM_GALLERY) {
                            Intent intent = new Intent(Consts.ACTION_MULTIPLE_PICK);
                            startActivityForResult(intent, Consts.SELECT_MULTIPLE_IMAGES);
                        } else { /*ignore*/ }
                    }
                });
        selectPhotosDialog = builder.create();
    }

    public static FragmentCreateNewStory getInstance() {
        return new FragmentCreateNewStory();
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Consts.SELECT_MULTIPLE_IMAGES && resultCode == Activity.RESULT_OK) {
            for(String imgPath : data.getStringArrayExtra("all_path")) {
                if(!selectedImages.contains(imgPath)) {
                    selectedImages.add(imgPath);
                }
            }
        } else if (requestCode == Consts.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File photo = new File(currentPhotoPath);
            if (photo.exists()) {
                Uri contentUri = Uri.fromFile(photo);
                mediaScanIntent.setData(contentUri);
                context.sendBroadcast(mediaScanIntent);
            }
            selectedImages.add(currentPhotoPath);
            currentPhotoPath = null;
        }

        if (selectedImages.size() > 0) {
            adapter.clear();
            List<CustomGalleryItem> dataT = new ArrayList<>();
            for (String path : selectedImages) {
                dataT.add(new CustomGalleryItem(path));
            }
            adapter.addAll(dataT);
            btnClearAllPhotos.setVisibility(Button.VISIBLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        if (storyDate != null) {
            storyDate.setText(dayOfMonth + "." + monthOfYear + "." + year);
        }
    }

    // TODO improve this with the loop
    private boolean validate() {

        String title = storyTitle.getText().toString();
        String location = storyLocation.getText().toString();
        String text = storyText.getText().toString();

        if (title.trim().isEmpty()) {
            storyTitle.setError("title is required");
            return false;
        }

        if (location.trim().isEmpty()) {
            storyLocation.setError("location is required");
            return false;
        }

        if (text.trim().isEmpty()) {
            storyText.setError("story is required");
            return false;
        }

        return true;
    }

    public String getName() {
        return Consts.TAB_NEWSTORY;
    }
}
