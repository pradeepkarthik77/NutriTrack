package com.example.calorietracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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


public class MainActivity extends AppCompatActivity
{

    private MainCardRecycler mainCardRecycler;
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
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {

                chosen_time = i+":"+i1;
            }
        });

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayTable.set_chosen_time(chosen_time);
                DisplayTable.set_chosen_date(chosen_date);
                MainInnerRecyclerAdapter.set_chosen_date(chosen_date);
                MainInnerRecyclerAdapter.set_chosen_time(chosen_time);
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

        //Toast.makeText(getApplicationContext(),chosen_date+" "+chosen_date,Toast.LENGTH_LONG).show();


        this.mainRecycler = (RecyclerView) findViewById(R.id.main_recycler);
        ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result)
                    {
                        if(result.getResultCode() == 0)
                        {
                            for(int x = 0;x<mainRecycler.getChildCount();x++)
                            {
                                MainCardRecyclerAdapter.ViewHolder holder = (MainCardRecyclerAdapter.ViewHolder) mainRecycler.getChildViewHolder(mainRecycler.getChildAt(x));
                                if(holder.card_title.getText().toString().equals(result.getData().getStringExtra("cardview_name")))
                                {
                                    MainInnerRecyclerAdapter mainInnerRecyclerAdapter = (MainInnerRecyclerAdapter) holder.inner_card_recycler.getAdapter();
                                    mainInnerRecyclerAdapter.UpdateViews();
                                }
                            }
                        }
                    }
                });



        this.mainCardRecycler = new MainCardRecycler(this,activityResultLaunch);

        this.mainCardRecycler.setAdapterforRecycler(this.mainRecycler);

    }
}