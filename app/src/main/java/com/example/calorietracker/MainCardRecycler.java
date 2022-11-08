package com.example.calorietracker;

import androidx.recyclerview.widget.RecyclerView;

public class MainCardRecycler
{
    private String[] cardview_titles;
    private int[] cardview_images;
    private int cardview_count;
    private MainCardRecyclerAdapter mainCardRecyclerAdapter;

    private RecyclerView recyclerView;

    public MainCardRecycler(String[] cardview_titles,int[] cardview_images,int cardview_count)
    {
        this.cardview_titles = cardview_titles;
        this.cardview_images = cardview_images;
        this.cardview_count = cardview_count;
    }

    public void setAdapterforRecycler(RecyclerView recView)
    {
        this.recyclerView = recView;

        mainRecyclerAdapter = new mainRecyclerAdapter(this,this.cardview_titles,this.cardview_images,this.cardview_count);


    }
}
