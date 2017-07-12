package com.example.hoang.mobies.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;


import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.example.hoang.mobies.R;

import com.example.hoang.mobies.Utils.Utils;
import com.example.hoang.mobies.adapters.WatchListAdapter;
import com.example.hoang.mobies.databases.RealmHandle;
import com.example.hoang.mobies.dialogs.NoConnectionDialog;
import com.example.hoang.mobies.fragments.CelebFragment;
import com.example.hoang.mobies.fragments.MovieDetailFragment;
import com.example.hoang.mobies.fragments.NewsDetailFragment;
import com.example.hoang.mobies.fragments.NewsFragment;
import com.example.hoang.mobies.fragments.SearchResultFragment;
import com.example.hoang.mobies.fragments.TVShowDetailFragment;
import com.example.hoang.mobies.fragments.TVShowsFragment;
import com.example.hoang.mobies.fragments.WatchListFragment;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.GenresModel;

import com.example.hoang.mobies.models.MovieModel;

import com.example.hoang.mobies.models.MultiSearchModel;
import com.example.hoang.mobies.models.TVModel;
import com.example.hoang.mobies.network.RetrofitFactory;


import com.example.hoang.mobies.fragments.MoviesFragment;
import com.example.hoang.mobies.network.get_movies.MainObject;

import com.example.hoang.mobies.network.get_tv.MainTvObject;
import com.example.hoang.mobies.network.guest_session.CreateGuestSessionService;
import com.example.hoang.mobies.network.guest_session.GuestObject;
import com.example.hoang.mobies.network.rate.GetRatedMoviesService;
import com.example.hoang.mobies.network.rate.GetRatedTVService;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;

import static com.example.hoang.mobies.network.RetrofitFactory.GUEST_ID;
import static com.example.hoang.mobies.network.RetrofitFactory.GUEST_ID_PREFERENCE;
import static com.example.hoang.mobies.network.RetrofitFactory.MyPREFERENCES;
import static com.example.hoang.mobies.network.RetrofitFactory.SHAREED_PREFERENCES;
import static com.example.hoang.mobies.network.RetrofitFactory.retrofitFactory;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Menu mMenu;
    private static List<GenresModel> genresModelList;
    public static List<MovieModel> RATED_MOVIE_LIST = new ArrayList<>();
    public static List<TVModel> RATED_TV_LIST = new ArrayList<>();
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    private Toast toast;
    private Snackbar snackbar;
    private NoConnectionDialog noConnectionDialog;
    private RecyclerView rvWatchList;
    private WatchListAdapter watchListAdapter;
    private List<MultiSearchModel> watchList;

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
        displayStartScreen();
        if (watchList == null)
            loadWatchListOnTime();

    }

    private void loadWatchListOnTime() {
        watchList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(calendar.getTime());
        for (MovieModel movieModel : RealmHandle.getInstance().getListMoviesWatchList()) {
            if (movieModel.isCheckLater()) {
                try {
                    Date parsed = df.parse(movieModel.getRelease_date());
                    Date now = calendar.getTime();
                    if (parsed.compareTo(now) <= 0) {
                        MultiSearchModel multiSearchModel = new MultiSearchModel(movieModel);
                        watchList.add(multiSearchModel);
                        RealmHandle.getInstance().setCheckLater(movieModel, false);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        }
        for (TVModel tvModel : RealmHandle.getInstance().getListTVWatchList()) {
            if (tvModel.isCheckLater()) {
                try {
                    Date parsed = df.parse(tvModel.getFirst_air_date());
                    Date now = calendar.getTime();
                    if (parsed.compareTo(now) <= 0) {
                        MultiSearchModel multiSearchModel = new MultiSearchModel(tvModel);
                        watchList.add(multiSearchModel);
                        RealmHandle.getInstance().setCheckLater(tvModel, false);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        if (watchList.size() > 0) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            Dialog dialog = new Dialog(MainActivity.this);
            View dialogView = layoutInflater.inflate(R.layout.dialog_coming_watch_list, null);
            dialog.setContentView(dialogView);
            rvWatchList = (RecyclerView) dialogView.findViewById(R.id.rv_coming_watch_list);
            dialog.setTitle("Released Today");

            watchListAdapter = new WatchListAdapter(this, watchList);
            watchListAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MultiSearchModel multiSearchModel = (MultiSearchModel) v.getTag();
                    if (multiSearchModel.getMedia_type().equals("movie")) {
                        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                        Bundle bundle = new Bundle();
                        MovieModel movieModel = new MovieModel(multiSearchModel);
                        movieModel.setGenresString(multiSearchModel.getGenresString());
                        bundle.putSerializable("MovieDetail", movieModel);
                        bundle.putBoolean("FromSearch", true);
                        movieDetailFragment.setArguments(bundle);
                        ScreenManager.openFragment(getSupportFragmentManager(), movieDetailFragment, R.id.drawer_layout, true, false);
                        dialog.cancel();
                    } else if (multiSearchModel.getMedia_type().equals("tv")) {
                        TVShowDetailFragment tvShowDetailFragment = new TVShowDetailFragment();
                        Bundle bundle = new Bundle();
                        TVModel tvModel = new TVModel(multiSearchModel);
                        tvModel.setGenresString(multiSearchModel.getGenresString());
                        bundle.putSerializable("TVDetail", tvModel);
                        bundle.putBoolean("FromSearch", true);
                        tvShowDetailFragment.setArguments(bundle);
                        ScreenManager.openFragment(getSupportFragmentManager(), tvShowDetailFragment, R.id.drawer_layout, true, false);
                        dialog.cancel();
                    }
                }
            });
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvWatchList.setLayoutManager(mLayoutManager);
            rvWatchList.setAdapter(watchListAdapter);

            dialog.show();
        }


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
                    if (toast != null) toast.cancel();
                    toast = Toast.makeText(MainActivity.this, "No connection!", Toast.LENGTH_SHORT);
                    toast.show();
                    if (noConnectionDialog != null) noConnectionDialog.dismiss();
                    noConnectionDialog = new NoConnectionDialog(MainActivity.this);
                    noConnectionDialog.show();
//                    if (snackbar != null) snackbar.dismiss();
//                    DrawerLayout flContainer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                    snackbar = Snackbar.make(flContainer, "No connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = getIntent();
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            finish();
//                            startActivity(intent);
//                        }
//                    });
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
        NewsDetailFragment newsDetailFragment = (NewsDetailFragment) getSupportFragmentManager().findFragmentByTag("NewsDetailFragment");
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            System.out.println("Close drawer");
            drawer.closeDrawer(GravityCompat.START);
        } else if (newsDetailFragment != null) {
            if (newsDetailFragment.getToolbar().getVisibility() == View.GONE) {
                System.out.println("Restore toolbar");
                newsDetailFragment.getToolbar().setVisibility(View.VISIBLE);
            } else super.onBackPressed();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            SearchResultFragment searchResultFragment = (SearchResultFragment) getSupportFragmentManager().findFragmentByTag("SearchResultFragment");
            if (searchResultFragment != null) {
                if (!searchResultFragment.isVisible()) {
                    System.out.println("Exit, not visible search");
                    finish();
                    System.exit(0);
                } else super.onBackPressed();
            } else {
                System.out.println("Exit, null search");
                finish();
                System.exit(0);
                super.onBackPressed();
            }
        } else super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.mMenu = menu;
//
//        MenuItem menuItem = menu.findItem(R.id.search);
//
////        SearchManager searchManager =
////                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
////        final SearchView searchView =
////                (SearchView) menu.findItem(R.id.search).getActionView();
////
//////        searchView.setSearchableInfo(
//////                searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), SearchResultsActivity.class)));
//////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//////            @Override
//////            public boolean onQueryTextSubmit(String query) {
//////                searchView.setQuery(query, false);
//////                searchView.clearFocus();
//////                LAST_QUERY = query;
//////                mMenu.findItem(R.id.search).collapseActionView();
//////                return false;
//////            }
//////
//////            @Override
//////            public boolean onQueryTextChange(String newText) {
//////                return false;
//////            }
//////        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
            startActivity(intent);
        }

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
//                else tvShowsFragment.loadRandom();
            }
            if (tvShowsFragment == null) {
                ScreenManager.openFragment(getSupportFragmentManager(), new TVShowsFragment(), R.id.fl_container, true, false);
            }
        } else if (id == R.id.nav_movie) {
            MoviesFragment moviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag("MoviesFragment");
            if (moviesFragment != null) {
                if (!moviesFragment.isVisible())
                    ScreenManager.openFragment(getSupportFragmentManager(), new MoviesFragment(), R.id.fl_container, true, false);
//                else moviesFragment.loadRandom();
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
        } else if (id == R.id.nav_watch_list) {
            WatchListFragment watchListFragment = (WatchListFragment) getSupportFragmentManager().findFragmentByTag("WatchListFragment");
            if (watchListFragment != null) {
                if (!watchListFragment.isVisible()) {
                    ScreenManager.openFragment(getSupportFragmentManager(), new WatchListFragment(), R.id.fl_container, true, false);
                }
            }
            if (watchListFragment == null) {
                ScreenManager.openFragment(getSupportFragmentManager(), new WatchListFragment(), R.id.fl_container, true, false);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadRatedList() {
        GetRatedTVService getRatedTVService = retrofitFactory.getInstance().createService(GetRatedTVService.class);
        getRatedTVService.getRatedTV(GUEST_ID, API_KEY).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                if (response.body() != null) {
                    if (response.body().getResults() != null)
                        for (TVModel tv_model : response.body().getResults()) {
                            RATED_TV_LIST.add(tv_model);

                        }
                }
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                if (toast != null) toast.cancel();
                toast = Toast.makeText(MainActivity.this, "No connection!", Toast.LENGTH_SHORT);
                toast.show();
                if (noConnectionDialog != null) noConnectionDialog.dismiss();
                noConnectionDialog = new NoConnectionDialog(MainActivity.this);
                noConnectionDialog.show();
//                if (snackbar != null) snackbar.dismiss();
//                DrawerLayout flContainer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                snackbar = Snackbar.make(flContainer, "No connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = getIntent();
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        finish();
//                        startActivity(intent);
//                    }
//                });
//                snackbar.show();
            }
        });

        GetRatedMoviesService getRatedMoviesService = retrofitFactory.getInstance().createService(GetRatedMoviesService.class);
        getRatedMoviesService.getRatedMovies(GUEST_ID, API_KEY).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                if (response.body() != null)
                    for (MovieModel movieModel : response.body().getResults()) {
                        RATED_MOVIE_LIST.add(movieModel);

                    }
                if (Utils.genresModelList == null)
                    displayStartScreen();
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                if (toast != null) toast.cancel();
                toast = Toast.makeText(MainActivity.this, "No connection!", Toast.LENGTH_SHORT);
                toast.show();
                if (noConnectionDialog != null) noConnectionDialog.dismiss();
                noConnectionDialog = new NoConnectionDialog(MainActivity.this);
                noConnectionDialog.show();
//                if (snackbar != null) snackbar.dismiss();
//                DrawerLayout flContainer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                snackbar = Snackbar.make(flContainer, "No connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = getIntent();
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        finish();
//                        startActivity(intent);
//                    }
//                });
//                snackbar.show();
            }
        });
    }
}
