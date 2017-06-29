package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.NewsModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsDetailFragment extends Fragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.wv_news_detail)
    WebView wvNewsDetail;
    private NewsModel newsModel;

    public NewsDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
        ButterKnife.bind(this, view);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorTextPrimary));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getFragmentManager());
            }
        });
        newsModel = (NewsModel) getArguments().getSerializable("NewsDetail");
        toolbar.setTitle(newsModel.getTitle());
        WebSettings webSettings = wvNewsDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvNewsDetail.loadUrl(newsModel.getUrl());
        return view;
    }



}
