package com.example.hoang.mobies.models;

import io.realm.RealmObject;

/**
 * Created by dell on 7/2/2017.
 */

public class GenreIDs extends RealmObject{

    int id;

    public GenreIDs() {
    }

    public GenreIDs(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
