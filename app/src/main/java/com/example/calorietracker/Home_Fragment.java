package com.example.calorietracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Home_Fragment extends Fragment
{

    private Context context;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Dashboard_ViewPage_Adapter fragmentPagerAdapter;
    private Calendar_ViewGroup_Adapter calendar_viewGroup_adapter;
    private ActionBarDrawerToggle toggle;
    private TextView calender_text;

    private View global_view;

    private String[] cardview_titles = new String[]{"BreakFast","Lunch","Dinner","Mid-Meals","Snacks","Juices"};
    private int[] cardview_images = new int[]{R.drawable.breakfast, R.drawable.lunch, R.drawable.dinner, R.drawable.other_items, R.drawable.snacks, R.drawable.juices};
    private int cardview_count = 6;

    private RecyclerView mainRecycler;
    private MainCardAdapter mainCardAdapter;
    private ImageButton calender_btn;

    private TextView current_water_log;
    private TextView todays_water_log;
    private ImageButton current_water_btn;
    private SeekBar water_bar;
    private SharedPreferences water_log_pref;
    private SharedPreferences.Editor water_edit;

    public Home_Fragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_layout, container, false);

        //Toolbar and its related values
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        Spannable wordtoSpan = new SpannableString("NutriTrack");
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_green)), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView toolbar_title = toolbar.findViewById(R.id.tool_title);
        toolbar_title.setText(wordtoSpan);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        SharedPreferences pref = context.getSharedPreferences("Login",0);
        SharedPreferences.Editor editor = pref.edit();

        setHasOptionsMenu(true); //to allow for sidebar
        activity.setSupportActionBar(toolbar);

        this.calender_text = view.findViewById(R.id.calender_text);
        this.global_view = view;

        SharedPreferences date_pref = context.getSharedPreferences("date",0);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String today_date = dateFormat.format(date);

        if(date_pref.getString("chosen_date","Today").equals(today_date))
        {
            this.calender_text.setText("Today");
        }
        else
        {
            this.calender_text.setText(date_pref.getString("chosen_date","Today"));
        }

        this.viewPager = view.findViewById(R.id.dashboard_viewpager);

        this.viewPager.setSaveEnabled(false); //to allow for fragmentadapters to be changed
        this.tabLayout = view.findViewById(R.id.dashboard_tabDots);
        this.fragmentPagerAdapter = new Dashboard_ViewPage_Adapter(getChildFragmentManager(),context);
        this.calendar_viewGroup_adapter = new Calendar_ViewGroup_Adapter(getChildFragmentManager(),context);

        viewPager.setAdapter(this.fragmentPagerAdapter);
        this.tabLayout.setupWithViewPager(this.viewPager,true);
        this.calender_btn = view.findViewById(R.id.homeactivity_calender);

        this.water_bar = view.findViewById(R.id.seek_bar);
        this.current_water_log = view.findViewById(R.id.current_water_log_text);
        this.todays_water_log = view.findViewById(R.id.todays_water_log);
        this.current_water_btn = view.findViewById(R.id.current_log_btn);


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

        FragmentManager fragmentManager = activity.getSupportFragmentManager();

        this.calender_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(viewPager.getAdapter() == fragmentPagerAdapter)
                {
                    viewPager.setAdapter(calendar_viewGroup_adapter);
                }
                else
                {
                    viewPager.setAdapter(fragmentPagerAdapter);
                }
            }
        });

        DrawerLayout drawerLayout = view.findViewById(R.id.dashboard_drawer);

        this.toggle = new ActionBarDrawerToggle((Activity) context,drawerLayout,R.string.nav_open,R.string.nav_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.mainRecycler = view.findViewById(R.id.main_recycler);
        this.mainCardAdapter = new MainCardAdapter(context,this.cardview_titles,this.cardview_count,this.cardview_images);
        this.mainRecycler.setAdapter(this.mainCardAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.context,2);
        this.mainRecycler.setLayoutManager(gridLayoutManager);

        this.mainRecycler.setNestedScrollingEnabled(false);

        current_water_log.setText("Current Log: 250ml");

        this.water_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                current_water_log.setText("Current Log: "+progress+"ml");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        TextView todays_log = view.findViewById(R.id.todays_water_log);

        this.water_log_pref = activity.getSharedPreferences("water_log",0);
        this.water_edit = water_log_pref.edit();

        String water_chosen_date = date_pref.getString("chosen_date",dateFormat.format(new Date()));

        int values = water_log_pref.getInt(water_chosen_date,0);

        todays_log.setText("Today's Log: "+values+"ml");


        ImageButton current_water_add = view.findViewById(R.id.current_log_btn);

        SeekBar seekBar = view.findViewById(R.id.seek_bar);

        current_water_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = seekBar.getProgress();

                String water_chosen_date = date_pref.getString("chosen_date",dateFormat.format(new Date()));

                int values = value+water_log_pref.getInt(water_chosen_date,0);

                water_edit.putInt(water_chosen_date,values);
                water_edit.commit();

                todays_log.setText("Today's Log: "+values+"ml");

                seekBar.setProgress(250);

            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void set_calender_text(String newString)
    {
        this.calender_text.setText(newString);

        TextView chosen_date_todays_log = this.global_view.findViewById(R.id.todays_water_log);

        int values = water_log_pref.getInt(newString,0);

        chosen_date_todays_log.setText("Today's Log: "+values+"ml");

    }

}
