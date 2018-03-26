package com.photozuri.photozuri.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.imagepicker.model.Image;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/8/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Image> images;
    private LayoutInflater inflater;
    private RequestOptions options;
    private OnclickRecyclerListener recyclerListener;

    public ImageAdapter(Context context,OnclickRecyclerListener listener) {
        this.context = context;
        this.recyclerListener=listener;
        inflater = LayoutInflater.from(context);
        images = new ArrayList<>();
        this.options = (new RequestOptions())
                .placeholder(R.drawable.imagepicker_image_placeholder)
                .error(R.drawable.imagepicker_image_placeholder)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      //  return new ImageViewHolder(inflater.inflate(R.layout.item_image, parent, false));

        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
       // return new MyViewHolder(itemView, onclickRecyclerListener);
        return new ImageViewHolder(itemView, recyclerListener);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        final Image image = images.get(position);

//        Glide.with(context)
//                .load(image.getPath())
//                .apply(options)
//                .into(holder.imageView);
        Glide.with(context)
                .load(image.getPath())
                .apply(options)
                .into(holder.imageView);

        if (GeneralUtills.isBelowRightResolution(image.getPath())) {
            holder.txtQuality.setVisibility(View.VISIBLE);
        }


//        Picasso.with(context)
//                .load(image.getPath())
//                .fit()
//                .placeholder(R.drawable.image_placeholder)
//                .error(R.drawable.image_placeholder)
//                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setData(List<Image> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView txtQuality;
        private WeakReference<OnclickRecyclerListener> listenerWeakReference;


        public ImageViewHolder(View itemView,OnclickRecyclerListener listener) {
            super(itemView);
            listenerWeakReference = new WeakReference<>(listener);

            txtQuality = itemView.findViewById(R.id.txt_quality);
            imageView = itemView.findViewById(R.id.image_thumbnail);
            imageView.setOnClickListener(this);
        }

        public ImageViewHolder(View inflate) {
            super(inflate);
        }

        @Override
        public void onClick(View v) {
            //new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, imageView, images.get(getAdapterPosition()), null);


             listenerWeakReference.get().onClickListener(getAdapterPosition(),imageView);

        }
    }
}
