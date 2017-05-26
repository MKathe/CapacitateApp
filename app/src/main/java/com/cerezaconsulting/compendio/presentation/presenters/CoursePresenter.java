package com.cerezaconsulting.compendio.presentation.presenters;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.util.Log;

import com.cerezaconsulting.compendio.data.local.CompedioDbHelper;
import com.cerezaconsulting.compendio.data.local.CompendioPersistenceContract;
import com.cerezaconsulting.compendio.data.local.SessionManager;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.data.model.CoursesEntity;
import com.cerezaconsulting.compendio.data.model.FragmentEntity;
import com.cerezaconsulting.compendio.data.model.ReviewEntity;
import com.cerezaconsulting.compendio.data.model.TrainingEntity;
import com.cerezaconsulting.compendio.data.remote.ServiceFactory;
import com.cerezaconsulting.compendio.data.remote.request.CourseRequest;
import com.cerezaconsulting.compendio.data.response.DoubtResponse;
import com.cerezaconsulting.compendio.presentation.contracts.CourseContract;
import com.cerezaconsulting.compendio.presentation.presenters.communicator.CommunicatorCourseItem;
import com.cerezaconsulting.compendio.utils.ImageDownloadManager;
import com.cerezaconsulting.compendio.utils.ListLinks;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by miguel on 15/03/17.
 */

public class CoursePresenter implements CourseContract.Presenter, CommunicatorCourseItem {

    private CourseContract.View mView;
    private SessionManager sessionManager;
    private boolean firstLoad = false;
    private CompedioDbHelper mCompedioDbHelper;
    private Context context;

    public CoursePresenter(CourseContract.View mView, Context context) {
        this.mView = mView;
        sessionManager = new SessionManager(context);
        this.mView.setPresenter(this);
        this.context = context;
        this.mCompedioDbHelper = new CompedioDbHelper(context);

    }


    public boolean isInternetConnection() {
        try {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public void start() {

        //if (!firstLoad) {
        loadCoursesFromLocalRepository();
        if (isInternetConnection())
            loadCourses();
        //  firstLoad = true;
        //}

    }

    @Override
    public void onClick(CourseEntity courseEntity) {
        mView.detailCourse(courseEntity);
    }

    @Override
    public void openDialogDoubt(String idTraining) {
        mView.showDialogDoubt(idTraining);
    }


    private void downloadAndSaveCourseInLocalStorage(CourseEntity courseEntity) {
        CoursesEntity coursesEntity = sessionManager.getCoures();

        if (coursesEntity != null) {
            for (int i = 0; i < coursesEntity.getCourseEntities().size(); i++) {
                if (courseEntity.getId().equals(coursesEntity.getCourseEntities().get(i).getId())) {
                    //courseEntity.setName(coursesEntity.getCourseEntities().get(i).getName());
                    //courseEntity.setDescription(coursesEntity.getCourseEntities().get(i).getDescription());
                    coursesEntity.getCourseEntities().set(i, courseEntity);

                    for (int j = 0; j < courseEntity.getTrainingEntity().getRelease().getCourse()
                            .getChapters().size(); j++) {

                        for (int k = 0; k < courseEntity.getTrainingEntity().getRelease().getCourse()
                                .getChapters().get(j).getFragments().size(); k++) {
                            saveFragmentsToSQL(courseEntity.getTrainingEntity().getRelease().getCourse()
                                            .getChapters().get(j).getFragments().get(k),
                                    courseEntity.getId(), courseEntity.getTrainingEntity().getRelease().getCourse()
                                            .getChapters().get(j).getId());
                        }

                    }
                    sessionManager.setCourses(coursesEntity);
                    mView.getCourses(sessionManager.getCoures().getCourseEntities());
                    return;
                }
            }

        }

    }


    private void saveFragmentsToSQL(FragmentEntity fragmentEntity, String idTraining, String idChapter) {
        SQLiteDatabase db = mCompedioDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CompendioPersistenceContract.FragmentEntity._ID, fragmentEntity.getId());
        values.put(CompendioPersistenceContract.FragmentEntity.CHAPTER, idChapter);
        values.put(CompendioPersistenceContract.FragmentEntity.TRAINING, idTraining);
        values.put(CompendioPersistenceContract.FragmentEntity.TITLE, fragmentEntity.getTitle());
        values.put(CompendioPersistenceContract.FragmentEntity.CONTENT, fragmentEntity.getContent());

        db.insert(CompendioPersistenceContract.FragmentEntity.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void downloadCourseById(CourseEntity idTraining) {
        downloadCourseById(sessionManager.getUserToken(), String.valueOf(sessionManager.getUserEntity().getId()), idTraining);
    }

    private void downloadCourseById(String Token, String idUser, final CourseEntity courseEntity) {
        mView.setLoadingIndicator(true);
        CourseRequest courseRequest = ServiceFactory.createService(CourseRequest.class);
        Call<TrainingEntity> call = courseRequest.downloadCourses(Token, idUser, courseEntity.getId());
        call.enqueue(new Callback<TrainingEntity>() {
            @Override
            public void onResponse(Call<TrainingEntity> call, Response<TrainingEntity> response) {
                mView.setLoadingIndicator(false);
                if (response.isSuccessful()) {

                    if (!mView.isActive()) {
                        return;
                    }

                    cachedUrls(response.body(), courseEntity);
                    courseEntity.setTrainingEntity(response.body());
                    courseEntity.setName(courseEntity.getRelease().getCourse());
                    courseEntity.setDescription(response.body().getRelease().getCourse().getDescription());
                    courseEntity.setOffLineDisposed(true);

                    downloadAndSaveCourseInLocalStorage(courseEntity);


                } else {
                    if (!mView.isActive()) {
                        return;
                    }
                    mView.showErrorMessage("Hubo un error, por favor intentar más tarde");
                }
            }

            @Override
            public void onFailure(Call<TrainingEntity> call, Throwable t) {
                if (!mView.isActive()) {
                    return;
                }
                mView.setLoadingIndicator(false);
                mView.showErrorMessage("No se puede conectar con el servidor, por favor intentar más tarde");
            }
        });
    }


    private void cachedUrls(TrainingEntity trainingEntity, CourseEntity courseEntity) {
        ArrayList<String> arrayLists = new ArrayList<>();

        for (int i = 0; i < trainingEntity.getRelease().getCourse().getChapters().size(); i++) {

            for (int j = 0; j < trainingEntity.getRelease().getCourse().getChapters().get(i).getFragments().size(); j++) {

               /* arrayLists.add(ListLinks.showLinks(trainingEntity.getRelease().getCourse().getChapters().get(i).
                        getFragments().get(j).getContent(), context));*/

                arrayLists.addAll(ListLinks.showLinks(trainingEntity.getRelease().getCourse().getChapters().get(i).
                        getFragments().get(j).getContent(), context));


            }

        }

        dowloadimages(arrayLists, courseEntity);
    }

    private void dowloadimages(ArrayList<String> arrayLists, CourseEntity courseEntity) {
/*        List<String> urls =
                Arrays.asList("http://design.ubuntu.com/wp-content/uploads/ubuntu-logo32.png",
                        "https://www.seeklogo.net/wp-content/uploads/2013/11/facebook-flat-vector-logo-400x400.png",
                        "https://maxcdn.icons8.com/Share/icon/User_Interface/toggle_off1600.png",
                        "https://cdn3.iconfinder.com/data/icons/free-social-icons/67/rss_circle_color-128.png");*/

        ArrayList<String> urls = arrayLists;
        mView.setLoadingIndicator(true);


        String path =
                Environment.getExternalStorageDirectory() + "/" +"COMPENDIO" +"/";
        ImageDownloadManager.getInstance(context).addTask(
                new ImageDownloadManager.ImageDownloadTask(this, ImageDownloadManager.Task.DOWNLOAD, urls,
                        path, new ImageDownloadManager.Callback() {
                    @Override
                    public void onSuccess(ImageDownloadManager.ImageDownloadTask task) {

                        mView.setLoadingIndicator(false);
                        mView.openCourse(courseEntity);
                        Log.d(ImageDownloadManager.class.getSimpleName(), "Image save success news ");
                    }

                    @Override
                    public void onFailure(ImageDownloadManager.ImageSaveFailureReason reason) {
                        mView.setLoadingIndicator(false);
                        Log.d(ImageDownloadManager.class.getSimpleName(), "Image save fail news " + reason);
                    }
                }));
    }

    private ArrayList<CourseEntity> updateTrainingResults(ArrayList<CourseEntity> coursesEntitiesLocal,
                                                          ArrayList<CourseEntity> coursesEntitiesRemote) {

        for (int j = 0; j < coursesEntitiesRemote.size(); j++) {

            for (int i = 0; i < coursesEntitiesLocal.size(); i++) {
                if (coursesEntitiesLocal.get(i).getId().equals(coursesEntitiesRemote.get(j).getId())) {
                    if (coursesEntitiesLocal.get(i).getTrainingEntity() != null) {


                        coursesEntitiesLocal.get(i).getTrainingEntity().setProgress(
                                coursesEntitiesRemote.get(j).getProgress()
                        );

                        coursesEntitiesLocal.get(i).getTrainingEntity().setPosition(
                                coursesEntitiesRemote.get(j).getPosition()
                        );
                        coursesEntitiesLocal.get(i).getTrainingEntity().setIntellect(
                                coursesEntitiesRemote.get(j).getIntellect()
                        );
                        coursesEntitiesLocal.get(i).getTrainingEntity().setCertificate(
                                coursesEntitiesRemote.get(j).getCertificate()
                        );

                    }
                }
            }

        }

        return coursesEntitiesLocal;
    }

    private ArrayList<CourseEntity> selectedCoursesFromServer(ArrayList<CourseEntity> coursesEntitiesLocal,
                                                              ArrayList<CourseEntity> coursesEntitiesRemote) {

        for (int j = 0; j < coursesEntitiesRemote.size(); j++) {

            for (int i = 0; i < coursesEntitiesLocal.size(); i++) {
                if (coursesEntitiesLocal.get(i).getId().equals(coursesEntitiesRemote.get(j).getId())) {
                    if (coursesEntitiesLocal.get(i).getTrainingEntity() != null) {


                        coursesEntitiesRemote.set(j, coursesEntitiesLocal.get(i));

                    }
                }
            }

        }

        return coursesEntitiesRemote;
    }

    private void loadCourses(String token, String idCompany) {
        mView.setLoadingIndicator(true);
        CourseRequest courseRequest = ServiceFactory.createService(CourseRequest.class);
        Call<ArrayList<CourseEntity>> call = courseRequest.getCourses(token, idCompany);
        call.enqueue(new Callback<ArrayList<CourseEntity>>() {
            @Override
            public void onResponse(Call<ArrayList<CourseEntity>> call, Response<ArrayList<CourseEntity>> response) {
                mView.setLoadingIndicator(false);
                if (response.isSuccessful()) {

                    if (!mView.isActive()) {
                        return;
                    }

                    ArrayList<CourseEntity> courseEntitiesTemp = new ArrayList<>(response.body());
                    ArrayList<CourseEntity> courseEntitiesTemp2 = new ArrayList<>(response.body());

                    if (sessionManager.getCoures() != null) {
                        ArrayList<CourseEntity> courseEntities = selectedCoursesFromServer(sessionManager.getCoures().getCourseEntities(),
                                courseEntitiesTemp);

                        ArrayList<CourseEntity> selectedCourses = updateTrainingResults(courseEntities, courseEntitiesTemp2);
                        sessionManager.setCourses(new CoursesEntity(selectedCourses));
                        mView.getCourses(courseEntities);
                    } else {

                        sessionManager.setCourses(new CoursesEntity(response.body()));
                        mView.getCourses(response.body());
                    }



                  /*  mView.getCourses(response.body());
                    sessionManager.setCourses(new CoursesEntity(response.body()));
                    mView.getCourses(response.body());*/
                } else {
                    if (!mView.isActive()) {
                        return;
                    }
                    mView.showErrorMessage("Hubo un error, por favor intentar más tarde");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CourseEntity>> call, Throwable t) {
                if (!mView.isActive()) {
                    return;
                }
                mView.setLoadingIndicator(false);
                mView.showErrorMessage("No se puede conectar con el servidor, por favor intentar más tarde");
            }
        });
    }


    @Override
    public void loadCourses() {

        loadCourses(sessionManager.getUserToken(), String.valueOf(sessionManager.getUserEntity().getId()));
    }

    @Override
    public void loadCoursesFromLocalRepository() {

        loadCoursesFromLocalStorage();
    }

    @Override
    public void sendDoubt(String doubt, String id) {
        mView.setLoadingIndicator(true);

        DoubtResponse doubtResponse = new DoubtResponse(doubt);
        CourseRequest courseRequest = ServiceFactory.createService(CourseRequest.class);
        Call<Void> call = courseRequest.sendDoubt(sessionManager.getUserToken(), sessionManager.getUserEntity().getId(),
                id, doubtResponse);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.setLoadingIndicator(false);
                if (response.isSuccessful()) {

                    if (!mView.isActive()) {
                        return;
                    }
                    mView.showMessage("Consulta enviada");
                } else {
                    if (!mView.isActive()) {
                        return;
                    }
                    mView.showErrorMessage("Hubo un error, por favor intentar más tarde");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (!mView.isActive()) {
                    return;
                }
                mView.setLoadingIndicator(false);
                mView.showErrorMessage("No se puede conectar con el servidor, por favor intentar más tarde");
            }
        });
    }

    @Override
    public void downloandCertify(String url) {

    }

    private void loadCoursesFromLocalStorage() {

        CoursesEntity coursesEntity = sessionManager.getCoures();
        if (coursesEntity != null) {
            mView.getCourses(coursesEntity.getCourseEntities());
        }

    }
}
