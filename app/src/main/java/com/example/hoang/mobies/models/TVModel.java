package com.example.hoang.mobies.models;

import com.example.hoang.mobies.network.get_people.KnownForObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Inpriron on 6/17/2017.
 */

public class TVModel extends RealmObject implements Serializable{
    private String original_name;
    @PrimaryKey
    private int id;
    private String name;
    private int vote_count;
    private float vote_average;
    private String poster_path;
    private String first_air_date;
    private float popularity;
    @Ignore
    private List<Integer> genre_ids;
    private String original_language;
    private String backdrop_path;
    private String overview;
    private boolean checkLater;

    public boolean isCheckLater() {
        return checkLater;
    }

    public void setCheckLater(boolean checkLater) {
        this.checkLater = checkLater;
    }

    //    @Ignore
//    private List<String> origin_country;
    float rating;
    private String belongTo;

    private String genresString;

    public String getGenresString() {
        return genresString;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

//    public void setOrigin_country(List<String> origin_country) {
//        this.origin_country = origin_country;
//    }

    public void setGenresString(String genresString) {
        this.genresString = genresString;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public TVModel() {
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }


    public TVModel(int id, float rating) {
        this.id = id;
        this.rating = rating;
    }

    public float getRating() {

        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

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

//    public List<String> getOrigin_country() {
//        return origin_country;
//    }

    @Override
    public String toString() {
        return String.format("name: %s, id: %d",original_name,id);
    }

    public TVModel(KnownForObject knownForObject) {
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
//        origin_country=knownForObject.getOrigin_country();
    }
    public TVModel(MultiSearchModel multiSearchModel)
    {
        original_name=multiSearchModel.getOriginal_name();
        id=multiSearchModel.getId();
        name=multiSearchModel.getName();
        vote_average=multiSearchModel.getVote_average();
        vote_count=multiSearchModel.getVote_count();
        poster_path=multiSearchModel.getPoster_path();
        first_air_date= multiSearchModel.getFirst_air_date();
        popularity=multiSearchModel.getPopularity();
        genre_ids=multiSearchModel.getGenre_ids();
        original_language=multiSearchModel.getOriginal_language();
        backdrop_path=multiSearchModel.getBackdrop_path();
        overview=multiSearchModel.getOverview();
//        origin_country=multiSearchModel.getOrigin_country();
    }
}
