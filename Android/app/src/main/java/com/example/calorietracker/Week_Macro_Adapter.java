package com.example.calorietracker;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Week_Macro_Adapter extends FragmentStatePagerAdapter {

    private Context context;

    public Week_Macro_Adapter(FragmentManager fm,Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0: return new week_macro_carbs(context);
            case 1: return new week_macro_protein(context);
            case 2: return new week_macro_fat(context);
            case 3: return new week_macro_vitc(context);
            case 4: return new week_macro_fiber(context);
            default: return new week_macro_carbs(context);
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Carbs";
            case 1:
                return "Protein";
            case 2:
                return "Fat";
            case 3:
                return "Vit C";
            case 4:
                return "Fiber";
            default:
                return null;
        }
    }

}