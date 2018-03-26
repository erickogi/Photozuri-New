package com.photozuri.photozuri.Views.V1.Usables;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.photozuri.photozuri.Data.Models.GDModel;
import com.photozuri.photozuri.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Eric on 1/10/2018.
 */

public class GridPopUpAdapter
    extends BaseAdapter

    {
        GDController GDController;
        ArrayList<GDModel> gdModels;
        Context mContext;
        private LayoutInflater mInflater;
        private RelativeLayout.LayoutParams mImageViewLayoutParams;
        private int mItemHeight = 0;
        private int mNumColumns = 0;

        public GridPopUpAdapter(Context context, ArrayList<GDModel> gdModels) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        mImageViewLayoutParams = new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
       // GDController = _GDController;
        this.gdModels=gdModels;
        this.mContext = context;
    }

        /**
         * Fetch the count of images in the results
         *
         * @return
         */
    public int getCount() {
        return gdModels.size();
    }

    /**
     * Get the number of columns in the grid
     *
     * @return
     */
    public int getNumColumns() {
        return mNumColumns;
    }

        /**
         * Set the number of columns in the grid
         *
         * @param numColumns
         */
        public void setNumColumns(int numColumns) {
            mNumColumns = numColumns;
        }

    /**
     * Set the height of image item
     *
     * @param height
     */
    public void setItemHeight(int height) {
        if (height == mItemHeight) {
            return;
        }
//        mItemHeight = height;
//        mImageViewLayoutParams = new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);
        notifyDataSetChanged();
    }

    /**
     * Return the GoogleDriveModel instance corresponding to the item at
     * given position
     *
     * @param position
     * @return
     */
    public GDModel getItem(int position) {
        return gdModels.get(position);
    }

    /**
     * Implement the method in interface android.widget.Adapter
     * @param position
     * @return
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Create the thumbnail view that is to be shown in a given position of the grid
     *
     * @param position
     * @param view
     * @param parent
     * @return
     */
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_image, null);
            holder.imgSelect = view.findViewById(R.id.image_select);
            holder.thumbnail = view.findViewById(R.id.image_thumbnail);
            //holder.title = (TextView) view.findViewById(R.id.title);
            // holder.tv_imagelink = (TextView) view.findViewById(R.id.tv_imagelink);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

       // holder.thumbnail.setLayoutParams(mImageViewLayoutParams);

        // Check the height matches our calculated column width
//        if (holder.thumbnail.getLayoutParams().height != mItemHeight) {
//            holder.thumbnail.setLayoutParams(mImageViewLayoutParams);
//        }


        GDModel photo = getItem(position);
        Picasso.with(parent.getContext())
                .load(photo.getThumbnailLink())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder.thumbnail);
        if (photo.isSelected()) {
            holder.thumbnail.setAlpha(0.4f);
            holder.imgSelect.setVisibility(View.VISIBLE);
        }
        // holder.thumbnail.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //holder.title.setText(photo.title);
        // holder.tv_imagelink.setText(photo.thumbnailLink );
        // AppConfig.setAnimation(mContext, view, 1);
        return view;
    }

    /**
     * A holder for the view. Currently has only the thumbnail enabled as per requirements.
     * The title can be used to handle the image title.
     */
    class ViewHolder {
        ImageView thumbnail, imgSelect;
        TextView title;
        TextView tv_imagelink;
    }
}
