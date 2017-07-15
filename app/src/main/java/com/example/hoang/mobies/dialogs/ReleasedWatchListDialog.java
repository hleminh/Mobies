package com.example.hoang.mobies.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.adapters.WatchListAdapter;
import com.example.hoang.mobies.fragments.MovieDetailFragment;
import com.example.hoang.mobies.fragments.TVShowDetailFragment;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.MultiSearchModel;
import com.example.hoang.mobies.models.TVModel;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hoang on 7/16/2017.
 */

public class ReleasedWatchListDialog extends Dialog implements View.OnClickListener {
    @BindView(R.id.btn_okay)
    Button btnOkay;
    @BindView(R.id.rv_coming_watch_list)
    RecyclerView rvComingWatchList;
    private WatchListAdapter watchListAdapter;
    private Context context;
    private List<MultiSearchModel> watchList;

    public ReleasedWatchListDialog(@NonNull Context context, List<MultiSearchModel> watchList) {
        super(context);
        this.context = context;
        this.watchList = watchList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_coming_watch_list);
        ButterKnife.bind(this);
        btnOkay.setOnClickListener(this);
        watchListAdapter = new WatchListAdapter(context, watchList);
        watchListAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiSearchModel multiSearchModel = (MultiSearchModel) v.getTag();
                if (multiSearchModel.getMedia_type().equals("movie")) {
                    MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                    Bundle bundle = new Bundle();
                    MovieModel movieModel = new MovieModel(multiSearchModel);
                    movieModel.setGenresString(multiSearchModel.getGenresString());
                    bundle.putSerializable("MovieDetail", movieModel);
                    bundle.putBoolean("FromSearch", false);
                    movieDetailFragment.setArguments(bundle);
                    ScreenManager.openFragment(((AppCompatActivity) context).getSupportFragmentManager(), movieDetailFragment, R.id.drawer_layout, true, false);
                    dismiss();
                } else if (multiSearchModel.getMedia_type().equals("tv")) {
                    TVShowDetailFragment tvShowDetailFragment = new TVShowDetailFragment();
                    Bundle bundle = new Bundle();
                    TVModel tvModel = new TVModel(multiSearchModel);
                    tvModel.setGenresString(multiSearchModel.getGenresString());
                    bundle.putSerializable("TVDetail", tvModel);
                    bundle.putBoolean("FromSearch", false);
                    tvShowDetailFragment.setArguments(bundle);
                    ScreenManager.openFragment(((AppCompatActivity) context).getSupportFragmentManager(), tvShowDetailFragment, R.id.drawer_layout, true, false);
                    dismiss();
                }
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rvComingWatchList.setLayoutManager(mLayoutManager);
        rvComingWatchList.setAdapter(watchListAdapter);
//        GravitySnapHelper snapHelper = new GravitySnapHelper(Gravity.TOP);
//        snapHelper.attachToRecyclerView(rvComingWatchList);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_okay) {
            dismiss();
        }
    }

}
