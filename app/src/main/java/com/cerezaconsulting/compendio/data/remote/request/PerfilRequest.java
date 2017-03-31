package com.cerezaconsulting.compendio.data.remote.request;

import com.cerezaconsulting.compendio.data.model.UserEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Interfaces de URL para logueo.
 */
public interface PerfilRequest {


    @GET("me")
    Call<UserEntity> getUser(@Header("Authorization") String token);


}
