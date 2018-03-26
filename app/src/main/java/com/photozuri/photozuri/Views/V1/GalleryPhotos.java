package com.photozuri.photozuri.Views.V1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.photozuri.photozuri.Adapter.ImageAdapter;
import com.photozuri.photozuri.Adapter.PhotoAdapter;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.Models.TitleModel;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.MyToast;
import com.photozuri.photozuri.imagepicker.model.Config;
import com.photozuri.photozuri.imagepicker.ui.imagepicker.ImagePicker;

import java.util.ArrayList;

public class GalleryPhotos extends AppCompatActivity {
    public static final String EXTRA_CONFIG = "Config";
    private static ArrayList<MyImage> images = new ArrayList<>();
    DbOperations dbOperations;
    DbContentValues dbContentValues;
    private AppUtils appUtils;
    private GridView gridView;
    private RecyclerView recyclerView;
    private Config config;
    private ImageAdapter adapter;
    private PhotoAdapter imageAdapter;
    private Button choose;
    private FloatingActionButton floatingActionButton;
    private int total_count = 0;
    private int gallery_count = 0;
    private int count_o = 0;
    private LinearLayout linearLayoutEmpty;
    private void InitUtils() {
        appUtils = new AppUtils(GalleryPhotos.this);
        //networkUtils= new NetworkUtils(GalleryPhotos.this);
        //generalUtills=new GeneralUtills(GalleryPhotos.this);
    }
    private void viewsPlay(boolean gridhasImages) {
        if (gridhasImages) {
            gridView.setVisibility(View.VISIBLE);
            linearLayoutEmpty.setVisibility(View.GONE);
            //  floatingActionButton.setVisibility(View.VISIBLE);

        } else {
            gridView.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            pick();
//            floatingActionButton.setVisibility(View.GONE);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_photos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbOperations = new DbOperations(GalleryPhotos.this);
        dbContentValues = new DbContentValues();
        linearLayoutEmpty = findViewById(R.id.layout_empty);

        InitUtils();
        Intent intent = getIntent();

        count_o = intent.getIntExtra("count_o", 0);
        total_count = intent.getIntExtra("count", 0);

        ArrayList<MyImage> images = //convertImages(
                (ArrayList<MyImage>) intent.getSerializableExtra("images");
        if (images != null) {
            GalleryPhotos.images.clear();
            GalleryPhotos.images = images;

            if (images.size() > 0) {
                gallery_count = images.size();
            }
        }

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (total_count < GlobalConsts.MAX_PHOTOS) {
                    pick();
                } else {
                    MyToast.toast("You have reached maximum allowed photos", GalleryPhotos.this, R.drawable.ic_done_color_24dp, Constants.TOAST_LONG);
                }
            }
        });


        initViews(images);
    }

    private TitleModel getTitle(int id) {
        TitleModel titleModel = new TitleModel();

        Cursor cursor = dbOperations.select(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, id);
        if (cursor != null && cursor.getCount() > 0) {
            ArrayList<TitleModel> titleModels = dbContentValues.getSavedDataTitles(cursor);
            if (titleModels != null && titleModels.size() > 0) {
                titleModel = titleModels.get(0);
                return titleModel;
            } else {
                return null;
            }
        } else {
            return null;
        }


    }

    private void alertDialogDelete(final String message, int id, int position) {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //popOutFragments();
                        // int id = timeLineModels.get(position).getId();

                        ///TitleModel titleModel = getTitle(id);
                        /// Boolean titledel = dbOperations.delete(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, titleModel.getTitle_id());

                        Boolean photodel = dbOperations.delete(DbConstants.TABLE_SAVED_DATA, DbConstants.KEY_ID, id);

                        dialog.dismiss();
                        images.remove(position);
                        initViews(images);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();

                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(GalleryPhotos.this);

        builder.setMessage(message).setPositiveButton("Okay", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();

    }

    void initViews(ArrayList<MyImage> myImages) {
        gridView = findViewById(R.id.gridview);

        total_count = count_o + myImages.size();
        imageAdapter = new PhotoAdapter(GalleryPhotos.this, myImages);
        gridView.setAdapter(imageAdapter);

        viewsPlay(imageAdapter.getCount() > 0);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                alertDialogDelete("Continue to delete this item ? ", images.get(position).getKEY_ID(), position);
                //images.get(position).getKEY_ID();
                return false;
            }
        });
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
                .setMaxSize(GlobalConsts.MAX_PHOTOS - total_count)                     //  Max images can be selected
                .setSavePath("ImagePicker")         //  MyImage capture folder name
                //.setSelectedImages(images)          //  Selected images
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start();                           //  Start ImagePicker
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            // images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            //adapter.setData(images);

            //adapter.setData(images);
            ArrayList<MyImage> images = appUtils.convertImages(data.getParcelableArrayListExtra(Config.EXTRA_IMAGES), 0);
            if (images != null && images.size() > 0) {
                for (int a = 0; a < images.size(); a++) {


                    images.get(a).setImageFrom(Constants.GALLERY);
                }
                update(images);
            }
            // update(data.getParcelableArrayListExtra(Config.EXTRA_IMAGES));
        }
    }

    void update(ArrayList<MyImage> imagess) {


        if (images != null && images.size() > 0) {


            // imagess.
            ArrayList<MyImage> imageds = appUtils.updateList(images, imagess);
            images.clear();
            images.addAll(imageds);


            initViews(imageds);

            //imageAdapter.setData(images);
            //viewsPlay(imageAdapter.getCount() > 0);

        } else {
            images = imagess;
            initViews(images);
            //imageAdapter.setData(images);
            //viewsPlay(imageAdapter.getCount() > 0);
        }
    }


    @Override
    public void onBackPressed() {

        if (images == null) {
            Log.d("returnedd", "from null images");
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("images", images);
        //returnIntent.putExtra("images", this.images);
        setResult(RESULT_OK, returnIntent);
        //super.onBackPressed();
        finish();

    }

    public void back(View view) {
        if (images == null) {
            Log.d("returnedd", "from null images");
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("images", images);
        //returnIntent.putExtra("images", this.images);
        setResult(RESULT_OK, returnIntent);
        //super.onBackPressed();
        finish();
    }
}
