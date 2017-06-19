package com.example.hoang.mobies.fragments;


import android.graphics.Movie;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.Utils.Utils;
import com.example.hoang.mobies.adapters.CastsAdapter;
import com.example.hoang.mobies.adapters.MoviesByCategoriesAdapter;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.CastModel;
import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_cast.GetCastOfAMovieService;
import com.example.hoang.mobies.network.get_cast.MainCastObject;
import com.example.hoang.mobies.network.get_movies.GetRecommendMovieService;
import com.example.hoang.mobies.network.get_movies.MainObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;
import static com.example.hoang.mobies.network.RetrofitFactory.DEFAULT_PAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.LANGUAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener {
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
    @BindView(R.id.tv_genre)
    TextView tvGenre;
    @BindView(R.id.rv_casts)
    RecyclerView rvCasts;
    private MovieModel movieModel;
    @BindView(R.id.tv_plot)
    TextView tvPlot;
    @BindView(R.id.rv_recommended)
    RecyclerView rvRecommended;
    @BindView(R.id.toolbar)
    Toolbar tbDetail;
    private List<CastModel> castModelList;
    private List<MovieModel> movieModelList;
    private MoviesByCategoriesAdapter moviesByCategoriesAdapter;
    private CastsAdapter castsAdapter;

    public MovieDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        movieModel = (MovieModel) getArguments().getSerializable("MovieDetail");
        loadData();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + movieModel.getPoster_path()).into(ivPoster);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + movieModel.getBackdrop_path()).into(ivBackDrop);
        tvMovieName.setText(movieModel.getTitle());
        tvRatingDetail.setText(movieModel.getVote_count() + " Ratings");
        rbMovieDetail.setRating(movieModel.getVote_average() / 2);
        tvMobieReleaseDate.setText(movieModel.getRelease_date());
        tvPlot.setText(movieModel.getOverview());

        String genres = "";
        for (int i = 0; i < movieModel.getGenre_ids().size(); i++) {
            for (GenresModel genreModel : Utils.genresModelList) {
                if (genreModel.getId() == movieModel.getGenre_ids().get(i).intValue()) {
                    if (i == movieModel.getGenre_ids().size() - 1) {
                        genres += genreModel.getName();
                    } else genres += genreModel.getName() + ", ";
                }
            }
        }
        tvGenre.setText(genres);

        castsAdapter = new CastsAdapter(castModelList, getContext());
        rvCasts.setAdapter(castsAdapter);

        moviesByCategoriesAdapter = new MoviesByCategoriesAdapter(movieModelList, getContext());
        moviesByCategoriesAdapter.setOnItemClickListener(this);
        rvRecommended.setAdapter(moviesByCategoriesAdapter);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvRecommended.setLayoutManager(linearLayoutManager1);
        rvCasts.setLayoutManager(linearLayoutManager2);

        tbDetail.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        tbDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getActivity().getSupportFragmentManager());
            }
        });

    }

    private void loadData() {
        castModelList = new ArrayList<>();
        movieModelList = new ArrayList<>();
        loadCasts();
        loadRecommended();
    }

    private void loadRecommended() {
        GetRecommendMovieService getRecommendMovieService = RetrofitFactory.getInstance().createService(GetRecommendMovieService.class);
        getRecommendMovieService.getRecommendMovies(movieModel.getId(), API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                for (MovieModel movieModel : response.body().getResults()) {
                    movieModelList.add(movieModel);
                }
                moviesByCategoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadCasts() {
        GetCastOfAMovieService getCastOfAMovieService = RetrofitFactory.getInstance().createService(GetCastOfAMovieService.class);
        getCastOfAMovieService.getCastOfAMovie(movieModel.getId(), API_KEY).enqueue(new Callback<MainCastObject>() {
            @Override
            public void onResponse(Call<MainCastObject> call, Response<MainCastObject> response) {
                MainCastObject mainCastObject = response.body();
                List<CastModel> castModels = mainCastObject.getCast();
                for (CastModel castModel : castModels) {
                    if (castModelList.size() < 5)
                        castModelList.add(castModel);
                }
                castsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainCastObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();

            }
        });
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

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof MovieModel) {
            MovieModel movieModel = (MovieModel) v.getTag();
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MovieDetail", movieModel);
            movieDetailFragment.setArguments(bundle);
            ScreenManager.openFragment(getFragmentManager(), movieDetailFragment, R.id.fl_container, true, false);
        }
    }
}
