package com.photozuri.photozuri.Views.V1.Stories;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.photozuri.photozuri.Adapter.StoryPhotoDetailAdapter;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.Models.PhotoList;
import com.photozuri.photozuri.Data.Models.StoryPhoto;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.DateTimeUtils;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.imagepicker.model.Config;
import com.photozuri.photozuri.imagepicker.ui.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Eric on 2/21/2018.
 */

public class FragmentJourneyDetails extends Fragment {
    private static ArrayList<MyImage> images = new ArrayList<>();
    private final int CAMERA_REQUEST = 1888;
    private String album_key;
    private String title;
    private AppUtils appUtils;
    private Bitmap bitmap;

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private StoryPhotoDetailAdapter storyPhotoDetailAdapter;
    //  private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private Fragment fragment = null;
    private ArrayList<StoryPhoto> storyPhotos = new ArrayList<>();
    private View view;
    private FloatingActionButton fab, fab_back;
    private RelativeLayout relativeLayoutempty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_journey_details, container, false);
        return view;
    }

    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        relativeLayoutempty = view.findViewById(R.id.relative_empty);
        relativeLayoutempty.setVisibility(View.GONE);
        fab_back = view.findViewById(R.id.fab_back);
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new FragmentJourneys();
                popOutFragments();
                setUpView();
            }
        });
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });

        try {
            title = getArguments().getString("title");
            album_key = getArguments().getString("album");

            getActivity().setTitle(title);

        } catch (Exception nm) {
            nm.printStackTrace();
        }
        if (album_key != null) {

            initviews(getData(album_key));
        }


    }

    private ArrayList<StoryPhoto> getData(String album_key) {
        ArrayList<StoryPhoto> storyPhotos = new ArrayList<>();

        DbOperations dbOperations = new DbOperations(getContext());
        DbContentValues dbContentValues = new DbContentValues();
        Cursor cursor = dbOperations.select(DbConstants.TABLE_SP, DbConstants.SPAlbum_ID, album_key);
        if (cursor != null) {
            storyPhotos = dbContentValues.getSavedPhotos(cursor);
        }
        return storyPhotos;
    }

    private void initviews(ArrayList<StoryPhoto> storyPhotos) {


        Log.d("noOfSaved", "" + storyPhotos.size());
        //  getPhotoList()
        storyPhotoDetailAdapter = new StoryPhotoDetailAdapter(getContext(), getPhotoList(storyPhotos), new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {


            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onClickListener(int adapterPosition, ImageView imageView) {

            }
        });


        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        storyPhotoDetailAdapter.notifyDataSetChanged();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(storyPhotoDetailAdapter);
        if (storyPhotoDetailAdapter.getItemCount() < 0) {
            relativeLayoutempty.setVisibility(View.VISIBLE);
        }



    }

    private ArrayList<PhotoList> getPhotoList(ArrayList<StoryPhoto> storyPhotos) {
        HashSet<String> d = new HashSet<>();

        ArrayList<PhotoList> photoLists = new ArrayList<>();
        for (StoryPhoto photo : storyPhotos) {
            d.add(photo.getDate());
        }
        for (String date : d) {

            ArrayList<StoryPhoto> storyPhotos1 = new ArrayList<>();
            for (StoryPhoto storyPhoto : storyPhotos) {
                if (storyPhoto.getDate().equals(date)) {
                    storyPhotos1.add(storyPhoto);
                }
            }
            PhotoList photoList = new PhotoList();
            photoList.setDate(date);
            photoList.setStoryPhotos(storyPhotos1);

            photoLists.add(photoList);


        }


        return photoLists;
    }

    private void startDialog() {


        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_picture, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setTitle("Select Source");

        ImageView camera = mView.findViewById(R.id.img_camera);
        ImageView gallery = mView.findViewById(R.id.img_gallery);


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Done", (dialogBox, id) -> {
                    // ToDo get user input here


                })

                .setNegativeButton("Dismiss",
                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialogAndroid));
        camera.setOnClickListener(new CustomListener(alertDialogAndroid));
        gallery.setOnClickListener(new CustomListener(alertDialogAndroid));
    }

    public void onActivityResult(int resquestCode, int resultCode, Intent data) {
        ArrayList<StoryPhoto> storyPhotos = new ArrayList<>();
        if (resquestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap sb = Bitmap.createScaledBitmap(photo, 200, 200, false);
            bitmap = sb;

            GeneralUtills generalUtills = new GeneralUtills(getContext());

            String path = generalUtills.saveProfilePic(bitmap, title + "" + DateTimeUtils.getNow());
            StoryPhoto storyPhoto = new StoryPhoto();
            storyPhoto.setPosition("0");
            storyPhoto.setDate(DateTimeUtils.getToday());
            storyPhoto.setDescription("No Desc");
            storyPhoto.setTitle("No Title");
            storyPhoto.setPath(path);
            storyPhoto.setAlbum_ID(album_key);
            storyPhotos.add(storyPhoto);

            new DbContentValues.savephoto(storyPhotos, getContext(), new DbContentValues.MyInterface() {
                @Override
                public void onComplete(boolean result) {
                    initviews(getData(album_key));
                    Log.d("savedPhoto", album_key);
                }
            }).execute();


        } else if (resquestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            // images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            //adapter.setData(images);

            appUtils = new AppUtils(getContext());
            //adapter.setData(images);
            ArrayList<MyImage> images = appUtils.convertImages(data.getParcelableArrayListExtra(Config.EXTRA_IMAGES), 0);
            if (images != null && images.size() > 0) {
                for (int a = 0; a < images.size(); a++) {

                    StoryPhoto storyPhoto = new StoryPhoto();
                    storyPhoto.setPosition("0");
                    storyPhoto.setDate(DateTimeUtils.getToday());
                    storyPhoto.setDescription("No Desc");
                    storyPhoto.setTitle("No Title");
                    storyPhoto.setPath(images.get(a).getPath());
                    storyPhoto.setAlbum_ID(album_key);


                    storyPhotos.add(storyPhoto);
                }
                new DbContentValues.savephoto(storyPhotos, getContext(), result -> {
                    initviews(getData(album_key));
                    Log.d("savedPhoto", album_key);
                }).execute();
                // update(images);
            }
            // update(data.getParcelableArrayListExtra(Config.EXTRA_IMAGES));
        }

    }

    private void pick() {
        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context

                .setToolbarColor("#00BCD4")         //  Toolbar color
                .setStatusBarColor("#0097A7")       //  StatusBar color (works with SDK >= 21  )
                //.setImageTitle()
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                .setBackgroundColor("#FFFFFF")      //  Background color
                .setCameraOnly(false)               //  Camera mode
                .setMultipleMode(true)              //  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  MyImage title (works with FolderMode = false)
                .setDoneTitle("Done")               //  Done button title
                .setLimitMessage("You have reached selection limit")    // Selection limit message
                // .setMaxSize(20)                     //  Max images can be selected
                .setSavePath("ImagePicker")         //  MyImage capture folder name
                //.setSelectedImages(images)          //  Selected images
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start();                           //  Start ImagePicker
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;


        public CustomListener(Dialog dialog) {
            this.dialog = dialog;

        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.img_camera) {
                dialog.dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {

                dialog.dismiss();
                pick();
            }
        }


    }
}
