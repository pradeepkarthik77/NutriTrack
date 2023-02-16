package com.example.calorietracker;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity_holder extends AppCompatActivity
{

    private BottomNavigationView bottomNavigationView;
    private Home_Fragment home_fragment = new Home_Fragment(this);
    private Chart_Fragment chart_fragment = new Chart_Fragment(this);
    private Weekly_Fragment weekly_fragment = new Weekly_Fragment(this);
    private User_Fragment user_fragment = new User_Fragment(this);
    private Goal_Fragment goal_fragment = new Goal_Fragment(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity_layout);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.home: getSupportFragmentManager().beginTransaction().replace(R.id.container,home_fragment).commit();return true;
                    case R.id.report: getSupportFragmentManager().beginTransaction().replace(R.id.container,chart_fragment).commit();return true;
                    case R.id.goals: getSupportFragmentManager().beginTransaction().replace(R.id.container,goal_fragment).commit();return true;
                    case R.id.weekly: getSupportFragmentManager().beginTransaction().replace(R.id.container,weekly_fragment).commit();return true;
                    case R.id.user: getSupportFragmentManager().beginTransaction().replace(R.id.container,user_fragment).commit();return true;

                    default: getSupportFragmentManager().beginTransaction().replace(R.id.container,home_fragment).commit();return true;

                }
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.home);
//        bottomNavigationView.setItemIconTintList(null);



    }
}
