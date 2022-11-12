package com.example.calorietracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainCardRecyclerAdapter extends RecyclerView.Adapter<MainCardRecyclerAdapter.ViewHolder>
{
    private String[] cardview_titles;
    private int cardview_count;
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView card_title;
        public RecyclerView inner_card_recycler;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.card_title = (TextView) itemView.findViewById(R.id.main_card_title);
            this.inner_card_recycler = (RecyclerView) itemView.findViewById(R.id.main_content_recycler);
        }
    }

    public MainCardRecyclerAdapter(String[] cardview_titles,int cardview_count,Context context)
    {
        this.cardview_titles = cardview_titles;
        this.cardview_count = cardview_count;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.mainactivty_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.card_title.setText(this.cardview_titles[position]);

        MainInnerRecycler mainInnerRecycler = new MainInnerRecycler(this.cardview_titles[position],this.context);

        mainInnerRecycler.setAdapter(holder.inner_card_recycler);
    }

    @Override//
    public int getItemCount() {
        return this.cardview_count;
    }
}
