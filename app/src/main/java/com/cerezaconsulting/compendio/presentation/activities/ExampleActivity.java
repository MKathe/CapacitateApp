package com.cerezaconsulting.compendio.presentation.activities;

import android.os.Bundle;


import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseActivity;
import com.cerezaconsulting.compendio.presentation.fragments.ExampleFragment;
import com.cerezaconsulting.compendio.presentation.presenters.ExamplePresenter;
import com.cerezaconsulting.compendio.utils.ActivityUtils;

import butterknife.ButterKnife;

public class ExampleActivity extends BaseActivity {

    /*@BindView(R.id.toolbar)
    Toolbar toolbar;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);
        ButterKnife.bind(this);

     /*   toolbar.setTitle("Ejemplo");

        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
*/
        ExampleFragment fragment = (ExampleFragment) getSupportFragmentManager()
                .findFragmentById(R.id.body);

        if (fragment == null) {
            fragment = ExampleFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.body);
        }

        // Create the presenter
        new ExamplePresenter(fragment,this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
