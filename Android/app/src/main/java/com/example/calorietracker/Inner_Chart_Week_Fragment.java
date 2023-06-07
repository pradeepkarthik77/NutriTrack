package com.example.calorietracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Inner_Chart_Week_Fragment extends Fragment
{
    private Context context;
    private BarChart calorie_barChart;
    private SharedPreferences date_pref;
    private String[] days = new String[]{"Sun","Mon","Tue","Wed","Thu", "Fri", "Sat"};
    // variable for our bar data set.
    private BarDataSet calorie_barDataset;
    private BarDataSet water_barDataset;

    // array list for storing entries.
    private ArrayList calorie_barEntry;
    private ArrayList<BarEntry> water_barEntry;
    private BarChart water_barChart;

    public Inner_Chart_Week_Fragment(Context context)
    {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inner_week_chart_fragment_layout, container, false);

        init_calorie_barChart(view);

        init_water_barChart(view);

        PieChart pieChart = view.findViewById(R.id.week_macro_pie);

        this.date_pref = context.getSharedPreferences("date",0);

        initPieChart(pieChart);
        showPieChart(pieChart,view);

        TabLayout tabLayout = view.findViewById(R.id.week_chart_tab_layout);
        ViewPager viewPager = view.findViewById(R.id.week_chart_view_pager);

        Week_Macro_Adapter week_macro_adapter = new Week_Macro_Adapter(getChildFragmentManager(),context);
        viewPager.setAdapter(week_macro_adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Update the ViewPager when a tab is selected
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void init_calorie_barChart(View view)
    {
        calorie_barChart = view.findViewById(R.id.week_calorie_bar);

        Chart_Week_Fragment chart_week_fragment = (Chart_Week_Fragment) this.getParentFragment();

        ArrayList<String> past7dates =  chart_week_fragment.get_dates_range();

        InsertCSV insertCSV = new InsertCSV(context);

        String[] past7days_strings = past7dates.toArray(new String[past7dates.size()]);

        String[] x_axis_values = getpastdays(past7days_strings);

        Float cal_values[] = insertCSV.get_past7_days(past7days_strings);

        // creating a new bar data set.
        calorie_barDataset = new BarDataSet(getCalorieBarEntry(cal_values), "Calories");
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

        calorie_barChart.getXAxis().setAxisMinimum(0);

        calorie_barChart.animateY(2000);

        calorie_barChart.getLegend().setTextColor(Color.parseColor("#000000"));

        calorie_barChart.getDescription().setTextColor(Color.parseColor("#000000"));

        TextView average_intake = view.findViewById(R.id.calorie_average_text);

        Float sum = 0f;

        for(Float i: cal_values)
        {
            sum+= i;
        }

        Float average = sum/cal_values.length;

        String formattedValue = String.format("%.2f", average);

        average_intake.setText("Average Daily Intake: "+formattedValue+"kcal");
    }

    private void init_water_barChart(View view)
    {
        this.water_barChart = view.findViewById(R.id.week_water_bar);

        Chart_Week_Fragment chart_week_fragment = (Chart_Week_Fragment) this.getParentFragment();

        ArrayList<String> past7dates =  chart_week_fragment.get_dates_range();

        String[] past7days_strings = past7dates.toArray(new String[past7dates.size()]);

        String[] x_axis_values = getpastdays(past7days_strings);

//        Float cal_values[] = insertCSV.get_past7_days(past7days_strings);

        Integer water_values[] = get_past_water_values(past7days_strings);


        // creating a new bar data set.
        water_barDataset = new BarDataSet(getWaterBarEntry(water_values), "Water Intake");
        water_barDataset.setColor(context.getResources().getColor(R.color.bar_chart_yellow));

        BarData data = new BarData(water_barDataset);

        water_barChart.setData(data);

        water_barChart.getDescription().setEnabled(false);
        water_barChart.setTouchEnabled(false);

        // below line is to get x axis
        // of our bar chart.
        XAxis xAxis = water_barChart.getXAxis();

        xAxis.setValueFormatter(new IndexAxisValueFormatter(x_axis_values));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.parseColor("#000000"));

        YAxis leftAxis = water_barChart.getAxisLeft();
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.parseColor("#000000"));

        YAxis rightAxis = water_barChart.getAxisRight();
        rightAxis.setDrawAxisLine(false);
        rightAxis.setEnabled(false);

        water_barChart.setDragEnabled(true);

        water_barChart.setDrawGridBackground(false);

        SharedPreferences pref = context.getSharedPreferences("Login",0);

        String water_goal  = pref.getFloat("Goal_Water",0)+"";

        Float water_float =  0f;

        if(water_goal == "")
        {
            water_goal="0";
        }

        water_float = Float.parseFloat(water_goal);

        LimitLine limitLine = new LimitLine(water_float);
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(limitLine);

        data.setBarWidth(0.75f);

        water_barChart.getXAxis().setAxisMinimum(0);

        water_barChart.animateY(2000);

        water_barChart.getLegend().setTextColor(Color.parseColor("#000000"));

        water_barChart.getDescription().setTextColor(Color.parseColor("#000000"));

        TextView average_intake = view.findViewById(R.id.week_water_average_text);

        Float sum = 0f;

        for(Integer i: water_values)
        {
            sum+= i;
        }

        Float average = sum/water_values.length;

        String formattedValue = String.format("%.2f", average);

        average_intake.setText("Average Daily Intake: "+formattedValue+"ml");
    }

    private Integer[] get_past_water_values(String[] past7days_strings)
    {
        SharedPreferences water_log = context.getSharedPreferences("water_log",0);

        Integer water_values[] = new Integer[]{0,0,0,0,0,0,0};

        Integer x = 0;

        int i = 0;

        for(String date: past7days_strings)
        {
            x = water_log.getInt(date,0);
            water_values[i++] = x;
        }

        return water_values;

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

    private ArrayList<BarEntry> getWaterBarEntry(Integer[] water_values)
    {
        // creating a new array list
        this.water_barEntry = new ArrayList<>();

        for(int i=0;i<7;i++)
        {
            water_barEntry.add(new BarEntry(i+1, water_values[i]));
        }

        return water_barEntry;
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
        pieChart.animateY(1400, Easing.EaseInOutQuad);
        //setting the color of the hole in the middle, default white
        pieChart.setHoleColor(Color.parseColor("#FFFFFF"));
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

        Chart_Week_Fragment chart_week_fragment = (Chart_Week_Fragment) this.getParentFragment();

        ArrayList<String> dates_arraylist = chart_week_fragment.get_dates_range();

        String date_range[] = dates_arraylist.toArray(new String[dates_arraylist.size()]);

        HashMap<String,Integer> dates_map = new HashMap<String,Integer>();

        for(String dates: date_range)
        {
            dates_map.put(dates,1);
        }

        Float item_value[] = insertCSV.get_week_pie_values(dates_map);

        //initializing data
        HashMap<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Other",Math.round(item_value[3]));
        typeAmountMap.put("Fat",Math.round(item_value[2]));
        typeAmountMap.put("Protein",Math.round(item_value[1]));
        typeAmountMap.put("Carbs",Math.round(item_value[0]));
//
//        TextView carbs_value_text = view.findViewById(R.id.carbs_value_indicator);
//        TextView protein_value_text = view.findViewById(R.id.protein_value_indicator);
//        TextView fat_value_text = view.findViewById(R.id.fat_value_indicator);
//        TextView other_value_text = view.findViewById(R.id.other_value_indicator);
//
//        SharedPreferences login_pref = context.getSharedPreferences("Login",0);
//
//        String carbs_goal = login_pref.getFloat("Goal_Carbs",100)+"";
//        String protein_goal = login_pref.getFloat("Goal_Protein",100)+"";
//        String fat_goal = login_pref.getFloat("Goal_Fat",100)+"";
//
//        carbs_value_text.setText(typeAmountMap.get("Carbs")+"/"+carbs_goal+" g");
//        protein_value_text.setText(typeAmountMap.get("Protein")+"/"+protein_goal+" g");
//        fat_value_text.setText(typeAmountMap.get("Fat")+"/"+fat_goal+" g");
//        other_value_text.setText(typeAmountMap.get("Other")+" g");


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
        pieDataSet.setValueFormatter(new Inner_Chart_Week_Fragment.PercentageValueFormatter());
        //providing color list for coloring different entries
        pieDataSet.setColors(Arrays.asList(color_arr));
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);

        if(item_value[0] == 0f && item_value[1] == 0f && item_value[2] == 0f && item_value[3] == 0f)
        {
            pieChart.setNoDataText("No Data Avaiable");
        }
        else
        {
            pieChart.setData(pieData);
        }

        pieChart.invalidate();

        pieChart.getLegend().setEnabled(false);

    }


}
