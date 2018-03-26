package com.photozuri.photozuri.Views.V1.Preview;

import android.support.v7.widget.CardView;

/**
 * Created by Eric on 1/26/2018.
 */

public interface WmtsCardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
