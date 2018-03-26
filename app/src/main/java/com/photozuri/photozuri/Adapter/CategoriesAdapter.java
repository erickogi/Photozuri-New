package com.photozuri.photozuri.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.photozuri.photozuri.Data.Models.ProductsModel;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/8/2018.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyImageViewHolder> {

    private Context context;
    private ArrayList<ProductsModel> images;

    private RequestOptions options;
    private OnclickRecyclerListener recyclerListener;

    public CategoriesAdapter(Context context, ArrayList<ProductsModel> myImages, OnclickRecyclerListener listener) {
        this.context = context;
        this.recyclerListener = listener;

        images = myImages;
        this.options = (new RequestOptions())
                .placeholder(R.drawable.imagepicker_image_placeholder)
                .error(R.drawable.imagepicker_image_placeholder)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

    }

//    @Override
//    public MyImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//      //  return new MyImageViewHolder(inflater.inflate(R.layout.item_image, parent, false));
//
//        View itemView = null;
//
//        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item_pps, parent, false);
//       // return new MyViewHolder(itemView, onclickRecyclerListener);
//        return new MyImageViewHolder(itemView, recyclerListener);
//    }

    @Override
    public MyImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new MyImageViewHolder(itemView, recyclerListener);

    }

    @Override
    public void onBindViewHolder(MyImageViewHolder holder, int position) {
        final ProductsModel image = images.get(position);

        String url = com.photozuri.photozuri.NetworkUtills.ApiConstants.categoryimgurl + "" + image.getImageurl();
        Log.d("logima", url);
        Glide.with(context)
                // .load(com.photozuri.photozuri.NetworkUtills.ApiConstants.categoryimgurl + image.getImageurl())
                .load(url)
                .apply(options)
                .into(holder.imageView);


        holder.title.setText(image.getCategory());
        holder.desc.setText(image.getDescription());


    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setData(List<ProductsModel> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    public void setDataChange(int position, ProductsModel image) {
        //images.get(position).setCaption(image.getCaption());
        notifyItemChanged(position);

    }

    public class MyImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView, imageViewdelete;
        TextView title, desc;
        CardView cardView;
        private WeakReference<OnclickRecyclerListener> listenerWeakReference;


        public MyImageViewHolder(View itemView, OnclickRecyclerListener listener) {
            super(itemView);
            listenerWeakReference = new WeakReference<>(listener);

            imageView = itemView.findViewById(R.id.image);

            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.subtitle);
            cardView = itemView.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);

            //imageView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            //new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, imageView, images.get(getAdapterPosition()), null);

            listenerWeakReference.get().onClickListener(getAdapterPosition());

        }
    }
}
