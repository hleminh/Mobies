package com.example.hoang.mobies.network.guest_session;

import com.example.hoang.mobies.network.get_movies.MainObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/20/2017.
 */

public interface CreateGuestSessionService {
    @GET("authentication/guest_session/new")
    Call<GuestObject> getNewGuest(@Query("api_key") String api_key);
}
