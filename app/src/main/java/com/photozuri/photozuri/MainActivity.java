package com.photozuri.photozuri;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.Utills.DrawerClass;
import com.photozuri.photozuri.Utills.UtilListeners.DrawerItemListener;
import com.photozuri.photozuri.Views.Login.LoginActivity;
import com.photozuri.photozuri.Views.V1.Stories.MainStories;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private PrefManager prefManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    //mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_dashboard:
////                    mTextMessage.setText(R.string.title_dashboard);
//                    return true;
//                case R.id.navigation_notifications:
//                   // mTextMessage.setText(R.string.title_notifications);
//                    return true;
//            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefManager=new PrefManager(MainActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
     //   mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //navigation.setVisibility(View.GONE);

        DrawerClass.getDrawer(MainActivity.this, toolbar, 1, prefManager.getUserData(), new DrawerItemListener() {
            @Override
            public void logOutClicked() {
                prefManager.setIsLoggedIn(false);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void helpClicked() {

            }

            @Override
            public void homeClicked() {

            }

            @Override
            public void settingsClicked() {
                // startActivity(new Intent(MainActivity.this, MainDashboard.class));
            }

            @Override
            public void profileClicked() {

            }

            @Override
            public void storiesClicked() {
                startActivity(new Intent(MainActivity.this, MainStories.class));
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


    }

}
