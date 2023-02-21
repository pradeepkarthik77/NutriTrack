package com.example.calorietracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainCardAdapter extends RecyclerView.Adapter<MainCardAdapter.ViewHolder>
{
    private Context context;
    private String[] cardview_titles;
    private int cardview_count;
    private int[] cardview_images;

    public MainCardAdapter(Context context,String[] cardview_titles,int cardview_count,int[] cardview_images)
    {
        this.context = context;
        this.cardview_titles = cardview_titles;
        this.cardview_count = cardview_count;
        this.cardview_images = cardview_images;//new int[]{R.drawable.breakfast, R.drawable.lunch, R.drawable.dinner, R.drawable.snacks, R.drawable.juices, R.drawable.water};
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.cardview_image);
            this.textView = itemView.findViewById(R.id.cardview_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(context,ListActivity.class);
                    intent.putExtra("cardview_title",textView.getText().toString().substring(4));
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public MainCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.mainactivty_card, parent, false);
        return new MainCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainCardAdapter.ViewHolder holder, int position)
    {
        holder.textView.setText("Log "+this.cardview_titles[position]);
        holder.imageView.setClipToOutline(true);
        holder.imageView.setImageResource(this.cardview_images[position]);
    }

    @Override
    public int getItemCount() {
        return this.cardview_count;
    }
}