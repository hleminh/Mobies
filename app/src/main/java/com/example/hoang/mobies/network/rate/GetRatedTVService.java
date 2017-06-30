package com.example.hoang.mobies.network.rate;

import com.example.hoang.mobies.network.get_movies.MainObject;
import com.example.hoang.mobies.network.get_tv.MainTvObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/29/2017.
 */

public interface GetRatedTVService {
    @GET("guest_session/{guest_session_id}/rated/tv")
    Call<MainTvObject> getRatedTV(@Path("guest_session_id") String guess_session_id, @Query("api_key")
            String api_key);
}
