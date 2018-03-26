package com.photozuri.photozuri.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.photozuri.photozuri.Data.Models.PhotoList;
import com.photozuri.photozuri.Data.Models.StoryAlbum;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/8/2018.
 */

public class StoryPhotoDetailAdapter extends RecyclerView.Adapter<StoryPhotoDetailAdapter.StoryPhotoViewHolder> {

    RecyclerView recyclerView;
    StaggeredGridLayoutManager mStaggeredLayoutManager;
    StoryPhotoAdapter storyPhotoAdapter;
    //  private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    Fragment fragment = null;
    ArrayList<StoryAlbum> storyAlbums = new ArrayList<>();
    StoryPhotoAdapter storyPhotoAdapter1;
    private Context context;
    private List<PhotoList> images;
    private LayoutInflater inflater;
    private RequestOptions options;
    private OnclickRecyclerListener recyclerListener;

    public StoryPhotoDetailAdapter(Context context, ArrayList<PhotoList> photoLists, OnclickRecyclerListener listener) {
        this.context = context;
        this.recyclerListener = listener;
        inflater = LayoutInflater.from(context);
        this.images = photoLists;
        this.options = (new RequestOptions())
                .placeholder(R.drawable.imagepicker_image_placeholder)
                .error(R.drawable.imagepicker_image_placeholder)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

    }

    @Override
    public StoryPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //  return new StoryPhotoViewHolder(inflater.inflate(R.layout.item_image, parent, false));

        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.journey_detail_item, parent, false);
        // return new MyViewHolder(itemView, onclickRecyclerListener);
        return new StoryPhotoViewHolder(itemView, recyclerListener);
    }

    @Override
    public void onBindViewHolder(StoryPhotoViewHolder holder, int position) {
        final PhotoList image = images.get(position);
        storyPhotoAdapter1 = new StoryPhotoAdapter(context, image.getStoryPhotos(), new OnclickRecyclerListener() {
            @Override
            public void onClickListener(int position) {

            }

            @Override
            public void onLongClickListener(int position) {

            }

            @Override
            public void onClickListener(int adapterPosition, ImageView imageView) {

            }
        });


        mStaggeredLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        storyPhotoAdapter1.notifyDataSetChanged();

        holder.recyclerView.setLayoutManager(mStaggeredLayoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(storyPhotoAdapter1);


//        PhotoAdapterStory photoAdapter=new PhotoAdapterStory(context,image.getStoryPhotos());
//        Log.d("dateset","o"+image.getDate());
//        holder.date.setText(image.getDate());
//
//        holder.gridView.setAdapter(photoAdapter);


        // imageAdapter =new ImageAdapter(context,n)


//        holder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//             StoryPhoto storyPhoto= image.getStoryPhotos().get(position);
//
//
//            }
//        });


//        Glide.with(context)
//                .load(image.getPath())
//                .apply(options)
//                .into(holder.imageView);
//        Glide.with(context)
//                .load(image.getCover_image())
//                //.apply(options)
//                .into(holder.imageView);
//        holder.count.setText(image.getCount());
//        holder.title.setText(image.getTitle());

        //holder.gridView

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setData(List<PhotoList> images) {
        this.images.clear();
        if (images != null) {
            this.images.addAll(images);
        }
        notifyDataSetChanged();
    }

    static class StoryPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        GridView gridView;
        TextView date;
        RecyclerView recyclerView;
        private WeakReference<OnclickRecyclerListener> listenerWeakReference;


        public StoryPhotoViewHolder(View itemView, OnclickRecyclerListener listener) {
            super(itemView);
            listenerWeakReference = new WeakReference<>(listener);

//            title=itemView.findViewById(R.id.txt_title);
//            count=itemView.findViewById(R.id.txt_count);
//            imageView = itemView.findViewById(R.id.image);
//            imageView.setOnClickListener(this);

            recyclerView = itemView.findViewById(R.id.recycler_view);
            gridView = itemView.findViewById(R.id.gridview);
            date = itemView.findViewById(R.id.txt_date);


        }

        public StoryPhotoViewHolder(View inflate) {
            super(inflate);
        }

        @Override
        public void onClick(View v) {
            //new PhotoFullPopupWindow(getActivity(), R.layout.popup_photo_full, imageView, images.get(getAdapterPosition()), null);


            listenerWeakReference.get().onClickListener(getAdapterPosition());

        }
    }
}
