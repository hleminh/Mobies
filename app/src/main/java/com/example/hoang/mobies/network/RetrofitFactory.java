package com.example.hoang.mobies.network;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Inpriron on 6/9/2017.
 */

public class RetrofitFactory {
    private static Retrofit retrofit;
    public static String API_KEY="edf1f4d5b56b3b1d9454f2b090695246";
    public static String LANGUAGE="en-US";
    public static int DEFAULT_PAGE=1;
    public static String REGION="us";
    public static String GUEST_ID;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String GUEST_ID_PREFERENCE = "guest_id";

    public static SharedPreferences SHAREED_PREFERENCES;
    public static RetrofitFactory retrofitFactory= new RetrofitFactory();
    public static RetrofitFactory getInstance(){
        return retrofitFactory;
    }
    private RetrofitFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static <ServiceClass> ServiceClass createService(Class<ServiceClass> serviceClass)
    {
        return retrofit.create(serviceClass);
    }
}
