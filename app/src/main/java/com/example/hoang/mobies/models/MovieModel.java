package com.example.hoang.mobies.models;

import com.example.hoang.mobies.network.get_people.KnownForObject;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Hoang on 6/9/2017.
 */

public class MovieModel extends RealmObject implements Serializable {
    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    @Ignore
    private List<Integer> genre_ids;
    private int id;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private float popularity;
    private int vote_count;
    private boolean video;
    private float vote_average;
    private String belongTo;


    private String genresString;

    public String getGenresString() {
        return genresString;
    }

    public void setGenresString(String genresString) {
        this.genresString = genresString;
    }

    public MovieModel() {
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    float rating;


    public MovieModel(int id,float rating) {
        this.id = id;
        this.rating=rating;
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

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
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

    @Override
    public String toString() {
        return String.format("ID:%s-title:%s", id, title);
    }

    public MovieModel(KnownForObject knownForObject) {

        poster_path= knownForObject.getPoster_path();
        overview= knownForObject.getOverview();
        adult= knownForObject.isAdult();
        release_date=knownForObject.getRelease_date();
        genre_ids=knownForObject.getGenre_ids();
        id=knownForObject.getId();
        original_language=knownForObject.getOriginal_language();
        original_title=knownForObject.getOriginal_title();
        title=knownForObject.getTitle();
        backdrop_path=knownForObject.getBackdrop_path();
        popularity=knownForObject.getPopularity();
        vote_count=knownForObject.getVote_count();
        vote_average=knownForObject.getVote_average();
        video=knownForObject.isVideo();
    }
    public MovieModel(MultiSearchModel multiSearchModel)
    {
        poster_path= multiSearchModel.getPoster_path();
        overview= multiSearchModel.getOverview();
        adult= multiSearchModel.isAdult();
        release_date=multiSearchModel.getRelease_date();
        genre_ids=multiSearchModel.getGenre_ids();
        id=multiSearchModel.getId();
        original_language=multiSearchModel.getOriginal_language();
        original_title=multiSearchModel.getOriginal_title();
        title=multiSearchModel.getTitle();
        backdrop_path=multiSearchModel.getBackdrop_path();
        popularity=multiSearchModel.getPopularity();
        vote_count=multiSearchModel.getVote_count();
        vote_average=multiSearchModel.getVote_average();
        video=multiSearchModel.isVideo();
    }
}
