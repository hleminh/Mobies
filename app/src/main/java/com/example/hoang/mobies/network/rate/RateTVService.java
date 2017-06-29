package com.example.hoang.mobies.network.rate;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/29/2017.
 */

public interface RateTVService {
    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("tv/{tv_id}/rating")
    Call<RateMoviesResponse> rateMovie(@Path("tv_id") int tv_id, @Body RateMovieRequest rateMovieRequest, @Query("api_key")
            String api_key, @Query("guest_session_id") String guest_session_id);
}

