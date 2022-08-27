package com.vise.bledemo.baiduMap.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import static android.content.ContentValues.TAG;

public class PublicMethod {

    /**
     * 判断是否为平板 ture 是 false不是
     */
    public static boolean isPad(Context context) {
        boolean result = false;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        // 屏幕尺寸
        double screenInches = Math.sqrt(x + y);
        // 大于6尺寸则为Pad
        if (screenInches >= 6.0) {
            result = true;
        }
        Log.d(TAG, "isPad:" + result);
        return result;
    }


    public static boolean isWifi(Context mContext) {
        boolean result = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            result = true;
            return result;
        }
        Log.d(TAG, "isWifiaaa:" + result);
        return result;
    }

}
