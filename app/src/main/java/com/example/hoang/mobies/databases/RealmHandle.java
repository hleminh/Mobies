package com.example.hoang.mobies.databases;

import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.TVModel;

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
        String genres = "";
        for (int i = 0; i < movieModel.getGenre_ids().size(); i++) {
            for (GenresModel genreModel : RealmHandle.getInstance().getListGenresModel()) {
                if (genreModel.getId() == movieModel.getGenre_ids().get(i).intValue()) {
                    if (i == movieModel.getGenre_ids().size() - 1) {
                        genres += genreModel.getName();
                    } else genres += genreModel.getName() + ", ";
                }
            }
        }

        movieModel.setGenresString(genres);
        movieModel.setBelongTo("WatchList");
        realm.copyToRealm(movieModel);
        realm.commitTransaction();
    }

    public boolean isAdded(MovieModel model) {
        if (realm.where(MovieModel.class).equalTo("belongTo","WatchList").equalTo("id", model.getId()).findAll().size() == 0) {
            return false;
        } else return true;
    }

    public boolean isAdded(TVModel model) {
        if (realm.where(TVModel.class).equalTo("belongTo","WatchList").equalTo("id", model.getId()).findAll().size() == 0) {
            return false;
        } else return true;
    }

    public void addToWatchList(TVModel tvModel) {
        realm.beginTransaction();
        String genres = "";
        for (int i = 0; i < tvModel.getGenre_ids().size(); i++) {
            for (GenresModel genreModel : RealmHandle.getInstance().getListGenresModel()) {
                if (genreModel.getId() == tvModel.getGenre_ids().get(i).intValue()) {
                    if (i == tvModel.getGenre_ids().size() - 1) {
                        genres += genreModel.getName();
                    } else genres += genreModel.getName() + ", ";
                }
            }
        }
        tvModel.setGenresString(genres);
        tvModel.setBelongTo("WatchList");
        realm.copyToRealm(tvModel);
        realm.commitTransaction();
    }

    public List<MovieModel> getListMoviesWatchList() {
        return realm.where(MovieModel.class).equalTo("belongTo", "WatchList").findAll();
    }

    public List<TVModel> getListTVWatchList() {
        return realm.where(TVModel.class).equalTo("belongTo", "WatchList").findAll();
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

    public void deleteFromWatchList(final TVModel model) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<TVModel> tv = realm.where(TVModel.class).equalTo("id", model.getId()).findAll();
                tv.deleteAllFromRealm();
            }
        });
    }


}
