package com.cerezaconsulting.coreproject.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.cerezaconsulting.coreproject.R;
import com.cerezaconsulting.coreproject.core.BaseActivity;
import com.cerezaconsulting.coreproject.data.events.MessageChapterCompleteEvent;
import com.cerezaconsulting.coreproject.data.model.CourseEntity;
import com.cerezaconsulting.coreproject.presentation.adapters.CardFragmentPagerAdapter;
import com.cerezaconsulting.coreproject.presentation.fragments.ChapterFragment;
import com.cerezaconsulting.coreproject.presentation.presenters.ChapterPresenter;
import com.cerezaconsulting.coreproject.utils.ActivityUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguel on 16/03/17.
 */

public class ChapterActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        CourseEntity courseEntity = new CourseEntity();
        if (getIntent().hasExtra("course")) {
            courseEntity = (CourseEntity) getIntent().getSerializableExtra("course");
        }

        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(courseEntity.getName());

        ChapterFragment fragment = (ChapterFragment) getSupportFragmentManager().findFragmentById(R.id.body);
        if (fragment == null) {
            fragment = ChapterFragment.newInstance(getIntent().getExtras());
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.body);
        }

        new ChapterPresenter(fragment, getApplicationContext());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
