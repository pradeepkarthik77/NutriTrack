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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class dashboard_chart_1_fragment extends Fragment {

    private Context context;
    private SharedPreferences date_pref;

    public dashboard_chart_1_fragment(Context context)
    {
        this.context = context;
    }

    private void initPieChart(PieChart pieChart)
    {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        //enabling the user to rotate the chart, default true
        pieChart.setRotationEnabled(true);
        //adding friction when rotating the pie chart
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        //setting the first entry start from right hand side, default starting from top
        pieChart.setRotationAngle(0);

        //highlight the entry when it is tapped, default true if not set
        pieChart.setHighlightPerTapEnabled(true);
        //adding animation so the entries pop up from 0 degree
        pieChart.animateY(1400,Easing.EaseInOutQuad);
        //setting the color of the hole in the middle, default white
        pieChart.setHoleColor(Color.parseColor("#2AD181"));
    }

    public class PercentageValueFormatter extends ValueFormatter {

        private DecimalFormat mFormat;

        public PercentageValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value) {
            String string = mFormat.format(value) + " %";
            return string;
        }
    }

    private void showPieChart(PieChart pieChart,View view)
    {

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "type";

        InsertCSV insertCSV = new InsertCSV(context);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        String chosen_date = this.date_pref.getString("chosen_date",formatter.format(new Date()));

        Float item_value[] = insertCSV.get_home_pie_values(chosen_date);

        //initializing data
        HashMap<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Other",Math.round(item_value[3]));
        typeAmountMap.put("Fat",Math.round(item_value[2]));
        typeAmountMap.put("Protein",Math.round(item_value[1]));
        typeAmountMap.put("Carbs",Math.round(item_value[0]));

        TextView carbs_value_text = view.findViewById(R.id.carbs_value_indicator);
        TextView protein_value_text = view.findViewById(R.id.protein_value_indicator);
        TextView fat_value_text = view.findViewById(R.id.fat_value_indicator);
        TextView other_value_text = view.findViewById(R.id.other_value_indicator);

        SharedPreferences login_pref = context.getSharedPreferences("Login",0);

        String carbs_goal = login_pref.getFloat("Goal_Carbs",100)+"";
        String protein_goal = login_pref.getFloat("Goal_Protein",100)+"";
        String fat_goal = login_pref.getFloat("Goal_Fat",100)+"";

        carbs_value_text.setText(typeAmountMap.get("Carbs")+"/"+carbs_goal+" g");
        protein_value_text.setText(typeAmountMap.get("Protein")+"/"+protein_goal+" g");
        fat_value_text.setText(typeAmountMap.get("Fat")+"/"+fat_goal+" g");
        other_value_text.setText(typeAmountMap.get("Other")+" g");


        Integer color_arr[] = new Integer[]{Color.parseColor("#E1AD01"),Color.parseColor("#EC830E"),Color.parseColor("#30D5C8"),Color.parseColor("#FFE74C")};

        String keys[] = {"Carbs","Protein","Fat","Other"};

        //input data and fit data into pie chart entry
        for(String type: keys){
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setValueTextSize(Color.parseColor("#FFFFFF"));
        pieDataSet.setValueFormatter(new PercentageValueFormatter());
        //providing color list for coloring different entries
        pieDataSet.setColors(Arrays.asList(color_arr));
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);

        pieChart.setData(pieData);

        pieChart.invalidate();

        pieChart.getLegend().setEnabled(false);



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_1_fragment, container, false);

        PieChart pieChart = view.findViewById(R.id.home_pie_fragment);

        this.date_pref = context.getSharedPreferences("date",0);

        initPieChart(pieChart);
        showPieChart(pieChart,view);

        return view;
    }
}