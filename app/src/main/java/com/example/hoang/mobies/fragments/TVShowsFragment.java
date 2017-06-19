package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.adapters.TVShowByCategoriesAdapter;
import com.example.hoang.mobies.adapters.TrendingPagerAdapter;
import com.example.hoang.mobies.models.TV_Model;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_tv.GetPopularTvService;
import com.example.hoang.mobies.network.get_tv.GetTopRatedTVService;
import com.example.hoang.mobies.network.get_tv.GetTvAiringToday;
import com.example.hoang.mobies.network.get_tv.GetTvOnTheAir;
import com.example.hoang.mobies.network.get_tv.MainTvObject;

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
public class TVShowsFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.vp_trending)
    ViewPager vpTrending;
    @BindView(R.id.rv_on_air)
    RecyclerView rvOnAir;
    @BindView(R.id.rv_airing_today)
    RecyclerView rvAiringToday;
    @BindView(R.id.rv_top_rated)
    RecyclerView rvTopRated;
    private List<TV_Model> tvShowTopRateList;
    private List<TV_Model> tvShowTrendingList;
    private List<TV_Model> tvShowOnAirList;
    private List<TV_Model> tvShowAiringTodayList;
    private TVShowByCategoriesAdapter topRatedAdapter;
    private TVShowByCategoriesAdapter onAirAdapter;
    private TVShowByCategoriesAdapter airingTodayAdapter;
    private TrendingPagerAdapter trendingPagerAdapter;

    public TVShowsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tvshows, container, false);
        loadData();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);


        trendingPagerAdapter = new TrendingPagerAdapter(getChildFragmentManager(), null, tvShowTrendingList);
        airingTodayAdapter = new TVShowByCategoriesAdapter(tvShowAiringTodayList, getContext());
        onAirAdapter = new TVShowByCategoriesAdapter(tvShowOnAirList, getContext());
        topRatedAdapter = new TVShowByCategoriesAdapter(tvShowTopRateList, getContext());

        rvAiringToday.setAdapter(airingTodayAdapter);
        rvOnAir.setAdapter(onAirAdapter);
        rvTopRated.setAdapter(topRatedAdapter);
        vpTrending.setAdapter(trendingPagerAdapter);
        vpTrending.setOffscreenPageLimit(3);

        onAirAdapter.setOnItemClickListener(this);
        topRatedAdapter.setOnItemClickListener(this);
        airingTodayAdapter.setOnItemClickListener(this);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvTopRated.setLayoutManager(linearLayoutManager1);
        rvOnAir.setLayoutManager(linearLayoutManager2);
        rvAiringToday.setLayoutManager(linearLayoutManager3);
    }

    private void loadData() {
        tvShowTopRateList = new ArrayList<>();
        tvShowTrendingList = new ArrayList<>();
        tvShowAiringTodayList = new ArrayList<>();
        tvShowOnAirList = new ArrayList<>();

        loadPopularTv();
        loadTvTopRated();
        loadAiringToday();
        loadTVOnAir();
    }

    private void loadTVOnAir() {
        GetTvOnTheAir getTvOnTheAir = RetrofitFactory.getInstance().createService(GetTvOnTheAir.class);
        getTvOnTheAir.getTvOnTheAir(API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                MainTvObject mainTvObject = response.body();
                for (TV_Model tv_model : mainTvObject.getResults()) {
                    tvShowOnAirList.add(tv_model);
                }
                onAirAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAiringToday() {
        GetTvAiringToday getTvAiringToday = RetrofitFactory.getInstance().createService(GetTvAiringToday.class);
        getTvAiringToday.getTvAiringToday(API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                MainTvObject mainTvObject = response.body();
                for (TV_Model tv_model : mainTvObject.getResults()) {
                    tvShowAiringTodayList.add(tv_model);
                }
                airingTodayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPopularTv() {
        GetPopularTvService getPopularTvService = RetrofitFactory.getInstance().createService(GetPopularTvService.class);
        getPopularTvService.getPopularTV(API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                MainTvObject mainObject = response.body();
                for (TV_Model tv_model : mainObject.getResults()) {
                    tvShowTrendingList.add(tv_model);
                }
                trendingPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadTvTopRated() {
        GetTopRatedTVService getTopRatedTVService = RetrofitFactory.getInstance().createService(GetTopRatedTVService.class);
        getTopRatedTVService.getTopRatedTv(API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                MainTvObject mainObject = response.body();
                for (TV_Model tv_model : mainObject.getResults()) {
                    tvShowTopRateList.add(tv_model);
                }
                topRatedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View v) {

    }
}
