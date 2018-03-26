package com.photozuri.photozuri.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.photozuri.photozuri.Views.V1.Fragments.Photobook.FragmentFour;
import com.photozuri.photozuri.Views.V1.Fragments.Photobook.FragmentOne;
import com.photozuri.photozuri.Views.V1.Fragments.Wallmounts.FragmentWmtsPreview;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by Eric on 1/25/2018.
 */

public class WallMountsProcessStepperAdapter extends AbstractFragmentStepAdapter {
    private static final String CURRENT_STEP_POSITION_KEY = "messageResourceId";

    public WallMountsProcessStepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position) {
            case 0:
                final FragmentOne step1 = new FragmentOne();
                Bundle b1 = new Bundle();
                b1.putInt(CURRENT_STEP_POSITION_KEY, position);
                step1.setArguments(b1);
                return step1;
            case 1:
                final FragmentWmtsPreview step2 = new FragmentWmtsPreview();
                Bundle b2 = new Bundle();
                b2.putInt(CURRENT_STEP_POSITION_KEY, position);
                step2.setArguments(b2);
                return step2;

            case 2:
                final FragmentFour step4 = new FragmentFour();
                Bundle b4 = new Bundle();
                b4.putInt(CURRENT_STEP_POSITION_KEY, position);
                step4.setArguments(b4);
                return step4;


        }
        return null;
    }

    @Override
    public int getCount() {

        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        switch (position) {
            case 0:
                return new StepViewModel.Builder(context)

                        .setTitle("Select Images") //can be a CharSequence instead
                        .create();
            case 1:
                return new StepViewModel.Builder(context)
                        .setTitle("Preview Images") //can be a CharSequence instead
                        .create();
            case 2:
                return new StepViewModel.Builder(context)
                        .setTitle("Delivery Details") //can be a CharSequence instead
                        .create();
//            case 3:
//                return new StepViewModel.Builder(context)
//                        .setTitle("Step 4") //can be a CharSequence instead
//                        .create();


        }
        return null;
    }
}
