package com.example.hoang.mobies.network.rate;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/20/2017.
 */

public interface RateMoviesService {
    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("movie/{movie_id}/rating")
    Call<RateMoviesResponse> rateMovie(@Path("movie_id") int movie_id, @Body RateMovieRequest rateMovieRequest, @Query("api_key")
            String api_key, @Query("guest_session_id") String guest_session_id);
}
