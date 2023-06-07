package com.example.calorietracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Goal_ListView_Adapter extends BaseAdapter
{
    private ArrayList<String> string_array = new ArrayList<String>();
    private ArrayList<Integer> image_array = new ArrayList<>();
    private Context context;
    private Integer ITEM_COUNT = 0;

    public Goal_ListView_Adapter(Context context)
    {
        this.context = context;

        this.string_array.add("Set Your Daily Calorie Intake Goal");
        this.string_array.add("Set You Daily Water Intake Goal");
        this.string_array.add("Set You Daily Carbohydrates Intake Goal");
        this.string_array.add("Set You Daily Protein Intake Goal");
        this.string_array.add("Set You Daily Fat Intake Goal");
        this.string_array.add("Set You Daily Fiber Intake Goal");
        this.string_array.add("Set You Daily Vitamin C Intake Goal");


        this.image_array.add(R.drawable.calorie_icon);
        this.image_array.add(R.drawable.water_drop_icon);
        this.image_array.add(R.drawable.carbohydrates_icon);
        this.image_array.add(R.drawable.protein_icon);
        this.image_array.add(R.drawable.fats_icon);
        this.image_array.add(R.drawable.fiber_icon);
        this.image_array.add(R.drawable.vitamin_c_icon);

        this.ITEM_COUNT = this.string_array.size();
    }


    @Override
    public int getCount() {
        return string_array.size();
    }

    @Override
    public Object getItem(int position) {
        return string_array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Inflate the layout for the list item
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.help_list_item, parent, false);
        }

        TextView item_text = convertView.findViewById(R.id.help_item_text);
        ImageView item_image = convertView.findViewById(R.id.help_item_icon);

        item_text.setText(this.string_array.get(position));
        item_image.setImageDrawable(context.getDrawable(this.image_array.get(position)));

        return convertView;
    }
}
