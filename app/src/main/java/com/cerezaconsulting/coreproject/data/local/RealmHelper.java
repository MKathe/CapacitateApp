package com.cerezaconsulting.coreproject.data.local;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by miguel on 06/02/17.
 */

public class RealmHelper {
    private Context context;
    private static String DEFAULT_NAME = "realm.compendio";
    private Realm realm;

    public void initRealm(Context context){
        this.context = context;
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(DEFAULT_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public Realm getInstance(){
        realm = Realm.getDefaultInstance();
        return realm;
    }
}