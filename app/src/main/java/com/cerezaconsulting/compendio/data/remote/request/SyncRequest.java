package com.cerezaconsulting.compendio.data.remote.request;

import com.cerezaconsulting.compendio.data.model.UserEntity;
import com.cerezaconsulting.compendio.data.response.ActivityResponse;
import com.cerezaconsulting.compendio.data.response.ResponseActivitySync;
import com.cerezaconsulting.compendio.data.response.ResponseCompleteSyncResponse;
import com.cerezaconsulting.compendio.data.response.ReviewResponse;
import com.cerezaconsulting.compendio.data.response.TrackReviewResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Interfaces de URL para logueo.
 */
public interface SyncRequest {


    @POST("sync/activity")
    Call<Void> syncActivity(@Header("Authorization") String token, @Body ArrayList<ActivityResponse> activityResponse);

    @POST("sync/review")
    Call<Void> syncReview(@Header("Authorization") String token , @Body ArrayList<ReviewResponse> reviewResponse);


    @GET("sync/complete")
    Call<ResponseCompleteSyncResponse> syncComplete(@Header("Authorization") String token, @Query("username") String username);


    @POST("sync/activity")
    Observable<ResponseActivitySync> uploadActivity(@Header("Authorization") String token, @Body ActivityResponse activityResponse);


    @POST("sync/review")
    Observable<ResponseActivitySync> uploadReview(@Header("Authorization") String token, @Body ReviewResponse activityResponse);

}
