package com.example.hoang.mobies.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.adapters.CastsAdapter;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.CastModel;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.TVModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_cast.GetCastOfAMovieService;
import com.example.hoang.mobies.network.get_cast.GetCastTvService;
import com.example.hoang.mobies.network.get_cast.MainCastObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullCastFragment extends Fragment {
    private MovieModel movieModel;
    private TVModel tvModel;
    private List<CastModel> castModelList;
    private CastsAdapter castsAdapter;

    @BindView(R.id.rv_full_cast)
    RecyclerView rvFullCast;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_no_connection)
    TextView tvNoConnection;

    public FullCastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_cast, container, false);
        ButterKnife.bind(this, view);
        movieModel = (MovieModel) getArguments().getSerializable("MovieDetail");
        tvModel = (TVModel) getArguments().getSerializable("TVDetail");
        loadData();
        setupUI();
        return view;
    }

    private void loadData() {
        castModelList = new ArrayList<>();
        loadCasts();
    }

    private void setupUI() {
        castsAdapter = new CastsAdapter(castModelList, getContext());
        rvFullCast.setAdapter(castsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvFullCast.setLayoutManager(linearLayoutManager);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getFragmentManager());
            }
        });
        toolbar.setTitle("Full cast");
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private void loadCasts() {
        if (movieModel != null) {
            GetCastOfAMovieService getCastOfAMovieService = RetrofitFactory.getInstance().createService(GetCastOfAMovieService.class);
            getCastOfAMovieService.getCastOfAMovie(movieModel.getId(), API_KEY).enqueue(new Callback<MainCastObject>() {
                @Override
                public void onResponse(Call<MainCastObject> call, Response<MainCastObject> response) {
                    MainCastObject mainCastObject = response.body();
                    List<CastModel> castModels = mainCastObject.getCast();
                    for (CastModel castModel : castModels)
                        castModelList.add(castModel);
                    castsAdapter.notifyDataSetChanged();
                    pbLoading.setVisibility(View.GONE);
                    rvFullCast.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<MainCastObject> call, Throwable t) {
                    Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                    pbLoading.setVisibility(View.GONE);
                    tvNoConnection.setVisibility(View.VISIBLE);
                }
            });
        }

        if (tvModel != null) {
            GetCastTvService getCastTvService = RetrofitFactory.getInstance().createService(GetCastTvService.class);
            getCastTvService.getCastOfAMovie(tvModel.getId(), API_KEY).enqueue(new Callback<MainCastObject>() {
                @Override
                public void onResponse(Call<MainCastObject> call, Response<MainCastObject> response) {
                    for (CastModel castModel : response.body().getCast())
                        castModelList.add(castModel);
                    castsAdapter.notifyDataSetChanged();
                    pbLoading.setVisibility(View.GONE);
                    rvFullCast.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<MainCastObject> call, Throwable t) {
                    Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                    pbLoading.setVisibility(View.GONE);
                    tvNoConnection.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
