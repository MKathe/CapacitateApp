package com.cerezaconsulting.compendio.presentation.activities;

import android.os.Bundle;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseActivity;
import com.cerezaconsulting.compendio.core.BaseActivityLogin;
import com.cerezaconsulting.compendio.presentation.fragments.LoginFragment;
import com.cerezaconsulting.compendio.presentation.presenters.LoginPresenter;
import com.cerezaconsulting.compendio.utils.ActivityUtils;

import butterknife.ButterKnife;

public class LoginActivity extends BaseActivityLogin {

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
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.body);

        if (fragment == null) {
            fragment = LoginFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.body);
        }

        // Create the presenter
        new LoginPresenter(fragment,this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
