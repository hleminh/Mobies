package com.example.hoang.mobies.models;

import com.example.hoang.mobies.network.get_people.KnownForObject;

import java.util.List;

/**
 * Created by Inpriron on 6/19/2017.
 */

public class MultiSearchModel {
    String poster_path;
    boolean adult;
    String overview;
    String release_date;
    String original_title;
    List<Integer> genre_ids;
    int id;
    String media_type;
    String original_language;
    String title;
    String backdrop_path;
    float popularity;
    int vote_count;
    boolean video;
    float vote_average;
    String first_air_date;
    List<String> origin_country;
    String name;
    String original_name;
    String profile_path;
    List<KnownForObject> known_for;
    String genresString;
    float rating;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getGenresString() {
        return genresString;
    }

    public void setGenresString(String genresString) {
        this.genresString = genresString;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
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

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
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

    public void setOrigin_country(List<String> origin_country) {
        this.origin_country = origin_country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    public List<KnownForObject> getKnown_for() {
        return known_for;
    }

    public void setKnown_for(List<KnownForObject> known_for) {
        this.known_for = known_for;
    }

    private boolean checkLater;

    public boolean isCheckLater() {
        return checkLater;
    }

    public void setCheckLater(boolean checkLater) {
        this.checkLater = checkLater;
    }

    @Override
    public String toString() {
        return String.format("media_type: %s",media_type);
    }

    public MultiSearchModel(MovieModel movieModel) {
        this.poster_path = movieModel.getPoster_path();
        this.adult = movieModel.isAdult();
        this.overview = movieModel.getOverview();
        this.release_date = movieModel.getRelease_date();
        this.id = movieModel.getId();
        this.media_type = "movie";
        this.backdrop_path = movieModel.getBackdrop_path();
        this.vote_count = movieModel.getVote_count();
        this.video = movieModel.isVideo();
        this.vote_average = movieModel.getVote_average();
        this.genresString = movieModel.getGenresString();
        this.title = movieModel.getTitle();
        this.original_language = movieModel.getOriginal_language();
        this.original_title = movieModel.getOriginal_title();
        this.rating = movieModel.getRating();
        this.checkLater = movieModel.isCheckLater();
    }

    public MultiSearchModel(TVModel tvModel) {
        this.poster_path = tvModel.getPoster_path();
        this.overview = tvModel.getOverview();
        this.id = tvModel.getId();
        this.media_type = "tv";
        this.backdrop_path = tvModel.getBackdrop_path();
        this.vote_count = tvModel.getVote_count();
        this.vote_average = tvModel.getVote_average();
        this.genresString = tvModel.getGenresString();
        this.original_language = tvModel.getOriginal_language();
        this.first_air_date = tvModel.getFirst_air_date();
        this.name = tvModel.getName();
        this.original_name = tvModel.getOriginal_name();
        this.popularity = tvModel.getPopularity();
        this.rating = tvModel.getRating();
        this.checkLater = tvModel.isCheckLater();
    }
}
