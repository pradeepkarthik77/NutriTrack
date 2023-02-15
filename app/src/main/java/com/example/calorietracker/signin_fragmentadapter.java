package com.example.calorietracker;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class signin_fragmentadapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;
    private Context context;

    public signin_fragmentadapter(FragmentManager fragmentManager,Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new gender_age_fragment(context);
            case 1:
                return new weight_calorie_fragment(context);
            case 2:
                return new goal_calorie_signup_fragment(context);
            default:
                return new gender_age_fragment(context);
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}