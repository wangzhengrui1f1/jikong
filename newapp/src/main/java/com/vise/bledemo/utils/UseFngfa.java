package com.vise.bledemo.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;

public class UseFngfa {
    //todo 关闭键盘
    public static void closejianpan(Context context, RadioButton radioButton){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(radioButton.getWindowToken(), 0); //强制隐藏键盘
    }

    public static void closejianpan2(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }

    //todo 关闭键盘
    public static void openjianpan(Context context, View view){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
    }


}
