package com.cerezaconsulting.coreproject.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cerezaconsulting.coreproject.R;
import com.cerezaconsulting.coreproject.core.BaseActivity;
import com.cerezaconsulting.coreproject.core.BaseFragment;
import com.cerezaconsulting.coreproject.core.ScrollChildSwipeRefreshLayout;
import com.cerezaconsulting.coreproject.data.model.CourseEntity;
import com.cerezaconsulting.coreproject.presentation.adapters.CourseAdapter;
import com.cerezaconsulting.coreproject.presentation.contracts.CourseContract;
import com.cerezaconsulting.coreproject.presentation.presenters.communicator.CommunicatorCourseItem;
import com.cerezaconsulting.coreproject.utils.ProgressDialogCustom;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miguel on 15/03/17.
 */

public class CourseFragment extends BaseFragment implements CourseContract.View{

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
    private ProgressDialogCustom mProgressDialogCustom;
    private CourseAdapter courseAdapter;
    private LinearLayoutManager layoutManager;
    private CourseContract.Presenter presenter;

    public static CourseFragment newInstance() {
        return new CourseFragment();
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
        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.black),
                ContextCompat.getColor(getActivity(), R.color.dark_gray),
                ContextCompat.getColor(getActivity(), R.color.black)
        );
        refreshLayout.setScrollUpChild(complatinsList);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            presenter.start();
            }
        });

        fabAddTask.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getContext());
        complatinsList.setLayoutManager(layoutManager);

        courseAdapter = new CourseAdapter(new ArrayList<CourseEntity>(),(CommunicatorCourseItem) presenter);
        complatinsList.setAdapter(courseAdapter);
        mProgressDialogCustom = new ProgressDialogCustom(getContext(), "Cargando...");
        presenter.start();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void getCourses(ArrayList<CourseEntity> list) {
        courseAdapter.setItems(list);
        if(list.size()!=0){
            noComplatins.setVisibility(View.GONE);
        }
    }

    @Override
    public void detailCourse(CourseEntity courseEntity) {

    }

    @Override
    public void setPresenter(CourseContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });

    }

    @Override
    public void showMessage(String msg) {
        ((BaseActivity) getActivity()).showMessage(msg);
    }

    @Override
    public void showErrorMessage(String message) {
        ((BaseActivity) getActivity()).showMessageError(message);
    }
}
