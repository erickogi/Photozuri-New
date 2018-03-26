package com.photozuri.photozuri.Views.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.photozuri.photozuri.Data.PrefManager;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Views.ProductsActivity;

public class SplashScreen extends AppCompatActivity {
    private static int spalsh_time_out = 1500;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager=new PrefManager(SplashScreen.this);

        setContentView(R.layout.activity_splash_screen);
        initUI();
        handler();
    }

    private void initUI() {
        if(prefManager.isLoggedIn()){


        }else {


        }
    }

    private void handler() {
        new Handler().postDelayed(() -> {
                    if (prefManager.isLoggedIn()) {
                        Intent intent = new Intent(SplashScreen.this, ProductsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);


                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashScreen.this, ProductsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);


                        //finish();

                        startActivity(intent);
                        finish();

                    }
                }


                , spalsh_time_out);

    }
}
