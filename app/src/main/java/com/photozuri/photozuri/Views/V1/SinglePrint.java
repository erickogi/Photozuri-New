package com.photozuri.photozuri.Views.V1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.photozuri.photozuri.Adapter.SinglePrintProcessStepperAdapter;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.Payments.Payments;
import com.photozuri.photozuri.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class SinglePrint extends AppCompatActivity implements StepperLayout.StepperListener {
    private StepperLayout mStepperLayout;
    private SinglePrintProcessStepperAdapter mStepperAdapter;
    private DbOperations dbOperations;
    private PrefManager prefrenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_print);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mStepperLayout = findViewById(R.id.stepperLayout);
        mStepperAdapter = new SinglePrintProcessStepperAdapter(getSupportFragmentManager(), this);
        mStepperLayout.setAdapter(mStepperAdapter);
        mStepperLayout.setListener(this);
    }

    @Override
    public void onCompleted(View completeButton) {

        startActivity(new Intent(SinglePrint.this, Payments.class));
        finish();
    }


    @Override
    public void onError(VerificationError verificationError) {
        // Toast.makeText(this, "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
        mStepperLayout.setShowErrorMessageEnabled(true);
        mStepperLayout.setShowErrorStateEnabled(true);

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
        finish();
    }

}
