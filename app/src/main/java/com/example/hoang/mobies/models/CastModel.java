package com.example.hoang.mobies.models;

/**
 * Created by Inpriron on 6/18/2017.
 */

public class CastModel {
    int cast_id;
    String character;
    String credit_id;
    int gender;
    int id;
    String name;
    int order;
    String profile_path;

    public int getCast_id() {
        return cast_id;
    }

    public String getCharacter() {
        return character;
    }

    public String getCredit_id() {
        return credit_id;
    }

    public int getGender() {
        return gender;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public String getProfile_path() {
        return profile_path;
    }

    @Override
    public String toString() {
        return character;
    }
}
