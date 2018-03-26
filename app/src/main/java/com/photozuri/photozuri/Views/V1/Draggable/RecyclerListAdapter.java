/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.photozuri.photozuri.Views.V1.Draggable;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.photozuri.photozuri.Data.Models.MyImage;
import com.photozuri.photozuri.GlobalConsts;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.GeneralUtills;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.Views.V1.Draggable.helper.ItemTouchHelperAdapter;
import com.photozuri.photozuri.Views.V1.Draggable.helper.ItemTouchHelperViewHolder;
import com.photozuri.photozuri.Views.V1.Draggable.helper.OnStartDragListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Simple RecyclerView.Adapter that implements {@link ItemTouchHelperAdapter} to respond to move and
 * dismiss events from a {@link android.support.v7.widget.helper.ItemTouchHelper}.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private final List<String> mItems = new ArrayList<>();
    private final OnStartDragListener mDragStartListener;
    private Context context;
    private ArrayList<MyImage> myImages;
    private OnclickRecyclerListener onclickRecyclerListener;
    private GeneralUtills generalUtills;

    public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener, ArrayList<MyImage> myImages, OnclickRecyclerListener onclickRecyclerListener) {
        mDragStartListener = dragStartListener;
        this.myImages = myImages;
        this.context = context;
        generalUtills = new GeneralUtills(context);
        this.onclickRecyclerListener = onclickRecyclerListener;
        //mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view, onclickRecyclerListener);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {


        MyImage image = myImages.get(position);
        holder.textView.setText(image.getCaption());
//        if (image.getImageFrom() == Constants.GALLERY) {
//            Glide.with(context)
//                    .load(image.getPath())
//                    //.apply(options)
//                    .into(holder.handleView);
//
//
//            Log.d("drawingbitp", "Local fromgalllery " + image.getPath());
//        } else if (image.isDownloadedComplete() && image.getImageFrom() != Constants.GALLERY) {
//
//            Bitmap b = generalUtills.loadImageFromStorage(image.getName());
//            ///holder.handleView.setImageBitmap(b);
//            Glide.with(context)
//                    .load(image.getPath())
//                    //.apply(options)
//                    .into(holder.handleView);
//
//            Log.d("drawingbitp", "Local isdownloadcomplete " + image.getPath()+""+image.getName());
//
//
//        } else if (image.isDownloadedInComplete() && image.getImageFrom() != Constants.GALLERY && !image.isDownloadedComplete()) {
//
//            Glide.with(context)
//                    .load(image.getPath())
//                    //.apply(options)
//                    .into(holder.handleView);
//
//
//            Log.d("drawingbitp", "Online isdownloadincomplete " + image.getPath());
//
//
//        }

        Glide.with(context)
                .load(image.getPath())
                //.apply(options)
                .into(holder.handleView);

        if (GeneralUtills.isBelowRightResolution(image.getPath())) {
            holder.txtQuality.setVisibility(View.VISIBLE);
        }


//        if (image.getImageFrom() == Constants.GALLERY) {
//            Glide.with(context)
//                    .load(image.getPath())
//                    //.apply(options)
//                    .into(imageView);
//
//
//            Log.d("drawingbit", "Local fromgalllery " + image.getPath());
//        } else if (image.isDownloadedComplete() && image.getImageFrom() != Constants.GALLERY) {
//
//            Bitmap b = generalUtills.loadImageFromStorage(image.getName());
//            imageView.setImageBitmap(b);
//
//
//            Log.d("drawingbit", "Local isdownloadcomplete " + image.getPath());
//
//
//        } else if (image.isDownloadedInComplete() && image.getImageFrom() != Constants.GALLERY && !image.isDownloadedComplete()) {
//
//            Glide.with(context)
//                    .load(image.getPath())
//                    //.apply(options)
//                    .into(imageView);
//
//
//            Log.d("drawingbit", "Online isdownloadincomplete " + image.getPath());
//
//
//        }



        // Start a drag whenever the handle view it touched
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
        holder.handleView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mDragStartListener.onStartDrag(holder);
                return false;
            }
        });
        // holder.handleView.setOn
    }

    @Override
    public void onItemDismiss(int position) {
        myImages.remove(position);


        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        try {

            Collections.swap(myImages, fromPosition, toPosition);
            GlobalConsts.myImages = myImages;
            notifyItemMoved(fromPosition, toPosition);
        } catch (Exception ignored) {

        }


        return true;
    }

    public void setData(ArrayList<MyImage> images) {
        this.myImages.clear();
        if (images != null) {
            this.myImages.addAll(images);
        }


        notifyDataSetChanged();
    }

    public void setDataChange(int position, MyImage image) {
        myImages.get(position).setCaption(image.getCaption());
        notifyItemChanged(position);

    }

    @Override
    public int getItemCount() {
        return myImages.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            // ItemTouchHelperViewHolder,
            View.OnClickListener, View.OnLongClickListener {

        public final TextView textView;
        public ImageView handleView, favDel;
        TextView txtQuality;
        private WeakReference<OnclickRecyclerListener> listenerWeakReference;


        public ItemViewHolder(View itemView, OnclickRecyclerListener onclickRecyclerListener) {
            super(itemView);

            txtQuality = itemView.findViewById(R.id.txt_quality);
            favDel = itemView.findViewById(R.id.deleteIV);
            listenerWeakReference = new WeakReference<OnclickRecyclerListener>(onclickRecyclerListener);

            textView = itemView.findViewById(R.id.text);
            textView.setOnClickListener(this);


            handleView = itemView.findViewById(R.id.image_thumbnail);
            handleView.setOnClickListener(this);
            handleView.setOnLongClickListener(this);
            favDel.setOnClickListener(this);
        }


//        @Override
//        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.LTGRAY);
//
//           // listenerWeakReference.get().onClickListener(getAdapterPosition());
//        }
//
//
////        @Override
////        public void onItemSelected() {
////
////        }
////
//        @Override
//        public void onItemClear() {
//            itemView.setBackgroundColor(0);
//        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.deleteIV) {
                listenerWeakReference.get().onLongClickListener(getAdapterPosition());
            } else if (v.getId() == R.id.image_thumbnail) {

                listenerWeakReference.get().onClickListener(getAdapterPosition());
            } else if (v.getId() == R.id.text) {

                listenerWeakReference.get().onClickListener(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            listenerWeakReference.get().onClickListener(getAdapterPosition());
            return false;
        }
    }
}
