package com.cerezaconsulting.compendio.presentation.presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cerezaconsulting.compendio.R;
import com.cerezaconsulting.compendio.data.remote.ServiceFactory;
import com.cerezaconsulting.compendio.data.local.SessionManager;
import com.cerezaconsulting.compendio.data.model.AccessTokenEntity;
import com.cerezaconsulting.compendio.data.model.UserEntity;
import com.cerezaconsulting.compendio.data.remote.request.LoginRequest;
import com.cerezaconsulting.compendio.data.remote.request.PerfilRequest;
import com.cerezaconsulting.compendio.data.response.LoginResponse;
import com.cerezaconsulting.compendio.presentation.contracts.LoginContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by manu on 08/08/16.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View mView;
    private Context context;
    private final SessionManager mSessionManager;


    public LoginPresenter(@NonNull LoginContract.View view,
                          Context context) {
        this.mView = view;
        this.mSessionManager = new SessionManager(context);
        this.mView.setPresenter(this);
        this.context = context;
    }


    @Override
    public void start() {

    }


    @Override
    public void loginNormal(String username, String password) {
        LoginRequest loginService =
                ServiceFactory.createService(LoginRequest.class);

        LoginResponse loginResponse = new LoginResponse(username, password);

        Call<AccessTokenEntity> call = loginService.basicLogin(loginResponse);
        mView.setLoadingIndicator(true);
        call.enqueue(new Callback<AccessTokenEntity>() {
            @Override
            public void onResponse(Call<AccessTokenEntity> call, Response<AccessTokenEntity> response) {
                if (response.isSuccessful()) {

                    getPerfil(response.body());

                } else {
                    mView.setLoadingIndicator(false);

                    switch (response.code()) {

                        case 404:
                            mView.showErrorMessage(context.getResources().getString(R.string.not_user));
                            break;
                        case 401:
                            mView.showErrorMessage(context.getResources().getString(R.string.invalid_password));
                            break;
                        case 400:
                            mView.showErrorMessage(context.getResources().getString(R.string.password_long));
                            break;
                        default:
                            mView.showErrorMessage("Error desconocido");
                            break;
                    }

                }
            }

            @Override
            public void onFailure(Call<AccessTokenEntity> call, Throwable t) {
                mView.setLoadingIndicator(false);
                mView.showErrorMessage(context.getResources().getString(R.string.no_connect));
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
