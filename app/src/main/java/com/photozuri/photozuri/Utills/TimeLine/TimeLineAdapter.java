package com.photozuri.photozuri.Utills.TimeLine;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.photozuri.photozuri.Data.Sqlite.DbConstants;
import com.photozuri.photozuri.Data.Sqlite.DbOperations;
import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.DateTimeUtils;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.photozuri.photozuri.Utills.VectorDrawableUtils;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.List;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<TimeLineModel> mFeedList;
    private Context mContext;
    private Orientation mOrientation;
    private boolean mWithLinePadding;
    private LayoutInflater mLayoutInflater;
    private OnclickRecyclerListener listener;
    private RequestOptions options;
    private boolean isSved = false;
    private DbOperations dbOperations;


    public TimeLineAdapter(List<TimeLineModel> feedList, Orientation orientation, boolean withLinePadding, OnclickRecyclerListener listener) {
        mFeedList = feedList;
        isSved = false;
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
        this.listener = listener;
        this.options = (new RequestOptions())
                .placeholder(R.drawable.imagepicker_image_placeholder)
                .error(R.drawable.imagepicker_image_placeholder)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

    }

    public TimeLineAdapter(boolean isSaved, Context context, List<TimeLineModel> feedList, Orientation orientation, boolean withLinePadding, OnclickRecyclerListener listener) {
        mFeedList = feedList;
        isSaved = isSaved;
        dbOperations = new DbOperations(context);
        mOrientation = orientation;
        mWithLinePadding = withLinePadding;
        this.listener = listener;
        this.options = (new RequestOptions())
                .placeholder(R.drawable.imagepicker_image_placeholder)
                .error(R.drawable.imagepicker_image_placeholder)
                .centerCrop().diskCacheStrategy(DiskCacheStrategy.RESOURCE);

    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view;

        if (mOrientation == Orientation.HORIZONTAL) {
            view = mLayoutInflater.inflate(mWithLinePadding ? R.layout.item_timeline_horizontal_line_padding : R.layout.item_timeline_horizontal, parent, false);
        } else {
            view = mLayoutInflater.inflate(mWithLinePadding ? R.layout.item_timeline_line_padding : R.layout.item_timeline, parent, false);
        }

        return new TimeLineViewHolder(view, viewType, listener);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        TimeLineModel timeLineModel = mFeedList.get(position);

        if (timeLineModel.getOrderstatus() != null) {
            holder.txtStatus.setText(timeLineModel.getOrderstatus());
            holder.txtStatus.setVisibility(View.VISIBLE);
        } else {
            holder.txtStatus.setVisibility(View.GONE);
        }
        if (timeLineModel.getStatus() == OrderStatus.INACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_inactive, android.R.color.darker_gray));
        } else if (timeLineModel.getStatus() == OrderStatus.ACTIVE) {
            holder.mTimelineView.setMarker(VectorDrawableUtils.getDrawable(mContext, R.drawable.ic_marker_active, R.color.colorPrimary));
        } else {
            holder.mTimelineView.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker), ContextCompat.getColor(mContext, R.color.colorPrimary));
        }
        Log.d("dataprint", timeLineModel.getMessage() + "  " + timeLineModel.getTitle());

        if (timeLineModel.getDate() != null && !timeLineModel.getDate().isEmpty()) {
            holder.mDate.setVisibility(View.VISIBLE);
            holder.mDate.setText(DateTimeUtils.parseDateTime(timeLineModel.getDate(), "yyyy-MM-dd HH:mm", "hh:mm a, dd-MMM-yyyy"));
        } else
            holder.mDate.setVisibility(View.GONE);
        holder.mDesc.setText("Desc: " + timeLineModel.getmMessage());

        holder.mMessage.setText("Order: " + timeLineModel.getTitle());


        if (isSved) {
            holder.mNoOfPhotos.setText(dbOperations.getCount(DbConstants.TABLE_SAVED_DATA, DbConstants.TITLE_ID, String.valueOf(timeLineModel.getId())) + " Photos ");
        } else {
            holder.mNoOfPhotos.setText(timeLineModel.getmNoOfPhotos() + " Photos ");
        }
        if (timeLineModel.getmCoverImage() != null) {
            Glide.with(mContext)
                    .load(timeLineModel.getmCoverImage())
                    .apply(options)
                    .into(holder.mCover);
        }


//        Picasso.with(mContext).load(timeLineModel.getmCoverImage()).fit().into(holder.mCover);
    }

    @Override
    public int getItemCount() {
        return (mFeedList != null ? mFeedList.size() : 0);
    }

}
