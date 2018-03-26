package com.photozuri.photozuri.Adapter;

import android.content.Context;
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
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/8/2018.
 */

public class MyImageAdapterSingle extends RecyclerView.Adapter<MyImageAdapterSingle.MyImageViewHolder> {

    private Context context;
    private ArrayList<MyImage> images;

    private RequestOptions options;
    private OnclickRecyclerListener recyclerListener;

    public MyImageAdapterSingle(Context context, ArrayList<MyImage> myImages, OnclickRecyclerListener listener) {
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

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new MyImageViewHolder(itemView, recyclerListener);

    }

    @Override
    public void onBindViewHolder(MyImageViewHolder holder, int position) {
        final MyImage image = images.get(position);

        Glide.with(context)
                .load(image.getPath())
                .apply(options)
                .into(holder.imageView);

        if (image.getCaption() != null) {

            holder.textViewCaption.setText(image.getCaption());
        }

        if (GeneralUtills.isBelowRightResolution(image.getPath())) {
            holder.txtQuality.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setData(List<MyImage> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    public void setDataChange(int position, MyImage image) {
        images.get(position).setCaption(image.getCaption());
        notifyItemChanged(position);

    }

    public class MyImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView, imageViewdelete;
        TextView textViewCaption, txtQuality;
        private WeakReference<OnclickRecyclerListener> listenerWeakReference;


        public MyImageViewHolder(View itemView, OnclickRecyclerListener listener) {
            super(itemView);
            txtQuality = itemView.findViewById(R.id.txt_quality);
            listenerWeakReference = new WeakReference<>(listener);

            imageView = itemView.findViewById(R.id.image_thumbnail);
            imageViewdelete = itemView.findViewById(R.id.deleteIV);
            imageViewdelete.setOnClickListener(this);
            textViewCaption = itemView.findViewById(R.id.text);

            imageView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            //new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, imageView, images.get(getAdapterPosition()), null);


            Log.d("viewclick", "VIEW CLIKED");
            if (v.getId() == R.id.deleteIV) {
                listenerWeakReference.get().onLongClickListener(getAdapterPosition());
            } else if (v.getId() == R.id.image_thumbnail) {
                listenerWeakReference.get().onClickListener(getAdapterPosition());
            }

        }
    }
}
