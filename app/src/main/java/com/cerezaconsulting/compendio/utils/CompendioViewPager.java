package com.cerezaconsulting.compendio.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class CompendioViewPager extends AutoScrollViewPager {
    private boolean isPagingEnabled = true;

    public CompendioViewPager(Context context) {
        super(context);
    }

    public CompendioViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}