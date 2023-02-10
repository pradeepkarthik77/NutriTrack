package com.example.calorietracker;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Signup_details extends AppCompatActivity
{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentPagerAdapter fragmentStateAdapter;
    private MaterialButton back_btn;
    private MaterialButton next_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_details_layout);

        this.tabLayout = findViewById(R.id.tabDots);
        this.viewPager = findViewById(R.id.fragment_pager);
        this.fragmentStateAdapter = new signin_fragmentadapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(this.fragmentStateAdapter);
        this.tabLayout.setupWithViewPager(this.viewPager,true);

        this.back_btn = findViewById(R.id.back_btn);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        this.next_btn = findViewById(R.id.next_btn);

        this.back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentpage = viewPager.getCurrentItem();

                if(currentpage == 0)
                {
//                    viewPager.setCurrentItem(currentpage-1,true);
                    next_btn.setText("Next");
                }
                else if(currentpage == 1)
                {
                    next_btn.setText("Next");
                    viewPager.setCurrentItem(currentpage-1,true);
                }
            }
        });



        this.next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentpage = viewPager.getCurrentItem();

                if(currentpage == 0)
                {
                    next_btn.setText("Finish");

                    viewPager.setCurrentItem(currentpage+1,true);

                }
                else
                {
                    next_btn.setText("Next");
                    viewPager.setCurrentItem(currentpage+1,true);
                }
            }
        });



    }
}