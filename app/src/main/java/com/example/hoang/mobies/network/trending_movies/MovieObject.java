package com.example.hoang.mobies.network.trending_movies;

import java.util.List;

/**
 * Created by Inpriron on 6/9/2017.
 */

public class MovieObject {
    private int id;
    private String title;
    private String poster_path;
    private String original_language;
    private String original_title;
    private List<Integer> genre_ids;
    private String backdrop_path;
    private String overview;
    private String release_date;

    @Override
    public String toString() {
        return String.format("ID:%s-title:%s",id,title);
    }
}
