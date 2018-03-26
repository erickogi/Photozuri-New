package com.photozuri.photozuri.Views.V1.Usables;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.photozuri.photozuri.Data.Models.GDModel;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.NetworkUtills.NetworkUtils;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.MyToast;
import com.photozuri.photozuri.Utills.RecyclerTouchListener;
import com.photozuri.photozuri.Utills.UtilListeners.AppOnclickRecyclerListener;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;

public class GridPopup extends AppCompatActivity {
    private final int KEY_DOWNLOAD_PICASSO = 303;
    private final int KEY_DOWNLOAD_PICASSO_GET = 304;
    ProgressDialog progressDialog;
    ArrayList<GDModel> gdModels1 = new ArrayList<>();
    int from;
    //i//nt a = 0;
    private String googleAuthToken;
    private String googleAccountName;
    /** Instance of the Google Play controller */
    private GridPopUpAdapter imageAdapterG;
    private GDController GDController;
    private GridView albumGrid;
    private Button buttonOkay;

    private AppUtils appUtils;
    private NetworkUtils networkUtils;
    private GeneralUtills generalUtills;
    private GridAdapter gridAdapter;
    private int total_count = 0;


    private RecyclerView recyclerView;
    //private SellListAdapter sellListAdapter;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;

    private void InitUtils() {
        appUtils = new AppUtils(GridPopup.this);
        // networkUtils= new NetworkUtils(FacebookPhotos.this);
        generalUtills = new GeneralUtills(GridPopup.this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images_select_view);
        InitUtils();
        SharedPreferences settings = GridPopup.this.getPreferences(Context.MODE_PRIVATE);




        Intent intent=getIntent();

        total_count = intent.getIntExtra("count", 0);
        ArrayList<GDModel> gdModels = appUtils.filterPhotos((ArrayList<GDModel>) intent.getSerializableExtra("data"));

        from = intent.getIntExtra("from", 0);

        buttonOkay = findViewById(R.id.btn_okay);
        recyclerView = findViewById(R.id.recyclerView);
        buttonOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gdModels1 != null && gdModels1.size() > 0) {

                    progressDialog = new ProgressDialog(GridPopup.this);
                    progressDialog.setMessage("Downloading selected photos....");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setIndeterminate(false);
                    progressDialog.setMax(gdModels1.size());
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                    for (GDModel gdModel : gdModels1) {
                        Log.d("drawing", " before filter" + gdModel.getTitle());

                    }
//                    if (from == Constants.GOOGLE) {
//                        new getAuth(gdModels1, KEY_DOWNLOAD_PICASSO_GET).execute();
//                    } else {
                        new loadImg(gdModels1).execute();
                    //  }

                }



            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(GridPopup.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (total_count < 50) {
                    GDModel gdModel = gdModels.get(position);
                    if (gdModels.get(position).isSelected()) {


                        fromSelected(gdModels.get(position));

                        total_count--;


                        //thumbnail.setAlpha(1f);
                        gdModels.get(position).setSelected(false);
                        gdModel.setSelected(false);
                        // gridAdapter.setData(gdModels);
                        gridAdapter.setDataChange(position, gdModel);


                        // imgSelect.setVisibility(View.GONE);
                    } else {
                        gdModels.get(position).setSelected(true);


                        total_count++;
                        toSelected(gdModels.get(position));
                        gdModel.setSelected(true);
                        gridAdapter.setDataChange(position, gdModel);

                        // gridAdapter.setData(gdModels);

                        //thumbnail.setAlpha(0.4f);

                        // imgSelect.setVisibility(View.VISIBLE);
                    }
                } else {
                    MyToast.toast("You have reached maximum allowed photos", GridPopup.this, R.drawable.ic_done_color_24dp, com.photozuri.photozuri.Utills.Constants.TOAST_LONG);

                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        gridAdapter = new GridAdapter(GridPopup.this, gdModels, new AppOnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {

            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onClickListener(int position, ImageView thumbnail, ImageView imgSelect) {
                //ImageView imgSelect = view.findViewById(R.id.image_select);
                // ImageView thumbnail = view.findViewById(R.id.image_thumbnail);
//
//                GDModel gdModel=gdModels.get(position);
//            if (gdModels.get(position).isSelected()) {
//
//
//                fromSelected(gdModels.get(position));
//
//
//               //thumbnail.setAlpha(1f);
//                gdModels.get(position).setSelected(false);
//                gdModel.setSelected(false);
//                gridAdapter.setDataChange(position,gdModel);
//
//
//               // imgSelect.setVisibility(View.GONE);
//            } else {
//                gdModels.get(position).setSelected(true);
//
//
//                toSelected(gdModels.get(position));
//                gdModel.setSelected(true);
//                gridAdapter.setDataChange(position,gdModel);

                //gridAdapter.setData(gdModels);

                //thumbnail.setAlpha(0.4f);

                // imgSelect.setVisibility(View.VISIBLE);
                //  }
            }
        });
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(gridAdapter);


//        albumGrid=findViewById(R.id.gridview);
//        imageAdapterG = new GridPopUpAdapter(GridPopup.this, gdModels);
//        albumGrid.setAdapter(imageAdapterG);
//
//        albumGrid.setOnItemClickListener((parent, view, position, id) -> {
//            ///  sendBackResult(gdModels.get(position));
//
//
//            ImageView imgSelect = view.findViewById(R.id.image_select);
//            ImageView thumbnail = view.findViewById(R.id.image_thumbnail);
//
//            if (gdModels.get(position).isSelected()) {
//
//
//                fromSelected(gdModels.get(position));
//
//
//                thumbnail.setAlpha(1f);
//                gdModels.get(position).setSelected(false);
//
//
//                imgSelect.setVisibility(View.GONE);
//            } else {
//                gdModels.get(position).setSelected(true);
//
//
//                toSelected(gdModels.get(position));
//
//                thumbnail.setAlpha(0.4f);
//
//                imgSelect.setVisibility(View.VISIBLE);
//            }
//
//
////            Intent returnIntent = new Intent();
////            returnIntent.putExtra("data", gdModels.get(position));
////            setResult(RESULT_OK,returnIntent);
////            finish();
//        });



    }

    public void fromSelected(GDModel gdModel) {
        gdModels1.remove(gdModel);
    }

    public void toSelected(GDModel gdModel) {

        gdModels1 = appUtils.addPhotos(gdModels1, gdModel);


    }


    private void download(ArrayList<GDModel> gdModels) {
        for (GDModel gdModel : gdModels) {
            Log.d("drawing", " after filter" + gdModel.getTitle());

        }
        ArrayList<GDModel> gdModels1 = new ArrayList<>();


        for (GDModel gdModel : gdModels) {
            Picasso.with(GridPopup.this)
                    .load(gdModel.getFullImageLink())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                            gdModel.setFullImageLink(generalUtills.saveProfilePic(bitmap, gdModel.getTitle()));
                            gdModel.setDownloadedComplete(true);
                            gdModel.setDownloadedInComplete(false);


                            gdModels1.add(gdModel);


                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            //gdModels.get(a).setFullImageLink(GeneralUtills.saveProfilePic(bitmap,GridPopup.this,gdModels.get(a).getTitle()));
                            gdModel.setDownloadedComplete(false);
                            gdModel.setDownloadedInComplete(true);

                            gdModels1.add(gdModel);

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {


                        }
                    });


        }

        progressDialog.dismiss();

        for (GDModel gdModel1 : gdModels1) {

            Log.d("drawingghh", gdModel1.getTitle());

        }


        Intent returnIntent = new Intent();
        returnIntent.putExtra("images", gdModels1);
        Log.d("drawingghhjk", String.valueOf(gdModels1.size()));

        setResult(RESULT_OK, returnIntent);
        finish();
    }


    @Override
    public void onBackPressed() {


        super.onBackPressed();


    }

    private String getLink(String fullImageLink, String thumbnailLink) {
        if (from == Constants.GOOGLE) return thumbnailLink;
        else return fullImageLink;
    }

    private class loadImg extends AsyncTask<Void, Integer, ArrayList<GDModel>> {


        private ArrayList<GDModel> gdModels = new ArrayList<>();

        private ArrayList<GDModel> gdModels1 = new ArrayList<>();

        loadImg(ArrayList<GDModel> gdModels) {
            this.gdModels = gdModels;
            Log.d("linkk", "CONSTRUCT-LOADIMG");
        }


        @Override
        protected ArrayList<GDModel> doInBackground(Void... params) {

            try {
                for (int i = 0; i < gdModels.size(); i++) {
                    GDModel gdModel = gdModels.get(i);

                    final String link = getLink(gdModel.getFullImageLink(), gdModel.getThumbnailLink());


                    if (link != null && !link.isEmpty()) {
                        Log.d("linkk", "link is " + link);


                        if (from == Constants.GOOGLE) {
//                            Bitmap bitmap = appUtils.getAuthPicasso(googleAuthToken)
//                                    .load(link).get();
//
//                            Log.d("linkk", "DOWNLOAD WITH PICCASO BEARER");
//
//                            gdModel.setFullImageLink(generalUtills.saveProfilePic(bitmap, gdModel.getTitle()));
//                            gdModel.setDownloadedComplete(true);
//                            gdModel.setDownloadedInComplete(false);
                            Log.d("linkk", "DOWNLOAD WITH PICASSO PICASSO");
                            Bitmap bitmap = Picasso.with(GridPopup.this).load(link).get();
                            gdModel.setFullImageLink(generalUtills.saveProfilePic(bitmap, gdModel.getTitle()));
                            gdModel.setDownloadedComplete(true);
                            gdModel.setDownloadedInComplete(false);


                        } else {

                            Log.d("linkk", "DOWNLOAD WITH PICASSO PICASSO");
                            Bitmap bitmap = Picasso.with(GridPopup.this).load(link).get();
                            gdModel.setFullImageLink(generalUtills.saveProfilePic(bitmap, gdModel.getTitle()));
                            gdModel.setDownloadedComplete(true);
                            gdModel.setDownloadedInComplete(false);

                        }
                        Log.d("linkk", "link afterdownloard " + gdModel.getFullImageLink());


                        gdModels1.add(gdModel);
                        publishProgress(i);
                    } else {
                        Log.d("linkk", "link is empty");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return gdModels1;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //mAdapter.notifyItemChanged(values[0]);
            progressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<GDModel> gdModels) {
            super.onPostExecute(gdModels);
            progressDialog.dismiss();
            for (GDModel gdModel1 : gdModels) {

                Log.d("drawingghh", gdModel1.getTitle());

            }


            Intent returnIntent = new Intent();
            returnIntent.putExtra("images", gdModels);
            Log.d("drawingghhjk", String.valueOf(gdModels.size()));

            setResult(RESULT_OK, returnIntent);
            finish();
        }

    }

    private class getAuth extends AsyncTask<Void, Void, String> {
        private ArrayList<GDModel> gdModels = new ArrayList<>();
        private int which = 0;


        getAuth(ArrayList<GDModel> gdModels, int which) {
            this.gdModels = gdModels;
            this.which = which;

        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.d("linkk", "STARTING GETAUTH BACKGROUND");
//
//            SharedPreferences settings = GridPopup.this.getPreferences(Context.MODE_PRIVATE);
//
//            googleAccountName=settings.getString(com.photozuri.photozuri.Views.V1.Usables.Constants.PREF_ACCOUNT_NAME,"null");

            PrefManager prefManager = new PrefManager(GridPopup.this);

            googleAccountName = prefManager.getGoogleAccountName();
            Log.d("linkk", "GETTING ACCOUNTNAME" + googleAccountName);

            try {
                googleAuthToken = GoogleAuthUtil.getToken(GridPopup.this,
                        googleAccountName, com.photozuri.photozuri.Views.V1.Usables.Constants.GPHOTOS_SCOPE);

                Log.d("linkk", "GETTING AUTHTOKEN " + googleAuthToken);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
            return googleAuthToken;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (which == KEY_DOWNLOAD_PICASSO) {
                Log.d("linkk", "POST EXCUTE WHICH PICASSO");

                download(gdModels);
            } else if (which == KEY_DOWNLOAD_PICASSO_GET) {
                Log.d("linkk", "POST EXCUTE WHICH PICASSO GET");

                new loadImg(gdModels).execute();
            }
            super.onPostExecute(s);

        }
    }


}
