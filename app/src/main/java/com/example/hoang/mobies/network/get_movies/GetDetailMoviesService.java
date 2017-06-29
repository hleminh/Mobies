package com.example.hoang.mobies.network.get_movies;

import com.example.hoang.mobies.models.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/29/2017.
 */

public interface GetDetailMoviesService {
    @GET("movie/{movie_id}")
    Call<MovieModel> getDetailMovie(@Path("movie_id") int movie_id, @Query("api_key")
            String api_key, @Query("language") String language);
}
