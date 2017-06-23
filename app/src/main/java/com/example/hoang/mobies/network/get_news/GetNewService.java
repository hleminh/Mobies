package com.example.hoang.mobies.network.get_news;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Inpriron on 6/23/2017.
 */

public interface GetNewService {
    @GET("https://newsapi.org/v1/articles?source=entertainment-weekly&apiKey=a02242c4acc9420f81276c830fb08bed")
    Call<MainNewsObject> getNews();

}
