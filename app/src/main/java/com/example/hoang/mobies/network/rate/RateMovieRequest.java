package com.example.hoang.mobies.network.rate;

import retrofit2.http.POST;

/**
 * Created by Inpriron on 6/20/2017.
 */

public class RateMovieRequest {
    float value;

    public RateMovieRequest(float value) {
        this.value = value;
    }
}
