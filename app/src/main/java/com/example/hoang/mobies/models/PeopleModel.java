package com.example.hoang.mobies.models;

import com.example.hoang.mobies.network.get_people.KnownForObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Inpriron on 6/18/2017.
 */

public class PeopleModel implements Serializable {
    float popularity;
    int id;
    String name;
    List<KnownForObject> known_for;
    String backdrop_path;
    boolean adult;
    List<String> also_known_as;
    String biography;
    String birthday;
    String deathday;
    int gender;
    String homepage;
    String imdb_id;

    String place_of_birth;
    String profile_path;

    public List<String> getAlso_known_as() {
        return also_known_as;
    }

    public String getBiography() {
        return biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public int getGender() {
        return gender;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public String getPlace_of_birth() {
        return place_of_birth;
    }

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
        return String.format("id:%d, name: %s", id, name);
    }
}
