package com.photozuri.photozuri.Views.V1.Preview;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.R;

/**
 * Created by Eric on 1/26/2018.
 */

public class WmtsCardFrament extends Fragment {

    private CardView cardView;
    private MyImage myImage;
    //private DbOperations dbOperations;
    //private FragmentQuestion f;
    private Button finish;
    private boolean lastOne;
    private Dialog dialog;
    // TouchyWebView webView;

    public static Fragment getInstance(int position, MyImage myImage, int lastOne) {
        WmtsCardFrament f = new WmtsCardFrament();
        Bundle args = new Bundle();

        args.putInt("last", lastOne);
        args.putSerializable("data", myImage);
//
        args.putInt("position", position);
//        args.putString("A",myImage.getQuestion_A());
//        args.putString("B",myImage.getQuestion_B());
//        args.putString("C",myImage.getQuestion_C());
//        args.putString("D",myImage.getQuestion_D());
//
//        args.putString("Q",myImage.getQuestion_text());
//        args.putInt("Pid",myImage.getPaper_id());
//
//        args.putSerializable("data",myImage);
//
        f.setArguments(args);

        return f;
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wallmount_card, container, false);
        cardView = view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * WmtsCardAdapter.MAX_ELEVATION_FACTOR);

        int position = getArguments().getInt("position");
        TextView title = view.findViewById(R.id.title);

        title.setText("Wallmount : " + (position + 1));

        MyImage images = (MyImage) getArguments().getSerializable("data");


        ImageView imageView1 = view.findViewById(R.id.img_mount);


        try {
            if (images != null) {

                setImage(images.getPath(), imageView1);
            } else {
                imageView1.setVisibility(View.GONE);
            }


        } catch (Exception nm) {
            nm.printStackTrace();
        }


        return view;
    }

    private void setImage(String path, ImageView imageView) {
        RequestOptions cropOptions = new RequestOptions().fitCenter();

        Glide.with(getContext())

                .load(path)

                .apply(cropOptions)
                .into(imageView);


    }


    public CardView getCardView() {
        return cardView;
    }

}

