package com.example.calorietracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>
{
    private int height;
    private int weight;
    private int activity_level;
    private String name;
    private int age;
    private String gender;
    private int goal_calroie;
    private Context context;
    private String[] cardview_titles = new String[]{"Gender","Age","Height","Weight","Activity","Calorie Goal"};
    private int[] cardview_images = new int[]{R.drawable.gender_icon,R.drawable.age_icon,R.drawable.height_icon,R.drawable.weight_icon,R.drawable.activity_icon,R.drawable.calorie_goal_icon};
    private SharedPreferences login_pref;
    private int cardview_count = 6;

    public UserRecyclerAdapter(Context context)
    {
        this.context = context;
        this.login_pref = context.getSharedPreferences("Login",0);
        this.height = this.login_pref.getInt("Height",150);
        this.weight = this.login_pref.getInt("Weight",60);
        this.activity_level = this.login_pref.getInt("Activity_Level",0);
        this.name = this.login_pref.getString("name","");
        this.age = this.login_pref.getInt("Age",30);
        this.gender = this.login_pref.getString("Gender","Male");
        this.goal_calroie = this.login_pref.getInt("Goal_Calorie",0);

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView titletext;
        TextView valuetext;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.profile_card_image);
            this.titletext = itemView.findViewById(R.id.profile_card_title);
            this.valuetext = itemView.findViewById(R.id.profile_card_text);
        }
    }

    @NonNull
    @Override
    public UserRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.profile_cardview, parent, false);
        return new UserRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerAdapter.ViewHolder holder, int position)
    {
        holder.titletext.setText(cardview_titles[position]);
        holder.imageView.setClipToOutline(true);
        holder.imageView.setImageResource(this.cardview_images[position]);

        if(cardview_titles[position].equals("Gender"))
        {
            holder.valuetext.setText(this.gender);
        }
        else if(cardview_titles[position].equals("Age"))
        {
            holder.valuetext.setText(this.age+" yrs");
        }
        else if(cardview_titles[position].equals("Height"))
        {
            holder.valuetext.setText(this.height+" cms");
        }
        else if(cardview_titles[position].equals("Weight"))
        {
            holder.valuetext.setText(this.weight+" kgs");
        }
        else if(cardview_titles[position].equals("Activity"))
        {
            String levels[] = new String[]{"Sedentary", "Lightly active", "Moderately active","Very active","Extra Active"};
            holder.valuetext.setText(levels[this.activity_level]);
        }
        else if(cardview_titles[position].equals("Calorie Goal"))
        {
            holder.valuetext.setText(this.goal_calroie+" kcal");
        }

    }

    @Override
    public int getItemCount() {
        return this.cardview_count;
    }
}