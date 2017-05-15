package com.cerezaconsulting.compendio.data.remote.request;

import com.cerezaconsulting.compendio.data.model.UserEntity;
import com.cerezaconsulting.compendio.data.response.ChangePasswordResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Interfaces de URL para logueo.
 */
public interface PerfilRequest {


    @GET("me")
    Call<UserEntity> getUser(@Header("Authorization") String token);



    @POST("auth/password/change/request")
    Call<Void> requestChangePassword(@Body  ChangePasswordResponse changePasswordResponse);

}
