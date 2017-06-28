package com.example.hoang.mobies.managers;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.hoang.mobies.fragments.CelebFragment;
import com.example.hoang.mobies.fragments.MoviesFragment;
import com.example.hoang.mobies.fragments.NewsFragment;
import com.example.hoang.mobies.fragments.TVShowsFragment;

/**
 * Created by tonto on 6/18/2017.
 */

public class ScreenManager {
    public static void openFragment(FragmentManager fragmentManager, Fragment fragment, int layoutID, boolean addToBackStack, boolean haveAnimation) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

//        if (haveAnimation) {
//            transaction.setCustomAnimations(R.anim.enter_from_bot, 0, 0, R.anim.exit_to_bot);
//        }

        if (fragment instanceof CelebFragment){
            transaction.replace(layoutID, fragment, "CelebFragment");
        }
        else if (fragment instanceof MoviesFragment){
            transaction.replace(layoutID, fragment, "MoviesFragment");
        }
        else if (fragment instanceof TVShowsFragment){
            transaction.replace(layoutID, fragment, "TVShowsFragment");
        } else if (fragment instanceof NewsFragment) {
            transaction.replace(layoutID, fragment, "NewsFragment");
        }
        else {
            transaction.replace(layoutID, fragment);
        }
        transaction.commit();
    }

    public static void backFragment(FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
    }
}
