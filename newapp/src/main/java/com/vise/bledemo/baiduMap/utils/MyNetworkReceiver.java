package com.vise.bledemo.baiduMap.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class MyNetworkReceiver extends BroadcastReceiver {
    boolean isconnect = false; //判断是否连接网络

    String sconnect="初始化";

    @Override
    public void onReceive(Context context, Intent intent) {

        //获取网络连接服务
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        //判断wifi是否连接
        NetworkInfo.State state = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (state == NetworkInfo.State.CONNECTED){
            isconnect = true;
            sconnect="wifi连接";
            Toast.makeText(context , "wifi连接", Toast.LENGTH_LONG).show();
        }
        //判断GPRS是否连接
        NetworkInfo.State gstate = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (gstate == NetworkInfo.State.CONNECTED){
            isconnect = true;
            sconnect="当前正在使用数据流量";
            Toast.makeText(context , "当前正在使用数据流量", Toast.LENGTH_LONG).show();
        }
        //没网络连接
        if (!isconnect){
            sconnect="无网络连接";
            Toast.makeText(context , "无网络连接", Toast.LENGTH_LONG).show();
        }
    }

    public String getNetwork() {

        return sconnect;
    }


}

