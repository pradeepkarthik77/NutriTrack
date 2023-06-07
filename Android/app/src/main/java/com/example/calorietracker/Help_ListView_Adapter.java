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

public class Help_ListView_Adapter extends BaseAdapter
{
    private ArrayList<String> string_array = new ArrayList<String>();
    private ArrayList<Integer> image_array = new ArrayList<>();
    private Context context;
    private Integer ITEM_COUNT = 0;

    public Help_ListView_Adapter(Context context)
    {
        this.context = context;

        this.string_array.add("From the DashBoard of NutriTrack, you can get a brief summary of your health along with options to log your nutrition intake. The dashboard also provides an option to change the date");
        this.string_array.add("From the Reports page of NutriTrack, you can visualize your nutrition intake over the past and draw insights about your diet.");
        this.string_array.add("From the Goals Page of NutriTrack, you can set up goals that you want to achieve and continuously track them regularly.");
        this.string_array.add("From the WeekLog Page of NutriTrack, you can view your Nutrition log for the past week and you can even log in the details of the diets that you have missed out.");
        this.string_array.add("From the User profile page of NutriTrack, you can view your personal details and you can also edit them to keep the values updated");
        this.string_array.add("While logging in your daily nutrition intake, NutriTrack also provides a option to scan your food and we use a AI model to predict the nutrients in the food in order to save your time and effort");

        this.image_array.add(R.drawable.dashboard_icon_green);
        this.image_array.add(R.drawable.chart_icon_green);
        this.image_array.add(R.drawable.goal_icon_green);
        this.image_array.add(R.drawable.weeklog_icon_green);
        this.image_array.add(R.drawable.user_profile_icon_green);
        this.image_array.add(R.drawable.photo_icon_green);

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
