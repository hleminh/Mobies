package com.example.hoang.mobies.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.hoang.mobies.fragments.MovieWatchListFragment;
import com.example.hoang.mobies.fragments.TVWatchListFragment;

/**
 * Created by dell on 7/2/2017.
 */

public class WatchListPagerAdapter extends FragmentStatePagerAdapter {
    private int nTabs;
    public WatchListPagerAdapter(FragmentManager fm, int nTabs) {
        super(fm);
        this.nTabs = nTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MovieWatchListFragment();
            default:
                return new TVWatchListFragment();
        }
    }

    @Override
    public int getCount() {
        return nTabs;
    }
}
