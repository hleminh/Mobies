package com.example.hoang.mobies.models;

import java.util.List;

/**
 * Created by Inpriron on 6/17/2017.
 */

public class TV_Model {
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

    @Override
    public String toString() {
        return String.format("name: %s, id: %d",original_name,id);
    }
}
