package com.photozuri.photozuri.Views.V1.Fragments.Photobook;

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

import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.PhotoFullPopupWindow;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.Utills.UtilListeners.PhotoPopUpListner;
import com.photozuri.photozuri.Views.V1.Draggable.RecyclerListAdapter;
import com.photozuri.photozuri.Views.V1.Draggable.helper.OnStartDragListener;
import com.photozuri.photozuri.Views.V1.Draggable.helper.SimpleItemTouchHelperCallback;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Eric on 1/17/2018.
 */

public class FragmentTwo extends Fragment implements BlockingStep, OnStartDragListener {
    //    public RecyclerGridFragment() {
//    }
    ArrayList<MyImage> myImages;
    private RecyclerListAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private DbContentValues dbContentValues;
    private AppUtils appUtils;
    private View view;
    private DbOperations dbOperations;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbOperations = new DbOperations(getContext());
        dbContentValues = new DbContentValues();
        appUtils = new AppUtils(getContext());
        if (GlobalConsts.myImages != null) {
            GlobalConsts.myImages.clear();
        }
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myImages = new ArrayList<>();
        this.view = view;
        // DbContentValues dbContentValues
        initUI(view);


    }

    private void updateImages(ArrayList<MyImage> myImages) {
        for (int a = 0; a < myImages.size(); a++) {

            ContentValues cv = new ContentValues();
            cv.put(DbConstants.IMAGE_POSITION, a + 1);
            dbOperations.update(DbConstants.TABLE_SAVED_DATA, DbConstants.KEY_ID, myImages.get(a).getKEY_ID(), cv);
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
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
        adapter = new RecyclerListAdapter(getContext(), this, myImages, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {

                Log.d("POPOP", "CLICKED");
                MyImage myImage = myImages.get(position);
                PhotoFullPopupWindow p = new PhotoFullPopupWindow(getContext(),
                        R.layout.popup_photo_full,
                        recyclerView, myImage.getPath(),
                        null, myImage.getCaption(),
                        myImages.get(position).getKEY_ID());
                Log.d("POPOP", "START");
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
                        //initUI(view);
                        //adapter.setData(myImages);
                        //adapter.notifyDataSetChanged();



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

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.getStepperLayout().showProgress("Operation in progress, please wait...");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (GlobalConsts.myImages != null && GlobalConsts.myImages.size() > 0) {
                    updateImages(GlobalConsts.myImages);
                }
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (GlobalConsts.myImages != null && GlobalConsts.myImages.size() > 0) {
                    updateImages(GlobalConsts.myImages);
                }
                //you can do anythings you want
                callback.goToPrevStep();
            }
        }, 0L);// delay open another fragment,
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
