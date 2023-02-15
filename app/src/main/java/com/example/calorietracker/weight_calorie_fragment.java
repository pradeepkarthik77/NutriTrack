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

public class weight_calorie_fragment extends Fragment {

    private Context context;
    private NumberPicker weight_picker;
    private TextView age_text;
    private TextView gender_text;
    private TextView height_text;


    public weight_calorie_fragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weight_calorie_layout, container, false);

        SharedPreferences pref = context.getSharedPreferences("Login",0);

        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("Weight",60);
        editor.putInt("Activity_Level",0);
        editor.commit();

        this.weight_picker = view.findViewById(R.id.weight_picker);
        this.age_text = view.findViewById(R.id.weight_text);

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        int bulletcolor = Color.parseColor("#228B22");

        ssb.clear();
        ssb.append("Choose Your Weight:");

        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        this.age_text.setText(ssb);

        this.weight_picker.setMaxValue(500);
        this.weight_picker.setMinValue(1);
        this.weight_picker.setValue(60);

        NumberPicker weight_picker = view.findViewById(R.id.weight_picker);

        weight_picker.setMaxValue(300);
        weight_picker.setMinValue(0);
        weight_picker.setValue(50);

        ssb.clear();

        Spinner spinner = view.findViewById(R.id.spinner);
        List<String> options = Arrays.asList("Sedentary", "Lightly active", "Moderately active","Very active","Extra Active");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editor.putInt("Activity_Level",position);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.weight_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                editor.putInt("Weight",newVal);
                editor.commit();
            }
        });

        TextView lifestyle_text = view.findViewById(R.id.lifestyle_text);

        ssb.clear();
        ssb.append("Choose Your Activity Level:");

        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,5,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        lifestyle_text.setText(ssb);

        editor.commit();
        return view;
    }
}