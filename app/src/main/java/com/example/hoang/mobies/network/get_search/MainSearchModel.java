package com.example.hoang.mobies.network.get_search;

import com.example.hoang.mobies.models.MultiSearchModel;

import java.util.List;

/**
 * Created by Inpriron on 6/19/2017.
 */

public class MainSearchModel {
    List<MultiSearchModel> results;

    public List<MultiSearchModel> getResults() {
        return results;
    }

    public void setResults(List<MultiSearchModel> results) {
        this.results = results;
    }
}
