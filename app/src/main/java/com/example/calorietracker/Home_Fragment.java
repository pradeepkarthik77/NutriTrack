package com.example.calorietracker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

public class Home_Fragment extends Fragment {

    private Context context;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ActionBarDrawerToggle toggle;


    public Home_Fragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);

         Toolbar toolbar = view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        setHasOptionsMenu(true);
        activity.setSupportActionBar(toolbar);


        this.viewPager = view.findViewById(R.id.dashboard_viewpager);
        this.tabLayout = view.findViewById(R.id.dashboard_tabDots);
        this.fragmentPagerAdapter = new Dashboard_ViewPage_Adapter(getChildFragmentManager(),context);
        viewPager.setAdapter(this.fragmentPagerAdapter);
        this.tabLayout.setupWithViewPager(this.viewPager,true);


        DrawerLayout drawerLayout = view.findViewById(R.id.dashboard_drawer);

        this.toggle = new ActionBarDrawerToggle((Activity) context,drawerLayout,R.string.nav_open,R.string.nav_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
