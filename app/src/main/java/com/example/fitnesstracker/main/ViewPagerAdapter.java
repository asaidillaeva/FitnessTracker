package com.example.fitnesstracker.main;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fitnesstracker.current.CurrentRunFragment;
import com.example.fitnesstracker.history.HistoryFragment;

import java.util.ArrayList;
import java.util.List;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HistoryFragment();
            case 1:
                return new CurrentRunFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

}
