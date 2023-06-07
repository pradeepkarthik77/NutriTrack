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

public class year_macro_carbs extends Fragment {

    private Context context;
    private BarChart calorie_barChart;
    private SharedPreferences date_pref;
    private String[] months = new String[]{"","Jan","Feb","Mar","Apr","May", "Jun", "Jul","Aug","Sep","Oct","Nov","Dec"};
    // variable for our bar data set.
    private BarDataSet calorie_barDataset;
    // array list for storing entries.
    private ArrayList calorie_barEntry;
    private String this_year;

    public year_macro_carbs(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.year_macro_carbs, container, false);

        init_calorie_barChart(view);

        return view;
    }

    private void init_calorie_barChart(View view)
    {
        calorie_barChart = view.findViewById(R.id.year_inner_carbs_bar);

        InsertCSV insertCSV = new InsertCSV(context);

        Chart_Year_Fragment chart_year_fragment = (Chart_Year_Fragment) getParentFragment().getParentFragment();

        this.this_year = chart_year_fragment.get_year();

        Float cal_values[] = insertCSV.get_year_carbs(this_year);

        // creating a new bar data set.
        calorie_barDataset = new BarDataSet(getCalorieBarEntry(cal_values), "Carbs");
        calorie_barDataset.setColor(context.getResources().getColor(R.color.bar_chart_yellow));

        BarData data = new BarData(calorie_barDataset);
        data.setBarWidth(0.1f);
        calorie_barChart.setData(data);

        calorie_barChart.getDescription().setEnabled(false);
        calorie_barChart.setTouchEnabled(false);

        // below line is to get x axis
        // of our bar chart.
        XAxis xAxis = calorie_barChart.getXAxis();

        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(13);
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
        leftAxis.removeAllLimitLines();

        data.setBarWidth(0.75f);

        calorie_barChart.getXAxis().setAxisMinimum(0);

        calorie_barChart.animateY(2000);

        calorie_barChart.getLegend().setTextColor(Color.parseColor("#000000"));

        calorie_barChart.getDescription().setTextColor(Color.parseColor("#000000"));

        TextView average_intake = view.findViewById(R.id.year_inner_carbs_average);

        Float sum = 0f;

        for(Float i: cal_values)
        {
            sum+= i;
        }

        Float average = sum/cal_values.length;

        String formattedValue = String.format("%.2f", average);

        average_intake.setText("Average Monthly Intake: "+formattedValue+"g");
    }

    private ArrayList<BarEntry> getCalorieBarEntry(Float[] calorie_values)
    {
        // creating a new array list
        calorie_barEntry = new ArrayList<>();

        for(int i=0;i<12;i++)
        {
            calorie_barEntry.add(new BarEntry(i+1, calorie_values[i]));

        }
        return calorie_barEntry;
    }
}