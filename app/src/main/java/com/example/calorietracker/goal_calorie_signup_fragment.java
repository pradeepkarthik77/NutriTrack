package com.example.calorietracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class goal_calorie_signup_fragment extends Fragment {

    private Context context;
    private NumberPicker calorie_picker;

    private String Gender = "Male";
    private int Weight = 60;
    private int Height = 150;
    private int Activity_Level = 0;
    private int Age = 30;
    private double bmr = 0.0f;
    private float tdee = 0f;

    private double recommended = 0.0f;

    public goal_calorie_signup_fragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goal_calorie_signup_layout, container, false);

        this.calorie_picker = view.findViewById(R.id.cal_pick);

        SharedPreferences pref = context.getSharedPreferences("Login",0);

        SharedPreferences.Editor editor = pref.edit();

        this.Gender = pref.getString("Gender","Male");
        this.Age = pref.getInt("Age",30);
        this.Weight = pref.getInt("Weight",60);
        this.Height = pref.getInt("Height",150);
        this.Activity_Level = pref.getInt("Activity_Level",0);

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        int bulletcolor = Color.parseColor("#228B22");

        ssb.clear();
        ssb.append("Your Daily Calorie Intake Goal:");

        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView cal_text = view.findViewById(R.id.cal_text);

        cal_text.setText(ssb);

        if(this.Gender.equals("Male"))
        {
            this.bmr = 88.362+(13.397*this.Weight)+(4.799*this.Height)-(5.677*this.Age);
        }
        else
        {
            this.bmr = 447.593+(9.247*this.Weight)+(3.098*this.Height)-(4.330*this.Age);
        }

        switch(this.Activity_Level)
        {
            case 0: this.tdee = 1.2f;break;
            case 1: this.tdee = 1.375f;break;
            case 2: this.tdee = 1.55f;break;
            case 3: this.tdee = 1.725f;break;
            case 4: this.tdee = 1.9f;break;
        }

        this.recommended = this.bmr*this.tdee;

        editor.putInt("Goal_Calorie",(int)Math.round(this.recommended));
        editor.commit();

        this.calorie_picker.setMaxValue(10000);
        this.calorie_picker.setMinValue(0);

        this.calorie_picker.setValue((int)Math.round(this.recommended));

        this.calorie_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                editor.putInt("Goal_Calorie",newVal);
                editor.commit();
            }
        });

//        Toast.makeText(context,this.Gender+" "+this.Age+" "+this.Height+" "+this.Weight+" "+this.Activity_Level,Toast.LENGTH_LONG).show();

        return view;
    }
}