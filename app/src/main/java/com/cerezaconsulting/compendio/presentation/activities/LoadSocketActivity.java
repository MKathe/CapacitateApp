package com.cerezaconsulting.compendio.presentation.activities;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseActivity;
import com.cerezaconsulting.compendio.data.events.ConnectedSocketEvent;
import com.cerezaconsulting.compendio.data.events.DisconectedSocketEvent;
import com.cerezaconsulting.compendio.data.events.NotificacionCancelEvent;
import com.cerezaconsulting.compendio.data.events.SyncProcessSocketEvent;
import com.cerezaconsulting.compendio.presentation.contracts.SyncContrac;
import com.cerezaconsulting.compendio.presentation.presenters.SyncPresenter;
import com.cerezaconsulting.compendio.services.NotificationReceiver;
import com.cerezaconsulting.compendio.services.SocketService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by miguel on 16/03/17.
 */

public class LoadSocketActivity extends BaseActivity implements SyncContrac.View {


    @BindView(R.id.btn_esc)
    Button btnEsc;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.layout_conected)
    LinearLayout layoutConected;
    @BindView(R.id.layout_disconected)
    LinearLayout layoutDisconected;
    @BindView(R.id.btn_again)
    Button btnAgain;
    @BindView(R.id.layout_error)
    LinearLayout layoutError;

    private SyncContrac.Presenter mSyncPresenter;
    private int isDownload = 0;

    BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("finish_activity")) {
                finish();
            }
        }
    };


    @Subscribe
    public void connectedSocketProcess(SyncProcessSocketEvent event) {


    }


    public static void sendNotificationService(Context context) {
        Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentText("Compendio Web está actualmente activo")
                .setContentTitle("Compendio Web");
        builder.setSmallIcon(R.drawable.ic_notification_compendio);


        builder.setAutoCancel(false);


        Intent yesReceive = new Intent(context, NotificationReceiver.class);
        yesReceive.setAction("stop_service");
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context, 0, yesReceive, 0);
        builder.addAction(R.drawable.ic_stat_cancel, "Detener", pendingIntentYes);


        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        nm.notify(-1, notification);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void socketEventDisconnected(DisconectedSocketEvent event) {
        if (event != null) {

            if (event.isActive()) {
                finish();
            }
            // presenter.loadCoursesFromLocalRepository();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notificationCancelSync(NotificacionCancelEvent event) {
        if (event != null) {

            if (event.isStatus()) {
                syncFinalize();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.fragment_loading_socket);
        ButterKnife.bind(this);
        registerReceiver(broadcast_reciever, new IntentFilter("finish_activity"));
        mSyncPresenter = new SyncPresenter(this, this);
        mSyncPresenter.start();
    }


    @Override
    public void syncSuccess() {

        Log.d("SYNC", "exito");
        layoutError.setVisibility(View.GONE);
        layoutDisconected.setVisibility(View.GONE);
        layoutConected.setVisibility(View.VISIBLE);
        EventBus.getDefault().post(new SyncProcessSocketEvent(1));
        sendNotificationService(this);
        // Toast.makeText(this, "EXITO", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void syncFailed() {
        Log.d("SYNC", "no exito");
        isDownload = 0;
        //Toast.makeText(this, "FALLO", Toast.LENGTH_SHORT).show();
        layoutError.setVisibility(View.VISIBLE);
        layoutDisconected.setVisibility(View.GONE);
        layoutConected.setVisibility(View.GONE);
    }

    @Override
    public void tryAgainDownloand() {
        isDownload = 1;
        layoutError.setVisibility(View.GONE);
        layoutDisconected.setVisibility(View.GONE);
        layoutConected.setVisibility(View.VISIBLE);

    }

    @Override
    public void notSync() {


    }

    @Override
    public void syncFinalize() {
        Toast.makeText(this, "Desconectando...", Toast.LENGTH_SHORT).show();
        String ns = Context.NOTIFICATION_SERVICE;

        NotificationManager nMgr = (NotificationManager) this.getSystemService(ns);
        nMgr.cancel(-1);
        stopService(new Intent(getBaseContext(), SocketService.class));
        finish();
    }

    @Override
    public void setPresenter(SyncContrac.Presenter presenter) {
        mSyncPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(broadcast_reciever);
    }


    @OnClick({R.id.btn_again, R.id.btn_esc, R.id.btn_cancel, R.id.btn_cancel_off})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_again:
                if (isDownload == 0) {
                    mSyncPresenter.syncTryAgain();
                } else {
                    mSyncPresenter.tryAgainDowloandCourses();
                }

                layoutError.setVisibility(View.GONE);
                layoutDisconected.setVisibility(View.GONE);
                layoutConected.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_esc:
                mSyncPresenter.syncFinalize();


                break;
            case R.id.btn_cancel:
                mSyncPresenter.syncFinalize();
                break;
            case R.id.btn_cancel_off:
                syncFinalize();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return false; //I have tried here true also
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

    }
}
