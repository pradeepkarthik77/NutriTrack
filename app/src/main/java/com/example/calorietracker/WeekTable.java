//package com.example.calorietracker;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CalendarView;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//public class WeekTable extends AppCompatActivity
//{
//    private Context context;
//    private RecyclerView weekRecycler;
//    private String chosen_date = "";
//    private String chosen_time = "";
//    private WeekRecyclerAdapter weekRecyclerAdapter;
//    private Date current_date;
//
//    private Dialog dialog;
//
//    private void create_dialogbox()
//    {
//        dialog = new Dialog(this);
//        dialog.setContentView(R.layout.timedate_dialog);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
//
//        Spinner spinner = (Spinner) dialog.findViewById(R.id.dateortime);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.dateortime, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//        CalendarView calendarView = (CalendarView) dialog.findViewById(R.id.select_date);
//        TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.select_time);
//        Button apply_btn = (Button) dialog.findViewById(R.id.apply_btn);
//        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
//            {
//                String chosen = (String) adapterView.getItemAtPosition(i);
//
//                if(chosen.equals("Date"))
//                {
//                    timePicker.setVisibility(View.INVISIBLE);
//                    calendarView.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    calendarView.setVisibility(View.INVISIBLE);
//                    timePicker.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView)
//            {
//                calendarView.setVisibility(View.INVISIBLE);
//                timePicker.setVisibility(View.VISIBLE);
//            }
//        });
//
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayofmonth)
//            {
//                String datestring="";
//                String monthstring = "";
//
//                if(dayofmonth<10)
//                {
//                    datestring = "0"+dayofmonth;
//                }
//                else
//                {
//                    datestring = ""+dayofmonth;
//                }
//
//                if(month<10)
//                {
//                    monthstring = "0"+(month+1);
//                }
//                else
//                {
//                    monthstring = ""+(month+1);
//                }
//
//                chosen_date = datestring+"/"+monthstring+"/"+year;
//            }
//        });
//
//        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker timePicker, int hr, int min)
//            {
//                String hour = "";
//                String time = "";
//
//                if(hr<10)
//                {
//                    hour = "0"+hr;
//                }
//                else
//                {
//                    hour = hr+"";
//                }
//
//                if(min<10)
//                {
//                    time = "0"+min;
//                }
//                else
//                {
//                    time = min+"";
//                }
//
//                chosen_time = hour+":"+time;
//            }
//        });
//
//        apply_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DisplayTable.set_chosen_time(chosen_time);
//                DisplayTable.set_chosen_date(chosen_date);
//                MainCardAdapter.set_chosen_date(chosen_date);
//                MainCardAdapter.set_chosen_time(chosen_time);
//                Intent intent = new Intent(getApplicationContext(),WeekTable.class);
//                intent.putExtra("chosen_date",chosen_date);
//                intent.putExtra("chosen_time",chosen_time);
//                finish();
//                startActivity(intent);
//            }
//        });
//
//        cancel_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.create();
//    }
//
//
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.weektable_layout);
//
//        this.context = this;
//
//        Intent intent = getIntent();
//
//        this.chosen_date = intent.getStringExtra("chosen_date");
//        this.chosen_time = intent.getStringExtra("chosen_time");
//
//        ImageButton display_btn = findViewById(R.id.display_back_btn);
//
//        create_dialogbox();
//
//        display_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//        if(this.chosen_date.equals(""))
//        {
//            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//            Date date1 = new Date();
//            this.chosen_date = formatter.format(date1);
//            //Toast.makeText(getApplicationContext(),formatter.format(date),Toast.LENGTH_SHORT).show();
//        }
//
//        if(this.chosen_time.equals(""))
//        {
//            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
//            Date date1 = new Date();
//            this.chosen_time = formatter.format(date1);
//        }
//
//        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolBar);
//
//        TextView title = (TextView) findViewById(R.id.display_title);
//
//        title.setText("Weekly Table");
//
//        ImageView calender_btn = findViewById(R.id.add_photo);
//
//        calender_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.show();
//            }
//        });
//
//        this.weekRecycler = (RecyclerView) findViewById(R.id.weektable_recycler);
//
//        this.current_date = new Date();
//
//        try {
//            this.current_date = new SimpleDateFormat("dd/MM/yyyy").parse(chosen_date);
//        } catch (ParseException e) {
//            Toast.makeText(this,"Unable to Parse Date",Toast.LENGTH_LONG).show();
//            finish();
//        }
////        Calendar c = Calendar.getInstance();
////        c.setTime(date);
////        c.add(Calendar.DATE, -1);
////        date = c.getTime();
//        //TODO: add this code when during implementing monday,tuesday etc
//
//
//        this.weekRecycler = (RecyclerView) findViewById(R.id.weektable_recycler);
//
//        ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result)
//                    {
//                        if(result.getResultCode() == 0)
//                        {
//                            //result.getData().getStringExtra("recycler_id")
//                            int id = 0;
//                            id = Integer.valueOf(result.getData().getStringExtra("recycler_id"));
//                            weekRecycler.getAdapter().notifyDataSetChanged();
//                        }
//                    }
//                });
//
//        this.weekRecyclerAdapter = new WeekRecyclerAdapter(this,this.current_date,activityResultLaunch);
//
//        this.weekRecycler.setAdapter(this.weekRecyclerAdapter);
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.context,2);
//        this.weekRecycler.setLayoutManager(gridLayoutManager);
//
//
//
//    }
//}
