package com.photozuri.photozuri.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.photozuri.photozuri.Data.Models.ProductsModel;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/8/2018.
 */

public class ProductsAdapter extends BaseAdapter {
    private List<ProductsModel> images;
    private Context context;
    private GeneralUtills generalUtills;
    private RequestOptions options;

    public ProductsAdapter(Context context, List<ProductsModel> images) {
        this.images = images;
        this.context = context;
        generalUtills = new GeneralUtills(context);
        this.options = (new RequestOptions())
                .placeholder(R.drawable.imagepicker_image_placeholder)
                .error(R.drawable.imagepicker_image_placeholder)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

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
        final ProductsModel image = images.get(position);

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.product_item, null);
        }
        final ImageView imageView = convertView.findViewById(R.id.image);

        final TextView title = convertView.findViewById(R.id.title);
        final TextView subtitle = convertView.findViewById(R.id.subtitle);


        title.setText(image.getCategory());
        subtitle.setText(image.getDescription());
//        Glide.with(context)
//                .load(image.getImage())
//
//                //.apply(options)
//                .into(imageView);


        Picasso.with(context).load(image.getImage()).fit().into(imageView);


        return convertView;
    }

    public void setData(ArrayList<ProductsModel> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    public void addData(ArrayList<ProductsModel> images) {

        if (this.images != null && this.images.size() > 0) {
            this.images.addAll(images);


        } else if (this.images != null && this.images.size() < 1) {
            this.images.clear();
            this.images = images;

        }
        notifyDataSetChanged();
    }
}
