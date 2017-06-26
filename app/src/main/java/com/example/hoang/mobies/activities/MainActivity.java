package com.example.hoang.mobies.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;


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

import com.example.hoang.mobies.fragments.CelebFragment;
import com.example.hoang.mobies.fragments.TVShowsFragment;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.GenresModel;

import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.NewsModel;

import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_movies.GetTrailerService;


import com.example.hoang.mobies.fragments.MoviesFragment;
import com.example.hoang.mobies.network.get_movies.MainObject;
import com.example.hoang.mobies.network.get_movies.MainTrailerObject;
import com.example.hoang.mobies.network.get_movies.TrailerObject;
import com.example.hoang.mobies.network.get_news.GetNewService;
import com.example.hoang.mobies.network.get_news.MainNewsObject;

import com.example.hoang.mobies.network.guest_session.CreateGuestSessionService;
import com.example.hoang.mobies.network.guest_session.GuestObject;
import com.example.hoang.mobies.network.rate.GetRatedMoviesService;


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
    public static List<MovieModel> RATED_MOVIE_LIST;

    @BindView(R.id.fl_container)
    FrameLayout flContainer;

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

        if (GUEST_ID == null) {
            CreateGuestSessionService createGuestSessionService = RetrofitFactory.getInstance().createService(CreateGuestSessionService.class);
            createGuestSessionService.getNewGuest(API_KEY).enqueue(new Callback<GuestObject>() {
                @Override
                public void onResponse(Call<GuestObject> call, Response<GuestObject> response) {
                    GUEST_ID = response.body().getGuest_session_id();
                    Log.d("test guest id 1: ", "" + GUEST_ID);
                    SharedPreferences.Editor editor = SHAREED_PREFERENCES.edit();
                    editor.putString(GUEST_ID_PREFERENCE, GUEST_ID);
                    editor.commit();
                }

                @Override
                public void onFailure(Call<GuestObject> call, Throwable t) {

                }
            });
        }

        RATED_MOVIE_LIST = new ArrayList<>();
        GetRatedMoviesService getRatedMoviesService = retrofitFactory.getInstance().createService(GetRatedMoviesService.class);
        getRatedMoviesService.getRatedMovies(GUEST_ID, API_KEY).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                for (MovieModel movieModel : response.body().getResults()) {
                    Log.d("rated movies", movieModel.toString());
                    RATED_MOVIE_LIST.add(movieModel);
                }
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {

            }
        });


//        GetTrailerService getTrailerService = RetrofitFactory.getInstance().createService(GetTrailerService.class);
//        getTrailerService.getMovieTrailer(291805, API_KEY, LANGUAGE).enqueue(new Callback<MainTrailerObject>() {
//            @Override
//            public void onResponse(Call<MainTrailerObject> call, Response<MainTrailerObject> response) {
//                Log.d("test trailer", response.body().getId() + "");
//                for (TrailerObject trailerObject : response.body().getResults())
//                    Log.d("test trailer:", trailerObject.toString());
//
//            }
//
//            @Override
//            public void onFailure(Call<MainTrailerObject> call, Throwable t) {
//
//            }
//        });


//        GetNewService getNewService = RetrofitFactory.getInstance().createService(GetNewService.class);
//        getNewService.getNews().enqueue(new Callback<MainNewsObject>() {
//            @Override
//            public void onResponse(Call<MainNewsObject> call, Response<MainNewsObject> response) {
//                for (NewsModel newsModel : response.body().getArticles()) {
//                    Log.d("test news:", newsModel.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MainNewsObject> call, Throwable t) {
//
//            }
//        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.movies);

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
        this.mMenu=menu;
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
            ScreenManager.openFragment(getSupportFragmentManager(), new CelebFragment(), R.id.fl_container, true, false);
            getSupportActionBar().setTitle(R.string.celeb);
        } else if (id == R.id.nav_tvshow) {
            ScreenManager.openFragment(getSupportFragmentManager(), new TVShowsFragment(), R.id.fl_container, true, false);
            getSupportActionBar().setTitle(R.string.tv_shows);
        } else if (id == R.id.nav_movie) {
            ScreenManager.openFragment(getSupportFragmentManager(), new MoviesFragment(), R.id.fl_container, true, false);
            getSupportActionBar().setTitle(R.string.movies);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
