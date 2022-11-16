package com.example.calorietracker;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainInnerRecycler
{

    private String cardview_title;

    private RecyclerView recyclerView;

    private MainInnerRecyclerAdapter mainInnerRecyclerAdapter;

    private Context context;

    private int cardview_count;

    private LoadTheDatabase loadTheDatabase;

    private List<List<String>> item_values;

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
        int count= loadTheDatabase.returncount(cardview_title);
        this.cardview_count = Math.min(count,3); //Change Value here to limit the no of items to be displayed in mainActivity
        this.item_values = this.loadTheDatabase.get_smaller_card_values(cardview_title,this.cardview_count);
    }

    public void setAdapter(RecyclerView recyclerView)
    {
        this.recyclerView = recyclerView;

        findandsetvalues();

        mainInnerRecyclerAdapter = new MainInnerRecyclerAdapter(this.context,this.cardview_title,this.cardview_count,this.loadTheDatabase,this.item_values);

        this.recyclerView.setAdapter(mainInnerRecyclerAdapter);

        this.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        LinearLayoutManager horizontalManager = new LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false);

        this.recyclerView.setLayoutManager(horizontalManager);

    }


}
