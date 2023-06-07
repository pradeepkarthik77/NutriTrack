package com.example.calorietracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class week_macro_fiber extends Fragment {

    private Context context;
    private BarChart calorie_barChart;
    private SharedPreferences date_pref;
    private String[] days = new String[]{"Sun","Mon","Tue","Wed","Thu", "Fri", "Sat"};
    // variable for our bar data set.
    private BarDataSet calorie_barDataset;
    // array list for storing entries.
    private ArrayList calorie_barEntry;

    public week_macro_fiber(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.week_macro_fiber_layout, container, false);

        init_calorie_barChart(view);

        return view;
    }

    private void init_calorie_barChart(View view)
    {
        calorie_barChart = view.findViewById(R.id.week_inner_fiber_bar);

        Chart_Week_Fragment chart_week_fragment = (Chart_Week_Fragment) this.getParentFragment().getParentFragment();

        ArrayList<String> past7dates =  chart_week_fragment.get_dates_range();

        InsertCSV insertCSV = new InsertCSV(context);

        String[] past7days_strings = past7dates.toArray(new String[past7dates.size()]);

        String[] x_axis_values = getpastdays(past7days_strings);

        Float cal_values[] = insertCSV.get_past7_days_fiber(past7days_strings);

        // creating a new bar data set.
        calorie_barDataset = new BarDataSet(getCalorieBarEntry(cal_values), "Fiber");
        calorie_barDataset.setColor(context.getResources().getColor(R.color.bar_chart_yellow));

        BarData data = new BarData(calorie_barDataset);

        calorie_barChart.setData(data);

        calorie_barChart.getDescription().setEnabled(false);
        calorie_barChart.setTouchEnabled(false);

        // below line is to get x axis
        // of our bar chart.
        XAxis xAxis = calorie_barChart.getXAxis();

        xAxis.setValueFormatter(new IndexAxisValueFormatter(x_axis_values));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.parseColor("#000000"));

        YAxis leftAxis = calorie_barChart.getAxisLeft();
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.parseColor("#000000"));

        YAxis rightAxis = calorie_barChart.getAxisRight();
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(false);

        calorie_barChart.setDragEnabled(true);

        calorie_barChart.setDrawGridBackground(false);

        SharedPreferences pref = context.getSharedPreferences("Login",0);

        String calgoals  = pref.getFloat("Goal_Fiber",0)+"";

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

        calorie_barChart.getXAxis().setAxisMinimum(0);

        calorie_barChart.animateY(2000);

        calorie_barChart.getLegend().setTextColor(Color.parseColor("#000000"));

        calorie_barChart.getDescription().setTextColor(Color.parseColor("#000000"));

        TextView average_intake = view.findViewById(R.id.week_inner_fiber_average);

        Float sum = 0f;

        for(Float i: cal_values)
        {
            sum+= i;
        }

        Float average = sum/cal_values.length;

        String formattedValue = String.format("%.2f", average);

        average_intake.setText("Average Daily Intake: "+formattedValue+"g");
    }

    private ArrayList<BarEntry> getCalorieBarEntry(Float[] calorie_values)
    {
        // creating a new array list
        calorie_barEntry = new ArrayList<>();

        for(int i=0;i<7;i++)
        {
            calorie_barEntry.add(new BarEntry(i+1, calorie_values[i]));
        }

        return calorie_barEntry;
    }

    private String[] getpastdays(String date_range[])
    {

        String return_values[] = new String[]{"","Sun","Mon","Tue","Wed","Thu","Fri","Sat"};

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        int i = 1;

        for(String date: date_range)
        {
            Calendar setcal = Calendar.getInstance();
            Date val_date = new Date();
            try {
                val_date = simpleDateFormat.parse(date);
            }
            catch(Exception e){}

            setcal.setTime(val_date);

            int val = setcal.get(Calendar.DAY_OF_WEEK);

            return_values[i++] = days[(val-1)];
        }

        return return_values;
    }
}