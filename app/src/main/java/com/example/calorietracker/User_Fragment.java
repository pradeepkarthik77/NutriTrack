package com.example.calorietracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class User_Fragment extends Fragment {

    private Context context;
    private ActionBarDrawerToggle toggle;
    private RecyclerView userRecycler;
    private UserRecyclerAdapter profileRecyclerAdapter;
    private SharedPreferences pref;

    public User_Fragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_layout, container, false);


        Toolbar toolbar = view.findViewById(R.id.profile_toolbar);


        //set of lines for enabling Menu
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true); //to allow for sidebar
        activity.setSupportActionBar(toolbar);
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        DrawerLayout drawerLayout = view.findViewById(R.id.user_drawer);
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

        RoundedLetterView profile_icon = view.findViewById(R.id.user_profile_icon);
        TextView profile_name = view.findViewById(R.id.user_profile_name);
        TextView profile_email = view.findViewById(R.id.user_profile_email);

        profile_icon.setTitleText(firstletter);
        profile_name.setText(name);
        profile_email.setText(email);

        ImageButton edit_btn = view.findViewById(R.id.user_profile_edit_btn);

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Edit_User_Profile.class);
                startActivity(intent);
            }
        });

        this.userRecycler = view.findViewById(R.id.user_recycler);

        this.profileRecyclerAdapter = new UserRecyclerAdapter(context);

        this.userRecycler.setAdapter(this.profileRecyclerAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.context,2);
        this.userRecycler.setLayoutManager(gridLayoutManager);

        this.userRecycler.setNestedScrollingEnabled(false);

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