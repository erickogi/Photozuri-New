package com.photozuri.photozuri.Views.V1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.photozuri.photozuri.Adapter.ImageAdapter;
import com.photozuri.photozuri.Adapter.PhotoAdapter;
import com.photozuri.photozuri.Data.Models.GDModel;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.NetworkUtills.NetworkUtils;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.MyToast;
import com.photozuri.photozuri.Views.V1.Usables.Constants;
import com.photozuri.photozuri.Views.V1.Usables.GridPopup;
import com.photozuri.photozuri.imagepicker.model.Config;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;



public class FacebookPhotos extends AppCompatActivity {
    public static final String EXTRA_CONFIG = "Config";
    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private static String USER_PHOTOS = "user_photos";
    private CallbackManager mCallbackManager;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private ArrayList<GDModel> gdModels = new ArrayList<>();
    private ArrayList<MyImage> images = new ArrayList<>();
    private GridView gridView;
    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;
    private Config config;
    private ImageAdapter adapter;
    private PhotoAdapter imageAdapter;
    private Button choose;
    private FloatingActionButton floatingActionButton;
    private NetworkUtils networkUtils;
    private AppUtils appUtils;

    private int total_count = 0;
    private int count_o = 0;
    private LinearLayout linearLayoutEmpty;


    //Facebook login button
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();


            // nextActivity(profile);
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException e) {
        }
    };

    private void viewsPlay(boolean gridhasImages) {
        if (gridhasImages) {
            gridView.setVisibility(View.VISIBLE);
            //  floatingActionButton.setVisibility(View.VISIBLE);
            linearLayoutEmpty.setVisibility(View.GONE);

        } else {
            linearLayoutEmpty.setVisibility(View.VISIBLE);

            gridView.setVisibility(View.GONE);
//            floatingActionButton.setVisibility(View.GONE);

        }
    }

    void initViews(ArrayList<MyImage> images) {
        gridView = findViewById(R.id.gridview);
        linearLayoutEmpty = findViewById(R.id.layout_empty);

        for (MyImage image : images) {
            Log.d("seepath", image.getPath());
        }
        imageAdapter = new PhotoAdapter(FacebookPhotos.this, images);
        gridView.setAdapter(imageAdapter);
        total_count = count_o + images.size();

        viewsPlay(imageAdapter.getCount() > 0);


        gridView.setOnItemClickListener((parent, view, position, id) -> {


        });


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });
    }

    void update(ArrayList<MyImage> imagess) {

        if (images != null && images.size() > 0) {


            ArrayList<MyImage> imageds = appUtils.updateList(images, imagess);
            images.clear();
            images.addAll(imageds);

            Log.d("returnedd", "from apputills update  " + String.valueOf(imageds.size()));
            initViews(imageds);



        } else {
            images = imagess;
            initViews(imagess);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login

        switch (requestCode) {
            case Constants.GRID_REQUEST:
                if (resultCode == RESULT_OK && data != null) {

                    try {
                        ArrayList<GDModel> model = (ArrayList<GDModel>) data.getSerializableExtra("images");

                        Log.d("returnedd", "from gridpopup " + model.size());
                        ArrayList<MyImage> images = new ArrayList<>();
                        if (model != null && model.size() > 0) {
                            int a = 0;
                            for (GDModel gdModel : model) {
                                // a, gdModel.getTitle(), gdModel.getFullImageLink()
                                MyImage image = new MyImage();
                                image.setId(a);
                                image.setName(gdModel.getTitle());
                                image.setPath(gdModel.getFullImageLink());
                                image.setImageFrom(com.photozuri.photozuri.Utills.Constants.FACEBOOK);
                                image.setDownloadedComplete(gdModel.isDownloadedComplete());
                                image.setDownloadedInComplete(gdModel.isDownloadedInComplete());

                                a++;

                                images.add(image);
                            }
                        }
                        update(images);


                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }

                }
                break;

            default:


                callbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    private void getFacebookImages() {
        progressDialog = new ProgressDialog(FacebookPhotos.this);
        progressDialog.setMessage("Fetching  photos....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        //progressDialog.setMax(gdModels1.size());
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.d("perrmissions", AccessToken.getCurrentAccessToken().getPermissions().toString());

        final String[] afterString = {""};  // will contain the next page cursor
        final Boolean[] noData = {false};
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//        do {
//            //Bundle params = new Bundle();
//            params.putString("after", afterString[0]);
//
//            Log.d("urrl", "Accesstoken  " + AccessToken.getCurrentAccessToken() + "  USerID  " + AccessToken.getCurrentAccessToken().getUserId() + " ");
//            new GraphRequest(
//                    AccessToken.getCurrentAccessToken(),
//                    "/" + AccessToken.getCurrentAccessToken().getUserId() + "",
//                    params,
//                    HttpMethod.GET,
//                    response -> {
//
//                        // response.getRawResponse();
//                        //Toast.makeText(getContext(),response.getRawResponse(),Toast.LENGTH_LONG).show();
//
//
//                        if (response != null) {
//                            String string = response.toString();
//                            Log.d("imagess", string);
//                            JSONObject jsonObject = null;
//                            try {
//                                jsonObject = response.getJSONObject();
//
////                                JSONArray jsonArray = jsonObject.getJSONArray("data");
////                                // Log.d("dataIn","  "+jsonArray.toString());
////                                // for (int a = 0; a < jsonArray.length(); a++) {
////
////
////                                if (jsonArray != null && jsonArray.length() > 0){
////                                    JSONObject jsonObject1 = jsonArray.getJSONObject(1);
////                                Log.d("dataIn", "       \n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n " + jsonObject1);
//
//                                JSONObject jsonObject2 = jsonObject.getJSONObject("photos");
//
//                                JSONArray jsonArray1 = jsonObject2.getJSONArray("data");
//                                for (int b = 0; b < jsonArray1.length(); b++) {
//
//                                    JSONObject jsonObject3 = jsonArray1.getJSONObject(b);
//                                    String path = jsonObject3.getString("picture");
//                                    String id = jsonObject3.getString("id");
//                                    String date = jsonObject3.getString("created_time");
//
//
//                                    JSONArray jsImages = jsonObject3.getJSONArray("images");
//                                    JSONObject jsonfullpath = jsImages.getJSONObject(1);
//                                    String fullpath = jsonfullpath.getString("source");
//
//                                    GDModel gdModel = new GDModel();
//                                    gdModel.setFullImageLink(fullpath);
//                                    gdModel.setThumbnailLink(path);
//                                    gdModel.setTitle(id);
//                                    gdModel.setCreatonTime(date);
//                                    Log.d("Dated : ", "" + gdModel.getCreatonTime());
//
//                                    gdModels.add(gdModel);
//
//
//                                }
//                                if (!jsonObject.isNull("paging")) {
//                                    JSONObject paging = jsonObject.getJSONObject("paging");
//                                    JSONObject cursors = paging.getJSONObject("cursors");
//                                    if (!cursors.isNull("after"))
//                                        afterString[0] = cursors.getString("after");
//                                    else
//                                        noData[0] = true;
//                                } else
//                                    noData[0] = true;
//
//                                // }
//
//
////                            }else {
////                                    if(progressDialog!=null&&progressDialog.isShowing()){
////                                        progressDialog.dismiss();
////                                    }
////                                    MyToast.toast("No images found ",FacebookPhotos.this,R.drawable.ic_error_outline_black_24dp, com.photozuri.photozuri.Utills.Constants.TOAST_LONG);
////                                }
//                            } catch (JSONException e) {
//                                Log.d("imagessee", e.toString());
//                                e.printStackTrace();
//                                if (progressDialog != null && progressDialog.isShowing()) {
//                                    progressDialog.dismiss();
//                                }
//                            } catch (NullPointerException nm) {
//                                nm.printStackTrace();
//                            }
//
//
//                        } else {
//                            if (progressDialog != null && progressDialog.isShowing()) {
//                                progressDialog.dismiss();
//                            }
//                        }
//
//
//    /* handle the result */
//     /* You can parse this response using Json  */
//                    }
//            ).executeAndWait();
//        }while(!noData[0]);
//
//        if(gdModels!=null&&gdModels.size()>0){
//            initPicker();
//        }else {
//            if (progressDialog != null && progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//            MyToast.toast("No images found ",FacebookPhotos.this,R.drawable.ic_error_outline_black_24dp, com.photozuri.photozuri.Utills.Constants.TOAST_LONG);
////
//        }
////        } else {
////            progressDialog.dismiss();
////            Snackbar.make(view, "Please Check your connection", Snackbar.LENGTH_LONG)
////                    .setAction("Action", null).show();
////        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                final GraphRequest.Callback graphCallback = new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {

                        if (response != null) {
                            String string = response.toString();
                            Log.d("imagess", string);
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = response.getJSONObject();

//                                JSONArray jsonArray = jsonObject.getJSONArray("data");
//                                // Log.d("dataIn","  "+jsonArray.toString());
//                                // for (int a = 0; a < jsonArray.length(); a++) {
//
//
//                                if (jsonArray != null && jsonArray.length() > 0){
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(1);
//                                Log.d("dataIn", "       \n >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n " + jsonObject1);

                                //JSONObject jsonObject2 = jsonObject.getJSONObject("photos");

                                JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                                for (int b = 0; b < jsonArray1.length(); b++) {

                                    JSONObject jsonObject3 = jsonArray1.getJSONObject(b);
                                    String path = jsonObject3.getString("picture");
                                    String id = jsonObject3.getString("id");
                                    String date = jsonObject3.getString("created_time");


                                    JSONArray jsImages = jsonObject3.getJSONArray("images");
                                    JSONObject jsonfullpath = jsImages.getJSONObject(1);
                                    String fullpath = jsonfullpath.getString("source");

                                    GDModel gdModel = new GDModel();
                                    gdModel.setFullImageLink(fullpath);
                                    gdModel.setThumbnailLink(path);
                                    gdModel.setTitle(id);
                                    gdModel.setCreatonTime(date);
                                    Log.d("Dated : ", "" + gdModel.getCreatonTime());

                                    gdModels.add(gdModel);


                                }

                            } catch (JSONException e) {
                                Log.d("imagessee", e.toString());
                                e.printStackTrace();
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } catch (NullPointerException nm) {
                                nm.printStackTrace();
                            }

                            GraphRequest nextRequest = null;
                            nextRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                            if (nextRequest != null) {
                                nextRequest.setCallback(this);
                                nextRequest.executeAndWait();
                            } else {

                            }
                        } else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                        //get next batch of results of exists

                    }
                };
//.type=uploaded

                Bundle params = new Bundle();
                //     params.putString("fields","photos");
                params.putString("fields", "photos{picture,images,created_time},type=uploaded");
                params.putString("type", "uploaded");


                new GraphRequest(AccessToken.getCurrentAccessToken(),
                        "/" + AccessToken.getCurrentAccessToken().getUserId() + "/photos?fields=created_time,picture,images&type=uploaded",
                        null,
                        HttpMethod.GET, graphCallback).executeAndWait();

                Log.d("urrl", "Accesstoken  " + AccessToken.getCurrentAccessToken() + "  USerID  " + AccessToken.getCurrentAccessToken().getUserId() + " ");
//


                if (gdModels != null && gdModels.size() > 0) {
                    runOnUiThread(FacebookPhotos.this::initPicker);
                }
            }


        }).start();






























    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_photos);
        FacebookSdk.sdkInitialize(FacebookPhotos.this);
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        networkUtils = new NetworkUtils(FacebookPhotos.this);
        appUtils = new AppUtils(FacebookPhotos.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (networkUtils.isNetworkAvailable()) {


                    if (total_count < GlobalConsts.MAX_PHOTOS) {
                        getFacebookImages();
                    } else {
                        MyToast.toast("You have reached maximum allowed photos", FacebookPhotos.this, R.drawable.ic_done_color_24dp, com.photozuri.photozuri.Utills.Constants.TOAST_LONG);
                    }

                } else {
                    //  progressDialog.dismiss();
                    Snackbar.make(view, "Please Check your connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        Intent intent = getIntent();
        count_o = intent.getIntExtra("count_o", 0);

        total_count = intent.getIntExtra("count", 0);

        ArrayList<MyImage> images = //convertImages(
                (ArrayList<MyImage>) intent.getSerializableExtra("images");
        if (images != null) {
            this.images.clear();
            this.images = images;
        }

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };
        if (AccessToken.getCurrentAccessToken() != null) {
//            loginButton.setVisibility(View.GONE);
        }


        accessTokenTracker.startTracking();
//        profileTracker.startTracking();


        LoginButton mLoginButton = findViewById(R.id.login_button);

        if (AccessToken.getCurrentAccessToken() != null) {
            mLoginButton.setVisibility(View.GONE);
            fab.show();
            if (networkUtils.isNetworkAvailable()) {

                if (images != null && images.size() < 0) {
                    getFacebookImages();
                }
            } else {
                //  progressDialog.dismiss();
                Snackbar.make(fab.getRootView(), "Please Check your connection", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } else {
            fab.hide();
        }
        // Set the initial permissions to request from the user while logging in
        mLoginButton.setReadPermissions(Arrays.asList(EMAIL, USER_POSTS, USER_PHOTOS));

        // Register a callback to respond to the user
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setResult(RESULT_OK);
                PrefManager prefManager = new PrefManager(FacebookPhotos.this);
//                Toast.makeText(FacebookPhotos.this, loginResult.getAccessToken().getUserId() + "  " + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                mLoginButton.setVisibility(View.GONE);

                fab.setVisibility(View.VISIBLE);
                //finish();
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                mLoginButton.setVisibility(View.GONE);
                //finish();
            }

            @Override
            public void onError(FacebookException e) {
                // Handle exception
            }
        });

        initViews(images);

        if (networkUtils.isNetworkAvailable()) {

        } else {
            Snackbar.make(fab, "Please Check your connection", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

    }

    private void initPicker() {



        runOnUiThread(() -> {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (gdModels != null) {
                if (gdModels.size() > 0) {


                    Intent intent = new Intent(FacebookPhotos.this, GridPopup.class);
                    intent.putExtra("data", gdModels);
                    intent.putExtra("count", total_count);
                    startActivityForResult(intent, Constants.GRID_REQUEST);
                } else {
                    Log.d("iNitpicker", "GDMODEL<1");
                }

            } else {
                Log.d("iNitpicker", "No images GDMODELS NULL");
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (images == null) {
            Log.d("returnedd", "from null images");
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("images", images);
        Log.d("returnedd", "no  " + String.valueOf(images.size()));
        //returnIntent.putExtra("images", this.images);
        setResult(RESULT_OK, returnIntent);
        //super.onBackPressed();
        finish();
    }

    public void back(View view) {
        if (images == null) {
            Log.d("returnedd", "from null images");
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("images", images);
        //returnIntent.putExtra("images", this.images);
        setResult(RESULT_OK, returnIntent);
        //super.onBackPressed();
        finish();
    }
}