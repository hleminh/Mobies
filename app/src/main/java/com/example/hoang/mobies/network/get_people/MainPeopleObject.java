package com.example.hoang.mobies.network.get_people;

import com.example.hoang.mobies.models.PeopleModel;

import java.util.List;

/**
 * Created by Inpriron on 6/18/2017.
 */

public class MainPeopleObject {
    List<PeopleModel> results;

    public List<PeopleModel> getResults() {
        return results;
    }

    public void setResults(List<PeopleModel> results) {
        this.results = results;
    }
}
