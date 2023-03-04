//package com.example.calorietracker;
//
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.NumberPicker;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.button.MaterialButton;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class GoalSettingActivity extends AppCompatActivity
//{
//    private float calorie_val = 2000f;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.goal_setting_activity);
//
//        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.calorie_picker);
//
//        String numval = "0";
//
//        SharedPreferences pref = getApplication().getSharedPreferences("Goals",0);
//
//        SharedPreferences.Editor editor = pref.edit();
//
//        String goal = pref.getString("Goal_Calorie","");
//        Float parse = 0f;
//        if(goal.equals(""))
//        {
//            parse = 0f;
//        }
//        else {
//            parse = Float.parseFloat(goal);
//            Toast.makeText(getApplicationContext(),Math.round(parse)+"",Toast.LENGTH_LONG).show();
//        }
//
//        numberPicker.setMinValue(5);
//        numberPicker.setMaxValue(50);
//        numberPicker.setValue(Math.round(parse/100f));
//        ImageButton back_btn = findViewById(R.id.display_back_btn);
//
//        back_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
//            @Override
//            public String format(int value) {
//                int diff = value*100;
//                return "" + diff;
//            }
//        };
//        numberPicker.setFormatter(formatter);
//
//        InsertCSV insertCsv = new InsertCSV(this);
//
//        Date date = new Date();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//        String date_string = simpleDateFormat.format(date);
//
//        float current_calories = insertCsv.get_day_calorie(date_string);
//
//        String current_cals = pref.getString("today_cals","0");
//
//        TextView current_calorie = findViewById(R.id.today_calorie);
//
//        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                calorie_val = newVal*100;
//                editor.putString("Goal_Calorie",calorie_val+"");
//                editor.commit();
//                Float val = calorie_val - Float.parseFloat(current_cals);
//                if(val<0)
//                {
//                    val = 0f;
//                }
//                try {
//                    current_calorie.setText("Calories to Consume to Reach Today's Goal: "+ val+ " Calories");
//                }
//                catch(Exception e)
//                {
//                    current_calorie.setText("Calories to Consume to Reach Today's Goal: "+ (calorie_val)+ " Calories");
//                }
//
//            }
//        });
//        editor.commit();
//
//        try {
//            Float val = calorie_val - Float.parseFloat(current_cals);
//            if(val<0)
//            {
//                val = 0f;
//            }
//            current_calorie.setText(current_calorie.getText().toString() + " " + val + " Calories");
//        }
//        catch(Exception e)
//        {
//            current_calorie.setText(current_calorie.getText().toString() + " " + (calorie_val)+ " Calories");
//        }
//
//    }
//}