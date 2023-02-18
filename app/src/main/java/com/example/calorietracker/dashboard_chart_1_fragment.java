package com.example.calorietracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class dashboard_chart_1_fragment extends Fragment {

    private Context context;
    private RadioButton male_btn;
    private RadioButton female_btn;
    private RadioGroup radioGroup;
    private NumberPicker age_picker;
    private TextView age_text;
    private TextView gender_text;
    private TextView height_text;


    public dashboard_chart_1_fragment(Context context)
    {
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_1_fragment, container, false);

        return view;
    }
}