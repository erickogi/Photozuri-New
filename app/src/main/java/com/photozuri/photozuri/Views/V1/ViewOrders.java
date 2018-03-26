package com.photozuri.photozuri.Views.V1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.photozuri.photozuri.Adapter.PhotoAdapter;
import com.photozuri.photozuri.Data.Models.Images;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.Data.Models.Order;
import com.photozuri.photozuri.NetworkUtills.NetworkUtils;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.GeneralUtills;

import java.util.ArrayList;

public class ViewOrders extends AppCompatActivity {
    private ArrayList<MyImage> images = new ArrayList<>();
    private GridView gridView;
    private PhotoAdapter imageAdapter;
    private NetworkUtils networkUtils;
    private GeneralUtills generalUtills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception nm) {
            nm.printStackTrace();
        }
        images = new ArrayList<>();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Intent intent = getIntent();
        Order orders = (Order) intent.getSerializableExtra("data");

        if (orders != null) {
            ArrayList<Images> images = orders.getImages();
            if (images != null) {
                for (Images images1 : images) {
                    MyImage image = new MyImage();
                    image.setPath(images1.getImage());
                    image.setTitle_id(Integer.valueOf(orders.getOrder_id()));


                    this.images.add(image);
                }
            }
        }

        gridView = findViewById(R.id.gridview);
        imageAdapter = new PhotoAdapter(ViewOrders.this, images, false);
        gridView.setAdapter(imageAdapter);

        // viewsPlay(imageAdapter.getCount() > 0);

    }

}
