package com.example.calorietracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.TaskStackBuilder;

import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OnOpenActivity extends AppCompatActivity
{

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onopen_layout);

        this.context = this;

        Boolean loggedin = false;
        Boolean signedin = false;

        SharedPreferences pref = this.getSharedPreferences("Login",0);

        SharedPreferences login_pref = context.getSharedPreferences("Login",0);

        SharedPreferences.Editor login_editor = login_pref.edit();

        Float recommeded_water = (login_pref.getInt("Weight",60))*29.5735f;
        Float recommended_carbs = ((login_pref.getInt("Goal_Calorie",0))*0.5f)/4f;
        Float recommended_protein = (login_pref.getInt("Weight",60)*0.8f);
        Float recommended_fat = ((login_pref.getInt("Goal_Calorie",0))*0.3f)/9f;
        Float recommended_fiber = ((login_pref.getInt("Goal_Calorie",0))*0.014f);
        Float recommended_vitamin_C = 0f;

        String gender = login_pref.getString("Gender","Male");
        Integer age = login_pref.getInt("Age",30);

        if(age < 3)
        {
            recommended_vitamin_C = 15f;
        }
        else if(age>=4 && age<=8)
        {
            recommended_vitamin_C = 25f;
        }
        else if(age>8 && age<=13)
        {
            recommended_vitamin_C = 45f;
        }
        else if(age >13 && age<=17)
        {
            recommended_vitamin_C = 70f;
        }
        else
        {
            if(gender.equals("Male"))
            {
                recommended_vitamin_C = 90f;
            }
            else
            {
                recommended_vitamin_C = 75f;
            }
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        login_editor.putFloat("Goal_Water",Float.parseFloat(decimalFormat.format(recommeded_water)));
        login_editor.putFloat("Goal_Carbs",Float.parseFloat(decimalFormat.format(recommended_carbs)));
        login_editor.putFloat("Goal_Protein",Float.parseFloat(decimalFormat.format(recommended_protein)));
        login_editor.putFloat("Goal_Fat",Float.parseFloat(decimalFormat.format(recommended_fat)));
        login_editor.putFloat("Goal_Fiber",Float.parseFloat(decimalFormat.format(recommended_fiber)));
        login_editor.putFloat("Goal_Vitamin_C",Float.parseFloat(decimalFormat.format(recommended_vitamin_C)));

        login_editor.commit();

        SharedPreferences.Editor editor = pref.edit();
        loggedin = pref.getBoolean("isLoggedin",false);
        signedin = pref.getBoolean("isSignedin",false);

        SharedPreferences date_pref = this.getSharedPreferences("date",0);
        SharedPreferences.Editor edit = date_pref.edit();

        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        String chosen_date = dateFormat.format(date);
        edit.putString("chosen_date",chosen_date);
        edit.commit();

        if(loggedin)
        {
            Intent newintent = new Intent(context,HomeActivity_holder.class);
            newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(newintent);
        }

        else if(signedin)
        {
            Intent newintent = new Intent(context,Signup_details.class);
            newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(newintent);
        }
        else
        {
        }

        //ImageView imageView = findViewById(R.id.onopen_image);

        //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES); //to set the Mode manually

        TextView textView = findViewById(R.id.welcome_calorie);

        //TextView descView = findViewById(R.id.onopen_desc);

        String string = textView.getText().toString();

        //String descstr = descView.getText().toString();

        SpannableString string1 = new SpannableString(string);
        string1.setSpan(new UnderlineSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        string1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_green)), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        SpannableString desc = new SpannableString(descstr);
//
//        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_green)),14,18,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_green)),23,32,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_green)),38,42,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        desc.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.dark_green)),49,53,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        //descView.setText(desc);

        textView.setText(string1);

        MaterialButton log_in_btn = findViewById(R.id.log_in_btn);

        this.context = this;

        log_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,LoginActivity.class);
                context.startActivity(intent);
            }
        });

        MaterialButton signup_btn  =  findViewById(R.id.sign_in_btn);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(context,SignupActivity.class);
                context.startActivity(intent1);
            }
        });
    }
}