package com.example.calorietracker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity_holder extends AppCompatActivity
{

    private BottomNavigationView bottomNavigationView;
    private Home_Fragment home_fragment = new Home_Fragment(this);
    private Chart_Fragment chart_fragment = new Chart_Fragment(this);
    private Weekly_Fragment weekly_fragment = new Weekly_Fragment(this);
    private User_Fragment user_fragment = new User_Fragment(this);
    private Goal_Fragment goal_fragment = new Goal_Fragment(this);
//    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity_layout);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

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

//        DrawerLayout drawerLayout = findViewById(R.id.navbar_drawer);
//
//
//        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        NavigationView navigationView = findViewById(R.id.nav_view );

    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (toggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout ) ;
//        if (drawer.isDrawerOpen(GravityCompat.START )) {
//            drawer.closeDrawer(GravityCompat.START ) ;
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu (Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.activity_main_drawer,menu) ;
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected (MenuItem item) {
//        int id = item.getItemId() ;
////        if (id == R.id.action_settings ) {
////            return true;
////        }
//        return super.onOptionsItemSelected(item) ;
//    }
//    @SuppressWarnings ( "StatementWithEmptyBody" )
//    @Override
//    public boolean onNavigationItemSelected ( @NonNull MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId() ;
//        if (id == R.id. nav_camera ) {
//            // Handle the camera action
//        } else if (id == R.id. nav_gallery ) {
//        } else if (id == R.id. nav_slideshow ) {
//        } else if (id == R.id. nav_manage ) {
//        } else if (id == R.id. nav_share ) {
//        } else if (id == R.id. nav_send ) {
//        }
//        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
//        drawer.closeDrawer(GravityCompat. START ) ;
//        return true;
//    }
}
