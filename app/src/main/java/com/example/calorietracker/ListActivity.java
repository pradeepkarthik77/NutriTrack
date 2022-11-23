package com.example.calorietracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListActivity extends AppCompatActivity
{
    private String cardview_title;

    private LoadTheDatabase loadTheDatabase;
    private ExcelClass excelClass;
    private Context context;
    private RecyclerView recyclerView;
    private NewRecyclerAdapter newRecyclerAdapter;
    private String[] favorites_list;
    private int cardview_count;
    private List<List<String>> item_values;
    private String[] not_favorite_list;
    private GridLayoutManager gridLayoutManager;

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putExtra("cardview_name", this.cardview_title);
        setResult(0, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listactivity);

        Intent intent = getIntent();

        this.cardview_title = intent.getStringExtra("cardview_title");

        this.context = this;


        this.loadTheDatabase = new LoadTheDatabase(this.context);
        this.excelClass = new ExcelClass(this.context);

        this.favorites_list = this.excelClass.get_favorites(this.cardview_title,false);

        this.loadTheDatabase.setValues();

        this.cardview_count = this.loadTheDatabase.get_count(this.cardview_title);

        this.not_favorite_list = this.loadTheDatabase.get_unFavorites(this.cardview_title,this.favorites_list);

        this.item_values = this.loadTheDatabase.get_smaller_card_values(this.favorites_list,this.not_favorite_list,this.cardview_count,true);

        this.gridLayoutManager = new GridLayoutManager(this.context,2);

        this.recyclerView = findViewById(R.id.listActivity_recycler);

       this.newRecyclerAdapter = new NewRecyclerAdapter(this.context,this.cardview_title,this.cardview_count,this.loadTheDatabase,this.item_values,this.excelClass);

       this.recyclerView.setAdapter(this.newRecyclerAdapter);

       this.recyclerView.setLayoutManager(gridLayoutManager);


    }
}
