package com.cerezaconsulting.coreproject.presentation.adapters;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;

import com.cerezaconsulting.coreproject.data.model.ChapterEntity;

public interface CardAdapter {

    public final int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);


    int getCount();
}