package com.example.calorietracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

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

        SharedPreferences pref = activity.getSharedPreferences("Login",0);

        setHasOptionsMenu(true);
        activity.setSupportActionBar(toolbar);

        this.viewPager = view.findViewById(R.id.dashboard_viewpager);
        this.tabLayout = view.findViewById(R.id.dashboard_tabDots);
        this.fragmentPagerAdapter = new Dashboard_ViewPage_Adapter(getChildFragmentManager(),context);
        viewPager.setAdapter(this.fragmentPagerAdapter);
        this.tabLayout.setupWithViewPager(this.viewPager,true);

        NavigationView navigationView = view.findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        TextView user_sidebar_name = header.findViewById(R.id.user_name_sidebar);
        TextView user_sidebar_email = header.findViewById(R.id.user_email_sidebar);
        RoundedLetterView user_profile_icon = header.findViewById(R.id.sidebar_user_profile);

        String name = pref.getString("name","");
        String email = pref.getString("email","");
        String firstletter = name.trim();

        if(!firstletter.equals(""))
        {
            firstletter = firstletter.charAt(0)+"";
            firstletter = firstletter.toUpperCase();
        }

        user_profile_icon.setTitleText(firstletter);
        user_sidebar_name.setText(name);
        user_sidebar_email.setText(email);


//        ImageView sidebar_background = header.findViewById(R.id.sidebar_background);
//        sidebar_background.setColorFilter(Color.BLACK,PorterDuff.Mode.LIGHTEN);

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
