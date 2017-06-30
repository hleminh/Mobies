package com.example.hoang.mobies.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.util.Log;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;


import com.example.hoang.mobies.R;

import com.example.hoang.mobies.Utils.Utils;
import com.example.hoang.mobies.fragments.CelebFragment;
import com.example.hoang.mobies.fragments.NewsFragment;
import com.example.hoang.mobies.fragments.TVShowsFragment;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.GenresModel;

import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.NewsModel;

import com.example.hoang.mobies.models.TV_Model;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_movies.GetDetailMoviesService;
import com.example.hoang.mobies.network.get_movies.GetTrailerService;


import com.example.hoang.mobies.fragments.MoviesFragment;
import com.example.hoang.mobies.network.get_movies.MainObject;
import com.example.hoang.mobies.network.get_movies.MainTrailerObject;
import com.example.hoang.mobies.network.get_movies.TrailerObject;
import com.example.hoang.mobies.network.get_news.GetNewService;
import com.example.hoang.mobies.network.get_news.MainNewsObject;

import com.example.hoang.mobies.network.get_tv.GetTvDetailService;
import com.example.hoang.mobies.network.get_tv.MainTvObject;
import com.example.hoang.mobies.network.guest_session.CreateGuestSessionService;
import com.example.hoang.mobies.network.guest_session.GuestObject;
import com.example.hoang.mobies.network.rate.GetRatedMoviesService;
import com.example.hoang.mobies.network.rate.GetRatedTVService;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;

import static com.example.hoang.mobies.network.RetrofitFactory.GUEST_ID;
import static com.example.hoang.mobies.network.RetrofitFactory.GUEST_ID_PREFERENCE;
import static com.example.hoang.mobies.network.RetrofitFactory.LANGUAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.MyPREFERENCES;
import static com.example.hoang.mobies.network.RetrofitFactory.SHAREED_PREFERENCES;
import static com.example.hoang.mobies.network.RetrofitFactory.retrofitFactory;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Menu mMenu;
    private static List<GenresModel> genresModelList;
    public static List<MovieModel> RATED_MOVIE_LIST = new ArrayList<>();
    public static List<TV_Model> RATED_TV_LIST = new ArrayList<>();
    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    public static NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.colorStatusBar));
        }

        SHAREED_PREFERENCES = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        GUEST_ID = SHAREED_PREFERENCES.getString(GUEST_ID_PREFERENCE, null);
        setUpGuestID();



//        GetDetailMoviesService getDetailMoviesService= retrofitFactory.getInstance().createService(GetDetailMoviesService.class);
//        getDetailMoviesService.getDetailMovie(297762,API_KEY,LANGUAGE).enqueue(new Callback<MovieModel>() {
//            @Override
//            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
//                Log.d("detail",response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<MovieModel> call, Throwable t) {
//
//            }
//        });
        GetTvDetailService getTvDetailService = retrofitFactory.getInstance().createService(GetTvDetailService.class);
        getTvDetailService.getDetailTv(57243, API_KEY, LANGUAGE).enqueue(new Callback<TV_Model>() {
            @Override
            public void onResponse(Call<TV_Model> call, Response<TV_Model> response) {
                Log.d("detail",response.body().toString());
            }

            @Override
            public void onFailure(Call<TV_Model> call, Throwable t) {

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.movies);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void setUpGuestID() {
        if (GUEST_ID == null) {
            CreateGuestSessionService createGuestSessionService = RetrofitFactory.getInstance().createService(CreateGuestSessionService.class);
            createGuestSessionService.getNewGuest(API_KEY).enqueue(new Callback<GuestObject>() {
                @Override
                public void onResponse(Call<GuestObject> call, Response<GuestObject> response) {
                    GUEST_ID = response.body().getGuest_session_id();
                    SharedPreferences.Editor editor = SHAREED_PREFERENCES.edit();
                    editor.putString(GUEST_ID_PREFERENCE, GUEST_ID);
                    editor.commit();
                    if (Utils.genresModelList == null)
                        displayStartScreen();
                }

                @Override
                public void onFailure(Call<GuestObject> call, Throwable t) {

                }
            });
        } else {
            loadRatedList();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    private void displayStartScreen() {
        ScreenManager.openFragment(getSupportFragmentManager(), new MoviesFragment(), R.id.fl_container, false, false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), SearchResultsActivity.class)));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setIconified(true);
                searchView.clearFocus();
                mMenu.findItem(R.id.search).collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_celeb) {
            CelebFragment celebFragment = (CelebFragment) getSupportFragmentManager().findFragmentByTag("CelebFragment");
            if (celebFragment != null) {
                if (!celebFragment.isVisible())
                    ScreenManager.openFragment(getSupportFragmentManager(), new CelebFragment(), R.id.fl_container, true, false);
            }
            if (celebFragment == null) {
                ScreenManager.openFragment(getSupportFragmentManager(), new CelebFragment(), R.id.fl_container, true, false);
            }
        } else if (id == R.id.nav_tvshow) {
            TVShowsFragment tvShowsFragment = (TVShowsFragment) getSupportFragmentManager().findFragmentByTag("TVShowsFragment");
            if (tvShowsFragment != null) {
                if (!tvShowsFragment.isVisible())
                    ScreenManager.openFragment(getSupportFragmentManager(), new TVShowsFragment(), R.id.fl_container, true, false);
            }
            if (tvShowsFragment == null) {
                ScreenManager.openFragment(getSupportFragmentManager(), new TVShowsFragment(), R.id.fl_container, true, false);
            }
        } else if (id == R.id.nav_movie) {
            MoviesFragment moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag("MoviesFragment");
            if (moviesFragment != null) {
                if (!moviesFragment.isVisible())
                    ScreenManager.openFragment(getSupportFragmentManager(), new MoviesFragment(), R.id.fl_container, true, false);
            }
            if (moviesFragment == null) {
                ScreenManager.openFragment(getSupportFragmentManager(), new MoviesFragment(), R.id.fl_container, true, false);
            }
        } else if (id == R.id.nav_new) {
            NewsFragment newsFragment = (NewsFragment) getSupportFragmentManager().findFragmentByTag("NewsFragment");
            if (newsFragment != null) {
                if (!newsFragment.isVisible()) {
                    ScreenManager.openFragment(getSupportFragmentManager(), new NewsFragment(), R.id.fl_container, true, false);
                }
            }
            if (newsFragment == null) {
                ScreenManager.openFragment(getSupportFragmentManager(), new NewsFragment(), R.id.fl_container, true, false);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadRatedList() {
        GetRatedMoviesService getRatedMoviesService = retrofitFactory.getInstance().createService(GetRatedMoviesService.class);
        getRatedMoviesService.getRatedMovies(GUEST_ID, API_KEY).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                for (MovieModel movieModel : response.body().getResults()) {
                    RATED_MOVIE_LIST.add(movieModel);

                }
                if (Utils.genresModelList == null)
                    displayStartScreen();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {

            }
        });
        GetRatedTVService getRatedTVService = retrofitFactory.getInstance().createService(GetRatedTVService.class);
        getRatedTVService.getRatedTV(GUEST_ID, API_KEY).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                for (TV_Model tv_model : response.body().getResults()) {
                    RATED_TV_LIST.add(tv_model);

                }
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {

            }
        });

    }

}
