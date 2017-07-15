package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.adapters.WatchListAdapter;
import com.example.hoang.mobies.databases.RealmHandle;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.GenreIDs;
import com.example.hoang.mobies.models.MovieModel;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieWatchListFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.rv_watch_list)
    RecyclerView rvWatchList;
    private WatchListAdapter watchListAdapter;
    public MovieWatchListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_watch_list, container, false);
        ButterKnife.bind(this, view);
        setupUI();
        return view;
    }

    private void setupUI() {
        watchListAdapter = new WatchListAdapter(getContext(), RealmHandle.getInstance().getListMoviesWatchList(), null);
        watchListAdapter.setOnClickListener(this);
        rvWatchList.setAdapter(watchListAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvWatchList.setLayoutManager(manager);
//        GravitySnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
//        snapHelper.attachToRecyclerView(rvWatchList);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof MovieModel) {
            MovieModel movieModel = (MovieModel) v.getTag();
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("MovieDetail", movieModel);
//            movieDetailFragment.setArguments(bundle);
            EventBus.getDefault().postSticky(movieModel);
            ScreenManager.openFragment(getActivity().getSupportFragmentManager(), movieDetailFragment, R.id.drawer_layout, true, false);
        }
    }
}
