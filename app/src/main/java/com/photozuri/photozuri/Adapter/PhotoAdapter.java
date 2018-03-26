package com.photozuri.photozuri.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.GeneralUtills;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/8/2018.
 */

public class PhotoAdapter extends BaseAdapter {
    private List<MyImage> images;
    private Context context;
    private GeneralUtills generalUtills;
    private RequestOptions options;
    private Boolean SHOW_QUALITY = true;


    public PhotoAdapter(Context context, List<MyImage> images, boolean SHOW_QUALITY) {
        this.images = images;
        this.context = context;
        generalUtills = new GeneralUtills(context);
        this.SHOW_QUALITY = SHOW_QUALITY;
        this.options = (new RequestOptions())
                .placeholder(R.drawable.imagepicker_image_placeholder)
                .error(R.drawable.imagepicker_image_placeholder)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

    }

    public PhotoAdapter(Context context, List<MyImage> images) {
        this.images = images;
        this.context = context;
        generalUtills = new GeneralUtills(context);

        this.options = (new RequestOptions())
                .placeholder(R.drawable.imagepicker_image_placeholder)
                .error(R.drawable.imagepicker_image_placeholder)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

    }


    //public ImageLoader() {
    //}
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
        final MyImage image = images.get(position);

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_image, null);
        }
        final ImageView imageView = convertView.findViewById(R.id.image_thumbnail);



        Glide.with(context)
                .load(image.getPath())
                .apply(options)
                .into(imageView);
        final TextView txtQuality = convertView.findViewById(R.id.txt_quality);

        txtQuality.setVisibility(SHOW_QUALITY ? GeneralUtills.isBelowRightResolution(image.getPath()) ? View.VISIBLE : View.GONE : View.GONE);

        return convertView;
    }

    public void setData(ArrayList<MyImage> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    public void addData(ArrayList<MyImage> images) {

        if (this.images != null && this.images.size() > 0) {
            this.images.addAll(images);


        } else if (this.images != null && this.images.size() < 1) {
            this.images.clear();
            this.images = images;

        }
        notifyDataSetChanged();
    }
}
