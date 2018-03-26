package com.photozuri.photozuri.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.photozuri.photozuri.Data.Models.StoryAlbum;
import com.photozuri.photozuri.Data.Models.StoryPhoto;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbContentValues;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/8/2018.
 */

public class StoryAlbumAdapter extends RecyclerView.Adapter<StoryAlbumAdapter.StoryAlbumViewHolder> {

    private Context context;
    private List<StoryAlbum> images;
    private LayoutInflater inflater;
    private RequestOptions options;
    private OnclickRecyclerListener recyclerListener;
    private DbOperations dbOperations;

    public StoryAlbumAdapter(Context context, ArrayList<StoryAlbum> images, OnclickRecyclerListener listener) {
        this.context = context;
        this.recyclerListener = listener;
        inflater = LayoutInflater.from(context);
        dbOperations = new DbOperations(context);
        this.images = images;
        this.options = (new RequestOptions())
                .placeholder(R.drawable.imagepicker_image_placeholder)
                .error(R.drawable.imagepicker_image_placeholder)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        // options = new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder);
    }

    @Override
    public StoryAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //  return new StoryAlbumViewHolder(inflater.inflate(R.layout.item_image, parent, false));

        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.journey_item, parent, false);
        // return new MyViewHolder(itemView, onclickRecyclerListener);
        return new StoryAlbumViewHolder(itemView, recyclerListener);
    }


    @Override
    public void onBindViewHolder(StoryAlbumViewHolder holder, int position) {
        final StoryAlbum image = images.get(position);

        try {
            Cursor c = dbOperations.select(DbConstants.TABLE_SP, DbConstants.SPAlbum_ID, image.getKEY_ID());
            if (c != null) {
                DbContentValues dbContentValues = new DbContentValues();
                ArrayList<StoryPhoto> storyPhotos = dbContentValues.getSavedPhotos(c);
                if (storyPhotos != null && storyPhotos.size() > 0) {
                    image.setCover_image(storyPhotos.get(0).getPath());
                }
            }
        } catch (Exception nm) {
            nm.printStackTrace();
        }

        Glide.with(context)
                .load(image.getCover_image())
                .apply(options)
                .into(holder.imageView);
        holder.count.setText(String.valueOf(dbOperations.getCount(DbConstants.TABLE_SP, DbConstants.SPAlbum_ID, image.getKEY_ID())) + " Photos");
        holder.title.setText(image.getTitle());
        holder.desc.setText(image.getDescription());


//        BitmapDrawable drawable = (BitmapDrawable) holder.imageView.getDrawable();
//        if (drawable != null) {
//            Bitmap bitmap = drawable.getBitmap();
//            if (bitmap != null) {
//                Palette.generateAsync(bitmap, palette -> {
//                    try {
//                        int bgColor = palette.getMutedColor(context.getResources().getColor(R.color.primary));
//
//                        holder.imageView.setBackgroundColor(bgColor);
//
//                    } catch (Exception n) {
//
//                    }
//
//
//                });
//            }
//        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setData(List<StoryAlbum> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    static class StoryAlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView title, count, desc;
        private WeakReference<OnclickRecyclerListener> listenerWeakReference;


        public StoryAlbumViewHolder(View itemView, OnclickRecyclerListener listener) {
            super(itemView);
            listenerWeakReference = new WeakReference<>(listener);

            desc = itemView.findViewById(R.id.txt_desc);
            title = itemView.findViewById(R.id.txt_title);
            count = itemView.findViewById(R.id.txt_count);
            imageView = itemView.findViewById(R.id.image);
            imageView.setOnClickListener(this);
        }

        public StoryAlbumViewHolder(View inflate) {
            super(inflate);
        }

        @Override
        public void onClick(View v) {
            //new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, imageView, images.get(getAdapterPosition()), null);


            listenerWeakReference.get().onClickListener(getAdapterPosition());

        }
    }
}
