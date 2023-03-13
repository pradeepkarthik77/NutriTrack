package com.example.calorietracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Settings_ListView_Adapter extends BaseAdapter
{
    private ArrayList<String> string_array = new ArrayList<String>();
    private ArrayList<Integer> image_array = new ArrayList<>();
    private Context context;
    private Integer ITEM_COUNT = 5;

    public Settings_ListView_Adapter(Context context)
    {
        this.context = context;

        this.string_array.add("");
        this.string_array.add("How to Use this App?");
        this.string_array.add("Report a Bug");
        this.string_array.add("Edit Your Profile");
        this.string_array.add("Logout");

        this.image_array.add(R.drawable.data_share_btn);
        this.image_array.add(R.drawable.help_green);
        this.image_array.add(R.drawable.bug_green);
        this.image_array.add(R.drawable.person_green);
        this.image_array.add(R.drawable.logout_green);


        this.ITEM_COUNT = this.string_array.size();
    }


    @Override
    public int getCount() {
        return ITEM_COUNT;
    }

    @Override
    public Object getItem(int position) {
        return string_array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.settings_user_choice_list, parent, false);
            TextView item_text = convertView.findViewById(R.id.help_item_text);
            ImageView item_image = convertView.findViewById(R.id.help_item_icon);
            item_text.setText(this.string_array.get(position));
            item_image.setImageDrawable(context.getDrawable(this.image_array.get(position)));

            SwitchMaterial toggleSwitch = convertView.findViewById(R.id.toggle_switch);

            SharedPreferences login_pref = context.getSharedPreferences("Login",0);
            SharedPreferences.Editor editor = login_pref.edit();

            // Set the state of the ToggleSwitch
            toggleSwitch.setChecked(login_pref.getBoolean("isShare",true));

            toggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    // Do something when the state of the ToggleSwitch changes

                    boolean toset = isChecked;
                    editor.putBoolean("isShare",isChecked);
                    editor.commit();
                }
            });
        }
        else{
            convertView = LayoutInflater.from(context).inflate(R.layout.help_list_item, parent, false);
            TextView item_text = convertView.findViewById(R.id.help_item_text);
            ImageView item_image = convertView.findViewById(R.id.help_item_icon);
            item_text.setText(this.string_array.get(position));
            item_image.setImageDrawable(context.getDrawable(this.image_array.get(position)));

        }

        return convertView;
    }
}
