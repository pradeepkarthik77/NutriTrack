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
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class gender_age_fragment extends Fragment {

    private Context context;
    private RadioButton male_btn;
    private RadioButton female_btn;
    private RadioGroup radioGroup;
    private NumberPicker age_picker;
    private TextView age_text;
    private TextView gender_text;
    private TextView height_text;


    public gender_age_fragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gender_age_layout, container, false);

        this.male_btn = view.findViewById(R.id.male_gender);
        this.female_btn = view.findViewById(R.id.female_gender);
        this.radioGroup = view.findViewById(R.id.gender_radiogroup);
        this.age_picker = view.findViewById(R.id.weight_picker);
        this.gender_text = view.findViewById(R.id.gender_text);
        this.age_text = view.findViewById(R.id.weight_text);
        this.height_text = view.findViewById(R.id.cal_text);

        this.radioGroup = view.findViewById(R.id.gender_radiogroup);

        SharedPreferences pref = context.getSharedPreferences("Login",0);

        SharedPreferences.Editor editor = pref.edit();

        editor.putString("Gender","Male");
        editor.putInt("Age",30);
        editor.putInt("Height",150);
        editor.commit();

        this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = view.findViewById(checkedId);
                editor.putString("Gender",radioButton.getText().toString());
                editor.commit();
            }
        });

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        ssb.append("Choose Your Gender:");

        int bulletcolor = Color.parseColor("#228B22");

        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        this.gender_text.setText(ssb);

        ssb.clear();
        ssb.append("Choose Your Age:");

        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        this.age_text.setText(ssb);

        this.age_picker.setMaxValue(120);
        this.age_picker.setMinValue(1);
        this.age_picker.setValue(30);

        NumberPicker height_picker = view.findViewById(R.id.height_picker);

        height_picker.setMaxValue(250);
        height_picker.setMinValue(50);
        height_picker.setValue(150);

        ssb.clear();
        ssb.append("Choose Your Height (in cms):");

        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        this.age_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                editor.putInt("Age",newVal);
                editor.commit();
            }
        });

        height_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                editor.putInt("Height",newVal);
                editor.commit();
            }
        });

        this.height_text.setText(ssb);
        editor.commit();

        return view;
    }
}