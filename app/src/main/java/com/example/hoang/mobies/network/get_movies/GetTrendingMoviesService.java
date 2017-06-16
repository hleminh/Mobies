package com.example.hoang.mobies.network.get_movies;

import com.example.hoang.mobies.network.get_movies.MainObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/9/2017.
 */

public interface GetTrendingMoviesService {

    @GET("movie/popular")
    Call<MainObject> getTrendingMovies(@Query("api_key") String api_key, @Query("language") String language
            , @Query("page") int page,@Query("region") String region);
}
