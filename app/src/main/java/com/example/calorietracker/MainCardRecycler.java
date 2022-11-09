package com.example.calorietracker;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainCardRecycler
{
    private String[] cardview_titles;
    private int[] cardview_images;
    private int cardview_count;
    private MainCardRecyclerAdapter mainCardRecyclerAdapter;
    private Context context;

    private RecyclerView recyclerView;

    public MainCardRecycler(Context context)
    {
        this.cardview_titles = new String[]{"BreakFast","Lunch","Dinner","Snacks","Juices","Water"};
        this.cardview_images = new int[]{R.drawable.lunch,R.drawable.breakfast,R.drawable.dinner,R.drawable.snacks};
        this.cardview_count = 4;
        this.context = context;
    }

    public void setAdapterforRecycler(RecyclerView recView)
    {
        this.recyclerView = recView;
        this.mainCardRecyclerAdapter = new MainCardRecyclerAdapter(this.cardview_titles,this.cardview_images,this.cardview_count,this.context);
        this.recyclerView.setAdapter(this.mainCardRecyclerAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.context,2);

        this.recyclerView.setLayoutManager(gridLayoutManager);
    }
}