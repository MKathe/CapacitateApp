package com.cerezaconsulting.compendio.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseActivity;
import com.cerezaconsulting.compendio.data.model.ChapterEntity;
import com.cerezaconsulting.compendio.presentation.fragments.QuestionFragment;
import com.cerezaconsulting.compendio.utils.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguel on 16/03/17.
 */

public class QuestionActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);


        ChapterEntity chapterEntity = new ChapterEntity();
        if (getIntent().hasExtra("chapter")) {
            chapterEntity = (ChapterEntity) getIntent().getSerializableExtra("chapter");
        }

        showDisplayHowEnabled(true, chapterEntity.getName());
        QuestionFragment fragment = (QuestionFragment) getSupportFragmentManager().findFragmentById(R.id.body);
        if (fragment == null) {
            fragment = QuestionFragment.newInstance(getIntent().getExtras());
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.body);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showDisplayHowEnabled(boolean active, String title) {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(active);
        ab.setDefaultDisplayHomeAsUpEnabled(active);
        ab.setTitle(title);
    }
}
