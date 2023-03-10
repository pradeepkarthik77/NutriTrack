package com.example.calorietracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class dashboard_chart_2_fragment extends Fragment {

    private Context context;
    private BarChart barChart;
    private SharedPreferences date_pref;
    private String[] days = new String[]{"Sun","Mon", "Tues","Wed","Thurs", "Fri", "Sat"};
    // variable for our bar data set.
    private BarDataSet barDataSet1, barDataSet2;

    // array list for storing entries.
    private ArrayList barEntries;


    public dashboard_chart_2_fragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_2_fragment, container, false);

        this.barChart = view.findViewById(R.id.home_bar_chart);
        this.date_pref = context.getSharedPreferences("date",0);

//        String getpast7date[] = getpastdates();

        InsertCSV insertCSV = new InsertCSV(context);

        String[] default_values = new String[]{"","BreakFast","Lunch","Dinner","Mid-Meals","Snacks","Juices"};

//        Float[] cal_values = insertCSV.get_past7_days(getpast7date);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        String chosen_date = date_pref.getString("chosen_date",formatter.format(new Date()));

        Float[] item_values = insertCSV.get_todays_meal_values(chosen_date);

        // creating a new bar data set.
        barDataSet1 = new BarDataSet(getBarEntriesOne(item_values), "Calories");
        barDataSet1.setColor(context.getResources().getColor(R.color.bar_chart_yellow));

        BarData data = new BarData(barDataSet1);

        barChart.setData(data);

        barChart.getDescription().setEnabled(false);
        barChart.setTouchEnabled(false);

        // below line is to get x axis
        // of our bar chart.
        XAxis xAxis = barChart.getXAxis();

        xAxis.setValueFormatter(new IndexAxisValueFormatter(default_values));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.parseColor("#FFFFFF"));

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.parseColor("#FFFFFF"));

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(false);

        barChart.setDragEnabled(true);

        barChart.setDrawGridBackground(false);

        SharedPreferences pref = context.getSharedPreferences("Login",0);

        String calgoals  = pref.getInt("Goal_Calorie",0)+"";

        Float cal_float =  0f;

        if(calgoals == "")
        {
            calgoals="0";
        }

        cal_float = Float.parseFloat(calgoals);

        LimitLine limitLine = new LimitLine(cal_float);
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(limitLine);

        data.setBarWidth(0.75f);

        barChart.getXAxis().setAxisMinimum(0);

        barChart.animateY(2000);

        barChart.getLegend().setTextColor(Color.parseColor("#FFFFFF"));

        barChart.getDescription().setTextColor(Color.parseColor("#FFFFFF"));

        return view;
    }

    private ArrayList<BarEntry> getBarEntriesOne(Float[] calorie_values)
    {
        // creating a new array list
        barEntries = new ArrayList<>();

        for(int i=0;i<7;i++)
        {
            barEntries.add(new BarEntry(i+1, calorie_values[i]));
        }

        return barEntries;
    }

//    private String[] getpastdates()
//    {
//
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//
//        String current_date = date_pref.getString("chosen_date",formatter.format(new Date()));
//
//        Date date = new Date();
//
//        try {
//            date = formatter.parse(current_date);
//        }
//        catch(Exception e)
//        {
//        }
//
//        String past_days[] = new String[]{"Monday", "Tuesday","Wednesday", "Thursday", "Friday", "Saturday","Sunday"};
//
//        for(int i=0;i<7;i++)
//        {
//            Calendar c = Calendar.getInstance();
//            c.setTime(date);
//            c.add(Calendar.DATE, -i);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            String item_date_string = simpleDateFormat.format(c.getTime());
//            past_days[i] = item_date_string;
//        }
//
//        return past_days;
//    }

//    private String[] getpastdays(String[] past7dates)
//    {
//        Date date = new Date();
//
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//
//        String current_date = date_pref.getString("chosen_date",formatter.format(new Date()));
//
//        String past_days[] = new String[]{"","Sunday","Monday", "Tuesday","Wednesday", "Thursday", "Friday", "Saturday"};
//
//        try {
//            date = formatter.parse(current_date);
//        }
//        catch (Exception e)
//        {
//
//        }
//
//        Calendar c = Calendar.getInstance();
//        c.setTime(date);
//
//        int current_day_indx = c.get(Calendar.DAY_OF_WEEK);
//
//        for(int i=0;i<7;i++)
//        {
//            c = Calendar.getInstance();
//            c.setTime(date);
//            c.add(Calendar.DATE, -i);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            int x = c.get(Calendar.DAY_OF_WEEK);
//            past_days[i+1] = days[(x-1)%7];
//        }
//        return past_days;
//    }
}