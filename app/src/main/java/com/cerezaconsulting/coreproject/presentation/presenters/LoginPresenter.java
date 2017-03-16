package com.cerezaconsulting.coreproject.presentation.presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cerezaconsulting.coreproject.data.remote.ServiceFactory;
import com.cerezaconsulting.coreproject.data.local.SessionManager;
import com.cerezaconsulting.coreproject.data.model.AccessTokenEntity;
import com.cerezaconsulting.coreproject.data.model.UserEntity;
import com.cerezaconsulting.coreproject.data.remote.request.LoginRequest;
import com.cerezaconsulting.coreproject.data.remote.request.PerfilRequest;
import com.cerezaconsulting.coreproject.data.response.LoginResponse;
import com.cerezaconsulting.coreproject.presentation.contracts.LoginContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by manu on 08/08/16.
 */
public class LoginPresenter implements LoginContract.Presenter{

    private final LoginContract.View mView;
    private final SessionManager mSessionManager;



    public LoginPresenter(@NonNull LoginContract.View view,
                          Context context) {
        this.mView = view;
        this.mSessionManager = new SessionManager(context);
        this.mView.setPresenter(this);
    }


    @Override
    public void start() {

    }


    @Override
    public void loginNormal(String username, String password) {
        LoginRequest loginService =
                ServiceFactory.createService(LoginRequest.class);

        LoginResponse loginResponse = new LoginResponse(username,password);

        Call<AccessTokenEntity> call = loginService.basicLogin(loginResponse);
        mView.setLoadingIndicator(true);
        call.enqueue(new Callback<AccessTokenEntity>() {
            @Override
            public void onResponse(Call<AccessTokenEntity> call, Response<AccessTokenEntity> response) {
                if (response.isSuccessful()) {

                    getPerfil(response.body());

                } else {
                    mView.setLoadingIndicator(false);
                    mView.showMessage("");
                }
            }

            @Override
            public void onFailure(Call<AccessTokenEntity> call, Throwable t) {
                mView.setLoadingIndicator(false);
                mView.showErrorMessage("");
            }
        });
    }


    public void getPerfil(final AccessTokenEntity accessTokenEntity) {
        PerfilRequest loginService =
                ServiceFactory.createService(PerfilRequest.class);
        Call<UserEntity> call = loginService.getUser(accessTokenEntity.getAccessToken());
        call.enqueue(new Callback<UserEntity>() {
            @Override
            public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                if (response.isSuccessful()) {
                    mView.setLoadingIndicator(false);
                    mSessionManager.openSession(accessTokenEntity);
                    mSessionManager.setUser(response.body());
                    mView.loginSuccess();

                } else {
                    mView.setLoadingIndicator(false);
                    mView.showMessage("");
                }
            }

            @Override
            public void onFailure(Call<UserEntity> call, Throwable t) {
                mView.setLoadingIndicator(false);
                mView.showErrorMessage("");
            }
        });
    }
}
