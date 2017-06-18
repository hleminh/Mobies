package com.example.hoang.mobies.network.get_movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/18/2017.
 */

public interface GetRecommendMovieService {
    @GET("movie/{movie_id}/recommendations")
    Call<MainObject> getRecommendMovies(@Path("movie_id") int movie_id, @Query("api_key")
            String api_key, @Query("language") String language, @Query("page") int page);

}
