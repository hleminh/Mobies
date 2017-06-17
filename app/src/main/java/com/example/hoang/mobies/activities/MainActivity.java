package com.example.hoang.mobies.activities;

import android.os.Bundle;

import android.util.Log;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.hoang.mobies.R;

import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.TV_Model;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_genres.GetGenresService;
import com.example.hoang.mobies.network.get_genres.MainGenresObject;
import com.example.hoang.mobies.network.get_movies.GetComingSoonService;
import com.example.hoang.mobies.network.get_movies.GetInCinemasMoviesService;
import com.example.hoang.mobies.network.get_movies.GetMovieByGenresService;
import com.example.hoang.mobies.network.get_movies.GetTopRatedMoviesService;
import com.example.hoang.mobies.network.get_movies.GetTrendingMoviesService;
import com.example.hoang.mobies.network.get_movies.MainObject;

import com.example.hoang.mobies.fragments.MoviesFragment;
import com.example.hoang.mobies.network.get_tv.GetPopularTvService;
import com.example.hoang.mobies.network.get_tv.GetTopRatedTVService;
import com.example.hoang.mobies.network.get_tv.MainTvObject;


import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;
import static com.example.hoang.mobies.network.RetrofitFactory.DEFAULT_PAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.LANGUAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.REGION;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetTrendingMoviesService getTrendingMoviesService = RetrofitFactory.getInstance().createService(GetTrendingMoviesService.class);
        getTrendingMoviesService.getTrendingMovies(API_KEY, LANGUAGE, DEFAULT_PAGE, REGION).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults())
                    Log.d("test trending:", movieModel.toString());
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {

            }
        });
        GetGenresService getGenresService = RetrofitFactory.getInstance().createService(GetGenresService.class);
        getGenresService.getAllGenres(API_KEY, LANGUAGE).enqueue(new Callback<MainGenresObject>() {
            @Override
            public void onResponse(Call<MainGenresObject> call, Response<MainGenresObject> response) {
                MainGenresObject mainGenresObject = response.body();
                for (GenresModel genresModel : mainGenresObject.getGenres())
                    Log.d("test genres:", genresModel.toString());
            }

            @Override
            public void onFailure(Call<MainGenresObject> call, Throwable t) {

            }
        });
        GetMovieByGenresService getMovieByGenresService = RetrofitFactory.getInstance().createService(GetMovieByGenresService.class);
        getMovieByGenresService.getMovieByGenres("28", API_KEY, LANGUAGE, "false").enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults())
                    Log.d("test movie genrse:", movieModel.toString());
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {

            }
        });

        GetInCinemasMoviesService getInCinemasMoviesService = RetrofitFactory.getInstance().createService(GetInCinemasMoviesService.class);
        getInCinemasMoviesService.getInCinemaMovies(API_KEY, LANGUAGE, DEFAULT_PAGE, REGION).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults())
                    Log.d("test in cinemas:", movieModel.toString());
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {

            }
        });

        GetComingSoonService getComingSoonService = RetrofitFactory.getInstance().createService(GetComingSoonService.class);
        getComingSoonService.getComingSoonMovies(API_KEY, LANGUAGE, DEFAULT_PAGE, REGION).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults())
                    Log.d("test coming soon:", movieModel.toString());
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {

            }
        });
        GetTopRatedMoviesService getTopRatedMoviesService = RetrofitFactory.getInstance().createService(GetTopRatedMoviesService.class);
        getTopRatedMoviesService.getTopRatedMovies(API_KEY, LANGUAGE, DEFAULT_PAGE, REGION).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults())
                    Log.d("test top rated:", movieModel.toString());
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {

            }
        });

        GetPopularTvService getPopularTvService=RetrofitFactory.getInstance().createService(GetPopularTvService.class);
        getPopularTvService.getPopularTV(API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                MainTvObject mainObject = response.body();
                for (TV_Model tv_model : mainObject.getResults())
                    Log.d("test tv popular:", tv_model.toString());
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Log.d("test","??");
            }
        });
        GetTopRatedTVService getTopRatedTVService=  RetrofitFactory.getInstance().createService(GetTopRatedTVService.class);
        getTopRatedTVService.getTopRatedTv(API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                MainTvObject mainObject = response.body();
                Log.d("size:",mainObject.getResults().size()+"");
                for (TV_Model tv_model : mainObject.getResults())
                    Log.d("test tv top rated:", tv_model.toString());
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Log.d("test","??");
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        displayStartScreen();
    }

    private void displayStartScreen() {
        MoviesFragment moviesFragment = new MoviesFragment();
        changeScreen(moviesFragment, false);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeScreen(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_container, fragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }
}
