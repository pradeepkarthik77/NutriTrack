package com.example.calorietracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainCardRecyclerAdapter extends RecyclerView.Adapter<MainCardRecyclerAdapter.ViewHolder>
{
    private String[] cardview_titles;
    private int[] cardview_images;
    private int cardview_count;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView card_image;
        public TextView card_text;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.card_image = (ImageView) itemView.findViewById(R.id.cardview_image);
            this.card_text = (TextView) itemView.findViewById(R.id.cardview_text);
        }
    }

    public MainCardRecyclerAdapter(String[] cardview_titles,int[] cardview_images,int cardview_count,Context context)
    {
        this.cardview_titles = cardview_titles;
        this.cardview_images = cardview_images;
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
        holder.card_text.setText(this.cardview_titles[position]);
        holder.card_image.setImageResource(this.cardview_images[position]);
    }

    @Override
    public int getItemCount() {
        return this.cardview_count;
    }
}
