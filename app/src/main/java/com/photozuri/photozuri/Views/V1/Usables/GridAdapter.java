package com.photozuri.photozuri.Views.V1.Usables;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.photozuri.photozuri.Data.Models.GDModel;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.UtilListeners.AppOnclickRecyclerListener;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Eric on 2/19/2018.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ImageViewHolder> {

    ArrayList<GDModel> gdModels;
    private Context context;
    private LayoutInflater inflater;
    private RequestOptions options;
    private AppOnclickRecyclerListener recyclerListener;

    public GridAdapter(Context context, ArrayList<GDModel> gdModels, AppOnclickRecyclerListener recyclerListener) {
        this.context = context;
        this.gdModels = gdModels;
        this.recyclerListener = recyclerListener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        // return new MyViewHolder(itemView, onclickRecyclerListener);
        return new ImageViewHolder(itemView, recyclerListener);
    }

    public void setData(ArrayList<GDModel> images) {
        this.gdModels.clear();
        if (images != null) {
            this.gdModels.addAll(images);
        }
        notifyDataSetChanged();
    }

    public void setDataChange(int position, GDModel image) {
        gdModels.get(position).setSelected(image.isSelected());
        notifyItemChanged(position);

//        public void updateItemItem(int position, StockItemsPojo stockItemsPojo) {
//            modelList.get(position).setItem_quantity(stockItemsPojo.getItem_quantity());
//            notifyItemChanged(position);
//        }

    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {


        GDModel photo = gdModels.get(position);
        Picasso.with(context)
                .load(photo.getThumbnailLink())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.thumbnail);
        if (photo.isSelected()) {
            holder.thumbnail.setAlpha(0.4f);
            holder.imgSelect.setVisibility(View.VISIBLE);
        } else {
            holder.thumbnail.setAlpha(0.9f);
            holder.imgSelect.setVisibility(View.GONE);
        }
//        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //GDModel gdModel=gdModels.get(position);
//                if (photo.isSelected()) {
//
//
//                    //fromSelected(gdModels.get(position));
//
//
//                    holder.thumbnail.setAlpha(1f);
//                    gdModels.get(position).setSelected(false);
//                    //gdModel.setSelected(false);
//                    //gridAdapter.setDataChange(position,gdModel);
//
//
//                    holder.imgSelect.setVisibility(View.GONE);
//                } else {
//                    photo.setSelected(true);
//
//                    gdModels.get(position).setSelected(true);
//                   // toSelected(gdModels.get(position));
//                   // gdModel.setSelected(true);
//                   // gridAdapter.setDataChange(position,gdModel);
//
//                    //gridAdapter.setData(gdModels);
//
//                   // holder.thumbnail.setAlpha(0.4f);
//
//                    holder.imgSelect.setVisibility(View.VISIBLE);
//                }
//            }
//        });


    }

    @Override

    public int getItemCount() {
        return gdModels.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView thumbnail, imgSelect;
        private WeakReference<AppOnclickRecyclerListener> listenerWeakReference;


        public ImageViewHolder(View itemView, AppOnclickRecyclerListener listener) {
            super(itemView);
            listenerWeakReference = new WeakReference<>(listener);

            thumbnail = itemView.findViewById(R.id.image_thumbnail);
            imgSelect = itemView.findViewById(R.id.image_select);

            // thumbnail.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

//            GDModel gdModel=gdModels.get(position);
//            if (gdModels.get(position).isSelected()) {
//
//
//                fromSelected(gdModels.get(position));
//
//
//                //thumbnail.setAlpha(1f);
//                gdModels.get(position).setSelected(false);
//                gdModel.setSelected(false);
//                gridAdapter.setDataChange(position,gdModel);
//
//
//                // imgSelect.setVisibility(View.GONE);
//            } else {
//                gdModels.get(position).setSelected(true);
//
//
//                toSelected(gdModels.get(position));
//                gdModel.setSelected(true);
//                gridAdapter.setDataChange(position,gdModel);

            //gridAdapter.setData(gdModels);

            //thumbnail.setAlpha(0.4f);

            // imgSelect.setVisibility(View.VISIBLE);
            //           }


            //   listenerWeakReference.get().onClickListener(getAdapterPosition(),thumbnail,imgSelect);
        }
    }
}
