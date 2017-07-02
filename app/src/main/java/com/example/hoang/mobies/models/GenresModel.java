package com.example.hoang.mobies.models;

import io.realm.RealmObject;

/**
 * Created by Inpriron on 6/9/2017.
 */

public class GenresModel extends RealmObject{
    private int id;
    private String name;

    @Override
    public String toString() {
        return String.format("ID:%d-name:%s",id,name);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
