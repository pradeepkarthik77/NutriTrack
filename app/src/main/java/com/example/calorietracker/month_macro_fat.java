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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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

public class month_macro_fat extends Fragment {

    private Context context;
    private LineChart calorie_lineChart;
    // variable for our bar data set.
    private LineDataSet calorie_linDataSet;
    private SimpleDateFormat month_formatter;
    private Date current_month_object;
    private int total_month_days;
    private ArrayList calorie_barEntry;


    public month_macro_fat(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_macro_fat_layout, container, false);

        init_calorie_barChart(view);

        return view;
    }

    private void init_calorie_barChart(View view)
    {
        calorie_lineChart = view.findViewById(R.id.month_inner_fat_bar);

        Chart_Month_Fragment chart_month_fragment = (Chart_Month_Fragment) this.getParentFragment().getParentFragment();

        String current_month =  chart_month_fragment.get_today_month();

        this.month_formatter = new SimpleDateFormat("MMM-yyyy");

        this.current_month_object = new Date();

        try {
            this.current_month_object = this.month_formatter.parse(current_month);
        }
        catch (Exception e)
        {}

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(this.current_month_object);

        this.total_month_days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        InsertCSV insertCSV = new InsertCSV(context);

        Float cal_values[] = insertCSV.get_month_fat(current_month);

        String[] x_axis_values = new String[this.total_month_days];

        for(int i=1;i<=this.total_month_days;i++)
        {
            x_axis_values[i-1] = i+"";
        }

        // creating a new bar data set.
        calorie_linDataSet = new LineDataSet(getCalorieBarEntry(cal_values), "Fat");
        calorie_linDataSet.setColor(context.getResources().getColor(R.color.bar_chart_yellow));

        LineData data = new LineData(calorie_linDataSet);

        calorie_lineChart.setData(data);

        calorie_lineChart.getDescription().setEnabled(false);
        calorie_lineChart.setTouchEnabled(false);
        calorie_lineChart.setDragEnabled(true);
        calorie_lineChart.setScaleEnabled(true);
        calorie_lineChart.setPinchZoom(true);


        // below line is to get x axis
        // of our bar chart.
        XAxis xAxis = calorie_lineChart.getXAxis();

        xAxis.setValueFormatter(new IndexAxisValueFormatter(x_axis_values));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.parseColor("#000000"));

        YAxis leftAxis = calorie_lineChart.getAxisLeft();
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.parseColor("#000000"));

        YAxis rightAxis = calorie_lineChart.getAxisRight();
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(false);

        calorie_lineChart.setDragEnabled(true);

        calorie_lineChart.setDrawGridBackground(false);

        SharedPreferences pref = context.getSharedPreferences("Login",0);

        String calgoals  = pref.getFloat("Goal_Fat",0)+"";

        Float cal_float =  0f;

        if(calgoals == "")
        {
            calgoals="0";
        }

        cal_float = Float.parseFloat(calgoals);

        LimitLine limitLine = new LimitLine(cal_float);
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(limitLine);

        calorie_lineChart.getXAxis().setAxisMinimum(0);

        calorie_lineChart.animateY(2000);

        calorie_lineChart.getLegend().setTextColor(Color.parseColor("#000000"));

        calorie_lineChart.getDescription().setTextColor(Color.parseColor("#000000"));

        TextView average_intake = view.findViewById(R.id.month_inner_fat_average);

        Float sum = 0f;

        for(Float i: cal_values)
        {
            sum+= i;
        }

        Float average = sum/cal_values.length;

        String formattedValue = String.format("%.2f", average);

        average_intake.setText("Average Daily Intake: "+formattedValue+"g");
    }

    private ArrayList<Entry> getCalorieBarEntry(Float[] calorie_values)
    {
        // creating a new array list
        calorie_barEntry = new ArrayList<>();

        for(int i=1;i<=this.total_month_days;i++)
        {
            calorie_barEntry.add(new Entry(i+1, calorie_values[i-1]));
        }

        return calorie_barEntry;
    }
}