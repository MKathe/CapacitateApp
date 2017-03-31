package com.cerezaconsulting.compendio.data.remote.request;

import com.cerezaconsulting.compendio.data.model.FragmentEntity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by miguel on 16/03/17.
 */

public interface FragmentRequest {
    @GET("admin/chapter/{idChapter}/fragment")
    Call<ArrayList<FragmentEntity>> getFragments(@Header("Authorization") String token, @Path("idChapter") String idChapter);
}
