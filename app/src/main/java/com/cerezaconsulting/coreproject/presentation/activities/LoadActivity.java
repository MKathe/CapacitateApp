package com.cerezaconsulting.coreproject.presentation.activities;

import android.os.Bundle;

import com.cerezaconsulting.coreproject.R;
import com.cerezaconsulting.coreproject.core.BaseActivity;
import com.cerezaconsulting.coreproject.data.local.SessionManager;


public class LoadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            initialProcess();
    }


    private void initialProcess() {
        SessionManager mSessionManager = new SessionManager(getApplicationContext());
        if(mSessionManager.isLogin()){
            //next(this,null, CatalogActivity.class, true);
            nextActivity(this,null, PanelActivity.class, true);
        }else{
            nextActivity(this,null, LoginActivity.class, true);
        }
    }
}
