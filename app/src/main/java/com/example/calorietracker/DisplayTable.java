package com.example.calorietracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DisplayTable extends AppCompatActivity
{
    private String item_title;
    private String item_id;
    private NutrtionValueSet nutrtionValueSet;
    private AlertDialog.Builder dialog;
    private FloatingActionButton floatingActionButton;
    private List<String> item_values;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_table);

        Intent intent = getIntent();

        this.item_title = intent.getStringExtra("item_name");
        this.item_id = intent.getStringExtra("item_id");

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
                insertCSV.insert_into_csv(item_values);
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.create();
    }
}
