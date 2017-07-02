package com.example.hoang.mobies.Utils;

import android.widget.Toast;

import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_genres.GetGenresService;
import com.example.hoang.mobies.network.get_genres.MainGenresObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;
import static com.example.hoang.mobies.network.RetrofitFactory.LANGUAGE;

/**
 * Created by tonto on 6/19/2017.
 */

public class Utils {
    public static List<GenresModel> genresModelList;

    private static String YOUTUBE_KEY = "AIzaSyB9zrN4FoPatH4HyoHf2SS8PND57t3Z8rk";

    public static String getYoutubeKey() {
        return YOUTUBE_KEY;
    }

    public static String capitalize(String s){
         return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
