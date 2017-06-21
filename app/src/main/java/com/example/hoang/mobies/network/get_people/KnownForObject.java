package com.example.hoang.mobies.network.get_people;

import java.util.List;

/**
 * Created by tonto on 6/19/2017.
 */

public class KnownForObject {
    int id; // cua ca 2 cai
    boolean video; //cua film

    List<Integer> genre_ids; // cua ca 2 cai
    String backdrop_path;// cua ca 2 cai
    boolean adult;// cua ca 2 cai
    String overview;
    String release_date;// cua ca 2 cai
    String name; // cua tv
    String original_name; // cua tv
    String first_air_date;// cua tv

    public void setOrigin_country(List<String> origin_country) {
        this.origin_country = origin_country;
    }

    List<String> origin_country;

    String media_type; // 'movie' cho phim, 'tv' cho tvshow
    float vote_average;// cua ca 2 cai
    int vote_count;// cua ca 2 cai
    String title; // cua film

    float popularity; // cua ca 2 cai
    String poster_path; // cua ca 2 cai
    String original_language;// cua ca 2 cai
    String original_title;// cua film

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public List<String> getOrigin_country() {
        return origin_country;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
