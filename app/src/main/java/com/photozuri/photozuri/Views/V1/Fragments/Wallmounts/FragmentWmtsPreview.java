package com.photozuri.photozuri.Views.V1.Fragments.Wallmounts;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.photozuri.photozuri.Adapter.MyImageAdapter;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Views.V1.Preview.CustomViewPager;
import com.photozuri.photozuri.Views.V1.Preview.ShadowTransformer;
import com.photozuri.photozuri.Views.V1.Preview.WmtsCardFragmentPageAdapter;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Eric on 1/26/2018.
 */

public class FragmentWmtsPreview extends Fragment implements BlockingStep {
    ArrayList<MyImage> myImages;

    private MyImageAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private DbContentValues dbContentValues;
    private AppUtils appUtils;

    private DbOperations dbOperations;
    private CustomViewPager viewPager;
    private View view;

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    private ArrayList<MyImage> getMyImages() {
        Cursor cursor = dbOperations.select(DbConstants.TABLE_SAVED_DATA, DbConstants.TITLE_ID, GlobalConsts.TITLE_ID);

        if (cursor != null && cursor.getCount() > 0) {

            myImages = appUtils.updateList(myImages, dbContentValues.getSavedData(cursor));

            Collections.sort(myImages);
        }
        return myImages;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dbOperations = new DbOperations(getContext());
        dbContentValues = new DbContentValues();
        appUtils = new AppUtils(getContext());

        return inflater.inflate(R.layout.fragment_four, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);


        //initUI(view);


    }

    void initUI(View view) {
        myImages = new ArrayList<>();
        getMyImages();
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setVisibility(View.VISIBLE);
        WmtsCardFragmentPageAdapter pagerAdapter = new WmtsCardFragmentPageAdapter(getActivity().getSupportFragmentManager(),
                dpToPixels(2, getContext()), myImages, myImages.size());
        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);

        fragmentCardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(pagerAdapter);
        try {
            viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
        } catch (Exception nm) {
            nm.printStackTrace();
            Log.d("nm", nm.toString());
        }
        viewPager.setOffscreenPageLimit(1);
        viewPager.setPagingEnabled(true);

        int lastPosition = Integer.valueOf(20);
        //viewPager.setCurrentItem(lastPosition);
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.getStepperLayout().showProgress("Operation in progress, please wait...");

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
        return null;
    }

    @Override
    public void onSelected() {

        initUI(view);
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
