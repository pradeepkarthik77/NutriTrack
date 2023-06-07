package com.example.calorietracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Signup_details extends AppCompatActivity
{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentPagerAdapter fragmentStateAdapter;
    private MaterialButton back_btn;
    private MaterialButton next_btn;
    private ProgressBar progressBar;
    private Context context;

    public void showspinner()
    {
        progressBar = findViewById(R.id.google_signupdetails_progress);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hidespinner()
    {
        progressBar = findViewById(R.id.google_signupdetails_progress);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_details_layout);

        SharedPreferences pref = this.getSharedPreferences("Login",0);
        SharedPreferences.Editor editor = pref.edit();

        this.context = this;

        this.tabLayout = findViewById(R.id.tabDots);
        this.viewPager = findViewById(R.id.fragment_pager);
        this.fragmentStateAdapter = new signin_fragmentadapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(this.fragmentStateAdapter);
        this.tabLayout.setupWithViewPager(this.viewPager,true);
        this.progressBar = findViewById(R.id.google_signupdetails_progress);

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
                else //if(currentpage == 1)
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
                    next_btn.setText("Next");
                    viewPager.setCurrentItem(currentpage + 1, true);
                }

                else if(currentpage == 1) {
                    next_btn.setText("Finish");
                    viewPager.setCurrentItem(currentpage + 1, true);
                }

                else
                {
                    //TODO SAVE THE VALUES IN THE BACKEND

                    String BASE_URL = getString(R.string.BASE_URL);

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

                    RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                    HashMap<String,String> map = new HashMap<>();

                    map.put("email",pref.getString("email",""));
                    map.put("gender",pref.getString("Gender","Male"));
                    map.put("age", String.valueOf(pref.getInt("Age",30)));
                    map.put("height",String.valueOf(pref.getInt("Height",150)));
                    map.put("weight",String.valueOf(pref.getInt("Weight",50)));
                    map.put("activity_level",String.valueOf(pref.getInt("Activity_Level",0)));
                    map.put("goal_calorie",String.valueOf(pref.getInt("Goal_Calorie",0)));

                    class longthread extends Thread
                    {
                        @Override
                        public void run() {
                            runOnUiThread(Signup_details.this::showspinner);
                            Call<Void> call = retrofitInterface.executesignupdetails(map);

                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response)
                                {
                                    if(response.code() == 200)
                                    {
                                        Toast.makeText(getApplicationContext(), "Details Updated SuccessFully!!!", Toast.LENGTH_LONG).show();
                                        editor.putBoolean("isLoggedin",true);
                                        editor.commit();
                                        Intent newintent = new Intent(context,HomeActivity_holder.class);
                                        newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(newintent);
                                    }
                                    else if(response.code() == 400)
                                    {
                                        Toast.makeText(getApplicationContext(),"Unable to Update Values. Try Again",Toast.LENGTH_LONG).show();
                                    }
                                    runOnUiThread(Signup_details.this::hidespinner);
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(),"Unable to Update Details.Check Your Internet Connection And Try Again.",Toast.LENGTH_LONG).show();
                                    runOnUiThread(Signup_details.this::hidespinner);

//                                    Intent newintent = new Intent(getApplicationContext(),Signup_details.class);
//                                    newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(newintent);
                                }
                            });

                            //runOnUiThread(SignupActivity.this::hidespinner);
                        }
                    }

                    new longthread().start();

                }
            }
        });



    }
}