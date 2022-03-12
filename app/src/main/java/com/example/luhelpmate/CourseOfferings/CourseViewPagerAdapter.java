package com.example.luhelpmate.CourseOfferings;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.luhelpmate.Routine.Fri_Fragment;
import com.example.luhelpmate.Routine.Mon_Fragment;
import com.example.luhelpmate.Routine.Sat_Fragment;
import com.example.luhelpmate.Routine.Sun_Fragment;
import com.example.luhelpmate.Routine.Thu_Fragment;
import com.example.luhelpmate.Routine.Tue_Fragment;
import com.example.luhelpmate.Routine.Wed_Fragment;

public class CourseViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public CourseViewPagerAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                First_Fragment firstFragment = new First_Fragment();
                return firstFragment;
            case 1:
                Second_Fragment secondFragment = new Second_Fragment();
                return secondFragment;
            case 2:
                Third_Fragment thirdFragment = new Third_Fragment();
                return thirdFragment;
            case 3:
                Fourth_Fragment fourthFragment = new Fourth_Fragment();
                return fourthFragment;
            case 4:
                Fifth_Fragment fifthFragment = new Fifth_Fragment();
                return fifthFragment;
            case 5:
                Sixth_Fragment sixthFragment = new Sixth_Fragment();
                return sixthFragment;
            case 6:
                Seventh_Fragment seventhFragment = new Seventh_Fragment();
                return seventhFragment;
            case 7:
                Eighth_Fragment eighthFragment = new Eighth_Fragment();
                return eighthFragment;
            case 8:
                Ninth_Fragment ninthFragment = new Ninth_Fragment();
                return ninthFragment;
            case 9:
                Tenth_Fragment tenth_fragment = new Tenth_Fragment();
                return tenth_fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
