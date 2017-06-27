package com.example.hoang.mobies.fragments;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        //TODO: Replace news detail fragment's layout in R.id.drawer_layout -> News detail fragment has its own Toolbar.
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.news);
        MainActivity.navigationView.setCheckedItem(R.id.nav_new);
        return view;
    }

}
