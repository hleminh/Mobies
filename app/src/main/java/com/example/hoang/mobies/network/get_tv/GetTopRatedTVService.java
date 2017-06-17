package com.example.hoang.mobies.network.get_tv;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/17/2017.
 */

public interface GetTopRatedTVService {
    @GET("tv/top_rated")
    Call<MainTvObject> getTopRatedTv(@Query("api_key") String api_key, @Query("language") String language
            , @Query("page") int page);
}
