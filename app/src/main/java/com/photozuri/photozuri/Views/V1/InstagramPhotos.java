package com.photozuri.photozuri.Views.V1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.photozuri.photozuri.Adapter.ImageAdapter;
import com.photozuri.photozuri.Adapter.PhotoAdapter;
import com.photozuri.photozuri.Data.Models.GDModel;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.NetworkUtills.NetworkUtils;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.DateTimeUtils;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.MyToast;
import com.photozuri.photozuri.Views.V1.Usables.Constants;
import com.photozuri.photozuri.Views.V1.Usables.GridPopup;
import com.photozuri.photozuri.Views.V1.Usables.InstagramApp;
import com.photozuri.photozuri.Views.V1.Usables.JSONParser;
import com.photozuri.photozuri.imagepicker.model.Config;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class InstagramPhotos extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG_DATA = "data";
    public static final String TAG_IMAGES = "images";
    public static final String TAG_THUMBNAIL = "thumbnail";
    public static final String TAG_URL = "url";
    public static final String TAG_STANDARD = "standard_resolution";
    private static int WHAT_ERROR = 1;
    FloatingActionButton fab;
    private InstagramApp mApp;
    private Button btnConnect, btnViewInfo, btnGetAllImages, btnFollowers,
            btnFollwing;
    private LinearLayout llAfterLoginView;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private ProgressDialog progressDialog;
    private View view;
    private GridView gvAllImages;
    private HashMap<String, String> userInfo;
    private ArrayList<String> imageThumbList = new ArrayList<String>();
    private Context context;
    private int WHAT_FINALIZE = 0;
    private ProgressDialog pd;
    private LinearLayout linearLayoutEmpty;
    private ArrayList<GDModel> gdModels = new ArrayList<>();
    private ArrayList<MyImage> images = new ArrayList<>();
    private GridView gridView;
    private RecyclerView recyclerView;
    private Config config;
    private ImageAdapter adapter;
    private PhotoAdapter imageAdapter;
    private AppUtils appUtils;
    private NetworkUtils networkUtils;
    private GeneralUtills generalUtills;
    private int total_count = 0;
    private int count_o = 0;
    private Handler handler2 = new Handler(new Handler.Callback() {


        @Override
        public boolean handleMessage(Message msg) {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            if (msg.what == WHAT_FINALIZE) {
                Intent intent = new Intent(InstagramPhotos.this, GridPopup.class);
                intent.putExtra("data", gdModels);
                intent.putExtra("count", total_count);

                startActivityForResult(intent, Constants.GRID_REQUEST);
                //setImageGridAdapter();
            } else {
                //    Toast.makeText(context, "Check your network.",
                //          Toast.LENGTH_LONG).show();
            }
            return false;
        }
    });
    private String url;
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == WHAT_FINALIZE) {
                Toast.makeText(InstagramPhotos.this, "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });

    private void InitUtils() {
        appUtils = new AppUtils(InstagramPhotos.this);
        networkUtils = new NetworkUtils(InstagramPhotos.this);
        // generalUtills=new GeneralUtills(InstagramPhotos.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_photos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InitUtils();
        init();
        fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (total_count < GlobalConsts.MAX_PHOTOS) {
                    if (mApp.hasAccessToken()) {


                        if (networkUtils.isNetworkAvailable()) {
                            getAllMediaImages("https://api.instagram.com/v1/users/"
                                    + userInfoHashmap.get(InstagramApp.TAG_ID)
                                    + "/media/recent/?access_token="
                                    + mApp.getTOken());
                        } else {
                            Snackbar.make(view, "Please Check your connection", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } else {

                    }
                } else {
                    MyToast.toast("You have reached maximum allowed photos", InstagramPhotos.this, R.drawable.ic_done_color_24dp, com.photozuri.photozuri.Utills.Constants.TOAST_LONG);
                }


            }
        });
        if (mApp.hasAccessToken()) {


            if (networkUtils.isNetworkAvailable()) {
                getAllMediaImages("https://api.instagram.com/v1/users/"
                        + userInfoHashmap.get(InstagramApp.TAG_ID)
                        + "/media/recent/?access_token="
                        + mApp.getTOken());
            } else {

            }
        } else {

        }

        Intent intent = getIntent();
        total_count = intent.getIntExtra("count", 0);
        count_o = intent.getIntExtra("count_o", 0);

        ArrayList<MyImage> images = //convertImages(
                (ArrayList<MyImage>) intent.getSerializableExtra("images");
        if (images != null) {
            this.images.clear();
            this.images = images;
        }

        initViews(images);


    }

    void init() {
        mApp = new InstagramApp(InstagramPhotos.this, Constants.INSTAGRAM_CLIENT_ID,
                Constants.INSTAGRAM_CLIENT_SECRET, Constants.INSTAGRAM_CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {

                btnConnect.setText("Disconnect");
                btnConnect.setVisibility(View.GONE);

                mApp.fetchUserName(handler);
            }

            @Override
            public void onFail(String error) {
                //  Toast.makeText(InstagramPhotos.this, error, Toast.LENGTH_SHORT)
                //         .show();
                MyToast.toast(error, InstagramPhotos.this, R.drawable.ic_error_outline_black_24dp, Toast.LENGTH_LONG);
            }
        });
        setWidgetReference();
        bindEventHandlers();

        if (mApp.hasAccessToken()) {
            btnConnect.setText("Disconnect");
            mApp.fetchUserName(handler);
            btnConnect.setVisibility(View.GONE);

        } else {
            btnConnect.setVisibility(View.VISIBLE);
        }
    }

    private void bindEventHandlers() {
        btnConnect.setOnClickListener(this);

    }

    private void setWidgetReference() {
        btnConnect = findViewById(R.id.login_button);

    }

    @Override
    public void onClick(View v) {
        if (v == btnConnect) {

            if (networkUtils.isNetworkAvailable()) {
                connectOrDisconnectUser();
            } else {
                Snackbar.make(fab, "Please Check your connection", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    private void connectOrDisconnectUser() {
        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    InstagramPhotos.this);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mApp.resetAccessToken();
                                    // btnConnect.setVisibility(View.VISIBLE);
                                    //llAfterLoginView.setVisibility(View.GONE);
                                    btnConnect.setText("Connect");
                                    // tvSummary.setText("Not connected");
                                }
                            })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mApp.authorize();
        }
    }

    private void getAllMediaImages(String url) {
        //  pd = ProgressDialog.show(context, "", "Loading images...");
        progressDialog = new ProgressDialog(InstagramPhotos.this);
        progressDialog.setMessage("Fetching photos....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        //progressDialog.setMax(gdModels1.size());
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(() -> {
            int what = WHAT_FINALIZE;
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = jsonParser

                        .getJSONFromUrlByGet(url
                        );
                JSONArray data = jsonObject.getJSONArray(TAG_DATA);
                // ArrayList<GDModel> gdModels=new ArrayList<>();
                for (int data_i = 0; data_i < data.length(); data_i++) {
                    JSONObject data_obj = data.getJSONObject(data_i);

                    JSONObject images_obj = data_obj
                            .getJSONObject(TAG_IMAGES);

                    JSONObject thumbnail_obj = images_obj
                            .getJSONObject(TAG_THUMBNAIL);


                    String str_url = thumbnail_obj.getString(TAG_URL);

                    JSONObject standard_obj = images_obj
                            .getJSONObject(TAG_STANDARD);


                    String standardr_url = standard_obj.getString(TAG_URL);
                    imageThumbList.add(str_url);
                    GDModel gdModel = new GDModel();

                    gdModel.setTitle(DateTimeUtils.getNow() + "" + data_i);
                    gdModel.setFullImageLink(standardr_url);
                    gdModel.setThumbnailLink(str_url);


                    Log.d("standardurl", standardr_url);
                    Log.d("thumbnailurl", str_url);

                    gdModels.add(gdModel);


                }

                System.out.println("jsonObject::" + jsonObject);

            } catch (Exception exception) {
                try {
                    progressDialog.dismiss();
                } catch (Exception nm) {

                }
                exception.printStackTrace();
                what = WHAT_ERROR;
            }

            handler2.sendEmptyMessage(what);
        }).start();
    }

    @Override
    public void onBackPressed() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.GRID_REQUEST:
                if (resultCode == RESULT_OK && data != null) {

                    try {
                        ArrayList<GDModel> model = (ArrayList<GDModel>) data.getSerializableExtra("images");


                        ArrayList<MyImage> images = new ArrayList<>();
                        if (model != null && model.size() > 0) {
                            int a = 0;
                            for (GDModel gdModel : model) {
                                MyImage image = new MyImage();
                                image.setId(a);
                                image.setName(gdModel.getTitle());
                                image.setPath(gdModel.getFullImageLink());
                                image.setImageFrom(com.photozuri.photozuri.Utills.Constants.INSTAGRAM);
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


                //callbackManager.onActivityResult(requestCode, resultCode, data);
        }


    }

    private void update(ArrayList<MyImage> imagess) {


        if (images != null && images.size() > 0) {


            ArrayList<MyImage> imageds = appUtils.updateList(images, imagess);
            images.clear();
            images.addAll(imageds);

            Log.d("returnedd", "from apputills update  " + String.valueOf(imageds.size()));
            initViews(imageds);

            //imageAdapter.setData(images);
            //viewsPlay(imageAdapter.getCount() > 0);

        } else {
            images = imagess;
            initViews(imagess);
            //imageAdapter.setData(images);
            //viewsPlay(imageAdapter.getCount() > 0);
        }
    }

    private void viewsPlay(boolean gridhasImages) {
        if (gridhasImages) {
            gridView.setVisibility(View.VISIBLE);
            linearLayoutEmpty.setVisibility(View.GONE);
            //  floatingActionButton.setVisibility(View.VISIBLE);

        } else {
            gridView.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.VISIBLE);
//            floatingActionButton.setVisibility(View.GONE);

        }
    }


    private void initViews(ArrayList<MyImage> images) {

        gridView = findViewById(R.id.gridview);
        linearLayoutEmpty = findViewById(R.id.layout_empty);

        imageAdapter = new PhotoAdapter(InstagramPhotos.this, images);
        gridView.setAdapter(imageAdapter);

        viewsPlay(imageAdapter.getCount() > 0);
        total_count = count_o + images.size();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });
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
