package com.cerezaconsulting.compendio.presentation.fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.core.BaseActivity;
import com.cerezaconsulting.compendio.core.BaseFragment;
import com.cerezaconsulting.compendio.core.ScrollChildSwipeRefreshLayout;
import com.cerezaconsulting.compendio.data.events.ConnectedSocketEvent;
import com.cerezaconsulting.compendio.data.events.MessageChapterCompleteEvent;
import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.data.model.CoursesEntity;
import com.cerezaconsulting.compendio.presentation.activities.ChapterActivity;
import com.cerezaconsulting.compendio.presentation.activities.FragmentsActivity;
import com.cerezaconsulting.compendio.presentation.activities.LoadSocketActivity;
import com.cerezaconsulting.compendio.presentation.activities.QuestionReviewActivity;
import com.cerezaconsulting.compendio.presentation.adapters.CourseAdapter;
import com.cerezaconsulting.compendio.presentation.contracts.CourseContract;
import com.cerezaconsulting.compendio.presentation.fragments.dialog.AlertConfirmDialog;
import com.cerezaconsulting.compendio.presentation.presenters.communicator.CommunicatorCourseItem;
import com.cerezaconsulting.compendio.services.SocketService;
import com.cerezaconsulting.compendio.utils.DateUtils;
import com.cerezaconsulting.compendio.utils.ProgressDialogCustom;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguel on 15/03/17.
 */

public class CourseFragment extends BaseFragment implements CourseContract.View {

    public static final int REQUEST_CHAPTER = 100;

    @BindView(R.id.complatins_list)
    RecyclerView complatinsList;
    @BindView(R.id.complatinsLL)
    LinearLayout complatinsLL;
    @BindView(R.id.noComplatinsIcon)
    ImageView noComplatinsIcon;
    @BindView(R.id.noComplatinsMain)
    TextView noComplatinsMain;
    @BindView(R.id.noComplatins)
    LinearLayout noComplatins;
    @BindView(R.id.clinic_container)
    RelativeLayout clinicContainer;
    @BindView(R.id.fab_add_task)
    FloatingActionButton fabAddTask;
    @BindView(R.id.refresh_layout)

    ScrollChildSwipeRefreshLayout refreshLayout;
    private CourseAdapter courseAdapter;
    private LinearLayoutManager layoutManager;
    private CourseContract.Presenter presenter;
    private ProgressDialogCustom mProgressDialogCustom;

    public static CourseFragment newInstance() {
        return new CourseFragment();
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void connectedSocket(ConnectedSocketEvent event) {


        switch (event.getStatus()) {

            case 0:
                break;

            case 1:
                nextActivity(getActivity(), null, LoadSocketActivity.class, false);
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCompletedChapterEvent(MessageChapterCompleteEvent event) {
        if (event != null) {
            // presenter.loadCoursesFromLocalRepository();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
        presenter.loadCoursesFromLocalRepository();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_special_schedules, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressDialogCustom = new ProgressDialogCustom(getContext(), "Descargando curso...");
        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.black),
                ContextCompat.getColor(getActivity(), R.color.dark_gray),
                ContextCompat.getColor(getActivity(), R.color.black)
        );
        refreshLayout.setScrollUpChild(complatinsList);
        refreshLayout.setOnRefreshListener(() -> presenter.loadCourses());

        fabAddTask.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getContext());
        complatinsList.setLayoutManager(layoutManager);

        courseAdapter = new CourseAdapter(getContext(), new ArrayList<CourseEntity>(), (CommunicatorCourseItem) presenter);
        complatinsList.setAdapter(courseAdapter);


        //  if(isMyServiceRunning(SocketService.class)){

        //}
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void getCourses(ArrayList<CourseEntity> list) {
        courseAdapter.setItems(list);
        if (list.size() != 0) {
            noComplatins.setVisibility(View.GONE);
        }
    }


    public void showDialogForDownloadCourse(Context context, String title, CharSequence message, final CourseEntity idTraining) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialogInterface, i) -> {

            dialogInterface.cancel();
            if (isInternetConnection(getContext())) {
                presenter.downloadCourseById(idTraining);
            } else {
                showErrorMessage("No está conectado a internet");
            }

        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }

    @Override
    public void detailCourse(CourseEntity courseEntity) {


        if (courseEntity.isOffLineDisposed()) {

            if (courseEntity.getTrainingEntity().getReviewEntities() != null) {


                if (courseEntity.getTrainingEntity().getReviewEntities().size() > 0) {
                    if (DateUtils.comparteDates(
                            courseEntity.getTrainingEntity().getReviewEntities()
                                    .get(courseEntity.getTrainingEntity().getReviewEntities().size() - 1)
                                    .getDate())) {

                        Bundle bundle = new Bundle();

                        bundle.putSerializable("course", courseEntity);
                        Intent intent = new Intent(getActivity(), QuestionReviewActivity.class);
                        if (bundle != null) {
                            intent.putExtras(bundle);
                        }
                        startActivityForResult(intent, 999);

                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("course", courseEntity);
                        nextActivity(getActivity(), bundle, ChapterActivity.class, false);
                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("course", courseEntity);
                    nextActivity(getActivity(), bundle, ChapterActivity.class, false);
                }


            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable("course", courseEntity);
                nextActivity(getActivity(), bundle, ChapterActivity.class, false);
            }

        } else {
            showDialogForDownloadCourse(getContext(), "Descargar Curso",
                    "Debes primero descargar el contenido del curso, recuerda que esto debes estar conectado a una red inalámbrica",
                    courseEntity);
        }


    }

    @Override
    public void openCourse(CourseEntity courseEntity) {


        if (courseEntity.getTrainingEntity().getReviewEntities() != null) {
            if (courseEntity.getTrainingEntity().getReviewEntities().size() > 0) {
                return;
            }
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("course", courseEntity);
        nextActivity(getActivity(), bundle, ChapterActivity.class, false, REQUEST_CHAPTER);
    }

    @Override
    public void sendDoubt(String doubt, String id) {
        presenter.sendDoubt(doubt, id);
    }

    @Override
    public void showDialogDoubt(String idTraining) {
        AlertConfirmDialog alertConfirmDialog = new AlertConfirmDialog(getActivity(), this,
                "Aceptar", idTraining);
        alertConfirmDialog.show();
    }

    @Override
    public void setPresenter(CourseContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void downloadingCourse(boolean active) {


        if (active) {
            mProgressDialogCustom.show();
        } else {
            if (mProgressDialogCustom.isShowing()) {
                mProgressDialogCustom.dismiss();
            }
        }
    }


    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(() -> srl.setRefreshing(active));

    }

    @Override
    public void showMessage(String msg) {
        ((BaseActivity) getActivity()).showMessage(msg);
    }

    @Override
    public void showErrorMessage(String message) {
        ((BaseActivity) getActivity()).showMessageError(message);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == 999) {

                CourseEntity courseEntity = (CourseEntity) data.getSerializableExtra("course");


                if (courseEntity != null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("course", courseEntity);
                    nextActivity(getActivity(), bundle, ChapterActivity.class, false);
                }

            }

        }
    }

    public void openChapter(Bundle bundle) {
        if (bundle != null) {
            nextActivity(getActivity(), bundle, ChapterActivity.class, false);
        }

    }
}
