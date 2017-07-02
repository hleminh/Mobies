package com.example.hoang.mobies.databases;

import com.example.hoang.mobies.models.GenresModel;

import java.util.List;

import io.realm.Realm;

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


}
