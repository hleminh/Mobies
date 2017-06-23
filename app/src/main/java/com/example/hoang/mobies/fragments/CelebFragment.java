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
import com.example.hoang.mobies.managers.ScreenManager;
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
public class CelebFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.rv_popular_celeb_content)
    RecyclerView rvPopularCeleb;
    private List<PeopleModel> popularList;
    private PopularCelebAdapter popularCelebAdapter;
    private boolean loading = true;
    private int pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private int loadTimes = 0;
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
        popularCelebAdapter.setOnItemClickListener(this);
        rvPopularCeleb.setAdapter(popularCelebAdapter);
        final GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        rvPopularCeleb.setLayoutManager(manager);

        rvPopularCeleb.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisiblesItems = manager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;
                            loadTimes ++;
                            loadPopularPeople();
                        }
                    }
                }
            }
        });
    }

    private void loadData() {
        popularList = new ArrayList<>();
        loadPopularPeople();

    }

    private void loadPopularPeople() {
        GetPopularPeopleService getPopularPeopleService = RetrofitFactory.createService(GetPopularPeopleService.class);
        getPopularPeopleService.getPopularPeople(API_KEY, LANGUAGE, DEFAULT_PAGE + loadTimes).enqueue(new Callback<MainPeopleObject>() {
            @Override
            public void onResponse(Call<MainPeopleObject> call, Response<MainPeopleObject> response) {
                for (PeopleModel peopleModel : response.body().getResults()) {

                    popularList.add(peopleModel);
                }
                loading = true;
                popularCelebAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MainPeopleObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof PeopleModel) {
            PeopleModel peopleModel = (PeopleModel) v.getTag();
            CelebDetailFragment celebDetailFragment = new CelebDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("CelebDetail", peopleModel);
            celebDetailFragment.setArguments(bundle);
            ScreenManager.openFragment(getFragmentManager(), celebDetailFragment, R.id.fl_container, true, false);
        }
    }
}
