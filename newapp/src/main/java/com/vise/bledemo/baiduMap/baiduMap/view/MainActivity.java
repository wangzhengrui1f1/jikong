package com.vise.bledemo.baiduMap.baiduMap.view;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.vise.bledemo.R;
import com.vise.bledemo.baiduMap.baiduMap.adapter.Adapter_SearchAddress;
import com.vise.bledemo.baiduMap.baiduMap.adapter.lixiancityAdapter;
import com.vise.bledemo.baiduMap.baiduMap.view.lixianList;
import com.vise.bledemo.baiduMap.utils.MyNetworkReceiver;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.vise.bledemo.baiduMap.baiduMap.adapter.helps.ScreenOrient;
import static com.vise.bledemo.utils.getTime.disposeTime;

public class MainActivity extends AppCompatActivity implements OnGetPoiSearchResultListener
        , MKOfflineMapListener {
    private static String TAG = "MainActivity";
    private Context mContext;
    private TextView tv_city;
    private EditText et_keyword;
    private ListView lv_searchAddress, lv_poiSearch;
    private LinearLayout ll_poiSearch;
    FrameLayout ll_mapView;
    double latitudes, longitudes;
    //地图定位
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private MyLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;
    private LocationClientOption option = null;
    private boolean isFirstLocation = true;
    private LatLng currentLatLng;//当前所在位置
    private Marker marker;//地图标注

    //poi检索
    private GeoCoder mGeoCoder;//反向地理解析，获取周边poi
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;//地点检索输入提示检索（Sug检索）
    private LatLng center;//地图中心点坐标
    private int radius = 300;//poi检索半径
    private int loadIndex = 0;
    private int pageSize = 50;
    private List<PoiInfo> poiInfoListForGeoCoder = new ArrayList<>();//地理反向解析获取的poi
    private List<PoiInfo> poiInfoListForSearch = new ArrayList<>();//检索结果集合
    private Adapter_SearchAddress adapter_searchAddress;
    private String cityName = "";
    private String keyword = "";
    MKOfflineMap mOffline;
    List<String> ls = new ArrayList<>();
    private String locationProvider;
    private double gpslatitude = 0.1;
    private double gpslongitude = 0.1;
    private LocationManager locationManager;
    //位置采集周期
    int gatherInterval = 2;
    //打包周期
    int packInterval = 12;
    //设置位置采集和打包周期
    private LinearLayout h1;
    private TextView xiazai,textview_lixianditu,text5;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private int settinsflag=0;
    private MyNetworkReceiver myNetworkReceiver;
    private int myNetwork=0;
    //Todo 运动轨迹点
    List<LatLng> ppp = new ArrayList<>();
    //todo 运动轨迹点 录入时间间隔
    private int runtime=10;
    private int daka=0;
    private BDLocation dakalocation;
    private LinearLayout ls1,ls2,ls6,ls7,ls8,ls11,ls12,ls15;
    private ImageView ima1;
    private int shensuo=0;//3最长 2中间 1最短
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_mainbaidu);
        checknetwork();
//        ActionBar actionBar=getSupportActionBar();
//        actionBar.hide();
        //TODO 设置状态栏透明
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            int ui = decorView.getSystemUiVisibility();
            ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //设置状态栏中字体的颜色为黑色
            decorView.setSystemUiVisibility(ui);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }else{
            Toast.makeText(MainActivity.this,"已开启定位权限", Toast.LENGTH_LONG).show();
            //存储权限

        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                verifyStoragePermissions(MainActivity.this);
            }
        },3000);
        //初始化控件
        initView();
        initOnclick();

        //监听输入框
        monitorEditTextChage();
        //todo 初始化地理解析、建议搜索、poi搜索
        initGeoCoder();
        initSuggestionSearch();
        initPoiSearch();
        //todo 初始化地图及定位
        initMap();
        initLocation();
        //todo 监听地图事件（加载完成、拖动等）
        monitorMap();
        //todo 选择地址点击事件
        listViewOncilck();

        mOffline = new MKOfflineMap();
        mOffline.init(this);
        // ArrayList<MKOLSearchRecord> records2 = mOffline.getOfflineCityList();
        final ArrayList<MKOLSearchRecord> records2 = mOffline.getOfflineCityList();
       // Toast.makeText(mContext, "" + records2.get(1).cityName, Toast.LENGTH_SHORT).show();
        lixiancityAdapter adapter2 = new lixiancityAdapter(this, records2);
        final ListView lixianListview = (ListView) findViewById(R.id.lixianListview);
        lixianListview.setAdapter(adapter2);
        lixianListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOffline.start(records2.get(position).cityID);
                Toast.makeText(mContext, "下载中...请不要退出app", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo();
        if(localMapList!=null){
            Toast.makeText(mContext, "已下载城市地图数量：" + localMapList.size(), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mContext, "已下载城市地图数量：0" , Toast.LENGTH_SHORT).show();
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
            //gps已打开
        } else {
            toggleGPS();
            new Handler() {
            }.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getLocation();
                }
            }, 2000);

        }
        xiazai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lixianListview.getVisibility()== View.GONE){
                    lixianListview.setVisibility(View.VISIBLE);
                    lv_searchAddress.setVisibility(View.GONE);
                }else {
                    lixianListview.setVisibility(View.GONE);
                    lv_searchAddress.setVisibility(View.VISIBLE);
                }
            }
        });

        textview_lixianditu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this, lixianList.class);
                startActivity(intent);
            }
        });


        View inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.dakaitem, null);
        mBaiduMap.addOverlay(new MarkerOptions()
                .position(new LatLng(38.93697955999, 121.65663953))
                .icon(BitmapDescriptorFactory.fromView(inflate)));

        mBaiduMap.addOverlay(new MarkerOptions()
                .position(new LatLng(38.92697955999, 121.63663953))
                .icon(BitmapDescriptorFactory.fromView(inflate)));

        mBaiduMap.addOverlay(new MarkerOptions()
                .position(new LatLng(38.92697955999, 121.53663953))
                .icon(BitmapDescriptorFactory.fromView(inflate)));
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(mContext, "打卡点被点击！", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }



    private void initView() {
        h1 = (LinearLayout) findViewById(R.id.h1);
        mContext = MainActivity.this;
        tv_city = (TextView) findViewById(R.id.tv_city);
        et_keyword = (EditText) findViewById(R.id.et_keyword);
        ll_mapView = (FrameLayout) findViewById(R.id.ll_mapView);
        ll_poiSearch = (LinearLayout) findViewById(R.id.ll_poiSearch);
        lv_searchAddress = (ListView) findViewById(R.id.lv_searchAddress);
        lv_poiSearch = (ListView) findViewById(R.id.lv_poiSearch);
        xiazai = (TextView) findViewById(R.id.xiazai);
        textview_lixianditu = (TextView) findViewById(R.id.textview_lixianditu);
        ls1 = (LinearLayout) findViewById(R.id.ls1);
        ls2 = (LinearLayout) findViewById(R.id.ls2);
        ls6 = (LinearLayout) findViewById(R.id.ls6);
        ls7 = (LinearLayout) findViewById(R.id.ls7);
        ls8 = (LinearLayout) findViewById(R.id.ls8);
        ls11 = (LinearLayout) findViewById(R.id.ls11);
        ls12 = (LinearLayout) findViewById(R.id.ls12);
        ls15 = (LinearLayout) findViewById(R.id.ls15);
        ima1 = (ImageView) findViewById(R.id.ima1);
        text5 = (TextView) findViewById(R.id.text5);
        text5.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        ls6.setVisibility(View.GONE);
        ls7.setVisibility(View.GONE);
        Log.e("ls6ls7","gone");
    }

    private void initOnclick() {
        ima1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 0);
                //整体2
                ls12.setVisibility(View.VISIBLE);
                //中
                if(shensuo == 2||shensuo == 0){
                    shensuo=1;
                    params.weight = 1.0f;
                    ls6.setVisibility(View.GONE);
                    ls7.setVisibility(View.GONE);
                }else if(shensuo == 1){
                    shensuo=2;
                    params.weight = 2.0f;
                    ls6.setVisibility(View.VISIBLE);
                    ls7.setVisibility(View.VISIBLE);
                }
                ls2.setLayoutParams(params);
            }
        });
        ima1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 0);
                shensuo=0;
                params.weight = 0.1f;
                ls2.setLayoutParams(params);
                //地图最大
                ls15.setLayoutParams(params);
                ls12.setVisibility(View.GONE);
                return true;
            }
        });
    }

    private void checknetwork() {
        //动态注册广播
        myNetworkReceiver = new MyNetworkReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myNetworkReceiver, intentFilter);
    }


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //在onCreate()方法中调用该方法即可
    private void listViewOncilck() {

    }

    private void monitorEditTextChage() {
        et_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                keyword = editable.toString();
                if (keyword.length() <= 0) {
                    //当清空文本后展示地图，隐藏搜索结果
                    showMapView();
                    return;
                }
                /* 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新 */
                /* 由于我们需要滑动地图展示周边poi，所以就不用建议搜索列表来搜索poi了，搜索时直接利用城市和输入的关键字进行城市内检索poi */
//                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
//                        .keyword(keyword)
//                        .city(cityName));
                searchCityPoiAddress();
            }
        });
    }

    private void initMap() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                settinsflag++;
                if(settinsflag%2==0){
                    h1.setVisibility(View.GONE);
                    lv_searchAddress.setVisibility(View.GONE);
                }else {
                    if(ScreenOrient(MainActivity.this)==1){
                        h1.setVisibility(View.VISIBLE);
                        lv_searchAddress.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(mContext, "横屏不建议拓展.", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    /**
     * 初始化定位相关
     */
    private void initLocation() {
        // 声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());

        // 利用LocationClientOption类配置定位SDK参数
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，设置定位模式，默认高精度  LocationMode.Hight_Accuracy：高精度； LocationMode. Battery_Saving：低功耗； LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        // 可选，设置返回经纬度坐标类型，默认gcj02
        // gcj02：国测局坐标；
        // bd09ll：百度经纬度坐标；
        // bd09：百度墨卡托坐标；
        // 海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setOpenGps(true);
        // 可选，设置是否使用gps，默认false
        // 使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        // 可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(true);
        // 可选，定位SDK内部是一个service，并放到了独立进程。
        // 设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        // 可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setEnableSimulateGps(false);
        // 可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setIsNeedLocationDescribe(true);
        // 可选，是否需要位置描述信息，默认为不需要，即参数为false

        option.setIsNeedLocationPoiList(true);
        // 可选，是否需要周边POI信息，默认为不需要，即参数为false

        option.setIsNeedAddress(true);// 获取详细信息
        //设置扫描间隔
//        option.setScanSpan(10000);

        mLocationClient.setLocOption(option);
        // 注册监听函数
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            dakalocation=location;
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
                onReceiveLocation2(location);
                if(location.getLatitude()>37.8330&&location.getLatitude()>49.9377){
                    Toast.makeText(mContext, "打卡成功！", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(mContext, ""+location.getLatitude(), Toast.LENGTH_SHORT).show();
                if(myNetworkReceiver.getNetwork().equals("无网络连接")){
                    //模拟定位
                    location.setLatitude(gpslatitude+0.008774687519+0.00074331248099);
                    location.setLongitude(gpslongitude+0.00374531687912+0.00960631645);
                    Log.e("aaasss", (gpslatitude+0.008774687519+0.00074331248099)+"GPS模拟定位---"+(gpslongitude+0.00374531687912+0.00960631645));
                }
                if(runtime>=10){
                    ppp.add(new LatLng(location.getLatitude(),location.getLongitude()));
                    runtime=0;
                    ls.add("da"+disposeTime()+'\n');
                    Log.e("aatime--da",disposeTime());
                }
                ls.add(disposeTime()+'\n');
                //Toast.makeText(mContext, ls.toString(), Toast.LENGTH_SHORT).show();
                Log.e("aatime",disposeTime());
                runtime++;
                Log.e("aaasss", (gpslatitude+0.008774687519+0.00074331248099)+"---"+(gpslongitude+0.00374531687912+0.00960631645));
                Log.e("aaasss", location.getLatitude()+"---"+location.getLongitude());
                if(gpslatitude>37.8330&&gpslatitude>49.9377){
                    Toast.makeText(mContext, "打卡成功！", Toast.LENGTH_SHORT).show();
                }
                navigateTo(location);
                cityName = location.getCity();
                tv_city.setText(cityName);
                Log.i("baidu_location_result", "location type = " + location.getLocType());
                Log.i("baidu_location_result1", "location type = " + location.getLatitude());
                Log.i("baidu_location_result2", "location type = " + location.getLongitude());
            }

        }
    }

    /**
     * 根据获取到的位置在地图上移动“我”的位置
     *
     * @param location
     */
    private void navigateTo(BDLocation location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String address = location.getAddrStr();
        if (isFirstLocation) {
            currentLatLng = new LatLng(latitude, longitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            MapStatus mapStatus = builder.target(currentLatLng).zoom(17.0f).build();
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                    .newMapStatus(mapStatus));
            isFirstLocation = false;

            //反向地理解析（含有poi列表）
            mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(currentLatLng));
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
    }


//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }

    /**
     * 监听当前位置
     */
//    public class MyLocationListener extends BDAbstractLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //mapView 销毁后不在处理新接收的位置
//            if (location == null || mMapView == null) {
//                return;
//            }
//            Log.e(TAG, "当前“我”的位置：" + location.getAddrStr());
//            if (location.getLocType() == BDLocation.TypeGpsLocation
//                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                navigateTo(location);
//                cityName = location.getCity();
//                tv_city.setText(cityName);
//                Log.e(TAG, "当前定位城市：" + location.getCity());
//                Toast.makeText(mContext, ""+location.getCity()+String.valueOf(location.getCityCode()), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    /**
     * 监听地图事件（这里主要监听移动地图）
     */
    private void monitorMap() {
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                // TODO Auto-generated method stub
                //地图加载完成
            }
        });
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            /**
             * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
             * @param mapStatus 地图状态改变开始时的地图状态
             */
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            /** 因某种操作导致地图状态开始改变。
             * @param mapStatus 地图状态改变开始时的地图状态
             * @param i 取值有：
             * 1：用户手势触发导致的地图状态改变,比如双击、拖拽、滑动底图
             * 2：SDK导致的地图状态改变, 比如点击缩放控件、指南针图标
             * 3：开发者调用,导致的地图状态改变
             */
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                Log.e(TAG, "地图状态改变开始时：" + i + "");
            }

            /**
             * 地图状态变化中
             * @param mapStatus 当前地图状态
             */
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                LatLng latlng = mBaiduMap.getMapStatus().target;
                double latitude = latlng.latitude;
                double longitude = latlng.longitude;
//                Log.e(TAG, "地图状态变化中：" + latitude + "," + longitude);
                addMarker(latlng);
            }

            /**
             * 地图状态改变结束
             * @param mapStatus 地图状态改变结束后的地图状态
             */
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                center = mBaiduMap.getMapStatus().target;
//                Log.e(TAG, "地图状态改变结束后：" + center.latitude + "," + center.longitude);
                if (poiInfoListForGeoCoder != null) {
                    poiInfoListForGeoCoder.clear();
                }
                //反向地理解析（含有poi列表）
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(center));
            }
        });
    }

    /**
     * 添加marker
     *
     * @param point Marker坐标点
     */
    private void addMarker(LatLng point) {
        if (marker != null) {
//            marker.remove();
            marker.setPosition(point);
        } else {
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.location);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            marker = (Marker) mBaiduMap.addOverlay(option);
        }
    }

    /**
     * 展示搜索的布局
     */
    private void showSeachView() {
        ll_mapView.setVisibility(View.GONE);
        ll_poiSearch.setVisibility(View.VISIBLE);
    }

    /**
     * 展示地图的布局
     */
    private void showMapView() {
        ll_mapView.setVisibility(View.VISIBLE);
        ll_poiSearch.setVisibility(View.GONE);
    }

    //-----------------------------------------反向地理解析，获取周边poi列表--------------------------------------------------

    /**
     * 反向地理解析,结果中含有poi信息，用于刚进入地图和移动地图时使用
     */
    private void initGeoCoder() {
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (poiInfoListForGeoCoder != null) {
                    poiInfoListForGeoCoder.clear();
                }
                if (reverseGeoCodeResult.error.equals(SearchResult.ERRORNO.NO_ERROR)) {
                    //获取城市
                    ReverseGeoCodeResult.AddressComponent addressComponent = reverseGeoCodeResult.getAddressDetail();
                    cityName = addressComponent.city;
                    tv_city.setText(cityName);
                    //获取poi列表
                    if (reverseGeoCodeResult.getPoiList() != null) {
                        poiInfoListForGeoCoder = reverseGeoCodeResult.getPoiList();
                    }
                } else {
                    Toast.makeText(mContext, "该位置范围内无信息", Toast.LENGTH_SHORT);
                }
                initGeoCoderListView();
            }
        });
    }

    //-----------------------------------------建议搜索（sug检索）------------------------------------------------------------------
    private void initSuggestionSearch() {
        // 初始化建议搜索模块，注册建议搜索事件监听(sug搜索)
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            /**
             * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果
             * @param suggestionResult    Sug检索结果
             */
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                    Toast.makeText(mContext, "未找到结果", Toast.LENGTH_LONG).show();
                    return;
                }

                List<SuggestionResult.SuggestionInfo> sugList = suggestionResult.getAllSuggestions();
                for (SuggestionResult.SuggestionInfo info : sugList) {
                    if (info.key != null) {
                        Log.e(TAG, "搜索结果：" + info.toString());
                        Log.e(TAG, "key：" + info.key);
                        DecimalFormat df = new DecimalFormat("######0");
                        //用当前所在位置算出距离
                        String distance = df.format(DistanceUtil.getDistance(currentLatLng, info.pt));
                        Log.e(TAG, "距离：" + distance);
                    }
                }

            }
        });
    }

    //--------------------------------------------------poi检索---------------------------------------------------------------------
    private void initPoiSearch() {
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     *
     * @param poiResult Poi检索结果，包括城市检索，周边检索，区域检索
     */
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiInfoListForSearch != null) {
            poiInfoListForSearch.clear();
        }
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(mContext, "未找到结果", Toast.LENGTH_LONG).show();
            initPoiSearchListView();
            return;
        }

        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            poiInfoListForSearch = poiResult.getAllPoi();
            showSeachView();
            initPoiSearchListView();
            return;
        }

        if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";

            for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }

            strInfo += "找到结果";
          //  Toast.makeText(mContext, strInfo, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     * V5.2.0版本之后，该方法废弃，使用{@link #onGetPoiDetailResult(PoiDetailSearchResult)}代替
     *
     * @param poiDetailResult POI详情检索结果
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext,
                    poiDetailResult.getName() + ": " + poiDetailResult.getAddress(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        if (poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();
            if (null == poiDetailInfoList || poiDetailInfoList.isEmpty()) {
                Toast.makeText(mContext, "抱歉，检索结果为空", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < poiDetailInfoList.size(); i++) {
                PoiDetailInfo poiDetailInfo = poiDetailInfoList.get(i);
                if (null != poiDetailInfo) {
                    Toast.makeText(mContext,
                            poiDetailInfo.getName() + ": " + poiDetailInfo.getAddress(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    private void initGeoCoderListView() {
        adapter_searchAddress = new Adapter_SearchAddress(poiInfoListForGeoCoder, mContext, currentLatLng){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view=super.getView(position, convertView, parent);
                final LinearLayout t1= (LinearLayout) view.findViewById(R.id.t1);
                t1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectedPosition(position);
                        notifyDataSetInvalidated();
                      //  Toast.makeText(MainActivity.this,poiInfoListForGeoCoder.get(position).getAddress(), Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                            lv_searchAddress.smoothScrollToPosition(position);
                        }
                        // Toast.makeText(MainActivity.this,poiInfoListForGeoCoder.get(position).getProvince(), Toast.LENGTH_SHORT).show();
                    }
                });
                return view;
            }
        };
        lv_searchAddress.setAdapter(adapter_searchAddress);
        adapter_searchAddress.setSelectedPosition(0);
        adapter_searchAddress.notifyDataSetInvalidated();


    }

    private void initPoiSearchListView() {
        adapter_searchAddress = new Adapter_SearchAddress(poiInfoListForSearch, mContext, currentLatLng){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view=super.getView(position, convertView, parent);
                final LinearLayout t1= (LinearLayout) view.findViewById(R.id.t1);
                final TextView tname= (TextView) view.findViewById(R.id.tv_name);
                final TextView tadress= (TextView) view.findViewById(R.id.tv_address);
                t1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setSelectedPosition(position);
                        notifyDataSetInvalidated();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                            lv_searchAddress.smoothScrollToPosition(position);
                        }
//                        back_name.setText(tname.getText().toString());
//                        back_adress.setText(tadress.getText().toString());
                    }
                });
                return view;
            }
        };
        lv_poiSearch.setAdapter(adapter_searchAddress);
        adapter_searchAddress.setSelectedPosition(0);
        adapter_searchAddress.notifyDataSetInvalidated();
    }



    /**
     * 周边搜索
     */
    private void searchNearbyProcess(LatLng center) {
        //以定位点为中心，搜索半径以内的
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption()
                .keyword(keyword)
                .sortType(PoiSortType.distance_from_near_to_far)
                .location(center)
                .radius(radius)
                .pageCapacity(pageSize)
                .pageNum(loadIndex);

        mPoiSearch.searchNearby(nearbySearchOption);
    }


    /**
     * 响应城市内搜索
     */
    public void searchCityPoiAddress() {
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(cityName)
                .keyword(keyword)//必填
                .pageCapacity(pageSize)
                .pageNum(loadIndex));//分页页码
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //重写返回键
            if (keyword.trim().length() > 0) {//如果输入框还有字，就返回到地图界面并清空输入框
                showMapView();
                et_keyword.setText("");
            } else {
                finish();
            }
            return true;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if(ScreenOrient(this)==1){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                settinsflag++;
                if(settinsflag%2==0){
                    h1.setVisibility(View.GONE);
                    lv_searchAddress.setVisibility(View.GONE);
                }else {
                    if(ScreenOrient(this)==1){
                        h1.setVisibility(View.VISIBLE);
//                        if(!myNetworkReceiver.getNetwork().equals("无网络连接")){
//
//                        }
                        lv_searchAddress.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(mContext, "横屏不建议拓展.", Toast.LENGTH_SHORT).show();
                    }

                }

                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        // 当不需要定位图层时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        // 取消监听函数
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(myListener);
        }
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        mGeoCoder.destroy();
    }
    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {
//                    stateView.setText();
                    //updateView();
                    Toast.makeText(mContext, ""+ String.format("%s : %d%%", update.cityName,
                            update.ratio), Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // 版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
                break;
            default:
                break;
        }
    }
    private void toggleGPS() {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(this, 0, gpsIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location1 != null) {
                gpslatitude = location1.getLatitude(); // 经度
                gpslatitude = location1.getLongitude(); // 纬度
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            gpslatitude = location.getLatitude();
            gpslongitude = location.getLongitude();
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }
        Toast.makeText(mContext, "GPS定位:"+'\n'+"纬度：" + gpslatitude + "\n" + "经度：" + gpslongitude, Toast.LENGTH_SHORT).show();

    }

    LocationListener locationListener = new LocationListener() {
        // Provider的状态在可用、临时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // Provider被enable时触发此函数，比方GPS被打开
        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, provider);
        }

        // Provider被disable时触发此函数，比方GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, provider);
        }

        // 当坐标改变时触发此函数，假设Provider传进同样的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.e("Map", "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
                gpslatitude = location.getLatitude(); // 经度
                gpslongitude = location.getLongitude(); // 纬度
            }
        }
    };


    public void onReceiveLocation2(BDLocation bdLocation) {
        if (bdLocation == null || mMapView == null) {
            return;
        }
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude())
                .build();
        mBaiduMap.setMyLocationData(locData);
        LatLng latLng = new LatLng(bdLocation.getLatitude(),
                bdLocation.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
        LatLng pointsss = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        if (pointsss.longitude > 0 && pointsss.latitude > 0)
            ppp.add(pointsss);
        if (ppp.size() >= 2) {
            OverlayOptions mOverlay;
            if (daka == 0) {
                mOverlay = new PolylineOptions()
                        .width(10)
                        .color(0xAAFF0000)
                        .points(ppp);
            } else {
                mOverlay = new PolylineOptions()
                        .width(10)
                        .color(R.color.orange)
                        .points(ppp);
            }

            Overlay mPolyline = (Polyline) mBaiduMap.addOverlay(mOverlay);
            mPolyline.setZIndex(3);
        }

        Log.i("MyMaps", " latitude:" + bdLocation.getLatitude()
                + " longitude:" + bdLocation.getLongitude() + ppp.size());



    }


}
