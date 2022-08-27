package com.vise.bledemo.baiduMap.baiduMap.adapter;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Display;
import android.view.WindowManager;

public class helps {
    public static int ScreenOrient(Activity activity)
    {
        int orient = activity.getRequestedOrientation();
        if(orient != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && orient != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            WindowManager windowManager = activity.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            int screenWidth  = display.getWidth();
            int screenHeight = display.getHeight();
            orient = screenWidth < screenHeight ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        return orient;
    }

}
