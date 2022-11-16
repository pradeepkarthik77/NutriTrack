package com.example.calorietracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainInnerRecyclerAdapter extends RecyclerView.Adapter<MainInnerRecyclerAdapter.ViewHolder>
{
    private Context context;
    private String cardview_name;
    private int cardview_count;
    private LoadTheDatabase loadTheDatabase;
    private List<List<String>> item_values;

    public MainInnerRecyclerAdapter(Context context, String cardview_name, int cardview_count, LoadTheDatabase loadTheDatabase, List<List<String>> item_values)
    {
        this.context = context;
        this.cardview_name = cardview_name;
        this.cardview_count = cardview_count;
        this.loadTheDatabase = loadTheDatabase;
        this.item_values = item_values;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView item_img;
        private TextView item_text;
        private TextView item_id;
        private TextView item_calories;
        private ImageView heart_btn;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.item_img = itemView.findViewById(R.id.cardview_image);
            this.item_text = itemView.findViewById(R.id.cardview_text);
            this.item_id = itemView.findViewById(R.id.item_id);
            this.item_calories = itemView.findViewById(R.id.cardview_calories);
            this.heart_btn = itemView.findViewById(R.id.heartbutton);
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
        holder.item_img.setClipToOutline(true);
        List<String> this_item = this.item_values.get(position);

        int resid;

        try
        {
            String img_path = this_item.get(5);

            resid = this.context.getResources().getIdentifier(img_path, "drawable", this.context.getPackageName());

            if (resid != 0)
            {
                holder.item_img.setImageResource(resid);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this.context,this_item.get(5),Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        holder.item_text.setText(this_item.get(1));
        holder.item_id.setText(this_item.get(0));
        holder.item_calories.setText("Calories: "+this_item.get(3)+" gms");
        if(this_item.get(4).equals("1"))
        {
            holder.heart_btn.setBackgroundResource(R.drawable.heart_liked);
            holder.heart_btn.setContentDescription("1");
        }
        else
        {
            holder.heart_btn.setBackgroundResource(R.drawable.heart_it);
            holder.heart_btn.setContentDescription("0");
        }

        holder.heart_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(view.getContentDescription().equals("1"))
                {
                    view.setBackgroundResource(R.drawable.heart_it);
                    view.setContentDescription("0");
                }
                else
                {
                    view.setBackgroundResource(R.drawable.heart_liked);
                    view.setContentDescription("1");
                }

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return this.cardview_count;
    }
}
