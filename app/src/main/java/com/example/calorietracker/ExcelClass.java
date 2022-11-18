package com.example.calorietracker;

import android.content.Context;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExcelClass
{

    private Map<String, List<String>> default_values;

    private File csvFile;

    private String APP_PATH;

    private Context context;

    private String EXCEL_FILE = "/favorites_list.csv";

    private CSVReader csvReader;

    private FileReader fileReader;

    public ExcelClass(Context context)
    {
        this.APP_PATH = this.context.getApplicationInfo().dataDir;
        this.csvFile = new File(APP_PATH+EXCEL_FILE);
        this.context = context;
        this.default_values.put("BreakFast", Arrays.asList("101","102","103"));
        this.default_values.put("Lunch", Arrays.asList("104","105","106"));
        this.default_values.put("Dinner", Arrays.asList("107","108","109"));
        this.default_values.put("Snacks", Arrays.asList("110","111","112"));
        this.default_values.put("Juices", Arrays.asList("113","114","115"));
        this.default_values.put("Water", Arrays.asList("116","117","118"));
        //TODO write logic to create a new Excelfile if not exisits and load values into it
    }

    public void copydefaults()
    {
        String data;
        try {
            FileOutputStream fileOutputStream = this.context.openFileOutput(APP_PATH + EXCEL_FILE, Context.MODE_APPEND);

            for(String key: this.default_values.keySet())
            {
                data = "";
                data+=key+",";
                data+= String.join(",",this.default_values.get(key))+"\n";
                fileOutputStream.write(data.getBytes());
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this.context,"Unable To Access Favorites",Toast.LENGTH_LONG);
            e.printStackTrace();
        }


    }

    public void create_excel(){
        if(!this.csvFile.exists())
        {
            //TODO write logic to create new file and write the default values into the files.
            try {
                this.csvFile.createNewFile();
                copydefaults();
            }
            catch(Exception e)
            {
                Toast.makeText(this.context,"Unable To Access Files",Toast.LENGTH_LONG);
                e.printStackTrace();
            }
        }
        try {
            this.fileReader = new FileReader(this.csvFile);
        }
        catch(Exception e)
        {
            Toast.makeText(this.context,"Unable To Access Files",Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }

    public String[] get_favorites(String cardview_title) {

        try {
            this.csvReader = new CSVReader(this.fileReader);

            String[] nextRecord;

            while ((nextRecord = csvReader.readNext()) != null)
            {
                if(nextRecord[0].equals(cardview_title))
                {
                    return nextRecord;
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this.context,"Unable To Access Files",Toast.LENGTH_LONG);
            e.printStackTrace();
        }

        return new String[]{};

    }
}
