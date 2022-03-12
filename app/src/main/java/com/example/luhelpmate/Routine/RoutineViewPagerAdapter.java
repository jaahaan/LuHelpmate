package com.example.luhelpmate.Routine;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.luhelpmate.Login.StudentFragment;
import com.example.luhelpmate.Login.TeacherFragment;

import java.util.ArrayList;

public class RoutineViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public RoutineViewPagerAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Sun_Fragment sunFragment = new Sun_Fragment();
                return sunFragment;
            case 1:
                Mon_Fragment monFragment = new Mon_Fragment();
                return monFragment;
            case 2:
                Tue_Fragment tueFragment = new Tue_Fragment();
                return tueFragment;
            case 3:
                Wed_Fragment wedFragment = new Wed_Fragment();
                return wedFragment;
            case 4:
                Thu_Fragment thuFragment = new Thu_Fragment();
                return thuFragment;
            case 5:
                Fri_Fragment friFragment = new Fri_Fragment();
                return friFragment;
            case 6:
                Sat_Fragment satFragment = new Sat_Fragment();
                return satFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
