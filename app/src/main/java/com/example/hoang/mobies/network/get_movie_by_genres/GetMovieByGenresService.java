package com.example.hoang.mobies.network.get_movie_by_genres;

import com.example.hoang.mobies.network.trending_movies.MainObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/9/2017.
 */

public interface GetMovieByGenresService {
    @GET("genre/{genre_id}/movies")
    Call<MainObject> getMovieByGenres(@Path("genre_id") String genre_id, @Query("api_key")
                                      String api_key, @Query("language") String language,
                                      @Query("include_adult") String include_adult);
}
