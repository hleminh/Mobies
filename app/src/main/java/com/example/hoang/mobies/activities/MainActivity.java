package com.example.hoang.mobies.activities;

import android.os.Build;
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
import android.view.Window;
import android.widget.FrameLayout;

import com.example.hoang.mobies.R;

import com.example.hoang.mobies.fragments.CelebFragment;
import com.example.hoang.mobies.fragments.MovieDetailFragment;
import com.example.hoang.mobies.fragments.TVShowsFragment;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.CastModel;
import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.PeopleModel;
import com.example.hoang.mobies.models.TV_Model;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_cast.GetCastOfAMovieService;
import com.example.hoang.mobies.network.get_cast.MainCastObject;
import com.example.hoang.mobies.network.get_genres.GetGenresService;
import com.example.hoang.mobies.network.get_genres.MainGenresObject;
import com.example.hoang.mobies.network.get_movies.GetComingSoonService;
import com.example.hoang.mobies.network.get_movies.GetInCinemasMoviesService;
import com.example.hoang.mobies.network.get_movies.GetMovieByGenresService;
import com.example.hoang.mobies.network.get_movies.GetRecommendMovieService;
import com.example.hoang.mobies.network.get_movies.GetTopRatedMoviesService;
import com.example.hoang.mobies.network.get_movies.GetTrendingMoviesService;
import com.example.hoang.mobies.network.get_movies.MainObject;

import com.example.hoang.mobies.fragments.MoviesFragment;
import com.example.hoang.mobies.network.get_people.GetPopularPeopleService;
import com.example.hoang.mobies.network.get_people.MainPeopleObject;
import com.example.hoang.mobies.network.get_tv.GetPopularTvService;
import com.example.hoang.mobies.network.get_tv.GetTopRatedTVService;
import com.example.hoang.mobies.network.get_tv.MainTvObject;


import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;
import static com.example.hoang.mobies.network.RetrofitFactory.DEFAULT_PAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.LANGUAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.REGION;
import static com.example.hoang.mobies.network.RetrofitFactory.retrofitFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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



        GetCastOfAMovieService getCastOfAMovieService= RetrofitFactory.getInstance().createService(GetCastOfAMovieService.class);
        getCastOfAMovieService.getCastOfAMovie(209112,API_KEY).enqueue(new Callback<MainCastObject>() {
            @Override
            public void onResponse(Call<MainCastObject> call, Response<MainCastObject> response) {
                MainCastObject mainCastObject= response.body();
                List<CastModel> castModels= mainCastObject.getCast();
                for(CastModel castModel: castModels)
                {
                    Log.d("test cast:", castModel.toString());
                }
            }

            @Override
            public void onFailure(Call<MainCastObject> call, Throwable t) {

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
