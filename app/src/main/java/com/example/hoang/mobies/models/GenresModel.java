package com.example.hoang.mobies.models;

/**
 * Created by Inpriron on 6/9/2017.
 */

public class GenresModel {
    private int id;
    private String name;

    @Override
    public String toString() {
        return String.format("ID:%d-name:%s",id,name);
    }
}
