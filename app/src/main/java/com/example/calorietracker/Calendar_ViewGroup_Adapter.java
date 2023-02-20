package com.example.calorietracker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Calendar_ViewGroup_Adapter extends FragmentStatePagerAdapter {

    private Context context;

    public Calendar_ViewGroup_Adapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return new Calendar_fragment(context);
    }

    @Override
    public int getCount() {
        return 1;
    }
}