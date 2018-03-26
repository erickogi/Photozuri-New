package com.photozuri.photozuri.Views.V1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.error.VolleyError;
import com.bumptech.glide.Glide;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.Data.UserModel;
import com.photozuri.photozuri.NetworkUtills.ApiConstants;
import com.photozuri.photozuri.NetworkUtills.DumbVolleyRequest;
import com.photozuri.photozuri.NetworkUtills.NetworkUtils;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.MyToast;
import com.photozuri.photozuri.Utills.UtilListeners.RequestListener;
import com.photozuri.photozuri.Views.ProductsActivity;
import com.photozuri.photozuri.imagepicker.model.Config;
import com.photozuri.photozuri.imagepicker.ui.imagepicker.ImagePicker;

import java.util.ArrayList;
import java.util.HashMap;

//import com.android.volley.VolleyError;

public class MyProfile extends AppCompatActivity {
    private static ArrayList<MyImage> images = new ArrayList<>();
    private final int CAMERA_REQUEST = 1888;
    PrefManager prefManager;
    UserModel userModel = null;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private Bitmap bitmap;
    private String decodeBitmap = null;
    private Button btnUpdate;
    private EditText mFirstName, mLastName, mEmail, mMobile, mPassword;
    private TextView txtTakePhoto;
    private ImageView imageCamera;
    private String imagePath = "1";
    private RelativeLayout relativeLayoutCamera;

    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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
                .setMultipleMode(false)              //  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  MyImage title (works with FolderMode = false)
                .setDoneTitle("Done")               //  Done button title
                .setLimitMessage("You can only choose one")    // Selection limit message
                .setMaxSize(1)                     //  Max images can be selected
                .setSavePath("Profile")         //  MyImage capture folder name
                //.setSelectedImages(images)          //  Selected images
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start();                           //  Start ImagePicker
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null) {
            // images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            //adapter.setData(images);

            //adapter.setData(images);'
            AppUtils appUtils = new AppUtils(MyProfile.this);
            ArrayList<MyImage> images = appUtils.convertImages(data.getParcelableArrayListExtra(Config.EXTRA_IMAGES), 0);
            if (images != null && images.size() > 0) {
                for (int a = 0; a < images.size(); a++) {

                    images.get(a).setImageFrom(Constants.GALLERY);
                }

                if (images.get(0) != null) {
                    imagee(images.get(0));
                }
            }
            // update(data.getParcelableArrayListExtra(Config.EXTRA_IMAGES));
        }
    }

    private void imagee(MyImage image) {

        Log.d("imagepath", image.getPath());
        Glide.with(MyProfile.this)
                .load(image.getPath())
                //.apply(options)
                .into(imageCamera);
        imagePath = image.getPath();

//        try {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    Bitmap b= null;
//                    try {
//                        b = Picasso.with(MyProfile.this).load(image.getPath()).get();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    bitmap=b;
//                    decodeBitmap=GeneralUtills.bitmapToBase64(b);
//                }
//            }).start();
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //    imageCamera.setImageBitmap(GeneralUtills.base64ToBitmap(decodeBitmap));

//        Bitmap photo = (Bitmap) data.getExtras().get("data");
//        Bitmap sb = Bitmap.createScaledBitmap(photo, 60, 60, false);
//        bitmap = sb;
//        try {
//            BitmapDrawable drawable = (BitmapDrawable) imageCamera.getDrawable();
//            Bitmap bitmap = drawable.getBitmap();
//            // Bitmap bitmap=imageCamera.getDrawable();
//            decodeBitmap = GeneralUtills.bitmapToBase64(bitmap);
//        } catch (Exception nm) {
//            nm.printStackTrace();
//            Log.d("bitmas", decodeBitmap);
//        }


        //    imageCamera.setImageBitmap(sb);


//        relativeLayoutCamera.setBackgroundResource(0);
//        Palette.generateAsync(bitmap, palette -> {
//            try {
//                int bgColor = palette.getMutedColor(this.getResources().getColor(R.color.primary));
//
//                relativeLayoutCamera.setBackgroundColor(bgColor);
//
//            } catch (Exception n) {
//
//            }
//
//
//        });
        txtTakePhoto.setVisibility(View.GONE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        prefManager = new PrefManager(MyProfile.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        relativeLayoutCamera = findViewById(R.id.camera);
        txtTakePhoto = findViewById(R.id.txt_take_photo);
        imageCamera = findViewById(R.id.take_image);
        mFirstName = findViewById(R.id.txt_firstname);
        mLastName = findViewById(R.id.txt_lastname);
        mEmail = findViewById(R.id.txt_emailAdress);
        mMobile = findViewById(R.id.txt_mobile);
        // mPassword=findViewById(R.id.txt_password);


        try {

            mFirstName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp, 0, 0, 0);
            mLastName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp, 0, 0, 0);
            mEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_black_24dp, 0, 0, 0);
            mMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_black_24dp, 0, 0, 0);
            // mPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp,0,0,0);
            // mEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp,0,0,0);


        } catch (Exception hj) {

            hj.printStackTrace();
        }


        userModel = prefManager.getUserData();

        if (userModel != null) {
            try {
                mFirstName.setText(userModel.getFirstName());
                mLastName.setText(userModel.getLastName());
                mEmail.setText(userModel.getEmail());
                mMobile.setText(userModel.getPhoneNumber());


                decodeBitmap = userModel.getPhoto();

//                Glide.with(MyProfile.this)
//                        .load(userModel.getPhoto())
//                        //.apply(options)
//                        .into(imageCamera);

                if (prefManager.isLoggedIn()) {
                    UserModel userModel = new UserModel();
                    if (userModel.getUriPhoto() != null) {
                        Glide.with(MyProfile.this).load(prefManager.getUserData().getUriPhoto())
                                //.apply(options)
                                .into(imageCamera);
                    } else if (userModel.getPhoto() != null) {
                        Glide.with(MyProfile.this).load(prefManager.getUserData().getPhoto())
                                //.apply(options)
                                .into(imageCamera);
                    } else {
                        Glide.with(MyProfile.this).load(R.drawable.ic_account_circle_black_24dp)
                                //.apply(options)
                                .into(imageCamera);
                    }
                }

                // imageCamera.setImageBitmap(GeneralUtills.base64ToBitmap(userModel.getPhoto()));

            } catch (Exception nm) {

                nm.printStackTrace();
            }
        }


        txtTakePhoto.setOnClickListener(v -> {
            pick();
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
//                // Start the image capture intent to take photo
//                startActivityForResult(cameraIntent, CAMERA_REQUEST);
//            }
        });
        try {

            txtTakePhoto.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_camera_alt_black_24dp, 0, 0, 0);
            // mEditTextUserId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp,0,0,0);


        } catch (Exception hj) {


            hj.printStackTrace();
        }


        //android:drawablePadding="4dp"
        // android:drawableStart="@drawable/ic_camera_alt_black_24dp"


        imageCamera.setOnClickListener(v -> {
            pick();
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (cameraIntent.resolveActivity(getPackageManager()) != null) {
//            // Start the image capture intent to take photo
//                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
//              }

        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.hide();


    }

    //    public void onActivityResult(int resquestCode, int resultCode, Intent data) {
//        if (resquestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            Bitmap sb = Bitmap.createScaledBitmap(photo, 60, 60, false);
//            bitmap = sb;
//
//            decodeBitmap= GeneralUtills.bitmapToBase64(bitmap);
//
//
//
//
//            imageCamera.setImageBitmap(sb);
//
//
//            relativeLayoutCamera.setBackgroundResource(0);
//            Palette.generateAsync(bitmap, palette -> {
//                try {
//                    int bgColor = palette.getMutedColor(this.getResources().getColor(R.color.primary));
//
//                    relativeLayoutCamera.setBackgroundColor(bgColor);
//
//                } catch (Exception n) {
//
//                }
//
//
//            });
//            txtTakePhoto.setVisibility(View.GONE);
//
//        }
//    }
    private boolean validateFields() {
        if (!isValidEmail(mEmail.getText().toString())) {
            mEmail.setError("Invalid email");
            return false;
        } else if (!isValidPhoneNumber(mMobile.getText().toString())) {
            mMobile.setError("Invalid Number");
            return false;
        } else {
            return true;
        }


    }

    private boolean isFilled(TextInputEditText textInputEditText) {
        if (textInputEditText.getText().toString().equals("")) {
            textInputEditText.setError("Required");
            return false;
        } else {
            return true;
        }

    }

    public void update(View view) {

        if (GeneralUtills.isFilledTextInputEditText(mFirstName) && GeneralUtills.isFilledTextInputEditText(mLastName) &&
                GeneralUtills.isFilledTextInputEditText(mEmail) && GeneralUtills.isFilledTextInputEditText(mMobile) && decodeBitmap != null) {

            if (validateFields()) {

                progressDialog = new ProgressDialog(MyProfile.this);
                progressDialog.setMessage("Updating....");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                String name = mFirstName.getText().toString();
                String email = mEmail.getText().toString();
                String mobile = mMobile.getText().toString();


                String token = "";

                //if (key == 1) {
                NetworkUtils n = new NetworkUtils(MyProfile.this);
                if (n.isNetworkAvailable()) {
                    updates(userModel.getUserId(), name, email, mobile, decodeBitmap);
                } else {
                    MyToast.toast("Check your internet Connection", MyProfile.this, R.drawable.ic_error_outline_black_24dp, Constants.TOAST_LONG);
                }
//                    requestForSMSStudent(name
//                            , email, mobile, token);
//                    } else {
//                        requestForSMS(name, email, mobile, token);
//                    }

            }
        } else {

        }

    }

    private void updates(int userId, String name, String email, String mobile, String decodeBitmap) {

        UserModel userModel = new UserModel();
        userModel.setUserId(prefManager.getUserData().getUserId());
        userModel.setPhoto(decodeBitmap);
        userModel.setPhoto(imagePath);
        userModel.setPhoneNumber(mobile);
        userModel.setEmail(email);
        userModel.setFirstName(mFirstName.getText().toString());
        userModel.setLastName(mLastName.getText().toString());
        userModel.setUserName(name);
        prefManager.setUserData(userModel);

        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("user", "PHOTOZURI");
        params.put("mobile", mobile);
        params.put("image", decodeBitmap);
        params.put("id", String.valueOf(userId));

        DumbVolleyRequest.getPostData(ApiConstants.update_profile_endpoint, params, new RequestListener() {
            @Override
            public void onError(VolleyError error) {
                progressDialog.dismiss();
                MyToast.toast("Error Updating profile", MyProfile.this, R.drawable.ic_error_outline_black_24dp, Constants.TOAST_LONG);
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

                progressDialog.dismiss();
                finish();
                startActivity(new Intent(MyProfile.this, ProductsActivity.class));

            }
        });

    }
}
