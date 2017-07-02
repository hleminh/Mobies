package com.example.hoang.mobies.databases;

import com.example.hoang.mobies.models.GenreIDs;
import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.TV_Model;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dell on 7/2/2017.
 */

public class RealmHandle {
    Realm realm = Realm.getDefaultInstance();
    private static RealmHandle instance;

    public static RealmHandle getInstance() {
        if (instance == null) instance = new RealmHandle();
        return instance;
    }

    public void addGenresToRealm(GenresModel genresModel) {
        realm.beginTransaction();
        realm.copyToRealm(genresModel);
        realm.commitTransaction();
    }

    public List<GenresModel> getListGenresModel() {
        return realm.where(GenresModel.class).findAll();
    }

    public void addToWatchList(MovieModel movieModel) {
        realm.beginTransaction();
        for (Integer id : movieModel.getGenre_ids()) {
            movieModel.getGenreIDsRealmList().add(new GenreIDs(id.intValue()));
        }
        movieModel.setBelongTo("WatchList");
        realm.copyToRealm(movieModel);
        realm.commitTransaction();
    }

    public boolean isAdded(MovieModel model) {
        if (realm.where(MovieModel.class).equalTo("belongTo","WatchList").equalTo("id", model.getId()).findAll().size() == 0) {
            return false;
        } else return true;
    }

    public boolean isAdded(TV_Model model) {
        if (realm.where(TV_Model.class).equalTo("belongTo","WatchList").equalTo("id", model.getId()).findAll().size() == 0) {
            return false;
        } else return true;
    }

    public void addToWatchList(TV_Model tvModel) {
        realm.beginTransaction();
        for (Integer id : tvModel.getGenre_ids()) {
            tvModel.getGenreIDsRealmList().add(new GenreIDs(id.intValue()));
        }
        tvModel.setBelongTo("WatchList");
        realm.copyToRealm(tvModel);
        realm.commitTransaction();
    }

    public List<MovieModel> getListMoviesWatchList() {
        return realm.where(MovieModel.class).equalTo("belongTo", "WatchList").findAll();
    }

    public List<TV_Model> getListTVWatchList() {
        return realm.where(TV_Model.class).equalTo("belongTo", "WatchList").findAll();
    }

    public void deleteFromWatchList(final MovieModel model) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<MovieModel> movie = realm.where(MovieModel.class).equalTo("id", model.getId()).findAll();
                movie.deleteAllFromRealm();
            }
        });
    }

    public void deleteFromWatchList(final TV_Model model) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TV_Model> tv = realm.where(TV_Model.class).equalTo("id", model.getId()).findAll();
                tv.deleteAllFromRealm();
            }
        });
    }


}
