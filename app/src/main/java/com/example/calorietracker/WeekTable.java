package com.example.calorietracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeekTable extends AppCompatActivity
{
    private Context context;
    private RecyclerView weekRecycler;
    private String chosen_date = "";
    private String chosen_time = "";
    private WeekRecyclerAdapter weekRecyclerAdapter;
    private Date current_date;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weektable_layout);

        this.context = this;

        Intent intent = getIntent();

        this.chosen_date = intent.getStringExtra("chosen_date");
        this.chosen_time = intent.getStringExtra("chosen_time");

        ImageButton display_btn = findViewById(R.id.display_back_btn);

        display_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(this.chosen_date.equals(""))
        {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = new Date();
            this.chosen_date = formatter.format(date1);
            //Toast.makeText(getApplicationContext(),formatter.format(date),Toast.LENGTH_SHORT).show();
        }

        if(this.chosen_time.equals(""))
        {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date1 = new Date();
            this.chosen_time = formatter.format(date1);
        }

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        TextView title = (TextView) findViewById(R.id.display_title);

        title.setText("Weekly Table");

        this.weekRecycler = (RecyclerView) findViewById(R.id.weektable_recycler);

        this.current_date = new Date();

        try {
            this.current_date = new SimpleDateFormat("dd/MM/yyyy").parse(chosen_date);
        } catch (ParseException e) {
            Toast.makeText(this,"Unable to Parse Date",Toast.LENGTH_LONG).show();
            finish();
        }
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);
//        c.add(Calendar.DATE, -1);
//        date = c.getTime();
        //TODO: add this code when during implementing monday,tuesday etc


        this.weekRecycler = (RecyclerView) findViewById(R.id.weektable_recycler);

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
                            weekRecycler.getAdapter().notifyDataSetChanged();
                        }
                    }
                });

        this.weekRecyclerAdapter = new WeekRecyclerAdapter(this,this.current_date,activityResultLaunch);

        this.weekRecycler.setAdapter(this.weekRecyclerAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.context,2);
        this.weekRecycler.setLayoutManager(gridLayoutManager);



    }
}
