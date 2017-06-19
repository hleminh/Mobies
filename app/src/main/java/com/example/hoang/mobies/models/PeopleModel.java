package com.example.hoang.mobies.models;

import java.util.List;

/**
 * Created by Inpriron on 6/18/2017.
 */

public class PeopleModel {
    float popularity;
    int id;
    String profile_path;
    String name;
    List<MediaModel> known_for;
    String backdrop_path;
    boolean adult;


    public float getPopularity() {
        return popularity;
    }

    public int getId() {
        return id;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public String getName() {
        return name;
    }

    public List<MediaModel> getKnown_for() {
        return known_for;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    @Override
    public String toString() {
        return String.format("id:%d, name: %s",id,name);
    }
}
