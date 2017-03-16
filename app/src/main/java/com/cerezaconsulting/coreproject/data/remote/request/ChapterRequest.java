package com.cerezaconsulting.coreproject.data.remote.request;

import com.cerezaconsulting.coreproject.data.model.ChapterEntity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by miguel on 16/03/17.
 */

public interface ChapterRequest {
    @GET("admin/course/{idCourse}/chapter")
    Call<ArrayList<ChapterEntity>> getChapters(@Header("Authorization") String token, @Path("idCourse") String idCourse);
}
