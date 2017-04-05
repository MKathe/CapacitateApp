package com.cerezaconsulting.compendio.presentation.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cerezaconsulting.compendio.core.BaseActivity;
import com.cerezaconsulting.compendio.data.local.SessionManager;


public class LoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            initialProcess();
    }


    private void initialProcess() {
        SessionManager mSessionManager = new SessionManager(getApplicationContext());
        if (mSessionManager.isLogin()) {
            //next(this,null, CatalogActivity.class, true);
            nextActivity(this, null, PanelActivity.class, true);
        } else {
            nextActivity(this, null, LoginActivity.class, true);
        }
    }

    protected void nextActivity(Activity context, Bundle bundle, Class<?> activity, boolean destroy) {
        Intent intent = new Intent(context, activity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (destroy) context.finish();
    }

}
