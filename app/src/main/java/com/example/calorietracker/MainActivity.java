package com.example.calorietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity
{

    private MainCardAdapter mainCardAdapter;
    private RecyclerView mainRecycler;
    private Context context;
    private ImageButton imageButton;
    private Dialog dialog;

    public static String chosen_time = "";
    public static String chosen_date = "";

    private void create_dialogbox()
    {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.timedate_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Spinner spinner = (Spinner) dialog.findViewById(R.id.dateortime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.dateortime, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        CalendarView calendarView = (CalendarView) dialog.findViewById(R.id.select_date);
        TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.select_time);
        Button apply_btn = (Button) dialog.findViewById(R.id.apply_btn);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String chosen = (String) adapterView.getItemAtPosition(i);

                if(chosen.equals("Date"))
                {
                    timePicker.setVisibility(View.INVISIBLE);
                    calendarView.setVisibility(View.VISIBLE);
                }
                else
                {
                    calendarView.setVisibility(View.INVISIBLE);
                    timePicker.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                calendarView.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.VISIBLE);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayofmonth)
            {

                chosen_date = dayofmonth+"/"+month+"/"+year;
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hr, int min)
            {
                String hour = "";
                String time = "";

                if(hr<10)
                {
                    hour = "0"+hr;
                }
                else
                {
                    hour = hr+"";
                }

                if(min<10)
                {
                    time = "0"+min;
                }
                else
                {
                    time = min+"";
                }

                chosen_time = hour+":"+time;
            }
        });

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayTable.set_chosen_time(chosen_time);
                DisplayTable.set_chosen_date(chosen_date);
                MainCardAdapter.set_chosen_date(chosen_date);
                MainCardAdapter.set_chosen_time(chosen_time);
                dialog.dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitle("Calorie Tracker");
        setSupportActionBar(toolBar);
        create_dialogbox();

        this.imageButton = findViewById(R.id.edit_time);

        this.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        FloatingActionButton tablebtn = findViewById(R.id.table_btn);

        tablebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                context.startActivity(intent);
            }
        });

        this.mainRecycler = (RecyclerView) findViewById(R.id.main_recycler);

        this.mainCardAdapter = new MainCardAdapter(this);

        this.mainRecycler.setAdapter(this.mainCardAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.context,2);
        this.mainRecycler.setLayoutManager(gridLayoutManager);
    }
}