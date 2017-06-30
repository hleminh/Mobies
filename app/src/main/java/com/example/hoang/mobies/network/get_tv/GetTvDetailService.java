package com.example.hoang.mobies.network.get_tv;

import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.TV_Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/30/2017.
 */

public interface GetTvDetailService {
    @GET("tv/{tv_id}")
    Call<TV_Model> getDetailTv(@Path("tv_id") int tv_id, @Query("api_key")
            String api_key, @Query("language") String language);
}
