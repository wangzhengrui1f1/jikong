package com.vise.bledemo.chezai.service;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyAdapter22 extends FragmentPagerAdapter {
    ArrayList<Fragment> list;
    public MyAdapter22(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }
    @Override
    public int getCount() {
        return list.size();
    }
}
