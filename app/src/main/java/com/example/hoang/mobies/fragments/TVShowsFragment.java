package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.example.hoang.mobies.network.get_movies.MainObject;
import com.example.hoang.mobies.network.get_tv.GetPopularTvService;
import com.example.hoang.mobies.network.get_tv.GetRandomTvService;
import com.example.hoang.mobies.network.get_tv.GetTopRatedTVService;
import com.example.hoang.mobies.network.get_tv.GetTvAiringToday;
import com.example.hoang.mobies.network.get_tv.GetTvOnTheAir;
import com.example.hoang.mobies.network.get_tv.MainTvObject;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;
import static com.example.hoang.mobies.network.RetrofitFactory.DEFAULT_PAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.LANGUAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.REGION;

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
    @BindView(R.id.tv_random)
    TextView tvRandom;
    @BindView(R.id.rv_random)
    RecyclerView rvRandom;
    @BindView(R.id.iv_randomize)
    ImageView ivRandomize;

    private List<TVModel> tvShowTopRateList;
    private List<TVModel> tvShowRandomList = new ArrayList<>();
    private List<TVModel> tvShowTrendingList;
    private List<TVModel> tvShowOnAirList;
    private List<TVModel> tvShowAiringTodayList;
    private TVShowByCategoriesAdapter topRatedAdapter;
    private TVShowByCategoriesAdapter randomAdapter;
    private TVShowByCategoriesAdapter onAirAdapter;
    private TVShowByCategoriesAdapter airingTodayAdapter;
    private TrendingPagerAdapter trendingPagerAdapter;
    private Snackbar snackbar;

    private int failConnection;
    private Toast toast;


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
        randomAdapter = new TVShowByCategoriesAdapter(tvShowRandomList, getContext());

        rvAiringToday.setAdapter(airingTodayAdapter);
        rvOnAir.setAdapter(onAirAdapter);
        rvTopRated.setAdapter(topRatedAdapter);
        rvRandom.setAdapter(randomAdapter);
        vpTrending.setAdapter(trendingPagerAdapter);
        vpTrending.setOffscreenPageLimit(3);

        onAirAdapter.setOnItemClickListener(this);
        topRatedAdapter.setOnItemClickListener(this);
        randomAdapter.setOnItemClickListener(this);
        airingTodayAdapter.setOnItemClickListener(this);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rvTopRated.setLayoutManager(linearLayoutManager1);
        rvOnAir.setLayoutManager(linearLayoutManager2);
        rvAiringToday.setLayoutManager(linearLayoutManager3);
        rvRandom.setLayoutManager(linearLayoutManager4);

        SnapHelper snapHelper1 = new GravitySnapHelper(Gravity.START);
        SnapHelper snapHelper2 = new GravitySnapHelper(Gravity.START);
        SnapHelper snapHelper3 = new GravitySnapHelper(Gravity.START);
        SnapHelper snapHelper4 = new GravitySnapHelper(Gravity.START);

        snapHelper1.attachToRecyclerView(rvTopRated);
        snapHelper2.attachToRecyclerView(rvOnAir);
        snapHelper3.attachToRecyclerView(rvAiringToday);
        snapHelper4.attachToRecyclerView(rvRandom);

        ivRandomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRandom();
            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.tv_shows);
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
        loadRandom();
    }

    public void loadRandom() {
        tvShowRandomList.clear();
        Random random = new Random();
        GetRandomTvService getRandomService = RetrofitFactory.getInstance().createService(GetRandomTvService.class);
        getRandomService.getRandomMovies(API_KEY, LANGUAGE, DEFAULT_PAGE + random.nextInt(20), REGION, random.nextInt(11), true).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                if (tvRandom.getVisibility() == View.GONE)
                    tvRandom.setVisibility(View.VISIBLE);
                if (ivRandomize.getVisibility() == View.GONE)
                    ivRandomize.setVisibility(View.VISIBLE);
                MainTvObject mainObject = response.body();
                for (TVModel tvModel : mainObject.getResults()) {
                    tvShowRandomList.add(tvModel);
                }
                randomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                if (toast != null) toast.cancel();
                toast = Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT);
                toast.show();
                failConnection++;
                if (failConnection == 5) {
                    pbLoading.setVisibility(View.GONE);
                    tvNoConnection.setVisibility(View.VISIBLE);
                    if (snackbar != null) snackbar.dismiss();
                    FrameLayout flContainer = (FrameLayout) getActivity().findViewById(R.id.fl_container);
                    snackbar = Snackbar.make(flContainer, "No connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(TVShowsFragment.this).attach(TVShowsFragment.this).commit();
                        }
                    });
                    snackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorStatusBar));
                    snackbar.show();
                }
            }
        });

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
                if (toast != null) toast.cancel();
                toast = Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT);
                toast.show();
                failConnection++;
                if (snackbar != null) snackbar.dismiss();
                FrameLayout flContainer = (FrameLayout) getActivity().findViewById(R.id.fl_container);
                snackbar = Snackbar.make(flContainer, "No connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(TVShowsFragment.this).attach(TVShowsFragment.this).commit();
                    }
                });
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorStatusBar));
                snackbar.show();
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
                if (toast != null) toast.cancel();
                toast = Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT);
                toast.show();
                failConnection++;
                if (snackbar != null) snackbar.dismiss();
                FrameLayout flContainer = (FrameLayout) getActivity().findViewById(R.id.fl_container);
                snackbar = Snackbar.make(flContainer, "No connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(TVShowsFragment.this).attach(TVShowsFragment.this).commit();
                    }
                });
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorStatusBar));
                snackbar.show();
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
                if (toast != null) toast.cancel();
                toast = Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT);
                toast.show();
                failConnection++;
                if (snackbar != null) snackbar.dismiss();
                FrameLayout flContainer = (FrameLayout) getActivity().findViewById(R.id.fl_container);
                snackbar = Snackbar.make(flContainer, "No connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(TVShowsFragment.this).attach(TVShowsFragment.this).commit();
                    }
                });
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorStatusBar));
                snackbar.show();
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
                if (toast != null) toast.cancel();
                toast = Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT);
                toast.show();
                failConnection++;
                if (snackbar != null) snackbar.dismiss();
                FrameLayout flContainer = (FrameLayout) getActivity().findViewById(R.id.fl_container);
                snackbar = Snackbar.make(flContainer, "No connection", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(TVShowsFragment.this).attach(TVShowsFragment.this).commit();
                    }
                });
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorStatusBar));
                snackbar.show();
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

}
