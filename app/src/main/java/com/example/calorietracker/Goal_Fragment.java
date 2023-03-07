package com.example.calorietracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.android.material.navigation.NavigationView;

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

public class Goal_Fragment extends Fragment {

    private Context context;
    private ActionBarDrawerToggle toggle;
    private ProgressBar progressBar;
    private AppCompatActivity activity;

    public Goal_Fragment(Context context)
    {
        this.context = context;
    }

    public void showspinner()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hidespinner()
    {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goal_fragment_layout, container, false);

        this.progressBar = view.findViewById(R.id.goal_progress);
        this.activity = (AppCompatActivity) context;

        Toolbar toolbar = view.findViewById(R.id.week_toolbar);
        TextView title_text = toolbar.findViewById(R.id.week_tool_title);
        title_text.setText("Your Goals");

        //set of lines for enabling Menu
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true); //to allow for sidebar
        activity.setSupportActionBar(toolbar);
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        DrawerLayout drawerLayout = view.findViewById(R.id.goal_drawer);
        this.toggle = new ActionBarDrawerToggle((Activity) context,drawerLayout,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //end of those set of line for menu

        TextView user_sidebar_name = header.findViewById(R.id.user_name_sidebar);
        TextView user_sidebar_email = header.findViewById(R.id.user_email_sidebar);
        RoundedLetterView user_profile_icon = header.findViewById(R.id.sidebar_user_profile);

        SharedPreferences pref = context.getSharedPreferences("Login",0);

        //set of instructions to set name,email in sidebar
        String name = pref.getString("name","");
        String email = pref.getString("email","");
        String firstletter = name.trim();
        if(!firstletter.equals(""))
        {
            firstletter = firstletter.charAt(0)+"";
            firstletter = firstletter.toUpperCase();
        }
        user_profile_icon.setTitleText(firstletter);
        user_sidebar_name.setText(name);
        user_sidebar_email.setText(email);
        //end of sidebar

        new Sidebar_Class().navigation_onclick(navigationView,context);

        ListView help_list = view.findViewById(R.id.goal_listview);

        Goal_ListView_Adapter goal_listView_adapter = new Goal_ListView_Adapter(context);

        help_list.setAdapter(goal_listView_adapter);

        help_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String list_type = "";
                String unit_type = "";

                switch(position)
                {
                    case 0: list_type = "Calorie";unit_type = "kcal";break;
                    case 1: list_type = "Water";unit_type = "ml";break;
                    case 2: list_type = "Carbohydrates";unit_type = "g";break;
                    case 3: list_type = "Protein";unit_type = "g";break;
                    case 4: list_type = "Fat";unit_type = "g";break;
                    case 5: list_type = "Fiber";unit_type = "g";break;
                    case 6: list_type = "Vitamin C";unit_type = "mg";break;
                    default: list_type = "Calorie";unit_type = "kcal";;break;
                }

                create_goal_dialog(list_type,unit_type);

            }
        });

        return view;
    }

    private void  create_goal_dialog(String list_type,String unit_type)
    {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.goal_setting_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        Button apply_btn = (Button) dialog.findViewById(R.id.apply_btn);
        Button cancel_btn = (Button) dialog.findViewById(R.id.cancel_btn);

        EditText quantity_value = dialog.findViewById(R.id.quantity_val);
        TextView quantity_unit = dialog.findViewById(R.id.quantity_unit);
        TextView goal_top_text = dialog.findViewById(R.id.goal_top_text);

        quantity_unit.setText(unit_type);
        goal_top_text.setText("Choose Your Daily "+list_type+" Goal:");

        //TODO add the logic to get the value from sharedpreferences the value of the goal and display the goal it here.

        SharedPreferences login_pref = context.getSharedPreferences("Login",0);

        SharedPreferences.Editor login_editor = login_pref.edit();

        switch(list_type)
        {
            case "Calorie": quantity_value.setText(login_pref.getInt("Goal_Calorie",0)+"");break;
            case "Water": quantity_value.setText(login_pref.getFloat("Goal_Water",0f)+"");break;
            case "Carbohydrates": quantity_value.setText(login_pref.getFloat("Goal_Carbs",0f)+"");break;
            case "Protein": quantity_value.setText(login_pref.getFloat("Goal_Protein",0f)+"");break;
            case "Fat": quantity_value.setText(login_pref.getFloat("Goal_Fat",0f)+"");break;
            case "Fiber": quantity_value.setText(login_pref.getFloat("Goal_Fiber",0f)+"");break;
            case "Vitamin C": quantity_value.setText(login_pref.getFloat("Goal_Vitamin_C",0f)+"");break;
            default: quantity_value.setText(login_pref.getInt("Goal_Calorie",0)+"");break;
        }

        String goal_value = quantity_value.getText().toString();

//        Toast.makeText(context,goal_value,Toast.LENGTH_LONG).show();
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        this.progressBar = dialog.findViewById(R.id.goal_progress);

        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                HashMap<String,String> map = new HashMap<>();

                map.put("email",login_pref.getString("email",""));

                String goal_value = quantity_value.getText().toString();

                if(list_type.equals("Calorie"))
                {
                    int temp_cal = Math.round(Float.parseFloat(goal_value));
                    login_editor.putInt("Goal_Calorie",temp_cal);
                    map.put("type","Goal_Calorie");
                    map.put("value",temp_cal+"");
                    login_editor.commit();
                }
                else if(list_type.equals("Water"))
                {
                    Float temp_water = Float.valueOf(decimalFormat.format(Float.parseFloat(goal_value)));
                    login_editor.putFloat("Goal_Water",temp_water);
                    login_editor.commit();
                    map.put("type","Goal_Water");
                    map.put("value",temp_water+"");
                }
                else if(list_type.equals("Carbohydrates"))
                {
                    Float temp_carb = Float.valueOf(decimalFormat.format(Float.parseFloat(goal_value)));
                    login_editor.putFloat("Goal_Carbs",temp_carb);
                    login_editor.commit();
                    map.put("type","Goal_Carbs");
                    map.put("value",temp_carb+"");
                }
                else if(list_type.equals("Protein"))
                {
                    Float temp_protein = Float.valueOf(decimalFormat.format(Float.parseFloat(goal_value)));
                    login_editor.putFloat("Goal_Protein",temp_protein);
                    login_editor.commit();
                    map.put("type","Goal_Protein");
                    map.put("value",temp_protein+"");
                }
                else if(list_type.equals("Fat"))
                {
                    Float temp_fat = Float.valueOf(decimalFormat.format(Float.parseFloat(goal_value)));
                    login_editor.putFloat("Goal_Fat",temp_fat);
                    login_editor.commit();
                    map.put("type","Goal_Fat");
                    map.put("value",temp_fat+"");
                }
                else if(list_type.equals("Fiber"))
                {
                    Float temp_fiber = Float.valueOf(decimalFormat.format(Float.parseFloat(goal_value)));
                    login_editor.putFloat("Goal_Fiber",temp_fiber);
                    login_editor.commit();
                    map.put("type","Goal_Fiber");
                    map.put("value",temp_fiber+"");
                }
                else
                {
                    Float temp_vitamin_c = Float.valueOf(decimalFormat.format(Float.parseFloat(goal_value)));
                    login_editor.putFloat("Goal_Vitamin_C",temp_vitamin_c);
                    login_editor.commit();
                    map.put("type","Goal_Vitamin_C");
                    map.put("value",temp_vitamin_c+"");
                }

                Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.BASE_URL)).addConverterFactory(GsonConverterFactory.create()).build();

                RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                class longthread extends Thread
                {
                    @Override
                    public void run() {
                        activity.runOnUiThread(Goal_Fragment.this::showspinner);
                        Call<Void> call = retrofitInterface.sendgoalactivity(map);

                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response)
                            {
                                if(response.code() == 200)
                                {
                                    Toast.makeText(context, "Details Updated SuccessFully!!!", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                                else if(response.code() == 400)
                                {
                                    Toast.makeText(context,"Unable to Update Values. Try Again",Toast.LENGTH_LONG).show();
                                }
                                activity.runOnUiThread(Goal_Fragment.this::hidespinner);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context,"Unable to Update Details.Check Your Internet Connection And Try Again.",Toast.LENGTH_LONG).show();
                                activity.runOnUiThread(Goal_Fragment.this::hidespinner);
                            }
                        });

                        //runOnUiThread(SignupActivity.this::hidespinner);
                    }
                }

                new longthread().start();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}