package com.photozuri.photozuri.Views.V1.Fragments.Photobook;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bdhobare.mpesa.interfaces.AuthListener;
import com.bdhobare.mpesa.interfaces.MpesaListener;
import com.bdhobare.mpesa.utils.Pair;
import com.photozuri.photozuri.Data.Models.LocationModel;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.Models.TitleModel;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.MyToast;
import com.photozuri.photozuri.Views.Login.LoginActivity;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.TELEPHONY_SERVICE;
import static com.photozuri.photozuri.Utills.GeneralUtills.isFilledTextInputEditText;

/**
 * Created by Eric on 1/17/2018.
 */

public class FragmentFour extends Fragment implements BlockingStep, AuthListener, MpesaListener {

    EditText mOrderName;
    private EditText edtTown, edtStreet, edtBuilding, edtPhone;
    private TextView mOrderAmount;
    private DbOperations dbOperations;
    private DbContentValues dbContentValues;
    private ArrayList<MyImage> myImages;
    private AppUtils appUtils;
    private GeneralUtills generalUtills;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        generalUtills = new GeneralUtills(getContext());
        appUtils = new AppUtils(getContext());
        dbContentValues = new DbContentValues();
        dbOperations = new DbOperations(getContext());
        myImages = new ArrayList<>();
        return inflater.inflate(R.layout.payments_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtTown = view.findViewById(R.id.edt_town);
        edtStreet = view.findViewById(R.id.street);
        edtBuilding = view.findViewById(R.id.edt_building);
        edtPhone = view.findViewById(R.id.edt_phone);

        getSim();
        Cursor cursor = dbOperations.select(DbConstants.TABLE_DELIVERY);
        if (cursor != null) {
            ArrayList<LocationModel> locationModels = dbContentValues.getSavedDelivery(cursor);
            if (locationModels != null && locationModels.size() > 0) {
                LocationModel locationModel = locationModels.get(0);
                edtTown.setText(locationModel.getTown());
                edtBuilding.setText(locationModel.getBuilding());
                edtStreet.setText(locationModel.getStreet());
                edtPhone.setText(locationModel.getMobile());
            }
        }

        mOrderAmount = view.findViewById(R.id.orderAmount);
        mOrderName = view.findViewById(R.id.orderName);

        initUI();


    }

    private void changeNameDialog() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.title_desc, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setTitle("");
        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
        RadioGroup radioGroupSize = mView.findViewById(R.id.radio_group_size);


        // TextView txtName = mView.findViewById(R.id.orderName);
        // txtName.setVisibility(View.VISIBLE);


        // final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", (dialogBox, id) -> {
                    // ToDo get user input here


                })

                .setNegativeButton("Dismiss",
                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialogAndroid));

    }

    private ArrayList<MyImage> getMyImages() {
        Cursor cursor = dbOperations.select(DbConstants.TABLE_SAVED_DATA, DbConstants.TITLE_ID, GlobalConsts.TITLE_ID);

        if (cursor != null && cursor.getCount() > 0) {

            myImages = appUtils.updateList(myImages, dbContentValues.getSavedData(cursor));

            Collections.sort(myImages);
        }
        return myImages;
    }

    void initUI() {
//        STKPush.Builder builder = new STKPush.Builder("600292", "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919", 10, "600000", "254714406984");
//
//        builder.setCallBackURL("192.168.120:47");
//        STKPush push = builder.build();
//
//        Mpesa.getInstance().pay(getContext(), push);
        getMyImages();
        String am = String.valueOf(DbContentValues.getPrice(GlobalConsts.TYPE_SELECTED) * myImages.size());
        mOrderAmount.setText(am + "Ksh");
        mOrderName.setText(GlobalConsts.TITLE_SELECTED);



    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.getStepperLayout().showProgress("Operation in progress, please wait...");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //initUI();

                //callback.goToNextStep();
                // callback.getStepperLayout().hideProgress();


            }
        }, 500L);// delay open another fragment,
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {


        ContentValues cv = new ContentValues();
        cv.put(DbConstants.TITLE_PHONE, edtPhone.getText().toString());
        cv.put(DbConstants.TITLE_AMOUNT, String.valueOf(DbContentValues.getPrice(GlobalConsts.TYPE_SELECTED) * myImages.size()));
        cv.put(DbConstants.TITLE_ADDRESS, "Town : " + edtTown.getText().toString() + " Street : " + edtStreet.getText().toString() + "  Building : " + edtBuilding.getText().toString());
        if (dbOperations.update(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, GlobalConsts.TITLE_ID, cv)) {
            LocationModel locationModel = new LocationModel();
            locationModel.setLat("");
            locationModel.setLon("");
            locationModel.setMobile(edtPhone.getText().toString());
            locationModel.setStreet(edtStreet.getText().toString());
            locationModel.setBuilding(edtBuilding.getText().toString());
            locationModel.setTown(edtTown.getText().toString());

            dbOperations.delete(DbConstants.TABLE_DELIVERY);
            new DbContentValues.saveLocation(locationModel, getContext(), result -> {

                if (result) {

                }
            }).execute();

            TitleModel titleModel = new TitleModel();
            titleModel.setTitle_name(mOrderName.getText().toString());
            new DbContentValues.updateTitle(titleModel, getContext(), result -> {

            });

            PrefManager prefManager = new PrefManager(getContext());
            if (prefManager.isLoggedIn()) {


                callback.complete();
            } else {
                login();
            }
        } else {
            MyToast.toast("Error occured ", getContext(), R.drawable.ic_error_outline_black_24dp, Constants.TOAST_LONG);
        }


    }

    private void login() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("Action", Constants.LOGIN_PAY);
        startActivity(intent);
        try {
            getActivity().finish();
        }catch (Exception nm){
            nm.printStackTrace();
        }


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
        if (isFilledTextInputEditText(edtBuilding) && isFilledTextInputEditText(edtPhone)
                && isFilledTextInputEditText(edtStreet) && isFilledTextInputEditText(edtTown)) {
            return null;
        } else {
            return new VerificationError("Some details are missing");
        }
    }

    @Override
    public void onSelected() {

        initUI();
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onAuthError(Pair<Integer, String> result) {

    }

    @Override
    public void onAuthSuccess() {

    }

    @Override
    public void onMpesaError(Pair<Integer, String> result) {

    }

    @Override
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage) {

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS}, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // startScan();
                getSim();

            } else {
                // permisionDeneied();
            }


        }
    }

    void getSim() {
        try {
            TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(TELEPHONY_SERVICE);

            String number = "07";
            if (tm != null) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    requestPermissions();
                }
                try {
                    number = tm.getLine1Number();
                } catch (Exception nm) {
                    nm.printStackTrace();
                }
            }
            Log.d("nimv", number);

            edtPhone.setText(number);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener(Dialog dialog) {
            this.dialog = dialog;


        }

        @Override
        public void onClick(View v) {
            Log.d("titlesave", "recieved onclick event");


            EditText edttitle = dialog.findViewById(R.id.txt_album_title);


        }
    }
}
