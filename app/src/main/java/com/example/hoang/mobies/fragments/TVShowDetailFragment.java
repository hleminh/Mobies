package com.example.hoang.mobies.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.Utils.Utils;
import com.example.hoang.mobies.adapters.CastsAdapter;
import com.example.hoang.mobies.adapters.TVShowByCategoriesAdapter;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.CastModel;
import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.models.TV_Model;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_cast.GetCastOfAMovieService;
import com.example.hoang.mobies.network.get_cast.MainCastObject;
import com.example.hoang.mobies.network.get_tv.GetRecommendTvService;
import com.example.hoang.mobies.network.get_tv.MainTvObject;
import com.squareup.picasso.Picasso;

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
public class TVShowDetailFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.iv_poster_tvshow_detail)
    ImageView ivPoster;
    @BindView(R.id.tv_tvshow_name_tvshow_detail)
    TextView tvTvShowName;
    @BindView(R.id.tv_tvshow_release_date_tvshow_detail)
    TextView tvTvShowReleaseDate;
    @BindView(R.id.rb_tvshow_detail)
    RatingBar rbTvShowDetail;
    @BindView(R.id.tv_rating_detail)
    TextView tvRatingDetail;
    @BindView(R.id.iv_back_drop_tvshow_detail)
    ImageView ivBackDrop;
    @BindView(R.id.tv_genre)
    TextView tvGenre;
    @BindView(R.id.rv_casts)
    RecyclerView rvCasts;
    @BindView(R.id.tv_plot)
    TextView tvPlot;
    @BindView(R.id.rv_recommended)
    RecyclerView rvRecommended;
    @BindView(R.id.toolbar)
    Toolbar tbDetail;
    private TV_Model tvModel;
    private List<CastModel> castModelList;
    private List<TV_Model> tv_modelList;
    private TVShowByCategoriesAdapter tvShowByCategoriesAdapter;
    private CastsAdapter castsAdapter;

    public TVShowDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tvshow_detail, container, false);
        tvModel = (TV_Model) getArguments().getSerializable("TVDetail");
        loadData();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + tvModel.getPoster_path()).into(ivPoster);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + tvModel.getBackdrop_path()).into(ivBackDrop);
        tvTvShowName.setText(tvModel.getName());
        tvRatingDetail.setText(String.format("%,d",tvModel.getVote_count()) + " Ratings");
        rbTvShowDetail.setRating(tvModel.getVote_average() / 2);
        tvTvShowReleaseDate.setText(tvModel.getFirst_air_date());
        tvPlot.setText(tvModel.getOverview());

        String genres = "";
        for (int i = 0; i < tvModel.getGenre_ids().size(); i++) {
            for (GenresModel genreModel : Utils.genresModelList) {
                if (genreModel.getId() == tvModel.getGenre_ids().get(i).intValue()) {
                    if (i == tvModel.getGenre_ids().size() - 1) {
                        genres += genreModel.getName();
                    } else genres += genreModel.getName() + ", ";
                }
            }
        }
        tvGenre.setText(genres);

        castsAdapter = new CastsAdapter(castModelList, getContext());
        rvCasts.setAdapter(castsAdapter);

        tvShowByCategoriesAdapter = new TVShowByCategoriesAdapter(tv_modelList, getContext());
        tvShowByCategoriesAdapter.setOnItemClickListener(this);
        rvRecommended.setAdapter(tvShowByCategoriesAdapter);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvRecommended.setLayoutManager(linearLayoutManager1);
        rvCasts.setLayoutManager(linearLayoutManager2);

        tbDetail.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        tbDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getActivity().getSupportFragmentManager());
            }
        });

    }

    private void loadData() {
        castModelList = new ArrayList<>();
        tv_modelList = new ArrayList<>();
        loadCasts();
        loadRecommended();
    }

    private void loadRecommended() {
        GetRecommendTvService getRecommendTvService = RetrofitFactory.getInstance().createService(GetRecommendTvService.class);
        getRecommendTvService.getRecommendTv(tvModel.getId(), API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                MainTvObject mainTvObject = response.body();
                for (TV_Model tv_model : mainTvObject.getResults()) {
                    tv_modelList.add(tv_model);
                }
                tvShowByCategoriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadCasts() {
        GetCastOfAMovieService getCastOfAMovieService = RetrofitFactory.getInstance().createService(GetCastOfAMovieService.class);
        getCastOfAMovieService.getCastOfAMovie(tvModel.getId(), API_KEY).enqueue(new Callback<MainCastObject>() {
            @Override
            public void onResponse(Call<MainCastObject> call, Response<MainCastObject> response) {
                MainCastObject mainCastObject = response.body();
                List<CastModel> castModels = mainCastObject.getCast();
                for (CastModel castModel : castModels) {
                    if (castModelList.size() < 5)
                        castModelList.add(castModel);
                }
                castsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainCastObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getActivity().getWindow();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof TV_Model) {
            TV_Model tvModel = (TV_Model) v.getTag();
            TVShowDetailFragment tvShowDetailFragment = new TVShowDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("TVDetail", tvModel);
            tvShowDetailFragment.setArguments(bundle);
            ScreenManager.openFragment(getFragmentManager(), tvShowDetailFragment, R.id.fl_container, true, false);
        }
    }
}
