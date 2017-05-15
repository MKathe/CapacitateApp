package com.cerezaconsulting.compendio.services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cerezaconsulting.compendio.data.events.DisconectedSocketEvent;
import com.cerezaconsulting.compendio.data.events.NotificacionCancelEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NotificationReceiver extends BroadcastReceiver {

    @Subscribe
    public void notificationCancelSync(NotificacionCancelEvent event) {
        if (event != null) {


        }
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action = intent.getAction();
        if (action.equals("stop_service")) {

            context.stopService(new Intent(context, SocketService.class));
           // Toast.makeText(context, "Compendio Web se ha desconectado", Toast.LENGTH_SHORT).show();
            String ns = Context.NOTIFICATION_SERVICE;



            NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
            nMgr.cancel(-1);
        }


    }
}
