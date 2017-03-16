package com.cerezaconsulting.coreproject.presentation.presenters;

import android.content.Context;

import com.cerezaconsulting.coreproject.data.local.SessionManager;
import com.cerezaconsulting.coreproject.data.model.CourseEntity;
import com.cerezaconsulting.coreproject.data.model.UserEntity;
import com.cerezaconsulting.coreproject.data.remote.ServiceFactory;
import com.cerezaconsulting.coreproject.data.remote.request.CourseRequest;
import com.cerezaconsulting.coreproject.presentation.contracts.CourseContract;
import com.cerezaconsulting.coreproject.presentation.presenters.communicator.CommunicatorCourseItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by miguel on 15/03/17.
 */

public class CoursePresenter implements CourseContract.Presenter,CommunicatorCourseItem {

    private CourseContract.View mView;
    private SessionManager sessionManager;

    public CoursePresenter(CourseContract.View mView, Context context) {
        this.mView = mView;
        sessionManager = new SessionManager(context);
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.setLoadingIndicator(true);
        String token = sessionManager.getUserToken();
        UserEntity userEntity = sessionManager.getUserEntity();
        String idCompany = "";
        if(userEntity.getCompany()!=null && userEntity.getCompany().getId()!=null){
            idCompany=userEntity.getCompany().getId();
        }
        CourseRequest courseRequest = ServiceFactory.createService(CourseRequest.class);
        Call<ArrayList<CourseEntity>> call = courseRequest.getCourses(token,idCompany);
        call.enqueue(new Callback<ArrayList<CourseEntity>>() {
            @Override
            public void onResponse(Call<ArrayList<CourseEntity>> call, Response<ArrayList<CourseEntity>> response) {
                if(!mView.isActive()){
                    return;
                }
                mView.setLoadingIndicator(false);
                if(response.isSuccessful()){
                    mView.getCourses(response.body());
                }
                else {
                    mView.showErrorMessage("Hubo un error, por favor intentar más tarde");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CourseEntity>> call, Throwable t) {
                if(!mView.isActive()){
                    return;
                }
                mView.setLoadingIndicator(false);
                mView.showErrorMessage("No se puede conectar con el servidor, por favor intentar más tarde");
            }
        });
    }

    @Override
    public void onClick(CourseEntity courseEntity) {

    }
}
