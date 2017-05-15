package com.cerezaconsulting.compendio.presentation.presenters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.data.local.CompedioDbHelper;
import com.cerezaconsulting.compendio.data.local.CompendioPersistenceContract;
import com.cerezaconsulting.compendio.data.local.SessionManager;
import com.cerezaconsulting.compendio.data.model.ActivityEntity;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.data.model.CoursesEntity;
import com.cerezaconsulting.compendio.data.model.ReviewEntity;
import com.cerezaconsulting.compendio.data.model.TrainingEntity;
import com.cerezaconsulting.compendio.data.model.UserEntity;
import com.cerezaconsulting.compendio.data.remote.ServiceFactory;
import com.cerezaconsulting.compendio.data.remote.request.CourseRequest;
import com.cerezaconsulting.compendio.data.remote.request.PerfilRequest;
import com.cerezaconsulting.compendio.data.remote.request.SyncRequest;
import com.cerezaconsulting.compendio.data.response.ActivityResponse;
import com.cerezaconsulting.compendio.data.response.ResponseActivitySync;
import com.cerezaconsulting.compendio.data.response.ResponseCompleteSyncResponse;
import com.cerezaconsulting.compendio.data.response.ReviewResponse;
import com.cerezaconsulting.compendio.data.response.TrackReviewResponse;
import com.cerezaconsulting.compendio.presentation.contracts.SyncContrac;
import com.cerezaconsulting.compendio.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by junior on 27/04/17.
 */

public class SyncPresenter implements SyncContrac.Presenter {

    private SyncContrac.View mView;
    private SessionManager sessionManager;
    private boolean firstLoad = false;
    private Context context;
    boolean treadhRunning = true;
    boolean requestProgress = false;
    private int incrementRequest = 0;
    private ArrayList<ActivityEntity> activitiesToSync;
    private ArrayList<ReviewEntity> reviewsToSync;
    private int number = 0;


    private int numberReview = 0;
    private int incrementAnInt = 0;

    private CompedioDbHelper mCompedioDbHelper;


    public SyncPresenter(SyncContrac.View mView, Context context) {
        this.mView = mView;
        sessionManager = new SessionManager(context);
        this.mView.setPresenter(this);
        this.context = context;
        activitiesToSync = new ArrayList<>();
        reviewsToSync = new ArrayList<>();
        mCompedioDbHelper = new CompedioDbHelper(context);


    }

    public ArrayList<ReviewEntity> getReviews() {


        List<ReviewEntity> reviewEntities = new ArrayList<>();
        SQLiteDatabase db = mCompedioDbHelper.getReadableDatabase();

        String[] projection = {
                CompendioPersistenceContract.ReviewEntity._ID,
                CompendioPersistenceContract.ReviewEntity.COLUMN_NAME_ENTRY_ID,
                CompendioPersistenceContract.ReviewEntity.DATE,
                CompendioPersistenceContract.ReviewEntity.COUNTDOWN,
                CompendioPersistenceContract.ReviewEntity.COMPLETED,
                CompendioPersistenceContract.ReviewEntity.TRAINING,
                CompendioPersistenceContract.ReviewEntity.SYNC

        };


        String[] args = new String[]{"1"};
        Cursor c = db.query(
                CompendioPersistenceContract.ReviewEntity.TABLE_NAME,
                projection, "sync = ?", args, null, null, null);


        ReviewEntity reviewEntity = null;

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String idLocal = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.ReviewEntity._ID));
                String id = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.ReviewEntity.COLUMN_NAME_ENTRY_ID));
                String date = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.ReviewEntity.DATE));
                String countdown = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.ReviewEntity.COUNTDOWN));
                boolean completed = c.getInt(c.getColumnIndexOrThrow(CompendioPersistenceContract.ReviewEntity.COMPLETED)) == 1;
                String training = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.ReviewEntity.TRAINING));
                boolean sync = c.getInt(c.getColumnIndexOrThrow(CompendioPersistenceContract.ReviewEntity.SYNC)) == 1;

                reviewEntity = new ReviewEntity(id, idLocal, new Date(Long.valueOf(date)), Integer.valueOf(countdown), completed,
                        sync, training);

                reviewEntities.add(reviewEntity);

            }

        }
        if (c != null) {
            c.close();
        }

        db.close();

        return (ArrayList<ReviewEntity>) reviewEntities;
    }


    private void checkInReviewToUpload(String idReviewLocal) {
        SQLiteDatabase db = mCompedioDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CompendioPersistenceContract.ReviewEntity.SYNC, false);

        String selection = CompendioPersistenceContract.ReviewEntity._ID + " LIKE ?";
        String[] selectionArgs = {idReviewLocal};

        db.update(CompendioPersistenceContract.ReviewEntity.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }


    private ArrayList<TrackReviewResponse> formReviewResponses() {
        ArrayList<ReviewEntity> reviewEntities = getReviews();
        ArrayList<TrackReviewResponse> reviewResponses = new ArrayList<>();
        ReviewResponse reviewResponse = null;

        TrackReviewResponse trackReviewResponse = null;

        for (int i = 0; i < reviewEntities.size(); i++) {
            reviewResponse = new ReviewResponse();
            reviewResponse.setId(reviewEntities.get(i).getId());
            reviewResponse.setTraining(reviewEntities.get(i).getIdTraining());
            // reviewResponse.setDate(DateUtils.getFormantSingle(reviewEntities.get(i).getDate()));
            reviewResponse.setDate(reviewEntities.get(i).getDate().getTime());
            reviewResponse.setCompleted(reviewEntities.get(i).isCompleted());
            reviewResponse.setCountdown(reviewEntities.get(i).getCountdown());
            trackReviewResponse = new TrackReviewResponse();
            trackReviewResponse.setReviewResponse(reviewResponse);
            trackReviewResponse.setIdLocal(reviewEntities.get(i).getIdLocal());
            reviewResponses.add(trackReviewResponse);

        }


        return reviewResponses;
    }


    public ArrayList<ActivityEntity> getActivities() {


        List<ActivityEntity> activityEntities = new ArrayList<>();
        SQLiteDatabase db = mCompedioDbHelper.getReadableDatabase();

        String[] projection = {
                CompendioPersistenceContract.ActivityEntity._ID,
                CompendioPersistenceContract.ActivityEntity.INTELLECT,
                CompendioPersistenceContract.ActivityEntity.CORRECT,
                CompendioPersistenceContract.ActivityEntity.INCORRECT,
                CompendioPersistenceContract.ActivityEntity.POORLY,
                CompendioPersistenceContract.ActivityEntity.TRAINING,
                CompendioPersistenceContract.ActivityEntity.CHAPTER,
                CompendioPersistenceContract.ActivityEntity.SYNC

        };


        String[] args = new String[]{"1"};
        Cursor c = db.query(
                CompendioPersistenceContract.ActivityEntity.TABLE_NAME,
                projection, "sync = ?", args, null, null, null);


        ActivityEntity activityEntity = null;

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.ActivityEntity._ID));
                String incorrect = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.ActivityEntity.INCORRECT));
                String correct = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.ActivityEntity.CORRECT));
                String intellect = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.ActivityEntity.INTELLECT));
                String poorly = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.ActivityEntity.POORLY));
                String training = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.ActivityEntity.TRAINING));
                String chapter = c.getString(c.getColumnIndexOrThrow(CompendioPersistenceContract.ActivityEntity.CHAPTER));
                boolean sync = c.getInt(c.getColumnIndexOrThrow(CompendioPersistenceContract.ActivityEntity.SYNC)) == 1;

                activityEntity = new ActivityEntity(id, Double.valueOf(intellect), Integer.valueOf(correct),
                        Integer.valueOf(incorrect), poorly, training, chapter, sync);

                activityEntities.add(activityEntity);

            }

        }
        if (c != null) {
            c.close();
        }

        db.close();

        return (ArrayList<ActivityEntity>) activityEntities;
    }

    private void checkInActivityToUpload(String idActivityLocal) {
        SQLiteDatabase db = mCompedioDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CompendioPersistenceContract.ActivityEntity.SYNC, false);

        String selection = CompendioPersistenceContract.ActivityEntity._ID + " LIKE ?";
        String[] selectionArgs = {idActivityLocal};

        db.update(CompendioPersistenceContract.ActivityEntity.TABLE_NAME, values, selection, selectionArgs);

        db.close();
    }


    private ArrayList<ActivityResponse> formActivityResponses() {
        ArrayList<ActivityEntity> activityEntities = getActivities();
        ArrayList<ActivityResponse> activityResponses = new ArrayList<>();
        ActivityResponse activityResponse = null;


        for (int i = 0; i < activityEntities.size(); i++) {
            activityResponse = new ActivityResponse();
            activityResponse.setChapter(activityEntities.get(i).getIdChapter());
            activityResponse.setTraining(activityEntities.get(i).getIdTraining());
            activityResponse.setCorrect(activityEntities.get(i).getCorrect());
            activityResponse.setIncorrect(activityEntities.get(i).getIncorrect());
            activityResponse.setPoorly(activityEntities.get(i).getPoorly());
            activityResponse.setId(activityEntities.get(i).getId());
            activityResponses.add(activityResponse);

        }


        return activityResponses;
    }


    private void synchronizateActivities() {

        ArrayList<ActivityResponse> activityResponses = formActivityResponses();

        if (activityResponses.size() > 0) {
            SyncRequest syncRequest =
                    ServiceFactory.createService(SyncRequest.class);
            Call<Void> call = syncRequest.syncActivity(sessionManager.getUserToken(),
                    activityResponses);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        for (int i = 0; i < activityResponses.size(); i++) {
                            checkInActivityToUpload(activityResponses.get(i).getId());
                        }
                        synchronizateReviews();

                    } else {
                        mView.syncFailed();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                    mView.syncFailed();
                }
            });
        } else {
            synchronizateReviews();
        }

    }


    private ArrayList<ReviewResponse> orderListTrackReviews(ArrayList<TrackReviewResponse> trackReviewResponses) {
        ArrayList<ReviewResponse> reviewResponses = new ArrayList<>();

        for (int i = 0; i < trackReviewResponses.size(); i++) {

            if (!trackReviewResponses.get(i).getReviewResponse().getId().equals("")) {
                reviewResponses.add(trackReviewResponses.get(i).getReviewResponse());
            }
        }


        for (int i = 0; i < trackReviewResponses.size(); i++) {

            if (trackReviewResponses.get(i).getReviewResponse().getId().equals("")) {
                reviewResponses.add(trackReviewResponses.get(i).getReviewResponse());
            }
        }


        return reviewResponses;
    }

    private void synchronizateReviews() {

        ArrayList<TrackReviewResponse> reviewResponses = formReviewResponses();

        ArrayList<ReviewResponse> reviewResponsesSync = orderListTrackReviews(reviewResponses);


        if (reviewResponses.size() > 0) {
            SyncRequest syncRequest =
                    ServiceFactory.createService(SyncRequest.class);
            Call<Void> call = syncRequest.syncReview(sessionManager.getUserToken(),
                    reviewResponsesSync);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {

                        for (int i = 0; i < reviewResponses.size(); i++) {
                            checkInReviewToUpload(reviewResponses.get(i).getIdLocal());
                        }
                        downloadTrainingsUpdate(0);
                    } else {
                        mView.syncFailed();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                    mView.syncFailed();
                }
            });
        } else {
            downloadTrainingsUpdate(0);
        }


    }

    private void uploadReviewsToserver() {

        final int[] failureRequest = {0};
        ArrayList<TrackReviewResponse> reviewResponses = formReviewResponses();

        if (reviewResponses.size() > 0) {

            ArrayList<ActivityResponse> activityResponses = formActivityResponses();

            SyncRequest syncRequest =
                    ServiceFactory.retrofitService(SyncRequest.class);

            for (int i = 0; i < reviewResponses.size(); i++) {
                final int finalI = i;
                syncRequest.uploadReview(sessionManager.getUserToken(), reviewResponses.get(i).getReviewResponse())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResponseActivitySync>() {
                            @Override
                            public final void onCompleted() {
                                // Toast.makeText(MainActivity.this, login, Toast.LENGTH_SHORT).show();
                                // do nothing

                                Log.e("ACTIVITIES SYNC", "complete");
                                if (failureRequest[0] > 0) {
                                    mView.syncFailed();
                                    return;
                                }

                                if (finalI == reviewResponses.size() - 1) {
                                    uploadActivitiesToServer();
                                }

                            }

                            @Override
                            public final void onError(Throwable e) {
                                failureRequest[0]++;
                                Log.e("ACTIVITIES SYNC", e.getMessage());
                                if (e.getMessage().equals("HTTP 500 ")) {
                                    mView.syncFailed();
                                }
                            }

                            @Override
                            public final void onNext(ResponseActivitySync response) {

                                Log.e("ACTIVITIES SYNC", "next");
                                checkInReviewToUpload(reviewResponses.get(finalI).getIdLocal());

                            }


                        });
            }

        } else {
            uploadActivitiesToServer();
        }

    }

    private void uploadActivitiesToServer() {

        final int[] failureRequest = {0};
        ArrayList<ActivityResponse> activityResponses = formActivityResponses();

        if (activityResponses.size() > 0) {


            SyncRequest syncRequest =
                    ServiceFactory.retrofitService(SyncRequest.class);

            for (int i = 0; i < activityResponses.size(); i++) {
                final int finalI = i;
                syncRequest.uploadActivity(sessionManager.getUserToken(), activityResponses.get(i))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResponseActivitySync>() {
                            @Override
                            public final void onCompleted() {
                                // Toast.makeText(MainActivity.this, login, Toast.LENGTH_SHORT).show();
                                // do nothing

                                Log.e("ACTIVITIES SYNC", "complete");
                                if (failureRequest[0] > 0) {
                                    mView.syncFailed();
                                    return;
                                }

                                if (finalI == activityResponses.size() - 1) {
                                    downloadTrainingsUpdate(0);
                                }

                            }

                            @Override
                            public final void onError(Throwable e) {
                                failureRequest[0]++;
                                Log.e("ACTIVITIES SYNC", e.getMessage() + "-----" + e.hashCode());

                                if (e.getMessage().equals("HTTP 500 ")) {
                                    mView.syncFailed();
                                }
                            }

                            @Override
                            public final void onNext(ResponseActivitySync response) {

                                Log.e("ACTIVITIES SYNC", "next");
                                checkInActivityToUpload(activityResponses.get(finalI).getId());

                            }


                        });
            }

        } else {
            downloadTrainingsUpdate(0);
        }

    }

    private void checkIsWebOpened() {

        SyncRequest syncRequest =
                ServiceFactory.createService(SyncRequest.class);
        Call<ResponseCompleteSyncResponse> call = syncRequest.syncComplete(sessionManager.getUserToken(),
                sessionManager.getUserEntity().getEmail());
        call.enqueue(new Callback<ResponseCompleteSyncResponse>() {
            @Override
            public void onResponse(Call<ResponseCompleteSyncResponse> call, Response<ResponseCompleteSyncResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().isLinked()) {
                        mView.syncSuccess();
                    } else {

                        Toast.makeText(context, "Contenido Actualizado", Toast.LENGTH_SHORT).show();
                        mView.syncFinalize();
                    }

                } else {
                    mView.syncFailed();
                }
            }

            @Override
            public void onFailure(Call<ResponseCompleteSyncResponse> call, Throwable t) {

                mView.syncFailed();
            }
        });


    }


    private void downloadTrainingsUpdate(int status) {

        final int[] failureRequest = {0};
        CoursesEntity coursesEntity = sessionManager.getCoures();

        if (coursesEntity.getCourseEntities().size() > 0) {


            CourseRequest syncRequest =
                    ServiceFactory.retrofitService(CourseRequest.class);

            for (int i = 0; i < coursesEntity.getCourseEntities().size(); i++) {
                final int finalI = i;
                syncRequest.downloadCourse(sessionManager.getUserToken(), String.valueOf(sessionManager.getUserEntity().getId()),
                        coursesEntity.getCourseEntities().get(i).getId())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<TrainingEntity>() {
                            @Override
                            public final void onCompleted() {
                                // Toast.makeText(MainActivity.this, login, Toast.LENGTH_SHORT).show();
                                // do nothing

                                Log.e("ACTIVITIES SYNC", "complete");
                                if (failureRequest[0] > 0) {
                                    mView.tryAgainDownloand();
                                    return;
                                }

                                if (finalI == coursesEntity.getCourseEntities().size() - 1) {

                                    if (status == 0)
                                        checkIsWebOpened();
                                    else
                                        mView.syncFinalize();
                                }

                            }

                            @Override
                            public final void onError(Throwable e) {
                                failureRequest[0]++;
                                Log.e("ACTIVITIES SYNC", e.getMessage());
                                if (e.getMessage().equals("HTTP 500 ")) {
                                    mView.syncFailed();
                                }
                            }

                            @Override
                            public final void onNext(TrainingEntity response) {

                                Log.e("ACTIVITIES SYNC", "next");
                                CourseEntity courseEntity = coursesEntity.getCourseEntities().get(finalI);
                                courseEntity.setTrainingEntity(response);
                                courseEntity.setName(courseEntity.getRelease().getCourse());
                                courseEntity.setDescription(response.getRelease().getCourse().getDescription());
                                courseEntity.setOffLineDisposed(true);

                                downloadAndSaveCourseInLocalStorage(courseEntity);


                            }


                        });
            }

        } else {
            Toast.makeText(context, "Nada por sincronizar, conectando ...", Toast.LENGTH_SHORT).show();
            mView.syncSuccess();
        }

    }

    private void downloadAndSaveCourseInLocalStorage(CourseEntity courseEntity) {
        CoursesEntity coursesEntity = sessionManager.getCoures();

        if (coursesEntity != null) {
            for (int i = 0; i < coursesEntity.getCourseEntities().size(); i++) {
                if (courseEntity.getId().equals(coursesEntity.getCourseEntities().get(i).getId())) {
                    //courseEntity.setName(coursesEntity.getCourseEntities().get(i).getName());
                    //courseEntity.setDescription(coursesEntity.getCourseEntities().get(i).getDescription());
                    coursesEntity.getCourseEntities().set(i, courseEntity);
                    sessionManager.setCourses(coursesEntity);
                    return;
                }
            }

        }

    }


    private void saveReviewToLocalBD(ReviewEntity reviewEntity) {


        SQLiteDatabase db = mCompedioDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CompendioPersistenceContract.ReviewEntity.COLUMN_NAME_ENTRY_ID, reviewEntity.getId());
        values.put(CompendioPersistenceContract.ReviewEntity.COUNTDOWN, reviewEntity.getCountdown());
        values.put(CompendioPersistenceContract.ReviewEntity.COMPLETED, reviewEntity.isCompleted());
        values.put(CompendioPersistenceContract.ReviewEntity.DATE, reviewEntity.getDate().getTime());
        values.put(CompendioPersistenceContract.ReviewEntity.TRAINING, reviewEntity.getIdTraining());
        values.put(CompendioPersistenceContract.ReviewEntity.SYNC, reviewEntity.isOffline());

        db.insert(CompendioPersistenceContract.ReviewEntity.TABLE_NAME, null, values);

        db.close();

    }

    private void saveActivityToLocalBD(ActivityEntity activityEntity) {


        SQLiteDatabase db = mCompedioDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CompendioPersistenceContract.ActivityEntity.COLUMN_NAME_ENTRY_ID, activityEntity.getId());
        values.put(CompendioPersistenceContract.ActivityEntity.INTELLECT, activityEntity.getIntellect());
        values.put(CompendioPersistenceContract.ActivityEntity.CORRECT, activityEntity.getCorrect());
        values.put(CompendioPersistenceContract.ActivityEntity.INCORRECT, activityEntity.getIncorrect());
        values.put(CompendioPersistenceContract.ActivityEntity.POORLY, activityEntity.getPoorly());
        values.put(CompendioPersistenceContract.ActivityEntity.TRAINING, activityEntity.getIdTraining());
        values.put(CompendioPersistenceContract.ActivityEntity.CHAPTER, activityEntity.getIdChapter());
        values.put(CompendioPersistenceContract.ActivityEntity.SYNC, activityEntity.isOffline());

        db.insert(CompendioPersistenceContract.ActivityEntity.TABLE_NAME, null, values);

        db.close();

    }

    @Override
    public void syncActivity(ActivityEntity activityEntity, String idTrainig) {

    }

    @Override
    public void syncReview(ReviewEntity reviewEntity) {

    }

    @Override
    public void syncTryAgain() {
        synchronizateActivities();
    }

    @Override
    public void syncFinalize() {
        mView.syncFinalize();
    }

    @Override
    public void tryAgainDowloandCourses() {
        downloadTrainingsUpdate(1);
    }


    private void synchronizedActivitiesAndReviews() {
        ActivityEntity activityEntity = null;
        SessionManager sessionManager = new SessionManager(context);


        for (int i = 0; i < sessionManager.getCoures().getCourseEntities().size(); i++) {


            if (sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity() != null) {
                if (sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getActivityEntities() != null) {
                    for (int j = 0; j < sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getActivityEntities().size(); j++) {
                        if (sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getActivityEntities().get(j).isOffline()) {
                            activityEntity = sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getActivityEntities().get(j);
                            activityEntity.setIdTraining(String.valueOf(sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getId()));
                            saveActivityToLocalBD(activityEntity);
                            updateOffLineActivities(i, j);
                        }


                        //activitiesToSync.add(activityEntity);


               /* syncActivity(
                        sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getActivityEntities().get(j),
                        String.valueOf(sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getId()));*/
                    }

                }
            }

        }

        synchronizedReviews();
        // mView.syncSuccess();
    }

    private void updateOffLineActivities(int i, int j) {
        CoursesEntity coursesEntity = sessionManager.getCoures();
        coursesEntity.getCourseEntities().get(i).getTrainingEntity().getActivityEntities().get(j).setOffline(false);
        sessionManager.setCourses(coursesEntity);
    }


    private void synchronizedReviews() {
        ReviewEntity reviewEntity = null;
        SessionManager sessionManager = new SessionManager(context);


        for (int i = 0; i < sessionManager.getCoures().getCourseEntities().size(); i++) {

            if (sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity() != null) {
                if (sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getReviewEntities() != null) {
                    for (int j = 0; j < sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getReviewEntities().size(); j++) {

                        if (sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getReviewEntities().get(j).isOffline()) {
                            reviewEntity = sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getReviewEntities().get(j);
                            reviewEntity.setIdTraining(String.valueOf(sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getId()));
                            saveReviewToLocalBD(reviewEntity);
                            updateOffLineReviews(i, j);
                        }

                        //  if ((sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getReviewEntities().get(j).isOffline() &&
                        //            sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getReviewEntities().get(j).getId().equals("")) ||
                        //              !sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getReviewEntities().get(j).getId().equals("")) {

                        // }

               /* syncActivity(
                        sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getActivityEntities().get(j),
                        String.valueOf(sessionManager.getCoures().getCourseEntities().get(i).getTrainingEntity().getId()));*/
                    }

                }
            }
        }
        // mView.syncSuccess();
    }

    private void updateOffLineReviews(int i, int j) {
        CoursesEntity coursesEntity = sessionManager.getCoures();
        coursesEntity.getCourseEntities().get(i).getTrainingEntity().getReviewEntities().get(j).setOffline(false);
        sessionManager.setCourses(coursesEntity);
    }


    @Override
    public void start() {


        synchronizedActivitiesAndReviews();
        synchronizateActivities();
        // synchronizedActivitiesAndReviews();
        //notificationWithProgressBar(context);

    }


   /* public void notificationWithProgressBar(Context context) {
        final int id = 1;
        final int[] incr = {0};
        *//*  final NotificationCompat.Builder mBuilder;
        final NotificationManager mNotifyManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("Sincronizando")
                .setContentText("Sincronización en progreso")
                .setSmallIcon(R.drawable.ic_force_synchronization);*//*


        new Thread(
                new Runnable() {
                    @Override
                    public void run() {


                        while (treadhRunning) {

                            if (!requestProgress) {

                                Log.d("SYNC", incr[0] + "% - increment " + incrementRequest);
                                *//*if (incr[0] == workerEntities.size() * incrementRequest) {
                                    mBuilder.setProgress(100, 100, false);
                                } else {
                                    mBuilder.setProgress(100, incr[0], false);
                                }*//*
                                //syngWorkerData(workerEntities.get(number).getDocumentNumber(), mSessionManager.getUserEntity().getGroupId());
                                if (number < activitiesToSync.size()) {

                                    syncActivity(activitiesToSync.get(number), activitiesToSync.get(number).getIdTraining());
                                    requestProgress = true;
                                    incr[0] += incrementRequest;
                                    // EventBus.getDefault().postSticky(incr[0]);
                                    number = number + 1;
                                    Log.d("SYNC", "Iteration: " + number);
                                } else {

                                    *//*if(number < (dniWorkersToSync.size()+signsWorkersToSync.size())){
                                        synchronizateNewSignatures(signsWorkersToSync.get(number-dniWorkersToSync.size()).getId(),signsWorkersToSync.get(number-dniWorkersToSync.size()).getSignature());
                                        requestProgress = true;
                                        incr[0] += incrementRequest;
                                        EventBus.getDefault().postSticky(incr[0]);
                                        number = number + 1;
                                        Log.d("SYNC" , "Iteration: "+number);
                                    }else{

                                    }*//*
                                }
                                requestProgress = true;


                                if (number == activitiesToSync.size()) {
                                    //EventBus.getDefault().postSticky(100);

                                    treadhRunning = false;
                                    Log.e("SOCKET", "FINAL");
                                    mView.syncSuccess();

                                }
                            }

                        }


                        if (!treadhRunning) {
                            Thread.currentThread().interrupt();

                        }


                    }

                }
        ).start();

    }*/

}
