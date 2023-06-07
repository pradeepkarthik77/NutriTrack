package com.example.calorietracker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Settings_Activity extends AppCompatActivity
{
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity_layout);

        Toolbar toolbar = findViewById(R.id.list_toolbar);

        TextView title = toolbar.findViewById(R.id.display_title);

        title.setText("Settings");

        ListView settings_list = findViewById(R.id.settings_listview);

        Settings_ListView_Adapter settings_listView_adapter = new Settings_ListView_Adapter(this);

        settings_list.setAdapter(settings_listView_adapter);

        ImageButton imgbtn = findViewById(R.id.display_back_btn);

        this.context= (Context) this;

        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        settings_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch(position)
                {
                    case 1: intent = new Intent(context,HelpActivity.class);startActivity(intent);break;
                    case 2: create_report_a_bug(context);break;
                    case 3: intent = new Intent(context,Edit_User_Profile.class);startActivity(intent);break;
                    case 4: create_logout_dialog(context);break;
                }
            }
        });

    }

    public void create_logout_dialog(Context context)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Do You want to Logout");
        alertDialog.setMessage("You will be Logged Out. All Your Local Data Will Be Lost!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "LOGOUT",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clear_local_data(context);
                        Toast.makeText(context,"Logged Out SuccesFully",Toast.LENGTH_SHORT).show();
                        Intent newintent = new Intent(context,OnOpenActivity.class);
                        newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(newintent);
                    }
                });
        alertDialog.show();
    }

//    public void clear_user_data(Context context)
//    {
//        try {
//            // clearing app data using PackageManager
//            PackageManager packageManager = context.getPackageManager();
//            String packageName = context.getPackageName();
//            packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
//
//            // clearing app data using ActivityManager
//            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            if (activityManager != null) {
//                activityManager.clearApplicationUserData();
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public void clear_local_data(Context context)
    {
        SharedPreferences login_pref = context.getSharedPreferences("Login",0);
        SharedPreferences date_pref = context.getSharedPreferences("date",0);

        SharedPreferences.Editor login_editor = login_pref.edit();
        SharedPreferences.Editor date_editor = date_pref.edit();

        login_editor.clear();
        login_editor.commit();

        date_editor.clear();
        date_editor.commit();

        File directory = context.getFilesDir();

        File[] files = directory.listFiles();
        for(File file: files)
        {
            file.delete();
        }
    }

    private void create_report_a_bug(Context context)
    {
        //TODO: Display the items consumed during the date and that card_title and the cumulative calories,protein,fat,carbohydrates,fat
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.report_a_bug_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        EditText report_edit = dialog.findViewById(R.id.report_bug_editext);
        MaterialButton report_btn = dialog.findViewById(R.id.report_btn);
        MaterialButton cancel_btn = dialog.findViewById(R.id.cancel_btn);

        SharedPreferences pref = context.getSharedPreferences("Login",0);

        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = report_edit.getText().toString();
                String email = pref.getString("email","");

                HashMap<String,String> map = new HashMap<>();

                map.put("description",description);
                map.put("email",email);

                Retrofit retrofit = new Retrofit.Builder().baseUrl(context.getString(R.string.BASE_URL)).addConverterFactory(GsonConverterFactory.create()).build();

                RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                Call<Void> call = retrofitInterface.reportabug(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response)
                    {
                        if(response.code() == 200)
                        {
                            Toast.makeText(context, "Bug Reported Successfully", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                        else if(response.code() == 400)
                        {
                            Toast.makeText(context,"Unable to Report Your Bug. Try Again",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context,"Unable to Send Your Bug.Check Your Internet Connection And Try Again.",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();

    }
}
