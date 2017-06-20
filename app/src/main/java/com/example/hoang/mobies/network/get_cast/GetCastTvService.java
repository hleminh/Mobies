package com.example.hoang.mobies.network.get_cast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/20/2017.
 */

public interface GetCastTvService {
    @GET("tv/{tv_id}/credits")
    Call<MainCastObject> getCastOfAMovie(@Path("tv_id") int tv_id, @Query("api_key")
            String api_key);
}
