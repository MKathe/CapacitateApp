package com.cerezaconsulting.compendio.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cerezaconsulting.compendio.data.events.ConnectedSocketEvent;
import com.cerezaconsulting.compendio.data.events.MessageChapterCompleteEvent;
import com.cerezaconsulting.compendio.data.events.SyncProcessSocketEvent;
import com.cerezaconsulting.compendio.data.local.SessionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.WebSocket;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;

/**
 * Created by junior on 12/04/17.
 */

public class SocketService extends Service {
    private StompClient mStompClient;
    private Subscription mRestPingSubscription;
    private SessionManager mSessionManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Subscribe
    public void connectedSocket(ConnectedSocketEvent event) {
        if (event != null) {
            // presenter.loadCoursesFromLocalRepository();
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void connectedSocketProcess(SyncProcessSocketEvent event) {

        switch (event.getStatus()) {
            case 1:

                sendConfirmation();
                break;

            case 2:

                sendConfirmation();
                mStompClient.disconnect();
                break;
            case 3:

                stopWeb();
                mStompClient.disconnect();
                break;
        }

    }


    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mSessionManager = new SessionManager(getApplicationContext());


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("STOMP", "onStartCommand SocketService");
        connectStomp();
        return Service.START_STICKY;
    }

    public void connectStomp() {

     /*
        mStompClient = Stomp.over(WebSocket.class, "ws://" + ANDROID_EMULATOR_LOCALHOST
                + ":" + RestClient.SERVER_PORT + "/example-endpoint/websocket");
*/
        mStompClient = Stomp.over(WebSocket.class, "ws://racompendio.cloudapp.net/ws/websocket");

        mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            toast("Conectado a Web...");
                            EventBus.getDefault().post(new ConnectedSocketEvent(1));
                            //sendConfirmation();
                            break;
                        case ERROR:
                            Log.e("Stomp", "Stomp connection error", lifecycleEvent.getException());
                            toast("Stomp connection error");
                            break;
                        case CLOSED:
                            toast("Stomp connection closed");
                    }
                });

        // Receive greetings
        mStompClient.topic("/topic/sync." + mSessionManager.getUserEntity().getEmail() + ".activity")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d("Stomp", "Received " + topicMessage.getPayload());
                    //addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
                });

        mStompClient.topic("/topic/sync." + mSessionManager.getUserEntity().getEmail() + ".review")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d("Stomp", "REVIEW Received " + topicMessage.getPayload());
                    //addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
                });
        mStompClient.topic("/app/sync." + mSessionManager.getUserEntity().getEmail() + ".web")
                .compose(applySchedulers())
                .subscribe(aVoid -> {
                    Log.d("STOMP", "WEB STOMP echo send successfully");
                }, throwable -> {
                    Log.e("STOMP", "Error send STOMP echo", throwable);
                    toast(throwable.getMessage());
                });
        mStompClient.connect();
    }


    public void sendConfirmation() {
        mStompClient.send("/app/sync." + mSessionManager.getUserEntity().getEmail() + ".complete", "true")
                .compose(applySchedulers())
                .subscribe(aVoid -> {
                    Log.d("STOMP", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("STOMP", "Error send STOMP echo", throwable);
                    toast(throwable.getMessage());
                });
    }

    public void stopWeb() {

    }


    private void toast(String text) {

        if (text!=null){
            Log.i("Stomp", text);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }

    }


    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return rObservable -> rObservable
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public void onDestroy() {
        stopWeb();
        EventBus.getDefault().unregister(this);
        mStompClient.disconnect();
        if (mRestPingSubscription != null) mRestPingSubscription.unsubscribe();
        super.onDestroy();
    }

}
