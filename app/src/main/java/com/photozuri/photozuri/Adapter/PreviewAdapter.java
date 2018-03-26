package com.photozuri.photozuri.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.MyImageViewHolder> {

    private Context context;
    private ArrayList<MyImage> images;

    private RequestOptions options;
    private OnclickRecyclerListener recyclerListener;
    private int type=0;

    public PreviewAdapter(Context context, ArrayList<MyImage> myImages, OnclickRecyclerListener listener,int type) {
        this.context = context;
        this.recyclerListener = listener;

        this.type=type;
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

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_book_page, parent, false);
        return new MyImageViewHolder(itemView, recyclerListener);

    }

    @Override
    public void onBindViewHolder(MyImageViewHolder holder, int position) {
        final MyImage image = images.get(position);

        Glide.with(context)
                // .load(com.photozuri.photozuri.NetworkUtills.ApiConstants.categoryimgurl + image.getImageurl())
                .load(image.getPath())
                .apply(options)
                .into(holder.imageView);



        holder.date.setText(image.getCreatonTime());
        holder.page.setText("pg "+String.valueOf(position+1));
        holder.caption.setText(image.getCaption());

        if(position%2==0){
            holder.cardView.setBackgroundResource(R.drawable.page_right);
        }
        if(type>0){

            holder.page.setVisibility(View.GONE);
        }
        if(GeneralUtills.isBelowRightResolution(image.getPath())){
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
        //images.get(position).setCaption(image.getCaption());
        notifyItemChanged(position);

    }

    public class MyImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView, imageViewdelete;
        TextView date, caption,page,txtQuality;
        RelativeLayout cardView;
        private WeakReference<OnclickRecyclerListener> listenerWeakReference;


        public MyImageViewHolder(View itemView, OnclickRecyclerListener listener) {
            super(itemView);
            listenerWeakReference = new WeakReference<>(listener);
            txtQuality=itemView.findViewById(R.id.txt_quality);

            imageView = itemView.findViewById(R.id.image);

            date = itemView.findViewById(R.id.date);
            page = itemView.findViewById(R.id.page);
            caption = itemView.findViewById(R.id.caption);
            cardView = itemView.findViewById(R.id.linear);
            //cardView.setOnClickListener(this);

            //imageView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            //new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, imageView, images.get(getAdapterPosition()), null);

            listenerWeakReference.get().onClickListener(getAdapterPosition());

        }
    }
}
