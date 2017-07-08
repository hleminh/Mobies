package com.example.hoang.mobies.activities;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.fragments.SearchResultFragment;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.searchs.MyRxSearchView;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import org.reactivestreams.Subscriber;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SearchResultsActivity extends AppCompatActivity {
    SearchView viewSearch;
    MenuItem menuItem;
    public static String LAST_QUERY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("search activity");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.colorStatusBar));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        handleIntent(getIntent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        System.out.println("Search on create option");

        if (menuItem == null) {

            getMenuInflater().inflate(R.menu.search, menu);

            menuItem = menu.findItem(R.id.search);

            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            viewSearch = (SearchView) menu.findItem(R.id.search).getActionView();

            viewSearch.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(),
                    SearchResultsActivity.class)));

            RxSearchView.queryTextChanges(viewSearch)
                    .debounce(600, TimeUnit.MILLISECONDS)
                    .filter(item -> item.length() > 1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(query -> {
                        SearchResultFragment searchResultFragment = new SearchResultFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("SearchQuery", query.toString());
                        searchResultFragment.setArguments(bundle);
                        ScreenManager.openFragment(getSupportFragmentManager(), searchResultFragment, R.id.fl_container, false, false);
                    });

//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<Item>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(List<ClipData.Item> items) {
//                        // adapter.addItems(...)
//                    }
//                });

            MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    onBackPressed();
                    return false;
                }
            });
            menuItem.expandActionView();
        } else {
            getMenuInflater().inflate(R.menu.search, menu);

            menuItem = menu.findItem(R.id.search);

            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);

            viewSearch = (SearchView) menu.findItem(R.id.search).getActionView();

            viewSearch.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(),
                    SearchResultsActivity.class)));

            RxSearchView.queryTextChanges(viewSearch)
                    .debounce(600, TimeUnit.MILLISECONDS)
                    .filter(item -> item.length() > 1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(query -> {
                        SearchResultFragment searchResultFragment = new SearchResultFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("SearchQuery", query.toString());
                        searchResultFragment.setArguments(bundle);
                        ScreenManager.openFragment(getSupportFragmentManager(), searchResultFragment, R.id.fl_container, false, false);
                    });

            MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    onBackPressed();
                    return false;
                }
            });
        }
        return true;
    }

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.search:
////                System.out.println(LAST_QUERY);
////                viewSearch.setQuery(LAST_QUERY, false);
//                viewSearch.setQueryHint(LAST_QUERY);
//                viewSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        LAST_QUERY = query;
//                        menuItem.collapseActionView();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        return false;
//                    }
//                });
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SearchResultFragment searchResultFragment = new SearchResultFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("SearchQuery", query);
            searchResultFragment.setArguments(bundle);
            ScreenManager.openFragment(getSupportFragmentManager(), searchResultFragment, R.id.fl_container, false, false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DrawerLayout) findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DrawerLayout) findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

}
