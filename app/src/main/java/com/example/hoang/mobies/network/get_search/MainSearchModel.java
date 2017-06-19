package com.example.hoang.mobies.network.get_search;

import com.example.hoang.mobies.models.MediaModel;

import java.util.List;

/**
 * Created by Inpriron on 6/19/2017.
 */

public class MainSearchModel {
    List<MediaModel> results;

    public List<MediaModel> getResults() {
        return results;
    }

    public void setResults(List<MediaModel> results) {
        this.results = results;
    }
}
