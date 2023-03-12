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
import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Inner_Chart_Month_Fragment extends Fragment
{
    private Context context;
    private LineChart calorie_lineChart;
    // variable for our bar data set.
    private LineDataSet calorie_linDataSet;
    private LineDataSet water_barDataset;

    // array list for storing entries.
    private ArrayList calorie_barEntry;
    private ArrayList<Entry> water_barEntry;
    private LineChart water_barChart;
    private SharedPreferences date_pref;
    private SimpleDateFormat month_formatter;
    private Date current_month_object;
    private int total_month_days;
    private String current_month;

    public Inner_Chart_Month_Fragment(Context context)
    {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inner_month_chart_fragment_layout, container, false);

        Chart_Month_Fragment chart_month_fragment = (Chart_Month_Fragment) getParentFragment();

        this.current_month =  chart_month_fragment.get_today_month();

        init_calorie_barChart(view);

        init_water_barChart(view);

        PieChart pieChart = view.findViewById(R.id.month_macro_pie);

        this.date_pref = context.getSharedPreferences("date",0);

        initPieChart(pieChart);
        showPieChart(pieChart,view);

        TabLayout tabLayout = view.findViewById(R.id.month_chart_tab_layout);
        ViewPager viewPager = view.findViewById(R.id.month_chart_view_pager);

        Month_Macro_Adapter month_macro_adapter = new Month_Macro_Adapter(getChildFragmentManager(),context);
        viewPager.setAdapter(month_macro_adapter);

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
        calorie_lineChart = view.findViewById(R.id.month_calorie_bar);

        Chart_Month_Fragment chart_month_fragment = (Chart_Month_Fragment) this.getParentFragment();

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

//        String[] x_axis_values = getpastdays(past7days_strings);

        Float cal_values[] = insertCSV.get_month_calories(current_month);

        String[] x_axis_values = new String[this.total_month_days];


        for(int i=1;i<=this.total_month_days;i++)
        {
            x_axis_values[i-1] = i+"";
        }

        // creating a new bar data set.
        calorie_linDataSet = new LineDataSet(getCalorieBarEntry(cal_values), "Calories");
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

        calorie_lineChart.getXAxis().setAxisMinimum(0);

        calorie_lineChart.animateY(2000);

        calorie_lineChart.getLegend().setTextColor(Color.parseColor("#000000"));

        calorie_lineChart.getDescription().setTextColor(Color.parseColor("#000000"));

        TextView average_intake = view.findViewById(R.id.month_calorie_average_text);

        Float sum = 0f;

        for(Float i: cal_values)
        {
            sum+= i;
        }

        Float average = sum/cal_values.length;

        String formattedValue = String.format("%.2f", average);

        average_intake.setText("Average Daily Intake: "+formattedValue+"kcal");
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

    private void init_water_barChart(View view)
    {
        water_barChart = view.findViewById(R.id.month_water_bar);

        Chart_Month_Fragment chart_month_fragment = (Chart_Month_Fragment) this.getParentFragment();

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


        Integer water_values[] = get_past_water_values();

        String[] x_axis_values = new String[this.total_month_days];

        for(int i=1;i<=this.total_month_days;i++)
        {
            x_axis_values[i-1] = i+"";
        }

        // creating a new bar data set.
        water_barDataset = new LineDataSet(getWaterBarEntry(water_values), "Water");
        water_barDataset.setColor(context.getResources().getColor(R.color.bar_chart_yellow));

        LineData data = new LineData(water_barDataset);

        water_barChart.setData(data);

        water_barChart.getDescription().setEnabled(false);
        water_barChart.setTouchEnabled(false);
        water_barChart.setDragEnabled(true);
        water_barChart.setScaleEnabled(true);
        water_barChart.setPinchZoom(true);

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

        String calgoals  = pref.getFloat("Goal_Water",0)+"";

        Float cal_float =  0f;

        if(calgoals == "")
        {
            calgoals="0";
        }

        cal_float = Float.parseFloat(calgoals);

        LimitLine limitLine = new LimitLine(cal_float);
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(limitLine);

        water_barChart.getXAxis().setAxisMinimum(0);

        water_barChart.animateY(2000);

        water_barChart.getLegend().setTextColor(Color.parseColor("#000000"));

        water_barChart.getDescription().setTextColor(Color.parseColor("#000000"));

        TextView average_intake = view.findViewById(R.id.month_water_average_text);

        Float sum = 0f;

        for(Integer i: water_values)
        {
            sum+= i;
        }

        Float average = sum/water_values.length;

//        Toast.makeText(context,water_values[11]+"",Toast.LENGTH_LONG).show();

        String formattedValue = String.format("%.2f", average);

        average_intake.setText("Average Daily Intake: "+formattedValue+"ml");
    }

    private Integer[] get_past_water_values()
    {
        SharedPreferences water_log = context.getSharedPreferences("water_log",0);

        Integer water_values[] = new Integer[this.total_month_days];

        for(int i=0;i<this.total_month_days;i++)
        {
            water_values[i] = 0;
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(this.current_month_object);

        int monthid = calendar.get(Calendar.MONTH);
        int yearid = calendar.get(Calendar.YEAR);

        calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,monthid);
        calendar.set(Calendar.YEAR,yearid);

        int maxdays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        SimpleDateFormat normal_formatter = new SimpleDateFormat("dd/MM/yyyy");

        for(int i=1;i<=maxdays;i++)
        {
            calendar.set(Calendar.DAY_OF_MONTH,i);
            Date date = calendar.getTime();

            String today_date = normal_formatter.format(date);

           water_values[i-1] += water_log.getInt(today_date,0);

        }

        return water_values;

    }


    private ArrayList<Entry> getWaterBarEntry(Integer[] water_values)
    {
        // creating a new array list
            water_barEntry = new ArrayList<>();

        for(int i=1;i<=this.total_month_days;i++)
        {
            water_barEntry.add(new Entry(i+1, water_values[i-1]));
        }

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

        Float item_value[] = insertCSV.get_month_pie_values(this.current_month);

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
        pieDataSet.setValueFormatter(new Inner_Chart_Month_Fragment.PercentageValueFormatter());
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
