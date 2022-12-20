package com.example.calorietracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekRecyclerAdapter extends RecyclerView.Adapter<WeekRecyclerAdapter.ViewHolder>
{
    private InsertCSV insertCSV;
    private String[] week_days = new String[]{"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    private Context context;
    private Date current_date= new Date();
    private Calendar calender;
    private int item_day_id;
    private ActivityResultLauncher<Intent> activityResultLaunch;

    private String chosen_time="";

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView week_day;
        public Date item_date;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            itemView.getLayoutParams().width = (int)((double)width / 2.15);
            this.week_day = itemView.findViewById(R.id.week_day);
            this.item_date = new Date();

        }
    }

    WeekRecyclerAdapter(Context context, Date chosen_date,ActivityResultLauncher<Intent> activityResultLaunch)
    {
        this.context = context;
        this.current_date = chosen_date;
        this.calender = Calendar.getInstance();
        this.calender.setTime(this.current_date);
        this.item_day_id = this.calender.get(Calendar.DAY_OF_WEEK);
        this.insertCSV = new InsertCSV(context);
        this.activityResultLaunch = activityResultLaunch;


    }


    private void create_dialog(String cardview_title,Date chosen_date,ViewHolder holder)
    {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.week_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

            TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.select_time);
            Button apply_btn = (Button) dialog.findViewById(R.id.apply_btn);
            Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);

            TextView title_date = dialog.findViewById(R.id.week_dialog_date);

            String date_string="";

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            date_string = simpleDateFormat.format(chosen_date);

            String printtxt = "<b>Date:<b> "+date_string;

            title_date.setText(Html.fromHtml(printtxt));

            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker timePicker, int hr, int min)
                {
                    String hour = "";
                    String time = "";

                    if(hr<10)
                    {
                        hour = "0"+hr;
                    }
                    else
                    {
                        hour = hr+"";
                    }

                    if(min<10)
                    {
                        time = "0"+min;
                    }
                    else
                    {
                        time = min+"";
                    }

                    chosen_time = hour+":"+time;
                }
            });

            apply_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String date_string = "";
                    date_string = simpleDateFormat.format(chosen_date);
                    Intent intent = new Intent(context,ListActivity.class);
                    intent.putExtra("cardview_title",cardview_title);
                    intent.putExtra("chosen_time",chosen_time);
                    intent.putExtra("chosen_date",date_string);
                    intent.putExtra("recycler_id",holder.getAdapterPosition()+"");
                    activityResultLaunch.launch(intent);
                    dialog.dismiss();
                }
            });

            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.create();
            dialog.show();
    }

    @NonNull
    @Override
    public WeekRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(this.context);

        View view  = inflater.inflate(R.layout.weektable_card,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekRecyclerAdapter.ViewHolder holder, int position)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(this.current_date);
        c.add(Calendar.DATE, -position);
        holder.item_date = c.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM");

        String item_date_string = simpleDateFormat.format(holder.item_date);

        String item_day_string = week_days[c.get(Calendar.DAY_OF_WEEK)-1];

        holder.week_day.setText(item_day_string+" - "+item_date_string);

        int[] card_values = this.insertCSV.return_marked(holder.item_date);

        CheckBox checkBox;

        if(card_values[0] == 1)
        {
            checkBox = holder.itemView.findViewById(R.id.breakfast_checkbox);
            checkBox.setChecked(true);
        }

        if(card_values[1] == 1)
        {
            checkBox = holder.itemView.findViewById(R.id.lunch_checkbox);
            checkBox.setChecked(true);
        }

        if(card_values[2] == 1)
        {
            checkBox = holder.itemView.findViewById(R.id.dinner_checkbox);
            checkBox.setChecked(true);
        }

        if(card_values[3] == 1)
        {
            checkBox = holder.itemView.findViewById(R.id.snacks_checkbox);
            checkBox.setChecked(true);
        }

        if(card_values[4] == 1)
        {
            checkBox = holder.itemView.findViewById(R.id.juice_checkbox);
            checkBox.setChecked(true);
        }

        if(card_values[5] == 1)
        {
            checkBox = holder.itemView.findViewById(R.id.water_checkbox);
            checkBox.setChecked(true);
        }

        Button breakfast_btn = holder.itemView.findViewById(R.id.breakfast_check_btn);
        Button lunch_btn = holder.itemView.findViewById(R.id.lunch_check_btn);
        Button dinner_btn = holder.itemView.findViewById(R.id.dinner_check_btn);
        Button snacks_btn = holder.itemView.findViewById(R.id.snacks_check_btn);
        Button juice_btn = holder.itemView.findViewById(R.id.juice_check_btn);
        Button water_btn = holder.itemView.findViewById(R.id.water_check_btn);

        breakfast_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_dialog("BreakFast",holder.item_date,holder);
            }
        });

        lunch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_dialog("Lunch",holder.item_date,holder);
            }
        });

        dinner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_dialog("Dinner",holder.item_date,holder);
            }
        });

        snacks_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_dialog("Snacks",holder.item_date,holder);
            }
        });

        juice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_dialog("Juices",holder.item_date,holder);
            }
        });

        water_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_dialog("Water",holder.item_date,holder);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 7;
    }
}
