package com.example.calorietracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        Spinner spinner = toolbar.findViewById(R.id.chart_decide_spinner);
        List<String> options = Arrays.asList("Weekly Report","Monthly Report","Yearly Report");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.chart_simple_spinner, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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