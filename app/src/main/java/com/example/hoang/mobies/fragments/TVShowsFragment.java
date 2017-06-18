package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.models.TV_Model;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_tv.GetPopularTvService;
import com.example.hoang.mobies.network.get_tv.GetTopRatedTVService;
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
public class TVShowsFragment extends Fragment {
    @BindView(R.id.vp_trending)
    ViewPager vpTrending;
    @BindView(R.id.tl_category)
    TabLayout tlCategory;
    @BindView(R.id.rv_top_rated)
    RecyclerView rvTopRated;
    private List<TV_Model> tvShowTopRateList;
    private List<TV_Model> tvShowTrendingList;

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

    }

    private void loadData() {
        tvShowTopRateList = new ArrayList<>();
        tvShowTrendingList = new ArrayList<>();

        loadPopularTv();
        loadTvTopRated();
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
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
