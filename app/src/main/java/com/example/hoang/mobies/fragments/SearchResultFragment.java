package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.adapters.MultiSearchAdapter;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.MultiSearchModel;
import com.example.hoang.mobies.models.PeopleModel;
import com.example.hoang.mobies.models.TVModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_search.GetMultiSearchService;
import com.example.hoang.mobies.network.get_search.MainSearchModel;

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
public class SearchResultFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.rv_search_result)
    RecyclerView rvSearchResult;
    @BindView(R.id.pb_search)
    ProgressBar pbSearch;
    @BindView(R.id.tv_no_connection)
    TextView tvNoConnection;
    List<MultiSearchModel> resultList;
    String query;
    MultiSearchAdapter multiSearchAdapter;


    public SearchResultFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        query = (String) getArguments().getSerializable("SearchQuery");
        loadData();
        setUpUI(view);

        return view;
    }

    private void setUpUI(View view) {
        ButterKnife.bind(this, view);
        multiSearchAdapter = new MultiSearchAdapter(getContext(), resultList);
        multiSearchAdapter.setOnItemClickListener(this);
        rvSearchResult.setAdapter(multiSearchAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvSearchResult.getContext(),
//                manager.getOrientation());
//
//        rvSearchResult.addItemDecoration(dividerItemDecoration);
        rvSearchResult.setLayoutManager(manager);

    }


    private void loadData() {
        resultList = new ArrayList<>();
        GetMultiSearchService getMultiSearchService = RetrofitFactory.getInstance().createService(GetMultiSearchService.class);
        getMultiSearchService.getMultiSearch(query, API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainSearchModel>() {
            @Override
            public void onResponse(Call<MainSearchModel> call, Response<MainSearchModel> response) {
                MainSearchModel mainSearchModel = response.body();
                for (MultiSearchModel searchModel : mainSearchModel.getResults()) {
                    resultList.add(searchModel);
                }
                multiSearchAdapter.notifyDataSetChanged();
                pbSearch.setVisibility(View.GONE);
                rvSearchResult.setVisibility(View.VISIBLE);
                if (resultList.size() == 0) {
                    rvSearchResult.setVisibility(View.GONE);
                    tvNoConnection.setVisibility(View.VISIBLE);
                    tvNoConnection.setText("No result found");
                }
            }

            @Override
            public void onFailure(Call<MainSearchModel> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                pbSearch.setVisibility(View.GONE);
                tvNoConnection.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public void onClick(View v) {

        if (v.getTag() instanceof MultiSearchModel) {
            MultiSearchModel multiSearchModel = (MultiSearchModel) v.getTag();
            if (multiSearchModel.getMedia_type().equals("movie")) {
                MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("MovieDetail", new MovieModel(multiSearchModel));
                movieDetailFragment.setArguments(bundle);
                ScreenManager.openFragment(getFragmentManager(), movieDetailFragment, R.id.drawer_layout, true, false);
            }
            else if(multiSearchModel.getMedia_type().equals("tv"))
            {
                TVShowDetailFragment tvShowDetailFragment = new TVShowDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("TVDetail", new TVModel(multiSearchModel));
                tvShowDetailFragment.setArguments(bundle);
                ScreenManager.openFragment(getFragmentManager(), tvShowDetailFragment, R.id.drawer_layout, true, false);
            }
            else {
                CelebDetailFragment celebDetailFragment = new CelebDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("CelebDetail", new PeopleModel(multiSearchModel));
                celebDetailFragment.setArguments(bundle);
                ScreenManager.openFragment(getFragmentManager(), celebDetailFragment, R.id.drawer_layout, true, false);
            }

        }
    }
}

