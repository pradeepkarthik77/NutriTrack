package com.example.calorietracker;

import android.content.Context;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ExcelClass
{

    private String[] default_cards;

    private List<List<String>> default_food_items;

    private File csvFile;

    private String APP_PATH;

    private Context context;

    private String EXCEL_FILE = "favorites_list.csv";

    private CSVReader csvReader;

    private FileReader fileReader;

    private File thisFile;

    public ExcelClass(Context context)
    {
        this.context = context;
        this.APP_PATH = this.context.getApplicationInfo().dataDir;
        this.thisFile = this.context.getFilesDir();
        this.csvFile = new File(this.thisFile,this.EXCEL_FILE);
        this.default_cards = new String[]{"BreakFast","Lunch","Dinner","Snacks","Juices","Water"};
        //TODO write logic to create a new Excelfile if not exisits and load values into it
    }

    public List<String> get_defaults(String cardview_title)
    {
        int position = Arrays.asList(this.default_cards).indexOf(cardview_title);
        return this.default_food_items.get(position);
    }

    public void copydefaults() {
        String data = "";

        for(int i=0;i<6;i++)
        {
            data+=this.default_cards[i]+",\n";
            //data+= String.join(",",this.default_food_items.get(i))+"\n";
        }

        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(this.csvFile);
            fileOutputStream.write(data.getBytes());
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,"Cannot write data into files",Toast.LENGTH_LONG);
        }

    }


    public void add_to_favorites(String add_item,String cardview_item)
    {
        //write logic to append a value to the .csv file where arr[0] == cardview_name
        String nextline;
        String[] nextlinearr;

        String totaldata = "";

        int position = Arrays.asList(this.default_cards).indexOf(cardview_item);
//
//        if(position != -1)
//        {
//            if(this.default_food_items.get(position).contains(add_item))
//            {
//                return;
//            }
//        }

        try {

            this.fileReader = new FileReader(this.csvFile);

            BufferedReader bufferedReader = new BufferedReader(this.fileReader);

            while ((nextline = bufferedReader.readLine()) != null)
            {
                nextlinearr = nextline.split(",");
                if(nextlinearr.length == 0)
                {
                    continue;
                }
                else
                {
                    if(nextlinearr[0].equals(cardview_item))
                    {
                        if(!Arrays.asList(nextlinearr).contains(add_item))
                        {
                            nextline = String.join(",", nextlinearr) + "," + add_item + "\n";
                            totaldata += nextline;
                        }
                    }
                    else
                    {
                        totaldata+=nextline+"\n";
                    }
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(this.csvFile);
            fileOutputStream.write(totaldata.getBytes());

        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,e.toString()+" add",Toast.LENGTH_LONG).show();
        }
    }

    public void remove_from_favorites(String remove_item,String cardview_item)
    {
        String nextline;
        String[] nextlinearr;

        String totaldata = "";

        int position = Arrays.asList(this.default_cards).indexOf(cardview_item);

//        if(position != -1)
//        {
//            if(this.default_food_items.get(position).contains(remove_item))
//            {
//                return;
//            }
//        }

        try {

            this.fileReader = new FileReader(this.csvFile);

            BufferedReader bufferedReader = new BufferedReader(this.fileReader);

            while ((nextline = bufferedReader.readLine()) != null)
            {

                nextlinearr = nextline.split(",");
                if(nextlinearr.length == 0)
                {
                    Toast.makeText(context,String.join("& ",nextlinearr),Toast.LENGTH_LONG).show();
                    continue;
                }
                else
                {
                    if(nextlinearr[0].equals(cardview_item))
                    {
                        if(Arrays.asList(nextlinearr).contains(remove_item))
                        {
                            List<String> arr = new LinkedList<String>(Arrays.asList(nextlinearr));
                            arr.remove(remove_item);
//                            if(arr.size() == 1)
//                            {
//                                totaldata += nextlinearr[0]+",\n";
//                            }
                            totaldata += String.join(",",arr)+"\n";
                        }
                    }
                    else
                    {
                        totaldata+=nextline+"\n";
                    }
                }
            }

            FileOutputStream fileOutputStream = new FileOutputStream(this.csvFile);
            fileOutputStream.write(totaldata.getBytes());

        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,e.toString()+" remove",Toast.LENGTH_LONG).show();
        }
    }

    public void create_excel()
    {
        if(!this.csvFile.exists())
        {
            //TODO write logic to create new file and write the default values into the files.
            try {
                this.csvFile.createNewFile();
                copydefaults();
            }
            catch(Exception e)
            {
                Toast.makeText(this.context,"Unable to Access Files 1",Toast.LENGTH_LONG).show();
            }
        }
    }

    public String[] get_favorites(String cardview_title)
    {
        String[] returnrecord;

        String nextline;

        String[] nextlinearr;

        try {

            this.fileReader = new FileReader(this.csvFile);

            BufferedReader bufferedReader = new BufferedReader(this.fileReader);

            while ((nextline = bufferedReader.readLine()) != null)
            {
                nextlinearr = nextline.split(",");
                if(nextlinearr.length == 0)
                {
                    continue;
                }
                else
                {
                    if(nextlinearr[0].equals(cardview_title))
                    {
                        returnrecord = Arrays.copyOfRange(nextlinearr,1,nextlinearr.length);
                        String temp;
                        int i=0;
                        int j = returnrecord.length-1;
                        while(i<j)
                        {
                            temp = returnrecord[i];
                            returnrecord[i] = returnrecord[j];
                            returnrecord[j] = temp;
                            i++;
                            j--;
                        }
                        return returnrecord;
                    }
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,e.toString()+" get",Toast.LENGTH_SHORT).show();
        }

        return new String[]{};

    }
}
