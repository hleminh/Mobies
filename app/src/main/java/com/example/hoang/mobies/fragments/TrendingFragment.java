package com.example.hoang.mobies.fragments;

import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.TV_Model;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.hoang.mobies.activities.MainActivity.RATED_MOVIE_LIST;
import static com.example.hoang.mobies.activities.MainActivity.RATED_TV_LIST;

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
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + movieModel.getBackdrop_path()).placeholder(R.drawable.no_image_movie_tv_landscape_final).fit().into(ivTrendingImage);
            tvTrendingName.setText(movieModel.getTitle());
            rbTrending.setRating(movieModel.getVote_average() / 2);
            boolean check=true;
            for (MovieModel model : RATED_MOVIE_LIST) {
                if (model.getId() == movieModel.getId()) {
                    tvTrendingRating.setText(String.format("%,d",movieModel.getVote_count()+1) + " Ratings");
                    check=false;
                    break;
                }
            }
            if(check)
            {
                tvTrendingRating.setText(String.format("%,d",movieModel.getVote_count()) + " Ratings");
            }
        }

        if (tvModel != null) {
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + tvModel.getBackdrop_path()).placeholder(R.drawable.no_image_movie_tv_landscape_final).fit().into(ivTrendingImage);
            tvTrendingName.setText(tvModel.getName());
            tvTrendingRating.setText(String.format("%,d",tvModel.getVote_count()) + " Ratings");
            for (TV_Model model : RATED_TV_LIST) {
                if (model.getId() == tvModel.getId()) {
                    tvTrendingRating.setText(String.format("%,d",tvModel.getVote_count()+1) + " Ratings");
                    break;
                }
            }
            rbTrending.setRating(tvModel.getVote_average() / 2);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() instanceof MovieModel) {
                    MovieModel movieModel = (MovieModel) v.getTag();
                    MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("MovieDetail", movieModel);
                    movieDetailFragment.setArguments(bundle);
                    ScreenManager.openFragment(getActivity().getSupportFragmentManager(), movieDetailFragment, R.id.drawer_layout, true, false);
                }
                if (v.getTag() instanceof TV_Model) {
                    TV_Model tv_model = (TV_Model) v.getTag();
                    TVShowDetailFragment tvShowDetailFragment = new TVShowDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("TVDetail", tv_model);
                    tvShowDetailFragment.setArguments(bundle);
                    ScreenManager.openFragment(getActivity().getSupportFragmentManager(), tvShowDetailFragment, R.id.drawer_layout, true, false);
                }
            }
        });
    }
}
