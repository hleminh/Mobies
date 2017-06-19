package com.example.hoang.mobies.network.get_search;

import com.example.hoang.mobies.models.PeopleModel;
import com.example.hoang.mobies.network.get_people.MainPeopleObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/19/2017.
 */

public interface GetSearchPeopleService {
    @GET("search/person")
    Call<MainPeopleObject> getPersonSearch(@Query("query") String query, @Query("api_key") String api_key, @Query("language") String language,
                                           @Query("page") int page);
}
