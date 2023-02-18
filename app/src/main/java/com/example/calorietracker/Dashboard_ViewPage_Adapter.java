package com.example.calorietracker;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Dashboard_ViewPage_Adapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private Context context;

    public Dashboard_ViewPage_Adapter(FragmentManager fragmentManager,Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new dashboard_chart_1_fragment(context);
            case 1:
                return new dashboard_chart_1_fragment(context);
            default:
                return new dashboard_chart_1_fragment(context);
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}