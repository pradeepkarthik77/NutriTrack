package com.example.calorietracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Edit_User_Profile extends AppCompatActivity
{

    private SharedPreferences login_pref;
    private String name;
    private String email;
    private String gender;
    private int age;
    private int height;
    private int weight;
    private int activity_level;
    private int goal_calorie;
    private ProgressBar progressBar;
    private Context context = this;
    private SharedPreferences.Editor login_editor;

    protected void setdefaultvalues()
    {
        this.login_pref = getSharedPreferences("Login",0);
        this.login_editor = this.login_pref.edit();
        this.name = this.login_pref.getString("name","");
        this.email = this.login_pref.getString("email","");
        this.gender = this.login_pref.getString("Gender","");
        this.height = this.login_pref.getInt("Height",0);
        this.weight = this.login_pref.getInt("Weight",0);
        this.age = this.login_pref.getInt("Age",0);
        this.activity_level = this.login_pref.getInt("Activity_Level",0);
        this.goal_calorie = this.login_pref.getInt("Goal_Calorie",0);
    }

    public void showspinner()
    {
        progressBar = findViewById(R.id.edit_profile_progress);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hidespinner()
    {
        progressBar = findViewById(R.id.edit_profile_progress);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_edit_profile_layout);

        setdefaultvalues();

        TextView age_text = findViewById(R.id.edit_age_text);
        TextView gender_text = findViewById(R.id.edit_gender_text);
        TextView height_text = findViewById(R.id.edit_height_text);
        TextView weight_text = findViewById(R.id.edit_weight_text);
        TextView activity_level_text = findViewById(R.id.edit_lifestyle_text);
        TextView goal_calorie_text = findViewById(R.id.edit_calorie_text);
        TextView name_text = findViewById(R.id.edit_name_text);

        this.progressBar = findViewById(R.id.edit_profile_progress);

        SpannableStringBuilder ssb = new SpannableStringBuilder();
        ssb.append("Edit Your Gender:");
        int bulletcolor = Color.parseColor("#228B22");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        gender_text.setText(ssb);

        ssb.clear();
        ssb.append("Edit Your Name:");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        name_text.setText(ssb);


        ssb.clear();
        ssb.append("Edit Your Age:");
        bulletcolor = Color.parseColor("#228B22");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        age_text.setText(ssb);

        ssb.clear();
        ssb.append("Edit Your Height:");
        bulletcolor = Color.parseColor("#228B22");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        height_text.setText(ssb);

        ssb.clear();
        ssb.append("Edit Your Weight:");
        bulletcolor = Color.parseColor("#228B22");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        weight_text.setText(ssb);

        ssb.clear();
        ssb.append("Edit Your Activity Level:");
        bulletcolor = Color.parseColor("#228B22");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        activity_level_text.setText(ssb);

        ssb.clear();
        ssb.append("Edit Your Calorie Intake Goal:");
        bulletcolor = Color.parseColor("#228B22");
        ssb.setSpan(new CustomBulletSpan(bulletcolor),0,ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        goal_calorie_text.setText(ssb);

        NumberPicker weight_picker = findViewById(R.id.edit_weight_picker);
        NumberPicker height_picker = findViewById(R.id.edit_height_picker);
        NumberPicker age_picker = findViewById(R.id.edit_age_picker);
        NumberPicker calorie_picker = findViewById(R.id.edit_calorie_picker);
        RadioGroup gender_group = findViewById(R.id.edit_gender_radiogroup);

        weight_picker.setMaxValue(500);
        weight_picker.setMinValue(1);
        weight_picker.setValue(60);

        height_picker.setMaxValue(250);
        height_picker.setMinValue(50);
        height_picker.setValue(150);

        age_picker.setMaxValue(120);
        age_picker.setMinValue(1);
        age_picker.setValue(30);

        calorie_picker.setMaxValue(10000);
        calorie_picker.setMinValue(0);

        EditText name_edit = findViewById(R.id.edit_name_editor);

        name_edit.setText(this.name);

        age_picker.setValue(this.age);

        height_picker.setValue(this.height);

        weight_picker.setValue(this.weight);

        calorie_picker.setValue(this.goal_calorie);

        Spinner spinner = findViewById(R.id.edit_lifestyle_spinner);
        List<String> options = Arrays.asList("Sedentary", "Lightly active", "Moderately active","Very active","Extra Active");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        RadioButton malebtn = findViewById(R.id.male_gender);
        RadioButton femalebtn = findViewById(R.id.female_gender);

        if(this.gender.equals("Male"))
        {
            malebtn.setChecked(true);
        }
        else
        {
            femalebtn.setChecked(true);
        }
        spinner.setSelection(this.activity_level);

        ImageButton back_btn = findViewById(R.id.edit_profile_back_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton save_btn = findViewById(R.id.profile_save_btn);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer age = age_picker.getValue();
                Integer height = height_picker.getValue();
                Integer weight = weight_picker.getValue();
                Integer goal_calorie = calorie_picker.getValue();
                Integer activity_level = spinner.getSelectedItemPosition();
                String name = name_edit.getText().toString();
                String gender = "Male";

                if(gender_group.getCheckedRadioButtonId() == malebtn.getId())
                {
                    gender = "Male";
                }
                else
                {
                    gender = "Female";
                }
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Are You Sure?");
                alertDialog.setMessage("Do You want to update you profile data?");

                String finalGender = gender;
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String,String> map = new HashMap<>();

                                map.put("email",email);
                                map.put("name",name);
                                map.put("gender", finalGender);
                                map.put("age",age+"");
                                map.put("activity_level",activity_level+"");
                                map.put("goal_calorie",goal_calorie+"");
                                map.put("height",height+"");
                                map.put("weight",weight+"");

                                Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.BASE_URL)).addConverterFactory(GsonConverterFactory.create()).build();

                                RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                                class longthread extends Thread
                                {
                                    @Override
                                    public void run() {
                                        runOnUiThread(Edit_User_Profile.this::showspinner);
                                        Call<Void> call = retrofitInterface.executeeditprofile(map);

                                        call.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response)
                                            {
                                                if(response.code() == 200)
                                                {
                                                    Toast.makeText(getApplicationContext(), "Details Updated SuccessFully!!!", Toast.LENGTH_LONG).show();
                                                    finish();
                                                }
                                                else if(response.code() == 400)
                                                {
                                                    Toast.makeText(getApplicationContext(),"Unable to Update Values. Try Again",Toast.LENGTH_LONG).show();
                                                }
                                                runOnUiThread(Edit_User_Profile.this::hidespinner);
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Toast.makeText(getApplicationContext(),"Unable to Update Details.Check Your Internet Connection And Try Again.",Toast.LENGTH_LONG).show();
                                                runOnUiThread(Edit_User_Profile.this::hidespinner);

//                                    Intent newintent = new Intent(getApplicationContext(),Signup_details.class);
//                                    newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(newintent);
                                            }
                                        });

                                        //runOnUiThread(SignupActivity.this::hidespinner);
                                    }
                                }

                                new longthread().start();

                                login_editor.putString("email",email);
                                login_editor.putString("name",name);
                                login_editor.putString("Gender",finalGender);
                                login_editor.putInt("Age",age);
                                login_editor.putInt("Height",height);
                                login_editor.putInt("Weight", weight);
                                login_editor.putInt("Activity_Level",activity_level);
                                login_editor.putInt("Goal_Calorie",goal_calorie);
                                login_editor.commit();

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();


            }
        });

    }
}
