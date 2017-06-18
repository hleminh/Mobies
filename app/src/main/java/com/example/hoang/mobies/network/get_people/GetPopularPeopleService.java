package com.example.hoang.mobies.network.get_people;

import com.example.hoang.mobies.network.get_movies.MainObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/18/2017.
 */

public interface GetPopularPeopleService {
    @GET("person/popular")
    Call<MainPeopleObject> getPopularPeople(@Query("api_key") String api_key, @Query("language") String language,
                                         @Query("page") int page);
}
