package com.example.calorietracker;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Year_Macro_Adapter extends FragmentStatePagerAdapter {

    private Context context;

    public Year_Macro_Adapter(FragmentManager fm,Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0: return new year_macro_carbs(context);
            case 1: return new year_macro_protein(context);
            case 2: return new year_macro_fat(context);
            case 3: return new year_macro_vitc(context);
            case 4: return new year_macro_fiber(context);
            default: return new year_macro_carbs(context);
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