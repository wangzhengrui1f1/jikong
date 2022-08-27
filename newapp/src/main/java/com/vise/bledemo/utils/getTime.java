package com.vise.bledemo.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class getTime {
    //todo 加载时间
    public static String disposeTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(c.getTime());
        return time;
    }

    //todo 加载时间2
    public static String disposeTime2() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String time = sdf.format(c.getTime());
        return time;
    }

    //todo 加载时间2
    public static String disposeTime3() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = sdf.format(c.getTime());
        return time;
    }

    /**
     * 判断是否平板设备
     * @param context
     * @return true:平板,false:手机
     */
    @TargetApi(Build.VERSION_CODES.DONUT)
    public static boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String getdidian="未选择";
    public static int xianchen=-1;
    //todo 0可以报警 1报警  2报警冷却
    public static int guangbaojin=0;
}
