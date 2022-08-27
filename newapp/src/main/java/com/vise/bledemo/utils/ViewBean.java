package com.vise.bledemo.utils;

import android.view.View;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;

import java.util.ArrayList;
import java.util.List;

public class ViewBean {
    //开路 蓝牙关闭 手持
    public static View closeblueview;
    //手持 蓝牙关闭 开路
    public static View closeblueview2;

    //开路-蓝牙关闭弹窗
    public static String lanyaClose2 = "无状态";
    public static String lanyaName2 ="无";

    //手持-蓝牙关闭弹窗
    public static String lanyaClose = "无状态";
    public static String lanyaName ="无";

    //标定开路
    public static int startBiaodingKailu = 0;
    public static int stopUpdate = 0;
    public static List<PoiInfo> poiInfoListForGeoCoder2 = new ArrayList<>();//地理反向解析获取的poi

    public static LatLng currentLatLng2;//当前所在位置

    public static BaiduMap mAllBaiduMap = null;

    //车载 视频name
    public static String SPFilename="no";

    public static View tname,tname3;
}
