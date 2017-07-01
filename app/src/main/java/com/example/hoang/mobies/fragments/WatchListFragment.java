package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.activities.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WatchListFragment extends Fragment {
    @BindView(R.id.tl_watch_list)
    TabLayout tlWatchList;


    public WatchListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watch_list, container, false);
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        tlWatchList.addTab(tlWatchList.newTab().setText(R.string.movies));
        tlWatchList.addTab(tlWatchList.newTab().setText(R.string.tv_shows));
        tlWatchList.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.watch_list);
        MainActivity.navigationView.setCheckedItem(R.id.nav_watch_list);
    }

}
