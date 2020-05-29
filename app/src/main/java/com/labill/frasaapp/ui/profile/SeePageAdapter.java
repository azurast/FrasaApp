package com.labill.frasaapp.ui.profile;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SeePageAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SeePageAdapter";
    private int numberOfTabs;
    String userId;

    public SeePageAdapter(@NonNull FragmentManager fm, int numberOfTabs, String userId) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        this.userId = userId;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "current user id :"+userId);
        Bundle bundle = new Bundle();
        bundle.putString("id", userId);
        Fragment fragment = null;
        switch(position){
            case 0:
                fragment = new SeeStoriesFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                fragment = new SeeFollowersFragment();
                fragment.setArguments(bundle);
                return fragment;
            case 2:
                fragment = new SeeFollowingFragment();
                fragment.setArguments(bundle);
                return fragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
