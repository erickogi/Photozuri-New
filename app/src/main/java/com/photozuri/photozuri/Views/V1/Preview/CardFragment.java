package com.photozuri.photozuri.Views.V1.Preview;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
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

import java.util.ArrayList;


public class CardFragment extends Fragment {

    private CardView cardView;
    private MyImage myImage;
    //private DbOperations dbOperations;
    //private FragmentQuestion f;
    private Button finish;
    private boolean lastOne;
    private Dialog dialog;
    // TouchyWebView webView;

    public static Fragment getInstance(int position, ArrayList<MyImage> myImage, int lastOne) {
        CardFragment f = new CardFragment();
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
        View view = inflater.inflate(R.layout.preview_card, container, false);
        cardView = view.findViewById(R.id.cardView);
        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);

        int position = getArguments().getInt("position");
        TextView title = view.findViewById(R.id.title);

        title.setText("Page : " + (position + 1));

        ArrayList<MyImage> images = (ArrayList<MyImage>) getArguments().getSerializable("data");

        for (MyImage image : images) {
            Log.d("imagesasof", image.getPath() + " \n  " + image.getName());
        }

        ImageView imageView1 = view.findViewById(R.id.image_thumbnail1);
        ImageView imageView2 = view.findViewById(R.id.image_thumbnail2);
        ImageView imageView3 = view.findViewById(R.id.image_thumbnail3);
        ImageView imageView4 = view.findViewById(R.id.image_thumbnail4);
//        ImageView imageView5 =view.findViewById(R.id.image_thumbnail5);
//        ImageView imageView6 =view.findViewById(R.id.image_thumbnail6);
//        ImageView imageView7 =view.findViewById(R.id.image_thumbnail7);
//        ImageView imageView8 =view.findViewById(R.id.image_thumbnail8);
//        ImageView imageView9 =view.findViewById(R.id.image_thumbnail9);


        try {
            if (images.get(0) != null) {

                setImage(images.get(0).getPath(), imageView1);
            } else {
                imageView1.setVisibility(View.GONE);
            }


            if (images.get(1) != null) {

                setImage(images.get(1).getPath(), imageView2);
            } else {
                imageView2.setVisibility(View.GONE);
            }


            if (images.get(2) != null) {

                setImage(images.get(2).getPath(), imageView3);
            } else {
                imageView2.setVisibility(View.GONE);
            }


            if (images.get(3) != null) {

                setImage(images.get(3).getPath(), imageView4);
            } else {
                imageView3.setVisibility(View.GONE);
            }
//            if (images.get(4) != null) {
//
//                setImage(images.get(4).getPath(), imageView5);
//            }
//            if (images.get(5) != null) {
//
//                setImage(images.get(5).getPath(), imageView6);
//            }
//            if (images.get(6) != null) {
//
//                setImage(images.get(6).getPath(), imageView7);
//            }
//            if (images.get(7) != null) {
//
//                setImage(images.get(7).getPath(), imageView8);
//            }
//            if (images.get(8) != null) {
//
//                setImage(images.get(8).getPath(), imageView9);
//            }
        } catch (Exception nm) {
            nm.printStackTrace();
        }


        return view;
    }

    private void setImage(String path, ImageView imageView) {
        RequestOptions cropOptions = new RequestOptions().fitCenter();

        Glide.with(getContext())

                .load(path)

                //.centerCrop()
                //.placeholder(R.drawable.thumbnail_placeholder)
                // .error(R.drawable.thumbnail_placeholder)
                //.apply(options)
                .apply(cropOptions)
                .into(imageView);


//        Glide.with(getContext())
//                .load(path)
//                .centerCrop()
//                .placeholder(R.drawable.thumbnail_placeholder)
//                .error(R.drawable.thumbnail_placeholder)
//                .into(holder.img_cardview_images_edit_image);
    }


    public CardView getCardView() {
        return cardView;
    }

}
