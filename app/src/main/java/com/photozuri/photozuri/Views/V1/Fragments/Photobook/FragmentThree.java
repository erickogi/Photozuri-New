package com.photozuri.photozuri.Views.V1.Fragments.Photobook;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.photozuri.photozuri.Adapter.PreviewAdapter;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.Views.V1.Preview.CustomViewPager;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Collections;

//import com.photozuri.photozuri.Views.V1.Flipper.PageFlipView;

/**
 * Created by Eric on 1/17/2018.
 */

public class FragmentThree extends Fragment implements BlockingStep, GestureDetector.OnGestureListener {
    DbOperations dbOperations;


    // PageFlipView mPageFlipView;
    GestureDetector mGestureDetector;
    private ArrayList<MyImage> myImages;
    private AppUtils appUtils;
    private DbContentValues dbContentValues;
    private CustomViewPager viewPager;
    private View view;
    private PreviewAdapter adapter;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private RecyclerView recyclerView;

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




//        int pages = myImages.size() / 9;
//        if (pages > 1) {
//
//            mPageFlipView = new PageFlipView(getContext());
//            //setContentView(mPageFlipView);
//            mGestureDetector = new GestureDetector(getContext(), this);
//        }


        // return mPageFlipView;
        return inflater.inflate(R.layout.fragment_four, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        //initUI(view);
        recyclerView = view.findViewById(R.id.recyclerView);


    }

    void initUI(View view) {
        myImages = new ArrayList<>();
        getMyImages();

        adapter = new PreviewAdapter(getContext(), myImages, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {

            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onClickListener(int adapterPosition, ImageView imageView) {

            }
        }, 0);
        adapter.notifyDataSetChanged();


        mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

//        myImages = new ArrayList<>();
//        getMyImages();
//        viewPager = view.findViewById(R.id.viewPager);
//        CardFragmentPagerAdapter pagerAdapter = new CardFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
//                dpToPixels(2, getContext()), myImages, myImages.size());
//        ShadowTransformer fragmentCardShadowTransformer = new ShadowTransformer(viewPager, pagerAdapter);
//        fragmentCardShadowTransformer.enableScaling(true);
//
//        viewPager.setAdapter(pagerAdapter);
//        try {
//            viewPager.setPageTransformer(false, fragmentCardShadowTransformer);
//        } catch (Exception nm) {
//            nm.printStackTrace();
//            Log.d("nm", nm.toString());
//        }
//        viewPager.setOffscreenPageLimit(1);
//        viewPager.setPagingEnabled(true);
//
//        int lastPosition = Integer.valueOf(20);
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

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            mPageFlipView.onFingerUp(event.getX(), event.getY());
//            return true;
//        }
//
//        return mGestureDetector.onTouchEvent(event);
//    }

    @Override
    public boolean onDown(MotionEvent e) {
        // mPageFlipView.onFingerDown(e.getX(), e.getY());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }


    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // mPageFlipView.onFingerMove(e2.getX(), e2.getY());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
}
