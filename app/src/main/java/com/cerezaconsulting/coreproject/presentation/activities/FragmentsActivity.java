package com.cerezaconsulting.coreproject.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.cerezaconsulting.coreproject.R;
import com.cerezaconsulting.coreproject.core.BaseActivity;
import com.cerezaconsulting.coreproject.data.model.ChapterEntity;
import com.cerezaconsulting.coreproject.data.model.CourseEntity;
import com.cerezaconsulting.coreproject.presentation.fragments.ChapterFragment;
import com.cerezaconsulting.coreproject.presentation.fragments.FragmentFragment;
import com.cerezaconsulting.coreproject.presentation.presenters.ChapterPresenter;
import com.cerezaconsulting.coreproject.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguel on 16/03/17.
 */

public class FragmentsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);



        ChapterEntity courseEntity = new ChapterEntity();
        if (getIntent().hasExtra("chapter")) {
            courseEntity = (ChapterEntity) getIntent().getSerializableExtra("chapter");
        }

        toolbar.setTitle(courseEntity.getName());
        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        ab.setTitle(courseEntity.getName());

        FragmentFragment fragment = (FragmentFragment) getSupportFragmentManager().findFragmentById(R.id.body);
        if (fragment == null) {
            fragment = FragmentFragment.newInstance(getIntent().getExtras());
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.body);
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
