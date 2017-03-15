package com.cerezaconsulting.coreproject.data.remote.request;

import com.cerezaconsulting.coreproject.data.model.AccessTokenEntity;
import com.cerezaconsulting.coreproject.data.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Interfaces de URL para logueo.
 */
public interface LoginRequest {


    @POST("auth/login")
    Call<AccessTokenEntity> basicLogin(@Body LoginResponse loginResponse);

}
