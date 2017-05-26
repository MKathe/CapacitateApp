package com.cerezaconsulting.compendio.presentation.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseActivity;
import com.cerezaconsulting.compendio.data.local.SessionManager;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class LoadActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int RC_CAMERA_PERM = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            methodRequiresPermissionCamera();
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

    @AfterPermissionGranted(RC_CAMERA_PERM)
    private void methodRequiresPermissionCamera() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this.getApplicationContext(), perms)) {
            initialProcess();
        } else {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.perm_camera),
                    RC_CAMERA_PERM, perms);
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == RC_CAMERA_PERM) {
            initialProcess();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
