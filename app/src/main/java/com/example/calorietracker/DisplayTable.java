package com.example.calorietracker;

import static android.icu.lang.UProperty.INT_START;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public static String chosen_date="";
    private String email = "";
    private String user_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_table);

        SharedPreferences pref = getSharedPreferences("Login",0);

        this.email = pref.getString("email","");

        this.user_name = pref.getString("name","");

        Intent intent = getIntent();

        this.item_title = intent.getStringExtra("item_name");
        this.item_id = intent.getStringExtra("item_id");
        this.item_type = intent.getStringExtra("item_type");

        SharedPreferences date_pref = getSharedPreferences("date",0);

        SimpleDateFormat formatter = new SimpleDateFormat();
        String today_date = formatter.format(new Date());

        this.chosen_date = date_pref.getString("chosen_date",today_date);

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

        floatingActionButton = findViewById(R.id.add_nutrition);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_dialogbox();
            }
        });

        ScrollView scrollView = findViewById(R.id.scrollview);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if(scrollY > oldScrollY)
                    {
                        floatingActionButton.setVisibility(View.GONE);
                    }
                    else
                    {
                        floatingActionButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }

    private void  create_dialogbox()
    {
        InsertCSV insertCSV = new InsertCSV(this);
        LoadTheDatabase loadTheDatabase = new LoadTheDatabase(this);
        List<String> item_values = loadTheDatabase.get_nutrition(this.item_id);
        List<String> one_item_values = new ArrayList<String>();
        one_item_values.addAll(item_values);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.save_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button apply_btn = (Button) dialog.findViewById(R.id.apply_btn);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);

        TextView date = dialog.findViewById(R.id.dialog_date);
        TextView calories = dialog.findViewById(R.id.dialog_calories);
        TextView protein = dialog.findViewById(R.id.dialog_protein);
        TextView fat = dialog.findViewById(R.id.dialog_fat);
        TextView carbs = dialog.findViewById(R.id.dialog_carbs);
        TextView name = dialog.findViewById(R.id.dialog_name);
        TextView type = dialog.findViewById(R.id.dialog_type);
        TextView item_unit = dialog.findViewById(R.id.quantity_val);

        EditText quantity_value = dialog.findViewById(R.id.quantity_val);
        TextView quantity_unit = dialog.findViewById(R.id.quantity_unit);

        quantity_unit.setText(one_item_values.get(one_item_values.size()-1));

        float serve_size = 1.0f;

        if(quantity_unit.getText().toString().equals("g"))
        {

            try
            {
                serve_size = Float.parseFloat(item_values.get(2));
            }
            catch(Exception e)
            {
                serve_size = 1.0f;
            }

            quantity_value.setText(Math.round(serve_size)+"");

            float x = 0.0f;

            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            for(int i=0;i<one_item_values.size();i++)
            {
                try
                {
                    x = Float.parseFloat(item_values.get(i));
                    x = Float.parseFloat(decimalFormat.format(x/serve_size));
                    one_item_values.set(i,(x)+"");
                }
                catch(Exception e)
                {
//                        Toast.makeText(context,item_values.get(i),Toast.LENGTH_SHORT).show();
                    System.out.println(item_values.get(i));
                }
            }

//            quantity_value.setText(serve_size+"");

        }
        else
        {
        }

        quantity_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                int value = 1;
                if(!s.toString().equals("")) {
                    value = Integer.parseInt(s.toString());
                }
                else
                {
                    value = 1;
                }

                float x;

                DecimalFormat decimalFormat = new DecimalFormat("#.##");

                for(int i=0;i<one_item_values.size();i++)
                {
                    try
                    {
                        x = Float.parseFloat(one_item_values.get(i));
                        x = Float.parseFloat(decimalFormat.format(x*value));
                        item_values.set(i,(x)+"");
                    }
                    catch(Exception e)
                    {
//                        Toast.makeText(context,item_values.get(i),Toast.LENGTH_SHORT).show();
                        System.out.println(item_values.get(i));
                    }
                }

                String str;

                str = "<b>Calories: </b>"+item_values.get(3)+"g";
                calories.setText(Html.fromHtml(str));

                str = "<b>Protein: </b>"+item_values.get(13)+"g";
                protein.setText(Html.fromHtml(str));

                str = "<b>Carbohydrates: </b>"+item_values.get(9)+"g";
                carbs.setText(Html.fromHtml(str));

                str = "<b>Fat: </b>"+item_values.get(4)+"g";
                fat.setText(Html.fromHtml(str));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(chosen_date.equals(""))
        {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = new Date();
            chosen_date = formatter.format(date1);
            //Toast.makeText(getApplicationContext(),formatter.format(date),Toast.LENGTH_SHORT).show();
        }

        String str;

        float x=1.0f;

//        if(cardview_name.equals("Fruits"))
//        {
//            for(int i=0;i<item_values.size();i++)
//            {
//                try
//                {
//                    x = Float.parseFloat(item_values.get(i));
//                    item_values.set(i,(x*quantity_val)+"");
//                }
//                catch(Exception e)
//                {
//                    Toast.makeText(context,item_values.get(i),Toast.LENGTH_SHORT).show();
//                }
//            }
//        }

        str = "<b>Name: </b>"+this.item_title;
        name.setText(Html.fromHtml(str));

        str = "<b>Type: </b>"+this.item_type;
        type.setText(Html.fromHtml(str));

        str = "<b>Date: </b>"+chosen_date;
        date.setText(Html.fromHtml(str));

        str = "<b>Calories: </b>"+item_values.get(3)+"g";
        calories.setText(Html.fromHtml(str));

        str = "<b>Protein: </b>"+item_values.get(13)+"g";
        protein.setText(Html.fromHtml(str));

        str = "<b>Carbohydrates: </b>"+item_values.get(9)+"g";
        carbs.setText(Html.fromHtml(str));

        str = "<b>Fat: </b>"+item_values.get(4)+"g";
        fat.setText(Html.fromHtml(str));

        String BASE_URL = getString(R.string.BASE_URL);

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                //Toast.makeText(getApplicationContext(),chosen_date+" "+chosen_time,Toast.LENGTH_LONG).show();

                insertCSV.insert_into_csv(item_type,item_values,chosen_date);

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
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
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
