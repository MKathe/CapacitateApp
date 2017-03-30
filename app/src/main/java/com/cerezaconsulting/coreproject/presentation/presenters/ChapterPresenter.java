package com.cerezaconsulting.coreproject.presentation.presenters;

import android.content.Context;

import com.cerezaconsulting.coreproject.data.local.SessionManager;
import com.cerezaconsulting.coreproject.data.model.ChapterEntity;
import com.cerezaconsulting.coreproject.data.remote.ServiceFactory;
import com.cerezaconsulting.coreproject.data.remote.request.ChapterRequest;
import com.cerezaconsulting.coreproject.presentation.contracts.ChapterContract;
import com.cerezaconsulting.coreproject.presentation.presenters.communicator.CommunicatorChapterItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by miguel on 16/03/17.
 */

public class ChapterPresenter implements ChapterContract.Presenter,CommunicatorChapterItem {

    private ChapterContract.View mView;
    private SessionManager sessionManager;

    public ChapterPresenter(ChapterContract.View mView, Context context) {
        this.mView = mView;
        sessionManager = new SessionManager(context);
        this.mView.setPresenter(this);
    }

    @Override
    public void getChapter(String id) {
        mView.setLoadingIndicator(true);
        String token = sessionManager.getUserToken();
        ChapterRequest chapterRequest = ServiceFactory.createService(ChapterRequest.class);
        Call<ArrayList<ChapterEntity>> call = chapterRequest.getChapters(token,id);
        call.enqueue(new Callback<ArrayList<ChapterEntity>>() {
            @Override
            public void onResponse(Call<ArrayList<ChapterEntity>> call, Response<ArrayList<ChapterEntity>> response) {
                if(!mView.isActive()){
                    return;
                }
                mView.setLoadingIndicator(false);
                if(response.isSuccessful()){
                    mView.getChapter(response.body());
                }
                else{
                    mView.showErrorMessage("Hubo un error, por favor intente más tarde");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ChapterEntity>> call, Throwable t) {
                if(!mView.isActive()){
                    return;
                }
                mView.setLoadingIndicator(false);
                mView.showErrorMessage("No se pudo conectar al servidor, por favor intente más tarde");
            }
        });
    }

    @Override
    public void start() {

    }

    @Override
    public void onClick(ChapterEntity chapterEntity) {
        mView.detailChapter(chapterEntity);
    }

    @Override
    public void openQuestions() {

    }
}
