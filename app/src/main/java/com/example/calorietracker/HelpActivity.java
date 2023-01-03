package com.example.calorietracker;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HelpActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helpactivity_layout);

        TextView txtview = findViewById(R.id.help_content);

        txtview.setText("Welcome to our 24/7 Dietery Assesment App which helps you keep track of your health by tracking your nutritional diet.\n\nUse our Assess Meal module to enter your daily food intake into our app!\n\nHaving Doubts in User Profile? Profile is here to help you out!!!\n\nDid you forget to log in the data for previous days? Use Notice Board to log previous days!!!\n\nAre you a irregular foodie? Mid-day meal is here to help you with your Brunching activities!!!\n\nDo you want to set and acheive Goals? Goal Setting is here!!!\n\nDo you want visualized charts of your data? head on to REports!!!");

        ImageButton imgbtn = findViewById(R.id.display_back_btn);

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
