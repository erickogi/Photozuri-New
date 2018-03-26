package com.photozuri.photozuri.Views.Login;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.error.VolleyError;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.Data.UserModel;
import com.photozuri.photozuri.Data.UserModelParse;
import com.photozuri.photozuri.NetworkUtills.ApiConstants;
import com.photozuri.photozuri.NetworkUtills.DumbVolleyRequest;
import com.photozuri.photozuri.NetworkUtills.NetworkUtils;
import com.photozuri.photozuri.Payments.Payments;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.UtilListeners.RequestListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import static com.photozuri.photozuri.Utills.GeneralUtills.isFilledTextInputEditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String EMAIL = "email";
    public static Fragment fragment = null;
    private CallbackManager callbackManager;
    private Button btnRegister;
    private LoginButton loginButton;
    private PrefManager prefrenceManager;
    private DumbVolleyRequest dumbVolleyRequest;
    private TextInputEditText mFirstName, mLastName, mEmail, mMobile, mPassword;
    private TextInputEditText mEditTextPassword, mEditTextUserId;
    private Button btnLogin;
    private String goooglePhoto = null;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog progressDialog;
    private MaterialDialog dialog;
    private boolean goToPay = false;

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

    private boolean isFilled(TextInputEditText textInputEditText) {
        if (textInputEditText.getText().toString().equals("")) {
            textInputEditText.setError("Required");
            return false;
        } else {
            return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_fragment);

        Intent intent = getIntent();
        String Action = intent.getStringExtra("Action");
        goToPay = Action != null && Action.equals(Constants.LOGIN_PAY);

        callbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));


        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //   loginResult.getAccessToken().g

                Profile profile = Profile.getCurrentProfile();
                profile.getFirstName();
                profile.getLastName();


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        fragment = new LoginFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, "fragmentWelcome").commit();

        SignInButton signInButton = findViewById(R.id.sign_in_button);


        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        SignInButton loginInButton = findViewById(R.id.login_in_button);
        loginInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.login_in_button).setOnClickListener(this);


        prefrenceManager = new PrefManager(LoginActivity.this);

        String id = prefrenceManager.getFbId();
        Log.d("idd", id);
        dumbVolleyRequest = new DumbVolleyRequest();
        mEditTextPassword = findViewById(R.id.edt_passwordl);
        mEditTextUserId = findViewById(R.id.edt_user_idl);

        try {

            mEditTextPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_24dp, 0, 0, 0);
            mEditTextUserId.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp, 0, 0, 0);


        } catch (Exception hj) {

            hj.printStackTrace();
        }
        btnLogin = findViewById(R.id.btn_login);
        //  key = getArguments().getInt("key");

        btnLogin.setOnClickListener(v -> {

            if (isFilledTextInputEditText(mEditTextPassword) && isFilled(mEditTextUserId)) {
                if (new NetworkUtils(LoginActivity.this).isNetworkAvailable()) {
                    login1(1);
                } else {
                    alertDialogDelete("Couldn't connect Please check your internet connection");
                }
            }
        });


        mFirstName = findViewById(R.id.txt_firstnames);
        mLastName = findViewById(R.id.txt_lastnames);
        mEmail = findViewById(R.id.txt_emailAdresss);
        mMobile = findViewById(R.id.txt_mobiles);
        mPassword = findViewById(R.id.txt_passwords);

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

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(view -> {
            if (isFilledTextInputEditText(mFirstName) && isFilledTextInputEditText(mLastName) && isFilledTextInputEditText(mEmail) && isFilledTextInputEditText(mMobile) && isFilledTextInputEditText(mPassword)) {

                if (validateFields()) {

                    progressDialog = new ProgressDialog(LoginActivity.this);
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
                    if (new NetworkUtils(LoginActivity.this).isNetworkAvailable()) {
                        register(name, email, mobile, mPassword.getText().toString());
                    } else {
                        alertDialogDelete("Couldn't connect Please check your internet connection");
                    }


                }
            } else {

            }
        });


        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(14);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText("Sign Up");
                tv.setTextColor(Color.parseColor("#212121"));

                tv.setSingleLine(true);
                //tv.setPadding(15, 15, 15, 15);

                return;
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

    private void login1(int a) {

        NetworkUtils networkUtils = new NetworkUtils(LoginActivity.this);

        if (networkUtils.isNetworkAvailable()) {

            if (isFilled(mEditTextPassword) && isFilledTextInputEditText(mEditTextUserId)) {

                MaterialDialog.Builder builder = new MaterialDialog.Builder(LoginActivity.this)
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
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
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

                                if (goooglePhoto != null) {
                                    userModel.setUriPhoto(Uri.parse(goooglePhoto));
                                }
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                prefrenceManager.setUserData(userModel);
                                prefrenceManager.setIsLoggedIn(true);
                                if (goToPay) {
                                    startActivity(new Intent(LoginActivity.this, Payments.class));
                                }
                                finish();

                            } else {

                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                alertDialogDelete(jsonObject.getString("message"));
                            }
                        } catch (JSONException j) {

                            Log.d("logingerr", j.toString() + "  " + response);
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }


                    }
                });


            }

        } else {
            alertDialogDelete("Check your Internet Connection");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);

    }

    //    @Override
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//    }
    void setUpView() {
        if (fragment != null) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment)
                    .addToBackStack(null).commit();
        }

    }

    void popOutFragments() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    public void newAccountBtnPressed(View view) {
//        popOutFragments();
//        fragment = new SignUpFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, "fragmentsignup").addToBackStack(null).commit();

        changeView(1);
       // signUpFragment = (SignUpFragment) getSupportFragmentManager().findFragmentByTag("fragmentsignup");


    }

    public void loginBtnPressed(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging you in....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();


    }

    public void newloginBtnPressed(View view) {
        //  popOutFragments();
        //fragment = new LoginFragment();
//        setUpView();

        changeView(2);


//
    }

    private void changeView(int i) {

        RelativeLayout relativeLayoutr = findViewById(R.id.register_view);
        RelativeLayout relativeLayoutl = findViewById(R.id.login_view);


        if (i == 1) {

            relativeLayoutr.setVisibility(View.VISIBLE);
            relativeLayoutl.setVisibility(View.GONE);
        } else {
            relativeLayoutr.setVisibility(View.GONE);
            relativeLayoutl.setVisibility(View.VISIBLE);
        }

    }

    public void addNew(View view) {


    }

    @Override
    public void onClick(View view) {
        if (new NetworkUtils(LoginActivity.this).isNetworkAvailable()) {
            if (view.getId() == R.id.login_in_button) {
                signIn(2002);
            } else {
                signIn(2001);
            }
        } else {
            alertDialogDelete("Couldn't connect Please check your internet connection");
        }

    }

    private void signIn(int a) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, a);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 2001) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResultL(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            signIn(account);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void handleSignInResultL(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            signInL(account);

            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void signIn(GoogleSignInAccount account) {
        if (account != null) {
            String email = account.getEmail();
            String names = account.getDisplayName();
            Uri d = account.getPhotoUrl();

//account.get
            PrefManager prefManager = new PrefManager(LoginActivity.this);
            prefManager.setIsLoggedIn(true);
            UserModel userModel = new UserModel();

            //final String[] photo = {""};
            if (d != null) {
                goooglePhoto = d.toString();
            }

//            Picasso.with(LoginActivity.this).load(d).into(new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    GeneralUtills generalUtills = new GeneralUtills(LoginActivity.this);
//                    photo[0] = generalUtills.saveProfilePic(bitmap, names);
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable errorDrawable) {
//
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                }
//            });

//            if (photo[0] != null) {
//                goooglePhoto = photo[0];
//            }
            //GeneralUtills generalUtills=new GeneralUtills(LoginActivity.this);
            //generalUtills.saveProfilePic()


            register(names, email, email, email);

            // userModel.setUserName(names);
            // userModel.setFirstName(account.getDisplayName());
            // userModel.setEmail(email);
            // userModel.setUserName(account.getDisplayName());
            // userModel.setPhoneNumber("");
            //userModel.setUserId(account.getId());


        }
    }

    private void signInL(GoogleSignInAccount account) {
        if (account != null) {
            String email = account.getEmail();
            String names = account.getDisplayName();
            Uri d = account.getPhotoUrl();


            PrefManager prefManager = new PrefManager(LoginActivity.this);
            prefManager.setIsLoggedIn(true);
            UserModel userModel = new UserModel();

            final String[] photo = {""};
            Picasso.with(LoginActivity.this).load(d).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    GeneralUtills generalUtills = new GeneralUtills(LoginActivity.this);
                    photo[0] = generalUtills.saveProfilePic(bitmap, names);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });

            if (photo[0] != null) {
                goooglePhoto = photo[0];
            }
            //GeneralUtills generalUtills=new GeneralUtills(LoginActivity.this);
            //generalUtills.saveProfilePic()


            login(email, email);

            // userModel.setUserName(names);
            // userModel.setFirstName(account.getDisplayName());
            // userModel.setEmail(email);
            // userModel.setUserName(account.getDisplayName());
            // userModel.setPhoneNumber("");
            //userModel.setUserId(account.getId());


        }
    }

    private void register(String name, String email, String mobile, String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("mobile", mobile);
        params.put("password", password);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Registering you in....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        DumbVolleyRequest.getPostData(ApiConstants.register_endpoint, params, new RequestListener() {
            @Override
            public void onError(VolleyError error) {

                Log.d("regerr", error.toString());
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
                        // popOutFragments();
                        // setUpView();
                        login(email, email);

                        //alertDialogDelete("succesfull");
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


        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setMessage(message).setPositiveButton("Okay", dialogClickListener)
                //.setNegativeButton("No", dialogClickListener)
                .show();

    }

    private void login(String email, String password) {
        NetworkUtils networkUtils = new NetworkUtils(LoginActivity.this);

        if (networkUtils.isNetworkAvailable()) {


            MaterialDialog.Builder builder = new MaterialDialog.Builder(LoginActivity.this)
                    .title("Verifying you")
                    .content("Please Wait")
                    .cancelable(false)
                    .progress(true, 0);


            dialog = builder.build();
            dialog.show();

            PrefManager prefManager = new PrefManager(LoginActivity.this);

            String url = ApiConstants.login_endpoint;
            HashMap<String, String> params = new HashMap<>();
            params.put("mobile", email);
            params.put("password", password);
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


                            if (goooglePhoto != null) {
                                // Uri uri;
                                // String stringUri;
                                //uri = Uri.parse(stringUri);
                                userModel.setUriPhoto(Uri.parse(goooglePhoto));
                            }

                            dialog.dismiss();
                            prefManager.setUserData(userModel);
                            prefManager.setIsLoggedIn(true);
                            if (goToPay) {
                                startActivity(new Intent(LoginActivity.this, Payments.class));
                            }
                            finish();

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


        } else {
            alertDialogDelete("Check your Internet Connection");
        }
    }

    private void register1(String name, String email, String mobile, String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("mobile", mobile);
        params.put("password", password);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Registering you in....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        DumbVolleyRequest.getPostData(ApiConstants.register_endpoint, params, new RequestListener() {
            @Override
            public void onError(VolleyError error) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
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

                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        login(email, password);

                        // alertDialogDelete("succesfull");
                    } else {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        alertDialogDelete(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                    Log.d("signupexc", e.toString());
                }

            }
        });

    }


}
