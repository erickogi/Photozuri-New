package com.photozuri.photozuri.Payments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.bdhobare.mpesa.Mode;
import com.bdhobare.mpesa.Mpesa;
import com.bdhobare.mpesa.interfaces.AuthListener;
import com.bdhobare.mpesa.interfaces.MpesaListener;
import com.bdhobare.mpesa.models.STKPush;
import com.bdhobare.mpesa.utils.Pair;
import com.photozuri.photozuri.Data.ApiConstants;
import com.photozuri.photozuri.Data.Models.ImageUpload;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.Models.TitleModel;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.Data.TestDataGen;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.NetworkUtills.DumbVolleyRequest;
import com.photozuri.photozuri.NetworkUtills.NetworkUtils;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.DateTimeUtils;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.MyToast;
import com.photozuri.photozuri.Utills.UtilListeners.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

//import com.android.volley.VolleyError;

public class Payments extends AppCompatActivity implements AuthListener, MpesaListener {
    TitleModel titleModel;
    int nonetwork = 34;
    ProgressBar progressBar;
    TextView txtImage;
    ArrayList<MyImage> myImages;
    int progress = 0;
    String orderId = GlobalConsts.ORDER_DEFAULT_ID;
    private int isUploading = 0;
    private DbOperations dbOperations;
    private DbContentValues dbContentValues;
    // private GeneralUtills generalUtills;
    private AppUtils appUtils;
    private TestDataGen testDataGen;
    private int upload = 90;
    private int save = 100;
    private int pay = 192;
    private ProgressDialog progressDialog;
    private TextView txtAmount, txtProgress;
    private EditText textInputPhone;
    private String SUCCESFULL_UPLOAD = "Congratulations, your order was received successfully. You will be notified once the printing is done.";
    private String SUCCESFULL_PAYMRNT = "Congratulations, payment was received successfully. Click okay to start upload";
    private String SUCCESFULL_SAVE = "Congratulations, your order was saved successfully. ";
    private String NO_NETWORK = "The apps needs a stable network connection ";
    private LinearLayout success;
    private LinearLayout linearpay;
    private Button paynow, savenow;
    private int uploadStatus;
    private Button btnUpload;
    private int start = 0;
    private int uploading = 1;
    private int paused = 2;
    private int done = 3;
    private NetworkUtils networkUtils;
    private int notUploadedcount = 0;
    private int uploadedcount = 0;
    private int uploadstatus = 0;
    private BroadcastReceiver codeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("closeoperation", "truecode received");
            if (intent.getAction().equals("com.photo.codereceived")) {
                try {

                    btnUpload.setVisibility(View.VISIBLE);

                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    updatePaid(titleModel.getTitle_id());
                    changeView();

                } catch (NullPointerException nm) {
                    nm.printStackTrace();
                    Log.d("closeoperation", nm.toString());
                }


            }

        }
    };
    private boolean triedOnce = false;

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

    void initSTK(int amount, String phone) {
        STKPush.Builder builder =
                new STKPush.Builder(
                        ApiConstants.safaricom_bussiness_short_code,///business shortcode
                        ApiConstants.safaricom_pass_key,//passkey
                        1, //amount
                        ApiConstants.safaricom_party_b,//party b key
                        phone);//phone no from

        //builder.setCallBackURL("192.168.120:47");
        STKPush push = builder.build();

        Mpesa.getInstance().pay(Payments.this, push);
    }

    void changeView() {
        paynow.setVisibility(View.GONE);
        savenow.setVisibility(View.GONE);
        success.setVisibility(View.VISIBLE);
        linearpay.setVisibility(View.GONE);


    }

    private void updatePaid(int title_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.ISPAID, GlobalConsts.TITLE_IS_PAID);
        dbOperations.update(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, title_id, contentValues);
    }

    private void updateOrderID(String id, int key_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.ORDER_ID, id);
        dbOperations.update(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, key_id, contentValues);
    }

    void initMpesa() {
        Mpesa.with(Payments.this, ApiConstants.safaricom_Auth_key, ApiConstants.safaricom_Secret, Mode.SANDBOX);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        savenow = findViewById(R.id.btn_save);
        Intent intent = getIntent();
        if (intent.getBooleanExtra("saved", false)) {
            savenow.setVisibility(View.GONE);
        }
        dbOperations = new DbOperations(Payments.this);

        txtImage = findViewById(R.id.txt_image);
        txtImage.setText("");
        upload = start;
        networkUtils = new NetworkUtils(Payments.this);
        btnUpload = findViewById(R.id.btn_upload);
        txtProgress = findViewById(R.id.txt_upload_text);
        progressBar = findViewById(R.id.pb);


        //  Mpesa.with(Payments.this, ApiConstants.safaricom_Auth_key, ApiConstants.safaricom_Secret, Mode.SANDBOX);

        initMpesa();
        registerReceiver(codeReceiver, new IntentFilter("com.photo.codereceived"));

        paynow = findViewById(R.id.buttonCheckOut);
        linearpay = findViewById(R.id.linear_pay);
        success = findViewById(R.id.success);


        txtAmount = findViewById(R.id.txt_amount);
        textInputPhone = findViewById(R.id.edt_phone);

        dbContentValues = new DbContentValues();
        // dbOperations = new DbOperations(Payments.this);
        //generalUtills = new GeneralUtills(Payments.this);
        appUtils = new AppUtils(Payments.this);
        myImages = new ArrayList<>();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();


        titleModel = getTitle(GlobalConsts.TITLE_ID);

        if (titleModel != null) {


            txtAmount.setText(titleModel.getAmount() + " - Ksh");
            textInputPhone.setText(GeneralUtills.sanitizePhone(titleModel.getContactphone()));

            try {
                //uploadedcount = titleModel.getUPLOADED_COUNT();
                // notUploadedcount = titleModel.getNOT_UPLOADED_COUNT();
                // uploadStatus = titleModel.getUPLOAD_STATUS();
                // orderId = titleModel.getUPLOAD_ORDER_ID();
            } catch (Exception nm) {
                nm.printStackTrace();
            }


        }

        if (titleModel != null && titleModel.getISPAID().equals(GlobalConsts.TITLE_IS_PAID)) {
            changeView();
            upload = start;
            isUploading = 2;
            if (titleModel.getOrder_id() != null && !titleModel.getOrder_id().equals(GlobalConsts.ORDER_DEFAULT_ID)) {
                orderId = titleModel.getOrder_id();
            } else {
                orderId = titleModel.getUPLOAD_ORDER_ID();
            }
            progressBar.setProgress(titleModel.getUPLOADED_COUNT());
            progress = titleModel.getUPLOADED_COUNT();
            btnUpload.setText("Resume");
            txtProgress.setText("Paused  " + "" + progress);


        }
    }

    private ArrayList<MyImage> getMyImages() {
        Cursor cursor = dbOperations.select(DbConstants.TABLE_SAVED_DATA, DbConstants.TITLE_ID, GlobalConsts.TITLE_ID);

        if (cursor != null && cursor.getCount() > 0) {

            myImages = appUtils.updateList(myImages, dbContentValues.getSavedData(cursor));
            Collections.sort(myImages);
        }

        return myImages;
    }

    public void pay(View view) {

        NetworkUtils networkUtils = new NetworkUtils(Payments.this);
        if (networkUtils.isNetworkAvailable()) {

            progressDialog = new ProgressDialog(Payments.this);
            progressDialog.setMessage("Preparing transaction....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            if (titleModel != null && !GeneralUtills.sanitizePhone(textInputPhone.getText().toString()).equals("")) {
                initSTK(Integer.valueOf(titleModel.getAmount()), GeneralUtills.sanitizePhone(textInputPhone.getText().toString()));
                //changeView();
            }
        } else {
            alertDialog(NO_NETWORK, nonetwork);
        }


    }
//    private void startDialog(int type) {
//
//        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(HomeActivity.this);
//        View mView = layoutInflaterAndroid.inflate(R.layout.title_desc, null);
//        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(HomeActivity.this);
//        alertDialogBuilderUserInput.setView(mView);
//        alertDialogBuilderUserInput.setTitle("New album");
//        alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
//        RadioGroup radioGroupSize=mView.findViewById(R.id.radio_group_size);
//        if(type== Constants.WALL_MOUNT_INTENT){
//            radioGroupSize.setVisibility(View.VISIBLE);
//        }
//
//        // final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
//        alertDialogBuilderUserInput
//                .setCancelable(false)
//                .setPositiveButton("Next", (dialogBox, id) -> {
//                    // ToDo get user input here
//
//
//                })
//
//                .setNegativeButton("Dismiss",
//                        (dialogBox, id) -> dialogBox.cancel());
//
//        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
//        alertDialogAndroid.show();
//        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
//        theButton.setOnClickListener(new HomeActivity.CustomListener(alertDialogAndroid, type));
//    }

    public void save(View view) {


        ContentValues cv = new ContentValues();
        cv.put(DbConstants.IMAGE_STATUS, DbConstants.saved);

        if (dbOperations.update(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, GlobalConsts.TITLE_ID, cv)) {
            alertDialog(SUCCESFULL_SAVE, save);

        }


    }

    private void alertDialog(final String message, int type) {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (type == pay) {
                            dialog.dismiss();
                            startUpload();

                        } else if (type == save) {
                            dialog.dismiss();
                            finish();
                        }


                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();

                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(Payments.this);
//        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Payments.this);
//        View mView = layoutInflaterAndroid.inflate(R.layout.success_popup, null);
//
//        builder.setView(mView);

        builder
                .setMessage(message)
                .setPositiveButton("Dismiss", dialogClickListener)
                //.setNegativeButton("No", dialogClickListener)
                .show();

    }

    private void alertDialogUploadSucces() {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:


                        dialog.dismiss();
                        finish();

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();


                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(Payments.this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Payments.this);
        View mView = layoutInflaterAndroid.inflate(R.layout.success_popup, null);

        builder.setView(mView);

        builder
                //.setMessage(message)
                .setPositiveButton("Dismiss", dialogClickListener)
                //.setNegativeButton("No", dialogClickListener)
                .show();

    }

    private void startUpload() {


    }

    @Override
    public void onAuthError(Pair<Integer, String> result) {

        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                alertDialog("An error occured Please confirm your mpesa number and internet connection");
            }
        } catch (Exception nm) {

        }
    }

    @Override
    public void onAuthSuccess() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                //progressDialog.dismiss();
            }
        } catch (Exception nm) {

        }
    }

    @Override
    public void onMpesaError(Pair<Integer, String> result) {

        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                alertDialog("An error occured Please confirm your mpesa number and internet connection");
            }
        } catch (Exception nm) {

        }
    }

    private void alertDialog(String s) {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        dialog.dismiss();


                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();

                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(Payments.this);
//        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(Payments.this);
//        View mView = layoutInflaterAndroid.inflate(R.layout.success_popup, null);
//
//        builder.setView(mView);

        builder
                .setMessage(s)
                .setPositiveButton("Dismiss", dialogClickListener)
                //.setNegativeButton("No", dialogClickListener)
                .show();
    }

    @Override
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage) {

        try {
            MyToast.toast(CustomerMessage, Payments.this, R.drawable.ic_done_color_24dp, Constants.TOAST_LONG);
            if (progressDialog != null && progressDialog.isShowing()) {
                // progressDialog.dismiss();

            }
        } catch (Exception nm) {

        }

        // progressDialog.dismiss();
        //changeView();


    }

    private void upload(MyImage image) {

        //if(image.getUPLOAD_STATUS()==GlobalConsts.IMAGE_NOT_UPLOADED) {
        Bitmap b = loadImageFromStorage(image.getPath());
        String imaged = getStringImage(b);


        PrefManager prefManager = new PrefManager(Payments.this);

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(prefManager.getUserData().getUserId()));
        params.put("title", titleModel.getTitle_name());
        String category = "";
        if (titleModel.getImage_type() == Constants.WALL_MOUNT_INTENT) {
            category = "Wallmount";
        } else if (titleModel.getImage_type() == Constants.PASSPORT_INTENT) {
            category = "Passport";
        } else if (titleModel.getImage_type() == Constants.PHOTO_BOOK_INTENT) {
            category = "Photobook";
        } else {
            category = "Others";
        }
        params.put("category", category);
        params.put("photoname", titleModel.getTitle_name() + "" + DateTimeUtils.getNow() + "" + String.valueOf(GeneralUtills.getRandon(5000, 1)));
        params.put("location", titleModel.getAddress());
        params.put("amount", titleModel.getAmount());
        // titleModel.getAddress();
        //if(myImages.size()>0)
        params.put("count", String.valueOf(myImages.size()));

        params.put("description", titleModel.getTitle_desc());
        params.put("front_cover", myImages.get(0).getPath());
        params.put("back_cover", myImages.get(myImages.size() - 1).getPath());
        params.put("status", "new");
        params.put("mobile", prefManager.getUserData().getPhoneNumber());


        // $created_at = $_POST['created_at'];
        params.put("order_id", orderId);
        if (imaged != null && imaged.length() > 1) {
            params.put("image", imaged);
        } else {
            params.put("image", "null");
        }
        String caption = "1";
        if (image.getCaption() != null) {
            caption = image.getCaption();
        }
        params.put("caption", caption);

        params.put("no_count", caption);
        String size = "null";
        if (GlobalConsts.WALLMOUNTSELECTED != null) {
            size = GlobalConsts.WALLMOUNTSELECTED;
        }
        params.put("size", size);
        if (image.getImagePos() > 0) {
            params.put("page_no", String.valueOf(image.getImagePos()));
        }
        if (orderId.equals(GlobalConsts.ORDER_DEFAULT_ID)) {
            params.put("uploadtype", "new");
        } else {
            params.put("uploadtype", "old");
        }
        params.put("back", "false");
        if (image == myImages.get(myImages.size() - 1)) {
            params.put("back", image.getNo());
        }
        params.put("cover", "false");
        if (image == myImages.get(0)) {
            params.put("cover", "true");
        }





//            Log.d("uploadParams", params.toString());
        DumbVolleyRequest.getPostData(com.photozuri.photozuri.NetworkUtills.ApiConstants.upload, params, new RequestListener() {
            @Override
            public void onError(VolleyError error) {

                //upload(image);
                if (networkUtils.isNetworkAvailable()) {
                    upload(image);
                } else {

                    //Toast.makeText(Payments.this, "Fatal error uploading ! Please check your internet connection", Toast.LENGTH_LONG).show();
                }
                Log.d("uploadErr", " Image  " + image.getImagePos() + "   " + error.toString());
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {
                Log.d("uploadErresponse", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {


                        // uploadMultipart(image);
                        uploadedcount++;
                        notUploadedcount = myImages.size() - uploadedcount;
                        updateProgress(image, orderId);


                        //progressBar.setProgress(progress+1);
                        if (orderId.equals(GlobalConsts.ORDER_DEFAULT_ID)) {

                            orderId = jsonObject.getString("orderId");
                        }
                        if (progress < myImages.size() - 1 && isUploading > 0 && isUploading < 2) {
                            progress++;


                            txtProgress = findViewById(R.id.txt_upload_text);
                            txtProgress.setText("Uploading " + "" + progress + "/" + myImages.size());
                            progressBar.setProgress(progress + 1);
                            upload(myImages.get(progress));
                        } else if (progress >= myImages.size() - 1 && isUploading > 0 && isUploading < 2) {


                            txtProgress.setText("Uploading " + "" + progress + 1);
                            progressBar.setProgress(progress + 1);
                            progressBar.setVisibility(View.GONE);

                            alertDialogUploadSucces();
                        } else {
                            progressBar.setProgress(progress + 1);
                        }
                        Log.d("uploadErr", " Image  " + image.getImagePos() + "   " + response);
                    } else {
                        if (networkUtils.isNetworkAvailable()) {
                            upload(image);
                        } else {
                            Toast.makeText(Payments.this, "Fatal error uploading ! Please check your internet connection", Toast.LENGTH_LONG).show();
                        }

                        Log.d("uploadErrormsg", jsonObject.getString("message"));
                        Toast.makeText(Payments.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();


                        // upload(myImages.get(progress));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

//        }else if(image.getUPLOAD_STATUS()==GlobalConsts.IMAGE_UPLOADED){
//            progress++;
//            upload(myImages.get(progress));
//        }else {
//            upload(myImages.get(progress));
//        }

    }

    private void updateProgress(MyImage image, String orderId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbConstants.UPLOAD_STATUS, GlobalConsts.SOME_UPLOADED);
        contentValues.put(DbConstants.UPLOADED_COUNT, uploadedcount);
        contentValues.put(DbConstants.UPLOAD_ORDER_ID, orderId);
        contentValues.put(DbConstants.ORDER_ID, orderId);
        contentValues.put(DbConstants.NOT_UPLOADED_COUNT, notUploadedcount);


        ContentValues cv = new ContentValues();
        cv.put(DbConstants.UPLOAD_STATUS, GlobalConsts.IMAGE_UPLOADED);


        dbOperations.update(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, image.getTitle_id(), contentValues);
        dbOperations.update(DbConstants.TABLE_SAVED_DATA, DbConstants.KEY_ID, image.getKEY_ID(), cv);
        updateOrderID(orderId, image.getTitle_id());

    }

    private void alertDialogUploadSucces1() {


    }

    public void startStopPause(View view) {
        if (upload == start) {
            upload = uploading;
            isUploading = 1;
            progressBar = findViewById(R.id.pb);
            btnUpload = findViewById(R.id.btn_upload);
            txtProgress = findViewById(R.id.txt_upload_text);
            btnUpload.setText("Pause");
            try {
                int pr = (progress / myImages.size()) * 100;
                txtProgress.setText("Uploading   " + progress);
            } catch (Exception nm) {
                nm.printStackTrace();
            }

            ArrayList<MyImage> images = getMyImages();
            if (images != null && images.size() > 0) {
                int size = images.size();
                int type = GlobalConsts.TYPE_SELECTED;

                progressBar.setMax(size);
                progressBar.setProgress(0);
                progressBar.setIndeterminate(true);


                uploadMultipart(myImages.get(progress));
                // upload(myImages.get(progress));


            }

        } else if (upload == uploading) {

            upload = start;
            isUploading = 2;
            progressBar.setIndeterminate(false);
            progressBar.setProgress(progress);
            int pr = (progress / myImages.size()) * 100;
            btnUpload.setText(" Resume   ");
            txtProgress.setText("Paused  at  " + String.valueOf(progress));
        } else {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(codeReceiver);

    }

    public Bitmap loadImageFromStorage(String path) {
        Bitmap b = null;
        try {
            //  ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            //  File path1 = cw.getDir("images", Context.MODE_PRIVATE);
            File f = new File(path);

            // Log.d("drawingbit", f.toString() + "    \n");
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            // ImageView img = (ImageView) findViewById(R.id.viewImage);
            // img.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Log.d("loadException", e.toString());
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            //Log.d("outOfMemory", e.toString());
        }

        return b;
    }

    public String getStringImage(Bitmap bmp) {
        String re = "n";
        try {
            // create lots of objects here and stash them somewhere
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (bmp != null) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                re = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            }
        } catch (OutOfMemoryError E) {


            System.gc();
            upload(myImages.get(progress));
            // release some (all) of the above objects
        }


//        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
//        byte [] b = baos.toByteArray();
//        return Base64.encodeToString(b, Base64.DEFAULT);
        return re;
    }

    public void uploadMultipart(MyImage image) {
        PrefManager prefManager = new PrefManager(Payments.this);
        ImageUpload imageUpload = new ImageUpload();

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(prefManager.getUserData().getUserId()));
        imageUpload.setUserId(String.valueOf(prefManager.getUserData().getUserId()));
        params.put("title", titleModel.getTitle_name());
        imageUpload.setTitle(titleModel.getTitle_name());
        String category = "";
        if (titleModel.getImage_type() == Constants.WALL_MOUNT_INTENT) {
            category = "Wallmount";
        } else if (titleModel.getImage_type() == Constants.PASSPORT_INTENT) {
            category = "Passport";
        } else if (titleModel.getImage_type() == Constants.PHOTO_BOOK_INTENT) {
            category = "Photobook";
        } else {
            category = "Singleprint";
        }
        imageUpload.setCategory(category);
        params.put("category", category);
        params.put("photoname", titleModel.getTitle_name() + "" + DateTimeUtils.getNow() + "" + String.valueOf(GeneralUtills.getRandon(5000, 1)));
        imageUpload.setLocation(titleModel.getAddress());
        params.put("location", titleModel.getAddress());
        imageUpload.setAmount(titleModel.getAmount());
        params.put("amount", titleModel.getAmount());
        // titleModel.getAddress();
        //if(myImages.size()>0)
        params.put("count", String.valueOf(myImages.size()));
        imageUpload.setCount(String.valueOf(myImages.size()));

        imageUpload.setDescription("description");

        params.put("description", "description");
        File filef = new File(myImages.get(0).getPath());
        String strFileNamef = filef.getName();
        imageUpload.setFront_cover(strFileNamef);


        File fileb = new File(myImages.get(myImages.size() - 1).getPath());
        String strFileNameb = fileb.getName();
        imageUpload.setBack_cover(strFileNameb);


        params.put("front_cover", myImages.get(0).getPath());
        params.put("back_cover", myImages.get(myImages.size() - 1).getPath());


        imageUpload.setStatus("new");
        params.put("status", "new");

        imageUpload.setMobile(prefManager.getUserData().getPhoneNumber());
        params.put("mobile", prefManager.getUserData().getPhoneNumber());
        params.put("created_at", DateTimeUtils.getNow());

        File file = new File(image.getPath());
        String strFileName = file.getName();
        // $created_at = $_POST['created_at'];

        imageUpload.setOrder_id(orderId);
        params.put("order_id", orderId);
//        if (imaged != null && imaged.length() > 1) {
        imageUpload.setImage("https://www.photozuri.com/photozuri/Upload/Upload/" + strFileName);

        params.put("image", "https://www.photozuri.com/photozuri/Upload/Upload/" + strFileName);
//        } else {
//            params.put("image", "null");photozuri/Upload/Upload
//        }


        String caption = "1";
        if (image.getCaption() != null) {
            caption = image.getCaption();
        }
        params.put("caption", caption);
        imageUpload.setCaption(caption);
        imageUpload.setNo_count(caption);
        params.put("no_count", caption);
        String size = "null";
        if (GlobalConsts.WALLMOUNTSELECTED != null) {
            size = GlobalConsts.WALLMOUNTSELECTED;
        }
        params.put("size", size);
        imageUpload.setSize(size);
        //if (image.getImagePos() > 0) {
        params.put("page_no", String.valueOf(image.getImagePos()));
        imageUpload.setPage_no(String.valueOf(image.getImagePos()));
        // }
        if (orderId.equals(GlobalConsts.ORDER_DEFAULT_ID)) {
            imageUpload.setUploadtype("new");
            params.put("uploadtype", "new");
        } else {
            imageUpload.setUploadtype("old");
            params.put("uploadtype", "old");
        }
        params.put("back", "false");
        imageUpload.setBack_cover("false");
        if (image == myImages.get(myImages.size() - 1)) {
            params.put("back", image.getNo());
            // imageUpload.set("false");
        }
        params.put("cover", "false");
        if (image == myImages.get(0)) {
            params.put("cover", "true");
        }
        Log.d("uploadParams", params.toString());

        txtImage = findViewById(R.id.txt_image);
        int[] res = {0, 0};
        try {
            res = GeneralUtills.getResolution(image.getPath());
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        String height = String.valueOf(res[0]);
        String width = String.valueOf(res[1]);
        String xw = height + "*" + width;


        String isize = "";
        try {
            isize = String.valueOf(GeneralUtills.checkFileSize(image.getPath()));

        } catch (Exception nm) {
            nm.printStackTrace();
            isize = "";
        }


        txtProgress.setText("Uploading " + "" + progress + "/" + myImages.size());

        txtImage.setText(strFileName + "   ,   " + xw + "h*w   ,   " + isize + " Kbs");


        DumbVolleyRequest.imageUpload(image.getPath(), imageUpload, com.photozuri.photozuri.NetworkUtills.ApiConstants.upload, new RequestListener() {
            @Override
            public void onError(VolleyError error) {
                // uploadMultipart(image);
                try {
                    Log.d("imgUploadE", "E" + error.toString());
                } catch (Exception m) {

                }
                if (networkUtils.isNetworkAvailable() && !triedOnce) {
                    triedOnce = true;
                    uploadMultipart(image);
                } else {
                    upload = start;
                    isUploading = 2;
                    progressBar.setIndeterminate(false);
                    progressBar.setProgress(progress);
                    int pr = (progress / myImages.size()) * 100;
                    btnUpload.setText(" Resume   ");
                    txtProgress.setText("Paused  at  " + String.valueOf(progress));
                    MyToast.toast("Error uploading ! Please check your internet connection", Payments.this, R.drawable.ic_error_outline_black_24dp, Constants.TOAST_LONG);

                    //Toast.makeText(Payments.this, "Fatal error uploading ! Please check your internet connection", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {
//                Log.d("imgUploadRes", response);
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    String message = jObj.getString("message");
//
//                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                    uploadedcount++;
//                    updateProgress(image, orderId);
//                    progress++;
//                    // progress++;
//
//
//                    txtProgress = findViewById(R.id.txt_upload_text);
//                    txtProgress.setText("Uploading " + "" + progress + "/" + myImages.size());
//                    progressBar.setProgress(progress + 1);
//                    uploadMultipart(myImages.get(progress));
//                    //upload(myImages.get(progress));
//
//                } catch (JSONException e) {
//                    // JSON error
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }

                Log.d("uploadErresponse", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        String message = jsonObject.getString("message");
                        // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        // uploadMultipart(image);
                        uploadedcount++;
                        updateProgress(image, orderId);


                        //progressBar.setProgress(progress+1);
                        if (orderId.equals(GlobalConsts.ORDER_DEFAULT_ID)) {

                            orderId = jsonObject.getString("orderId");
                        }
                        if (progress < myImages.size() - 1 && isUploading > 0 && isUploading < 2) {
                            progress++;


                            txtProgress = findViewById(R.id.txt_upload_text);
                            txtProgress.setText("Uploading " + "" + progress + "/" + myImages.size());
                            progressBar.setProgress(progress + 1);
                            uploadMultipart(myImages.get(progress));
                        } else if (progress >= myImages.size() - 1 && isUploading > 0 && isUploading < 2) {


                            txtProgress.setText("Uploading " + "" + progress + 1);
                            progressBar.setProgress(progress + 1);
                            progressBar.setVisibility(View.GONE);

                            Boolean titledel = dbOperations.delete(DbConstants.TABLE_SAVED_TITLES, DbConstants.KEY_ID, GlobalConsts.TITLE_ID);
                            Boolean photodel = dbOperations.delete(DbConstants.TABLE_SAVED_DATA, DbConstants.SPAlbum_ID, GlobalConsts.TITLE_ID);
                            if (titledel && photodel) {
                                alertDialogUploadSucces();
                            } else {
                                MyToast.toast("Error deleting cache", Payments.this, R.drawable.ic_error_outline_black_24dp, Constants.TOAST_LONG);

                            }
                        } else {
                            progressBar.setProgress(progress + 1);
                        }
                        Log.d("uploadErr", " Image  " + image.getImagePos() + "   " + response);
                    } else {
                        if (networkUtils.isNetworkAvailable()) {
                            // uploadMultipart(image);
                        } else {
                            MyToast.toast("Fatal error uploading ! Please check your internet connection", Payments.this, R.drawable.ic_error_outline_black_24dp, Constants.TOAST_LONG);
                            ///Toast.makeText(Payments.this, "Fatal error uploading ! Please check your internet connection", Toast.LENGTH_LONG).show();
                        }

                        Log.d("uploadErrormsg", jsonObject.getString("message"));
                        //  Toast.makeText(Payments.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        MyToast.toast(jsonObject.getString("message"), Payments.this, R.drawable.ic_error_outline_black_24dp, Constants.TOAST_LONG);


                        // upload(myImages.get(progress));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });


    }
}
