package com.example.hoang.mobies.network.get_people;

import com.example.hoang.mobies.models.PeopleModel;
import com.example.hoang.mobies.network.get_movies.MainObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/20/2017.
 */

public interface GetDetailPeopleService {
    @GET("person/{person_id}")
    Call<PeopleModel> getDetailPeople(@Path("person_id") int person_id,@Query("api_key") String api_key);

}
