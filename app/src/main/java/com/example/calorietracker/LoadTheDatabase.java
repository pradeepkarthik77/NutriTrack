package com.example.calorietracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Queue;

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

    public int returncount(String cardview_title)
    {
        this.sqLiteDatabase = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM user_nutrition WHERE item_type = '"+cardview_title+"' ";
            Cursor cursor = this.sqLiteDatabase.rawQuery(query, null);
            return cursor.getCount();
        }
        catch(Exception e)
        {
            Toast.makeText(this.context,"Error in Loading the Data",Toast.LENGTH_LONG);
            e.printStackTrace();
            return 0;
        }
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
