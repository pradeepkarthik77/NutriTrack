package com.example.calorietracker;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainInnerRecycler
{
    private int item_ids[] = new int[]{101,102};

    private int item_imgs[] = new int[]{R.drawable.idli_icon,R.drawable.dosa_icon};

    private String cardview_title;

    private RecyclerView recyclerView;

    private MainInnerRecyclerAdapter mainInnerRecyclerAdapter;

    private Context context;

    private int cardview_count;

    private LoadTheDatabase loadTheDatabase;

    public MainInnerRecycler(String cardview_title,Context context)
    {
        this.cardview_title = cardview_title;
        this.context = context;
    }

    //TODO write a function to load the values from the database and then set the adapter for the most priority 5 items ALONE.

    public void findandsetvalues()
    {
        this.loadTheDatabase = new LoadTheDatabase(this.context);
        loadTheDatabase.setValues();
        this.cardview_count = loadTheDatabase.returncount(cardview_title);
    }

    public void setAdapter(RecyclerView recyclerView)
    {
        this.recyclerView = recyclerView;

        findandsetvalues();

        mainInnerRecyclerAdapter = new MainInnerRecyclerAdapter(this.context,this.cardview_title,this.cardview_count,this.loadTheDatabase);

        this.recyclerView.setAdapter(mainInnerRecyclerAdapter);

        this.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        LinearLayoutManager horizontalManager = new LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false);

        this.recyclerView.setLayoutManager(horizontalManager);
    }


}
