package com.example.hoang.mobies.network.get_genres;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/9/2017.
 */

public interface GetGenresService {
    @GET("genre/movie/list")
    Call<MainGenresObject> getAllGenres(@Query("api_key") String api_key,@Query("language") String language);
}
