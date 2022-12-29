package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.TaskStackBuilder;

import com.google.android.material.button.MaterialButton;

public class OnOpenActivity extends AppCompatActivity
{

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onopen_layout);

        ImageView imageView = findViewById(R.id.onopen_image);

        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        imageView.getLayoutParams().height = (int)(((double)height)/ 1);//1.75);
        imageView.setAlpha(0.55f);

        TextView textView = findViewById(R.id.welcome_calorie);

        String string = textView.getText().toString();

        SpannableString string1 = new SpannableString(string);
        string1.setSpan(new UnderlineSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(string1);

        MaterialButton log_in_btn = findViewById(R.id.log_in_btn);

        this.context = this;

        log_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,LoginActivity.class);
                context.startActivity(intent);
            }
        });

        MaterialButton signup_btn  =  findViewById(R.id.sign_in_btn);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context,SignupActivity.class);
                context.startActivity(intent1);
            }
        });
    }
}
