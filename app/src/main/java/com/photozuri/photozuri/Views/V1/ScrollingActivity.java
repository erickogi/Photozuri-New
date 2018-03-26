package com.photozuri.photozuri.Views.V1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.TestDataGen;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.DrawerClass;
import com.photozuri.photozuri.Utills.TimeLine.Orientation;
import com.photozuri.photozuri.Utills.TimeLine.TimeLineAdapter;
import com.photozuri.photozuri.Utills.TimeLine.TimeLineModel;
import com.photozuri.photozuri.Utills.UtilListeners.DrawerItemListener;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.Views.Login.LoginActivity;
import com.photozuri.photozuri.Views.V1.Fragments.FragmentCompleted;
import com.photozuri.photozuri.Views.V1.Fragments.FragmentInProgress;
import com.photozuri.photozuri.Views.V1.Fragments.FragmentSaved;
import com.photozuri.photozuri.Views.V1.Stories.MainStories;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ScrollingActivity extends AppCompatActivity {
    ViewPagerAdapter adapter;
    private RecyclerView mRecyclerView;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mDataList = new ArrayList<>();
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private PrefManager prefManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SwipeRefreshLayout swipe_refresh_layout;
    private FragmentInProgress fragmentActive;
    private FragmentCompleted fragmentNew;
    private FragmentSaved fragmentCanceled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");
        FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startDialog();
                startActivity(new Intent(ScrollingActivity.this, HomeActivity.class));
            }
        });
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);


        tabLayout = findViewById(R.id.tabs);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        setupTabIcons();
        ImageView imageView = findViewById(R.id.mount);
        Picasso.with(ScrollingActivity.this).load(R.drawable.mounted_centurion).into(imageView);

//        mRecyclerView = findViewById(R.id.recyclerView);
//
//
//        mRecyclerView.setLayoutManager(getLinearLayoutManager());
//        mRecyclerView.setHasFixedSize(true);
//
//        mOrientation = Orientation.VERTICAL;
//        mWithLinePadding = false;
        prefManager = new PrefManager(ScrollingActivity.this);

        DrawerClass.getDrawer(ScrollingActivity.this, toolbar, 1, prefManager.getUserData(), new DrawerItemListener() {
            @Override
            public void logOutClicked() {
                prefManager.setIsLoggedIn(false);
                startActivity(new Intent(ScrollingActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void helpClicked() {

                //  startActivity(new Intent(ScrollingActivity.this, MainDashboard.class));
            }

            @Override
            public void homeClicked() {

            }

            @Override
            public void settingsClicked() {

                // startActivity(new Intent(ScrollingActivity.this, MainDashboard.class));
            }

            @Override
            public void profileClicked() {
                startActivity(new Intent(ScrollingActivity.this, MyProfile.class));
                //finish();

            }

            @Override
            public void storiesClicked() {
                startActivity(new Intent(ScrollingActivity.this, MainStories.class));
                //finish();

            }

            @Override
            public void shareClicked() {
                // startActivity(new Intent(MainActivity.this, MainStories.class));
                //finish();
                try {
                    Intent in = new Intent();
                    in.setAction(Intent.ACTION_SEND);
                    in.putExtra(Intent.EXTRA_TEXT, " Print your photos withs us Download at http://www.erickogi.co.ke");
                    in.setType("text/plain");
                    startActivity(in);
                } catch (Exception nm) {

                }
            }
        } );


        //initView();

    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


        fragmentNew = new FragmentCompleted();
        fragmentActive = new FragmentInProgress();
        fragmentCanceled = new FragmentSaved();


        adapter.addFragment(fragmentNew, "lo");
        adapter.addFragment(fragmentActive, "li");
        adapter.addFragment(fragmentCanceled, "lu");


        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);

        View root = tabLayout.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.black));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }

    }

    private void setupTabIcons() {


        tabLayout.getTabAt(0).setText("My Orders");
        tabLayout.getTabAt(1).setText("Editing");
        tabLayout.getTabAt(2).setText("Saved Orders");


    }

    private void setupWindowAnimationss() {
        Slide slide = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            slide = new Slide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            slide.setDuration(1000);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(slide);
        }
    }

    private void initView(int a) {

    }

    private void initView() {
       // setDataListItems();
        mTimeLineAdapter = new TimeLineAdapter(TestDataGen.setDataListItems(DbConstants.selected, DbConstants.uploaded), mOrientation, mWithLinePadding, new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {

            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onClickListener(int adapterPosition, ImageView imageView) {

            }
        });
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

    private LinearLayoutManager getLinearLayoutManager() {
        if (mOrientation == Orientation.HORIZONTAL) {
            return new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        } else {
            return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }
    }

    private void startDialog() {


        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_type, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput.setTitle("Choose Print type");

        LinearLayout mLinearPhotobook,mLinearWallmount,mLinearPassport,mLinearWallmountSize;

        mLinearPassport=mView.findViewById(R.id.l_passports);
        mLinearPhotobook=mView.findViewById(R.id.l_photobook);
        mLinearWallmount=mView.findViewById(R.id.l_wallmount);



        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here


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


        Button theButton = alertDialogAndroid.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setVisibility(View.GONE);
        mLinearPassport.setOnClickListener(new CustomListener(alertDialogAndroid, theButton));
        mLinearPhotobook.setOnClickListener(new CustomListener(alertDialogAndroid, theButton));
        mLinearWallmount.setOnClickListener(new CustomListener(alertDialogAndroid, theButton));



        theButton.setOnClickListener(new CustomListener(alertDialogAndroid,theButton));
    }

    public void addNew(View view) {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new LinkedList<>();
        private final List<String> mFragmentTitleList = new LinkedList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    //    private String getFragmentTag(int pos) {
//        return "android:switcher:" + R.id.viewpager + ":" + pos;
//    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        private Button button;

        public CustomListener(Dialog dialog, Button theButton) {
            this.dialog = dialog;
            this.button=theButton;


        }

        @Override
        public void onClick(View v) {




        }



    }

}
