package com.example.calorietracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity
{
    private String email="";
    private String name="";

    private String[] maintitle = {
            "Profile",
            "Help",
            "Assess Meal",
            "Notice Board",
            "Mid-Meal Diet",
            "Daily/Weekly/Monthly",
            "Goal Setting",
            "Reports"
    };

    private String[] subtitle ={
            "Sub Title 1","Sub Title 2",
            "Sub Title 3","Sub Title 4",
            "Sub Title 5","Sub Title 6",
            "Sub Title 7","Sub Title 8"};

    private Integer[] imgid={
            R.drawable.southmeal,R.drawable.helpnutrition,
            R.drawable.assessmeal,R.drawable.noticeboard,
            R.drawable.midtimediet,R.drawable.chartanalysis,
            R.drawable.dietgoal,R.drawable.nutritionfacts,
            R.drawable.mealreport
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.homeactivity);

        Toolbar toolBar = (Toolbar) findViewById(R.id.list_toolbar);
        toolBar.setTitle("24/7 Dietary Assessment");
        setSupportActionBar(toolBar);

        SharedPreferences pref = getSharedPreferences("Login",0);

        this.email = pref.getString("email","");
        this.name = pref.getString("name","");

        ImageView homeactivity_image = findViewById(R.id.homeactivity_image);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        homeactivity_image.getLayoutParams().height = (int)(((double)height)/ 4);//1.75);

        HomeActivity_adapter adapter=new HomeActivity_adapter(this, maintitle, subtitle,imgid);
        ListView list=(ListView)findViewById(R.id.home_list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if(position == 0) {
                    //code specific to first list item
                    //Toast.makeText(getApplicationContext(),"Place Your First Option Code",Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(myIntent);
                }

                else if(position == 1) {
                    Intent intent = new Intent(getApplicationContext(),HelpActivity.class);
                    startActivity(intent);
                }

                else if(position == 2) {

                    //Toast.makeText(getApplicationContext(),"Place Your Third Option Code",Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(myIntent);
                }
                else if(position == 3) {

                    //Toast.makeText(getApplicationContext(),"Place Your Forth Option Code",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),WeekTable.class);
                    intent.putExtra("chosen_date","");
                    intent.putExtra("chosen_time","");
                    startActivity(intent);

                }
                else if(position == 4) {

                    //Toast.makeText(getApplicationContext(),"Place Your Fifth Option Code",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MidMealDiet.class);
                    intent.putExtra("chosen_date","");
                    intent.putExtra("chosen_time","");
                    startActivity(intent);
                }
                else if(position == 5) {

                    Toast.makeText(getApplicationContext(),"Place Your Seventh Option Code",Toast.LENGTH_SHORT).show();
//                    Intent myIntent = new Intent(MainActivity.this, MealActivity.class);
//                    myIntent.putExtra("key", "Hello"); //Optional parameters
//                    MainActivity.this.startActivity(myIntent);
                }
                else if(position == 6) {

//                    Toast.makeText(getApplicationContext(),"Place Your Eighth Option Code",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),GoalSettingActivity.class);
                    startActivity(intent);
                }
                else if(position == 7) {
                    //Toast.makeText(getApplicationContext(),"Place Your Eighth Option Code",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),ChartActivity.class);
                    startActivity(intent);
                }

            }
        });

    }
}
