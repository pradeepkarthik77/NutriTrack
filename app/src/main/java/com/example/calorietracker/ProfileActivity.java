package com.example.calorietracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity
{

    private String email_id;
    private String name;
    private Context context;
    private int age;
    private String gender;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        SharedPreferences pref = getSharedPreferences("Login",0);

        this.email_id = pref.getString("email","");
        this.name = pref.getString("name","");
        this.age = pref.getInt("Age",0);
        this.gender = pref.getString("Gender","gender");

        this.context = this;

        EditText profile_email = findViewById(R.id.profile_email_id);

        EditText profile_name = findViewById(R.id.profile_name);

        profile_email.setHint(this.email_id);

        profile_name.setHint(this.name);

        EditText profile_age = findViewById(R.id.profile_age);
        EditText profile_gender = findViewById(R.id.profile_gender);

        profile_age.setHint(this.age+" yrs");
        profile_gender.setHint(this.gender);

        ImageView back_btn = findViewById(R.id.display_back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button logout = findViewById(R.id.log_out_btn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Are You Sure?");

                builder.setMessage("Do You Want to Logout Of this App? All your Data, Local Files will be Lost.");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SharedPreferences preferences = getSharedPreferences("Login", 0);
                        preferences.edit().clear().commit();
                        Intent intent = new Intent(getApplicationContext(),OnOpenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertdialog = builder.create();

                alertdialog.show();
            }
        });

    }
}
