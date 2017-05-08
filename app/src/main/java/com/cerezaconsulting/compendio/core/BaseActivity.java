package com.cerezaconsulting.compendio.core;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.data.local.SessionManager;
import com.cerezaconsulting.compendio.utils.MaterialColor;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Base Actividad de la cual se va a exteder las otras actividades de la app
 */
public abstract class BaseActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sessionManager = new SessionManager(this);
       /* setTheme();*/
        selectTheme(sessionManager.getUserEntity().getCompany().getColor());
        super.onCreate(savedInstanceState);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void selectTheme(String theme) {

        switch (theme) {
            case MaterialColor.AMBER:
                setTheme(R.style.amberTheme);
                break;
            case MaterialColor.LIGHT_BLUE:
                setTheme(R.style.ligthBlueTheme);
                break;
            case MaterialColor.RED:
                setTheme(R.style.redTheme);
                break;
            case MaterialColor.BLUE_GRAY:
                setTheme(R.style.blueGreyTheme);
                break;
            case MaterialColor.BROWN:
                setTheme(R.style.brownTheme);
                break;
            case MaterialColor.DEEP_ORANGE:
                setTheme(R.style.deepOrangeTheme);
                break;
            case MaterialColor.DEEP_PURPLE:
                setTheme(R.style.deepPurpleTheme);
                break;
            case MaterialColor.GRAY:
                setTheme(R.style.greyTheme);
                break;
            case MaterialColor.GREEN:
                setTheme(R.style.greenTheme);
                break;
            case MaterialColor.INDIGO:
                setTheme(R.style.indigoTheme);
                break;
            case MaterialColor.LIGTH_GREEN:
                setTheme(R.style.ligthGreenTheme);
                break;
            case MaterialColor.LIME:
                setTheme(R.style.limeTheme);
                break;
            case MaterialColor.ORANGE:
                setTheme(R.style.orangeTheme);
                break;
            case MaterialColor.PINK:
                setTheme(R.style.pinkTheme);
                break;
            case MaterialColor.PURPLE:
                setTheme(R.style.purpleTheme);
                break;
            case MaterialColor.TEAL:
                setTheme(R.style.tealTheme);
                break;
            case MaterialColor.YELLOW:
                setTheme(R.style.yelloweTheme);
                break;
            case MaterialColor.CYAN:
                setTheme(R.style.cyanTheme);
                break;
            case MaterialColor.BLUE:
                setTheme(R.style.indigoTheme);
                break;
            case MaterialColor.BLACK:
                setTheme(R.style.blackTheme);
                break;

            default:
                setTheme(R.style.indigoTheme);
                break;

        }
    }

    public void showMessageSnack(View container, String message, int colorResource) {
        if (container != null) {
            Snackbar snackbar = Snackbar
                    .make(container, message, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.WHITE);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, colorResource));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            Toast toast =
                    Toast.makeText(getApplicationContext(),
                            message, Toast.LENGTH_LONG);

            toast.show();
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

    protected void newActivityClearPreview(Activity context, Bundle bundle, Class<?> activity) {
        Intent intent = new Intent(context, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        context.finish();
    }

    protected void nextActivityNewTask(Activity context, Bundle bundle, Class<?> activity, boolean destroy) {
        Intent intent = new Intent(context, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (destroy) context.finish();
    }

    public void showMessage(String message) {
        CoordinatorLayout container = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        this.showMessageSnack(container, message, R.color.black);
    }

    public void showMessageError(String message) {
        CoordinatorLayout container = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        this.showMessageSnack(container, message, R.color.error_red);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
