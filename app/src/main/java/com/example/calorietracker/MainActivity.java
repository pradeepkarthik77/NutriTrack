package com.example.calorietracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{

    private MainCardRecycler mainCardRecycler;
    private RecyclerView mainRecycler;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        this.mainRecycler = (RecyclerView) findViewById(R.id.main_recycler);
        ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result)
                    {
                        if(result.getResultCode() == 0)
                        {
                            for(int x = 0;x<mainRecycler.getChildCount();x++)
                            {
                                MainCardRecyclerAdapter.ViewHolder holder = (MainCardRecyclerAdapter.ViewHolder) mainRecycler.getChildViewHolder(mainRecycler.getChildAt(x));
                                if(holder.card_title.getText().toString().equals(result.getData().getStringExtra("cardview_name")))
                                {
                                    MainInnerRecyclerAdapter mainInnerRecyclerAdapter = (MainInnerRecyclerAdapter) holder.inner_card_recycler.getAdapter();
                                    mainInnerRecyclerAdapter.UpdateViews();
                                }
                            }
                        }
                    }
                });


        this.mainCardRecycler = new MainCardRecycler(this,activityResultLaunch);//);,this);

        this.mainCardRecycler.setAdapterforRecycler(this.mainRecycler);

    }
}