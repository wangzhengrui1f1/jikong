package com.vise.bledemo.otherView.view2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.vise.bledemo.Base.BaseActivity;
import com.vise.bledemo.R;

public class Test1Activity extends BaseActivity {
    TextView textView;
    private FragmentManager fragmentManager2 = null;
    private FragmentTransaction fragmentTransaction2 = null;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

    }
}