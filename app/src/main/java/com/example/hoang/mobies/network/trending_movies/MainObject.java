package com.example.hoang.mobies.network.trending_movies;

import com.example.hoang.mobies.models.MovieModel;


import java.util.List;

/**
 * Created by Inpriron on 6/9/2017.
 */

public class MainObject {
    List<MovieModel> results;

    public List<MovieModel> getResults() {
        return results;
    }

    public void setResults(List<MovieModel> results) {
        this.results = results;
    }
}
