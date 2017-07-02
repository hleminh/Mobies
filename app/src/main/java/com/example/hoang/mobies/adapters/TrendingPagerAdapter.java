package com.example.hoang.mobies.adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hoang.mobies.fragments.TrendingFragment;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.TVModel;

import java.util.List;

/**
 * Created by Hoang on 6/9/2017.
 */

public class TrendingPagerAdapter extends FragmentStatePagerAdapter {
    private List<MovieModel> movieModelList;
    private List<TVModel> tv_modelList;

    public TrendingPagerAdapter(FragmentManager fm, @Nullable List<MovieModel> movieModelList, @Nullable List<TVModel> tv_modelList) {
        super(fm);
        this.movieModelList = movieModelList;
        this.tv_modelList = tv_modelList;
    }

    @Override
    public Fragment getItem(int position) {
        TrendingFragment trendingFragment = new TrendingFragment();
        Bundle bundle = new Bundle();
        if (movieModelList != null) {
            bundle.putSerializable("TrendingModel", movieModelList.get(position));
        }

        if (tv_modelList != null) {
            bundle.putSerializable("TrendingModel", tv_modelList.get(position));
        }
        trendingFragment.setArguments(bundle);
        return trendingFragment;
    }

    @Override
    public int getCount() {
        if (movieModelList != null)
            return movieModelList.size();
        if (tv_modelList != null)
            return tv_modelList.size();
        return 0;
    }
}
