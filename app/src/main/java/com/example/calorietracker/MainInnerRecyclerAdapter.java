package com.example.calorietracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainInnerRecyclerAdapter extends RecyclerView.Adapter<MainInnerRecyclerAdapter.ViewHolder>
{
    private Context context;
    private String cardview_name;
    private int cardview_count;
    private LoadTheDatabase loadTheDatabase;

    public MainInnerRecyclerAdapter(Context context,String cardview_name,int cardview_count,LoadTheDatabase loadTheDatabase)
    {
        this.context = context;
        this.cardview_name = cardview_name;
        this.cardview_count = cardview_count;
        this.loadTheDatabase = loadTheDatabase;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public MainInnerRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.maininner_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainInnerRecyclerAdapter.ViewHolder holder, int position)
    {
        //TODO Call the LoadDatabase function and get the values regarding the cardview such as
    }

    @Override
    public int getItemCount() {
        return this.cardview_count;
    }
}
