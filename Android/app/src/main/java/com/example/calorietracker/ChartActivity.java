package com.example.calorietracker;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ChartActivity extends AppCompatActivity {

        // variable for our bar chart
        private BarChart barChart;

        // variable for our bar data set.
        private BarDataSet barDataSet1, barDataSet2;

        // array list for storing entries.
        private ArrayList barEntries;

    // creating a string array for displaying days.
    //private String[] days = new String[]{"Sunday","Monday", "Tuesday","Wednesday","Thursday", "Friday", "Saturday"};

    private String[] days = new String[]{"Sun","Mon", "Tues","Wed","Thurs", "Fri", "Sat"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chartactivity_layout);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        TextView list_title = (TextView) findViewById(R.id.display_title);

        ImageButton imgbtn = findViewById(R.id.display_back_btn);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list_title.setText("Reports");

        // initializing variable for bar chart.
        barChart = findViewById(R.id.barchart);

        String getpast7date[] = getpastdates();

        InsertCSV insertCSV = new InsertCSV(this);

        String[] pastdays = getpastdays(getpast7date);

        Float[] cal_values = insertCSV.get_past7_days(getpast7date);

        //Toast.makeText(getApplicationContext(),String.join(",",pastdays),Toast.LENGTH_LONG).show();

        //Toast.makeText(getApplicationContext(),cal_values[0]+" "+cal_values[1]+" "+cal_values[2],Toast.LENGTH_LONG).show();

        // creating a new bar data set.
        barDataSet1 = new BarDataSet(getBarEntriesOne(cal_values), "Calories");
        barDataSet1.setColor(getApplicationContext().getResources().getColor(R.color.teal_200));

        // below line is to add bar data set to our bar data.
        BarData data = new BarData(barDataSet1);

        // after adding data to our bar data we
        // are setting that data to our bar chart.
        barChart.setData(data);

        // below line is to remove description
        // label of our bar chart.
        barChart.getDescription().setEnabled(false);

        // below line is to get x axis
        // of our bar chart.
        XAxis xAxis = barChart.getXAxis();

        // below line is to set value formatter to our x-axis and
        // we are adding our days to our x axis.
        xAxis.setValueFormatter(new IndexAxisValueFormatter(pastdays));

        // below line is to set center axis
        // labels to our bar chart.
        //xAxis.setCenterAxisLabels(true);

        // below line is to set position
        // to our x-axis to bottom.
        //xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // below line is to set granularity
        // to our x axis labels.
       // xAxis.setGranularity(1);

        // below line is to enable
        // granularity to our x axis.
        //xAxis.setGranularityEnabled(true);
        //change the position of x-axis to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //set the horizontal distance of the grid line
        //xAxis.setGranularity(1f);
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false);
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = barChart.getAxisRight();
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false);

        // below line is to make our
        // bar chart as draggable.
        barChart.setDragEnabled(true);

        barChart.setDrawGridBackground(false);

        SharedPreferences pref = getSharedPreferences("Goals",0);

        String calgoals  = pref.getString("calorie_goals","");

        Float cal_float =  0f;

        if(calgoals == "")
        {
            calgoals="0";
        }

        cal_float = Float.parseFloat(calgoals);

        LimitLine limitLine = new LimitLine(cal_float);
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(limitLine);

        // below line is to make visible
        // range for our bar chart.
        //barChart.setVisibleXRangeMaximum(3);

        // below line is to add bar
        // space to our chart.
        //float barSpace = 0.1f;

        // below line is use to add group
        // spacing to our bar chart.
        //float groupSpace = 0.5f;

        // we are setting width of
        // bar in below line.
        data.setBarWidth(0.75f);

        // below line is to set minimum
        // axis to our chart.
        barChart.getXAxis().setAxisMinimum(0);

        // below line is to
        // animate our chart.
        barChart.animateY(2000);


        // below line is to group bars
        // and add spacing to it.
        //barChart.groupBars(0, groupSpace, barSpace);

        // below line is to invalidate
        // our bar chart.
        //barChart.invalidate();
    }

    // array list for first set
    private ArrayList<BarEntry> getBarEntriesOne(Float[] calorie_values)
    {
        // creating a new array list
        barEntries = new ArrayList<>();
        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
//        barEntries.add(new BarEntry(1f, 4));
//        barEntries.add(new BarEntry(2f, 6));
//        barEntries.add(new BarEntry(3f, 8));
//        barEntries.add(new BarEntry(4f, 2));
//        barEntries.add(new BarEntry(5f, 4));
//        barEntries.add(new BarEntry(6f, 1));
//        return barEntries;

        for(int i=0;i<7;i++)
        {
            barEntries.add(new BarEntry(i+1, calorie_values[i]));
        }

        return barEntries;
    }

    private String[] getpastdates()
    {
        Date date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        String current_date = formatter.format(date);

        String past_days[] = new String[]{"Monday", "Tuesday","Wednesday", "Thursday", "Friday", "Saturday","Sunday"};

        for(int i=0;i<7;i++)
        {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -i);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String item_date_string = simpleDateFormat.format(c.getTime());
            past_days[i] = item_date_string;
        }

        return past_days;
    }

    private String[] getpastdays(String[] past7dates)
    {
        Date date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        String current_date = formatter.format(date);

        String past_days[] = new String[]{"","Sunday","Monday", "Tuesday","Wednesday", "Thursday", "Friday", "Saturday"};

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());

        int current_day_indx = c.get(Calendar.DAY_OF_WEEK);

        for(int i=0;i<7;i++)
        {
            c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -i);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            int x = c.get(Calendar.DAY_OF_WEEK);
            past_days[i+1] = days[(x-1)%7];
        }
        return past_days;
    }

}
