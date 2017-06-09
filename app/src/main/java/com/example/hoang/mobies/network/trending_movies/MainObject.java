package com.example.hoang.mobies.network.trending_movies;

import com.example.hoang.mobies.models.TrendingModel;

import java.util.List;

/**
 * Created by Inpriron on 6/9/2017.
 */

public class MainObject {
    List<TrendingModel> results;

    public List<TrendingModel> getResults() {
        return results;
    }

    public void setResults(List<TrendingModel> results) {
        this.results = results;
    }
}
