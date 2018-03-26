package com.photozuri.photozuri.Views.V1.Fragments.SinglePrints;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.photozuri.photozuri.Adapter.MyImageAdapterSingle;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.PhotoFullPopupWindow3;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.Utills.UtilListeners.PhotoPopUpListner;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Eric on 1/25/2018.
 */

public class FragmentSpsTwo extends Fragment implements BlockingStep {
    ArrayList<MyImage> myImages;
    private View view;
    private MyImageAdapterSingle adapter;
    private ItemTouchHelper mItemTouchHelper;
    private DbContentValues dbContentValues;
    private AppUtils appUtils;

    private DbOperations dbOperations;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbOperations = new DbOperations(getContext());
        dbContentValues = new DbContentValues();
        appUtils = new AppUtils(getContext());


        if (GlobalConsts.myImages != null) {
            GlobalConsts.myImages.clear();
        }
        return inflater.inflate(R.layout.fragment_pps2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        myImages = new ArrayList<>();
        // getMyImages();
        initUI(view);
    }


    private void updateImages(ArrayList<MyImage> myImages) {
        for (int a = 0; a < myImages.size(); a++) {

            ContentValues cv = new ContentValues();
            cv.put(DbConstants.IMAGE_POSITION, a + 1);
            dbOperations.update(DbConstants.TABLE_SAVED_DATA, DbConstants.KEY_ID, myImages.get(a).getKEY_ID(), cv);
        }
    }


    private ArrayList<MyImage> getMyImages() {
        Cursor cursor = dbOperations.select(DbConstants.TABLE_SAVED_DATA, DbConstants.TITLE_ID, GlobalConsts.TITLE_ID);

        if (cursor != null && cursor.getCount() > 0) {

            myImages = appUtils.updateList(myImages, dbContentValues.getSavedData(cursor));
            Collections.sort(myImages);
        }
        //myImages=myImages;
        return myImages;
    }

    void initUI(View view) {

        myImages.clear();

        getMyImages();


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new MyImageAdapterSingle(getContext(), myImages, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {

                Log.d("pps", "onClicked started");
                MyImage myImage = myImages.get(position);
                PhotoFullPopupWindow3 p = new PhotoFullPopupWindow3(getContext(),
                        R.layout.popup_photo_full,
                        recyclerView, myImage.getPath(),
                        null, myImage.getCaption(),
                        myImages.get(position).getKEY_ID());
                Log.d("pps", "Start ");
                p.start(new PhotoPopUpListner() {
                    @Override
                    public void onDismiss() {

                        p.dismiss();
                    }

                    @Override
                    public void onDismissave(String caption) {


                        p.dismiss();


                        myImage.setCaption(caption);
                        adapter.setDataChange(position, myImage);
                        myImages.get(position).setCaption(caption);


                    }
                });
            }


            @Override
            public void onLongClickListener(int position) {

                if (dbOperations.delete(DbConstants.TABLE_SAVED_DATA, DbConstants.KEY_ID, myImages.get(position).getKEY_ID())) {
                    adapter.setData(getMyImages());
                    initUI(view);
                }
            }

            @Override
            public void onClickListener(int adapterPosition, ImageView imageView) {

            }
        });

        // RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        final int spanCount = 2;
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
//
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        callback.getStepperLayout().showProgress("Operation in progress, please wait...");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//
//                if (GlobalConsts.myImages != null && GlobalConsts.myImages.size() > 0) {
//                    updateImages(GlobalConsts.myImages);
//                }

                // dbOperations.delete(DbConstants.TABLE_SAVED_DATA,DbConstants.TITLE_ID,GlobalConsts.TITLE_ID);
                // for(MyImage)

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

        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        if (GlobalConsts.myImages != null) {
            GlobalConsts.myImages.clear();
        }
        initUI(view);
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
