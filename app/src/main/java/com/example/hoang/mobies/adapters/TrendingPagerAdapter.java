package com.example.hoang.mobies.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.hoang.mobies.fragments.TrendingFragment;
import com.example.hoang.mobies.models.TrendingModel;

import java.util.List;

/**
 * Created by Hoang on 6/9/2017.
 */

public class TrendingPagerAdapter extends FragmentStatePagerAdapter {
    private List<TrendingModel> trendingModelList;

    public TrendingPagerAdapter(FragmentManager fm, List<TrendingModel> trendingModelList) {
        super(fm);
        this.trendingModelList = trendingModelList;
    }

    @Override
    public Fragment getItem(int position) {
        TrendingFragment trendingFragment = new TrendingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MovieModel", trendingModelList.get(position));
        trendingFragment.setArguments(bundle);
        return trendingFragment;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
