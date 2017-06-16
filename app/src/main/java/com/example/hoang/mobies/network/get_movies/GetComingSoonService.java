package com.example.hoang.mobies.network.get_movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by Inpriron on 6/16/2017.
 */

public interface GetComingSoonService {
    @GET("movie/upcoming")
    Call<MainObject> getComingSoonMovies(@Query("api_key") String api_key, @Query("language") String language, @Query("page") int page,@Query("region") String region);
}
