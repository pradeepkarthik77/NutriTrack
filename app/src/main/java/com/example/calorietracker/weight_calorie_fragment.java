package com.example.calorietracker;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class weight_calorie_fragment extends Fragment {

    private Context context;
    private NumberPicker age_picker;
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

        this.age_picker = view.findViewById(R.id.weight_picker);
        this.age_text = view.findViewById(R.id.weight_text);
        this.height_text = view.findViewById(R.id.cal_text);

        class CustomBulletSpan extends BulletSpan {
            private int color;
            private static final int BULLET_RADIUS = 15;
            private static final int PADDING = BULLET_RADIUS;

            public CustomBulletSpan(int color) {
                this.color = color;
            }

            @Override
            public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
                Paint.Style style = p.getStyle();
                int oldColor = p.getColor();

                p.setColor(color);
                p.setStyle(Paint.Style.FILL);

                c.drawCircle(x + dir * BULLET_RADIUS, (top + bottom) / 2.0f, BULLET_RADIUS, p);

                p.setColor(oldColor);
                p.setStyle(style);
            }

            @Override
            public int getLeadingMargin(boolean first) {
                return BULLET_RADIUS * 2 + PADDING;
            }
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        int bulletcolor = Color.parseColor("#228B22");

        ssb.clear();
        ssb.append("Choose Your Weight:");

        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        this.age_text.setText(ssb);

        this.age_picker.setMaxValue(120);
        this.age_picker.setMinValue(1);
        this.age_picker.setValue(30);

        NumberPicker height_picker = view.findViewById(R.id.cal_number);

        height_picker.setMaxValue(250);
        height_picker.setMinValue(50);
        height_picker.setValue(150);

        ssb.clear();
        ssb.append("Choose Your Target Calorie:");

        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        this.height_text.setText(ssb);

        Spinner spinner = view.findViewById(R.id.spinner);
        List<String> options = Arrays.asList("Sedentary", "Lightly active", "Moderately active","Very active","Extra active");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        TextView lifestyle_text = view.findViewById(R.id.lifestyle_text);

        ssb.clear();
        ssb.append("Choose Your Target Calorie:");

        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        lifestyle_text.setText(ssb);

        return view;
    }
}