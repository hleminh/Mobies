package com.example.hoang.mobies.network.get_tv;

import com.example.hoang.mobies.network.get_movies.MainObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dell on 7/12/2017.
 */

public interface GetRandomTvService {
    @GET("discover/movie")
    Call<MainTvObject> getRandomMovies(@Query("api_key") String api_key,
                                     @Query("language") String language,
                                     @Query("page") int page,
                                     @Query("region") String region,
                                     @Query("vote_average.gte") int vote);
}
