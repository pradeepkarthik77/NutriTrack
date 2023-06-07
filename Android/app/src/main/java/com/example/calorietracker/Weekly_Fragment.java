package com.example.calorietracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Weekly_Fragment extends Fragment {

    private Context context;
    private String chosen_date = "";
    private SharedPreferences date_pref;
    private SharedPreferences.Editor date_edit;
    private ActionBarDrawerToggle toggle;
    private RecyclerView weekRecycler;
    private WeekRecyclerAdapter weekRecyclerAdapter;

    public Weekly_Fragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weekly_fragment_layout, container, false);

        this.date_pref = context.getSharedPreferences("date",0);
        this.date_edit = this.date_pref.edit();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        this.chosen_date = this.date_pref.getString("chosen_date",dateFormat.format(new Date()));

        Toolbar toolbar = view.findViewById(R.id.week_toolbar);
        TextView title_text = toolbar.findViewById(R.id.week_tool_title);
        Spannable wordtoSpan = new SpannableString("WeekLog");
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_green)), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title_text.setText(wordtoSpan);


        //set of lines for enabling Menu
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true); //to allow for sidebar
        activity.setSupportActionBar(toolbar);
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        DrawerLayout drawerLayout = view.findViewById(R.id.week_dashboard_drawer);
        this.toggle = new ActionBarDrawerToggle((Activity) context,drawerLayout,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //end of those set of line for menu

        TextView user_sidebar_name = header.findViewById(R.id.user_name_sidebar);
        TextView user_sidebar_email = header.findViewById(R.id.user_email_sidebar);
        RoundedLetterView user_profile_icon = header.findViewById(R.id.sidebar_user_profile);

        SharedPreferences pref = context.getSharedPreferences("Login",0);

        //set of instructions to set name,email in sidebar
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
        //end of sidebar

        this.weekRecycler = (RecyclerView) view.findViewById(R.id.weektable_recycler);

        this.chosen_date = date_pref.getString("chosen_date",dateFormat.format(new Date()));



        ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result)
                    {
                        if(result.getResultCode() == 0)
                        {
                            //result.getData().getStringExtra("recycler_id")
                            int id = 0;
                            id = Integer.valueOf(result.getData().getStringExtra("recycler_id"));
//                            weekRecycler.getAdapter().notifyDataSetChanged();
                            weekRecycler.getAdapter().notifyItemChanged(id);
                        }
                    }
                });

        Date today_date;

        try {
            today_date = dateFormat.parse(this.chosen_date);
        } catch (ParseException e) {
            today_date = new Date();
        }

//        Toast.makeText(context,dateFormat.format(today_date),Toast.LENGTH_LONG).show();

        this.weekRecyclerAdapter = new WeekRecyclerAdapter(context,today_date,activityResultLaunch);

        this.weekRecycler.setAdapter(this.weekRecyclerAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.context,2);
        this.weekRecycler.setLayoutManager(gridLayoutManager);

        new Sidebar_Class().navigation_onclick(navigationView,context);

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