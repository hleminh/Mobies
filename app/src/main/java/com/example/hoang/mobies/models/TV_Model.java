package com.example.hoang.mobies.models;

import com.example.hoang.mobies.network.get_people.KnownForObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Inpriron on 6/17/2017.
 */

public class TV_Model implements Serializable{
    private String original_name;
    private int id;
    private String name;
    private int vote_count;
    private float vote_average;
    private String poster_path;
    private String first_air_date;
    private float popularity;
    private List<Integer> genre_ids;
    private String original_language;
    private String backdrop_path;
    private String overview;
    private List<String> origin_country;


    public String getOriginal_name() {
        return original_name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVote_count() {
        return vote_count;
    }

    public float getVote_average() {
        return vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public float getPopularity() {
        return popularity;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public List<String> getOrigin_country() {
        return origin_country;
    }

    @Override
    public String toString() {
        return String.format("name: %s, id: %d",original_name,id);
    }

    public TV_Model(KnownForObject knownForObject) {
        original_name=knownForObject.getOriginal_name();
        id=knownForObject.getId();
        name=knownForObject.getName();
        vote_average=knownForObject.getVote_average();
        vote_count=knownForObject.getVote_count();
        poster_path=knownForObject.getPoster_path();
        first_air_date= knownForObject.getFirst_air_date();
        popularity=knownForObject.getPopularity();
        genre_ids=knownForObject.getGenre_ids();
        original_language=knownForObject.getOriginal_language();
        backdrop_path=knownForObject.getBackdrop_path();
        overview=knownForObject.getOverview();
        origin_country=knownForObject.getOrigin_country();
    }
}
