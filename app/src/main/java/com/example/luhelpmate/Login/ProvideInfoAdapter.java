package com.example.luhelpmate.Login;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.luhelpmate.Login.StudentFragment;
import com.example.luhelpmate.Login.TeacherFragment;

public class ProvideInfoAdapter extends FragmentPagerAdapter {

    private Context context;
    private int totalTabs;

    public ProvideInfoAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context=context;
        this.totalTabs=totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TeacherFragment teacherFragment = new TeacherFragment();
                return teacherFragment;
            case 1:
                StudentFragment studentFragment = new StudentFragment();
                return studentFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
