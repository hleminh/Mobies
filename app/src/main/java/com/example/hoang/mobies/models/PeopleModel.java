package com.example.hoang.mobies.models;

import com.example.hoang.mobies.network.get_people.KnownForObject;

import java.util.List;

/**
 * Created by Inpriron on 6/18/2017.
 */

public class PeopleModel {
    float popularity;
    int id;
    String profile_path;
    String name;
    List<KnownForObject> known_for;
    String backdrop_path;
    boolean adult;

    @Override
    public String toString() {
        return String.format("id:%d, name: %s",id,name);
    }
}
