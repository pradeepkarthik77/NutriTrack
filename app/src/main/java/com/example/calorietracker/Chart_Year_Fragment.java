package com.example.calorietracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Chart_Year_Fragment extends Fragment
{

    private Context context;
    private SharedPreferences date_pref;
    private SimpleDateFormat formatter;
    private String today_date;
    private Date today_date_object;
    private SimpleDateFormat year_formatter;
    private AppCompatActivity appCompatActivity;
    private String today_year;

    public Chart_Year_Fragment(Context context)
    {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_year_fragment_layout, container, false);

        this.date_pref = context.getSharedPreferences("date",0);

        this.formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.year_formatter = new SimpleDateFormat("yyyy");

//        this.placeholder = view.findViewById(R.id.date_placeholder);

        this.today_date = this.date_pref.getString("chosen_date",formatter.format(new Date()));

        Date date = new Date();

        try{
            date = this.formatter.parse(this.today_date);
        }catch (Exception e){}

        this.today_year = (date.getYear()+1900)+"";

        TextView chosen_year = view.findViewById(R.id.chosen_year);

        chosen_year.setText(this.today_year);

        ImageButton year_previous = view.findViewById(R.id.year_previous);
        ImageButton year_forward = view.findViewById(R.id.year_forward);

        year_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String previous_year = get_previous_year(today_year, true);

                change_today_year(previous_year);

                chosen_year.setText(previous_year);

                Inner_Chart_Year_Fragment inner_chart_year_fragment = (Inner_Chart_Year_Fragment) getChildFragmentManager().findFragmentById(R.id.year_fragment_holder);

                getChildFragmentManager().beginTransaction().detach(inner_chart_year_fragment).commit();

                Inner_Chart_Year_Fragment inner_chart_year_fragment1 = new Inner_Chart_Year_Fragment(context);

                getChildFragmentManager().beginTransaction().replace(R.id.year_fragment_holder,inner_chart_year_fragment1).commit();

            }
        });

        year_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String previous_year = get_previous_year(today_year, false);

                change_today_year(previous_year);

                chosen_year.setText(previous_year);

                Inner_Chart_Year_Fragment inner_chart_year_fragment = (Inner_Chart_Year_Fragment) getChildFragmentManager().findFragmentById(R.id.year_fragment_holder);

                getChildFragmentManager().beginTransaction().detach(inner_chart_year_fragment).commit();

                Inner_Chart_Year_Fragment inner_chart_year_fragment1 = new Inner_Chart_Year_Fragment(context);

                getChildFragmentManager().beginTransaction().replace(R.id.year_fragment_holder,inner_chart_year_fragment1).commit();


            }
        });

        this.appCompatActivity = (AppCompatActivity) context;

        getChildFragmentManager().beginTransaction().add(R.id.year_fragment_holder, new Inner_Chart_Year_Fragment(context)).commit();

        return view;
    }

    private void change_today_year(String previous_year)
    {
        this.today_year = previous_year;

        try{
            this.today_date_object = this.year_formatter.parse(this.today_year);
        }
        catch (Exception e)
        {
            today_date_object = new Date();
        }
    }

    private String get_previous_year(String today_year, boolean isprevious)
    {

        Integer year = Integer.parseInt(today_year);

        if(isprevious)
            return (year-1)+"";
        else
            return (year+1)+"";
    }

    public String get_year()
    {
        return this.today_year;
    }

}
