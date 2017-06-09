package com.example.hoang.mobies.network.get_genres;

import com.example.hoang.mobies.models.GenresModel;

import java.util.List;

/**
 * Created by Inpriron on 6/9/2017.
 */

public class MainGenresObject {
    public List<GenresModel> getGenres() {
        return genres;
    }

    public void setGenres(List<GenresModel> genres) {
        this.genres = genres;
    }

    List<GenresModel> genres;
}
