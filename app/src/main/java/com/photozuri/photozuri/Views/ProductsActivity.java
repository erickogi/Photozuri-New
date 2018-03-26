package com.photozuri.photozuri.Views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.photozuri.photozuri.Adapter.CategoriesAdapter;
import com.photozuri.photozuri.Adapter.ProductsAdapter;
import com.photozuri.photozuri.Data.Models.ProductsModel;
import com.photozuri.photozuri.Data.Models.TitleModel;
import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.Data.TestDataGen;
import com.photozuri.photozuri.Data.UserModel;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.ResideMenu.ResideMenu;
import com.photozuri.photozuri.ResideMenu.ResideMenuItem;
import com.photozuri.photozuri.Utills.AppUtils;
import com.photozuri.photozuri.Utills.Constants;
import com.photozuri.photozuri.Utills.DateTimeUtils;
import com.photozuri.photozuri.Utills.MyToast;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.Views.Login.LoginActivity;
import com.photozuri.photozuri.Views.V1.Fragments.FragmentCompleted;
import com.photozuri.photozuri.Views.V1.Fragments.FragmentInProgress;
import com.photozuri.photozuri.Views.V1.Fragments.FragmentProducts;
import com.photozuri.photozuri.Views.V1.Fragments.FragmentSaved;
import com.photozuri.photozuri.Views.V1.MyProfile;
import com.photozuri.photozuri.Views.V1.Passports;
import com.photozuri.photozuri.Views.V1.Photobooks;
import com.photozuri.photozuri.Views.V1.SinglePrint;
import com.photozuri.photozuri.Views.V1.Wallmounts;
import com.photozuri.photozuri.imagepicker.model.Config;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {
    public static Fragment fragment = null;
    private static ArrayList<ProductsModel> images = new ArrayList<>();
    Intent intent;
    String desc = "";
    String title = "";
    ResideMenuItem resideMenuItemlogout;
    AHBottomNavigationItem item2;
    AHBottomNavigation bottomNavigation;
    private DbOperations dbOperations;
    private AppUtils appUtils;
    private GridView gridView;
    private ResideMenu resideMenu;
    private RecyclerView recyclerView;
    private Config config;
    private ProductsAdapter adapter;
    private CategoriesAdapter imageAdapter;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PrefManager prefManager;
    private RequestOptions options;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {


        Bundle bundle = new Bundle();
        switch (item.getItemId()) {
            case R.id.navigation_products:
                fragment = new FragmentProducts();
                popOutFragments();
                bundle.putInt("type", 0);
                fragment.setArguments(bundle);
                setFragment();
                // mTextMessage.setText(R.string.title_home);
                return true;
            case R.id.navigation_cart:
                popOutFragments();
                fragment = new FragmentSaved();
                bundle.putInt("type", 1);
                fragment.setArguments(bundle);
                setFragment();
                // mTextMessage.setText(R.string.title_dashboard);
                return true;
            case R.id.navigation_history:
                fragment = new FragmentCompleted();
                popOutFragments();
                bundle.putInt("type", 2);
                fragment.setArguments(bundle);
                setFragment();
                //mTextMessage.setText(R.string.title_notifications);
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNav();
        checkTerms("This app requires access to a Social sites information, Personal pictures and contact information from Facebook ,Google Photos , Instagram. This information will not be shared with any third party and will be encrypted to protect your privacy.  Accept these terms by clicking Okay (Privacy policy  http://www.photozuri.com/photozuri/privacypolicy.html )");

        dbOperations = new DbOperations(ProductsActivity.this);
        prefManager = new PrefManager(ProductsActivity.this);
        this.options = (new RequestOptions())
                .placeholder(R.drawable.imagepicker_image_placeholder)
                .error(R.drawable.ic_person_black_24dp)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);


        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingAB_layout);

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimary));


        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.background_bf);
        resideMenu.attachToActivity(this);

        resideMenu.setFitsSystemWindows(true);
        resideMenu.addIgnoredView(bottomNavigation);
        resideMenu.setShadowVisible(false);





//        BottomNavigationView navigation = findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        ResideMenuItem resideMenuItemhome = new ResideMenuItem(this, R.drawable.ic_home_black_24dp, "Home ");
        //ResideMenuItem resideMenuItem0=new ResideMenuItem(this,R.drawable.ic_add_bla_24dp,"Add Assets ");
        ResideMenuItem resideMenuItemprofile = new ResideMenuItem(this, R.drawable.ic_account_circle_black_24dp, "Profile");
        //ResideMenuItem resideMenuItem2=new ResideMenuItem(this,R.drawable.ic_history_black_24dp,"History");
        String logg = "Login";
        if (prefManager.isLoggedIn()) {
            logg = "Log Out";
        }
        resideMenuItemlogout = new ResideMenuItem(this, R.drawable.ic_exit_to_app_black_24dp, logg);
        ResideMenuItem resideMenuItemsettings = new ResideMenuItem(this, R.drawable.ic_settings_black_24dp, "Settings");
        ResideMenuItem resideMenuItemshare = new ResideMenuItem(this, R.drawable.ic_share_black_24dp, "Share");
        //ResideMenuItem resideMenuItemOrders = new ResideMenuItem(this, R.drawable.ic_photo_library_black_24dp, "Stories");

        resideMenu.addMenuItem(resideMenuItemhome, ResideMenu.DIRECTION_LEFT);
        //resideMenu.addMenuItem(resideMenuItem0, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(resideMenuItemprofile, ResideMenu.DIRECTION_LEFT);
        // resideMenu.addMenuItem(resideMenuItem2, ResideMenu.DIRECTION_LEFT);
        //resideMenu.addMenuItem(resideMenuItemsettings, ResideMenu.DIRECTION_LEFT);

        // resideMenu.addMenuItem(resideMenuItemOrders, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(resideMenuItemshare, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(resideMenuItemlogout, ResideMenu.DIRECTION_LEFT);

        resideMenuItemhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resideMenu.isOpened()) {
                    resideMenu.closeMenu();
                }
                // Toast.makeText(ProductsActivity.this,"Test",Toast.LENGTH_LONG).show();
                // MyToast.toast("Home Clicked",MainActivity.this,R.drawable.ic_home_black_24dp, Constants.TOAST_LONG);
                //dial();
            }
        });
//        resideMenuItem0.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // MyToast.toast("Home Clicked",MainActivity.this,R.drawable.ic_home_black_24dp, Constants.TOAST_LONG);
//
//               // startActivity(new Intent(MainActivity.this,CreateAsset.class));
//            }
//        });
        resideMenuItemprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resideMenu.isOpened()) {
                    resideMenu.closeMenu();
                }
                // MyToast.toast("Account Clicked",MainActivity.this,R.drawable.ic_account_circle_black_24dp, Constants.TOAST_LONG);
                if (prefManager.isLoggedIn()) {
                    startActivity(new Intent(ProductsActivity.this, MyProfile.class));
                } else {
                    MyToast.toast("Please login or register first to view your profile", ProductsActivity.this, R.drawable.ic_error_outline_black_24dp, Constants.TOAST_LONG);
                }

            }
        });
//        resideMenuItem2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //  MyToast.toast("History Clicked",MainActivity.this,R.drawable.ic_history_black_24dp, Constants.TOAST_LONG);
//
//            }
//        });
//        resideMenuItemOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (resideMenu.isOpened()) {
//                    resideMenu.closeMenu();
//                }
//                startActivity(new Intent(ProductsActivity.this, MainStories.class));
//
//                // MyToast.toast("Log Out Clicked",MainActivity.this,R.drawable.ic_exit_to_app_black_24dp, Constants.TOAST_LONG);
//            }
//        });

        resideMenuItemshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resideMenu.isOpened()) {
                    resideMenu.closeMenu();
                }
                try {
                    Intent in = new Intent();
                    in.setAction(Intent.ACTION_SEND);
                    in.putExtra(Intent.EXTRA_TEXT, " Print your photos withs us Download at http://www.erickogi.co.ke");
                    in.setType("text/plain");
                    startActivity(in);
                } catch (Exception nm) {

                }
                // MyToast.toast("Log Out Clicked",MainActivity.this,R.drawable.ic_exit_to_app_black_24dp, Constants.TOAST_LONG);
            }
        });
        resideMenuItemlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resideMenu.isOpened()) {
                    resideMenu.closeMenu();
                }
                if (prefManager.isLoggedIn()) {
                    prefManager.setIsLoggedIn(false);
                    prefManager.clearData();

                    alertDialogLogOut("You are now logged Out");
                    resideMenuItemlogout.setTitle("Login");

                    //startActivity(new Intent(ProductsActivity.this, LoginActivity.class));
                    //finish();
                } else {
                    Intent intent = new Intent(ProductsActivity.this, LoginActivity.class);
                    intent.putExtra("Action", Constants.LOGIN_EMPTY);
                    startActivity(intent);
                }
                // MyToast.toast("Log Out Clicked",MainActivity.this,R.drawable.ic_exit_to_app_black_24dp, Constants.TOAST_LONG);
            }
        });
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        fragment = new FragmentProducts();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, "fragmentMain").commit();

        ImageView imageView = findViewById(R.id.image);
        if (prefManager.isLoggedIn()) {
            UserModel userModel = new UserModel();
            if (userModel.getUriPhoto() != null) {
                Glide.with(ProductsActivity.this).load(prefManager.getUserData().getUriPhoto())
                        //.apply(options)
                        .into(imageView);
            } else if (userModel.getPhoto() != null) {
                Glide.with(ProductsActivity.this).load(prefManager.getUserData().getPhoto())
                        //.apply(options)
                        .into(imageView);
            } else {
                Glide.with(ProductsActivity.this).load(R.drawable.ic_account_circle_black_24dp)
                        //.apply(options)
                        .into(imageView);
            }
        }

        // initViews(getProducts());




        if (dbOperations.getCount(DbConstants.TABLE_SAVED_TITLES, DbConstants.IMAGE_STATUS, String.valueOf(DbConstants.selected)) > 0) {

            // startDialog();
        }
    }

    private void alertDialogLogOut(String s) {
        final DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:


                    dialog.dismiss();
                    // getData();
                    //this.finish();
                    //saveData(true);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //saveData(true);
                    // prefrenceManager.clearBeneficiary();
                    dialog.dismiss();


                    break;
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);

        builder.setMessage(s).setPositiveButton("Okay", dialogClickListener)
                //.setNegativeButton("Dismiss", dialogClickListener)
                .show();


    }

    private void bottomNav() {
        bottomNavigation = findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_home_black_24dp, R.color.white);
        item2 = new AHBottomNavigationItem("Cart", R.drawable.ic_shopping_cart_black_24dp, R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("History", R.drawable.ic_history_black_24dp, R.color.white);


// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#209fdf"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(true);

// Enable the translation of the FloatingActionButton
        //bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);

// Change colors
        // bottomNavigation.setAccentColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setInactiveColor(Color.parseColor("#FFFFFF"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Display color under navigation bar (API 21+)
// Don't forget these lines in your style-v21
// <item name="android:windowTranslucentNavigation">true</item>
// <item name="android:fitsSystemWindows">true</item>
        // bottomNavigation.setTranslucentNavigationEnabled(true);

// Manage titles
        // bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        // bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        // bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

// Use colored navigation with circle reveal effect
        //bottomNavigation.setColored(true);

// Set current item programmatically
        bottomNavigation.setCurrentItem(0);
        // bottomNavigation.s

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

// Add or remove notification for each item

//        int count=getCount(DbConstants.TABLE_SAVED_TITLES,
//                DbConstants.IMAGE_STATUS, String.valueOf(DbConstants.saved));
//
//        if(count>0) {
//            bottomNavigation.setNotification(String.valueOf(count), 1);
//        }

// OR
//        AHNotification notification = new AHNotification.Builder()
//                .setText("1")
//                .setBackgroundColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_back))
//                .setTextColor(ContextCompat.getColor(DemoActivity.this, R.color.color_notification_text))
//                .build();
//        bottomNavigation.setNotification(notification, 1);

// Enable / disable item & set disable color
        // bottomNavigation.enableItemAtPosition(2);
        // bottomNavigation.disableItemAtPosition(2);
        // bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

// Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                Bundle bundle = new Bundle();
                switch (position) {
                    case 0:
                        fragment = new FragmentProducts();
                        popOutFragments();
                        bundle.putInt("type", 0);
                        fragment.setArguments(bundle);
                        setFragment();
                        break;
                    case 1:
                        popOutFragments();
                        fragment = new FragmentSaved();
                        bundle.putInt("type", 1);
                        fragment.setArguments(bundle);
                        setFragment();
                        break;
                    case 2:
                        fragment = new FragmentCompleted();
                        popOutFragments();
                        bundle.putInt("type", 2);
                        fragment.setArguments(bundle);
                        setFragment();
                        break;
                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

        try {
            setCount();
        } catch (Exception nm) {
            nm.printStackTrace();
        }

    }

    void initViews(ArrayList<ProductsModel> myImages) {
        recyclerView = findViewById(R.id.recyclerView);
        ArrayList<ProductsModel> productsModels = getProducts();
        imageAdapter = new CategoriesAdapter(ProductsActivity.this, productsModels, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {
                Log.d("oncatclicked", productsModels.get(position).getId());
                if (productsModels.get(position).getId().equals("18")) {
                    startDialog(Constants.PHOTO_BOOK_INTENT, productsModels.get(position).getCategory());

                } else if (productsModels.get(position).getId().equals("19")) {
                    startDialog(Constants.PASSPORT_INTENT, productsModels.get(position).getCategory());

                } else if (productsModels.get(position).getId().equals("20")) {
                    startDialog(Constants.WALL_MOUNT_INTENT, productsModels.get(position).getCategory());
                } else if (productsModels.get(position).getId().equals("21")) {
                    startDialog(Constants.SINGLE_PRINT_INTENT, productsModels.get(position).getCategory());
                }
            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onClickListener(int adapterPosition, ImageView imageView) {

            }
        });
        imageAdapter.notifyDataSetChanged();

//        gridView = findViewById(R.id.gridview);
//
//        adapter = new ProductsAdapter(ProductsActivity.this, myImages);
//        gridView.setAdapter(adapter);
//
//        //viewsPlay(imageAdapter.getCount() > 0);
//
//
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//            }
//        });
//
//
//        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                return false;
//            }
//        });
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(imageAdapter);
    }

    private ArrayList<ProductsModel> getProducts() {

        ArrayList<ProductsModel> productsModels = new ArrayList<>();

        ProductsModel p = new ProductsModel();
        p.setImage(R.drawable.wallj);
        p.setDescription("Great wallmounts to decorate your living room");
        p.setSizable("true");
        p.setCategory("Wallmount");
        p.setId("20");

        ProductsModel p2 = new ProductsModel();
        p2.setImage(R.drawable.pasp);
        p2.setDescription("Create awesome Passport photos");
        p2.setSizable("true");
        p2.setId("19");
        p2.setCategory("Passports");

        ProductsModel p3 = new ProductsModel();
        p3.setImage(R.drawable.bok);
        p3.setDescription("Put your memories in awesome photobooks");
        p3.setSizable("true");
        p3.setId("18");
        p3.setCategory("Photo book");

        ProductsModel p4 = new ProductsModel();
        p4.setImage(R.drawable.sinp);
        p4.setDescription("Order as single prints");
        p4.setSizable("true");
        p4.setId("21");
        p4.setCategory("Single Prints");

        productsModels.add(p);
        productsModels.add(p2);
        productsModels.add(p3);
        productsModels.add(p4);

        productsModels.add(p);
        productsModels.add(p2);
        productsModels.add(p3);
        productsModels.add(p4);

        productsModels.add(p);
        productsModels.add(p2);
        productsModels.add(p3);
        productsModels.add(p4);

        return productsModels;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_products, menu);
        return true;
    }

    //    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return resideMenu.dispatchTouchEvent(ev);
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_nav) {
            resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
        }

        return super.onOptionsItemSelected(item);
    }

    private void startDialog(int type, String name) {
        if (type == Constants.WALL_MOUNT_INTENT) {

            LayoutInflater layoutInflaterAndroid = LayoutInflater.from(ProductsActivity.this);
            View mView = layoutInflaterAndroid.inflate(R.layout.title_desc, null);
            AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ProductsActivity.this);
            alertDialogBuilderUserInput.setView(mView);
            alertDialogBuilderUserInput.setTitle("New  " + name);
            alertDialogBuilderUserInput.setIcon(R.drawable.ic_add_black_24dp);
            RadioGroup radioGroupSize = mView.findViewById(R.id.radio_group_size);
            if (type == Constants.WALL_MOUNT_INTENT) {
                radioGroupSize.setVisibility(View.VISIBLE);
            } else {
                radioGroupSize.setVisibility(View.GONE);
            }

            // final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
            alertDialogBuilderUserInput
                    .setCancelable(false)
                    .setPositiveButton("Next", (dialogBox, id) -> {
                        // ToDo get user input here


                    })

                    .setNegativeButton("Dismiss",
                            (dialogBox, id) -> dialogBox.cancel());

            AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
            alertDialogAndroid.show();
            Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
            theButton.setOnClickListener(new CustomListener(alertDialogAndroid, type));
        } else {
            doP(type);
        }
    }

    public void wallmount(View view) {


        startDialog(Constants.WALL_MOUNT_INTENT, "");
    }

    public void passport(View view) {

        startDialog(Constants.PASSPORT_INTENT, "");
    }

    public void photobook(View view) {
        startDialog(Constants.PHOTO_BOOK_INTENT, "");
    }

    private void alertDialog(String message) {
        final DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:


                    dialog.dismiss();
                    // getData();
                    //this.finish();
                    //saveData(true);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //saveData(true);
                    // prefrenceManager.clearBeneficiary();
                    dialog.dismiss();


                    break;
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(ProductsActivity.this);

        builder.setMessage(message).setPositiveButton("Retry", dialogClickListener)
                .setNegativeButton("Dismiss", dialogClickListener)
                .show();

    }

    public void resideShow(View view) {
        resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
    }

    private void doP(int type) {
        // if (GeneralUtills.isFilledTextInputEditText(edttitle) && GeneralUtills.isFilledTextInputEditText(edtdesc)) {
        desc = "desc";
        title = DateTimeUtils.getNow() + "" + type;
        Log.d("titlesave", "Fields are good");

        new DbContentValues.savetitle(new TitleModel(), ProductsActivity.this, title, desc, type, DbConstants.selected, titleid -> {
            if (titleid != 9999) {
                GlobalConsts.TITLE_SELECTED = title;
                GlobalConsts.DESCRIPTION = desc;
                GlobalConsts.TITLE_ID = titleid;
                Intent intent;
                switch (type) {
                    case Constants.WALL_MOUNT_INTENT:
                        //dialog.dismiss();
                        intent = new Intent(ProductsActivity.this, Wallmounts.class);
                        GlobalConsts.TYPE_SELECTED = Constants.WALL_MOUNT_INTENT;
                        intent.putExtra(Constants.INTENT_NAME, Constants.WALL_MOUNT_INTENT);
                        startActivity(intent);

                        break;
                    case Constants.PASSPORT_INTENT:
                        //  dialog.dismiss();
                        intent = new Intent(ProductsActivity.this, Passports.class);

                        GlobalConsts.TYPE_SELECTED = Constants.PASSPORT_INTENT;
                        intent.putExtra(Constants.INTENT_NAME, Constants.PASSPORT_INTENT);
                        startActivity(intent);

                        break;
                    case Constants.PHOTO_BOOK_INTENT:
                        //dialog.dismiss();
                        intent = new Intent(ProductsActivity.this, Photobooks.class);

                        GlobalConsts.TYPE_SELECTED = Constants.PHOTO_BOOK_INTENT;
                        intent.putExtra(Constants.INTENT_NAME, Constants.PHOTO_BOOK_INTENT);
                        startActivity(intent);

                        break;
                    case Constants.SINGLE_PRINT_INTENT:
                        //dialog.dismiss();
                        intent = new Intent(ProductsActivity.this, SinglePrint.class);

                        GlobalConsts.TYPE_SELECTED = Constants.SINGLE_PRINT_INTENT;
                        intent.putExtra(Constants.INTENT_NAME, Constants.SINGLE_PRINT_INTENT);
                        startActivity(intent);

                        break;

                    default:
                        //dialog.dismiss();
                        Log.d("titlesave", "invalidoptionselected   " + type);
                }
            } else {
                Log.d("titlesave", "Couldnit save " + titleid);
            }
        }).execute();

//        } else {
//            Log.d("titlesave", "Fields are empty");
//        }
    }

    void setFragment() {
        // fragment = new FragmentSearch();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, fragment, "fragmentMain").commit();
    }

    void popOutFragments() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            fragmentManager.popBackStack();
        }
    }

    private int getCount(String table, String column, String item) {
        DbOperations dbOperations = new DbOperations(ProductsActivity.this);

        return dbOperations.getCount(table, column, item);
    }

    private void startDialog() {


        // LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        //View mView = layoutInflaterAndroid.inflate(R.layout.dialog_type, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        ///alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setTitle("In Progress");
        alertDialogBuilderUserInput.setMessage("Finish editing photos ");


        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here


                        fragment = new FragmentInProgress();

                        Bundle bundle = new Bundle();
                        popOutFragments();
                        bundle.putInt("type", 3);
                        fragment.setArguments(bundle);
                        setFragment();

                        dialogBox.cancel();
                    }
                })

                .setNegativeButton("Dismiss",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        })
        ;

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();


//        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
//        theButton.setVisibility(View.GONE);
//        mLinearPassport.setOnClickListener(new ScrollingActivity.CustomListener(alertDialogAndroid, theButton));
//        mLinearPhotobook.setOnClickListener(new ScrollingActivity.CustomListener(alertDialogAndroid, theButton));
//        mLinearWallmount.setOnClickListener(new ScrollingActivity.CustomListener(alertDialogAndroid, theButton));
//
//
//
//        theButton.setOnClickListener(new ScrollingActivity.CustomListener(alertDialogAndroid,theButton));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefManager.isLoggedIn()) {
            try {
                resideMenuItemlogout.setTitle("Log Out");
            } catch (Exception nm) {
                nm.printStackTrace();
            }
        } else {
            try {
                resideMenuItemlogout.setTitle("Login");
            } catch (Exception nm) {
                nm.printStackTrace();
            }
        }

        try {
            setCount();
        } catch (Exception nm) {

        }


        // bottomNav();
    }

    public void setCount() {
        try {
            int count = getCount(DbConstants.TABLE_SAVED_TITLES,
                    DbConstants.IMAGE_STATUS,

                    String.valueOf(DbConstants.saved),
                    String.valueOf(DbConstants.selected),
                    String.valueOf(DbConstants.uploaded)



            );

            if (count > 0) {
                bottomNavigation.setNotification(String.valueOf(count), 1);
            } else {
                bottomNavigation.setNotification("", 1);
            }
        } catch (Exception nm) {
            nm.printStackTrace();
        }
    }

    private int getCount(String table, String column, String item, String s1, String s2) {
        DbOperations dbOperations = new DbOperations(ProductsActivity.this);

        TestDataGen testDataGen = new TestDataGen(ProductsActivity.this);
        return testDataGen.getInSaved().size();//dbOperations.getCount(table, column, item, s1, s2);
    }

    private void checkTerms(String text) {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean agreed = sharedPreferences.getBoolean("agreed", false);
        if (!agreed) {
            new AlertDialog.Builder(ProductsActivity.this)
                    .setTitle("License agreement")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("agreed", true);
                            editor.commit();
                        }
                    })
                    .setNegativeButton("No", null)
                    .setMessage(text)
                    .show();
        }
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        private int type;

        public CustomListener(Dialog dialog, int type) {
            this.dialog = dialog;
            this.type = type;

        }

        @Override
        public void onClick(View v) {
            Log.d("titlesave", "recieved onclick event");


            EditText edttitle = dialog.findViewById(R.id.txt_album_title);
            EditText edtdesc = dialog.findViewById(R.id.txt_album_desc);
            if (type == Constants.WALL_MOUNT_INTENT) {
                RadioGroup radioGroup = dialog.findViewById(R.id.radio_group_size);

                if (radioGroup.getCheckedRadioButtonId() < 0) {
                    MyToast.toast("Select Size", getApplicationContext(), R.drawable.ic_error_outline_black_24dp, Constants.TOAST_LONG);
                    // Toast.makeText(ProductsActivity.this, "Select Size", Toast.LENGTH_SHORT).show();
                } else {

                    GlobalConsts.WALLMOUNTSELECTED = radioGroup.getCheckedRadioButtonId() == R.id.radio_btn_a3 ? "A4" : "A3";
                    desc = "desc";
                    title = DateTimeUtils.getNow() + "" + type;
                    Log.d("titlesave", "Fields are good");
                    //  Context context, String title, String desc, int type, int status,
                    new DbContentValues.savetitle(new TitleModel(), ProductsActivity.this, title, desc, type, DbConstants.selected, new DbContentValues.MyInterfaceTitles() {
                        @Override
                        public void onComplete(int titleid) {
                            if (titleid != 9999) {
                                GlobalConsts.TITLE_SELECTED = title;
                                GlobalConsts.DESCRIPTION = desc;
                                GlobalConsts.TITLE_ID = titleid;

                                Log.d("titleID", "" + titleid);
                                Intent intent;
                                switch (type) {
                                    case Constants.WALL_MOUNT_INTENT:
                                        dialog.dismiss();
                                        intent = new Intent(ProductsActivity.this, Wallmounts.class);
                                        GlobalConsts.TYPE_SELECTED = Constants.WALL_MOUNT_INTENT;
                                        intent.putExtra(Constants.INTENT_NAME, Constants.WALL_MOUNT_INTENT);
                                        startActivity(intent);

                                        break;
                                    case Constants.PASSPORT_INTENT:
                                        dialog.dismiss();
                                        intent = new Intent(ProductsActivity.this, Passports.class);

                                        GlobalConsts.TYPE_SELECTED = Constants.PASSPORT_INTENT;
                                        intent.putExtra(Constants.INTENT_NAME, Constants.PASSPORT_INTENT);
                                        startActivity(intent);

                                        break;
                                    case Constants.PHOTO_BOOK_INTENT:
                                        dialog.dismiss();
                                        intent = new Intent(ProductsActivity.this, Photobooks.class);

                                        GlobalConsts.TYPE_SELECTED = Constants.PHOTO_BOOK_INTENT;
                                        intent.putExtra(Constants.INTENT_NAME, Constants.PHOTO_BOOK_INTENT);
                                        //this.startActivity(intent);
                                        startActivity(intent);

                                        break;

                                    default:
                                        dialog.dismiss();
                                        Log.d("titlesave", "invalidoptionselected   " + type);
                                }
                            } else {
                                Log.d("titlesave", "Couldnit save " + titleid);
                            }
                        }
                    }).execute();


                }

            } else {


            }


        }
    }
}
