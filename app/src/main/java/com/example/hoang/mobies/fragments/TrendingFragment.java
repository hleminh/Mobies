package com.example.hoang.mobies.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.TV_Model;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hoang on 6/9/2017.
 */

public class TrendingFragment extends Fragment {
    @BindView(R.id.iv_trending_image)
    ImageView ivTrendingImage;
    @BindView(R.id.tv_trending_name)
    TextView tvTrendingName;
    @BindView(R.id.tv_trending_rating)
    TextView tvTrendingRating;
    @BindView(R.id.rb_trending)
    RatingBar rbTrending;
    private MovieModel movieModel;
    private TV_Model tvModel;

    public TrendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.item_trending, container, false);
        if (getArguments().getSerializable("TrendingModel") instanceof MovieModel) {
            movieModel = (MovieModel) getArguments().getSerializable("TrendingModel");
            view.setTag(movieModel);
        } else if (getArguments().getSerializable("TrendingModel") instanceof TV_Model) {
            tvModel = (TV_Model) getArguments().getSerializable("TrendingModel");
            view.setTag(tvModel);
        }
        loadData(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData(getView());
    }

    private void loadData(View view) {
        ButterKnife.bind(this, view);
        if (movieModel != null) {
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + movieModel.getBackdrop_path()).into(ivTrendingImage);
            tvTrendingName.setText(movieModel.getTitle());
            tvTrendingRating.setText(movieModel.getVote_count() + " Ratings");
            rbTrending.setRating(movieModel.getVote_average() / 2);
        }

        if (tvModel != null) {
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + tvModel.getBackdrop_path()).into(ivTrendingImage);
            tvTrendingName.setText(tvModel.getName());
            tvTrendingRating.setText(tvModel.getVote_count() + " Ratings");
            rbTrending.setRating(tvModel.getVote_average() / 2);
        }

    }
}
