package com.example.calorietracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
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
import java.util.Map;


public class Inner_Chart_Year_Fragment extends Fragment
{
    private Context context;
    private BarChart calorie_barChart;
    private SharedPreferences date_pref;
    private String[] months = new String[]{"","Jan","Feb","Mar","Apr","May", "Jun", "Jul","Aug","Sep","Oct","Nov","Dec"};
    // variable for our bar data set.
    private BarDataSet calorie_barDataset;
    private BarDataSet water_barDataset;
    private String this_year;

    // array list for storing entries.
    private ArrayList calorie_barEntry;
    private ArrayList<BarEntry> water_barEntry;
    private BarChart water_barChart;

    public Inner_Chart_Year_Fragment(Context context)
    {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inner_year_chart_fragment_layout, container, false);

        Chart_Year_Fragment chart_year_fragment = (Chart_Year_Fragment) getParentFragment();

        this.this_year = chart_year_fragment.get_year();

        init_calorie_barChart(view);

        init_water_barChart(view);

        PieChart pieChart = view.findViewById(R.id.year_macro_pie);

        this.date_pref = context.getSharedPreferences("date",0);

        initPieChart(pieChart);
        showPieChart(pieChart,view);

        TabLayout tabLayout = view.findViewById(R.id.year_chart_tab_layout);
        ViewPager viewPager = view.findViewById(R.id.year_chart_view_pager);

        Year_Macro_Adapter year_macro_adapter = new Year_Macro_Adapter(getChildFragmentManager(),context);
        viewPager.setAdapter(year_macro_adapter);

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
        calorie_barChart = view.findViewById(R.id.year_calorie_bar);

        InsertCSV insertCSV = new InsertCSV(context);

        Float cal_values[] = insertCSV.get_year_calories(this_year);

        // creating a new bar data set.
        calorie_barDataset = new BarDataSet(getCalorieBarEntry(cal_values), "Calories");
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

        TextView average_intake = view.findViewById(R.id.year_calorie_average_text);

        Float sum = 0f;

        for(Float i: cal_values)
        {
            sum+= i;
        }

        Float average = sum/cal_values.length;

        String formattedValue = String.format("%.2f", average);

        average_intake.setText("Average Monthly Intake: "+formattedValue+"kcal");
    }

    private void init_water_barChart(View view)
    {
        water_barChart = view.findViewById(R.id.year_water_bar);

        InsertCSV insertCSV = new InsertCSV(context);

        Integer water_value[] = get_past_water_values();

        // creating a new bar data set.
        water_barDataset = new BarDataSet(getWaterBarEntry(water_value), "Water");
        water_barDataset.setColor(context.getResources().getColor(R.color.bar_chart_yellow));

        BarData data = new BarData(water_barDataset);
        data.setBarWidth(0.1f);
        water_barChart.setData(data);

        water_barChart.getDescription().setEnabled(false);
        water_barChart.setTouchEnabled(false);

        // below line is to get x axis
        // of our bar chart.
        XAxis xAxis = water_barChart.getXAxis();

        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(13);
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
        leftAxis.removeAllLimitLines();

        data.setBarWidth(0.75f);

        water_barChart.getXAxis().setAxisMinimum(0);

        water_barChart.animateY(2000);

        water_barChart.getLegend().setTextColor(Color.parseColor("#000000"));

        water_barChart.getDescription().setTextColor(Color.parseColor("#000000"));

        TextView average_intake = view.findViewById(R.id.year_water_average_text);

        Float sum = 0f;

        for(Integer i: water_value)
        {
            sum+= i;
        }

        Float average = sum/water_value.length;

        String formattedValue = String.format("%.2f", average);

        average_intake.setText("Average Monthly Intake: "+formattedValue+"ml");
    }

    private Integer[] get_past_water_values()
    {
        SharedPreferences water_log = context.getSharedPreferences("water_log",0);

        Map<String,?> water_map = water_log.getAll();

        Integer water_values[] = new Integer[13];

        for(int i=0;i<13;i++)
        {
            water_values[i] = 0;
        }

        Integer x = 0;

        int i = 0;

        Date date = new Date();
        Date today_date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        for (Map.Entry<String, ?> entry : water_map.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();

            try {
                today_date = formatter.parse(key);
            }catch (Exception e){today_date =new Date();}

            Calendar calendar = Calendar.getInstance();

            calendar.setTime(today_date);

            String year_val = calendar.get(Calendar.YEAR)+"";

            if(year_val.equals(this_year)) {
                int monthid = calendar.get(Calendar.MONTH);
                water_values[monthid] += water_log.getInt(key, 0);
            }

        }

        return water_values;
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

    private ArrayList<BarEntry> getWaterBarEntry(Integer[] water_values)
    {
        // creating a new array list
        this.water_barEntry = new ArrayList<>();

//        String data = "";

        for(int i=0;i<12;i++)
        {
            water_barEntry.add(new BarEntry(i+1, water_values[i]));
//            data+= water_values[i]+" ";
        }

//        Toast.makeText(context,data,Toast.LENGTH_LONG).show();

        return water_barEntry;
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

        Float item_value[] = insertCSV.get_year_pie_values(this_year);

        //initializing data
        HashMap<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Other",Math.round(item_value[3]));
        typeAmountMap.put("Fat",Math.round(item_value[2]));
        typeAmountMap.put("Protein",Math.round(item_value[1]));
        typeAmountMap.put("Carbs",Math.round(item_value[0]));

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
        pieDataSet.setValueFormatter(new Inner_Chart_Year_Fragment.PercentageValueFormatter());
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
