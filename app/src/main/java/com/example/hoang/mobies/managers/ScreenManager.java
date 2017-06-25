package com.example.hoang.mobies.managers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

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

        transaction.add(layoutID, fragment);
        transaction.commit();
    }

    public static void backFragment(FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
    }
}
