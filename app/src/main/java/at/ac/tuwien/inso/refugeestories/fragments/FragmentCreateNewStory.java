package at.ac.tuwien.inso.refugeestories.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
    private ImageButton btnAddPhotos;
    private ImageButton btnClearAllPhotos;

    private List<String> selectedImages;

    private ExpandableGridView gridGallery;
    private GalleryAdapter adapter;

    private ImageLoader imageLoader;
    private String currentPhotoPath;

    //TODO prepare everything for for story editing...
    private Story story;

    private SharedPreferencesHandler sharedPrefs;

    private StoryControllerImpl storyControllerInstance;
    private ImageControllerImpl imageControllerInstance;
    private MyDatabaseHelper dbHelper;

    //dialog
    AlertDialog.Builder builder;
    AlertDialog selectPhotosDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_create_new_story, container, false);

        //init db, shared preferences and imageLoader
        dbHelper = new MyDatabaseHelper(context);
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
        btnAddPhotos = (ImageButton) contentView.findViewById(R.id.btn_add_photos);
        btnAddPhotos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createSelectPhotosDialog();
                selectPhotosDialog.show();
            }
        });

        btnClearAllPhotos = (ImageButton) contentView.findViewById(R.id.btn_clear_all_photos);
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
                    story = null;
                    return;
                }

                if (!handleActionPublish()) {
                    Toast.makeText(context, "Story could not be published!", Toast.LENGTH_SHORT).show();
                }

                ((MainActivity) getActivity()).pushFragments(FragmentTimeline.getInstance(), true, Consts.TAB_MYSTORIES);
                clearBackStack();
                story = null;
            }
        });

        if (story != null) {
            setValues();
        }

        return contentView;
    }

    /**
     * This method is used to add the selected images to the story
     *
     * @return false if any of the creation of a record fails, otherwise true
     */
    private boolean addImages() {
        boolean success = true;
        for (String imgPath : selectedImages) {
            if (imageControllerInstance.createRecord(new Image(imgPath, story)) <= 0) {
                Log.e(TAG, "img: " + imgPath + " could not be added to the story");
                success = false;
            }
        }
        return success;
    }

    private void clearBackStack() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    /**
     * This method creates new or updates the selected story if it is used for editing
     *
     * @return boolean value based on success
     */
    private boolean handleActionPublish() {
        boolean isNew = false;
        if (story == null) {
            story = new Story();
            isNew = true;
        }

        story.setAuthor(sharedPrefs.getUser());
        story.setTitle(storyTitle.getText().toString());
        story.setLocation(storyLocation.getText().toString());

        try { // TODO leave original date if updating or not?
            story.setDate(Utils.dateFormat.parse(storyDate.getText().toString()));
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        story.setText(storyText.getText().toString());

        if (isNew) {
            int storyId = storyControllerInstance.createRecord(story);
            if (storyId <= 0) {
                Log.e(TAG, "story was not created");
                return false;
            }
            story.setId(storyId);
        } else {
            if (!storyControllerInstance.updateRecord(story)) {
                Log.e(TAG, "story was not updated");
                return false;
            }
        }

        //Images
        if (selectedImages.isEmpty()) {
            if(!isNew) {
                imageControllerInstance.deleteAllRecords(story);
            }
            return true; //Ok, images are not required / not changed
        }

        if (isNew) {
            addImages();
        } else {
            imageControllerInstance.deleteAllRecords(story);
            addImages();
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
                                    photoFile = Utils.createImageFile();
                                } catch (IOException ex) {
                                    Log.e(TAG, ex.getMessage());
                                }
                                if (photoFile != null) {
                                    currentPhotoPath = photoFile.getAbsolutePath();
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
            for (String imgPath : data.getStringArrayExtra("all_path")) {
                if (!selectedImages.contains(imgPath)) {
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

        updateGallery();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = activity;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        if (storyDate != null) {
            String dayOfMonthString;
            String monthOfYearString;
            monthOfYear++;

            if(dayOfMonth < 10) {
                dayOfMonthString = "0" + dayOfMonth;
            } else {
                dayOfMonthString = Integer.toString(dayOfMonth);
            }
            if(monthOfYear < 10) {
                monthOfYearString = "0" + monthOfYear;
            } else {
                monthOfYearString = Integer.toString(monthOfYear);
            }
            storyDate.setText(dayOfMonthString + "." + monthOfYearString + "." + year);
        }
    }

    public void setStory(Story story) {
        this.story = story;
    }

    private void setValues() {
        storyTitle.setText(story.getTitle());
        storyLocation.setText(story.getLocation());
        storyDate.setText(Utils.dateFormat.format(story.getDate()));
        storyText.setText(story.getText());
        if (story.getImages() != null && story.getImages().size() > 0) {
            for (Image img : story.getImages()) {
                selectedImages.add(img.getImg());
            }
            updateGallery();
        }
    }

    private void updateGallery() {
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

    private boolean validate() {

        String title = storyTitle.getText().toString();
        String location = storyLocation.getText().toString();
        String text = storyText.getText().toString();

        if (title.trim().isEmpty()) {
            storyTitle.requestFocus();
            storyTitle.setError("title is required");
            return false;
        }

        if (location.trim().isEmpty()) {
            storyLocation.requestFocus();
            storyLocation.setError("location is required");
            return false;
        }

        if (text.trim().isEmpty()) {
            storyText.requestFocus();
            storyText.setError("story is required");
            return false;
        }

        return true;
    }

}
