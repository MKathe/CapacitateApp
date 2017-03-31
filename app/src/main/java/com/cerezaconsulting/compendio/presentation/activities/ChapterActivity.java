package com.cerezaconsulting.compendio.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseActivity;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.presentation.fragments.ChapterFragment;
import com.cerezaconsulting.compendio.presentation.presenters.ChapterPresenter;
import com.cerezaconsulting.compendio.utils.ActivityUtils;

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
