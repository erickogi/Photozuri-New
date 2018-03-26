package com.photozuri.photozuri.Views.Login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.error.VolleyError;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.Data.UserModel;
import com.photozuri.photozuri.Data.UserModelParse;
import com.photozuri.photozuri.MainActivity;
import com.photozuri.photozuri.NetworkUtills.ApiConstants;
import com.photozuri.photozuri.NetworkUtills.DumbVolleyRequest;
import com.photozuri.photozuri.NetworkUtills.NetworkUtils;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.UtilListeners.RequestListener;
import com.photozuri.photozuri.Views.V1.ScrollingActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.HashMap;

//import com.android.volley.VolleyError;


/**
 * Created by Eric on 9/12/2017.
 */

public class LoginFragment extends Fragment {
    ProgressDialog progressDialog;
    boolean debug = false;
    boolean debugusertype = true;
    // PicassoClient picassoClient = new PicassoClient();
    Bitmap thumnail = null;//BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_person_black_24dp);
    private PrefManager prefrenceManager;
    private TextInputEditText mEditTextPassword, mEditTextUserId;
    private MaterialDialog dialog;
    private View view;
    private PrefManager pref;
    private Dialog dialoge;
    private TextInputEditText mEmail;
    private Button btnLogin;
   // private Controller controller;
    private Spinner spinnerUser;
    private int key = 1;
    private DumbVolleyRequest dumbVolleyRequest;

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
        // view = inflater.inflate(R.layout.login_fragment, container, false);
        prefrenceManager = new PrefManager(getContext());

        String id=prefrenceManager.getFbId();
        Log.d("idd",id);
        dumbVolleyRequest = new DumbVolleyRequest();
        mEditTextPassword = view.findViewById(R.id.edt_passwordl);
        mEditTextUserId = view.findViewById(R.id.edt_user_idl);

        try{

            mEditTextPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp,0,0,0);
            mEditTextUserId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp,0,0,0);


        }catch (Exception hj){

            hj.printStackTrace();
        }
        btnLogin = view.findViewById(R.id.btn_login);
      //  key = getArguments().getInt("key");

        btnLogin.setOnClickListener(v -> login(1));
       // controller = new Controller();


        return view;
    }

    private void initiate() {
        if (isFilled(mEmail)) {
            //if(validateFields())
            //{
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Login in ....");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            //if(key==1) {
           // requestForSMS(mEmail.getText().toString());
            // }else {
            // requestLoginTutor(mEmail.getText().toString());
            //  }

            //}
        }
    }

//    private void login(int a) {
//        NetworkUtils networkUtils=new NetworkUtils(getContext());
//
//        if(networkUtils.isNetworkAvailable()) {
//            if (!debug) {
//                if (isFilled(mEditTextPassword) && isFilled(mEditTextUserId)) {
//
//                    MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext())
//                            .title("Logging in")
//                            .content("Please Wait")
//                            .cancelable(false)
//                            .progress(true, 0);
//
//
//                    dialog = builder.build();
//                    dialog.show();
//
//
//                    String url = ApiConstants.login_endpoint;
//                    HashMap<String, String> params = new HashMap<>();
//                    params.put("mobile",mEditTextUserId.getText().toString());
//                    params.put("password",mEditTextUserId.getText().toString());
//
//                    DumbVolleyRequest.getPostData(url, params, new RequestListener() {
//                        @Override
//                        public void onError(VolleyError error) {
//                            alertDialogDelete("Login error ,Try registering");
//                            dialog.dismiss();
//                        }
//
//                        @Override
//                        public void onError(String error) {
//
//                            dialog.dismiss();
//                        }
//
//                        @Override
//                        public void onSuccess(String response) {
//
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                if (jsonObject.getString("error").equals("false")) {
//                                    UserModel userModel = UserModelParse.getUser(response);
//
//                                    prefrenceManager.setUserData(userModel);
//                                    prefrenceManager.setIsLoggedIn(true);
//                                    startActivity(new Intent(getActivity(), ScrollingActivity.class));
//                                    getActivity().finish();
//
//                                } else {
//
//                                    alertDialogDelete(jsonObject.getString("message"));
//                                }
//                            } catch (JSONException j) {
//
//                            }
//
//
//                        }
//                    });
//
//
//                }
//            } else {
//                UserModel userModel = new UserModel();
//                userModel.setUserName("Eric Kogi");
//                // userModel.setUserType(Constants.DEBUG_SOCIAL_WORKER);
//                userModel.setPhoneNumber("0714406984");
//                userModel.setEmail("erickogi14@gmail.com");
//                userModel.setLastName("Kogi");
//                userModel.setFirstName("Eric");
//                //  userModel.setDesignation("");
//                userModel.setPhoto("");
//                //  prefrenceManager.setAccount(0);
//                prefrenceManager.setUserData(userModel);
//                prefrenceManager.setIsLoggedIn(true);
//                startActivity(new Intent(getActivity(), ScrollingActivity.class));
//                getActivity().finish();
////            dialog.dismiss();
//
//
//            }
//        }else {
//            alertDialogDelete("Check your Internet Connection");
//        }
//    }

    private boolean validateFields() {
        if (!isValidEmail(mEmail.getText().toString())) {
            mEmail.setError("Invalid Email");
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


    void popOutFragments() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    private void login(int a) {
        NetworkUtils networkUtils = new NetworkUtils(getContext());

        if (networkUtils.isNetworkAvailable()) {
            if (!debug) {
                if (GeneralUtills.isFilledTextInputEditText(mEditTextPassword) && GeneralUtills.isFilledTextInputEditText(mEditTextUserId)) {

                    MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext())
                            .title("Logging in")
                            .content("Please Wait")
                            .cancelable(false)
                            .progress(true, 0);


                    dialog = builder.build();
                    dialog.show();


                    String url = ApiConstants.login_endpoint;
                    HashMap<String, String> params = new HashMap<>();
                    params.put("mobile", mEditTextUserId.getText().toString());
                    params.put("password", mEditTextPassword.getText().toString());
                    params.put("user", "PHOTOZURI");

                    DumbVolleyRequest.getPostData(url, params, new RequestListener() {
                        @Override
                        public void onError(VolleyError error) {
                            alertDialogDelete("Login error ,Try registering");
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(String error) {

                            dialog.dismiss();
                        }

                        @Override
                        public void onSuccess(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("error").equals("false")) {
                                    UserModel userModel = UserModelParse.getUser(response);

                                    dialog.dismiss();
                                    prefrenceManager.setUserData(userModel);
                                    prefrenceManager.setIsLoggedIn(true);
                                    startActivity(new Intent(getActivity(), ScrollingActivity.class));
                                    getActivity().finish();

                                } else {

                                    dialog.dismiss();
                                    alertDialogDelete(jsonObject.getString("message"));
                                }
                            } catch (JSONException j) {

                                Log.d("logingerr", j.toString() + "  " + response);
                                dialog.dismiss();
                            }


                        }
                    });


                }
            } else {
                UserModel userModel = new UserModel();
                userModel.setUserName("Eric Kogi");
                // userModel.setUserType(Constants.DEBUG_SOCIAL_WORKER);
                userModel.setPhoneNumber("0714406984");
                userModel.setEmail("erickogi14@gmail.com");
                userModel.setLastName("Kogi");
                userModel.setFirstName("Eric");
                //  userModel.setDesignation("");
                userModel.setPhoto("");
                //  prefrenceManager.setAccount(0);
                prefrenceManager.setUserData(userModel);
                prefrenceManager.setIsLoggedIn(true);
                startActivity(new Intent(getActivity(), ScrollingActivity.class));
                getActivity().finish();
//            dialog.dismiss();


            }
        } else {
            alertDialogDelete("Check your Internet Connection");
        }
    }


    private void alertDialogDelete(final String message) {
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //popOutFragments();
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();

                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(message).setPositiveButton("Okay", dialogClickListener)
                //.setNegativeButton("No", dialogClickListener)
                .show();

    }

//    private void requestForSMS(final String mobile) {
//        String ur = ApiConstants.login_endpoint
//
//                + "UserID=6287"
//                + "&Password=6287";
//
//
//        DumbVolleyRequest.getGetData(ur, new RequestListener() {
//            @Override
//            public void onError(VolleyError error) {
//                try {
//                    dialog.dismiss();
//                } catch (Exception ignored) {
//
//                }
//                alertDialogDownload("Something went wrong. Please try again");
//
//
//            }
//
//            @Override
//            public void onError(String error) {
//
//            }
//
//            @Override
//            public void onSuccess(String response) {
//                if (!response.equals("ErrorEmpty") && !response.equals("Empty")) {
//
//                    try {
//                        Gson gson = new Gson();
//
//                        Type userListType = new TypeToken<LinkedList<UserModel>>() {
//                        }.getType();
//
//                        LinkedList<UserModel> userModels = gson.fromJson(response, userListType);
//
//                        UserModel userModel = userModels.get(0);
//
//                        prefrenceManager.setUserData(userModel);
//                        prefrenceManager.setIsLoggedIn(true);
//
//                        int sp;
//                        int sw;
//                        if (debugusertype) {
//                            sp = Constants.DEBUG_SERVICE_PROVIDER;
//                            sw = Constants.DEBUG_SOCIAL_WORKER;
//
//
//                        } else {
//                            sp = Constants.SERVICE_PROVIDER;
//                            sw = Constants.SOCIAL_WORKER;
//                        }
//
//
//                        if (userModel.getUserType() == sw) {
//                            try {
//                                dialog.dismiss();
//                            } catch (Exception ignored) {
//
//                            }
//
//
//                            prefrenceManager.setAccount(0);
//
//                            if (a == 1) {
//                                prefrenceManager.setAccount(1);
//
//                                startActivity(new Intent(LoginActivity.this, MainScreen.class));
//                            } else {
//
//                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                            }
//                            finish();
//
//
//                        } else if (userModel.getUserType() == sp) {
//                            try {
//                                dialog.dismiss();
//                            } catch (Exception ignored) {
//
//                            }
//                            prefrenceManager.setAccount(1);
//                            startActivity(new Intent(LoginActivity.this, MainScreen.class));
//                            finish();
//
//
//                        } else {
//                            alertDialogDownload("UN-Recognized User Account");
//                            try {
//                                dialog.dismiss();
//                            } catch (Exception ignored) {
//
//                            }
//                        }
//
//
//                    } catch (Exception nm) {
//                        try {
//                            dialog.dismiss();
//                        } catch (Exception ignored) {
//
//                        }
//                        nm.printStackTrace();
//                        alertDialogDownload("Login Failed . Please Try Again");
//                    }
//
//                } else {
//                    try {
//                        dialog.dismiss();
//                    } catch (Exception ignored) {
//
//                    }
//                    alertDialogDownload("Login Failed . Please Try Again");
//
//                }
//
//            }
//        });
//    }

    //
    public Bitmap getThumbnail(String filename, final String name) {
        //   final Bitmap thumnail = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_person_black_24dp);
//
        Picasso.with(getContext()).load(filename).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d("bitmap", "Loaded");
                // Toast.makeText(getContext(), "Bitmap loaded"+bitmap, Toast.LENGTH_SHORT).show();
                thumnail = bitmap;
                if (
                        storeImage(bitmap, name)) {
                    progressDialog.dismiss();

                    //  if(key==1) {
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                    //  }else {
                    //      startActivity(new Intent(getContext(), MainActivityTutor.class));
                    //  }

                } else {
                    progressDialog.dismiss();
                    // if(key==1) {
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                    // }else {
                    //     startActivity(new Intent(getContext(), MainActivityTutor.class));
                    // }

                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("bitmap", "Failed");
                progressDialog.dismiss();

                // if(key==1) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
//                }else {
//                    startActivity(new Intent(getContext(), MainActivityTutor.class));
//                }
                // Toast.makeText(getContext(), "Bitmap failed"+errorDrawable, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                //progressDialog.dismiss();

                //startActivity(new Intent(getContext(), MainActivity.class));

                Log.d("bitmap", "prepared");
                // Toast.makeText(getContext(), "Bitmap prepare"+placeHolderDrawable, Toast.LENGTH_SHORT).show();

            }
        });
//        Bitmap thumnail = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_person_black_24dp);
//        try {
//            File filepath = getContext().getFileStreamPath(filename);
//            FileInputStream fi = new FileInputStream(filepath);
//            thumnail = BitmapFactory.decodeStream(fi);
//
//        } catch (Exception m) {
//            m.printStackTrace();
//        }
        return thumnail;
    }

    private boolean storeImage(Bitmap b, String nameo) {
        try {
            PrefManager pref = new PrefManager(getContext());

            String name = "T" + nameo;
            String imagePath = name + ".png";
            FileOutputStream fos = getContext().openFileOutput(name + ".png", Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            //pref.storeImg(imagePath);
            Log.d("imagestored", imagePath);


            // prefManager.storeImg(imagePath);
            return true;
        } catch (Exception m) {
            Log.d("imagestored", "" + m.getMessage());

            //controller.toast("Error Storing MyImage",ItemDetails.this,R.drawable.ic_error_outline_black_24dp);
            Toast.makeText(getContext(), "not sac" + m.getMessage(), Toast.LENGTH_SHORT).show();
            m.printStackTrace();
            return false;
        }
    }


}
