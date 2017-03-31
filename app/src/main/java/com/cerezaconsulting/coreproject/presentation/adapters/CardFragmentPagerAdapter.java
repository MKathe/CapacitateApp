package com.cerezaconsulting.coreproject.presentation.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.cerezaconsulting.coreproject.data.model.ChapterEntity;
import com.cerezaconsulting.coreproject.data.model.CourseEntity;

import java.util.ArrayList;
import java.util.List;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    private List<CardFragment> fragments;
    private ArrayList<ChapterEntity> chapterEntities = null;
    private float baseElevation;
    private CourseEntity courseEntity;


    public CardFragmentPagerAdapter(CourseEntity courseEntity, FragmentManager supportFragmentManager, float v, ArrayList<ChapterEntity> chapters) {
        super(supportFragmentManager);
        fragments = new ArrayList<>();
        this.baseElevation = v;
        this.chapterEntities = chapters;

        for (int i = 0; i < chapterEntities.size(); i++) {

            addCardFragment(new CardFragment());
        }
        this.courseEntity = courseEntity;
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return fragments.get(position).getCardView();
    }


    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {

        return CardFragment.getInstance(courseEntity, chapterEntities.get(position), chapterEntities);
        /*return CardFragment.getInstance(position);*/
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragment fragment) {
        fragments.add(fragment);
    }

}