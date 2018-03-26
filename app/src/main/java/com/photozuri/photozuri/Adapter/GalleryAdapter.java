package com.photozuri.photozuri.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.imagepicker.model.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/8/2018.
 */

public class GalleryAdapter extends BaseAdapter {
    private List<Image> images;
    private Context context;

    public GalleryAdapter(Context context, List<Image> images) {
        this.images = images;
        this.context=context;
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
        final Image image = images.get(position);

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_image, null);
        }
        final ImageView imageView = convertView.findViewById(R.id.image_thumbnail);


       // imageView.setImageResource(book.getImageResource());

        Glide.with(context)
                .load(image.getPath())
                //.apply(options)
                .into(imageView);
        return convertView;
    }

    public void setData(ArrayList<Image> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    public void addData(ArrayList<Image> images) {

        if (this.images != null && this.images.size() > 0) {
            this.images.addAll(images);


        } else if (this.images != null && this.images.size() < 1) {
            this.images.clear();
            this.images = images;

        }
        notifyDataSetChanged();
    }
}
