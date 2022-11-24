package com.example.calorietracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewRecyclerAdapter extends RecyclerView.Adapter<NewRecyclerAdapter.ViewHolder>
{
    private Context context;
    private String cardview_name;
    private int cardview_count;
    private LoadTheDatabase loadTheDatabase;
    private List<List<String>> item_values;
    private ExcelClass excelClass;

    public String chosen_date="";
    public String chosen_time="";

    public NewRecyclerAdapter(Context context, String cardview_name, int cardview_count, LoadTheDatabase loadTheDatabase, List<List<String>> item_values,ExcelClass excelClass,String chosen_date,String chosen_time)
    {
        this.context = context;
        this.cardview_name = cardview_name;
        this.cardview_count = cardview_count;
        this.loadTheDatabase = loadTheDatabase;
        this.item_values = item_values;
        this.excelClass = excelClass;
        this.chosen_date = chosen_date;
        this.chosen_time = chosen_time;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView item_img;
        private TextView item_text;
        private TextView item_id;
        private TextView item_calories;
        private ImageView heart_btn;
        private TextView save_btn;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.item_img = itemView.findViewById(R.id.cardview_image);
            this.item_text = itemView.findViewById(R.id.cardview_text);
            this.item_id = itemView.findViewById(R.id.item_id);
            this.item_calories = itemView.findViewById(R.id.cardview_calories);
            this.heart_btn = itemView.findViewById(R.id.heartbutton);
            this.save_btn = itemView.findViewById(R.id.save_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newintent = new Intent(context,DisplayTable.class);
                    newintent.putExtra("item_id",item_id.getText().toString());
                    newintent.putExtra("item_name",item_text.getText().toString());
                    newintent.putExtra("chosen_date",chosen_date);
                    newintent.putExtra("chosen_time",chosen_time);
                    context.startActivity(newintent);
                }
            });
        }
    }

    @NonNull
    @Override
    public NewRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.newactivity_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewRecyclerAdapter.ViewHolder holder, int position)
    {
        //TODO Call the LoadDatabase function and get the values regarding the cardview such as
        holder.item_img.setClipToOutline(true); //to set the image with curved edges
        List<String> this_item = this.item_values.get(position);

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

        holder.item_text.setText(this_item.get(1));
        holder.item_id.setText(this_item.get(0));
        holder.item_calories.setText("Calories: "+this_item.get(2)+" gms");
        if(this_item.get(3).equals("1"))
        {
            holder.heart_btn.setBackgroundResource(R.drawable.heart_liked);
            holder.heart_btn.setContentDescription("1");
        }
        else
        {
            holder.heart_btn.setBackgroundResource(R.drawable.heart_it);
            holder.heart_btn.setContentDescription("0");
        }

        //Toast.makeText(this.context,this.cardview_count+"",Toast.LENGTH_SHORT).show();

        holder.heart_btn.setOnClickListener(new View.OnClickListener()
        {
            private  ExcelClass xlclass;
            private  String[] fav_list;

            @Override
            public void onClick(View view)
            {
                this.xlclass = new ExcelClass(context);

                this.fav_list = this.xlclass.get_favorites(cardview_name,false);

                if(view.getContentDescription().equals("1"))
                {
                    //TODO write logic to change the values in .csvFile and change the value in database and notify the recyclerView.
                    excelClass.remove_from_favorites(holder.item_id.getText().toString(),cardview_name);
                    loadTheDatabase.remove_liked(holder.item_id.getText().toString());
                    view.setBackgroundResource(R.drawable.heart_it);
                    view.setContentDescription("0");
                    if(this.fav_list.length!=cardview_count) {
                        notifyItemMoved(holder.getAdapterPosition(), this.fav_list.length);
                    }
                    else
                    {
                        notifyItemMoved(holder.getAdapterPosition(),cardview_count-1);
                    }

                }
                else //logic to add liked buottn
                {
                    excelClass.add_to_favorites(holder.item_id.getText().toString(),cardview_name);
                    loadTheDatabase.add_liked(holder.item_id.getText().toString());
                    view.setBackgroundResource(R.drawable.heart_liked);
                    view.setContentDescription("1");
                    notifyItemMoved(holder.getAdapterPosition(),0);
                }
            }
        });

        holder.save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Are You Sure?");
                dialog.setMessage("Do You Want to Save this Data?");
                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO add logic to insert the value into the .csv file

                        if (chosen_date.equals("")) {
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = new Date();
                            chosen_date = formatter.format(date);
                            //Toast.makeText(getApplicationContext(),formatter.format(date),Toast.LENGTH_SHORT).show();
                        }

                        if (chosen_time.equals("")) {
                            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                            Date date = new Date();
                            chosen_time = formatter.format(date);
                        }



                        InsertCSV insertCSV = new InsertCSV(context);

                        LoadTheDatabase loadTheDatabase = new LoadTheDatabase(context);
                        List<String> item_values = loadTheDatabase.get_nutrition(holder.item_id.getText().toString());

                        //Toast.makeText(context,chosen_date+" "+chosen_time,Toast.LENGTH_LONG).show();

                        insertCSV.insert_into_csv(item_values, chosen_date, chosen_time);
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return this.cardview_count;
    }
}
