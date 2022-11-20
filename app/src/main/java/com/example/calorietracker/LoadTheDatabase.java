package com.example.calorietracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class LoadTheDatabase extends SQLiteOpenHelper
{
    private static String DB_NAME = "nutrition_values";
    private static String DB_PATH = "/databases/"+DB_NAME;
    private static String APP_DATA_PATH = "";
    private static String database_name = "user_nutrition";
    private Context context;

    private SQLiteDatabase sqLiteDatabase;

    public LoadTheDatabase(@Nullable Context context)
    {
        super(context, DB_NAME, null, 3);// 1? Its database Version
        this.context = context;
        APP_DATA_PATH = this.context.getApplicationInfo().dataDir;
    }

    public int get_count(String cardview_title)
    {
        this.sqLiteDatabase = this.getReadableDatabase();
        try {
            Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT * FROM "+database_name+" where item_type = '"+cardview_title+"' ", null);
            return cursor.getCount();
        }
        catch(Exception e)
        {
            Toast.makeText(this.context,"Unable to Load Data",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return 0;
        }
    }

    public boolean checkDataBase(String path)
    {
        SQLiteDatabase checkit = null;

        try
        {
            checkit = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
        }
        catch(Exception e)
        {

        }

        if(checkit!=null)
        {
            checkit.close();
        }

        return checkit != null ? true : false;

    }

    public void add_liked(String item_id)
    {
        try
        {
            this.sqLiteDatabase = this.getWritableDatabase();
            this.sqLiteDatabase.execSQL("UPDATE "+database_name+" SET isLiked = '1' WHERE item_id = '"+item_id+"'");
            Toast.makeText(this.context,"Added to Favorites",Toast.LENGTH_SHORT).show();

        }
        catch(Exception e)
        {
            Toast.makeText(this.context,"Cannot add to Favorites",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void remove_liked(String item_id)
    {
        try
        {
            this.sqLiteDatabase = this.getWritableDatabase();
            this.sqLiteDatabase.execSQL("UPDATE "+database_name+" SET isLiked = '0' WHERE item_id = '"+item_id+"'");
            Toast.makeText(this.context,"Remove from Favorites",Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Toast.makeText(this.context,"Cannot Remove from Favorites",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void copyDatabase(String path) throws IOException
    {
        InputStream myInput = this.context.getAssets().open("nutrition_values");

        OutputStream myOutput = new FileOutputStream(path);

        byte[] buffer = new byte[1024];

        int length;

        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void setValues()
    {

        String mpath = APP_DATA_PATH+DB_PATH;

        boolean dbexists = checkDataBase(mpath);

        if(!dbexists)
        {
            this.getWritableDatabase();

            try {
                copyDatabase(mpath);
            }
            catch(Exception e)
            {
                Toast.makeText(context.getApplicationContext(),"Error in Loading data",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public String[] get_unFavorites(String cardview_title,String[] favorite_list)
    {
        this.sqLiteDatabase = this.getReadableDatabase();

        List<String> item_values = new ArrayList<String>();
        String item;

        String querydata = "(";

        for(int i=0;i<favorite_list.length;i++)
        {
            if(i<favorite_list.length-1)
            {
                querydata += "'" + favorite_list[i]+ "', ";
            }
            else
            {
                querydata += "'"+favorite_list[i]+"'";
            }
        }

        querydata+=")";

        //Toast.makeText(this.context,querydata+" hello",Toast.LENGTH_LONG).show();
        //Toast.makeText(this.context,querydata+" hello",Toast.LENGTH_LONG).show();

        try
        {
            Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT item_id FROM "+database_name+" where item_id NOT IN "+querydata+" AND item_type='"+cardview_title+"'", null);

            int i;

            if(cursor.moveToFirst())
            {
                do
                {
                    item = "";
                    item = cursor.getString(0);
                    item_values.add(item);
                }while(cursor.moveToNext());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,e.toString(),Toast.LENGTH_LONG).show();
        }
        String[] returnarr = item_values.toArray(new String[item_values.size()]);

        //Toast.makeText(this.context,String.join(",",returnarr),Toast.LENGTH_LONG).show();

        return returnarr;
    }

    public List<List<String>> get_smaller_card_values(String[] favorite_list,String[] unfavorite_list,int counter,boolean isNotMain)
    {
        this.sqLiteDatabase = this.getReadableDatabase();

        List<List<String>> item_values = new ArrayList<List<String>>();
        List<String> item;

        String querydata = "(";

        for(int i=0;i<favorite_list.length;i++)
        {
            if(i<favorite_list.length-1)
            {
                querydata += "'" + favorite_list[i]+ "', ";
            }
            else
            {
                querydata += "'"+favorite_list[i]+"'";
            }
        }

        if(isNotMain)
        {
            querydata+=", ";
            for(int i=0;i<unfavorite_list.length;i++)
            {
                if(i<unfavorite_list.length-1)
                {
                    querydata += "'" + unfavorite_list[i]+ "', ";
                }
                else
                {
                    querydata += "'"+unfavorite_list[i]+"'";
                }
            }
        }

        querydata+=")";

        //Toast.makeText(this.context,querydata+" hello",Toast.LENGTH_SHORT).show();

        try
        {
            Cursor cursor = this.sqLiteDatabase.rawQuery("SELECT item_id,item_name,calories,isLiked,img_id FROM "+database_name+" where item_id IN "+querydata, null);

            int i;

            if(cursor.moveToFirst())
            {
                do
                {
                    item = new ArrayList<String>();
                    for(i=0;i<5;i++)
                    {
                        item.add(cursor.getString(i));
                    }
                    item_values.add(item);
                }while(cursor.moveToNext());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this.context,e.toString(),Toast.LENGTH_LONG).show();
            Toast.makeText(this.context,e.toString(),Toast.LENGTH_LONG).show();
        }
        return item_values;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
