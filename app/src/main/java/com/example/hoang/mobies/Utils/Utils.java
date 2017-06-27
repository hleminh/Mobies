package com.example.hoang.mobies.Utils;

import android.widget.Toast;

import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_genres.GetGenresService;
import com.example.hoang.mobies.network.get_genres.MainGenresObject;

import java.util.ArrayList;
import java.util.List;

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
    public static String capitalize(String s){
         return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
