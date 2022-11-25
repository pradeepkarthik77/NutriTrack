package com.example.calorietracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DisplayTable extends AppCompatActivity
{
    private String item_title;
    private String item_id;
    private NutrtionValueSet nutrtionValueSet;
    private AlertDialog.Builder dialog;
    private FloatingActionButton floatingActionButton;
    private List<String> item_values;

    public static String chosen_time="";
    public static String chosen_date="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_table);

        Intent intent = getIntent();

        this.item_title = intent.getStringExtra("item_name");
        this.item_id = intent.getStringExtra("item_id");

        this.chosen_time = intent.getStringExtra("chosen_time");
        this.chosen_date = intent.getStringExtra("chosen_date");

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        TextView list_title = (TextView) findViewById(R.id.display_title);

        list_title.setText(this.item_title);

        //Toast.makeText(getApplicationContext(),this.chosen_date+" "+this.chosen_time,Toast.LENGTH_LONG).show();

        this.nutrtionValueSet = new NutrtionValueSet(this,this.item_id);

        this.item_values = this.nutrtionValueSet.setvalues();

        create_dialogbox();

        floatingActionButton = findViewById(R.id.add_nutrition);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });


    }

    public static void set_chosen_date(String chosen)
    {
        DisplayTable.chosen_date = chosen;
    }

    public static void set_chosen_time(String chosen)
    {
        DisplayTable.chosen_time = chosen;
    }

    private void create_dialogbox()
    {
        InsertCSV insertCSV = new InsertCSV(this);
        dialog = new AlertDialog.Builder(this);

        dialog.setTitle("Are You Sure?");
        dialog.setMessage("Do You Want to Save this Data?");
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO add logic to insert the value into the .csv file

                if(chosen_date.equals(""))
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    chosen_date = formatter.format(date);
                    //Toast.makeText(getApplicationContext(),formatter.format(date),Toast.LENGTH_SHORT).show();
                }

                if(chosen_time.equals(""))
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    Date date = new Date();
                    chosen_time = formatter.format(date);
                }

                //Toast.makeText(getApplicationContext(),chosen_date+" "+chosen_time,Toast.LENGTH_LONG).show();

                insertCSV.insert_into_csv(item_values,chosen_date,chosen_time);
//                chosen_time = getIntent().getStringExtra("chosen_time");
//                chosen_date = getIntent().getStringExtra("chosen_date");
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.create();
    }
}
