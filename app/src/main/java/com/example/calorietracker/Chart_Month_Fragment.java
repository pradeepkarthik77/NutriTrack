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

public class Chart_Month_Fragment extends Fragment
{

    private Context context;
    private SharedPreferences date_pref;
    private SimpleDateFormat formatter;
    private String today_date;
    private Date today_date_object;
    private SimpleDateFormat month_formatter;
    private AppCompatActivity appCompatActivity;
    private String today_month;

    public Chart_Month_Fragment(Context context)
    {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_month_fragment_layout, container, false);

        this.date_pref = context.getSharedPreferences("date",0);

        this.formatter = new SimpleDateFormat("dd/MM/yyyy");
        this.month_formatter = new SimpleDateFormat("MMM-yyyy");

//        this.placeholder = view.findViewById(R.id.date_placeholder);

        this.today_date = this.date_pref.getString("chosen_date",formatter.format(new Date()));

        try{
            this.today_date_object = this.formatter.parse(this.today_date);
        }
        catch (Exception e)
        {
            today_date_object = new Date();
        }

        this.today_month = this.month_formatter.format(today_date_object);

        String to_set_text = this.month_formatter.format(this.today_date_object);

        TextView chosen_month = view.findViewById(R.id.chosen_month);

        chosen_month.setText(to_set_text);

        ImageButton month_previous = view.findViewById(R.id.month_previous);
        ImageButton month_forward = view.findViewById(R.id.month_forward);

        month_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String previous_month = get_previous_month(today_date_object, true);

//                Toast.makeText(context,previous_month,Toast.LENGTH_LONG).show();
//                Toast.makeText(context,today_month,Toast.LENGTH_LONG).show();

                change_today_month(previous_month);

                chosen_month.setText(previous_month);

                Inner_Chart_Month_Fragment inner_chart_month_fragment = (Inner_Chart_Month_Fragment) getChildFragmentManager().findFragmentById(R.id.month_fragment_holder);

                getChildFragmentManager().beginTransaction().detach(inner_chart_month_fragment).commit();

                Inner_Chart_Month_Fragment inner_chart_month_fragment1 = new Inner_Chart_Month_Fragment(context);

                getChildFragmentManager().beginTransaction().replace(R.id.month_fragment_holder,inner_chart_month_fragment1).commit();

            }
        });

        month_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String previous_month = get_previous_month(today_date_object, false);

                change_today_month(previous_month);

                chosen_month.setText(previous_month);

                Inner_Chart_Month_Fragment inner_chart_month_fragment = (Inner_Chart_Month_Fragment) getChildFragmentManager().findFragmentById(R.id.month_fragment_holder);

                getChildFragmentManager().beginTransaction().detach(inner_chart_month_fragment).commit();

                Inner_Chart_Month_Fragment inner_chart_month_fragment1 = new Inner_Chart_Month_Fragment(context);

                getChildFragmentManager().beginTransaction().replace(R.id.month_fragment_holder,inner_chart_month_fragment1).commit();


            }
        });

        this.appCompatActivity = (AppCompatActivity) context;

        getChildFragmentManager().beginTransaction().add(R.id.month_fragment_holder, new Inner_Chart_Month_Fragment(context)).commit();

        return view;
    }

    private void change_today_month(String previous_month)
    {
        this.today_month = previous_month;

        try{
            this.today_date_object = this.month_formatter.parse(this.today_month);
        }
        catch (Exception e)
        {
            today_date_object = new Date();
        }
    }

    private String get_previous_month(Date today_date_object, boolean isprevious)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(today_date_object);
        if(isprevious)
            c.add(Calendar.MONTH, -1);
        else
            c.add(Calendar.MONTH,1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM-yyyy");
        String item_date_string = simpleDateFormat.format(c.getTime());
        return item_date_string;
    }

    public String get_today_month()
    {
        return this.today_month;
    }

}
