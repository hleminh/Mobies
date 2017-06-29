package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.Utils.Utils;
import com.example.hoang.mobies.activities.MainActivity;
import com.example.hoang.mobies.adapters.MoviesByCategoriesAdapter;
import com.example.hoang.mobies.adapters.TrendingPagerAdapter;
import com.example.hoang.mobies.managers.ScreenManager;
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
import com.github.rubensousa.gravitysnaphelper.GravityPagerSnapHelper;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

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
public class MoviesFragment extends Fragment implements View.OnClickListener {
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
    @BindView(R.id.tv_coming_soon)
    TextView tvComingSoon;
    @BindView(R.id.tv_top_rated)
    TextView tvTopRated;
    @BindView(R.id.tv_in_cinemas)
    TextView tvInCinemas;
    @BindView(R.id.tv_no_connection)
    TextView tvNoConnection;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.pb_category_loading)
    ProgressBar pbCategory;
//    @BindView(R.id.tv_category_no_connection)
//    TextView tvCategoryNoConnect;

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
    private int failConnection;


    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        failConnection = 0;
        loadData();
        setupUI(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        trendingPagerAdapter = new TrendingPagerAdapter(getChildFragmentManager(), trendingMoviesList, null);
        vpTrending.setAdapter(trendingPagerAdapter);
        vpTrending.setOffscreenPageLimit(3);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        tlCategory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                rvMovies.stopScroll();
                rvMovies.removeAllViews();
                pbCategory.setVisibility(View.VISIBLE);
//                if (tvCategoryNoConnect.getVisibility() == View.VISIBLE) {
//                    tvCategoryNoConnect.setVisibility(View.GONE);
//                }
                loadMoviesByCaterogy((genresModelList.get(tab.getPosition()).getId() + ""));
                rvMovies.getLayoutManager().scrollToPosition(0);
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

        moviesByCategoriesAdapter.setOnItemClickListener(this);
        topRatedAdapter.setOnItemClickListener(this);
        comingSoonAdapter.setOnItemClickListener(this);
        inCinemasAdapter.setOnItemClickListener(this);

        rvMovies.setHasFixedSize(true);
        rvMovies.setAdapter(moviesByCategoriesAdapter);
        rvComingSoon.setAdapter(comingSoonAdapter);
        rvInCinemas.setAdapter(inCinemasAdapter);
        rvTopRated.setAdapter(topRatedAdapter);

        rvMovies.setLayoutManager(linearLayoutManager1);
        rvTopRated.setLayoutManager(linearLayoutManager2);
        rvInCinemas.setLayoutManager(linearLayoutManager3);
        rvComingSoon.setLayoutManager(linearLayoutManager4);

        SnapHelper snapHelper1 = new GravitySnapHelper(Gravity.START);
        SnapHelper snapHelper2 = new GravitySnapHelper(Gravity.START);
        SnapHelper snapHelper3 = new GravitySnapHelper(Gravity.START);
        SnapHelper snapHelper4 = new GravitySnapHelper(Gravity.START);

        snapHelper1.attachToRecyclerView(rvMovies);
        snapHelper2.attachToRecyclerView(rvTopRated);
        snapHelper3.attachToRecyclerView(rvInCinemas);
        snapHelper4.attachToRecyclerView(rvComingSoon);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.movies);
        MainActivity.navigationView.setCheckedItem(R.id.nav_movie);

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
                tvTopRated.setVisibility(View.VISIBLE);
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults()) {
                    topRatedMoviesList.add(movieModel);
                }
                topRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                failConnection++;
                if (failConnection == 5) {
                    pbLoading.setVisibility(View.GONE);
                    tvNoConnection.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void loadInCinemas() {
        GetInCinemasMoviesService getInCinemasMoviesService = RetrofitFactory.getInstance().createService(GetInCinemasMoviesService.class);
        getInCinemasMoviesService.getInCinemaMovies(API_KEY, LANGUAGE, DEFAULT_PAGE, REGION).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                tvInCinemas.setVisibility(View.VISIBLE);
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults()) {
                    inCinemasMoviesList.add(movieModel);
                }
                inCinemasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                failConnection++;

            }
        });
    }

    private void loadComingSoon() {
        GetComingSoonService getComingSoonService = RetrofitFactory.getInstance().createService(GetComingSoonService.class);
        getComingSoonService.getComingSoonMovies(API_KEY, LANGUAGE, DEFAULT_PAGE, REGION).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                tvComingSoon.setVisibility(View.VISIBLE);
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults()) {
                    comingSoonMoviesList.add(movieModel);
                }
                comingSoonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                failConnection++;

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
                pbCategory.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                failConnection++;
                pbCategory.setVisibility(View.GONE);
//                tvCategoryNoConnect.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadGenres() {
        Utils.genresModelList = new ArrayList<>();
        GetGenresService getGenresService = RetrofitFactory.getInstance().createService(GetGenresService.class);
        getGenresService.getAllGenres(API_KEY, LANGUAGE).enqueue(new Callback<MainGenresObject>() {
            @Override
            public void onResponse(Call<MainGenresObject> call, Response<MainGenresObject> response) {
                MainGenresObject mainGenresObject = response.body();
                for (GenresModel genresModel : mainGenresObject.getGenres()) {
                    genresModelList.add(genresModel);
                    Utils.genresModelList.add(genresModel);
                    tlCategory.addTab(tlCategory.newTab().setText(genresModel.getName()));
                }
            }

            @Override
            public void onFailure(Call<MainGenresObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                failConnection++;

            }
        });
    }

    private void loadTrendingMovies() {
        GetTrendingMoviesService getTrendingMoviesService = RetrofitFactory.getInstance().createService(GetTrendingMoviesService.class);
        getTrendingMoviesService.getTrendingMovies(API_KEY, LANGUAGE, DEFAULT_PAGE, REGION).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                pbLoading.setVisibility(View.GONE);
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults()) {
                    trendingMoviesList.add(movieModel);
                }
                trendingPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                failConnection++;

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v.getTag() instanceof MovieModel) {
            MovieModel movieModel = (MovieModel) v.getTag();
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MovieDetail", movieModel);
            movieDetailFragment.setArguments(bundle);
            ScreenManager.openFragment(getFragmentManager(), movieDetailFragment, R.id.drawer_layout, true, false);
        }
    }
}
