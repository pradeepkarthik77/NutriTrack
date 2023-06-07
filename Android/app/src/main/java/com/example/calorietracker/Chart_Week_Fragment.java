package com.example.calorietracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Chart_Week_Fragment extends Fragment
{

    private Context context;
    private SharedPreferences date_pref;
    private SimpleDateFormat formatter;
    private String today_date;
    private Date today_date_object;
    private SimpleDateFormat month_formatter;
    private AppCompatActivity appCompatActivity;
    private TextView placeholder;
    private SimpleDateFormat placeholder_formatter;

    public Chart_Week_Fragment(Context context)
    {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_week_fragment_layout, container, false);

        this.date_pref = context.getSharedPreferences("date",0);

        this.formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.month_formatter = new SimpleDateFormat("dd/MMM");
        this.placeholder_formatter = new SimpleDateFormat("dd/MM/yyyy");

        this.placeholder = view.findViewById(R.id.date_placeholder);

        this.today_date = this.date_pref.getString("chosen_date",formatter.format(new Date()));

        String to_set_to_week[] = find_the_previous_7_days_of(today_date);

        TextView chosen_week = view.findViewById(R.id.chosen_week);

        chosen_week.setText(to_set_to_week[0]);

//        String placeholder_today = this.today_date;
//        String placeholder_prev = get_week_before_date(this.today_date,true);

        placeholder.setText(to_set_to_week[1]);

        ImageButton week_previous = view.findViewById(R.id.week_previous);
        ImageButton week_forward = view.findViewById(R.id.week_forward);

        week_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String previous_date = get_week_before_date(today_date, true);

                Date prev_date = new Date();

                try {
                    prev_date = placeholder_formatter.parse(previous_date);
                }catch (Exception e){}

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(prev_date);

                calendar.add(Calendar.DATE,-1);

                String prev_prev_date = placeholder_formatter.format(calendar.getTime());

                change_today_text(prev_prev_date);

                String to_set_val[] = find_the_previous_7_days_of(prev_prev_date);
                chosen_week.setText(to_set_val[0]);

                placeholder.setText(to_set_val[1]);

                Inner_Chart_Week_Fragment inner_chart_week_fragment = (Inner_Chart_Week_Fragment) getChildFragmentManager().findFragmentById(R.id.week_fragment_holder);

                getChildFragmentManager().beginTransaction().detach(inner_chart_week_fragment).commit();

                Inner_Chart_Week_Fragment inner_chart_week_fragment1 = new Inner_Chart_Week_Fragment(context);

                getChildFragmentManager().beginTransaction().replace(R.id.week_fragment_holder,inner_chart_week_fragment1).commit();

            }
        });

        week_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String forward_date = get_week_before_date(today_date,false);

                Date next_date = new Date();

                try {
                    next_date = placeholder_formatter.parse(today_date);
                }catch (Exception e){}

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(next_date);

                calendar.add(Calendar.DATE,1);

                String next_next_date = placeholder_formatter.format(calendar.getTime());

                change_today_text(next_next_date);

                String to_set_vals[] = find_the_next_7_days_of(next_next_date);
                chosen_week.setText(to_set_vals[0]);
                change_today_text(get_week_before_date(next_next_date,false));
                placeholder.setText(to_set_vals[1]);

                Inner_Chart_Week_Fragment inner_chart_week_fragment = (Inner_Chart_Week_Fragment) getChildFragmentManager().findFragmentById(R.id.week_fragment_holder);

                getChildFragmentManager().beginTransaction().detach(inner_chart_week_fragment).commit();

                Inner_Chart_Week_Fragment inner_chart_week_fragment1 = new Inner_Chart_Week_Fragment(context);

                getChildFragmentManager().beginTransaction().replace(R.id.week_fragment_holder,inner_chart_week_fragment1).commit();


            }
        });

        this.appCompatActivity = (AppCompatActivity) context;

//        appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.week_fragment_holder,new Inner_Chart_Week_Fragment(context)).commit();

        getChildFragmentManager().beginTransaction().add(R.id.week_fragment_holder, new Inner_Chart_Week_Fragment(context)).commit();

        return view;
    }

    private String[] find_the_next_7_days_of(String today_date) {
        //the idea of this function is to find the date of the p7th before day and return the string

        String arrs[] = new String[2];

        try {
            this.today_date_object = this.formatter.parse(today_date);
        }
        catch (Exception e)
        {
            this.today_date_object = new Date();
        }

        String next_date = get_week_before_date(today_date,false);

        Date week_next_object = new Date();

        try {
            week_next_object = this.formatter.parse(next_date);
        }
        catch (Exception e)
        {}

        String formatted_today = month_formatter.format(this.today_date_object);
        String formatter_next = month_formatter.format(week_next_object);

        String placeholder_today = placeholder_formatter.format(this.today_date_object);
        String placeholder_next = placeholder_formatter.format(week_next_object);

        arrs[0] = formatted_today+" - "+formatter_next;

        arrs[1] = placeholder_today+","+placeholder_next;

        return arrs;
    }

    public void change_today_text(String today_date)
    {
        this.today_date = today_date;
    }

    private String[] find_the_previous_7_days_of(String today_date)
    {
        //the idea of this function is to find the date of the p7th before day and return the string
        try {
            this.today_date_object = this.formatter.parse(today_date);
        }
        catch (Exception e)
        {
            this.today_date_object = new Date();
        }

        String previous_date = get_week_before_date(today_date,true);

        Date week_previous_object = new Date();

        try {
            week_previous_object = this.formatter.parse(previous_date);
        }
        catch (Exception e)
        {}

        String formatted_today = month_formatter.format(this.today_date_object);
        String formatted_previous = month_formatter.format(week_previous_object);

        String placeholder_today = placeholder_formatter.format(this.today_date_object);
        String placeholder_previous = placeholder_formatter.format(week_previous_object);


        String arrs[] = new String[2];

        arrs[0] = formatted_previous+" - "+formatted_today;

        arrs[1] = placeholder_previous+","+placeholder_today;

        return arrs;

    }

    public String get_week_before_date(String today_date,boolean isprevious)
    {
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        try {
            date = formatter.parse(today_date);
        }
        catch (Exception e)
        {}
        c.setTime(date);
        if(isprevious)
            c.add(Calendar.DATE, -6);
        else
            c.add(Calendar.DATE,6);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String item_date_string = simpleDateFormat.format(c.getTime());
        return item_date_string;
    }

    public ArrayList<String> get_dates_range()
    {
        String dates = placeholder.getText().toString();

        String arr[] = dates.split(",");

        String upper_date = arr[1];
        String lower_date = arr[0];

        ArrayList<String> range_dates = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        Date date = new Date();
        try {
            date = formatter.parse(lower_date);
        }
        catch (Exception e)
        {}
        c.setTime(date);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String current_date = lower_date;

        while(!current_date.equals(upper_date))
        {
            range_dates.add(current_date);
            c.add(Calendar.DATE,1);
            current_date = simpleDateFormat.format(c.getTime());
        }

        range_dates.add(upper_date);

        return range_dates;

    }

}
