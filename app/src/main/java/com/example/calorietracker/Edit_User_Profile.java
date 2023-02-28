package com.example.calorietracker;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Edit_User_Profile extends AppCompatActivity
{

    private SharedPreferences login_pref;
    private String name;
    private String email;
    private String gender;
    private int age;
    private int height;
    private int weight;
    private int activity_level;
    private int goal_calorie;

    protected void setdefaultvalues()
    {
        this.login_pref = getSharedPreferences("Login",0);
        this.name = this.login_pref.getString("name","");
        this.email = this.login_pref.getString("email","");
        this.gender = this.login_pref.getString("Gender","");
        this.height = this.login_pref.getInt("Height",0);
        this.weight = this.login_pref.getInt("Weight",0);
        this.activity_level = this.login_pref.getInt("Activity_Level",0);
        this.goal_calorie = this.login_pref.getInt("Goal_Calorie",0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_edit_profile_layout);

        setdefaultvalues();

        TextView age_text = findViewById(R.id.edit_age_text);
        TextView gender_text = findViewById(R.id.edit_gender_text);
        TextView height_text = findViewById(R.id.edit_height_text);
        TextView weight_text = findViewById(R.id.edit_weight_text);
        TextView activity_level_text = findViewById(R.id.edit_lifestyle_text);
        TextView goal_calorie_text = findViewById(R.id.edit_calorie_text);
        TextView name_text = findViewById(R.id.edit_name_text);

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append("Edit Your Gender:");
        int bulletcolor = Color.parseColor("#228B22");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        gender_text.setText(ssb);

        ssb.clear();
        ssb.append("Edit Your Name:");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        name_text.setText(ssb);


        ssb.clear();
        ssb.append("Edit Your Age:");
        bulletcolor = Color.parseColor("#228B22");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        age_text.setText(ssb);

        ssb.clear();
        ssb.append("Edit Your Height:");
        bulletcolor = Color.parseColor("#228B22");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        height_text.setText(ssb);

        ssb.clear();
        ssb.append("Edit Your Weight:");
        bulletcolor = Color.parseColor("#228B22");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        weight_text.setText(ssb);

        ssb.clear();
        ssb.append("Edit Your Activity Level:");
        bulletcolor = Color.parseColor("#228B22");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        activity_level_text.setText(ssb);

        ssb.clear();
        ssb.append("Edit Your Calorie Intake Goal:");
        bulletcolor = Color.parseColor("#228B22");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        goal_calorie_text.setText(ssb);

        NumberPicker weight_picker = findViewById(R.id.edit_weight_picker);
        NumberPicker height_picker = findViewById(R.id.edit_height_picker);
        NumberPicker age_picker = findViewById(R.id.edit_age_picker);
        NumberPicker calorie_picker = findViewById(R.id.edit_calorie_picker);

        weight_picker.setMaxValue(500);
        weight_picker.setMinValue(1);
        weight_picker.setValue(60);

        height_picker.setMaxValue(250);
        height_picker.setMinValue(50);
        height_picker.setValue(150);

        age_picker.setMaxValue(120);
        age_picker.setMinValue(1);
        age_picker.setValue(30);

        calorie_picker.setMaxValue(10000);
        calorie_picker.setMinValue(0);

        RadioGroup gender_group = findViewById(R.id.gender_radiogroup);
    }
}
