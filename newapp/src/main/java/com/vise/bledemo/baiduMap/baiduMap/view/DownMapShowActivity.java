package com.vise.bledemo.baiduMap.baiduMap.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.FrameLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class DownMapShowActivity extends Activity implements MKOfflineMapListener {
    private MapView mMapView;
    FrameLayout layout;
    Double lat=11.11;
    Double lon=11.11;
    LatLng center;
    float zoom;
    MKOfflineMap mkOfflineMap;
    int cityId;
    BaiduMap baiduMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //调用离线地图，否则第二次进入会加载不到
        mkOfflineMap=new MKOfflineMap();
        mkOfflineMap.init(this);

        MapStatus.Builder builder = new MapStatus.Builder();

        Intent intent = getIntent();
//        if (null != intent) {
//            Bundle bundle=intent.getExtras();
//            String latstr=bundle.getString("lat");
//            lat=Double.valueOf(latstr);
//            String lonstr=bundle.getString("lon");
//            lon=Double.valueOf(lonstr);
//            Log.e("lon",lon+"");
//        }
       // center = new LatLng(lat,lon);
      //  zoom = 13.0f;
      //  builder.target(center).zoom(zoom);

        setMapCustomFile();

        mMapView = new MapView(this, new BaiduMapOptions());
        initView(this);

        baiduMap=mMapView.getMap();
        //默认显示普通地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        setContentView(layout);



    }
    // 初始化View
    private void initView(Context context) {
        layout = new FrameLayout(this);
        layout.addView(mMapView);
    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位成功
                Log.i("baidu_location_result", "offline location success");
                double lat = location.getLatitude();
                double lon = location.getLongitude();

            } else if (location.getLocType() == BDLocation.TypeOffLineLocationFail) {
                // 离线定位失败
                Log.i("baidu_location_result", "offline location fail");
            } else {

                Log.i("baidu_location_result", "location type = " + location.getLocType());
            }
        }
    }

    // 设置地图config文件路径
    private void setMapCustomFile() {
        String str = Environment.getExternalStorageState().toString()+"/BaiduMapSDKNew/vmp/";
        File file = new File(str);
        if (!(file.exists())) {
            try {
                new File(str).createNewFile();
                InputStream iinput = openFileInput(str + ".cfg");
                FileOutputStream output = new FileOutputStream(str);
                byte[] buffer = new byte[8192];
                int i = 0;
                while ((i = iinput.read(buffer)) > 0) {
                    output.write(buffer, 0, i);
                }
                output.close();
                iinput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMapView.onDestroy();
    }

    @Override
    public void onGetOfflineMapState(int i, int i1) {

    }
}