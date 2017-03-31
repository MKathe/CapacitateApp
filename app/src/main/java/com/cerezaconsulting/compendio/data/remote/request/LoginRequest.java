package com.cerezaconsulting.compendio.data.remote.request;

import com.cerezaconsulting.compendio.data.model.AccessTokenEntity;
import com.cerezaconsulting.compendio.data.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Interfaces de URL para logueo.
 */
public interface LoginRequest {


    @POST("auth/login")
    Call<AccessTokenEntity> basicLogin(@Body LoginResponse loginResponse);

}
