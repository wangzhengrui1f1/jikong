package com.vise.bledemo.chezai.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ZoomControls;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.vise.baseble.ViseBle;
import com.vise.baseble.callback.scan.IScanCallback;
import com.vise.baseble.callback.scan.ScanCallback;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.BluetoothLeDeviceStore;
import com.vise.baseble.utils.HexUtil;
import com.vise.bledemo.Base.BaseFragment;
import com.vise.bledemo.R;
import com.vise.bledemo.aliyun.ALiUploadManager;
import com.vise.bledemo.baiduMap.baiduMap.adapter.Adapter_SearchAddress;
import com.vise.bledemo.baiduMap.baiduMap.adapter.lixianListAdapter;
import com.vise.bledemo.baiduMap.baiduMap.adapter.lixiancityAdapter;
import com.vise.bledemo.baiduMap.baiduMap.view.WelcomeActivity;
import com.vise.bledemo.baiduMap.baiduMap.view.lixianList;
import com.vise.bledemo.baiduMap.utils.MyNetworkReceiver;
import com.vise.bledemo.bluetooth.adapter.DeviceAdapter;
import com.vise.bledemo.bluetooth.common.BluetoothDeviceManager;
import com.vise.bledemo.chezai.bean.ShouDongQuZheng;
import com.vise.bledemo.shouchi.fragment.blueToothDataFragment;
import com.vise.bledemo.sqlite.MyDatabase;
import com.vise.bledemo.tcp.TaskCenter;
import com.vise.bledemo.utils.ConnectionDetector;
import com.vise.bledemo.utils.GeoHasher;
import com.vise.bledemo.utils.Jiexi;
import com.vise.bledemo.utils.OkHttpUtils;
import com.vise.bledemo.utils.screen.ScreenShotActivity;
import com.vise.bledemo.utils.screen.Shooter;
import com.vise.bledemo.utils.screen.luxiang.ScreenCaptureService;
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
import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WINDOW_SERVICE;
import static com.vise.bledemo.R.mipmap.xiaoche;
import static com.vise.bledemo.aliyun.ALiUploadManager.initAlyun;
import static com.vise.bledemo.shouchi.data.Data_all.devices;
import static com.vise.bledemo.shouchi.data.Data_all.pppa;
import static com.vise.bledemo.shouchi.fragment.SoneFragment.Shouchisendlocation;
import static com.vise.bledemo.sqlite.sqliteMethod.addCheZaiBaoJingData;
import static com.vise.bledemo.sqlite.sqliteMethod.lessUid;
import static com.vise.bledemo.utils.Jiexi.a1;
import static com.vise.bledemo.utils.Jiexi.jiexiwifi10;
import static com.vise.bledemo.utils.Jiexi.jiexiwifi13;
import static com.vise.bledemo.utils.Jiexi.nowifidata10;
import static com.vise.bledemo.utils.Jiexi.nowifidata13;
import static com.vise.bledemo.utils.Jiexi.ssmShouDongQuZheng;
import static com.vise.bledemo.utils.Jiexi.wifidata10;
import static com.vise.bledemo.utils.Jiexi.wifidata13;
import static com.vise.bledemo.utils.LongitudelatitudeUtil.GCJ02_To_BD09;
import static com.vise.bledemo.utils.LongitudelatitudeUtil.WGS84_To_GCJ02;
import static com.vise.bledemo.utils.Mima.createMima;
import static com.vise.bledemo.utils.UseFngfa.closejianpan2;
import static com.vise.bledemo.utils.ViewBean.tname;
import static com.vise.bledemo.utils.getTime.disposeTime;
import static com.vise.bledemo.utils.getTime.disposeTime2;
import static com.vise.bledemo.utils.getTime.disposeTime3;
import static com.vise.bledemo.utils.url.DownController;
import static com.vise.bledemo.utils.url.LeftController;
import static com.vise.bledemo.utils.url.RightController;
import static com.vise.bledemo.utils.url.SQLITE_NAME;
import static com.vise.bledemo.utils.url.SaveAlert;
import static com.vise.bledemo.utils.url.SaveEventurl;
import static com.vise.bledemo.utils.url.SavePlaceUrl;
import static com.vise.bledemo.utils.url.StopController;
import static com.vise.bledemo.utils.url.SuduController;
import static com.vise.bledemo.utils.url.UpController;
import static com.vise.bledemo.utils.url.alyima1;
import static com.vise.bledemo.utils.url.alyima2;
import static com.vise.bledemo.utils.url.bfm;
import static com.vise.bledemo.utils.url.getWifiData;
import static com.vise.bledemo.utils.url.getWifiData2;
import static com.vise.bledemo.utils.url.isStart;
import static com.vise.bledemo.utils.url.lvguang;
import static com.vise.utils.handler.HandlerUtil.runOnUiThread;


public class SoneFragment1 extends BaseFragment implements OnGetPoiSearchResultListener
        , MKOfflineMapListener {

    public static final String ACCESS_ID = "LTAI5t5nHEUsjSY5EnZTjzBD";                                  //?????????ID
    public static final String ACCESS_KEY = "L3r0Fnd2VbVTNYcXAAIJ1N6YI2AEmI";                           //?????????KEY
    public static final String ACCESS_BUCKET_NAME = "lingjiedian";
    public static final String ACCESS_ENDPOINT = "http://oss-cn-beijing.aliyuncs.com/";
    public static final String ACCESS_DOMAINNAME = "http:xxxxx";
    View view;
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
    private MapView mMapView2 = null;
    private BaiduMap mBaiduMap = null;
    private BaiduMap mBaiduMap2 = null;
    private MyLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;
    private LocationClientOption option = null;
    private boolean isFirstLocation = true;
    private LatLng currentLatLng = null;//??????????????????
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
    private LinearLayout up, down, left, right;
    private TextView xiazai, textview_lixianditu, text5, text_add, text_number, text_less, username;
    boolean isFrist = true;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private int FirstFlag = 0, FirstFlag2 = 0;
    private MyNetworkReceiver myNetworkReceiver;
    private int myNetwork = 0;
    //todo ??????????????? ??????????????????
    private int runtime = 10;
    private int daka = 0;
    private BDLocation dakalocation;
    private LinearLayout ls1, ls2, ls6, ls7, ls8, ls11, ls12, ls15, sview1, sview2;
    private ImageView ima1, ima2, back, lvguangb, tname3;
    private int shensuo = 0;//3?????? 2?????? 1??????
    List<LatLng> list = new ArrayList<>();
    private TextView text3, text_shebei;
    Boolean lineStop = false;
    //todo ??????------------------------------------------------------------------
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;
    //?????? ??????---
    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;
    //?????????---
    private FragmentManager fragmentManager2 = null;
    private FragmentTransaction fragmentTransaction2 = null;
    int ss = 0;
    private ListView deviceLv;
    private TextView scanCountTv, text_nongdu, text_wendu, text_guangqiang;
    //?????????????????????????????????
    private DeviceAdapter adapter;
    private BluetoothLeDeviceStore bluetoothLeDeviceStore = new BluetoothLeDeviceStore();
    private int SxtTime = 300;
    private ProgressBar pro_guangqiang;
    private int dialogFlag = 0;
    BDLocation sendlocation = null;
    //?????????
    View displayView;
    private WindowManager windowManager2;
    private WindowManager.LayoutParams layoutParams2;
    int DisFirst = 0, xunjianFlag = 0, sFlag = 0;

    int saveNongdu = 0;
    List<String> sendList = new ArrayList<>();
    int sendListIndex = 0;
    //todo ???????????? 0?????? 1???
    int isBj = 0;
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
    boolean appQian = true;
    int findex = 0;
    private ViewPager main_viewPager;
    private List<Integer> laa = new ArrayList<>();
    //todo ???????????????
    int VisDialog = 0;
    //todo wifi
    private TextView kaishi, qiehuan, quzheng;
    //todo ?????????????????????????????????
    public Thread dataCheckThread;
    //
    public Handler dataCheckHandler = new Handler() {
        @SuppressLint("NewApi")
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 70) {
                if (displayView.getVisibility() == View.VISIBLE) {
                    displayView.setVisibility(View.GONE);
                }
            }
            if (msg.what == 1001) {
                if (xunjianFlag == 1) {
                    tname3.setBackground(getActivity().getDrawable(R.drawable.shuangchuan2));
                }
            }
            if (msg.what == 1002) {
                if (xunjianFlag == 1) {
                    tname3.setBackground(getActivity().getDrawable(R.drawable.shangchuan));
                }
            }
            if (msg.what == 1003) {
                tname3.setBackground(null);
            }
            //????????????view
            //todo ????????????
            if (msg.what == 101) {
                text_shebei.setText(String.valueOf(ss++));
                //todo ???????????????
            } else if (msg.what == 50 && wifidata13.size() == 12) {
                Random random = new Random();
                int s = random.nextInt(30000) + 1;
//                if(findex>23){
//                    findex = 0;
//                }
//                wifidata13.set(0, String.valueOf(laa.get(findex++)));
                // saveIma();
                mylista.add(Integer.valueOf(wifidata13.get(0)));
                Log.e("jeixiwifi13", wifidata13.get(0));
                if (getpre("TESTTYPE_CHEZAI").equals("PPM.M")) {
                    text_nongdu.setText(String.valueOf(wifidata13.get(0)) + " PPM.M");
                } else if (getpre("TESTTYPE_CHEZAI").equals("VOL.M")) {
                    text_nongdu.setText(String.valueOf(Float.valueOf(wifidata13.get(0)) / 500) + " VOL.M");
                } else if (getpre("TESTTYPE_CHEZAI").equals("LEL.M")) {
                    text_nongdu.setText(String.valueOf(Float.valueOf(wifidata13.get(0)) / 1000) + " LEL.M");
                }
                // ????????????
                if (Integer.valueOf(wifidata13.get(0)) >= Integer.valueOf(getpre("GAOJINGZHI1_CHEZAI"))
                        && VisDialog == 0) {
                    if (sFlag == 0) {
                        nowifidata13 = wifidata13;
                        nowifidata10 = wifidata10;
                        Log.e("nowifidata", nowifidata13.get(0));
                        pre("setflag", "2");
                        setQuz();
                        onClickReqPermission();
                        //baojings();
                        saveNongdu = Integer.parseInt(wifidata13.get(0));
                        sFlag = 1;
                        VisDialog = 1;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialogFlag = 0;
                                sFlag = 0;
                            }
                        }, 30000);
                    }
                }
                if (mylista.size() > 25) {
                    mylista.remove(0);
                }
                if (mylista.get(23) < Integer.valueOf(getpre("GAOJINGZHI1_CHEZAI")) &&
                        mylista.get(24) > Integer.valueOf(getpre("GAOJINGZHI1_CHEZAI")) && VisDialog == 0) {
                    pre("setflag", "2");
                    VisDialog = 1;
                    nowifidata13 = wifidata13;
                    onClickReqPermission();
                    // baojings();
                }
                setData2(mylista);
                if (wifidata10.size() >= 3) {
                    text_wendu.setText(Float.valueOf(wifidata10.get(2)) * 1.852 + "km/h");
                    // text_wendu.setText(Float.valueOf(String.valueOf(s))*1.852+"km/h");
                    Log.e("eeeeeeeee", "111111");
                }
                Log.e("eeeeeeeee", String.valueOf(wifidata10.size()));
                pro_guangqiang.setProgress(Integer.parseInt(wifidata13.get(2)));
                text_guangqiang.setText(wifidata13.get(2));


                // wifidata13.set(3,"1");
                //todo ?????? 0 ??? 1???
                if (wifidata13.get(3).equals("1")) {
                    lvguangb.setImageDrawable(getResources().getDrawable(R.drawable.jiguang2));
                } else {
                    lvguangb.setImageDrawable(getResources().getDrawable(R.drawable.jiguang));
                }
            } else if (msg.what == 77) {
                Log.e("sadasdtan", "aaa");

            }
        }
    };
    private int sendPlaceFalg = 0;

    private void setQuz() {
        //TODO ????????????
        //???????????????
        //TODO ?????? 2
        //????????????
        //???????????????
        //TODO ?????????????????? 5
        //??????
        //????????????
        //???????????? 8
        //?????????????????????
        //??????????????????
        //???????????? 11
//                wifidata13.clear();
        //nowShouDongQuZheng.setWendu();
    }

    //todo ?????????
    List<Integer> mylista = new ArrayList<>();
    LineDataSet lineDataSet;
    LineChart lineChart;
    int shituqiehuanFlag = 0;
    private ImageView sxt;

    ShouDongQuZheng nowShouDongQuZheng = new ShouDongQuZheng();
    //todo ??????
    Activity activity = getActivity();
    private static final int REQ_CODE_PER = 0x2304;
    private static final int REQ_CODE_ACT = 0x2305;

    //todo ??????
    private MediaProjectionManager mMediaProjectionManager;
    public static MediaProjection mMediaProjection;
    ToggleButton tbtScreenCaptureService;

    private ScreenCaptureService.MyBinder mBinder;
    private boolean SERVICE_HAS_BIND = false;
    private boolean SERVICE_IS_START = false;
    private boolean SC_IS_RUN = false;

    private static final int CAPTURE_CODE = 115;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 123;
    private List<String> timeList = new ArrayList<>();
    private int saveTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        view = inflater.inflate(R.layout.activity_mainchezai, container, false);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//?????????????????????
            //??????????????????,200????????????
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_SHORT).show();
            //????????????

        }
        wifidata10.add("0");
        wifidata10.add("0");
        wifidata10.add("0");
        sendList.add(getWifiData);
        sendList.add(getWifiData2);
        for (int i = 0; i < 25; i++) {
            mylista.add(0);
        }
        laa.add(10000);
        laa.add(15500);
        laa.add(15500);
        laa.add(18500);
        laa.add(17500);
        laa.add(19500);
        laa.add(18500);
        laa.add(17500);
        laa.add(19000);
        laa.add(19000);
        laa.add(17000);
        laa.add(16000);
        laa.add(18000);
        laa.add(13000);
        laa.add(12000);
        laa.add(15500);
        laa.add(10000);
        laa.add(10000);
        laa.add(10000);
        laa.add(10000);
        laa.add(10000);
        laa.add(10000);
        laa.add(10000);
        laa.add(10000);
        //saveIma();
        initAlyun(getActivity());
        myDatebaseHelper = new MyDatabase(getActivity(), SQLITE_NAME, null, 1);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        displayView = layoutInflater.inflate(R.layout.image_display, null);
        activity = getActivity();
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
        lixiancityAdapter adapter2 = new lixiancityAdapter(getActivity(), records2);
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


//        if(wifidata10.size()>0){
//            View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dakaitem, null);
//        mBaiduMap.addOverlay(new MarkerOptions()
//                .position(new LatLng(Float.valueOf(wifidata10.get(1)), Float.valueOf(wifidata10.get(0))))
//                .icon(BitmapDescriptorFactory.fromView(inflate)));
//        }

//        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dakaitem, null);
//        mBaiduMap.addOverlay(new MarkerOptions()
//                .position(new LatLng(38.93697955999, 121.65663953))
//                .icon(BitmapDescriptorFactory.fromView(inflate)));
//
//        mBaiduMap.addOverlay(new MarkerOptions()
//                .position(new LatLng(38.92697955999, 121.63663953))
//                .icon(BitmapDescriptorFactory.fromView(inflate)));
//
//        mBaiduMap.addOverlay(new MarkerOptions()
//                .position(new LatLng(38.92697955999, 121.53663953))
//                .icon(BitmapDescriptorFactory.fromView(inflate)));
//        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                Toast.makeText(mContext, "?????????????????????", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        //mmend
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(lineStop==true){
//                    lineStop = false;
//                    Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
//                }else {
//                    lineStop = true;
//                    Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(mContext, "" + calculateDistance(), Toast.LENGTH_SHORT).show();
                if (calculateDistance() > 0) {
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
                }
            }
        });
        // pppa.add(new LatLng(39.3333333,122.3213533));
        // pppa.add(new LatLng(39.3333332,122.3213532));
        //????????????????????????
        // updateDataByBuleTooth();

        Handler hs = new Handler();
        hs.postDelayed(new Runnable() {
            @Override
            public void run() {
                pppa.clear();
            }
        }, 15000);


        mMapView.showZoomControls(false);
        // mMapView2.showZoomControls(false);
        //

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMapView.bringToFront();
                //  mMapView2.bringToFront();
            }
        }, 20000);
        View views = view.findViewById(R.id.bmapView);
        views.bringToFront();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMapView.setStateListAnimator(null);
            //  mMapView2.setStateListAnimator(null);
        }

        //todo ??????????????????
        initWIfi();

        InitWifiController();
        connect();

        //?????????
        InitView(730, 850, 1657, 668);

        // ??????logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        child = mMapView2.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        mMapView.showZoomControls(false);
        mMapView2.showZoomControls(false);

        //todo ??????????????????
        updateData();
        //checknetwork();

        initLuzhi();
        return view;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initLuzhi() {
        mMediaProjectionManager = (MediaProjectionManager) getActivity().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        ReadRunState();
        myBindService();
    }

    /**
     * ???????????????view??????
     *
     * @param v ????????????view
     * @return Bitmap
     */
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


    private void updateData() {
        if (dataCheckThread == null) {
            dataCheckThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try {
                            //??????500ms
                            sleep(150);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //?????????????????????
                        //TODO ??????
                        if (xunjianFlag == 1) {
                            //??????wifi??????
                            if (sendListIndex >= sendList.size()) {
                                sendListIndex = 0;
                            }
                            Log.e("sendListindex", String.valueOf(sendListIndex));
                            sendMessage(sendList.get(sendListIndex));
                            sendListIndex++;
                        } else {
                            dataCheckHandler.sendEmptyMessage(1003);
                        }

                        saveTime += 150;

                        Log.e("xianchen", "start");

                        if (sendPlaceFalg > 40 && sendlocation != null && wifidata13.size() == 12) {
                            Log.e("sendpostaa1", "start");
                            sendPlaceFalg = 0;
                            CheckData();
                        } else {
                            sendPlaceFalg++;
                        }

//                            if(!appQian){
//                                dataCheckHandler.sendEmptyMessage(70);
//                            }
                        //?????????????????????
                        //dataCheckHandler.sendEmptyMessage(50);
                    }
                }
            };
            dataCheckThread.start();
        }
    }

    private void initView() {
        h1 = (LinearLayout) view.findViewById(R.id.h1);
        mContext = getActivity();
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        et_keyword = (EditText) view.findViewById(R.id.et_keyword);
        ll_mapView = (FrameLayout) view.findViewById(R.id.ll_mapView);
        ll_poiSearch = (LinearLayout) view.findViewById(R.id.ll_poiSearch);
        lv_searchAddress = (ListView) view.findViewById(R.id.lv_searchAddress);
        lv_poiSearch = (ListView) view.findViewById(R.id.lv_poiSearch);
        xiazai = (TextView) view.findViewById(R.id.xiazai);
        textview_lixianditu = (TextView) view.findViewById(R.id.textview_lixianditu);
        ls1 = (LinearLayout) view.findViewById(R.id.ls1);
        ls2 = (LinearLayout) view.findViewById(R.id.ls2);
        ls6 = (LinearLayout) view.findViewById(R.id.ls6);
        ls7 = (LinearLayout) view.findViewById(R.id.ls7);
        ls8 = (LinearLayout) view.findViewById(R.id.ls8);
        ls11 = (LinearLayout) view.findViewById(R.id.ls11);
        ls12 = (LinearLayout) view.findViewById(R.id.ls12);
        ls15 = (LinearLayout) view.findViewById(R.id.ls15);
        ima1 = (ImageView) view.findViewById(R.id.ima1);
        ima2 = (ImageView) view.findViewById(R.id.ima2);
        back = (ImageView) view.findViewById(R.id.back);
        lvguangb = (ImageView) view.findViewById(R.id.imageView6);
        text5 = (TextView) view.findViewById(R.id.text5);
        text5.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//?????????
        ls6.setVisibility(View.GONE);
        ls7.setVisibility(View.GONE);
        Log.e("ls6ls7", "gone");
        text3 = (TextView) view.findViewById(R.id.text3);
        text_shebei = (TextView) view.findViewById(R.id.text_shebei);
        kaishi = (TextView) view.findViewById(R.id.kaishi);
        qiehuan = (TextView) view.findViewById(R.id.qiehuan);
        quzheng = (TextView) view.findViewById(R.id.quzheng);
        text_nongdu = (TextView) view.findViewById(R.id.text_nongdu);
        lineChart = (LineChart) view.findViewById(R.id.lineChart);
        sview1 = (LinearLayout) view.findViewById(R.id.sview1);
        sview2 = (LinearLayout) view.findViewById(R.id.sview2);
        text_wendu = (TextView) view.findViewById(R.id.text_wendu);
        pro_guangqiang = (ProgressBar) view.findViewById(R.id.pro_guangqiang);
        text_guangqiang = (TextView) view.findViewById(R.id.text_guangqiang);
        text_add = (TextView) view.findViewById(R.id.text_add);
        text_number = (TextView) view.findViewById(R.id.text_number);
        text_less = (TextView) view.findViewById(R.id.text_less);
        username = (TextView) view.findViewById(R.id.username);
        sxt = (ImageView) view.findViewById(R.id.sxt);
        tname3 = (ImageView) view.findViewById(R.id.tname3);
        lvguangb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(lvguang);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendMessage(StopController);
                    }
                }, 500);
            }
        });
        sxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "??????????????????,?????????????????????...", Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getFragmentManager();
                final shexiangtouFragment id = (shexiangtouFragment) fragmentManager.findFragmentByTag("shipin");
                id.Zhuce();
                id.Bofang();

            }
        });

        username.setText("| " + getpre("username"));
        //TODO ???????????????14
        pro_guangqiang.setMax(14);
        kaishi.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (kaishi.getText().toString().equals("????????????")) {
                    pre("starttime", disposeTime());
                    //todo ????????????
                    xunjianFlag = 1;
                    kaishi.setText("????????????");
                    kaishi.setBackgroundResource(R.drawable.bbhuang);
                    kaishi.setTextColor(getResources().getColor(R.color.black));
                    Log.e("xunjian-????????????", getpre("starttime"));
                    sendPostStart();
                    TaskCenter.sharedCenter().connect(getpre("e6"), Integer.parseInt(getpre("e8")));
                } else {
                    //todo ????????????
                    xunjianFlag = 0;
                    kaishi.setText("????????????");
                    kaishi.setTextColor(getResources().getColor(R.color.white));
                    kaishi.setBackgroundResource(R.drawable.sbutton);
                    sendPostEnd();
                }
                //todo  ?????????
//                if (SC_IS_RUN) {
//                    //todo ????????????
//                    xunjianFlag = 0;
//                    kaishi.setText("????????????");
//                    kaishi.setTextColor(getResources().getColor(R.color.white));
//                    kaishi.setBackgroundResource(R.drawable.sbutton);
//                    ReadRunState();
//                    if (SC_IS_RUN) {
//                        mBinder.StopScreenCapture();
//                        SC_IS_RUN = false;
//                        SaveRunState(SERVICE_IS_START, SERVICE_HAS_BIND, SC_IS_RUN);
//                        //????????????  SPFilename ????????????
//                        if (timeList.size() > 0) {
////                            ChezaiShiping chezaiShiping = new ChezaiShiping();
////                            chezaiShiping.setFlag("no");
////                            chezaiShiping.setPath(SPFilename);
////                            chezaiShiping.setTime(timeList.toString());
//                        }
//                        Toast.makeText(getActivity(), "??????????????????????????????", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getActivity(), "????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    timeList.clear();
//                    saveTime = 0;
//                    pre("starttime",disposeTime());
//                    //todo ????????????
//                    xunjianFlag = 1;
//                    kaishi.setText("????????????");
//                    kaishi.setBackgroundResource(R.drawable.bbhuang);
//                    kaishi.setTextColor(getResources().getColor(R.color.black));
//                    Log.e("xunjian-????????????",getpre("starttime"));
//
//                    if (!SC_IS_RUN) {
//                        startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), CAPTURE_CODE);
//                    }
//                }
            }
        });
        qiehuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shituqiehuanFlag++;
                //todo ????????????
                if (shituqiehuanFlag % 2 == 0) {
                    sview1.setVisibility(View.VISIBLE);
                    sview2.setVisibility(View.GONE);
                    sxt.setVisibility(View.VISIBLE);
                    //todo ????????????
                } else if (shituqiehuanFlag % 2 == 1) {
                    sview2.setVisibility(View.VISIBLE);
                    sview1.setVisibility(View.GONE);
                    ls8.setVisibility(View.GONE);
                    sxt.setVisibility(View.GONE);
                }


            }
        });
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        quzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getpre("starttime").equals("1")) {
                    pre("setflag", "1");
                    if (wifidata13.size() == 12) {
                        nowifidata13 = wifidata13;
                        onClickReqPermission();
                        // baojings();
                        //createScreenCaptureIntenttips_che_shou(saveIma());

                    } else {
                        toastShow("??????????????????.");
                    }

                } else {
                    toastShow("???????????????");
                    Log.e("ajietu", "1");

                }
            }
        });
        text_number.setText(getpre("setchesu"));
        text_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(getpre("setchesu")) < 64) {
                    String chesu = String.valueOf(Integer.valueOf(getpre("setchesu")) + 1);
                    text_number.setText(chesu);
                    pre("setchesu", chesu);
                    if (Integer.valueOf(chesu) < 10) {
                        sendMessage(SuduController + "0" + chesu);
                        Log.e("setchesu1", SuduController + "0" + chesu);
                    } else {
                        sendMessage(SuduController + chesu);
                        Log.e("setchesu2", SuduController + chesu);
                    }
                } else {
                    toastShow("?????????????????????");
                }

            }
        });

        text_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.valueOf(getpre("setchesu")) > 0) {
                    String chesu = String.valueOf(Integer.valueOf(getpre("setchesu")) - 1);
                    text_number.setText(chesu);
                    pre("setchesu", chesu);
                    if (Integer.valueOf(chesu) < 10) {
                        sendMessage(SuduController + "0" + chesu);
                        Log.e("setchesu1", SuduController + "0" + chesu);
                    } else {
                        sendMessage(SuduController + chesu);
                        Log.e("setchesu2", SuduController + chesu);
                    }

                } else {
                    toastShow("?????????????????????");
                }

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

    public void InitViewPager() {
        if (fragmentManager == null) {
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.zt1, new blueToothDataFragment(), "one");
            fragmentTransaction.commit();
        } else {
            //??????
            fragmentManager = getFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag("one");
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
            //??????
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.zt1, new blueToothDataFragment(), "one");
            fragmentTransaction.commit();
        }

        //??????


    }


    @Deprecated
    public static Bitmap createBitmapFromView(View view) {
        Bitmap bitmap;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap src = view.getDrawingCache();
        bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight());
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);
        src.recycle();
        src = null;
        return bitmap;
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
                if (shensuo == 2 || shensuo == 0) {
                    shensuo = 1;
                    params.weight = 1.0f;
                    ls6.setVisibility(View.GONE);
                    ls7.setVisibility(View.GONE);
                } else if (shensuo == 1) {
                    shensuo = 2;
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
//                mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
//                    @SuppressLint("NewApi")
//                    @Override
//                    public void onSnapshotReady(Bitmap bitmap) {
//                        ima1.setBackground(null);
//                        ima1.setImageBitmap(bitmap);
//                        saveImageToGallery(getActivity(),bitmap);
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                ima1.setBackground(getActivity().getDrawable(R.drawable.xx2));
//                                ima1.setImageBitmap(null);
//                            }
//                        },1500);
//                    }
//                });
                // ??????????????????
//                Rect rect = new Rect(0, 0, 8000, 8000);// ???xy ???xy
//                mBaiduMap.snapshotScope(rect, new BaiduMap.SnapshotReadyCallback() {
//                    @Override
//                    public void onSnapshotReady(Bitmap snapshot) {
//                        File file = new File("/mnt/sdcard/testal"+disposeTime()+".png");
//                        FileOutputStream out;
//                        try {
//                            out = new FileOutputStream(file);
//                            if (snapshot.compress(Bitmap.CompressFormat.PNG, 100, out)) {
//                                out.flush();
//                                out.close();
//                            }
//                            Toast.makeText(getActivity(), "?????????????????????????????????: " + file.toString(),
//                                    Toast.LENGTH_SHORT).show();
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                });
//                Toast.makeText(getActivity(), "????????????????????????...", Toast.LENGTH_SHORT).show();

//                // ??????????????????
//                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(pppa.get(pppa.size() - 1));
//                mBaiduMap.animateMapStatus(mapStatusUpdate);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 0);
                shensuo = 0;
                params.weight = 0.1f;
                ls2.setLayoutParams(params);
                //????????????
                ls15.setLayoutParams(params);
                ls12.setVisibility(View.GONE);
                return true;
            }
        });
        ima2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        ima2.setBackground(null);
                        ima2.setImageBitmap(bitmap);
                        saveImageToGallery(getActivity(), bitmap);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ima2.setBackground(getActivity().getDrawable(R.drawable.qiandao));
                                ima2.setImageBitmap(null);
                            }
                        }, 1500);
                    }
                });
                return false;
            }
        });

        text_shebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan();
                if (deviceLv.getVisibility() == View.GONE) {
                    deviceLv.setVisibility(View.VISIBLE);
                } else {
                    deviceLv.setVisibility(View.GONE);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());

            }
        });

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
        mMapView2 = (MapView) displayView.findViewById(R.id.bmapView2);
        final Button move = displayView.findViewById(R.id.button4);
        final Button xiazai = displayView.findViewById(R.id.button11);
        final Button ditulist = displayView.findViewById(R.id.button12);
        final Button guiji = displayView.findViewById(R.id.guiji);
        mBaiduMap = mMapView2.getMap();
        mBaiduMap2 = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap2.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // ??????????????????
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap2.setMyLocationEnabled(true);

        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMapView2.setVisibility(View.GONE);
                move.setVisibility(View.VISIBLE);
                xiazai.setVisibility(View.VISIBLE);
                ditulist.setVisibility(View.VISIBLE);
                guiji.setVisibility(View.VISIBLE);
            }
        });
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView2.setVisibility(View.VISIBLE);
                move.setVisibility(View.GONE);
                xiazai.setVisibility(View.GONE);
                ditulist.setVisibility(View.GONE);
                guiji.setVisibility(View.GONE);
            }
        });
        guiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lineStop) {
                    lineStop = false;
                    guiji.setText("????????????");
                } else {
                    lineStop = true;
                    pppa.clear();
                    // mBaiduMap.clear();
                    guiji.setText("????????????");
                }
            }
        });
        xiazai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tips_xiazai();
            }
        });
        ditulist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent =new Intent(getActivity(), lixianList.class);
//                startActivity(intent);
                tips_xiazailist();
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
//        option.setScanSpan(10000);

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
                if (lineStop == true) {
                    onReceiveLocation2(location);
                    if (runtime >= 10) {
                        pppa.add(new LatLng(location.getLatitude(), location.getLongitude()));
                        runtime = 0;
                        ls.add("da" + disposeTime() + '\n');
                        Log.e("aatime--da", disposeTime());
                        runtime++;
                    }
                }
                ConnectionDetector cd = new ConnectionDetector(getActivity().getApplicationContext());
                Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
                //  Toast.makeText(mContext, ""+location.getLatitude(), Toast.LENGTH_SHORT).show();
                if (!isInternetPresent) {
                    //????????????
//                    location.setLatitude(gpslatitude+0.008774687519+0.00074331248099);
//                    location.setLongitude(gpslongitude+0.00374531687912+0.00960631645);
//                    Log.e("aaasss", (gpslatitude+0.008774687519+0.00074331248099)+"GPS????????????---"+(gpslongitude+0.00374531687912+0.00960631645));
                }
                ls.add(disposeTime() + '\n');
                //Toast.makeText(mContext, ls.toString(), Toast.LENGTH_SHORT).show();
                Log.e("aatime", disposeTime());

                navigateTo(location);
                cityName = location.getCity();
                tv_city.setText(cityName);
                sendlocation = location;
                Log.i("baidu_location_result", "location type = " + location.getLocType());
                Log.i("baidu_location_result1", "location type = " + location.getLatitude());
                Log.i("baidu_location_result2", "location type = " + location.getLongitude());
            }

        }
    }

    private void checknetwork() {
//        //??????????????????
        myNetworkReceiver = new MyNetworkReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(myNetworkReceiver, intentFilter);
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
            mBaiduMap2.animateMapStatus(MapStatusUpdateFactory
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
        mBaiduMap2.setMyLocationData(locationData);
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

        mBaiduMap2.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                // TODO Auto-generated method stub
                //??????????????????
            }
        });
        mBaiduMap2.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

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
                LatLng latlng = mBaiduMap2.getMapStatus().target;
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
                center = mBaiduMap2.getMapStatus().target;
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
     * @param point Marker????????? ??????
     */
    private void addMarker(LatLng point) {
        if (marker != null) {
//            marker.remove();
            marker.setPosition(point);
        } else {
            //??????Marker??????
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(xiaoche);
            //??????MarkerOption???????????????????????????Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //??????????????????Marker????????????
            marker = (Marker) mBaiduMap.addOverlay(option);
            marker = (Marker) mBaiduMap2.addOverlay(option);
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
                    Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
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
        adapter_searchAddress = new Adapter_SearchAddress(poiInfoListForGeoCoder, mContext, currentLatLng) {
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
                        lv_searchAddress.smoothScrollToPosition(position);
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
        adapter_searchAddress = new Adapter_SearchAddress(poiInfoListForSearch, mContext, currentLatLng) {
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
    public void onResume() {
        super.onResume();
        // mMapView.onResume();
        // showMapView();
        Log.e("onresume11", "1");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fragmentManager2 == null) {
                    fragmentManager2 = getFragmentManager();
                    fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.add(R.id.ship, new shexiangtouFragment(), "shipin");
                    fragmentTransaction2.commit();
                    SxtTime = 0;
                } else {
//                        //??????
                    fragmentManager2 = getFragmentManager();
                    Fragment fragment = fragmentManager2.findFragmentByTag("shipin");
                    fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.remove(fragment);
                    fragmentTransaction2.commit();
                    //??????
                    fragmentManager2 = getFragmentManager();
                    fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.add(R.id.ship, new shexiangtouFragment(), "shipin");
                    fragmentTransaction2.commit();

                }
                if (isBackground(getActivity())) {

                }

                Log.e("displayView", "222");
                Log.e("displayView", "333");
                appQian = isBackground(getActivity());
            }
        }, SxtTime);

        if (displayView.getVisibility() == View.GONE) {
            displayView.setVisibility(View.VISIBLE);
            Log.e("displayView", "111");
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        // mMapView.onPause();
        if (!isBackground(getActivity())) {
            if (displayView.getVisibility() == View.VISIBLE) {
                displayView.setVisibility(View.GONE);
            }
        }
        appQian = isBackground(getActivity());
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
////        mMapView.onDestroy();
////        // ?????????????????????????????????????????????
////        mBaiduMap.setMyLocationEnabled(false);
////        // ??????????????????
////        if (mLocationClient != null) {
////            mLocationClient.unRegisterLocationListener(myListener);
////        }
////        mPoiSearch.destroy();
////        mSuggestionSearch.destroy();
////        mGeoCoder.destroy();
//    }

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
        double a[] = WGS84_To_GCJ02(gpslongitude, gpslatitude);
        double b[] = GCJ02_To_BD09(a[0], a[1]);
        Log.e("pianyi gps?????????", String.valueOf(gpslongitude) + "---" + String.valueOf(gpslatitude));
        Log.e("pianyi gps?????????", String.valueOf(b[0]) + "---" + String.valueOf(b[1]));
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
        mBaiduMap2.setMyLocationData(locData);
        LatLng latLng = new LatLng(bdLocation.getLatitude(),
                bdLocation.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mBaiduMap2.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
        mBaiduMap2.animateMapStatus(msu);
        LatLng pointsss = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        if (pointsss.longitude > 0 && pointsss.latitude > 0)
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

    public static void saveImageToGallery(Context context, Bitmap bmp) {
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
        deviceLv = (ListView) view.findViewById(android.R.id.list);
        scanCountTv = (TextView) view.findViewById(R.id.scan_device_count);

        adapter = new DeviceAdapter(getActivity());
        deviceLv.setAdapter(adapter);

        deviceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //????????????????????????????????????????????????????????????
                BluetoothLeDevice device = (BluetoothLeDevice) adapter.getItem(position);
                if (device == null) {
                    return;
                } else {
                    devices = device;
                    InitViewPager();
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
        updateItemCount(0);
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
        // invalidateOptionsMenu();
    }

    /**
     * ??????????????????????????????
     *
     * @param count
     */
    private void updateItemCount(final int count) {
        scanCountTv.setText(getString(R.string.formatter_item_count, String.valueOf(count)));
    }


    //todo ----------------------------------------------------------------------------------------------------------
    //????????????
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //TODO ????????????
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (fragmentManager2 == null) {

                    } else {
                        if (displayView.getVisibility() == View.GONE) {
                            displayView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }, 300);
            if (DisFirst >= 1) {
                if (displayView.getVisibility() == View.GONE) {
                    displayView.setVisibility(View.VISIBLE);
                }
            }

        } else {
//            //TODO ???????????????
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (DisFirst >= 1) {
                        if (displayView.getVisibility() == View.VISIBLE) {
                            displayView.setVisibility(View.GONE);
                        }
                    }
                    DisFirst++;
                    FirstFlag++;
                }
            }, 300);
            if (DisFirst >= 1) {
                if (displayView.getVisibility() == View.VISIBLE) {
                    displayView.setVisibility(View.GONE);
                }
            }
        }
    }

    //todo wifi?????????????????????
    public static void sendMessage(String msg) {
        Log.i("TaskCenter", "????????????");
        Log.e("biaofeng", msg);
        //todo ????????????????????????????????????????????????
        TaskCenter.sharedCenter().send(HexUtil.decodeHex(msg.toCharArray()));
    }

    //todo ?????????wifi?????????-??????s??????
    public void initWIfi() {
        TaskCenter.sharedCenter().setDisconnectedCallback(new TaskCenter.OnServerDisconnectedCallbackBlock() {
            @Override
            public void callback(IOException e) {
                //textView_receive.setText(textView_receive.getText().toString() + "????????????" + "\n");

            }
        });

        TaskCenter.sharedCenter().setConnectedCallback(new TaskCenter.OnServerConnectedCallbackBlock() {
            @Override
            public void callback() {
                // textView_receive.setText(textView_receive.getText().toString() + "????????????" + "\n");
                Log.i("TaskCenter", "????????????");
            }
        });

        TaskCenter.sharedCenter().setReceivedCallback(new TaskCenter.OnReceiveCallbackBlock() {
            @Override
            public void callback(String receicedMessage) {
                Log.e("sscallback", receicedMessage);
                //TODO ????????????
                //???????????????
                //TODO ?????? 2
                //????????????
                //???????????????
                //TODO ?????????????????? 5
                //??????
                //????????????
                //???????????? 8
                //?????????????????????
                //??????????????????
                //???????????? 11
//                wifidata13.clear();
                //  receicedMessage = "00000000000F01030C42F2FE18421B649D00000000";

                char ss[] = receicedMessage.toCharArray();
                if (ss[14] == '0' && ss[15] == '3' && ss[16] == '1') {
                    wifidata13 = jiexiwifi13(receicedMessage);
                    Log.e("aaaduandian", wifidata13.toString());
                } else if (ss[14] == '0' && ss[15] == '3' && ss[16] == '0') {
                    wifidata10 = jiexiwifi10(receicedMessage);
                    Log.e("aaaduandian", wifidata10.toString());
                }

                Random random = new Random();
                int s = random.nextInt(30000) + 1;
                // wifidata13.set(0,""+s);
                Log.e("duandian", "2");
                Log.e("dsasdad", String.valueOf(wifidata13.size()));
                Log.e("dsasdad--10", String.valueOf(wifidata10.size()));
                if (wifidata13.size() == 12) {
                    //?????????????????????
                    dataCheckHandler.sendEmptyMessage(50);
                    //todo ??????
                    Log.e("duandian", "3");
                }
                Log.e("duandian", "4");
                Log.e("duandian", receicedMessage);
                //todo ????????????
                char bfchar[] = receicedMessage.toCharArray();
                Log.e("duandian", "5");
                if (bfchar.length >= 2) {
                    Log.e("duandian", "6");
                    if (bfchar.length < 3 && bfchar[0] == '0' && bfchar[1] == '0') {
                        bfm = "??????";
                    } else if (bfchar.length < 3 && bfchar[0] == '0' && bfchar[1] == '2') {
                        bfm = "??????";
                    }
                }
                Log.e("duandian", "7");
                if (receicedMessage.equals("00000000000601060D66000C")) {
                    Log.i("TaskCenter2", "?????????...");
                }


                // textView_receive.setText(textView_receive.getText().toString() +"????????????"+ receicedMessage + "\n");
            }
        });
    }


    //todo ??????
    public void connect() {
        // TaskCenter.sharedCenter().connect("192.168.1.195",503);
        TaskCenter.sharedCenter().connect(getpre("e6"), 503);
        Log.e("sconnect", getpre("e6"));

    }

    //todo ????????????
    public void disconnect() {
        TaskCenter.sharedCenter().disconnect();
    }

    private void InitWifiController() {
        up = view.findViewById(R.id.up);
        down = view.findViewById(R.id.down);
        left = view.findViewById(R.id.left);
        right = view.findViewById(R.id.right);

        up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sendMessage(UpController);
                        //      Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        sendMessage(StopController);
                        //       Toast.makeText(mContext, "???", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;//true?????????????????????????????????????????????????????????????????????
            }
        });

        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sendMessage(DownController);
                        //    Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        sendMessage(StopController);
                        // Toast.makeText(mContext, "???", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;//true?????????????????????????????????????????????????????????????????????
            }
        });

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sendMessage(LeftController);
                        //     Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        sendMessage(StopController);
                        //    Toast.makeText(mContext, "???", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;//true?????????????????????????????????????????????????????????????????????
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sendMessage(RightController);
                        //     Toast.makeText(mContext, "??????", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        sendMessage(StopController);
                        //    Toast.makeText(mContext, "???", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;//true?????????????????????????????????????????????????????????????????????
            }
        });
    }


    //todo

    /**
     * ?????????????????????
     *
     * @param list ?????????
     */
    public void setData2(final List<Integer> list) {

        //????????????
        lineChart.setDrawBorders(false);
        //????????????
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            entries.add(new Entry(i, (float) list.get(i)));
        }
        //??????LineDataSet???????????????
        lineDataSet = new LineDataSet(entries, "");
        //?????????
        lineDataSet.setColor(Color.parseColor("#F15A4A"));
        //?????????
        lineDataSet.setLineWidth(1.6f);
        //???????????????
        lineDataSet.setDrawCircles(true);
        //????????????
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillAlpha(50);
        lineDataSet.setFillColor(Color.RED);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setLineWidth(1.75f); // ??????
        lineDataSet.setCircleSize(5f);// ?????????????????????
        lineDataSet.setDrawCircles(false);// ???????????????
        lineDataSet.setDrawValues(true);//?????????????????????????????????
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//????????????
        lineDataSet.setHighlightEnabled(true); // allow highlighting for DataSet
        lineDataSet.setHighLightColor(R.color.huangse2);
        lineDataSet.setHighlightLineWidth(2);

        ChartHighlighter highlight = new ChartHighlighter(lineChart);

        lineChart.setHighlighter(highlight);

        // set this to false to disable the drawing of highlight indicator (lines)
        lineDataSet.setDrawHighlightIndicators(true);
        LineData data = new LineData(lineDataSet);
        //???????????????????????????
        lineChart.setNoDataText("??????????????????");
        //????????????????????????
        data.setDrawValues(false);
        //??????X???
        XAxis xAxis = lineChart.getXAxis();
        //??????X??????????????????????????????)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //??????X??????????????????????????????
        xAxis.setGranularity(1f);
        //??????????????????
        xAxis.setDrawGridLines(false);
        Legend mLegend = lineChart.getLegend(); //?????????????????????????????????y???value???
        mLegend.setForm(Legend.LegendForm.LINE); //???????????????
        mLegend.setFormSize(6f); //??????
//        mLegend.setXEntrySpace(6f); //????????????????????? legend-entries ?????????
        mLegend.setYEntrySpace(6f); //????????????????????? legend-entries ?????????
        mLegend.setFormToTextSpace(6f); //?????? legend-form ??? legend-label ???????????????
        mLegend.setWordWrapEnabled(true); //?????? Legend ????????????????????? ???????????????BelowChartLeft???BelowChartRight
        mLegend.setTextColor(Color.RED); //??????????????????
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT); //??????????????????????????????
        //mLegend.setTypeface(Typeface.DEFAULT); //???????????????????????????
        mLegend.setEnabled(true);
        //??????Y???
        YAxis yAxis = lineChart.getAxisLeft();
        YAxis rightYAxis = lineChart.getAxisRight();
        YAxis left = lineChart.getAxisLeft();
        left.setEnabled(true); //??????Y????????????
        //??????Y???????????????
        rightYAxis.setEnabled(false); //??????Y????????????
        //??????y??????????????????????????????
        //??????????????????
        yAxis.setDrawGridLines(false);
        //??????Y??????????????????????????????
        yAxis.setGranularity(1);
//        lineChart.setHighlightPerTapEnabled(false);
//        lineChart.setHighlightPerDragEnabled(false);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        //????????????
        lineChart.setData(data);
        //????????????
        lineChart.invalidate();
    }


    //todo
    private void tips(final String imaPath) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.tips_che_shou, null);
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
        final TextView info8 = view.findViewById(R.id.info8);
        final TextView info9 = view.findViewById(R.id.info9);
        final ImageView exit = view.findViewById(R.id.exit);
        final EditText beizhu = view.findViewById(R.id.beizhu);
        final Button submit = view.findViewById(R.id.button3);
        if (getpre("setflag").equals("1")) {
            biaoti.setText("????????????");
        } else {
            biaoti.setText("????????????");
        }
        VisDialog = 1;
        dialogFlag = 1;
        String usetime = disposeTime();
        info1.setText(" " + getpre("username"));
        info2.setText(" " + "??????");

        info4.setText(" " + usetime);

        if (nowifidata13.size() == 12) {
            info5.setText(" " + nowifidata13.get(0));
            info6.setText(" " + nowifidata13.get(4));
            info7.setText(" " + nowifidata13.get(2));
            info8.setText(" 0");
        } else {
            info5.setText("---");
            info6.setText("---");
            info7.setText("---");
            info8.setText("---");
        }

        info9.setText("  ????????????   " + getpre("starttime") + " - " + usetime);

//        Log.e("save2",String.valueOf(currentLatLng.latitude));
//        Log.e("save2",String.valueOf(currentLatLng.longitude));
//        double a[]=WGS84_To_GCJ02(currentLatLng.latitude,currentLatLng.longitude);
//        final double b[]=GCJ02_To_BD09(a[0],a[1]);
//        Log.e("save2",String.valueOf(b[0]));
//        Log.e("save2",String.valueOf(b[1]));
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

                int nd = 0;
                if (nowifidata13.size() == 12) {
                    nd = Integer.parseInt(nowifidata13.get(0));
                }
                ShouDongQuZheng mShouDongQuZheng = new ShouDongQuZheng();
                mShouDongQuZheng.setShebeiname(getpre("SHEBEIWEIHAO"));
                mShouDongQuZheng.setUsername(getpre("username"));
                mShouDongQuZheng.setType("??????");
                mShouDongQuZheng.setDidian("????????????");
                //todo ?????????
                Log.e("aadata1", String.valueOf("start"));
                if (currentLatLng != null) {
                    Log.e("aadata1", String.valueOf(currentLatLng.latitude));
                    Log.e("aadata2", String.valueOf(currentLatLng.longitude));
                    mShouDongQuZheng.setJingdu(String.valueOf(currentLatLng.latitude));
                    mShouDongQuZheng.setWeidu(String.valueOf(currentLatLng.longitude));
                } else {
                    mShouDongQuZheng.setJingdu(String.valueOf("0.000000000000"));
                    mShouDongQuZheng.setWeidu(String.valueOf("0.000000000000"));
                }

//                mShouDongQuZheng.setPianyijingdu(String.valueOf(b[0]));
//                mShouDongQuZheng.setPianyiweidu(String.valueOf(b[1]));
                if (nowifidata10.size() >= 3) {
                    mShouDongQuZheng.setPianyiweidu(nowifidata10.get(0));
                    mShouDongQuZheng.setPianyijingdu(nowifidata10.get(1));
                    mShouDongQuZheng.setChesu(nowifidata10.get(2));
                } else {
                    mShouDongQuZheng.setPianyijingdu("0");
                    mShouDongQuZheng.setPianyiweidu("0");
                    mShouDongQuZheng.setChesu("0");
                }
                if (nowifidata13.get(4) == null) {
                    mShouDongQuZheng.setNongdu(String.valueOf("0"));
                } else {
                    mShouDongQuZheng.setNongdu(String.valueOf(nd));
                }

//                mShouDongQuZheng.setWendu(wifidata13.get(4));
//                mShouDongQuZheng.setGuangqiang(wifidata13.get(2));
                if (nowifidata13.get(4) == null) {
                    mShouDongQuZheng.setWendu("0");
                } else {
                    mShouDongQuZheng.setWendu(nowifidata13.get(4));
                }
                if (nowifidata13.get(2) == null) {
                    mShouDongQuZheng.setGuangqiang("0");
                } else {
                    mShouDongQuZheng.setGuangqiang(nowifidata13.get(2));
                }

                mShouDongQuZheng.setImagePath2("111");

                mShouDongQuZheng.setStarttime(disposeTime());
                mShouDongQuZheng.setEndtime(disposeTime());
                mShouDongQuZheng.setEndtime2(disposeTime2());
                mShouDongQuZheng.setBeizhu(beizhu.getText().toString());
                mShouDongQuZheng.setImagePath(imaPath);

                if (Integer.valueOf(nd) >= Integer.valueOf(getpre("GAOJINGZHI1_CHEZAI"))
                        && Integer.valueOf(nd) < Integer.valueOf(getpre("GAOJINGZHI2_CHEZAI"))) {
                    mShouDongQuZheng.setGrade("1");
                } else if (Integer.valueOf(nd) >= Integer.valueOf(getpre("GAOJINGZHI2_CHEZAI"))
                        && Integer.valueOf(nd) < Integer.valueOf(getpre("GAOJINGZHI3_CHEZAI"))) {
                    mShouDongQuZheng.setGrade("2");
                } else if (Integer.valueOf(nd) >= Integer.valueOf(getpre("GAOJINGZHI3_CHEZAI"))) {
                    mShouDongQuZheng.setGrade("3");
                } else {
                    mShouDongQuZheng.setGrade("0");
                }
                //??????
                mShouDongQuZheng.setFlag(getpre("setflag"));
                Random random = new Random();
                mShouDongQuZheng.setUid(disposeTime3() + String.valueOf(random.nextInt(90000000) + 1));
                addCheZaiBaoJingData(myDatebaseHelper, mShouDongQuZheng);
                toastShow("????????????...");
                //??????
                alyima1 = "create";
                try {
                    Log.e("aliy", mShouDongQuZheng.getImagePath());
                    aliyunShangchuan(mShouDongQuZheng.getImagePath(), mShouDongQuZheng);
                } catch (EnumConstantNotPresentException e) {

                }
                if (displayView.getVisibility() == View.GONE) {
                    displayView.setVisibility(View.VISIBLE);
                }
                SendPost2(mShouDongQuZheng);
                VisDialog = 0;
                beizhu.clearFocus();
                beizhu.setVisibility(View.GONE);
                closejianpan2(getActivity(), beizhu);
                dialog.dismiss();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (displayView.getVisibility() == View.GONE) {
                    displayView.setVisibility(View.VISIBLE);
                }
                VisDialog = 0;
                beizhu.clearFocus();
                beizhu.setVisibility(View.GONE);
                closejianpan2(getActivity(), beizhu);
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    @TargetApi(Build.VERSION_CODES.FROYO)
    public String saveIma() {
        String path = "";

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "????????????" + disposeTime() + ".jpg");
        // Bitmap bitmap = screenShot(getActivity());
        Bitmap bitmap = getViewBitmap(activity.getWindow().getDecorView());
        //Bitmap bitmap = screenShot(activity);

        try {
            if (!file.exists())
                file.createNewFile();

            boolean ret = saveSrc(bitmap, file, Bitmap.CompressFormat.JPEG, true);
            if (ret) {
                path = file.getAbsolutePath();
                //Toast.makeText(getActivity().getApplicationContext(), "?????????????????? " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //final ImageView imageView = view.findViewById(R.id.imageView4);
        //        Glide.with(this).load(as).into(imageView);
        return path;
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

    public void InitView(int width, int height, int x, int y) {
        //todo ?????????
        windowManager2 = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        layoutParams2 = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams2.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams2.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams2.format = PixelFormat.RGBA_8888;
        layoutParams2.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams2.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams2.width = width;
        layoutParams2.height = height;
        layoutParams2.x = x;
        layoutParams2.y = y;
        initFloatView();
    }

    private void initFloatView() {
        displayView.setOnTouchListener(new FloatingOnTouchListener());
        windowManager2.addView(displayView, layoutParams2);
    }

    private void updateFloatView() {
        layoutParams2.width = 1000;
        layoutParams2.height = 1000;
        layoutParams2.x = 10;
        layoutParams2.y = 100;
        windowManager2.updateViewLayout(view, layoutParams2);
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams2.x = layoutParams2.x + movedX;
                    layoutParams2.y = layoutParams2.y + movedY;
                    Log.e("movex", String.valueOf(layoutParams2.x));
                    Log.e("movey", String.valueOf(layoutParams2.y));
                    windowManager2.updateViewLayout(view, layoutParams2);
                    break;
                default:
                    break;
            }
            return false;
        }
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
        toast("Press home key,open another app.");//if you want to take screenshot on another app.
    }

    @SuppressLint("NewApi")
    private String getSavedPath() {
        return getActivity().getExternalFilesDir("screenshot").getAbsoluteFile() + "/"
                + SystemClock.currentThreadTimeMillis() + disposeTime() + ".png";
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("onActivityResult", "onActivityResult");
        //????????????
        if (requestCode == CAPTURE_CODE) {
            if (resultCode != RESULT_OK) {
                SC_IS_RUN = true;
                if (displayView.getVisibility() == View.GONE) {
                    displayView.setVisibility(View.VISIBLE);
                }
                return;
            } else {
                if (displayView.getVisibility() == View.GONE) {
                    displayView.setVisibility(View.VISIBLE);
                }
                mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
                ReadRunState();
                if (!SC_IS_RUN) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        //??????API-23???????????????Android???????????????
                        AskForPermission();
                    }
                    myShareScreen();
                    Toast.makeText(getActivity(), "??????????????????????????????", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "?????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }

        }
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
                                    if (displayView.getVisibility() == View.VISIBLE) {
                                        displayView.setVisibility(View.GONE);
                                    }
                                    tips(path);
                                    Log.e("aapath", path);
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
                    VisDialog = 0;
                } else {

                }
            }
        }
    }

    private void baojings() {
        timeList.add(String.valueOf(saveTime));
        Log.e("wwzr", timeList.toString());
        String path = "wu";
        if (displayView.getVisibility() == View.VISIBLE) {
            displayView.setVisibility(View.GONE);
        }
        tips(path);
    }

    private void toast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
    }

    private void goBackground() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
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

    private void tips_xiazailist() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_xiazai, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();

        final ListView lixianListview = (ListView) view.findViewById(R.id.lixianListview);

        final ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo();
        if (localMapList != null) {
            Toast.makeText(getActivity(), "??????????????????????????????" + localMapList.size(), Toast.LENGTH_SHORT).show();
            lixianListAdapter adapter = new lixianListAdapter(getActivity(), localMapList) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    final View view = super.getView(position, convertView, parent);
                    final TextView ss4 = (TextView) view.findViewById(R.id.ss4);
                    ss4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOffline.remove(localMapList.get(position).cityID);
                            Toast.makeText(getActivity(), localMapList.get(position).cityName + "????????????", Toast.LENGTH_SHORT).show();
//                            init();
                        }
                    });

                    return view;
                }
            };
            lixianListview.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "??????????????????????????????0", Toast.LENGTH_SHORT).show();
        }
        //Log.e("ssaazzz", String.valueOf(isNetworkOnline()));
        dialog.show();
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "??????"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "??????"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    //todo --------------????????????-------------------------------------------------------------------------------------


    private void AskForPermission() {
        Log.v("AskForPermission()", "AskForPermission()");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.v("AskForPermission()", "requestPermissions");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            Log.v("onActivityResult", "myThread.start(); start");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.v("PermissionsResult", "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("PermissionsResult", "myThread.start(); start");
                } else {
                    Log.i("PermissionsResult", "WRITE_EXTERNAL_STORAGE permission denied");
                }
                break;
            default:
                break;
        }
    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (ScreenCaptureService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void myShareScreen() {
        Intent intent = new Intent(getActivity(), ScreenCaptureService.class);
        myStartService(intent);
        mBinder.StartScreenCapture();
        SC_IS_RUN = true;
        SaveRunState(SERVICE_IS_START, SERVICE_HAS_BIND, SC_IS_RUN);
    }

    @Override
    public void onDestroy() {
        myUnbindService();
        myStopService();
        super.onDestroy();

    }

    private void myBindService() {
        ReadRunState();
        Intent bindIntent = new Intent(getActivity(), ScreenCaptureService.class);
        getActivity().bindService(bindIntent, connection, BIND_AUTO_CREATE);
        SERVICE_HAS_BIND = true;
        SaveRunState(SERVICE_IS_START, SERVICE_HAS_BIND, SC_IS_RUN);
        Log.v(TAG, "bindService");
    }

    private void myUnbindService() {
        ReadRunState();
        if (SERVICE_HAS_BIND) {
            getActivity().unbindService(connection);
            SERVICE_HAS_BIND = false;
            Log.v(TAG, "unbindService");
        }
        SaveRunState(SERVICE_IS_START, SERVICE_HAS_BIND, SC_IS_RUN);
    }

    private void myStartService(Intent startIntent) {
        ReadRunState();
        getActivity().startService(startIntent);
        SERVICE_IS_START = true;
        SaveRunState(SERVICE_IS_START, SERVICE_HAS_BIND, SC_IS_RUN);
        Log.v(TAG, "startService");
    }

    private void myStopService() {
        ReadRunState();
        if ((SERVICE_IS_START) && (!SC_IS_RUN)) {
            Intent stopIntent = new Intent(getActivity(), ScreenCaptureService.class);
            getActivity().stopService(stopIntent);
            SERVICE_IS_START = false;
            releaseEncoder();
            Log.v(TAG, "stopService");
        }
        SaveRunState(SERVICE_IS_START, SERVICE_HAS_BIND, SC_IS_RUN);
    }

    private void SaveRunState(boolean bSERVICE_IS_START, boolean bSERVICE_HAS_BIND, boolean bSC_IS_RUN) {
        SC_IS_RUN = bSC_IS_RUN;
        SERVICE_HAS_BIND = bSERVICE_HAS_BIND;
        SERVICE_IS_START = bSERVICE_IS_START;
        //?????????SharedPreferences?????????????????????
        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(TAG, MODE_PRIVATE);
        //?????????SharedPreferences.Editor?????????????????????
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //???putString?????????????????????
        editor.putBoolean("SC_IS_RUN", SC_IS_RUN);
        editor.putBoolean("SERVICE_HAS_BIND", SERVICE_HAS_BIND);
        editor.putBoolean("SERVICE_IS_START", SERVICE_IS_START);
        //??????????????????
        editor.commit();
        Log.v("SaveRunState", "SERVICE_IS_START = " + SERVICE_IS_START
                + ";SERVICE_HAS_BIND = " + SERVICE_HAS_BIND + ";SC_IS_RUN = " + SC_IS_RUN);
    }

    private void ReadRunState() {
        //??????????????????SharedPreferences??????????????????????????????SharedPreferences??????
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(TAG, MODE_PRIVATE);
        // ??????getString????????????value????????????2????????????value????????????
        SC_IS_RUN = sharedPreferences.getBoolean("SC_IS_RUN", false);
        SERVICE_HAS_BIND = sharedPreferences.getBoolean("SERVICE_HAS_BIND", false);
        SERVICE_IS_START = sharedPreferences.getBoolean("SERVICE_IS_START", false);
        //Log.v("ReadRunState","SERVICE_IS_START = "+SERVICE_IS_START+";SERVICE_HAS_BIND = "+SERVICE_HAS_BIND+";SC_IS_RUN = "+SC_IS_RUN);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void releaseEncoder() {
        Log.d(TAG, "releasing encoder objects");

        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }


    public static void cutVideo() {

    }


    //todo ???????????????
    public void aliyunShangchuan(String path, final ShouDongQuZheng mShouDongQuZheng) {
        ALiUploadManager.getInstance().uploadFile(path, new ALiUploadManager.ALiCallBack() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result, String url) {
                Log.e("?????????????????????:", url);
                Log.e("?????????????????????:", result.toString());
                String url2 = "https://" + ACCESS_BUCKET_NAME + "." + ACCESS_ENDPOINT + "/" + "aaa";
                Log.e("?????????????????????:", url2);
                // putImage(url);  //????????????????????????
                Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                Log.e("?????????????????????clientExcepion:",
                        clientExcepion.getMessage() + ",serviceException:" + serviceException);
//                ToastUtil.showShortToast(ImageSubmitActivity.this, "????????????");
                Toast.makeText(getActivity(), "????????????", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void process(long currentSize, long totalSize) {
                Log.e("?????????:", (currentSize * 100) / totalSize + "%");

            }
        });
    }

    //todo ???????????? http://123.56.230.79:6799/user/JikongSave/SaveAlert?
    // deviceName=321321&userName=321&type=32&longitude=321&latitude=321&
    // gpslongitude=5125&gpslatitude=21424&concentration=321&temperature=3213&
    // lightIntensity=4214&nospeed=5215&starttime=3123&endtime=321312&grade=5215&automatic=312321&uid=3213
    public void SendPost2(ShouDongQuZheng mShouDongQuZheng) {
        final String uid = mShouDongQuZheng.getUid() + "AA";
        final int id = mShouDongQuZheng.getId();
        Log.e("sssendpost2", "1111");
        Map<String, String> hs = new HashMap<>();
        //http://localhost:8080/user/saveShops?latitude=33&longitude=23&placeTime=dsdsad&placeName=909
        //INSERT INTO alerts (deviceName, userName, type, longitude, latitude
        //        , gpslongitude, gpslatitude, CH4, temperature, lightIntensity
        //        , nospeed, starttime, endtime, grade, automatic, uid,screenshotImaPath,
        //        photoImaPath, remarks, resolve, implementer, exe_time)
        hs.put("deviceName", getpre("androidIds"));
        hs.put("userName", getpre("username"));
        hs.put("type", mShouDongQuZheng.getType());
        hs.put("longitude", mShouDongQuZheng.getJingdu());
        hs.put("latitude", mShouDongQuZheng.getWeidu());
        hs.put("gpslongitude", String.valueOf(mShouDongQuZheng.getJingdu()));
        hs.put("gpslatitude", String.valueOf(mShouDongQuZheng.getWeidu()));
        hs.put("CH4", mShouDongQuZheng.getNongdu());
        hs.put("temperature", mShouDongQuZheng.getWendu());
        hs.put("lightIntensity", mShouDongQuZheng.getGuangqiang());
        hs.put("nospeed", mShouDongQuZheng.getChesu());
        hs.put("starttime", mShouDongQuZheng.getStarttime());
        hs.put("endtime", mShouDongQuZheng.getEndtime());
        hs.put("grade", mShouDongQuZheng.getGrade());
        hs.put("automatic", mShouDongQuZheng.getFlag());
        hs.put("uid", mShouDongQuZheng.getUid());
        hs.put("screenshotImaPath", alyima1);
        hs.put("photoImaPath", alyima2);
        hs.put("remarks", mShouDongQuZheng.getBeizhu());
        hs.put("resolve", "0");
        Log.e("aass22", "??????");
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SaveAlert, hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                if (result.contains("??????")) {
                    toastShow("????????????");
                    lessUid(myDatebaseHelper, uid, id);
                    Log.e("aass22", "????????????");
                } else {

                }
            }
        });
    }

    public void SendPost(ShouDongQuZheng ssmShouDongQuZheng) {
        Map<String, String> hs = new HashMap<>();
        //http://localhost:8080/user/saveShops?latitude=33&longitude=23&placeTime=dsdsad&placeName=909
        //latitude, longitude, placeTime, placeName, deviceStatus,
        //        userName,deviceName)
        //(#{latitude},#{longitude},
        //                      #{placeTime},#{placeName},#{deviceStatus},#{userName}
        //                      ,#{deviceName},#{alerts},#{fault},#{CH4},#{nospeed},#{video}
        //                      ,#{uid},#{temperature},#{light},#{IntervalData});
        ////http://localhost:8080/user/saveShops?latitude=33&longitude=23&placeTime=dsdsad&placeName=909
        //        //latitude, longitude, placeTime, placeName, deviceStatus,
        //        //        userName,deviceName)
        //        //(#{latitude},#{longitude},
        //        //                      #{placeTime},#{placeName},#{deviceStatus},#{userName}
        //        //                      ,#{deviceName},#{alerts},#{fault},#{CH4},#{nospeed},#{video}
        //        //                      ,#{uid},#{temperature},#{light},#{IntervalData});
        //        ////TODO ????????????
        //        //                //???????????????
        //        //                //TODO ?????? 2
        //        //                //????????????
        //        //                //???????????????
        //        //                //TODO ?????????????????? 5
        //        //                //??????
        //        //                //????????????
        //        //                //???????????? 8
        //        //                //?????????????????????
        //        //                //??????????????????
        //        //                //???????????? 11
        dataCheckHandler.sendEmptyMessage(1002);
        hs.put("latitude", String.valueOf(sendlocation.getLatitude()));
        hs.put("longitude", String.valueOf(sendlocation.getLongitude()));
        hs.put("placeName", "??????");
        //todo ???????????????
        if (!isStart) {
            hs.put("deviceStatus", "??????");
        } else {
            hs.put("deviceStatus", "??????");
        }
        hs.put("userName", getpre("username"));
        hs.put("deviceName", getpre("androidIds"));
        hs.put("alerts", "??????");
        hs.put("fault", "??????");
        hs.put("CH4", ssmShouDongQuZheng.getNongdu());
        hs.put("nospeed", ssmShouDongQuZheng.getChesu());
        hs.put("video", "??????");
        hs.put("uid", getpre("eventId"));
        hs.put("temperature", ssmShouDongQuZheng.getWendu());
        hs.put("light", ssmShouDongQuZheng.getGuangqiang());
        hs.put("IntervalData", "??????");
        hs.put("placeTime", disposeTime());
        Log.e("send--post2", "1");
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SavePlaceUrl, hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                if (result.contains("??????")) {
                    dataCheckHandler.sendEmptyMessage(1001);
                    Log.e("send--post2", "2");
                } else {
                    Log.e("send--post2", "3");
                }
            }
        });
        Log.e("send--post2", ssmShouDongQuZheng.toString());
    }

    public void CheckData() {
        int nd = 0;
        if (wifidata13.size() == 12) {
            nd = Integer.parseInt(wifidata13.get(0));
        }
        ShouDongQuZheng mShouDongQuZheng = new ShouDongQuZheng();
        mShouDongQuZheng.setShebeiname(getpre("SHEBEIWEIHAO"));
        mShouDongQuZheng.setUsername(getpre("username"));
        mShouDongQuZheng.setType("??????");
        mShouDongQuZheng.setDidian("????????????");
        //todo ?????????
        Log.e("aadata1", String.valueOf("start"));
        if (currentLatLng != null) {
            Log.e("aadata1", String.valueOf(currentLatLng.latitude));
            Log.e("aadata2", String.valueOf(currentLatLng.longitude));
            mShouDongQuZheng.setJingdu(String.valueOf(currentLatLng.latitude));
            mShouDongQuZheng.setWeidu(String.valueOf(currentLatLng.longitude));
        } else {
            mShouDongQuZheng.setJingdu(String.valueOf("0.000000000000"));
            mShouDongQuZheng.setWeidu(String.valueOf("0.000000000000"));
        }

        if (wifidata10.size() >= 3) {
            mShouDongQuZheng.setPianyiweidu(wifidata10.get(0));
            mShouDongQuZheng.setPianyijingdu(wifidata10.get(1));
            mShouDongQuZheng.setChesu(wifidata10.get(2));
        } else {
            mShouDongQuZheng.setPianyijingdu("0");
            mShouDongQuZheng.setPianyiweidu("0");
            mShouDongQuZheng.setChesu("0");
        }
        if (wifidata13.get(4) == null) {
            mShouDongQuZheng.setNongdu(String.valueOf("0"));
        } else {
            mShouDongQuZheng.setNongdu(String.valueOf(nd));
        }

        if (wifidata13.get(4) == null) {
            mShouDongQuZheng.setWendu("0");
        } else {
            mShouDongQuZheng.setWendu(wifidata13.get(4));
        }
        if (wifidata13.get(2) == null) {
            mShouDongQuZheng.setGuangqiang("0");
        } else {
            mShouDongQuZheng.setGuangqiang(wifidata13.get(2));
        }

        mShouDongQuZheng.setImagePath2("111");

        mShouDongQuZheng.setStarttime(disposeTime());
        mShouDongQuZheng.setEndtime(disposeTime());
        mShouDongQuZheng.setEndtime2(disposeTime2());
        mShouDongQuZheng.setBeizhu("create-null");
        mShouDongQuZheng.setImagePath("create-null");

        if (Integer.valueOf(nd) >= Integer.valueOf(getpre("GAOJINGZHI1_CHEZAI"))
                && Integer.valueOf(nd) < Integer.valueOf(getpre("GAOJINGZHI2_CHEZAI"))) {
            mShouDongQuZheng.setGrade("1");
        } else if (Integer.valueOf(nd) >= Integer.valueOf(getpre("GAOJINGZHI2_CHEZAI"))
                && Integer.valueOf(nd) < Integer.valueOf(getpre("GAOJINGZHI3_CHEZAI"))) {
            mShouDongQuZheng.setGrade("2");
        } else if (Integer.valueOf(nd) >= Integer.valueOf(getpre("GAOJINGZHI3_CHEZAI"))) {
            mShouDongQuZheng.setGrade("3");
        } else {
            mShouDongQuZheng.setGrade("0");
        }
        //??????
        mShouDongQuZheng.setFlag(getpre("setflag"));
        Random random = new Random();
        mShouDongQuZheng.setUid(disposeTime3() + String.valueOf(random.nextInt(90000000) + 1));
        Log.e("send--post111", mShouDongQuZheng.toString());
        SendPost(mShouDongQuZheng);
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