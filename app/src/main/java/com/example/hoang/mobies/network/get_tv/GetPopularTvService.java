package com.example.hoang.mobies.network.get_tv;

import com.example.hoang.mobies.network.get_movies.MainObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/17/2017.
 */

public interface GetPopularTvService {
    @GET("tv/popular")
    Call<MainTvObject> getPopularTV(@Query("api_key") String api_key, @Query("language") String language
            , @Query("page") int page);
}
