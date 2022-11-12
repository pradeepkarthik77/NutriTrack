package com.example.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    private MainCardRecycler mainCardRecycler;
    private RecyclerView mainRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mainCardRecycler = new MainCardRecycler(this);
        this.mainRecycler = (RecyclerView) findViewById(R.id.main_recycler);
        this.mainCardRecycler.setAdapterforRecycler(this.mainRecycler);

    }
}