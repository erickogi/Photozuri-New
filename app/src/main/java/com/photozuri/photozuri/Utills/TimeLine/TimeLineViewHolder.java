package com.photozuri.photozuri.Utills.TimeLine;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.photozuri.photozuri.R;
import com.photozuri.photozuri.Utills.UtilListeners.OnclickRecyclerListener;
import com.github.vipulasri.timelineview.TimelineView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_album_desc)
    TextView mDesc;

    @BindView(R.id.text_timeline_date)
    TextView mDate;


    @BindView(R.id.text_album_title)
    TextView mMessage;

    @BindView(R.id.text_no_of_photos)
    TextView mNoOfPhotos;

    @BindView(R.id.img_cover)
    ImageView mCover;



    @BindView(R.id.time_marker)

    TimelineView mTimelineView;

    //    @BindView(R.id.card)
//
    CardView mCard;
    TextView txtStatus;


    private WeakReference<OnclickRecyclerListener> listenerWeakReference;


    public TimeLineViewHolder(View itemView, int viewType, OnclickRecyclerListener listener) {
        super(itemView);
        mCard = itemView.findViewById(R.id.card);
        txtStatus = itemView.findViewById(R.id.tv_order_state);
        listenerWeakReference = new WeakReference<OnclickRecyclerListener>(listener);


        mCard.setOnClickListener(v -> listenerWeakReference.get().onClickListener(getAdapterPosition()));
        mCard.setOnLongClickListener(v -> {
            listenerWeakReference.get().onLongClickListener(getAdapterPosition());
            return false;
        });

        ButterKnife.bind(this, itemView);


        mTimelineView.initLine(viewType);
    }
}
