package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.hoang.mobies.activities.MainActivity;
import com.example.hoang.mobies.adapters.TVShowByCategoriesAdapter;
import com.example.hoang.mobies.adapters.TrendingPagerAdapter;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.TVModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_tv.GetPopularTvService;
import com.example.hoang.mobies.network.get_tv.GetTopRatedTVService;
import com.example.hoang.mobies.network.get_tv.GetTvAiringToday;
import com.example.hoang.mobies.network.get_tv.GetTvOnTheAir;
import com.example.hoang.mobies.network.get_tv.MainTvObject;
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
    @BindView(R.id.tv_top_rated)
    TextView tvTopRated;
    @BindView(R.id.tv_on_air)
    TextView tvOnAir;
    @BindView(R.id.tv_airing_today)
    TextView tvAiringToday;
    @BindView(R.id.tv_no_connection)
    TextView tvNoConnection;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    private List<TVModel> tvShowTopRateList;
    private List<TVModel> tvShowTrendingList;
    private List<TVModel> tvShowOnAirList;
    private List<TVModel> tvShowAiringTodayList;
    private TVShowByCategoriesAdapter topRatedAdapter;
    private TVShowByCategoriesAdapter onAirAdapter;
    private TVShowByCategoriesAdapter airingTodayAdapter;
    private TrendingPagerAdapter trendingPagerAdapter;

    private int failConnection;


    public TVShowsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tvshows, container, false);
        failConnection = 0;
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

        SnapHelper snapHelper1 = new GravitySnapHelper(Gravity.START);
        SnapHelper snapHelper2 = new GravitySnapHelper(Gravity.START);
        SnapHelper snapHelper3 = new GravitySnapHelper(Gravity.START);

        snapHelper1.attachToRecyclerView(rvTopRated);
        snapHelper2.attachToRecyclerView(rvOnAir);
        snapHelper3.attachToRecyclerView(rvAiringToday);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.tv_shows);
        MainActivity.navigationView.setCheckedItem(R.id.nav_tvshow);

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
                tvOnAir.setVisibility(View.VISIBLE);
                if (response.body() != null) {
                    if (response.body().getResults() != null) {
                        MainTvObject mainTvObject = response.body();
                        for (TVModel tv_model : mainTvObject.getResults()) {
                            tvShowOnAirList.add(tv_model);
                        }
                        onAirAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                failConnection++;
                if (failConnection == 4) {
                    pbLoading.setVisibility(View.GONE);
                    tvNoConnection.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void loadAiringToday() {
        GetTvAiringToday getTvAiringToday = RetrofitFactory.getInstance().createService(GetTvAiringToday.class);
        getTvAiringToday.getTvAiringToday(API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                tvAiringToday.setVisibility(View.VISIBLE);
                if (response.body() != null) {
                    if (response.body().getResults() != null) {
                        MainTvObject mainTvObject = response.body();
                        for (TVModel tv_model : mainTvObject.getResults()) {
                            tvShowAiringTodayList.add(tv_model);
                        }
                        airingTodayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                failConnection++;

            }
        });
    }

    private void loadPopularTv() {
        GetPopularTvService getPopularTvService = RetrofitFactory.getInstance().createService(GetPopularTvService.class);
        getPopularTvService.getPopularTV(API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                pbLoading.setVisibility(View.GONE);
                if (response.body() != null) {
                    if (response.body().getResults() != null) {
                        MainTvObject mainObject = response.body();
                        for (TVModel tv_model : mainObject.getResults()) {
                            tvShowTrendingList.add(tv_model);
                        }
                        trendingPagerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                failConnection++;

            }
        });
    }

    private void loadTvTopRated() {
        GetTopRatedTVService getTopRatedTVService = RetrofitFactory.getInstance().createService(GetTopRatedTVService.class);
        getTopRatedTVService.getTopRatedTv(API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                tvTopRated.setVisibility(View.VISIBLE);
                if (response.body() != null) {
                    if (response.body().getResults() != null) {
                        MainTvObject mainObject = response.body();
                        for (TVModel tv_model : mainObject.getResults()) {
                            tvShowTopRateList.add(tv_model);
                        }
                        topRatedAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                failConnection++;

            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof TVModel) {
            TVModel tvModel = (TVModel) v.getTag();
            TVShowDetailFragment tvShowDetailFragment = new TVShowDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("TVDetail", tvModel);
            tvShowDetailFragment.setArguments(bundle);
            ScreenManager.openFragment(getFragmentManager(), tvShowDetailFragment, R.id.drawer_layout, true, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DrawerLayout) getActivity().findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((DrawerLayout) getActivity().findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}
