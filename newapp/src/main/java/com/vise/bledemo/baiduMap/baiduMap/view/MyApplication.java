package com.vise.bledemo.baiduMap.baiduMap.view;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import java.lang.reflect.Method;

import static android.os.Build.VERSION.SDK_INT;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SDKInitializer.initialize(MyApplication.this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}
