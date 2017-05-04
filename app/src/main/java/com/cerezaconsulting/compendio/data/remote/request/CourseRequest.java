package com.cerezaconsulting.compendio.data.remote.request;

import com.cerezaconsulting.compendio.data.model.CourseEntity;
import com.cerezaconsulting.compendio.data.model.TrainingEntity;
import com.cerezaconsulting.compendio.data.response.DoubtResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by miguel on 15/03/17.
 */

public interface CourseRequest {
    @GET("training/employee/{idCompany}/training")
    Call<ArrayList<CourseEntity>> getCourses(@Header("Authorization") String token,
                                             @Path("idCompany") String idCompany);


    @GET("training/employee/{idUser}/training/{idTraining}")
    Call<TrainingEntity> downloadCourses(@Header("Authorization") String token,
                                         @Path("idUser") String idUser,
                                         @Path("idTraining") String idTraining);



    @GET("training/employee/{idUser}/training/{idTraining}")
    Observable<TrainingEntity> downloadCourse(@Header("Authorization") String token,
                                               @Path("idUser") String idUser,
                                               @Path("idTraining") String idTraining);



    @POST("training/employee/{idUser}/training/{idTraining}/doubt")
    Call<Void> sendDoubt(@Header("Authorization") String token,
                         @Path("idUser") int idUser,
                         @Path("idTraining") String idTraining,
                         @Body DoubtResponse doubt) ;
}
