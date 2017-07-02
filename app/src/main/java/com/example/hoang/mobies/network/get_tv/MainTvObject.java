package com.example.hoang.mobies.network.get_tv;

import com.example.hoang.mobies.models.TVModel;

import java.util.List;

/**
 * Created by Inpriron on 6/17/2017.
 */

public class MainTvObject {
    List<TVModel> results;

    public List<TVModel> getResults() {
        return results;
    }

    public void setResults(List<TVModel> results) {
        this.results = results;
    }
}
