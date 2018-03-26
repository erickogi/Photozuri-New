package com.photozuri.photozuri.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.photozuri.photozuri.Data.Models.StoryPhoto;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.GeneralUtills;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/8/2018.
 */

public class PhotoAdapterStory extends BaseAdapter {
    private List<StoryPhoto> images;
    private Context context;
    private GeneralUtills generalUtills;

    public PhotoAdapterStory(Context context, List<StoryPhoto> images) {
        this.images = images;
        this.context = context;
        generalUtills = new GeneralUtills(context);
    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1
        final StoryPhoto image = images.get(position);

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_image, null);
        }
        final ImageView imageView = convertView.findViewById(R.id.image_thumbnail);



        Glide.with(context)
                .load(image.getPath())
                //.apply(options)
                .into(imageView);
        final TextView txtQuality = convertView.findViewById(R.id.txt_quality);

        if (GeneralUtills.isBelowRightResolution(image.getPath())) {
            txtQuality.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void setData(ArrayList<StoryPhoto> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    public void addData(ArrayList<StoryPhoto> images) {

        if (this.images != null && this.images.size() > 0) {
            this.images.addAll(images);


        } else if (this.images != null && this.images.size() < 1) {
            this.images.clear();
            this.images = images;

        }
        notifyDataSetChanged();
    }
}
