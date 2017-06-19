package com.example.hoang.mobies.network.get_search;

import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.network.get_movies.MainObject;
import com.example.hoang.mobies.network.get_search.MainSearchModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/19/2017.
 */

public interface GetSearchMoviesService {
    @GET("search/movie")
    Call<MainObject> getMovieSearch(@Query("query") String query, @Query("api_key") String api_key, @Query("language") String language,
                                    @Query("page") int page);
}
