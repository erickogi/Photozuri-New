package com.photozuri.photozuri.Views.V1.Fragments.Photobook;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.photozuri.photozuri.Adapter.ImageAdapter;
import com.photozuri.photozuri.Adapter.PhotoAdapter;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.NetworkUtills.NetworkUtils;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.MyToast;
import com.photozuri.photozuri.Views.V1.FacebookPhotos;
import com.photozuri.photozuri.Views.V1.GalleryPhotos;
import com.photozuri.photozuri.Views.V1.GooglePhotos;
import com.photozuri.photozuri.Views.V1.InstagramPhotos;
import com.photozuri.photozuri.Views.V1.Usables.Constants;
import com.photozuri.photozuri.imagepicker.model.Config;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Eric on 1/17/2018.
 */

public class FragmentOne extends Fragment implements BlockingStep {
    public static final String EXTRA_CONFIG = "Config";
    DbOperations dbOperations = new DbOperations(getContext());
    private View view;
    private ArrayList<MyImage> images = new ArrayList<>();
    private GridView gridView;
    private PhotoAdapter imageAdapter;
    private NetworkUtils networkUtils;
    private GeneralUtills generalUtills;

    private RecyclerView recyclerView;
    private Config config;
    private ImageAdapter adapter;

    private Button choose;
    private FloatingActionButton floatingActionButton;
    private AppUtils appUtils;
    private LinearLayout linearLayoutEmpty;


    private void InitUtils() {
        appUtils = new AppUtils(getContext());
        // networkUtils= new NetworkUtils(FacebookPhotos.this);
        // generalUtills=new GeneralUtills(FacebookPhotos.this);
    }
    private void viewsPlay(boolean gridhasImages) {
        if (gridhasImages) {
            gridView.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.VISIBLE);
            choose.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.GONE);
        } else {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.VISIBLE);
            choose.setVisibility(View.GONE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  InitUtils();

        dbOperations = new DbOperations(getContext());
        InitUtils();
        images = new ArrayList<>();
        DbContentValues dbContentValues = new DbContentValues();
        if (dbOperations.getCount(DbConstants.TABLE_SAVED_DATA) > 0) {
            images = dbContentValues.getSavedData(dbOperations.select(DbConstants.TABLE_SAVED_DATA));
            Collections.sort(images);
        }


        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        linearLayoutEmpty = view.findViewById(R.id.layout_empty);
        initView(view);
    }

    private void initView(View view) {


        getMyImages();
        choose = view.findViewById(R.id.btn_pick);
        gridView = view.findViewById(R.id.gridview);
        floatingActionButton = view.findViewById(R.id.fab_add);


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDialog();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });


        imageAdapter = new PhotoAdapter(getContext(), images);
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


                alertDialogDelete("Continue to delete this item ? ", images.get(position).getKEY_ID());


                return false;
            }
        });
    }

    private void alertDialogDelete(final String message, int id) {
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

                        initView(view);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();

                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(message).setPositiveButton("Okay", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();

    }

    private ArrayList<MyImage> getMyImages() {

        images = new ArrayList<>();
        Log.d("titleID", "" + GlobalConsts.TITLE_ID);

        Cursor cursor = dbOperations.select(DbConstants.TABLE_SAVED_DATA,
                DbConstants.TITLE_ID, GlobalConsts.TITLE_ID);

        DbContentValues dbContentValues = new DbContentValues();
        if (cursor != null && cursor.getCount() > 0) {

            ArrayList<MyImage> imagges = appUtils.updateList(images, dbContentValues.getSavedData(cursor));

            for (MyImage image : imagges) {
                if (image.getTitle_id() == GlobalConsts.TITLE_ID) {
                    images.add(image);
                    Log.d("titleID", "For image " + image.getTitle_id());
                }
            }
            //images.get(0).g
        }
        Collections.sort(images);
        return images;
    }
    private void startDialog() {


        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.popup, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        // alertDialogBuilderUserInput.setTitle("Choose Print type");

        LinearLayout mLinearFb, mLinearGoogle, mLinearGallery, mLinearInstagram;

        mLinearFb = mView.findViewById(R.id.option_fb);
        mLinearGoogle = mView.findViewById(R.id.option_googlephotos);
        mLinearGallery = mView.findViewById(R.id.option_gallery);
        mLinearInstagram = mView.findViewById(R.id.option_instagram);


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here


                    }
                })

                .setNegativeButton("Dismiss",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        })
        ;

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();


        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setVisibility(View.GONE);
        mLinearGallery.setOnClickListener(new CustomListener(alertDialogAndroid, theButton));
        mLinearGoogle.setOnClickListener(new CustomListener(alertDialogAndroid, theButton));
        mLinearInstagram.setOnClickListener(new CustomListener(alertDialogAndroid, theButton));
        mLinearFb.setOnClickListener(new CustomListener(alertDialogAndroid, theButton));


        theButton.setOnClickListener(new CustomListener(alertDialogAndroid, theButton));
    }

    void update(ArrayList<MyImage> imagess) {


        if (images != null && images.size() > 0) {

            ArrayList<MyImage> newImages = appUtils.getNewUniqueImages(images, imagess);


            insert(newImages);


        } else {

            insert(imagess);

        }

    }

    private void insert(ArrayList<MyImage> imagess) {


        new DbContentValues.loadImg(imagess,
                getContext(), DbConstants.selected, GlobalConsts.TITLE_ID, new DbContentValues.MyInterface() {
            @Override
            public void onComplete(boolean result) {
                if (result) {

                    ContentValues cv = new ContentValues();
                    cv.put(DbConstants.IMAGE_NO, imagess.size());
                    if (dbOperations.update(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, GlobalConsts.TITLE_ID, cv)) {
                        Log.d("titlenoupdate", "succesful");
                    } else {
                        Log.d("titlenoupdate", "succesful");
                    }
                    FragmentOne.this.getMyImages();
                    FragmentOne.this.initView(view);

                } else {

                    //Toast.makeText(getContext(), "Could not save", Toast.LENGTH_LONG).show();
                }
            }
        }).execute();

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.getStepperLayout().showProgress("Operation in progress, please wait...");
        if (GlobalConsts.TYPE_SELECTED == com.photozuri.photozuri.Utills.Constants.PHOTO_BOOK_INTENT) {
            if (dbOperations.getCount(DbConstants.TABLE_SAVED_DATA) > 4) {
                goNext(callback);
            }
        } else {
            goNext(callback);
        }
    }


    private void goNext(StepperLayout.OnNextClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.goToNextStep();
                callback.getStepperLayout().hideProgress();

            }
        }, 500L);// delay open another fragment,
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //you can do anythings you want
                callback.goToPrevStep();
            }
        }, 0L);// delay open another fragment,
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {

        ArrayList<MyImage> myImages = getMyImages();
        if (GlobalConsts.TYPE_SELECTED != com.photozuri.photozuri.Utills.Constants.WALL_MOUNT_INTENT && GlobalConsts.TYPE_SELECTED != com.photozuri.photozuri.Utills.Constants.PASSPORT_INTENT) {
            if (myImages.size() > GlobalConsts.MAX_PHOTOS + 1) {
                MyToast.toast("Delete Some Photo's By Long Pressing on ", getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
                return new VerificationError("Must be less than 40" + (GlobalConsts.MAX_PHOTOS + 1));

            } else if (myImages.size() < GlobalConsts.MIN_PHOTOS) {
                MyToast.toast("Add More Photo's", getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
                return new VerificationError("Must be more than " + (GlobalConsts.MIN_PHOTOS - 1));

            } else {
                return null;
            }
        } else if (GlobalConsts.TYPE_SELECTED == com.photozuri.photozuri.Utills.Constants.PASSPORT_INTENT) {
            if (myImages.size() > GlobalConsts.PS_MAX_PHOTOS) {
                MyToast.toast("Delete Some Photo's By Long Pressing on ", getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
                return new VerificationError("Must be less than " + (GlobalConsts.PS_MAX_PHOTOS));

            } else if (myImages.size() < GlobalConsts.PS_MIN_PHOTOS) {
                MyToast.toast("Add More Photo's", getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
                return new VerificationError("Must be more than " + (GlobalConsts.PS_MIN_PHOTOS));

            } else {
                return null;
            }

        } else if (GlobalConsts.TYPE_SELECTED == com.photozuri.photozuri.Utills.Constants.WALL_MOUNT_INTENT) {
            if (myImages.size() > GlobalConsts.WP_MAX_PHOTOS) {
                MyToast.toast("Delete Some Photo's By Long Pressing on ", getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
                return new VerificationError("Must be less than " + (GlobalConsts.WP_MAX_PHOTOS));

            } else if (myImages.size() < GlobalConsts.WP_MIN_PHOTOS) {
                MyToast.toast("Add More Photo's", getContext(), R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
                return new VerificationError("Must be more than " + (GlobalConsts.WP_MIN_PHOTOS));

            } else {
                return null;
            }
        }






        if (GlobalConsts.TYPE_SELECTED == com.photozuri.photozuri.Utills.Constants.WALL_MOUNT_INTENT || GlobalConsts.TYPE_SELECTED == com.photozuri.photozuri.Utills.Constants.PASSPORT_INTENT) {
            if (images != null && images.size() > 4) {
                return null;
            } else {
                return new VerificationError("Photos either exceeds maximum count Or are less than Min");

            }
        }
        // if(GlobalConsts.WALLMOUNTSELECTED||GlobalConsts.I)
        return images != null && images.size() > 19 && images.size() < 40 ? null : new VerificationError("Photos either exceeds maximum count  (" + (GlobalConsts.MAX_PHOTOS + 1) + ")  Or are less than Min(" + GlobalConsts.MIN_PHOTOS + ")");
    }

    @Override
    public void onSelected() {

        initView(view);
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);

        // Log.d("returnedd",intent.getDataString()+"j");
        switch (requestCode) {
            case Constants.GRID_REQUEST:
                if (responseCode == RESULT_OK && intent != null) {

                    ArrayList<MyImage> images = (ArrayList<MyImage>) intent.getSerializableExtra("images");
                    Log.d("returnedd", intent.getDataString() + "j");

                    if (images != null && images.size() > 0) {
                        update(images);
                    } else {
                        Log.d("returnedd", "null images");

                    }
                } else {
                    Log.d("returnedd", "null j");
                }
                break;

            default:

                Log.d("looo", intent.getDataString());
                //callbackManager.onActivityResult(requestCode, responseCode, intent);
        }


    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        private Button button;

        public CustomListener(Dialog dialog, Button theButton) {
            this.dialog = dialog;
            this.button = theButton;


        }

        @Override
        public void onClick(View v) {


            Intent intent = null;
            int from = 0;
            if (v.getId() == R.id.option_fb) {
                from = com.photozuri.photozuri.Utills.Constants.FACEBOOK;
                intent = new Intent(getActivity(), FacebookPhotos.class);


            } else if (v.getId() == R.id.option_gallery) {

                from = com.photozuri.photozuri.Utills.Constants.GALLERY;
                intent = new Intent(getActivity(), GalleryPhotos.class);

            } else if (v.getId() == R.id.option_googlephotos) {

                from = com.photozuri.photozuri.Utills.Constants.GOOGLE;
                intent = new Intent(getActivity(), GooglePhotos.class);


            } else if (v.getId() == R.id.option_instagram) {

                from = com.photozuri.photozuri.Utills.Constants.INSTAGRAM;
                intent = new Intent(getActivity(), InstagramPhotos.class);


            }


            if (intent != null) {
                dialog.dismiss();

                ArrayList<MyImage> imagess = new ArrayList<>();
                for (MyImage image : images) {
                    if (image.getImageFrom() == from) {
                        imagess.add(image);
                    }
                }


                intent.putExtra("images", imagess);
                if (images != null && images.size() > 0) {
                    intent.putExtra("count", images.size());
                    intent.putExtra("count_o", (images.size() - imagess.size()));
                } else {
                    intent.putExtra("count", 0);
                    intent.putExtra("count_o", 0);
                }
                // intent.putExtra("images", convertImages(images, 0));
                // intent.putParcelableArrayListExtra("images",images);
                startActivityForResult(intent, Constants.GRID_REQUEST);

            }

        }


    }

}
