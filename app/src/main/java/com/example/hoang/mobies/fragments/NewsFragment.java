package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.activities.MainActivity;
import com.example.hoang.mobies.adapters.NewsAdapter;
import com.example.hoang.mobies.models.NewsModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_news.GetNewService;
import com.example.hoang.mobies.network.get_news.MainNewsObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    @BindView(R.id.rv_news)
    RecyclerView rvNews;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_no_connection)
    TextView tvNoConnection;
    private List<NewsModel> newsModelList;
    private NewsAdapter newsAdapter;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        //TODO: Replace news detail fragment's layout in R.id.drawer_layout -> News detail fragment has its own Toolbar.
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.news);
        MainActivity.navigationView.setCheckedItem(R.id.nav_new);
        loadData();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        newsAdapter = new NewsAdapter(newsModelList, getContext(), getFragmentManager());
        rvNews.setAdapter(newsAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvNews.setLayoutManager(manager);
    }

    private void loadData() {
        newsModelList = new ArrayList<>();
        GetNewService getNewService = RetrofitFactory.getInstance().createService(GetNewService.class);
        getNewService.getNews().enqueue(new Callback<MainNewsObject>() {
            @Override
            public void onResponse(Call<MainNewsObject> call, Response<MainNewsObject> response) {
                for (NewsModel newsModel : response.body().getArticles()) {
                    newsModelList.add(newsModel);
                }
                newsAdapter.notifyDataSetChanged();
                pbLoading.setVisibility(View.GONE);
                rvNews.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MainNewsObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                pbLoading.setVisibility(View.GONE);
                tvNoConnection.setVisibility(View.VISIBLE);
            }
        });
    }

}