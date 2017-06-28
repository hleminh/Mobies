package com.example.hoang.mobies.models;

import java.io.Serializable;

/**
 * Created by Inpriron on 6/23/2017.
 */

public class NewsModel implements Serializable {
    String author;
    String title;
    String description;
    String url;
    String urlToImage;
    String publishedAt;

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    @Override
    public String toString() {
        return String.format("title: %s", title);
    }
}
