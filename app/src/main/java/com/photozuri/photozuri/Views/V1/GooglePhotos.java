package com.photozuri.photozuri.Views.V1;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.photozuri.photozuri.Adapter.ImageAdapter;
import com.photozuri.photozuri.Adapter.PhotoAdapter;
import com.photozuri.photozuri.Data.Models.GDModel;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.NetworkUtills.NetworkUtils;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.DateTimeUtils;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.MyToast;
import com.photozuri.photozuri.Views.V1.Usables.Constants;
import com.photozuri.photozuri.Views.V1.Usables.GridPopup;
import com.photozuri.photozuri.imagepicker.model.Config;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.GphotoFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.ServiceForbiddenException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GooglePhotos extends AppCompatActivity {
    private static final Logger LOG = LoggerFactory.getLogger(GooglePhotos.class);
    private static final String API_PREFIX
            = "https://picasaweb.google.com/data/feed/api/user/";
    //ProgressDialog progressDialog ;
    public static int currentPage = 1;
    private final int PICK_ACCOUNT_REQUEST = 1;
    private final int REQUEST_AUTHENTICATE = 2;
    PicasawebService picasaService;
    Button selectAccount;
    AccountManager am;
    Account[] list;
    String selectedAccountName;
    Account selectedAccount;
    ImageView picture;
    private ProgressDialog progressDialog;
    private String googleAuthToken;
    //Facebook login button
    private String googleAccountName;
    private com.photozuri.photozuri.Views.V1.Usables.GDController GDController;
    private ArrayList<GDModel> gdModels = new ArrayList<>();
    private ArrayList<MyImage> images = new ArrayList<>();
    private GridView gridView;
    private RecyclerView recyclerView;
    private Config config;
    private ImageAdapter adapter;
    private PhotoAdapter imageAdapter;
    private Button choose;
    private FloatingActionButton floatingActionButton;
    private PrefManager prefManager;
    private int total_count = 0;
    private int count_o = 0;
    private LinearLayout linearLayoutEmpty;

    /**
     * Tag for Logging messages
     */
    private String TAG = "ActivityGooglePhotosPick";
    /**
     * Instance of the Google Play controller
     */
    //private GDController GDController;
//
    private AppUtils appUtils;
    private NetworkUtils networkUtils;
    private GeneralUtills generalUtills;

    private void InitUtils() {
        appUtils = new AppUtils(GooglePhotos.this);
        networkUtils = new NetworkUtils(GooglePhotos.this);
        generalUtills = new GeneralUtills(GooglePhotos.this);
    }
    private void viewsPlay(boolean gridhasImages) {
        if (gridhasImages) {
            gridView.setVisibility(View.VISIBLE);
            choose.setVisibility(View.GONE);
            linearLayoutEmpty.setVisibility(View.GONE);

        } else {
            linearLayoutEmpty.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.GONE);
            // choose.setVisibility(View.VISIBLE);

        }
    }

    void initViews(ArrayList<MyImage> images) {
        gridView = findViewById(R.id.gridview);
        linearLayoutEmpty = findViewById(R.id.layout_empty);

        imageAdapter = new PhotoAdapter(GooglePhotos.this, images);
        gridView.setAdapter(imageAdapter);
        total_count = count_o + images.size();

        viewsPlay(imageAdapter.getCount() > 0);


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

    void update(ArrayList<MyImage> imagess) {

        if (images != null && images.size() > 0) {


            ArrayList<MyImage> imageds = appUtils.updateList(images, imagess);
            images.clear();
            images.addAll(imageds);


            initViews(imageds);

            //imageAdapter.setData(images);
            //viewsPlay(imageAdapter.getCount() > 0);

        } else {
            images = imagess;
            initViews(images);
            //imageAdapter.setData(images);
            //viewsPlay(imageAdapter.getCount() > 0);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_photos);
        InitUtils();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefManager = new PrefManager(GooglePhotos.this);

        progressDialog = new ProgressDialog(GooglePhotos.this);
        am = (AccountManager) getSystemService(ACCOUNT_SERVICE);

        GDController = com.photozuri.photozuri.Views.V1.Usables.GDController.getInstance();

        choose = findViewById(R.id.login_button);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  fetchAuthToken();
                if (!prefManager.getPiccasaWebUserid().equals("null")) {
//                    List<AlbumEntry> albums = null;
//                    try {
//                        //albums = getAlbums(prefManager.getPiccasaWebUserid());
//                        //LOG.debug("Got {} albums", albums.size());
//
//                        //AlbumEntry album = albums.get(0);
//
//                        //List<PhotoEntry> photos = getPhotos(selectedAccountName, album);
//                       // PhotoEntry photo = photos.get(0);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (ServiceException e) {
//                        e.printStackTrace();
                    // }

                } else {
                    list = am.getAccounts();
                    LOG.debug("Got {} accounts", list.length);
                    for (Account a : list) {
                        LOG.debug("{} {}", a.name, a.type);
                    }

                    Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                            false, null, null, null, null);
                    startActivityForResult(intent, PICK_ACCOUNT_REQUEST);
                }
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // chooseAccount();
                //fetchAndShowImages();
                //fetchAuthToken();

                if (total_count < GlobalConsts.MAX_PHOTOS) {
                    list = am.getAccounts();
                    LOG.debug("Got {} accounts", list.length);
                    for (Account a : list) {
                        LOG.debug("{} {}", a.name, a.type);
                    }

                    Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                            false, null, null, null, null);
                    startActivityForResult(intent, PICK_ACCOUNT_REQUEST);


                } else {
                    MyToast.toast("You have reached maximum allowed photos", GooglePhotos.this, R.drawable.ic_done_color_24dp, com.photozuri.photozuri.Utills.Constants.TOAST_LONG);
                }



            }
        });

        Intent intent = getIntent();
        count_o = intent.getIntExtra("count_o", 0);

        ArrayList<MyImage> images = //convertImages(
                (ArrayList<MyImage>) intent.getSerializableExtra("images");
        if (images != null) {
            this.images.clear();
            this.images = images;
        }

        initViews(images);

        if (networkUtils.isNetworkAvailable()) {

            if (total_count < GlobalConsts.MAX_PHOTOS) {
                list = am.getAccounts();
                LOG.debug("Got {} accounts", list.length);
                for (Account a : list) {
                    LOG.debug("{} {}", a.name, a.type);
                }

                Intent intent2 = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                        false, null, null, null, null);
                startActivityForResult(intent2, PICK_ACCOUNT_REQUEST);


            } else {
                MyToast.toast("You have reached maximum allowed photos", GooglePhotos.this, R.drawable.ic_done_color_24dp, com.photozuri.photozuri.Utills.Constants.TOAST_LONG);
            }

//
//            list = am.getAccounts();
//            LOG.debug("Got {} accounts", list.length);
//            for (Account a : list) {
//                LOG.debug("{} {}", a.name, a.type);
//            }
//
//            Intent intent2 = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
//                    false, null, null, null, null);
//            startActivityForResult(intent2, PICK_ACCOUNT_REQUEST);
        } else {
            Snackbar.make(fab, "Please Check your connection", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }


    }

    private void chooseAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);

        Log.d("vvf", "Starting activity for Choosing Account");
        startActivityForResult(intent, Constants.REQUEST_ACCOUNT_PICKER);
    }

    private void chooseGoogleAccount() {

        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);

        Log.d(TAG, "Starting activity for Choosing Account");
        startActivityForResult(intent, Constants.REQUEST_ACCOUNT_PICKER);
    }

    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case Constants.REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    googleAccountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

                    fetchAuthToken();
                } else if (resultCode == RESULT_CANCELED) {
                    Log.d(TAG, "Google account unspecified");
                }
                break;
            case Constants.REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    chooseGoogleAccount();
                }
                break;
            case Constants.REQ_SIGN_IN_REQUIRED:
                if (resultCode == RESULT_OK) {
                    // We had to sign in - now we can finish off the token request.
                    fetchAuthToken();
                }
                break;
            case Constants.GRID_REQUEST:
                if (resultCode == RESULT_OK && data != null) {

                    try {
                        ArrayList<GDModel> model = (ArrayList<GDModel>) data.getSerializableExtra("images");

                        Log.d("vvf", "jj" + model.size());


                        ArrayList<MyImage> images = new ArrayList<>();
                        if (model != null && model.size() > 0) {
                            int a = 0;
                            for (GDModel gdModel : model) {
                                // a, gdModel.getTitle(), gdModel.getFullImageLink()
                                MyImage image = new MyImage();
                                image.setId(a);
                                image.setName(gdModel.getTitle());
                                image.setPath(gdModel.getFullImageLink());
                                image.setImageFrom(com.photozuri.photozuri.Utills.Constants.GOOGLE);
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

            case PICK_ACCOUNT_REQUEST:
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Connecting");
                progressDialog.show();


                if (resultCode == RESULT_OK) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    LOG.debug("Selected Account {}", accountName);
                    selectedAccount = null;
                    for (Account a : list) {
                        if (a.name.equals(accountName)) {
                            selectedAccount = a;
                            break;
                        }
                    }


                    try {
                        selectedAccountName = accountName;

                        am.getAuthToken(
                                selectedAccount,                     // Account retrieved using getAccountsByType()
                                "lh2",            // Auth scope
                                null,                        // Authenticator-specific options
                                this,                           // Your activity
                                new OnTokenAcquired(),          // Callback called when a token is successfully acquired
                                null);
                        // Callback called if an error occ
                    } catch (Exception nm) {
                        nm.printStackTrace();
                    }
                }
                break;
            case REQUEST_AUTHENTICATE:
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Connecting");
                progressDialog.show();

                if (resultCode == RESULT_OK) {
                    am.getAuthToken(
                            selectedAccount,                     // Account retrieved using getAccountsByType()
                            "lh2",            // Auth scope
                            null,                        // Authenticator-specific options
                            this,                           // Your activity
                            new OnTokenAcquired(),          // Callback called when a token is successfully acquired
                            null);    // Callback called if an error occ
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(GooglePhotos.this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            // Display a dialog showing the connection error
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                            connectionStatusCode, GooglePhotos.this,
                            Constants.REQUEST_GOOGLE_PLAY_SERVICES);
                    dialog.show();
                }
            });
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }


    private void fetchAndShowImages() {
        Log.d("vvf", "Stity for Choosing Account");

//        progressDialog = new ProgressDialog(GooglePhotos.this);
//        progressDialog.setMessage("Fetching photos....");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.setIndeterminate(false);
//        //progressDialog.setMax(gdModels1.size());
//        progressDialog.setCancelable(false);
        //  progressDialog.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("vvf", "1Stity for Choosing Account");
                Log.d(TAG, " 1 STARTING FETCH ASYNC");
                GDController.getImageFeed().clear();
                Log.d("vvf", "2Stity for Choosing Account");
                Log.d(TAG, "2 STARTING FETCH ASYNC");
                if (networkUtils.isNetworkAvailable()) {
                    Log.d("vvf", "3Stity for Choosing Account");
                    Log.d(TAG, "STARTING FETCH ASYNC");
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            //if(currentPage ==1 || (currentPage > 1 && GDController.isMoreAvailable())) {
                            ArrayList<GDModel> photoResults;
                            // get the photo search results
                            photoResults = GDController.getPhotos(currentPage);
                            // if (photoResults.size() > 0)
                                GDController.getImageFeed().addAll(photoResults);
                            // else
                                // endOfResultsDisplay = true;

                                currentPage++;
//                    } else {
//                       // endOfResultsDisplay = true;
//                    }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            if (GDController.getImageFeed().size() > 0) {
                                Log.d(TAG, "DONE FETCH ASYNC CALLNG setReadyToLoadImages");
                                setReadyToLoadImages();
                                //imageAdapter.notifyDataSetChanged();
                                // Obtain current position to maintain scroll position
                                //  int currentPosition = albumGrid.getFirstVisiblePosition();

                                // Set new scroll position
                                // albumGrid.smoothScrollToPosition(currentPosition + 1, 0);
                            } //else
                            // txtNoAlbums.setVisibility(View.VISIBLE);

                            //progressDialog.dismiss();
                            // progressLoadMore.setVisibility(View.GONE);
                        }
                    }.execute();
                } else {
                    //progressDialog.dismiss();
                    Toast.makeText(GooglePhotos.this, "No Internet", Toast.LENGTH_SHORT).show();
                    // progressDialog.dismiss();
                    // progressLoadMore.setVisibility(View.GONE);
                }
            }
        });


    }

    private void fetchAuthToken() {

        Log.d(TAG, "STARTING FETCHAUTHTOKEN");
        if (googleAccountName != null) {
            PrefManager prefManager = new PrefManager(GooglePhotos.this);
            prefManager.setGoogleAccountName(googleAccountName);
            Log.d(TAG, "FETCHAUTHTOKEN GOOGLEACOUNTNAME NOT NULL");
            SharedPreferences settings = GooglePhotos.this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Constants.PREF_ACCOUNT_NAME, googleAccountName);
            editor.commit();

            if (networkUtils.isNetworkAvailable()) {
                new AsyncTask() {

                    @Override
                    protected Object doInBackground(Object[] objects) {
                        try {
                            Log.d(TAG, " FETCHAUTHTOKEN Requesting token for account: " +
                                    googleAccountName);
                            googleAuthToken = GoogleAuthUtil.getToken(GooglePhotos.this,
                                    googleAccountName, Constants.GPHOTOS_SCOPE);

                            Log.d(TAG, " FETCHAUTHTOKEN Received Token: " + googleAuthToken);
                            com.photozuri.photozuri.Views.V1.Usables.GDController.setAPIToken(googleAuthToken);
                            Log.d(TAG, "FETCHAUTHTOKEN CALL TO SETREADYLOADIMAGE");
                            setReadyToLoadImages();
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                        } catch (UserRecoverableAuthException e) {
                            startActivityForResult(e.getIntent(), Constants.REQ_SIGN_IN_REQUIRED);
                        } catch (GoogleAuthException e) {
                            Log.e(TAG, e.getMessage());
                        }
                        return null;
                    }
                }.execute();
            } else {
                Toast.makeText(GooglePhotos.this, "Device not online", Toast.LENGTH_LONG).show();
            }
        } else {
            chooseGoogleAccount();
        }
    }

    private void setReadyToLoadImages() {

//        try {
//            if (progressDialog != null) {
//                progressDialog.dismiss();
//            }
//        }catch (Exception nm){
//
//        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//            progressDialog.dismiss();
                choose.setText("Ready");

                Log.d(TAG, "STARTING SETREADYTOLOAD");
                // fetchAndShowImages();
                if (GDController.getImageFeed() != null) {
                    Log.d(TAG, "STARTING SETREADYTOLOAD GETIMAGEFEED IS NOT NULL");
                    if (GDController.getImageFeed().size() > 1) {
                        Log.d(TAG, "STARTING SETREADYTOLOAD GETIMAGEFEED SIZE >1 LAUNCH GRID POPUP");
                        Intent intent = new Intent(GooglePhotos.this, GridPopup.class);
                        intent.putExtra("data", GDController.getImageFeed());
                        intent.putExtra("count", total_count);
                        intent.putExtra("from", com.photozuri.photozuri.Utills.Constants.GOOGLE);
                        GooglePhotos.this.startActivityForResult(intent, Constants.GRID_REQUEST);


                    } else {
                        GooglePhotos.this.fetchAndShowImages();
                        Log.d(TAG, "end SETREADYTOLOAD GETIMAGEFEED SIZE <1 ");

                    }

                } else {

                    GooglePhotos.this.fetchAndShowImages();
                }
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


    public <T extends GphotoFeed> T getFeed(String feedHref,
                                            Class<T> feedClass) throws IOException, ServiceException {
        LOG.debug("Get Feed URL: " + feedHref);

        return picasaService.getFeed(new URL(feedHref), feedClass);
    }

    public List<AlbumEntry> getAlbums(String userId) throws IOException,
            ServiceException {
        PrefManager prefManager = new PrefManager(GooglePhotos.this);
        prefManager.setPiccasaWebUserid(userId);


        String albumUrl = API_PREFIX + userId;
        UserFeed userFeed = getFeed(albumUrl, UserFeed.class);

        List<GphotoEntry> entries = userFeed.getEntries();
        List<AlbumEntry> albums = new ArrayList<AlbumEntry>();
        //albums.addAll(entries);
        for (GphotoEntry entry : entries) {
            AlbumEntry ae = new AlbumEntry(entry);
            LOG.debug("Album name {}", ae.getName());

            albums.add(ae);
        }

        return albums;
    }

    public List<PhotoEntry> getPhotos(String userId, AlbumEntry album) throws IOException,
            ServiceException {
        AlbumFeed feed = album.getFeed();
        List<PhotoEntry> photos = new ArrayList<PhotoEntry>();
        for (GphotoEntry entry : feed.getEntries()) {
            PhotoEntry pe = new PhotoEntry(entry);
            photos.add(pe);
        }
        //LOG.debug("Album {} has {} photos", album.getName(), photos.size());
        return photos;
    }

    private void loadImages(List<PhotoEntry> result) {
        if (result != null) {

            ArrayList<GDModel> photos = new ArrayList<>();
            for (PhotoEntry photoEntry : result) {
                GDModel photo = new GDModel();


                try {
                    photo.setCreatonTime(photoEntry.getTimestamp().toString());
                    Log.d("Dated : ", "" + photo.getCreatonTime());
                } catch (ServiceException e) {
                    e.printStackTrace();
                    photo.setCreatonTime("");
                }

                photo.setTitle(DateTimeUtils.getNow() + "p" + photoEntry.getGphotoId());
                photo.setThumbnailLink(photoEntry.getMediaContents().get(0).getUrl());
                photo.setFullImageLink(photoEntry.getMediaContents().get(0).getUrl());
                Log.d("gdgphotos", photo.getFullImageLink());
                photos.add(photo);
            }


            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Log.d(TAG, "STARTING SETREADYTOLOAD GETIMAGEFEED SIZE >1 LAUNCH GRID POPUP");
            Intent intent = new Intent(GooglePhotos.this, GridPopup.class);
            intent.putExtra("data", photos);
            intent.putExtra("count", total_count);
            intent.putExtra("from", com.photozuri.photozuri.Utills.Constants.GOOGLE);
            GooglePhotos.this.startActivityForResult(intent, Constants.GRID_REQUEST);


        } else {
            list = am.getAccounts();
            LOG.debug("Got {} accounts", list.length);
            for (Account a : list) {
                LOG.debug("{} {}", a.name, a.type);
            }

            Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                    false, null, null, null, null);
            startActivityForResult(intent, PICK_ACCOUNT_REQUEST);
            // GooglePhotos.this.fetchAndShowImages();
            Log.d(TAG, "end SETREADYTOLOAD GETIMAGEFEED SIZE <1 ");

        }


    }

    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            try {
                Bundle b = result.getResult();

                if (b.containsKey(AccountManager.KEY_INTENT)) {
                    Intent intent = b.getParcelable(AccountManager.KEY_INTENT);
                    int flags = intent.getFlags();
                    intent.setFlags(flags);
                    flags &= ~Intent.FLAG_ACTIVITY_NEW_TASK;
                    startActivityForResult(intent, REQUEST_AUTHENTICATE);
                    return;
                }

                if (b.containsKey(AccountManager.KEY_AUTHTOKEN)) {
                    final String authToken = b.getString(AccountManager.KEY_AUTHTOKEN);
                    LOG.debug("Auth token {}", authToken);
                    picasaService = new PicasawebService("pictureframe");
                    picasaService.setUserToken(authToken);

                    new AsyncTask<Void, Void, List<PhotoEntry>>() {
                        @Override
                        protected List<PhotoEntry> doInBackground(Void... voids) {
                            List<AlbumEntry> albums = null;
                            try {

                                albums = getAlbums(selectedAccountName);
                                LOG.debug("Got {} albums", albums.size());
//                                for (AlbumEntry myAlbum : albums) {
//                                    LOG.debug("Album {} ", myAlbum.getTitle().getPlainText());
//                                }
                                AlbumEntry album = albums.get(0);

                                List<PhotoEntry> photos = getPhotos(selectedAccountName, album);
                                // PhotoEntry photo = photos.get(0);


                                //URL photoUrl = new URL(photo.getMediaContents().get(0).getUrl());
                                //Bitmap bmp = BitmapFactory.decodeStream(photoUrl.openConnection().getInputStream());
                                return photos;
                            } catch (ServiceForbiddenException e) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                LOG.error("Token expired, invalidating");
                                am.invalidateAuthToken("com.google", authToken);
                            } catch (IOException e) {
                                e.printStackTrace();
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } catch (ServiceException e) {
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                            return null;
                        }

                        protected void onPostExecute(List<PhotoEntry> result) {
                            //picture.setImageBitmap(result);
                            loadImages(result);


                        }
                    }.execute(null, null, null);
                }
            } catch (Exception e) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                e.printStackTrace();
            }
        }
    }
}
