package com.example.hoang.mobies.models;

import com.example.hoang.mobies.network.get_people.KnownForObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Inpriron on 6/17/2017.
 */

public class TV_Model extends RealmObject implements Serializable{
    private String original_name;
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
    @Ignore
    private List<String> origin_country;
    float rating;
    private String belongTo;

    private String genresString;

    public String getGenresString() {
        return genresString;
    }

    public void setGenresString(String genresString) {
        this.genresString = genresString;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public TV_Model() {
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }


    public TV_Model(int id, float rating) {
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
    public TV_Model(MultiSearchModel multiSearchModel)
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
        origin_country=multiSearchModel.getOrigin_country();
    }
}
