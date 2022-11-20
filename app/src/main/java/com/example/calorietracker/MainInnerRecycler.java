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

    public LoadTheDatabase loadTheDatabase;

    private List<List<String>> item_values;

    public ExcelClass excelClass;

    private String[] favorite_list;

    public MainInnerRecycler(String cardview_title,Context context,LoadTheDatabase loadTheDatabase,ExcelClass excelClass)
    {
        this.cardview_title = cardview_title;
        this.context = context;
        this.loadTheDatabase = loadTheDatabase;
        this.excelClass = excelClass;
    }

    public void findandsetvalues()
    {

        this.favorite_list = excelClass.get_favorites(this.cardview_title,true);
        loadTheDatabase.setValues();
        this.cardview_count = 3;
        this.item_values = this.loadTheDatabase.get_smaller_card_values(this.favorite_list,new String[]{},this.cardview_count,false);

    }

    public void setAdapter(RecyclerView recyclerView)
    {
        this.recyclerView = recyclerView;

        findandsetvalues();

        mainInnerRecyclerAdapter = new MainInnerRecyclerAdapter(this.context,this.cardview_title,this.cardview_count,this.loadTheDatabase,this.item_values,this.excelClass);

        this.recyclerView.setAdapter(mainInnerRecyclerAdapter);

        //this.recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());

        LinearLayoutManager horizontalManager = new LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false);

        this.recyclerView.setLayoutManager(horizontalManager);

    }


}
