package com.example.hoang.mobies.network.get_movies;

import java.util.List;

/**
 * Created by Inpriron on 6/19/2017.
 */

public class MainTrailerObject {
    int id;
    List<TrailerObject> results;

    public List<TrailerObject> getResults() {
        return results;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
