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
    //????????????
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private MyLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;
    private LocationClientOption option = null;
    private boolean isFirstLocation = true;
    private LatLng currentLatLng;//??????????????????
    private Marker marker;//????????????

    //poi??????
    private GeoCoder mGeoCoder;//?????????????????????????????????poi
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;//?????????????????????????????????Sug?????????
    private LatLng center;//?????????????????????
    private int radius = 300;//poi????????????
    private int loadIndex = 0;
    private int pageSize = 50;
    private List<PoiInfo> poiInfoListForGeoCoder = new ArrayList<>();//???????????????????????????poi
    private List<PoiInfo> poiInfoListForSearch = new ArrayList<>();//??????????????????
    private Adapter_SearchAddress adapter_searchAddress;
    private String cityName = "";
    private String keyword = "";
    MKOfflineMap mOffline;
    List<String> ls = new ArrayList<>();
    private String locationProvider;
    private double gpslatitude = 0.1;
    private double gpslongitude = 0.1;
    private LocationManager locationManager;
    //??????????????????
    int gatherInterval = 2;
    //????????????
    int packInterval = 12;
    //?????????????????????????????????
    private LinearLayout h1;
    private TextView xiazai,textview_lixianditu,text5;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private int settinsflag=0;
    private MyNetworkReceiver myNetworkReceiver;
    private int myNetwork=0;
    //Todo ???????????????
    List<LatLng> ppp = new ArrayList<>();
    //todo ??????????????? ??????????????????
    private int runtime=10;
    private int daka=0;
    private BDLocation dakalocation;
    private LinearLayout ls1,ls2,ls6,ls7,ls8,ls11,ls12,ls15;
    private ImageView ima1;
    private int shensuo=0;//3?????? 2?????? 1??????
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_mainbaidu);
        checknetwork();
//        ActionBar actionBar=getSupportActionBar();
//        actionBar.hide();
        //TODO ?????????????????????
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            int ui = decorView.getSystemUiVisibility();
            ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //??????????????????????????????????????????
            decorView.setSystemUiVisibility(ui);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){//?????????????????????
            //??????????????????,200????????????
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }else{
            Toast.makeText(MainActivity.this,"?????????????????????", Toast.LENGTH_LONG).show();
            //????????????

        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                verifyStoragePermissions(MainActivity.this);
            }
        },3000);
        //???????????????
        initView();
        initOnclick();

        //???????????????
        monitorEditTextChage();
        //todo ???????????????????????????????????????poi??????
        initGeoCoder();
        initSuggestionSearch();
        initPoiSearch();
        //todo ????????????????????????
        initMap();
        initLocation();
        //todo ????????????????????????????????????????????????
        monitorMap();
        //todo ????????????????????????
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
                Toast.makeText(mContext, "?????????...???????????????app", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo();
        if(localMapList!=null){
            Toast.makeText(mContext, "??????????????????????????????" + localMapList.size(), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mContext, "??????????????????????????????0" , Toast.LENGTH_SHORT).show();
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
            //gps?????????
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
                Toast.makeText(mContext, "?????????????????????", Toast.LENGTH_SHORT).show();
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
        text5.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//?????????
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
                //??????2
                ls12.setVisibility(View.VISIBLE);
                //???
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
                //????????????
                ls15.setLayoutParams(params);
                ls12.setVisibility(View.GONE);
                return true;
            }
        });
    }

    private void checknetwork() {
        //??????????????????
        myNetworkReceiver = new MyNetworkReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myNetworkReceiver, intentFilter);
    }


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //???????????????????????????
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // ???????????????????????????????????????????????????????????????
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //???onCreate()??????????????????????????????
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
                    //???????????????????????????????????????????????????
                    showMapView();
                    return;
                }
                /* ??????????????????????????????????????????????????????onSuggestionResult()????????? */
                /* ??????????????????????????????????????????poi?????????????????????????????????????????????poi???????????????????????????????????????????????????????????????????????????poi */
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
        // ??????????????????
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
                        Toast.makeText(mContext, "?????????????????????.", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    /**
     * ?????????????????????
     */
    private void initLocation() {
        // ??????LocationClient???
        mLocationClient = new LocationClient(getApplicationContext());

        // ??????LocationClientOption???????????????SDK??????
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // ?????????????????????????????????????????????  LocationMode.Hight_Accuracy??????????????? LocationMode. Battery_Saving??????????????? LocationMode. Device_Sensors?????????????????????

        option.setCoorType("bd09ll");
        // ???????????????????????????????????????????????????gcj02
        // gcj02?????????????????????
        // bd09ll???????????????????????????
        // bd09???????????????????????????
        // ????????????????????????????????????????????????????????????wgs84????????????

        option.setOpenGps(true);
        // ???????????????????????????gps?????????false
        // ???????????????????????????????????????????????????????????????????????????true

        option.setLocationNotify(true);
        // ????????????????????????GPS???????????????1S/1???????????????GPS???????????????false

        option.setIgnoreKillProcess(true);
        // ???????????????SDK???????????????service??????????????????????????????
        // ???????????????stop???????????????????????????????????????????????????????????????setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        // ???????????????????????????Crash????????????????????????????????????false

        option.setEnableSimulateGps(false);
        // ?????????????????????????????????GPS??????????????????????????????????????????false

        option.setIsNeedLocationDescribe(true);
        // ???????????????????????????????????????????????????????????????????????????false

        option.setIsNeedLocationPoiList(true);
        // ???????????????????????????POI??????????????????????????????????????????false

        option.setIsNeedAddress(true);// ??????????????????
        //??????????????????
//        option.setScanSpan(10000);

        mLocationClient.setLocOption(option);
        // ??????????????????
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
    }
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            dakalocation=location;
            //?????????BDLocation?????????????????????????????????????????????get??????????????????????????????????????????
            if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // ??????????????????
                Log.i("baidu_location_result", "offline location success");
                double lat = location.getLatitude();
                double lon = location.getLongitude();
            } else if (location.getLocType() == BDLocation.TypeOffLineLocationFail) {
                // ??????????????????
                Log.i("baidu_location_result", "offline location fail");
            } else {
                onReceiveLocation2(location);
                if(location.getLatitude()>37.8330&&location.getLatitude()>49.9377){
                    Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(mContext, ""+location.getLatitude(), Toast.LENGTH_SHORT).show();
                if(myNetworkReceiver.getNetwork().equals("???????????????")){
                    //????????????
                    location.setLatitude(gpslatitude+0.008774687519+0.00074331248099);
                    location.setLongitude(gpslongitude+0.00374531687912+0.00960631645);
                    Log.e("aaasss", (gpslatitude+0.008774687519+0.00074331248099)+"GPS????????????---"+(gpslongitude+0.00374531687912+0.00960631645));
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
                    Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
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
     * ????????????????????????????????????????????????????????????
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

            //???????????????????????????poi?????????
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
     * ??????????????????
     */
//    public class MyLocationListener extends BDAbstractLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //mapView ???????????????????????????????????????
//            if (location == null || mMapView == null) {
//                return;
//            }
//            Log.e(TAG, "???????????????????????????" + location.getAddrStr());
//            if (location.getLocType() == BDLocation.TypeGpsLocation
//                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                navigateTo(location);
//                cityName = location.getCity();
//                tv_city.setText(cityName);
//                Log.e(TAG, "?????????????????????" + location.getCity());
//                Toast.makeText(mContext, ""+location.getCity()+String.valueOf(location.getCityCode()), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    /**
     * ??????????????????????????????????????????????????????
     */
    private void monitorMap() {
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                // TODO Auto-generated method stub
                //??????????????????
            }
        });
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            /**
             * ?????????????????????????????????????????????????????????????????????????????????
             * @param mapStatus ??????????????????????????????????????????
             */
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            /** ????????????????????????????????????????????????
             * @param mapStatus ??????????????????????????????????????????
             * @param i ????????????
             * 1????????????????????????????????????????????????,????????????????????????????????????
             * 2???SDK???????????????????????????, ??????????????????????????????????????????
             * 3??????????????????,???????????????????????????
             */
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                Log.e(TAG, "??????????????????????????????" + i + "");
            }

            /**
             * ?????????????????????
             * @param mapStatus ??????????????????
             */
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                LatLng latlng = mBaiduMap.getMapStatus().target;
                double latitude = latlng.latitude;
                double longitude = latlng.longitude;
//                Log.e(TAG, "????????????????????????" + latitude + "," + longitude);
                addMarker(latlng);
            }

            /**
             * ????????????????????????
             * @param mapStatus ??????????????????????????????????????????
             */
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                center = mBaiduMap.getMapStatus().target;
//                Log.e(TAG, "??????????????????????????????" + center.latitude + "," + center.longitude);
                if (poiInfoListForGeoCoder != null) {
                    poiInfoListForGeoCoder.clear();
                }
                //???????????????????????????poi?????????
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(center));
            }
        });
    }

    /**
     * ??????marker
     *
     * @param point Marker?????????
     */
    private void addMarker(LatLng point) {
        if (marker != null) {
//            marker.remove();
            marker.setPosition(point);
        } else {
            //??????Marker??????
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.location);
            //??????MarkerOption???????????????????????????Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //??????????????????Marker????????????
            marker = (Marker) mBaiduMap.addOverlay(option);
        }
    }

    /**
     * ?????????????????????
     */
    private void showSeachView() {
        ll_mapView.setVisibility(View.GONE);
        ll_poiSearch.setVisibility(View.VISIBLE);
    }

    /**
     * ?????????????????????
     */
    private void showMapView() {
        ll_mapView.setVisibility(View.VISIBLE);
        ll_poiSearch.setVisibility(View.GONE);
    }

    //-----------------------------------------?????????????????????????????????poi??????--------------------------------------------------

    /**
     * ??????????????????,???????????????poi??????????????????????????????????????????????????????
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
                    //????????????
                    ReverseGeoCodeResult.AddressComponent addressComponent = reverseGeoCodeResult.getAddressDetail();
                    cityName = addressComponent.city;
                    tv_city.setText(cityName);
                    //??????poi??????
                    if (reverseGeoCodeResult.getPoiList() != null) {
                        poiInfoListForGeoCoder = reverseGeoCodeResult.getPoiList();
                    }
                } else {
                    Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_SHORT);
                }
                initGeoCoderListView();
            }
        });
    }

    //-----------------------------------------???????????????sug?????????------------------------------------------------------------------
    private void initSuggestionSearch() {
        // ????????????????????????????????????????????????????????????(sug??????)
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            /**
             * ???????????????????????????????????????requestSuggestion?????????????????????
             * @param suggestionResult    Sug????????????
             */
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                    Toast.makeText(mContext, "???????????????", Toast.LENGTH_LONG).show();
                    return;
                }

                List<SuggestionResult.SuggestionInfo> sugList = suggestionResult.getAllSuggestions();
                for (SuggestionResult.SuggestionInfo info : sugList) {
                    if (info.key != null) {
                        Log.e(TAG, "???????????????" + info.toString());
                        Log.e(TAG, "key???" + info.key);
                        DecimalFormat df = new DecimalFormat("######0");
                        //?????????????????????????????????
                        String distance = df.format(DistanceUtil.getDistance(currentLatLng, info.pt));
                        Log.e(TAG, "?????????" + distance);
                    }
                }

            }
        });
    }

    //--------------------------------------------------poi??????---------------------------------------------------------------------
    private void initPoiSearch() {
        // ????????????????????????????????????????????????
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    /**
     * ??????POI?????????????????????searchInCity???searchNearby???searchInBound?????????????????????
     *
     * @param poiResult Poi???????????????????????????????????????????????????????????????
     */
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiInfoListForSearch != null) {
            poiInfoListForSearch.clear();
        }
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(mContext, "???????????????", Toast.LENGTH_LONG).show();
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
            // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            String strInfo = "???";

            for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }

            strInfo += "????????????";
          //  Toast.makeText(mContext, strInfo, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * ??????POI???????????????????????????searchPoiDetail?????????????????????
     * V5.2.0???????????????????????????????????????{@link #onGetPoiDetailResult(PoiDetailSearchResult)}??????
     *
     * @param poiDetailResult POI??????????????????
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "????????????????????????", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext,
                    poiDetailResult.getName() + ": " + poiDetailResult.getAddress(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        if (poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "????????????????????????", Toast.LENGTH_SHORT).show();
        } else {
            List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();
            if (null == poiDetailInfoList || poiDetailInfoList.isEmpty()) {
                Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_SHORT).show();
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
     * ????????????
     */
    private void searchNearbyProcess(LatLng center) {
        //?????????????????????????????????????????????
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
     * ?????????????????????
     */
    public void searchCityPoiAddress() {
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(cityName)
                .keyword(keyword)//??????
                .pageCapacity(pageSize)
                .pageNum(loadIndex));//????????????
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //???????????????
            if (keyword.trim().length() > 0) {//?????????????????????????????????????????????????????????????????????
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
//                        if(!myNetworkReceiver.getNetwork().equals("???????????????")){
//
//                        }
                        lv_searchAddress.setVisibility(View.VISIBLE);
                    }else {
                        Toast.makeText(mContext, "?????????????????????.", Toast.LENGTH_SHORT).show();
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
        // ?????????????????????????????????????????????
        mBaiduMap.setMyLocationEnabled(false);
        // ??????????????????
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
                // ??????????????????????????????
                if (update != null) {
//                    stateView.setText();
                    //updateView();
                    Toast.makeText(mContext, ""+ String.format("%s : %d%%", update.cityName,
                            update.ratio), Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // ????????????????????????
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                // ??????????????????
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
                gpslatitude = location1.getLatitude(); // ??????
                gpslatitude = location1.getLongitude(); // ??????
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
        Toast.makeText(mContext, "GPS??????:"+'\n'+"?????????" + gpslatitude + "\n" + "?????????" + gpslongitude, Toast.LENGTH_SHORT).show();

    }

    LocationListener locationListener = new LocationListener() {
        // Provider??????????????????????????????????????????????????????????????????????????????????????????
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        // Provider???enable???????????????????????????GPS?????????
        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, provider);
        }

        // Provider???disable???????????????????????????GPS?????????
        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, provider);
        }

        // ??????????????????????????????????????????Provider?????????????????????????????????????????????
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.e("Map", "Location changed : Lat: " + location.getLatitude() + " Lng: " + location.getLongitude());
                gpslatitude = location.getLatitude(); // ??????
                gpslongitude = location.getLongitude(); // ??????
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
