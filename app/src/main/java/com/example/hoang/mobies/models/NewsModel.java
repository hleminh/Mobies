package com.example.hoang.mobies.models;

/**
 * Created by Inpriron on 6/23/2017.
 */

public class NewsModel {
    String  author;
    String title;
    String description;
    String url;
    String urlToImage;
    String publishedAt;

    @Override
    public String toString() {
        return String.format("title: %s",title);
    }
}
