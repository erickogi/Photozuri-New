package com.photozuri.photozuri.Utills;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kogi Shop on 8/14/2017.
 */

public class ItemOffsetDecorationV2 extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public ItemOffsetDecorationV2(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public ItemOffsetDecorationV2(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}
