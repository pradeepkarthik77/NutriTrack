package com.example.calorietracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity
{
    private ImageView logo;
    private static int splashTimeOut = 5000;
    private TextView splashtext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        logo=(ImageView)findViewById(R.id.splash_logo);

        splashtext = findViewById(R.id.splash_title);

        Spannable wordtoSpan = new SpannableString("NutriTrack");

        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_green)), 5, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        splashtext.setText(wordtoSpan);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,OnOpenActivity.class);
                startActivity(i);
                finish();
            }
        },splashTimeOut);

        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.splash_anim);
        logo.startAnimation(myanim);
        splashtext.startAnimation(myanim);
    }
}
