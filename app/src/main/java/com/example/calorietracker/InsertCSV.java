package com.example.calorietracker;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class InsertCSV
{

    private String[] default_values;

    private List<String> item_values;

    private Context context;

    private String EXCEL_FILE = "user_nutrition.csv";

    private String BUFFER_FILE = "user_buffer.csv";

    private FileOutputStream fileOutputStream;

    private String[] cardview_titles = new String[]{"BreakFast","Lunch","Dinner","Snacks","Juices","Water"};

    public InsertCSV(Context context)
    {
        this.context = context;
        default_values = new String[]{"item_id","item_name","serving_size","calories","fat","saturated_fat","trans_fat","cholesterol","sodium","carbohydrates","dietary_fiber","sugar","added_sugar","protein","vitamin_D","calcium","iron","potassium","vitamin_A","vitamin_C","manganese","vitamin_K","item_type","Date","Time"
        };
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
            Enter_into_buffer_csv(data);
            fileOutputStream.write(data.getBytes());
            Toast.makeText(this.context, "Data Saved!!!", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,"Error in Saving Data",Toast.LENGTH_LONG).show();
        }
    }

    public int[] return_marked(Date current_date)
    {

        int i=0;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String current_date_string = simpleDateFormat.format(current_date);

        String data;

        int[] return_arr = new int[]{0,0,0,0,0,0};

        try
        {
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

            try {

                FileReader filereader = new FileReader(filer);

                BufferedReader bufferedReader = new BufferedReader(filereader);

                String nextline;

                String[] nextlinearr = new String[]{};

                nextline = bufferedReader.readLine();

                while ((nextline = bufferedReader.readLine()) != null)
                {
                    nextlinearr = nextline.split(",");
                    if(nextlinearr.length == 0)
                    {
                        continue;
                    }
                    else
                    {
                        if(nextlinearr[nextlinearr.length-2].equals(current_date_string))
                        {
                            for(i=0;i<cardview_titles.length;i++)
                            {
                                if(nextlinearr[nextlinearr.length-3].equals(this.cardview_titles[i]))
                                {
                                    return_arr[i] = 1;
                                }
                            }
                        }
                    }
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(this.context,e.toString()+" add",Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,"Error in Accessing Data",Toast.LENGTH_LONG).show();
        }

        return return_arr;
    }

    public void Enter_into_buffer_csv(String data)
    {
        FileOutputStream fileOutputStream;

        try
        {
            File file = this.context.getFilesDir();
            File filer = new File(this.context.getFilesDir().toString()+"/"+this.BUFFER_FILE);
            if(!filer.exists())
            {
                fileOutputStream = this.context.openFileOutput(this.BUFFER_FILE,this.context.MODE_APPEND);
                String tempdata = "";
                fileOutputStream.write(tempdata.getBytes());
                //Toast.makeText(context,"No file",Toast.LENGTH_SHORT).show();
            }
            else
            {
                fileOutputStream = this.context.openFileOutput(this.BUFFER_FILE,this.context.MODE_APPEND);
            }
            //Toast.makeText(context,data,Toast.LENGTH_LONG).show();
            fileOutputStream.write(data.getBytes());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    public HashMap<String,String> read_from_buffer()
    {

        HashMap<String,String> map = new HashMap<>();

        try
        {
            File filer = new File(this.context.getFilesDir().toString()+"/"+this.BUFFER_FILE);
            if(!filer.exists())
            {
                fileOutputStream = this.context.openFileOutput(BUFFER_FILE,this.context.MODE_APPEND);
            }
            else
            {
                fileOutputStream = this.context.openFileOutput(EXCEL_FILE,this.context.MODE_APPEND);
            }

            try {
                FileReader filereader = new FileReader(filer);

                BufferedReader bufferedReader = new BufferedReader(filereader);

                String nextline;

                String[] nextlinearr = new String[]{};

                int count = 1;

                while ((nextline = bufferedReader.readLine()) != null)
                {
                    nextlinearr = nextline.split(",");
                    if(nextlinearr.length == 0)
                    {
                        continue;
                    }
                    else
                    {
                        map.put(count+"",nextline);
                        count++;
                    }
                }

            }
            catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(this.context,e.toString()+" add",Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,"Error in Accessing Data",Toast.LENGTH_LONG).show();
        }

        return map;

    }

    public void delete_buffer()
    {
        HashMap<String,String> map = new HashMap<>();

        try
        {
            File filer = new File(this.context.getFilesDir().toString()+"/"+this.BUFFER_FILE);
            if(!filer.exists())
            {
                fileOutputStream = this.context.openFileOutput(BUFFER_FILE,this.context.MODE_APPEND);
            }
            else
            {
                fileOutputStream = this.context.openFileOutput(EXCEL_FILE,this.context.MODE_APPEND);
            }
            fileOutputStream.write("".getBytes());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,"Error in Accessing Data",Toast.LENGTH_LONG).show();
        }

    }

}