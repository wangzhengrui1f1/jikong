package com.vise.bledemo.shouchi.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.vise.baseble.ViseBle;
import com.vise.baseble.callback.scan.IScanCallback;
import com.vise.baseble.callback.scan.ScanCallback;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.BluetoothLeDeviceStore;
import com.vise.bledemo.Base.BaseFragment;
import com.vise.bledemo.R;
import com.vise.bledemo.baiduMap.baiduMap.adapter.Adapter_SearchAddress2;
import com.vise.bledemo.baiduMap.baiduMap.adapter.lixiancityAdapter;
import com.vise.bledemo.baiduMap.baiduMap.view.WelcomeActivity;
import com.vise.bledemo.baiduMap.baiduMap.view.lixianList;
import com.vise.bledemo.baiduMap.utils.MyNetworkReceiver;
import com.vise.bledemo.bluetooth.adapter.DeviceAdapter2;
import com.vise.bledemo.bluetooth.common.BluetoothDeviceManager;
import com.vise.bledemo.chezai.bean.ShouDongQuZheng;
import com.vise.bledemo.sqlite.MyDatabase;
import com.vise.bledemo.utils.GeoHasher;
import com.vise.bledemo.utils.OkHttpUtils;
import com.vise.bledemo.utils.screen.ScreenShotActivity;
import com.vise.bledemo.utils.screen.Shooter;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;
import com.vise.xsnow.event.BusManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vise.bledemo.R.mipmap.location;
import static com.vise.bledemo.chezai.view.JavaActivity.screenShot;
import static com.vise.bledemo.shouchi.data.Data_all.devices;
import static com.vise.bledemo.shouchi.data.Data_all.pppa;
import static com.vise.bledemo.sqlite.sqliteMethod.addCheZaiBaoJingData;
import static com.vise.bledemo.utils.Mima.createMima;
import static com.vise.bledemo.utils.Shengyin.registerPlayer;
import static com.vise.bledemo.utils.UseFngfa.closejianpan2;
import static com.vise.bledemo.utils.ViewBean.currentLatLng2;
import static com.vise.bledemo.utils.ViewBean.lanyaName2;
import static com.vise.bledemo.utils.ViewBean.mAllBaiduMap;
import static com.vise.bledemo.utils.ViewBean.poiInfoListForGeoCoder2;
import static com.vise.bledemo.utils.ViewBean.tname;
import static com.vise.bledemo.utils.getTime.disposeTime;
import static com.vise.bledemo.utils.getTime.disposeTime2;
import static com.vise.bledemo.utils.getTime.disposeTime3;
import static com.vise.bledemo.utils.getTime.xianchen;
import static com.vise.bledemo.utils.url.SQLITE_NAME;
import static com.vise.bledemo.utils.url.SaveEventurl;
import static com.vise.bledemo.utils.url.SavePlaceUrl;
import static com.vise.bledemo.utils.url.ShoudongFlag;
import static com.vise.bledemo.utils.url.isStart;
import static com.vise.utils.handler.HandlerUtil.runOnUiThread;
import static com.vise.bledemo.utils.ViewBean.closeblueview2;

public class SoneFragment extends BaseFragment implements OnGetPoiSearchResultListener
        , MKOfflineMapListener {
    View view;
    private static String TAG = "MainActivity";
    private Context mContext;
    private TextView tv_city, jieshu, kaishi, text_caozuoyuan;
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
    private TextView shoudongjilu;
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
    private Adapter_SearchAddress2 adapter_searchAddress;
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
    private FrameLayout sroot;
    private TextView xiazai, textview_lixianditu;
    boolean isFrist = true;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private int settinsflag = 0;
    private MyNetworkReceiver myNetworkReceiver;
    private int myNetwork = 0;
    //todo ??????????????? ??????????????????
    private int runtime = 10;
    private int daka = 0;
    private BDLocation dakalocation;
    private ImageView back;
    private int shensuo = 0;//3?????? 2?????? 1??????
    List<LatLng> list = new ArrayList<>();
    private TextView text3, text_shebei;
    LinearLayout lanyalist;
    Boolean lineStop = false;
    private ListView getdizhiListview;
    String imaPath = "";
    int blueone = 0;
    public static BDLocation Shouchisendlocation = null;
    //sqilte sqlite
    private MyDatabase myDatebaseHelper;
    //todo ??????------------------------------------------------------------------
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;
    //?????? ??????---
    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;
    private ListView deviceLv;
    private TextView scanCountTv;
    //?????????????????????????????????
    private DeviceAdapter2 adapter;
    private BluetoothLeDeviceStore bluetoothLeDeviceStore = new BluetoothLeDeviceStore();
    /**
     * ????????????
     */
    private ScanCallback periodScanCallback = new ScanCallback(new IScanCallback() {
        @Override
        public void onDeviceFound(final BluetoothLeDevice bluetoothLeDevice) {
            ViseLog.i("Founded Scan Device:" + bluetoothLeDevice);
            bluetoothLeDeviceStore.addDevice(bluetoothLeDevice);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adapter != null && bluetoothLeDeviceStore != null) {
                        adapter.setListAll(bluetoothLeDeviceStore.getDeviceList());
                        //   updateItemCount(adapter.getCount());
                    }
                }
            });
        }

        @Override
        public void onScanFinish(BluetoothLeDeviceStore bluetoothLeDeviceStore) {
            ViseLog.i("scan finish " + bluetoothLeDeviceStore);
        }

        @Override
        public void onScanTimeout() {
            ViseLog.i("scan timeout");
        }

    });
    String savenongdu, savewendu, saveguangqiang;
    private ViewPager main_viewPager;

    private static final int REQ_CODE_PER = 0x2304;
    private static final int REQ_CODE_ACT = 0x2305;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        view = inflater.inflate(R.layout.activity_mainbaidu, container, false);
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//?????????????????????
            //??????????????????,200????????????
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_LONG).show();
            //????????????

        }
        //sqlite?????????
        myDatebaseHelper = new MyDatabase(getActivity(), SQLITE_NAME, null, 1);
        //???????????????
        ViseLog.getLogConfig().configAllowLog(true);//??????????????????
        ViseLog.plant(new LogcatTree());//??????Logcat????????????
        BluetoothDeviceManager.getInstance().init(getActivity());
        BusManager.getBus().register(this);
        initBluetooth();
        startScan();
        //invalidateOptionsMenu();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                verifyStoragePermissions(getActivity());
            }
        }, 3000);
        //???????????????
        initView();
        initOnclick();
        Log.e("aaacheck", "1");
        //???????????????
        monitorEditTextChage();
        //todo ???????????????????????????????????????poi??????
        initGeoCoder();
        Log.e("aaacheck", "2");
        initSuggestionSearch();
        initPoiSearch();
        //todo ????????????????????????
        initMap();
        Log.e("aaacheck", "3");
        initLocation();
        //todo ????????????????????????????????????????????????
        monitorMap();
        //todo ????????????????????????
        listViewOncilck();
        monitorMap();
        Log.e("aaacheck", "4");
        final ArrayList<MKOLSearchRecord> records2 = mOffline.getOfflineCityList();
        // Toast.makeText(mContext, "" + records2.get(1).cityName, Toast.LENGTH_SHORT).show();
        final lixiancityAdapter adapter2 = new lixiancityAdapter(getActivity(), records2);
        final ListView lixianListview = (ListView) view.findViewById(R.id.lixianListview);
        lixianListview.setAdapter(adapter2);
        lixianListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOffline.start(records2.get(position).cityID);
                Toast.makeText(mContext, "?????????...???????????????app", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo();
        if (localMapList != null) {
            Toast.makeText(mContext, "??????????????????????????????" + localMapList.size(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "??????????????????????????????0", Toast.LENGTH_SHORT).show();
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                Toast.makeText(mContext, "111", Toast.LENGTH_SHORT).show();
                if (lixianListview.getVisibility() == View.GONE) {
                    lixianListview.setVisibility(View.VISIBLE);
                    lv_searchAddress.setVisibility(View.GONE);
                } else {
                    lixianListview.setVisibility(View.GONE);
                    lv_searchAddress.setVisibility(View.VISIBLE);
                }
            }
        });

        textview_lixianditu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), lixianList.class);
                startActivity(intent);
            }
        });
        tname = view.findViewById(R.id.tname2);
        Handler hs = new Handler();
        hs.postDelayed(new Runnable() {
            @Override
            public void run() {
                pppa.clear();
            }
        }, 15000);
        checknetwork();
        return view;
    }

    private void initView() {
        h1 = (LinearLayout) view.findViewById(R.id.h1);
        sroot = (FrameLayout) view.findViewById(R.id.sroot);
        mContext = getActivity();
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        et_keyword = (EditText) view.findViewById(R.id.et_keyword);
        ll_mapView = (FrameLayout) view.findViewById(R.id.ll_mapView);
        ll_poiSearch = (LinearLayout) view.findViewById(R.id.ll_poiSearch);
        lv_searchAddress = (ListView) view.findViewById(R.id.lv_searchAddress);
        lv_poiSearch = (ListView) view.findViewById(R.id.lv_poiSearch);
        xiazai = (TextView) view.findViewById(R.id.xiazai);
        textview_lixianditu = (TextView) view.findViewById(R.id.textview_lixianditu);
        lanyalist = (LinearLayout) view.findViewById(R.id.lanyalist);
        closeblueview2 = lanyalist;
        back = (ImageView) view.findViewById(R.id.back);

        text_shebei = (TextView) view.findViewById(R.id.text_shebei);
        shoudongjilu = (TextView) view.findViewById(R.id.shoudongjilu);
        kaishi = f(view, R.id.kaishi);
        text_caozuoyuan = f(view, R.id.text_caozuoyuan);
        text_caozuoyuan.setText(getpre("username"));
    }

    public void InitViewPager() {
        if (blueone == 0) {
            if (fragmentManager == null) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.aatt1, new blueToothDataFragment(), "scone3");
                fragmentTransaction.commit();
                blueone = 1;
                Log.e("blueone", "1");
            }
        } else if (blueone == 1) {
            blueToothDataFragment id = (blueToothDataFragment) fragmentManager.findFragmentByTag("scone3");
            id.ssint();
            Log.e("blueone", "2");
            blueone++;
        } else {
            blueToothDataFragment id = (blueToothDataFragment) fragmentManager.findFragmentByTag("scone3");
            id.createInit();
            Log.e("blueone", "3");
        }
    }

    private void initOnclick() {
        kaishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kaishi.getText().toString().equals("????????????")) {
                    pre("starttime", disposeTime());
                    //todo ????????????
                    isStart = true;
                    kaishi.setText("????????????");
                    kaishi.setBackgroundResource(R.drawable.bbhuang);
                    kaishi.setTextColor(getResources().getColor(R.color.black));
                    Log.e("xunjian-????????????", getpre("starttime"));
                    sendPostStart();
                } else {
                    //todo ????????????
                    isStart = false;
                    kaishi.setText("????????????");
                    kaishi.setTextColor(getResources().getColor(R.color.white));
                    kaishi.setBackgroundResource(R.drawable.sbutton);
                    sendPostEnd();
                }
                registerPlayer(getActivity());
            }
        });


        //??????????????????

        text_shebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lanyalist.getVisibility() == View.GONE) {
                    startScan();
                    lanyalist.setVisibility(View.VISIBLE);
                } else {
                    stopScan();
                    lanyalist.setVisibility(View.GONE);
                }
                registerPlayer(getActivity());
            }
        });

        shoudongjilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoudongFlag = 1;
                registerPlayer(getActivity());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aaxianchen", "????????????");
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });


    }


    public void onClickReqPermission() {
        Log.e("ajietu", "2");
        if (Build.VERSION.SDK_INT >= 21) {
            startActivityForResult(createScreenCaptureIntent(), REQ_CODE_PER);
        }
        Log.e("ajietu", "4");
    }

    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Intent createScreenCaptureIntent() {
        //Here using media_projection instead of Context.MEDIA_PROJECTION_SERVICE to  make it successfully build on low api.
        Log.e("ajietu", "3");
        return ((MediaProjectionManager) getActivity().getSystemService("media_projection")).createScreenCaptureIntent();
    }

    private void checknetwork() {
//        //??????????????????
        myNetworkReceiver = new MyNetworkReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(myNetworkReceiver, intentFilter);
    }


    public static void verifyStoragePermissions(Activity activity) {

        try {
            //???????????????????????????
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // ???????????????????????????????????????????????????????????????
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

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
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mAllBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // ??????????????????
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                tips_xiazai();
            }
        });

    }

    /**
     * ?????????????????????
     */
    private void initLocation() {
        // ??????LocationClient???
        mLocationClient = new LocationClient(getActivity());

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
        //  option.setScanSpan(10000);

        mLocationClient.setLocOption(option);
        // ??????????????????
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            dakalocation = location;
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
                //??????
                if (lineStop == false) {
                    // onReceiveLocation2(location);
                }

//                if (location.getLatitude() > 37.8330 && location.getLatitude() > 49.9377) {
//                    Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
//                }
                //  Toast.makeText(mContext, ""+location.getLatitude(), Toast.LENGTH_SHORT).show();
                if (myNetworkReceiver.getNetwork().equals("???????????????")) {
                    //????????????

                }
//                location.setLatitude(gpslatitude+0.008774687519+0.00074331248099);
//                location.setLongitude(gpslongitude+0.00374531687912+0.00960631645);
                Log.e("aaasss", (gpslatitude + 0.008774687519 + 0.00074331248099) + "GPS????????????---" + (gpslongitude + 0.00374531687912 + 0.00960631645));
                if (runtime >= 10) {
                    pppa.add(new LatLng(location.getLatitude(), location.getLongitude()));
                    runtime = 0;
                    ls.add("da" + disposeTime() + '\n');
                    Log.e("aatime--da", disposeTime());
                }
                ls.add(disposeTime() + '\n');
                //Toast.makeText(mContext, ls.toString(), Toast.LENGTH_SHORT).show();
                Log.e("aatime", disposeTime());
                runtime++;
                Log.e("aaasss", (gpslatitude + 0.008774687519 + 0.00074331248099) + "---" + (gpslongitude + 0.00374531687912 + 0.00960631645));
                Log.e("aaasss", location.getLatitude() + "---" + location.getLongitude());
//                if (gpslatitude > 37.8330 && gpslatitude > 49.9377) {
//                    Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
//                }
                //???????????? ??????
                navigateTo(location);
                cityName = location.getCity();
                tv_city.setText(cityName);
                Log.i("baidu_location_result", "location type = " + location.getLocType());
                Log.i("baidu_location_result1", "location type = " + location.getLatitude());
                Log.i("baidu_location_result2", "location type = " + location.getLongitude());
                // baiduMap.setMyLocationEnabled(true);
                //Toast.makeText(MainActivity.this,"??????",Toast.LENGTH_SHORT).show();
                //??????????????????
                Shouchisendlocation = location;
                final BDLocation Backloction = location;
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
            currentLatLng2 = currentLatLng;
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
                if (poiInfoListForGeoCoder != null && poiInfoListForGeoCoder2 != null) {
                    poiInfoListForGeoCoder.clear();
                    poiInfoListForGeoCoder2.clear();
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
                    .fromResource(location);
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

    //todo -----------------------------------------?????????????????????????????????poi??????--------------------------------------------------

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
                if (poiInfoListForGeoCoder != null && poiInfoListForGeoCoder2 != null) {
                    poiInfoListForGeoCoder.clear();
                    poiInfoListForGeoCoder2.clear();
                }
                if (reverseGeoCodeResult.error.equals(SearchResult.ERRORNO.NO_ERROR)) {
                    //????????????
                    ReverseGeoCodeResult.AddressComponent addressComponent = reverseGeoCodeResult.getAddressDetail();
                    cityName = addressComponent.city;
                    tv_city.setText(cityName);
                    //??????poi??????
                    if (reverseGeoCodeResult.getPoiList() != null) {
                        poiInfoListForGeoCoder = reverseGeoCodeResult.getPoiList();
                        poiInfoListForGeoCoder2 = reverseGeoCodeResult.getPoiList();
                    }
                } else {
                    Toast.makeText(mContext, "???????????????????????????", Toast.LENGTH_SHORT);
                }
                // initGeoCoderListView();
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

    private void initGeoCoderListView(final ListView listView) {
        adapter_searchAddress = new Adapter_SearchAddress2(poiInfoListForGeoCoder, mContext, currentLatLng) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                final LinearLayout t1 = (LinearLayout) view.findViewById(R.id.t1);
                t1.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(View v) {
                        setSelectedPosition(position);
                        notifyDataSetInvalidated();
                        //  Toast.makeText(MainActivity.this,poiInfoListForGeoCoder.get(position).getAddress(), Toast.LENGTH_SHORT).show();
                        listView.smoothScrollToPosition(position);
                        // Toast.makeText(MainActivity.this,poiInfoListForGeoCoder.get(position).getProvince(), Toast.LENGTH_SHORT).show();
                    }
                });
                return view;
            }
        };
        listView.setAdapter(adapter_searchAddress);
        adapter_searchAddress.setSelectedPosition(0);
        adapter_searchAddress.notifyDataSetInvalidated();


    }

    private void initPoiSearchListView() {
        adapter_searchAddress = new Adapter_SearchAddress2(poiInfoListForSearch, mContext, currentLatLng) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                final LinearLayout t1 = (LinearLayout) view.findViewById(R.id.t1);
                final TextView tname = (TextView) view.findViewById(R.id.tv_name);
                final TextView tadress = (TextView) view.findViewById(R.id.tv_address);
                t1.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(View v) {
                        setSelectedPosition(position);
                        notifyDataSetInvalidated();

                        lv_searchAddress.smoothScrollToPosition(position);
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            //???????????????
//            if (keyword.trim().length() > 0) {//?????????????????????????????????????????????????????????????????????
//                showMapView();
//                et_keyword.setText("");
//            } else {
//               // finish();
//            }
//            return true;
//        }
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                if(ScreenOrient(getActivity())==1){
//                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                }else {
//                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                }
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                settinsflag++;
//                if(settinsflag%2==0){
//                    h1.setVisibility(View.GONE);
//                    lv_searchAddress.setVisibility(View.GONE);
//                }else {
//                    if(ScreenOrient(getActivity())==1){
//                        h1.setVisibility(View.VISIBLE);
////                        if(!myNetworkReceiver.getNetwork().equals("???????????????")){
////
////                        }
//                        lv_searchAddress.setVisibility(View.VISIBLE);
//                    }else {
//                        Toast.makeText(mContext, "?????????????????????.", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//                return true;
//            default:
//                break;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        showMapView();
        Log.e("????????????", "onResume");
        // toastShow("????????????-onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        Log.e("????????????", "onPause");
        //toastShow("????????????-onPause");
    }

    @Override
    public void onDestroy() {
        sendPostEnd();
        Log.e("????????????", "onDestroy");
        super.onDestroy();
        //  toastShow("????????????-onDestroy");
//        mMapView.onDestroy();
//        // ?????????????????????????????????????????????
//        mBaiduMap.setMyLocationEnabled(false);
//        // ??????????????????
//        if (mLocationClient != null) {
//            mLocationClient.unRegisterLocationListener(myListener);
//        }
//        mPoiSearch.destroy();
//        mSuggestionSearch.destroy();
//        mGeoCoder.destroy();
        Log.e("aaxianchen", String.valueOf(xianchen));
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
                    Toast.makeText(mContext, "" + String.format("%s : %d%%", update.cityName,
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
                MKOLUpdateElement e = mOffline.getUpdateInfo(state);
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
            PendingIntent.getBroadcast(getActivity(), 0, gpsIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        Toast.makeText(mContext, "GPS??????:" + '\n' + "?????????" + gpslatitude + "\n" + "?????????" + gpslongitude, Toast.LENGTH_SHORT).show();

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
        //??????
//        MapStatusUpdate mapStatusUpdate  = MapStatusUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
//        mBaiduMap.animateMapStatus(mapStatusUpdate);
        LatLng pointsss = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        if (pointsss.longitude > 0 && pointsss.latitude > 0)
            pppa.add(pointsss);
        if (pppa.size() >= 2) {
            OverlayOptions mOverlay;
            if (daka == 0) {
                mOverlay = new PolylineOptions()
                        .width(10)
                        .color(0xAAFF0000)
                        .points(pppa);
            } else {
                mOverlay = new PolylineOptions()
                        .width(10)
                        .color(R.color.orange)
                        .points(pppa);
            }

            Overlay mPolyline = (Polyline) mBaiduMap.addOverlay(mOverlay);
            mPolyline.setZIndex(3);
        }

        Log.i("MyMaps", " latitude:" + bdLocation.getLatitude()
                + " longitude:" + bdLocation.getLongitude() + pppa.size());

    }


    /**
     * ????????????????????????Overlay????????????????????????
     * <p>
     * ?????? ???????????????Marker?????????overlay??????
     * </p>
     */

    public static String saveImageToGallery2(Context context, Bitmap bmp) {
        // ??????????????????
        File appDir = new File(Environment.getExternalStorageDirectory(), "jikong" + disposeTime());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // ????????????????????????????????????
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // ????????????????????????
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "????????????/pictures")));
        String path = file.getAbsolutePath();

        return path;
    }

//    // ?????? ?????????????????????
//    BaiduMap.OnMapStatusChangeListener listent = new BaiduMap.OnMapStatusChangeListener() {
//
//        @Override
//        public void onMapStatusChangeStart(MapStatus arg0) {
//            Log.e("??????", "onMapStatusChangeStart");
//        }
//
//        @Override
//        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
//
//        }
//
//        @Override
//        public void onMapStatusChangeFinish(MapStatus map) {
//            Log.e("????????????", "map" + map.zoom);
//
//        }
//
//        @Override
//        public void onMapStatusChange(MapStatus status) {
//            Log.e("?????????", "onMapStatusChange");
//            if (status.zoom < 14) {
//                try {
//                    float zoomLevel = Float.parseFloat("14");
//                    Log.e("s", "1" + zoomLevel);
//                    MapStatusUpdate u = MapStatusUpdateFactory
//                            .zoomTo(zoomLevel);
//                    mBaiduMap.animateMapStatus(u);
//                } catch (NumberFormatException e) {
//                    // Toast.makeText(this, "??????????????????????????????",
//                    // Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                // if (!mapView.isDoubleClickZooming()) {
//                // mapView.setDoubleClickZooming(true);
//                // }
//            }
//        }
//    };


    /**
     * ????????????Marker???????????????
     */
    private double calculateDistance() {
        //   pppa.add(new LatLng(39.33333,122.32135));
        //   pppa.add(new LatLng(39.33335,122.32136));
        double distance = GeoHasher.GetDistance(39.3333333, 122.3213332, 39.3333533, 122.3213532);
        return distance;
    }

    //todo ??????-----------------------------------------------------------------------------------------------------------

    private void initBluetooth() {
        deviceLv = (ListView) view.findViewById(R.id.blueList);
        scanCountTv = (TextView) view.findViewById(R.id.scan_device_count);

        adapter = new DeviceAdapter2(getActivity());
        deviceLv.setAdapter(adapter);
        InitViewPager();
        deviceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //????????????????????????????????????????????????????????????
                BluetoothLeDevice device = (BluetoothLeDevice) adapter.getItem(position);
//                if (lanyaClose2.equals("?????????") && device.getName().equals(lanyaName2)){
//                    close(closeblueview2);
//                    toastShow("??????????????????");
//                    return;
//                }
                if (device == null) {
                    return;
//                }else if (device.getName().equals("????????????")) {
//                    toastShow("?????????????????????");
//                    return;
                } else {
                    try {

                    } catch (Exception e) {
                        toastShow("??????????????????");
                    }
                    // lanyaName2= device.getName();
                    // Log.e("wzraaa",lanyaName2);
                    // pre("SHEBEIWEIHAOSHOUCHI",devices.getName());
                    String deviceName = device.getDevice().getName();
                    if (deviceName != null && !deviceName.isEmpty()) {
                        devices = device;
                        InitViewPager();
                        close(closeblueview2);
                    } else {
                        toastShow("??????????????????");
                    }
                }
            }
        });
    }
    /**
     * ??????????????????
     *
     * @param menu ??????
     * @return ????????????????????????
     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    @Override
//    public boolean onCreateOptionsMenu(final Menu menu) {
//        getMenuInflater().inflate(R.menu.scan, menu);
//        if (periodScanCallback != null && !periodScanCallback.isScanning()) {
//            menu.findItem(R.id.menu_stop).setVisible(false);
//            menu.findItem(R.id.menu_scan).setVisible(true);
//            menu.findItem(R.id.menu_refresh).setActionView(null);
//        } else {
//            menu.findItem(R.id.menu_stop).setVisible(true);
//            menu.findItem(R.id.menu_scan).setVisible(false);
//            menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_progress_indeterminate);
//        }
//        return true;
//    }

    /**
     * ????????????????????????
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan://????????????
                startScan();
                break;
            case R.id.menu_stop://????????????
                stopScan();
                break;
        }
        return true;
    }

    /**
     * ????????????
     */
    private void startScan() {
        if (adapter != null) {
            adapter.setListAll(new ArrayList<BluetoothLeDevice>());
        }
        ViseBle.getInstance().startScan(periodScanCallback);
        //invalidateOptionsMenu();
    }

    /**
     * ????????????
     */
    private void stopScan() {
        ViseBle.getInstance().stopScan(periodScanCallback);
        //invalidateOptionsMenu();
    }

    /**
     * ??????????????????????????????
     *
     * @param count
     */
    private void updateItemCount(final int count) {
        scanCountTv.setText(getString(R.string.formatter_item_count, String.valueOf(count)));
    }


    //tips
    //todo
    private void tips(final String imaPath) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.tips_che_shou3, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final TextView biaoti = view.findViewById(R.id.textView12);
        final TextView info1 = view.findViewById(R.id.info1);
        final TextView info2 = view.findViewById(R.id.info2);
        final TextView info3 = view.findViewById(R.id.info3);
        final TextView info4 = view.findViewById(R.id.info4);
        final TextView info5 = view.findViewById(R.id.info5);
        final TextView info6 = view.findViewById(R.id.info6);
        final TextView info7 = view.findViewById(R.id.info7);
        final TextView info9 = view.findViewById(R.id.info9);
        final ImageView exit = view.findViewById(R.id.exit);
        final EditText beizhu = view.findViewById(R.id.beizhu);
        final Button submit = view.findViewById(R.id.button3);
        LinearLayout ssaa = view.findViewById(R.id.ssaa);
        // imaPath = saveIma(ssaa);
//        mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
//            @SuppressLint("NewApi")
//            @Override
//            public void onSnapshotReady(Bitmap bitmap) {
//                imaPath =saveImageToGallery2(getActivity(),bitmap);
//            }
//        });
        if (getpre("setflag").equals("1")) {
            biaoti.setText("????????????");
        } else {
            biaoti.setText("????????????");
        }
        String usetime = disposeTime();
        info1.setText(" " + getpre("username"));
        info2.setText(" " + "??????");

        info4.setText(" " + usetime);

        info9.setText("  ????????????   " + getpre("starttime") + " - " + usetime);

        Glide.with(this).load(imaPath).into(exit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int id;
                //    String shebeiname;
                //    String username;
                //    String type;//????????????-??????
                //    String didian;
                //    String jingdu;
                //    String weidu;
                //    String pianyijingdu;
                //    String pianyiweidu;
                //    String nongdu;
                //    String wendu;
                //    String guangqiang;
                //    String chesu;
                //    String starttime;
                //    String endtime; //???????????? = ??????????????????
                //    String endtime2;//????????????
                //    String beizhu;
                //    String ImagePath;
                //    String grade;//1 2 3??????
                //    String flag;//1?????? 2??????
                //    int uid;
                ShouDongQuZheng mShouDongQuZheng = new ShouDongQuZheng();
                mShouDongQuZheng.setShebeiname(devices.getName());
                mShouDongQuZheng.setUsername(getpre("username"));
                mShouDongQuZheng.setType("??????");
                mShouDongQuZheng.setDidian("dsadsa");
                mShouDongQuZheng.setJingdu("111");
                mShouDongQuZheng.setWeidu("123");
                mShouDongQuZheng.setPianyijingdu("23");
                mShouDongQuZheng.setPianyiweidu("111");
                mShouDongQuZheng.setNongdu("11");
                mShouDongQuZheng.setWendu("23");
                mShouDongQuZheng.setGuangqiang("23");
                mShouDongQuZheng.setChesu("0");
                mShouDongQuZheng.setStarttime(disposeTime());
                mShouDongQuZheng.setEndtime(disposeTime());
                mShouDongQuZheng.setEndtime2(disposeTime2());
                mShouDongQuZheng.setBeizhu("aaa");
                mShouDongQuZheng.setImagePath(imaPath);
                mShouDongQuZheng.setGrade("1");
                mShouDongQuZheng.setFlag("1");
                mShouDongQuZheng.setUid("1");
                addCheZaiBaoJingData(myDatebaseHelper, mShouDongQuZheng);
                toastShow("????????????...");

                beizhu.clearFocus();
                beizhu.setVisibility(View.GONE);
                closejianpan2(getActivity(), beizhu);
                dialog.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                beizhu.clearFocus();
                beizhu.setVisibility(View.GONE);
                closejianpan2(getActivity(), beizhu);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public String saveIma(View view) {
        String path = "";

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "????????????" + disposeTime() + ".jpg");
        Bitmap bitmap = screenShot(getActivity());

//        if(ScrccView!=null){
//
//        }
        Log.e("aaazzz", "1");
        // Bitmap bitmap = getViewBitmap(view);
        //Bitmap bitmap = screenShot(activity);
        Log.e("aaazzz", "2");
        try {
            if (!file.exists())
                file.createNewFile();
            Log.e("aaazzz", "3");
            boolean ret = saveSrc(bitmap, file, Bitmap.CompressFormat.JPEG, true);
            if (ret) {
                path = file.getAbsolutePath();
                Toast.makeText(getActivity().getApplicationContext(), "?????????????????? " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("aaazzz", "4");
            e.printStackTrace();
        }
        Log.e("aaazzz", "5");
        //final ImageView imageView = view.findViewById(R.id.imageView4);
        //        Glide.with(this).load(as).into(imageView);
        return path;
    }

    public Bitmap shotActivity(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bp = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(),
                view.getMeasuredHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bp;
    }

    public static Bitmap getViewBitmap(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(), (int) v.getX() + v.getMeasuredWidth(), (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }

        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return bitmap;
    }

    /**
     * ?????????????????????File???
     *
     * @param src     ?????????
     * @param file    ?????????????????????
     * @param format  ??????
     * @param recycle ????????????
     * @return true ?????? false ??????
     */
    public static boolean saveSrc(Bitmap src, File file, Bitmap.CompressFormat format, boolean recycle) {
        if (isEmptyBitmap(src))
            return false;

        OutputStream os;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled())
                src.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * Bitmap?????????????????????
     */
    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    /**
     * This is an example for using Shooter.
     * This method will request permission and take screenshot on this Activity.
     */
    public void onClickReqPermission(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            startActivityForResult(createScreenCaptureIntent(), REQ_CODE_PER);
        }
    }

    /**
     * using {@see ScreenShotActivity} to take screenshot on current Activity directly.
     * If you press home it will take screenshot on another app.
     *
     * @param view
     */
    public void onClickShot(View view) {
        startActivityForResult(ScreenShotActivity.createIntent(getActivity(), null, 0), REQ_CODE_ACT);
        // toast("Press home key,open another app.");//if you want to take screenshot on another app.
    }


    @SuppressLint("NewApi")
    private String getSavedPath() {
        return getActivity().getExternalFilesDir("screenshot").getAbsoluteFile() + "/"
                + SystemClock.currentThreadTimeMillis() + disposeTime() + ".png";
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQ_CODE_ACT: {
                if (resultCode == RESULT_OK && data != null) {
                    // toast("Screenshot saved at " + data.getData().toString());

                } else {
                    // toast("You got wrong.");
                }
            }
            break;
            case REQ_CODE_PER: {
                if (resultCode == RESULT_OK && data != null) {
                    String path = getSavedPath();
                    Shooter shooter = new Shooter(getActivity(), resultCode, data);
                    shooter.startScreenShot(path, new Shooter.OnShotListener() {
                                @Override
                                public void onFinish(String path) {
                                    //here is done status.
                                    // toast("Screenshot saved at " + path);
                                    toastShow(path);
                                    tips(path);

                                }

                                @Override
                                public void onError() {
                                    // toast("You got wrong.");
                                }
                            }
                    );
                } else if (resultCode == RESULT_CANCELED) {
                    //user canceled.
                    Log.e("quanxians", "quxiao");
                } else {

                }
            }
        }
    }

    private void tips_xiazai() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_xiazai, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        final ArrayList<MKOLSearchRecord> records2 = mOffline.getOfflineCityList();
        // Toast.makeText(mContext, "" + records2.get(1).cityName, Toast.LENGTH_SHORT).show();
        final lixiancityAdapter adapter2 = new lixiancityAdapter(getActivity(), records2);
        final ListView lixianListview = (ListView) view.findViewById(R.id.lixianListview);
        lixianListview.setAdapter(adapter2);
        lixianListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOffline.start(records2.get(position).cityID);
                Toast.makeText(mContext, "?????????...???????????????app", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void sendPostStart() {
        Map<String, String> hs = new HashMap<>();
        //http://localhost:6060/user/updateEvent?starttime=1111
        // &endtime=2222&statusId=3333&deviceName=3232&eventId=5555
        Random random = new Random();
        Log.e("aamima", createMima());
        pre("sendpoststarttime", disposeTime());
        hs.put("starttime", getpre("sendpoststarttime"));
        hs.put("endtime", "?????????");
        hs.put("statusId", "?????????");
        hs.put("deviceName", getpre("androidIds"));
        String eventId = disposeTime3() + createMima() + String.valueOf(random.nextInt(900000) + 1250);
        hs.put("eventId", eventId);
        pre("eventId", eventId);
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SaveEventurl, hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                if (result.contains("??????")) {

                } else {
                    toastShow("?????????????????????");
                }
            }
        });
    }

    private void sendPostEnd() {
        Map<String, String> hs = new HashMap<>();
        //http://localhost:6060/user/updateEvent?starttime=1111
        // &endtime=2222&statusId=3333&deviceName=3232&eventId=5555
        hs.put("starttime", getpre("sendpoststarttime"));
        hs.put("endtime", disposeTime());
        hs.put("statusId", "????????????");
        hs.put("deviceName", getpre("androidIds"));
        hs.put("eventId", getpre("eventId"));
        pre("eventId", getpre("eventId") + "end");
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SaveEventurl, hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                if (result.contains("??????")) {

                } else {
                    toastShow("?????????????????????");
                }
            }
        });
    }


}