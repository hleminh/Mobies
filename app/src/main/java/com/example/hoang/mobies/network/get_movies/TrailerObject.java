package com.example.hoang.mobies.network.get_movies;

/**
 * Created by Inpriron on 6/19/2017.
 */

public class TrailerObject {
    String id;
    String key;

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return id+" "+key;
    }
}
