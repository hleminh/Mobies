package com.example.hoang.mobies.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

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
    @BindView(R.id.iv_fullscreen)
    ImageView ivFullScreen;
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
        WebSettings webSettings = wvNewsDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            wvNewsDetail.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            wvNewsDetail.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        wvNewsDetail.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvNewsDetail.loadUrl(newsModel.getUrl());
        ivFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.setVisibility(View.GONE);
            }
        });
        toolbar.setTitle(newsModel.getTitle());
        return view;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
