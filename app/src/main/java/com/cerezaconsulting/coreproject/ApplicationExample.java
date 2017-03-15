package com.cerezaconsulting.coreproject;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by junior on 25/12/16.
 */

public class ApplicationExample extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/proximanovaregular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


    }





}
