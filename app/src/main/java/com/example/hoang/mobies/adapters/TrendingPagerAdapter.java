package com.example.hoang.mobies.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hoang.mobies.fragments.TrendingFragment;
import com.example.hoang.mobies.models.MovieModel;

import java.util.List;

/**
 * Created by Hoang on 6/9/2017.
 */

public class TrendingPagerAdapter extends FragmentStatePagerAdapter {
    private List<MovieModel> movieModelList;

    public TrendingPagerAdapter(FragmentManager fm, List<MovieModel> movieModelList) {
        super(fm);
        this.movieModelList = movieModelList;
    }

    @Override
    public Fragment getItem(int position) {
        TrendingFragment trendingFragment = new TrendingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("TrendingModel", movieModelList.get(position));
        trendingFragment.setArguments(bundle);
        return trendingFragment;
    }

    @Override
    public int getCount() {
        return movieModelList.size();
    }
}
