package com.example.calorietracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WeekRecyclerAdapter extends RecyclerView.Adapter<WeekRecyclerAdapter.ViewHolder>
{
    private final SharedPreferences date_pref;
    private InsertCSV insertCSV;
    private String[] week_days = new String[]{"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    private Context context;
    private Date current_date= new Date();
    private Calendar calender;
    private int item_day_id;
    private ActivityResultLauncher<Intent> activityResultLaunch;
    private String chosen_date  = "";

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView week_day;
        public Date item_date;
        public TextView calorie_text;
        public String chosen_date = "";

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            itemView.getLayoutParams().width = (int)((double)width / 2.15);
            this.week_day = itemView.findViewById(R.id.week_day);
            this.calorie_text = itemView.findViewById(R.id.day_calorie);

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

        this.date_pref = context.getSharedPreferences("date",0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        this.chosen_date = dateFormat.format(this.current_date);

    }


//    private void create_dialog(String cardview_title,Date chosen_date,ViewHolder holder)
//    {
//            Dialog dialog = new Dialog(context);
//            dialog.setContentView(R.layout.week_dialog);
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(true);
//            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
//
//            TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.select_time);
//            Button apply_btn = (Button) dialog.findViewById(R.id.apply_btn);
//            Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);
//
//            TextView title_date = dialog.findViewById(R.id.week_dialog_date);
//
//            String date_string="";
//
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            date_string = simpleDateFormat.format(chosen_date);
//
//            String printtxt = "<b>Date:<b> "+date_string;
//
//            title_date.setText(Html.fromHtml(printtxt));
//
//            apply_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view)
//                {
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                    String date_string = "";
//                    date_string = simpleDateFormat.format(chosen_date);
//                    Intent intent = new Intent(context,ListActivity.class);
//                    intent.putExtra("cardview_title",cardview_title);
//                    intent.putExtra("chosen_date",date_string);
//                    intent.putExtra("recycler_id",holder.getAdapterPosition()+"");
//                    activityResultLaunch.launch(intent);
//                    dialog.dismiss();
//                }
//            });
//
//            cancel_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                }
//            });
//
//            dialog.create();
//            dialog.show();
//    }

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

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");

        holder.chosen_date = simpleDateFormat1.format(holder.item_date);

        Float today_calorie = insertCSV.get_day_calorie(simpleDateFormat1.format(holder.item_date));

        DecimalFormat df = new DecimalFormat("#.##"); // rounds to 2 decimal places
        today_calorie = Float.parseFloat(df.format(today_calorie));

        String Caltext = "<b>Calories</b>: "+today_calorie;

        holder.calorie_text.setText(Html.fromHtml(Caltext));

        int[] card_values = this.insertCSV.return_marked(holder.item_date);

//        Toast.makeText(context,simpleDateFormat1.format(holder.item_date),Toast.LENGTH_SHORT).show();

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
            checkBox = holder.itemView.findViewById(R.id.midmeals_checkbox);
            checkBox.setChecked(true);
        }

        if(card_values[4] == 1)
        {
            checkBox = holder.itemView.findViewById(R.id.snacks_checkbox);
            checkBox.setChecked(true);
        }
        if(card_values[5] == 1)
        {
            checkBox = holder.itemView.findViewById(R.id.juice_checkbox);
            checkBox.setChecked(true);
        }


        MaterialButton breakfast_btn = holder.itemView.findViewById(R.id.breakfast_check_btn);
        MaterialButton lunch_btn = holder.itemView.findViewById(R.id.lunch_check_btn);
        MaterialButton dinner_btn = holder.itemView.findViewById(R.id.dinner_check_btn);
        MaterialButton snacks_btn = holder.itemView.findViewById(R.id.snacks_check_btn);
        MaterialButton juice_btn = holder.itemView.findViewById(R.id.juice_check_btn);
        MaterialButton midmeal_btn = holder.itemView.findViewById(R.id.midmeals_check_btn);

        breakfast_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                create_dialog("BreakFast",holder.item_date,holder);

                Intent intent = new Intent(context,ListActivity.class);
                intent.putExtra("cardview_title","BreakFast");
                intent.putExtra("chosen_date",holder.chosen_date);
                intent.putExtra("recycler_id",holder.getAdapterPosition()+"");
//                Toast.makeText(context,simpleDateFormat1.format(holder.item_date),Toast.LENGTH_LONG).show();
                activityResultLaunch.launch(intent);
            }
        });

        lunch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ListActivity.class);
                intent.putExtra("cardview_title","Lunch");
                intent.putExtra("chosen_date",holder.chosen_date);
                intent.putExtra("recycler_id",holder.getAdapterPosition()+"");
                activityResultLaunch.launch(intent);
            }
        });

        dinner_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ListActivity.class);
                intent.putExtra("cardview_title","Dinner");
                intent.putExtra("chosen_date",holder.chosen_date);
                intent.putExtra("recycler_id",holder.getAdapterPosition()+"");
                activityResultLaunch.launch(intent);
            }
        });

        snacks_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ListActivity.class);
                intent.putExtra("cardview_title","Snacks");
                intent.putExtra("chosen_date",holder.chosen_date);
                intent.putExtra("recycler_id",holder.getAdapterPosition()+"");
                activityResultLaunch.launch(intent);
            }
        });

        juice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ListActivity.class);
                intent.putExtra("cardview_title","Juices");
                intent.putExtra("chosen_date",holder.chosen_date);
                intent.putExtra("recycler_id",holder.getAdapterPosition()+"");
                activityResultLaunch.launch(intent);
            }
        });

        midmeal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ListActivity.class);
                intent.putExtra("cardview_title","Mid-Meals");
                intent.putExtra("chosen_date",holder.chosen_date);
                intent.putExtra("recycler_id",holder.getAdapterPosition()+"");
                activityResultLaunch.launch(intent);
            }
        });

        ImageButton breakfast_eye = holder.itemView.findViewById(R.id.eye_btn_breakfast);
        ImageButton lunch_eye = holder.itemView.findViewById(R.id.eye_btn_lunch);
        ImageButton dinner_eye = holder.itemView.findViewById(R.id.eye_btn_dinner);
        ImageButton midmeals_eye = holder.itemView.findViewById(R.id.eye_btn_midmeals);
        ImageButton snacks_eye = holder.itemView.findViewById(R.id.eye_btn_snacks);
        ImageButton juices_eye = holder.itemView.findViewById(R.id.eye_btn_juice);

        breakfast_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_display_dialog("BreakFast",holder.item_date,holder);
            }
        });

        lunch_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_display_dialog("Lunch",holder.item_date,holder);
            }
        });

        dinner_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_display_dialog("Dinner",holder.item_date,holder);
            }
        });

        snacks_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_display_dialog("Snacks",holder.item_date,holder);
            }
        });

        juices_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_display_dialog("Juices",holder.item_date,holder);
            }
        });

        midmeals_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_display_dialog("Mid-Meals",holder.item_date,holder);
            }
        });

    }

    private void create_display_dialog(String card_title, Date item_date, ViewHolder holder)
    {
        //TODO: Display the items consumed during the date and that card_title and the cumulative calories,protein,fat,carbohydrates,fat
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.display_day_date_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button apply_btn = (Button) dialog.findViewById(R.id.apply_btn);

//        TextView title_date = dialog.findViewById(R.id.week_dialog_date);

        InsertCSV insertCSV = new InsertCSV(context);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date_string = simpleDateFormat.format(item_date);

        TextView date_view = dialog.findViewById(R.id.item_date);

        String tempstring = "<b>Date</b>: "+date_string;

        date_view.setText(Html.fromHtml(tempstring));

        date_view = dialog.findViewById(R.id.item_type);

        tempstring = "<b>Type</b>: "+card_title;

        date_view.setText(Html.fromHtml(tempstring));

        Map<String,String> map = insertCSV.get_givendata_week_values(date_string,card_title);

        //Toast.makeText(context,map.get("Items")+" "+map.get("Calories")+" "+map.get("Carbs")+" "+map.get("Protein")+" "+map.get("Fat"),Toast.LENGTH_LONG).show();
        String string = "";
        if(map.get("Items").equals(""))
        {
            string = "<b>Items</b>: None";
        }
        else {
         string = "<b>Items</b>: " + map.get("Items");
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        TextView txtview = dialog.findViewById(R.id.items_list);

        txtview.setText(Html.fromHtml(string));

        string = "<b>Total Calories</b>: "+Float.parseFloat(decimalFormat.format(Float.parseFloat(map.get("Calories"))))+"kcal";

        txtview = dialog.findViewById(R.id.dialog_calories);

        txtview.setText(Html.fromHtml(string));

        string = "<b>Total Protein</b>: "+Float.parseFloat(decimalFormat.format(Float.parseFloat(map.get("Protein"))))+"g";

        txtview = dialog.findViewById(R.id.dialog_protein);

        txtview.setText(Html.fromHtml(string));

        string = "<b>Total Fat</b>: "+Float.parseFloat(decimalFormat.format(Float.parseFloat(map.get("Fat"))))+"g";

        txtview = dialog.findViewById(R.id.dialog_fat);

        txtview.setText(Html.fromHtml(string));

        string = "<b>Total Carbohydrates</b>: "+Float.parseFloat(decimalFormat.format(Float.parseFloat(map.get("Carbs"))))+"g";

        txtview = dialog.findViewById(R.id.dialog_carbs);

        txtview.setText(Html.fromHtml(string));


        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();

    }

    @Override
    public int getItemCount() {
        return 7;
    }
}
