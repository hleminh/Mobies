package com.example.hoang.mobies.fragments;


import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.adapters.PopularCelebAdapter;
import com.example.hoang.mobies.models.PeopleModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_people.GetPopularPeopleService;
import com.example.hoang.mobies.network.get_people.MainPeopleObject;

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
public class CelebFragment extends Fragment {
    @BindView(R.id.rv_popular_celeb_content)
    RecyclerView rvPopularCeleb;
    private List<PeopleModel> popularList;
    private PopularCelebAdapter popularCelebAdapter;

    public CelebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_celeb, container, false);
        loadData();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        popularCelebAdapter = new PopularCelebAdapter(getContext(), popularList);
        rvPopularCeleb.setAdapter(popularCelebAdapter);
        rvPopularCeleb.setPadding(70,0,0,0);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        rvPopularCeleb.setLayoutManager(manager);

    }

    private void loadData() {
        popularList = new ArrayList<>();

        GetPopularPeopleService getPopularPeopleService = RetrofitFactory.createService(GetPopularPeopleService.class);
        getPopularPeopleService.getPopularPeople(API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainPeopleObject>() {
            @Override
            public void onResponse(Call<MainPeopleObject> call, Response<MainPeopleObject> response) {
                for (PeopleModel peopleModel : response.body().getResults()) {
                    popularList.add(peopleModel);
                }
                popularCelebAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainPeopleObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
