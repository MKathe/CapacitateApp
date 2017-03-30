package com.cerezaconsulting.coreproject.data.remote.request;

import com.cerezaconsulting.coreproject.data.model.CourseEntity;
import com.cerezaconsulting.coreproject.data.model.TrainingEntity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by miguel on 15/03/17.
 */

public interface CourseRequest {
    @GET("admin/company/{idCompany}/course")
    Call<ArrayList<CourseEntity>> getCourses(@Header("Authorization") String token,
                                             @Path("idCompany") String idCompany);


    @GET("training/employee/{idUser}/training/{idTraining}")
    Call<TrainingEntity> downloadCourses(@Header("Authorization") String token,
                                         @Path("idUser") String idUser,
                                         @Path("idTraining") String idTraining);
}
