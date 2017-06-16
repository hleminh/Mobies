package com.example.hoang.mobies.network.get_movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/16/2017.
 */

public interface GetInCinemasMoviesService {
    @GET("movie/now_playing")
    Call<MainObject> getInCinemaMovies(@Query("api_key") String api_key, @Query("language") String language,
                                       @Query("page") int page,@Query("region") String region);
}
