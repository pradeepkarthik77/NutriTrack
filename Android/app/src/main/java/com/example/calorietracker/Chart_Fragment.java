package com.example.calorietracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

public class Chart_Fragment extends Fragment {

    private Context context;
    private ActionBarDrawerToggle toggle;

    public Chart_Fragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment_layout, container, false);

        Toolbar toolbar = view.findViewById(R.id.chart_toolbar);


        //set of lines for enabling Menu
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true); //to allow for sidebar
        activity.setSupportActionBar(toolbar);
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        DrawerLayout drawerLayout = view.findViewById(R.id.chart_drawer);
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

        new Sidebar_Class().navigation_onclick(navigationView,context);


        Spinner spinner = toolbar.findViewById(R.id.chart_decide_spinner);
        List<String> options = Arrays.asList("Weekly Report","Monthly Report","Yearly Report");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.chart_simple_spinner, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        FrameLayout frameLayout = view.findViewById(R.id.chart_fragment_holder);

//        int heightPixels = toolbar.getHeight();

        toolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightPixels = toolbar.getHeight();

                DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

                float heightDp = heightPixels / displayMetrics.density;

//                Toast.makeText(context,heightDp+"",Toast.LENGTH_LONG).show();

                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.findViewById(R.id.chart_fragment_holder).getLayoutParams();

                layoutParams.setMargins(0,heightPixels,0,0);

                frameLayout.setLayoutParams(layoutParams);

                toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        AppCompatActivity appCompatActivity = (AppCompatActivity) context;

        appCompatActivity.getSupportFragmentManager().beginTransaction().add(R.id.chart_fragment_holder,new Chart_Week_Fragment(context)).commit();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case 0: appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.chart_fragment_holder,new Chart_Week_Fragment(context)).commit();break;
                    case 1: appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.chart_fragment_holder,new Chart_Month_Fragment(context)).commit();break;
                    case 2: appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.chart_fragment_holder,new Chart_Year_Fragment(context)).commit();break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

}