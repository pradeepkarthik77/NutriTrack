package com.example.calorietracker;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class InsertCSV
{

    private String[] default_values;

    private List<String> item_values;

    private Context context;

    private String EXCEL_FILE = "user_nutrition.csv";

    private FileOutputStream fileOutputStream;

    public InsertCSV(Context context)
    {
        this.context = context;
        default_values = new String[]{"item_id","item_name","serving_size","calories","fat","saturated_fat","trans_fat","cholesterol","sodium","carbohydrates","dietary_fiber","sugar","added_sugar","protein","vitamin_D","calcium","iron","potassium","vitamin_A","vitamin_C","manganese","vitamin_K","item_type","Date","Time"};
    }


    public void insert_into_csv(String cardview_name,List<String> values,String chosen_date,String chosen_time)
    {
        String data;

        this.item_values = values;

        try
        {
            File file = this.context.getFilesDir();
            File filer = new File(this.context.getFilesDir().toString()+"/"+this.EXCEL_FILE);
            if(!filer.exists())
            {
                fileOutputStream = this.context.openFileOutput(EXCEL_FILE,this.context.MODE_APPEND);
                data = String.join(",",this.default_values)+"\n";
                fileOutputStream.write(data.getBytes());
            }
            else
            {
                fileOutputStream = this.context.openFileOutput(EXCEL_FILE,this.context.MODE_APPEND);
            }
            data = String.join(",",this.item_values)+","+cardview_name+","+chosen_date+","+chosen_time+"\n";
            fileOutputStream.write(data.getBytes());
            Toast.makeText(this.context, "Data Saved!!!", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,"Error in Saving Data",Toast.LENGTH_LONG).show();
        }
    }
}