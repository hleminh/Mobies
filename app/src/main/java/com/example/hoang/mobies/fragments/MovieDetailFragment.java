package com.example.hoang.mobies.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.models.MovieModel;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment {
    @BindView(R.id.iv_poster_movie_detail)
    ImageView ivPoster;
    @BindView(R.id.tv_movie_name_movie_detail)
    TextView tvMovieName;
    @BindView(R.id.tv_movie_release_date_movie_detail)
    TextView tvMobieReleaseDate;
    @BindView(R.id.rb_movie_detail)
    RatingBar rbMovieDetail;
    @BindView(R.id.tv_rating_detail)
    TextView tvRatingDetail;
    @BindView(R.id.iv_back_drop_movie_detail)
    ImageView ivBackDrop;
    private MovieModel movieModel;

    public MovieDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        movieModel = (MovieModel) getArguments().getSerializable("MovieDetail");

        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + movieModel.getPoster_path()).into(ivPoster);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + movieModel.getBackdrop_path()).into(ivBackDrop);
        tvMovieName.setText(movieModel.getTitle());
        tvRatingDetail.setText(movieModel.getVote_count() + " Ratings");
        rbMovieDetail.setRating(movieModel.getVote_average()/2);
        tvMobieReleaseDate.setText(movieModel.getRelease_date());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getActivity().getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}
