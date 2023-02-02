package com.example.calorietracker;

import static android.icu.lang.UProperty.INT_START;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayTable extends AppCompatActivity
{
    private String item_title;
    private String item_id;
    private NutrtionValueSet nutrtionValueSet;
    private Dialog dialog;
    private FloatingActionButton floatingActionButton;
    private List<String> item_values;
    private String item_type;
    public static String chosen_time="";
    public static String chosen_date="";
    private String email = "";
    private String user_name = "";
    private String age = "";
    private String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_table);

        SharedPreferences pref = getSharedPreferences("Login",0);

        this.email = pref.getString("email","");

        this.user_name = pref.getString("name","");

        this.age = pref.getString("age","");

        this.gender  = pref.getString("gender","");

        Intent intent = getIntent();

        this.item_title = intent.getStringExtra("item_name");
        this.item_id = intent.getStringExtra("item_id");
        this.item_type = intent.getStringExtra("item_type");

        this.chosen_time = intent.getStringExtra("chosen_time");
        this.chosen_date = intent.getStringExtra("chosen_date");

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        TextView list_title = (TextView) findViewById(R.id.display_title);

        list_title.setText("Nutrition Facts - "+this.item_title);

        ImageButton display_btn = findViewById(R.id.display_back_btn);

        display_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Toast.makeText(getApplicationContext(),this.chosen_date+" "+this.chosen_time,Toast.LENGTH_LONG).show();

        this.nutrtionValueSet = new NutrtionValueSet(this,this.item_id);

        this.item_values = this.nutrtionValueSet.setvalues(item_type);

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
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.save_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button apply_btn = (Button) dialog.findViewById(R.id.apply_btn);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);

        TextView date = dialog.findViewById(R.id.dialog_date);
        TextView time = dialog.findViewById(R.id.dialog_time);
        TextView calories = dialog.findViewById(R.id.dialog_calories);
        TextView protein = dialog.findViewById(R.id.dialog_protein);
        TextView fat = dialog.findViewById(R.id.dialog_fat);
        TextView carbs = dialog.findViewById(R.id.dialog_carbs);
        TextView type = dialog.findViewById(R.id.dialog_type);

        if(chosen_date.equals(""))
        {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = new Date();
            chosen_date = formatter.format(date1);
            //Toast.makeText(getApplicationContext(),formatter.format(date),Toast.LENGTH_SHORT).show();
        }

        if(chosen_time.equals(""))
        {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date date1 = new Date();
            chosen_time = formatter.format(date1);
        }

        String str;

        TextView name = dialog.findViewById(R.id.dialog_name);

        str = "<b>Name: </b>"+item_title;
        name.setText(Html.fromHtml(str));

        str = "<b>Type: </b>"+item_type;
        type.setText(Html.fromHtml(str));

        str = "<b>Date: </b>"+chosen_date;
        date.setText(Html.fromHtml(str));

        str = "<b>Time: </b>"+chosen_time;
        time.setText(Html.fromHtml(str));

        str = "<b>Calories: </b>"+this.item_values.get(3);
        calories.setText(Html.fromHtml(str));

        str = "<b>Protein: </b>"+this.item_values.get(13);
        protein.setText(Html.fromHtml(str));

        str = "<b>Carbohydrates: </b>"+this.item_values.get(9);
        carbs.setText(Html.fromHtml(str));

        str = "<b>Fat: </b>"+this.item_values.get(4);
        fat.setText(Html.fromHtml(str));

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getApplicationContext(),String.join(",",item_values),Toast.LENGTH_LONG).show();

                insertCSV.insert_into_csv(item_type,item_values,chosen_date,chosen_time);

                String BASE_URL = getString(R.string.BASE_URL);

                if(isConnected())
                {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

                    RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                    //Toast.makeText(context,"Connected",Toast.LENGTH_SHORT).show();

                    HashMap<String,String> map = insertCSV.read_from_buffer();

                    //Toast.makeText(context,map.size()+"",Toast.LENGTH_SHORT).show();

                    map.put("size",map.size()+"");

                    map.put("email",email);
                    map.put("name",user_name);
                    map.put("age",age);
                    map.put("gender",gender);

                    Call<Void> call = retrofitInterface.executesend(map);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response)
                        {
                            if(response.code() == 200) {
                                insertCSV.delete_buffer();
                            }
                            //Toast.makeText(context,"Success ra",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            //Toast.makeText(context,"Failrue",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else
                {
                    //Toast.makeText(context,"Not Connected",Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
//                chosen_time = getIntent().getStringExtra("chosen_time");
//                chosen_date = getIntent().getStringExtra("chosen_date");
                finish();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.create();
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}
