package com.example.hoang.mobies.network.get_movies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dell on 7/12/2017.
 */

public interface GetRandomService {
    @GET("discover/movie")
    Call<MainObject> getRandomMovies(@Query("api_key") String api_key,
                                     @Query("language") String language,
                                     @Query("page") int page,
                                     @Query("region") String region,
                                     @Query("vote_average.gte") int vote,
                                     @Query("include_video") boolean include_video);

}
