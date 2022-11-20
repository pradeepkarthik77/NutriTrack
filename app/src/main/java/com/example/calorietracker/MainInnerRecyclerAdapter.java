package com.example.calorietracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainInnerRecyclerAdapter extends RecyclerView.Adapter<MainInnerRecyclerAdapter.ViewHolder>
{
    private Context context;
    private String cardview_name;
    private int cardview_count;
    private LoadTheDatabase loadTheDatabase;
    private List<List<String>> item_values;
    private ExcelClass excelClass;

    public MainInnerRecyclerAdapter(Context context, String cardview_name, int cardview_count, LoadTheDatabase loadTheDatabase, List<List<String>> item_values,ExcelClass excelClass)
    {
        this.context = context;
        this.cardview_name = cardview_name;
        this.cardview_count = cardview_count;
        this.loadTheDatabase = loadTheDatabase;
        this.item_values = item_values;
        this.excelClass = excelClass;
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
        holder.item_img.setClipToOutline(true); //to set the image with curved edges
        List<String> this_item = Arrays.asList();
        try {
            this_item = this.item_values.get(position);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,"Unable to fetch all data",Toast.LENGTH_SHORT).show();
        }

        int resid;

        try
        {
            String img_path = this_item.get(4);

            resid = this.context.getResources().getIdentifier(img_path, "drawable", this.context.getPackageName());

            if (resid != 0)
            {
                holder.item_img.setImageResource(resid);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this.context,"Unable to Fetch Data",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        try {
            holder.item_text.setText(this_item.get(1));
            holder.item_id.setText(this_item.get(0));
            holder.item_calories.setText("Calories: " + this_item.get(2) + " gms");
            if (this_item.get(3).equals("1")) {
                holder.heart_btn.setBackgroundResource(R.drawable.heart_liked);
                holder.heart_btn.setContentDescription("1");
            } else {
                holder.heart_btn.setBackgroundResource(R.drawable.heart_it);
                holder.heart_btn.setContentDescription("0");
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,e.toString(),Toast.LENGTH_LONG).show();
        }

        holder.heart_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(view.getContentDescription().equals("1"))
                {
                    //TODO write logic to change the values in .csvFile and change the value in database and notify the recyclerView.
                    excelClass.remove_from_favorites(holder.item_id.getText().toString(),cardview_name);
                    loadTheDatabase.remove_liked(holder.item_id.getText().toString());
                    view.setBackgroundResource(R.drawable.heart_it);
                    view.setContentDescription("0");
                    //notifyItemChanged(holder.getAdapterPosition());
                }
                else //logic to add liked buottn
                {
                    //Toast.makeText(context,"Hello da",Toast.LENGTH_SHORT).show();
                    excelClass.add_to_favorites(holder.item_id.getText().toString(),cardview_name);
                    loadTheDatabase.add_liked(holder.item_id.getText().toString());
                    view.setBackgroundResource(R.drawable.heart_liked);
                    view.setContentDescription("1");
                    notifyItemChanged(holder.getAdapterPosition());
                    //notifyDataSetChanged();
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
