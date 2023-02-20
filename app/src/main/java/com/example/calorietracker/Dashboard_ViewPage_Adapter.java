package com.example.calorietracker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Dashboard_ViewPage_Adapter extends FragmentStatePagerAdapter {

    private Context context;

    public Dashboard_ViewPage_Adapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0: return new dashboard_chart_1_fragment(context);
            case 1: return new dashboard_chart_2_fragment(context);
            default: return new dashboard_chart_1_fragment(context);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}