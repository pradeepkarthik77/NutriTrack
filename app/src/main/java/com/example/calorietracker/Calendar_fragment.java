package com.example.calorietracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar_fragment extends Fragment {

    private Context context;
    private CalendarView calendarView;


    public Calendar_fragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homeactivity_calender_fragment, container, false);

        SharedPreferences pref = context.getSharedPreferences("date",0);

        SharedPreferences.Editor edit = pref.edit();

        this.calendarView = view.findViewById(R.id.dashboard_calendar);

        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        String before_date = dateFormat.format(date);

        before_date = pref.getString("chosen_date",before_date);

        try {
            date = dateFormat.parse(before_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.calendarView.setDate(date.getTime());

        Home_Fragment home_fragment = (Home_Fragment) this.getParentFragment();

        this.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                edit.putString("chosen_date",dayOfMonth+"/"+month+"/"+year);
                edit.commit();
                home_fragment.set_calender_text(pref.getString("chosen_date","Today"));
            }
        });

        return view;
    }
}