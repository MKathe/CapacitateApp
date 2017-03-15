package com.cerezaconsulting.coreproject.data.remote.request;

import com.cerezaconsulting.coreproject.data.model.AccessTokenEntity;
import com.cerezaconsulting.coreproject.data.model.UserEntity;

import retrofit2.Call;
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


}
