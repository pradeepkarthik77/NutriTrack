package com.example.calorietracker;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import java.util.List;

public class NutrtionValueSet
{
    private Context context;
    private String item_id;
    private LoadTheDatabase loadTheDatabase;
    private List<String> item_values;
    private int[] item_res_ids;


    public NutrtionValueSet(Context context,String item_id)
    {
        this.context = context;
        this.item_id = item_id;
    }

    public List<String> setvalues()
    {
        loadTheDatabase = new LoadTheDatabase(context);
        loadTheDatabase.setValues();

        this.item_values = loadTheDatabase.get_nutrition(this.item_id);

        item_res_ids = new int[]{R.id.serving_size,R.id.calories,R.id.fat,R.id.saturated_fat,R.id.trans_fat,R.id.cholesterol,R.id.sodium,R.id.carbohydrates,R.id.dietary_fiber,R.id.sugar,R.id.added_sugar,R.id.protein,R.id.vitamin_D,R.id.calcium,R.id.iron,R.id.potassium,R.id.vitamin_A,R.id.vitamin_C,R.id.manganese,R.id.vitamin_K};

        TextView txtview;

        Activity activity = (Activity) this.context;

        txtview = activity.findViewById(R.id.serving_size);

        txtview.setText(txtview.getText().toString()+": "+this.item_values.get(3)+"g");

        txtview = activity.findViewById(R.id.calories);

        txtview.setText(txtview.getText().toString()+": "+this.item_values.get(4));

        int indx = 2;

        for(int i=5;i<23;i++)
        {
            txtview = activity.findViewById(item_res_ids[indx]);
            txtview.setText(this.item_values.get(i)+"%");
            indx++;
        }

        return this.item_values;

    }
}
