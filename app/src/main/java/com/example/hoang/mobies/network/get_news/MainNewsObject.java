package com.example.hoang.mobies.network.get_news;

import com.example.hoang.mobies.models.NewsModel;

import java.util.List;

/**
 * Created by Inpriron on 6/23/2017.
 */

public class MainNewsObject {
    List<NewsModel> articles;

    public List<NewsModel> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsModel> articles) {
        this.articles = articles;
    }
}
