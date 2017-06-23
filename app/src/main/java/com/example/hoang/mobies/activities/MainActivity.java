package com.example.hoang.mobies.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;


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
import com.example.hoang.mobies.models.MultiSearchModel;
import com.example.hoang.mobies.models.NewsModel;
import com.example.hoang.mobies.models.PeopleModel;
import com.example.hoang.mobies.models.TV_Model;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_movies.GetTrailerService;
import com.example.hoang.mobies.network.get_movies.MainObject;

import com.example.hoang.mobies.fragments.MoviesFragment;
import com.example.hoang.mobies.network.get_movies.MainTrailerObject;
import com.example.hoang.mobies.network.get_movies.TrailerObject;
import com.example.hoang.mobies.network.get_news.GetNewService;
import com.example.hoang.mobies.network.get_news.MainNewsObject;
import com.example.hoang.mobies.network.get_people.MainPeopleObject;
import com.example.hoang.mobies.network.get_search.GetMultiSearchService;
import com.example.hoang.mobies.network.get_search.GetSearchMoviesService;
import com.example.hoang.mobies.network.get_search.GetSearchPeopleService;
import com.example.hoang.mobies.network.get_search.GetSearchTvService;
import com.example.hoang.mobies.network.get_search.MainSearchModel;
import com.example.hoang.mobies.network.get_tv.MainTvObject;
import com.example.hoang.mobies.network.guest_session.CreateGuestSessionService;
import com.example.hoang.mobies.network.guest_session.GuestObject;
import com.example.hoang.mobies.network.rate.RateMovieRequest;
import com.example.hoang.mobies.network.rate.RateMoviesResponse;
import com.example.hoang.mobies.network.rate.RateMoviesService;


import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;
import static com.example.hoang.mobies.network.RetrofitFactory.DEFAULT_PAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.GUEST_ID;
import static com.example.hoang.mobies.network.RetrofitFactory.GUEST_ID_PREFERENCE;
import static com.example.hoang.mobies.network.RetrofitFactory.LANGUAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.MyPREFERENCES;
import static com.example.hoang.mobies.network.RetrofitFactory.SHAREED_PREFERENCES;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static List<GenresModel> genresModelList;


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




        GetSearchMoviesService getSearchMoviesService = RetrofitFactory.getInstance().createService(GetSearchMoviesService.class);
        getSearchMoviesService.getMovieSearch("now you", API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                MainObject mainObject = response.body();
                for (MovieModel movieModel : mainObject.getResults()) {
                    Log.d("test search movie:", movieModel.toString());
                }
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {

            }
        });

        GetSearchTvService getSearchTvService = RetrofitFactory.getInstance().createService(GetSearchTvService.class);
        getSearchTvService.getTVSearch("now you", API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                for (TV_Model tv_model : response.body().getResults())
                    Log.d("test serach tv:", tv_model.toString());
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {

            }
        });

        GetSearchPeopleService getSearchPeopleService = RetrofitFactory.getInstance().createService(GetSearchPeopleService.class);
        getSearchPeopleService.getPersonSearch("gal gadot", API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainPeopleObject>() {
            @Override
            public void onResponse(Call<MainPeopleObject> call, Response<MainPeopleObject> response) {
                for (PeopleModel peopleModel : response.body().getResults())
                    Log.d("test search ppl:", peopleModel.toString());
            }

            @Override
            public void onFailure(Call<MainPeopleObject> call, Throwable t) {

            }
        });

        GetTrailerService getTrailerService = RetrofitFactory.getInstance().createService(GetTrailerService.class);
        getTrailerService.getMovieTrailer(291805, API_KEY, LANGUAGE).enqueue(new Callback<MainTrailerObject>() {
            @Override
            public void onResponse(Call<MainTrailerObject> call, Response<MainTrailerObject> response) {
                Log.d("test trailer", response.body().getId() + "");
                for (TrailerObject trailerObject : response.body().getResults())
                    Log.d("test trailer:", trailerObject.toString());

            }

            @Override
            public void onFailure(Call<MainTrailerObject> call, Throwable t) {

            }
        });
//        GUEST_ID = SHAREED_PREFERENCES.getString(GUEST_ID_PREFERENCE, null);
//        if (GUEST_ID == null) {
//            CreateGuestSessionService createGuestSessionService = RetrofitFactory.getInstance().createService(CreateGuestSessionService.class);
//            createGuestSessionService.getNewGuest(API_KEY).enqueue(new Callback<GuestObject>() {
//                @Override
//                public void onResponse(Call<GuestObject> call, Response<GuestObject> response) {
//                    GUEST_ID = response.body().getGuest_session_id();
//                    Log.d("test guest id 1: ", "" + GUEST_ID);
//                    SharedPreferences.Editor editor = SHAREED_PREFERENCES.edit();
//                    editor.putString(GUEST_ID_PREFERENCE, GUEST_ID);
//                    editor.commit();
//                }
//
//                @Override
//                public void onFailure(Call<GuestObject> call, Throwable t) {
//
//                }
//            });
//        }
//        if (GUEST_ID != null) {
//            RateMoviesService rateMoviesService = RetrofitFactory.getInstance().createService(RateMoviesService.class);
//            rateMoviesService.rateMovie(75656, new RateMovieRequest((float) 8.5), API_KEY, GUEST_ID).enqueue(new Callback<RateMoviesResponse>() {
//                @Override
//                public void onResponse(Call<RateMoviesResponse> call, Response<RateMoviesResponse> response) {
//                    Log.d("test rate:", response.body().getStatus_message());
//                }
//
//                @Override
//                public void onFailure(Call<RateMoviesResponse> call, Throwable t) {
//
//                }
//            });
//        }

        GetNewService getNewService= RetrofitFactory.getInstance().createService(GetNewService.class);
        getNewService.getNews().enqueue(new Callback<MainNewsObject>() {
            @Override
            public void onResponse(Call<MainNewsObject> call, Response<MainNewsObject> response) {
                for(NewsModel newsModel: response.body().getArticles())
                {
                    Log.d("test news:",newsModel.toString());
                }
            }

            @Override
            public void onFailure(Call<MainNewsObject> call, Throwable t) {

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
        getMenuInflater().inflate(R.menu.search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), SearchResultsActivity.class)));
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
            ScreenManager.openFragment(getSupportFragmentManager(), new CelebFragment(), R.id.fl_container, true, false);
        } else if (id == R.id.nav_gallery) {
            ScreenManager.openFragment(getSupportFragmentManager(), new TVShowsFragment(), R.id.fl_container, true, false);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
