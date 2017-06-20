package com.example.hoang.mobies.models;

import com.example.hoang.mobies.network.get_people.KnownForObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Inpriron on 6/18/2017.
 */

public class PeopleModel implements Serializable{
    float popularity;
    int id;
    String profile_path;
    String name;
    List<KnownForObject> known_for;
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

    public List<KnownForObject> getKnown_for() {
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
