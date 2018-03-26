package com.photozuri.photozuri.Views.Login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.error.VolleyError;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.NetworkUtills.ApiConstants;
import com.photozuri.photozuri.NetworkUtills.DumbVolleyRequest;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.UtilListeners.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

//import com.android.volley.VolleyError;

/**
 * Created by Eric on 9/12/2017.
 */


public class SignUpFragment extends Fragment implements View.OnClickListener {
    public static Fragment fragment = null;
    private DumbVolleyRequest dumbVolleyRequest;
    //public static OTPFragment otpFragment;
    private View view;
    private TextInputEditText mFirstName, mLastName, mEmail, mMobile, mPassword;
    private Button btnRegister;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private PrefManager pref;
    private RelativeLayout relativeLayoutOtp;
    private CardView relativeLayoutSignup;

    private EditText edtCode;
    private Button btnSignup, btnVerify, btnResend;
    private ImageView imagback;


    private Dialog dialoge;
    private int key = 1;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        pref = new PrefManager(getContext());
        dumbVolleyRequest = new DumbVolleyRequest();
        mFirstName = view.findViewById(R.id.txt_firstnames);
        mLastName = view.findViewById(R.id.txt_lastnames);
        mEmail = view.findViewById(R.id.txt_emailAdresss);
        mMobile = view.findViewById(R.id.txt_mobiles);
        mPassword = view.findViewById(R.id.txt_passwords);

        try {

            mFirstName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp, 0, 0, 0);
            mLastName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp, 0, 0, 0);
            mEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email_black_24dp, 0, 0, 0);
            mMobile.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone_black_24dp, 0, 0, 0);
            mPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp, 0, 0, 0);
            // mEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp,0,0,0);


        } catch (Exception hj) {

            hj.printStackTrace();
        }

        btnRegister = view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);



        return view;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register) {

            if (GeneralUtills.isFilledTextInputEditText(mFirstName) && GeneralUtills.isFilledTextInputEditText(mLastName) && GeneralUtills.isFilledTextInputEditText(mEmail) && GeneralUtills.isFilledTextInputEditText(mMobile) && GeneralUtills.isFilledTextInputEditText(mPassword)) {

                if (validateFields()) {

                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Registering you in....");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    String name = mFirstName.getText().toString() + "  " + mLastName.getText().toString();
                    String email = mEmail.getText().toString();
                    String mobile = mMobile.getText().toString();


                    String token = "";

                    //if (key == 1) {
                    register(name, email, mobile, mPassword.getText().toString());
//                    requestForSMSStudent(name
//                            , email, mobile, token);
//                    } else {
//                        requestForSMS(name, email, mobile, token);
//                    }

                }
            } else {

            }


        }
    }

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

//    private void alertDialog(final String message) {
//        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case DialogInterface.BUTTON_POSITIVE:
//                        dialog.dismiss();
//                        relativeLayoutOtp.setVisibility(View.GONE);
//                        relativeLayoutSignup.setVisibility(View.VISIBLE);
//                        break;
//                    case DialogInterface.BUTTON_NEGATIVE:
//                        dialog.dismiss();
//
//                        break;
//                }
//            }
//        };
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//        builder.setMessage(message).setPositiveButton("Okay", dialogClickListener)
//                // .setNegativeButton("No", dialogClickListener)
//                .show();
//
//    }


    /**
     * Method initiates the SMS request on the server
     *
     * @param name   user name
     * @param email  user email address
     * @param mobile user valid mobile number
     */


    private void register(String name, String email, String mobile, String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("mobile", mobile);
        params.put("password", password);

        DumbVolleyRequest.getPostData(ApiConstants.register_endpoint, params, new RequestListener() {
            @Override
            public void onError(VolleyError error) {

                progressDialog.dismiss();
                alertDialogDelete("Error Registering");
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String response) {

                Log.d("respp", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getString("error").equals("true")) {
                        fragment = new LoginFragment();
                        progressDialog.dismiss();
                        popOutFragments();
                        setUpView();

                        alertDialogDelete("succesfull");
                    } else {
                        progressDialog.dismiss();
                        alertDialogDelete(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                    Log.d("signupexc", e.toString());
                }

            }
        });

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



    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }



}
