package com.cerezaconsulting.compendio.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.data.events.ConnectedSocketEvent;
import com.cerezaconsulting.compendio.data.events.DisconectedSocketEvent;
import com.cerezaconsulting.compendio.data.events.MessageChapterCompleteEvent;
import com.cerezaconsulting.compendio.data.events.NotificacionCancelEvent;
import com.cerezaconsulting.compendio.data.events.SyncProcessSocketEvent;
import com.cerezaconsulting.compendio.data.local.SessionManager;
import com.cerezaconsulting.compendio.data.model.ActivityEntity;
import com.cerezaconsulting.compendio.data.model.CoursesEntity;
import com.cerezaconsulting.compendio.data.model.ReviewEntity;
import com.cerezaconsulting.compendio.data.model.TrainingEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompClient;

/**
 * Created by junior on 12/04/17.
 */

public class SocketService extends Service {
    private StompClient mStompClient;
    private Subscription mRestPingSubscription;
    private SessionManager mSessionManager;
    private ArrayList<StompHeader> stompHeaders;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Subscribe
    public void socketEventDisconnected(ConnectedSocketEvent event) {
        if (event != null) {
        }
    }

    @Subscribe
    public void connectedSocket(ConnectedSocketEvent event) {
        if (event != null) {
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
                mStompClient.disconnect();
                stopWeb();
                break;
        }

    }


    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mSessionManager = new SessionManager(getApplicationContext());
        stompHeaders = new ArrayList<>();
        stompHeaders.add(new StompHeader("username", mSessionManager.getUserEntity().getEmail()));
        stompHeaders.add(new StompHeader("device", "app"));

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("STOMP", "onStartCommand SocketService");

        connectStomp();
        return Service.START_NOT_STICKY;
    }

    public void connectStomp() {

     /*
        mStompClient = Stomp.over(WebSocket.class, "ws://" + ANDROID_EMULATOR_LOCALHOST
                + ":" + RestClient.SERVER_PORT + "/example-endpoint/websocket");
*/
        mStompClient = Stomp.over(WebSocket.class, "ws://racompendio.cloudapp.net/ws/websocket");
        //mStompClient = Stomp.over(WebSocket.class, "ws://192.168.1.5:8080/ws/websocket");
        mStompClient.connect(stompHeaders);

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
                            toast("Error de conexión a la web");
                            break;
                        case CLOSED:
                            EventBus.getDefault().post(new DisconectedSocketEvent(true));
                            toast("Conexión cerrada");
                            stopSelf();
                    }
                });

        // Receive greetings
        mStompClient.topic("/topic/sync." + mSessionManager.getUserEntity().getEmail() + ".activity")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d("Stomp", "Received " + topicMessage.getPayload());
                    try {
                        JSONObject jsonObj = new JSONObject(topicMessage.getPayload());

                        String id = jsonObj.getString("id");
                        int correct = jsonObj.getInt("correct");
                        int incorrect = jsonObj.getInt("incorrect");
                        double intellect = jsonObj.getDouble("intellect");
                        String poorly = jsonObj.getString("poorly");
                        String idChapter = null;
                        if (jsonObj.has("chapter")) {
                            idChapter = jsonObj.getString("chapter");
                        }

                        String idTraining = jsonObj.getString("training");
                        ActivityEntity activityEntity = new ActivityEntity(id, correct,
                                incorrect, intellect, poorly, idChapter, idTraining);
                        updateActivityToLocalDB(activityEntity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
                });

        mStompClient.topic("/topic/sync." + mSessionManager.getUserEntity().getEmail() + ".review")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {


                    try {
                        JSONObject jsonObj = new JSONObject(topicMessage.getPayload());

                        String id = jsonObj.getString("id");
                        Date date = new Date(jsonObj.getLong("date"));
                        int coutdown = jsonObj.getInt("countdown");
                        boolean completed = jsonObj.getBoolean("completed");
                        String idTraining = jsonObj.getString("training");
                        ReviewEntity reviewEntity = new ReviewEntity(id, date, coutdown, completed, idTraining);
                        updateReviewToLocalDB(reviewEntity);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("Stomp", "REVIEW Received " + topicMessage.getPayload());
                    //addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
                });


        mStompClient.topic("/topic/session." + mSessionManager.getUserEntity().getEmail() + ".web")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d("Stomp", "REVIEW Received " + topicMessage.getPayload());
                    stopSelf();
                    //addItem(mGson.fromJson(topicMessage.getPayload(), EchoModel.class));
                });

    }


    private void updateReviewToLocalDB(ReviewEntity reviewEntity) {
        CoursesEntity coursesEntity = mSessionManager.getCoures();
        for (int i = 0; i < coursesEntity.getCourseEntities().size(); i++) {

            if (String.valueOf(coursesEntity.getCourseEntities().get(i).getTrainingEntity().getId()).equals(
                    reviewEntity.getIdTraining())) {
                coursesEntity.getCourseEntities().get(i).getTrainingEntity().getReviewEntities().add(reviewEntity);
                mSessionManager.setCourses(coursesEntity);
                return;
            }
        }
    }

    private void updateActivityToLocalDB(ActivityEntity activityEntity) {
        CoursesEntity coursesEntity = mSessionManager.getCoures();
        for (int i = 0; i < coursesEntity.getCourseEntities().size(); i++) {

            if (String.valueOf(coursesEntity.getCourseEntities().get(i).getTrainingEntity().getId()).equals(
                    activityEntity.getIdTraining())) {
                coursesEntity.getCourseEntities().get(i).getTrainingEntity().getActivityEntities().add(activityEntity);


                if (activityEntity.getIdTraining() != null) {
                    TrainingEntity trainingEntity = updateChapters(
                            coursesEntity.getCourseEntities().get(i).getTrainingEntity(),
                            activityEntity.getIdChapter()
                    );

                    coursesEntity.getCourseEntities().get(i).setTrainingEntity(trainingEntity);
                }


                mSessionManager.setCourses(coursesEntity);
                return;
            }
        }
    }

    private TrainingEntity updateChapters(TrainingEntity trainingEntity, String idChapter) {
        for (int i = 0; i < trainingEntity.getRelease().getCourse().getChapters().size(); i++) {

            if (trainingEntity.getRelease().getCourse().getChapters().get(i).getId().equals(idChapter)) {
                trainingEntity.getRelease().getCourse().getChapters().get(i).setFinished(true);
                return trainingEntity;
            }
        }

        return trainingEntity;
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

        if (text != null) {
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
