package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.Utils.Utils;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI;

/**
 * A simple {@link Fragment} subclass.
 */
public class YoutubePlayFragment extends YouTubePlayerSupportFragment implements YouTubePlayer.OnInitializedListener {
    @BindView(R.id.youtube)
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer player;
    private List<String> urls = new ArrayList<>();

    public YoutubePlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_youtube_play, container, false);
        urls.addAll(getArguments().getStringArrayList("keys"));
        ButterKnife.bind(this, view);
        youTubePlayerView.initialize(Utils.getYoutubeKey(), this);
        return view;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        player = youTubePlayer;
        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE | FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        player.loadVideos(urls);
        player.play();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
