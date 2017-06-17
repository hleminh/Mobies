package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.CubeInTransformer;
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.example.hoang.mobies.R;
import com.example.hoang.mobies.adapters.MoviesByCategoriesAdapter;
import com.example.hoang.mobies.adapters.TrendingPagerAdapter;
import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_genres.GetGenresService;
import com.example.hoang.mobies.network.get_genres.MainGenresObject;
import com.example.hoang.mobies.network.get_movies.GetComingSoonService;
import com.example.hoang.mobies.network.get_movies.GetInCinemasMoviesService;
import com.example.hoang.mobies.network.get_movies.GetMovieByGenresService;
import com.example.hoang.mobies.network.get_movies.GetTopRatedMoviesService;
import com.example.hoang.mobies.network.get_movies.GetTrendingMoviesService;
import com.example.hoang.mobies.network.get_movies.MainObject;

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
import static com.example.hoang.mobies.network.RetrofitFactory.REGION;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    @BindView(R.id.vp_trending)
    ViewPager vpTrending;
    @BindView(R.id.tl_category)
    TabLayout tlCategory;
    @BindView(R.id.rv_movies_by_categories)
    RecyclerView rvMovies;
    @BindView(R.id.rv_coming_soon)
    RecyclerView rvComingSoon;
    @BindView(R.id.rv_in_cinemas)
    RecyclerView rvInCinemas;
    @BindView(R.id.rv_top_rated)
    RecyclerView rvTopRated;

    private List<MovieModel> topRatedMoviesList;
    private List<MovieModel> comingSoonMoviesList;
    private List<MovieModel> inCinemasMoviesList;
    private List<MovieModel> trendingMoviesList;
    private List<MovieModel> moviesByCategoryList;
    private List<GenresModel> genresModelList;
    private TrendingPagerAdapter trendingPagerAdapter;
    private MoviesByCategoriesAdapter moviesByCategoriesAdapter;
    private MoviesByCategoriesAdapter topRatedAdapter;
    private MoviesByCategoriesAdapter comingSoonAdapter;
    private MoviesByCategoriesAdapter inCinemasAdapter;


    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        loadData();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        trendingPagerAdapter = new TrendingPagerAdapter(getFragmentManager(), trendingMoviesList);
        vpTrending.setAdapter(trendingPagerAdapter);
        vpTrending.setOffscreenPageLimit(3);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);



        tlCategory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadMoviesByCaterogy((genresModelList.get(tab.getPosition()).getId() + ""));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        moviesByCategoriesAdapter = new MoviesByCategoriesAdapter(moviesByCategoryList, getContext());
        topRatedAdapter = new MoviesByCategoriesAdapter(topRatedMoviesList, getContext());
        comingSoonAdapter = new MoviesByCategoriesAdapter(comingSoonMoviesList, getContext());
        inCinemasAdapter = new MoviesByCategoriesAdapter(inCinemasMoviesList, getContext());

        rvMovies.setAdapter(moviesByCategoriesAdapter);
        rvComingSoon.setAdapter(comingSoonAdapter);
        rvInCinemas.setAdapter(inCinemasAdapter);
        rvTopRated.setAdapter(topRatedAdapter);

        rvMovies.setLayoutManager(linearLayoutManager1);
        rvTopRated.setLayoutManager(linearLayoutManager2);
        rvInCinemas.setLayoutManager(linearLayoutManager3);
        rvComingSoon.setLayoutManager(linearLayoutManager4);
    }

    private void loadData() {
        trendingMoviesList = new ArrayList<>();
        genresModelList = new ArrayList<>();
        moviesByCategoryList = new ArrayList<>();
        inCinemasMoviesList = new ArrayList<>();
        topRatedMoviesList = new ArrayList<>();
        comingSoonMoviesList = new ArrayList<>();

        loadTrendingMovies();
        loadGenres();
        loadComingSoon();
        loadInCinemas();
        loadTopRated();
    }

    private void loadTopRated() {
        GetTopRatedMoviesService getTopRatedMoviesService = RetrofitFactory.getInstance().createService(GetTopRatedMoviesService.class);
        getTopRatedMoviesService.getTopRatedMovies(API_KEY, LANGUAGE, DEFAULT_PAGE, REGION).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults()) {
                    topRatedMoviesList.add(movieModel);
                }
                topRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadInCinemas() {
        GetInCinemasMoviesService getInCinemasMoviesService = RetrofitFactory.getInstance().createService(GetInCinemasMoviesService.class);
        getInCinemasMoviesService.getInCinemaMovies(API_KEY, LANGUAGE, DEFAULT_PAGE, REGION).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults()) {
                    inCinemasMoviesList.add(movieModel);
                }
                inCinemasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComingSoon() {
        GetComingSoonService getComingSoonService = RetrofitFactory.getInstance().createService(GetComingSoonService.class);
        getComingSoonService.getComingSoonMovies(API_KEY, LANGUAGE, DEFAULT_PAGE, REGION).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults()) {
                    comingSoonMoviesList.add(movieModel);
                }
                comingSoonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMoviesByCaterogy(String genreID) {
        moviesByCategoryList.clear();
        GetMovieByGenresService getMovieByGenresService = RetrofitFactory.getInstance().createService(GetMovieByGenresService.class);
        getMovieByGenresService.getMovieByGenres(genreID, API_KEY, LANGUAGE, "false").enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults()) {
                    moviesByCategoryList.add(movieModel);
                }
                moviesByCategoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGenres() {
        GetGenresService getGenresService = RetrofitFactory.getInstance().createService(GetGenresService.class);
        getGenresService.getAllGenres(API_KEY, LANGUAGE).enqueue(new Callback<MainGenresObject>() {
            @Override
            public void onResponse(Call<MainGenresObject> call, Response<MainGenresObject> response) {
                MainGenresObject mainGenresObject = response.body();
                for (GenresModel genresModel : mainGenresObject.getGenres()) {
                    genresModelList.add(genresModel);
                    tlCategory.addTab(tlCategory.newTab().setText(genresModel.getName()));
                }
            }

            @Override
            public void onFailure(Call<MainGenresObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTrendingMovies() {
        GetTrendingMoviesService getTrendingMoviesService = RetrofitFactory.getInstance().createService(GetTrendingMoviesService.class);
        getTrendingMoviesService.getTrendingMovies(API_KEY, LANGUAGE, DEFAULT_PAGE, REGION).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults()) {
                    trendingMoviesList.add(movieModel);
                }
                trendingPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
