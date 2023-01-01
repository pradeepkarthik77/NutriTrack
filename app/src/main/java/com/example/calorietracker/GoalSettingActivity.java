package com.example.calorietracker;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class GoalSettingActivity extends AppCompatActivity
{
    private float calorie_val = 2000f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_setting_activity);

        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.calorie_picker);

        String numval = "0";

        numberPicker.setMinValue(5);
        numberPicker.setMaxValue(50);
        numberPicker.setValue(20);
        ImageButton back_btn = findViewById(R.id.display_back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                int diff = value*100;
                return "" + diff;
            }
        };
        numberPicker.setFormatter(formatter);

        SharedPreferences pref = getApplication().getSharedPreferences("Goals",0);

        SharedPreferences.Editor editor = pref.edit();

        InsertCSV insertCsv = new InsertCSV(this);

        float current_calories = insertCsv.get_today_calorie();

        String current_cals = pref.getString("today_cals","0");

        TextView current_calorie = findViewById(R.id.today_calorie);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                calorie_val = newVal;
                editor.putString("calorie_goals",calorie_val+"");
                try {
                    current_calorie.setText("Calories to Consume to Reach Today's Goal: "+ (calorie_val*100 - Float.parseFloat(current_cals)) + " Calories");
                }
                catch(Exception e)
                {
                    current_calorie.setText("Calories to Consume to Reach Today's Goal: "+ (calorie_val*100)+ " Calories");
                }

            }
        });
        editor.commit();

        try {
            current_calorie.setText(current_calorie.getText().toString() + " " + (calorie_val - Float.parseFloat(current_cals)) + " Calories");
        }
        catch(Exception e)
        {
            current_calorie.setText(current_calorie.getText().toString() + " " + (calorie_val)+ " Calories");
        }

    }
}