package com.example.hoang.mobies.network.get_cast;

import com.example.hoang.mobies.network.get_movies.MainObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/18/2017.
 */

public interface GetCastOfAMovieService {
    @GET("movie/{movie_id}/credits")
    Call<MainCastObject> getCastOfAMovie(@Path("movie_id") int movie_id, @Query("api_key")
            String api_key);

}
