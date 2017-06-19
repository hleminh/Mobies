package com.example.hoang.mobies.network.get_tv;

import com.example.hoang.mobies.network.get_movies.MainObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/19/2017.
 */

public interface GetRecommendTvService {
    @GET("tv/{tv_id}/recommendations")
    Call<MainTvObject> getRecommendTv(@Path("tv_id") int tv_id, @Query("api_key")
            String api_key, @Query("language") String language, @Query("page") int page);

}
