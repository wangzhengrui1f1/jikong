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

    public static final String ACCESS_ID = "LTAI5t5nHEUsjSY5EnZTjzBD";                                  //阿里云ID
    public static final String ACCESS_KEY = "L3r0Fnd2VbVTNYcXAAIJ1N6YI2AEmI";                           //阿里云KEY
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
    //地图定位
    private MapView mMapView = null;
    private MapView mMapView2 = null;
    private BaiduMap mBaiduMap = null;
    private BaiduMap mBaiduMap2 = null;
    private MyLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;
    private LocationClientOption option = null;
    private boolean isFirstLocation = true;
    private LatLng currentLatLng = null;//当前所在位置
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
    //todo 运动轨迹点 录入时间间隔
    private int runtime = 10;
    private int daka = 0;
    private BDLocation dakalocation;
    private LinearLayout ls1, ls2, ls6, ls7, ls8, ls11, ls12, ls15, sview1, sview2;
    private ImageView ima1, ima2, back, lvguangb, tname3;
    private int shensuo = 0;//3最长 2中间 1最短
    List<LatLng> list = new ArrayList<>();
    private TextView text3, text_shebei;
    Boolean lineStop = false;
    //todo 蓝牙------------------------------------------------------------------
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;
    //浓度 光强---
    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;
    //摄像头---
    private FragmentManager fragmentManager2 = null;
    private FragmentTransaction fragmentTransaction2 = null;
    int ss = 0;
    private ListView deviceLv;
    private TextView scanCountTv, text_nongdu, text_wendu, text_guangqiang;
    //设备扫描结果展示适配器
    private DeviceAdapter adapter;
    private BluetoothLeDeviceStore bluetoothLeDeviceStore = new BluetoothLeDeviceStore();
    private int SxtTime = 300;
    private ProgressBar pro_guangqiang;
    private int dialogFlag = 0;
    BDLocation sendlocation = null;
    //悬浮窗
    View displayView;
    private WindowManager windowManager2;
    private WindowManager.LayoutParams layoutParams2;
    int DisFirst = 0, xunjianFlag = 0, sFlag = 0;

    int saveNongdu = 0;
    List<String> sendList = new ArrayList<>();
    int sendListIndex = 0;
    //todo 是否报警 0不报 1报
    int isBj = 0;
    /**
     * 扫描回调
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
    //todo 报警未关闭
    int VisDialog = 0;
    //todo wifi
    private TextView kaishi, qiehuan, quzheng;
    //todo 数据获取解析更新多线程
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
            //刷新重绘view
            //todo 更新浓度
            if (msg.what == 101) {
                text_shebei.setText(String.valueOf(ss++));
                //todo 绘制曲线图
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
                // 自动报警
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
                //todo 绿光 0 关 1开
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
        //TODO 气体浓度
        //浓度最大值
        //TODO 光强 2
        //绿光状态
        //传感器温度
        //TODO 标定调峰状态 5
        //峰值
        //水平角度
        //垂直角度 8
        //激光器通讯状态
        //云台通讯状态
        //定位状态 11
//                wifidata13.clear();
        //nowShouDongQuZheng.setWendu();
    }

    //todo 绘制图
    List<Integer> mylista = new ArrayList<>();
    LineDataSet lineDataSet;
    LineChart lineChart;
    int shituqiehuanFlag = 0;
    private ImageView sxt;

    ShouDongQuZheng nowShouDongQuZheng = new ShouDongQuZheng();
    //todo 截图
    Activity activity = getActivity();
    private static final int REQ_CODE_PER = 0x2304;
    private static final int REQ_CODE_ACT = 0x2305;

    //todo 录屏
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
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            Toast.makeText(getActivity(), "已开启定位权限", Toast.LENGTH_SHORT).show();
            //存储权限

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
        //初始化蓝牙
        ViseLog.getLogConfig().configAllowLog(true);//配置日志信息
        ViseLog.plant(new LogcatTree());//添加Logcat打印信息
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
        lixiancityAdapter adapter2 = new lixiancityAdapter(getActivity(), records2);
        final ListView lixianListview = (ListView) view.findViewById(R.id.lixianListview);
        lixianListview.setAdapter(adapter2);
        lixianListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOffline.start(records2.get(position).cityID);
                Toast.makeText(mContext, "下载中...请不要退出app", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo();
        if (localMapList != null) {
            Toast.makeText(mContext, "已下载城市地图数量：" + localMapList.size(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "已下载城市地图数量：0", Toast.LENGTH_SHORT).show();
        }
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
//                Toast.makeText(mContext, "打卡点被点击！", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        //mmend
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(lineStop==true){
//                    lineStop = false;
//                    Toast.makeText(mContext, "打开", Toast.LENGTH_SHORT).show();
//                }else {
//                    lineStop = true;
//                    Toast.makeText(mContext, "关闭", Toast.LENGTH_SHORT).show();
//                }
                Toast.makeText(mContext, "" + calculateDistance(), Toast.LENGTH_SHORT).show();
                if (calculateDistance() > 0) {
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
                }
            }
        });
        // pppa.add(new LatLng(39.3333333,122.3213533));
        // pppa.add(new LatLng(39.3333332,122.3213532));
        //更新数据多线程类
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

        //todo 初始化局域网
        initWIfi();

        InitWifiController();
        connect();

        //悬浮窗
        InitView(730, 850, 1657, 668);

        // 隐藏logo
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

        //todo 开始数据线程
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
     * 根据指定的view截图
     *
     * @param v 要截图的view
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
                            //休眠500ms
                            sleep(150);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //判断是否是暂停
                        //TODO 暂停
                        if (xunjianFlag == 1) {
                            //发送wifi数据
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
                        //通知主线程刷新
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
        text5.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
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
                Toast.makeText(getActivity(), "摄像头开启中,请不要重复点击...", Toast.LENGTH_LONG).show();
                FragmentManager fragmentManager = getFragmentManager();
                final shexiangtouFragment id = (shexiangtouFragment) fragmentManager.findFragmentByTag("shipin");
                id.Zhuce();
                id.Bofang();

            }
        });

        username.setText("| " + getpre("username"));
        //TODO 光强最大值14
        pro_guangqiang.setMax(14);
        kaishi.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (kaishi.getText().toString().equals("开始巡检")) {
                    pre("starttime", disposeTime());
                    //todo 开始巡检
                    xunjianFlag = 1;
                    kaishi.setText("停止巡检");
                    kaishi.setBackgroundResource(R.drawable.bbhuang);
                    kaishi.setTextColor(getResources().getColor(R.color.black));
                    Log.e("xunjian-开始时间", getpre("starttime"));
                    sendPostStart();
                    TaskCenter.sharedCenter().connect(getpre("e6"), Integer.parseInt(getpre("e8")));
                } else {
                    //todo 停止巡检
                    xunjianFlag = 0;
                    kaishi.setText("开始巡检");
                    kaishi.setTextColor(getResources().getColor(R.color.white));
                    kaishi.setBackgroundResource(R.drawable.sbutton);
                    sendPostEnd();
                }
                //todo  有视频
//                if (SC_IS_RUN) {
//                    //todo 停止巡检
//                    xunjianFlag = 0;
//                    kaishi.setText("开始巡检");
//                    kaishi.setTextColor(getResources().getColor(R.color.white));
//                    kaishi.setBackgroundResource(R.drawable.sbutton);
//                    ReadRunState();
//                    if (SC_IS_RUN) {
//                        mBinder.StopScreenCapture();
//                        SC_IS_RUN = false;
//                        SaveRunState(SERVICE_IS_START, SERVICE_HAS_BIND, SC_IS_RUN);
//                        //保存数据  SPFilename 数据地址
//                        if (timeList.size() > 0) {
////                            ChezaiShiping chezaiShiping = new ChezaiShiping();
////                            chezaiShiping.setFlag("no");
////                            chezaiShiping.setPath(SPFilename);
////                            chezaiShiping.setTime(timeList.toString());
//                        }
//                        Toast.makeText(getActivity(), "屏幕录制服务停止运行", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getActivity(), "屏幕录制服务并没有开启，操作无效", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    timeList.clear();
//                    saveTime = 0;
//                    pre("starttime",disposeTime());
//                    //todo 开始巡检
//                    xunjianFlag = 1;
//                    kaishi.setText("停止巡检");
//                    kaishi.setBackgroundResource(R.drawable.bbhuang);
//                    kaishi.setTextColor(getResources().getColor(R.color.black));
//                    Log.e("xunjian-开始时间",getpre("starttime"));
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
                //todo 视频全图
                if (shituqiehuanFlag % 2 == 0) {
                    sview1.setVisibility(View.VISIBLE);
                    sview2.setVisibility(View.GONE);
                    sxt.setVisibility(View.VISIBLE);
                    //todo 地图全图
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
                        toastShow("未检测到数据.");
                    }

                } else {
                    toastShow("未开始巡检");
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
                    toastShow("速度已达到上限");
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
                    toastShow("速度已达到下限");
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
            //销毁
            fragmentManager = getFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag("one");
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
            //创建
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.zt1, new blueToothDataFragment(), "one");
            fragmentTransaction.commit();
        }

        //销毁


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
                //整体2
                ls12.setVisibility(View.VISIBLE);
                //中
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
                // 指定区域截图
//                Rect rect = new Rect(0, 0, 8000, 8000);// 左xy 右xy
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
//                            Toast.makeText(getActivity(), "屏幕截图成功，图片存在: " + file.toString(),
//                                    Toast.LENGTH_SHORT).show();
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                });
//                Toast.makeText(getActivity(), "正在截取屏幕图片...", Toast.LENGTH_SHORT).show();

//                // 返回当前位置
//                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(pppa.get(pppa.size() - 1));
//                mBaiduMap.animateMapStatus(mapStatusUpdate);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 0);
                shensuo = 0;
                params.weight = 0.1f;
                ls2.setLayoutParams(params);
                //地图最大
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
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);

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
        // 开启定位图层
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
                    guiji.setText("开启轨迹");
                } else {
                    lineStop = true;
                    pppa.clear();
                    // mBaiduMap.clear();
                    guiji.setText("关闭轨迹");
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
     * 初始化定位相关
     */
    private void initLocation() {
        // 声明LocationClient类
        mLocationClient = new LocationClient(getActivity());

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
        public void onReceiveLocation(BDLocation location) {
            dakalocation = location;
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
                    //模拟定位
//                    location.setLatitude(gpslatitude+0.008774687519+0.00074331248099);
//                    location.setLongitude(gpslongitude+0.00374531687912+0.00960631645);
//                    Log.e("aaasss", (gpslatitude+0.008774687519+0.00074331248099)+"GPS模拟定位---"+(gpslongitude+0.00374531687912+0.00960631645));
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
//        //动态注册广播
        myNetworkReceiver = new MyNetworkReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(myNetworkReceiver, intentFilter);
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
            mBaiduMap2.animateMapStatus(MapStatusUpdateFactory
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
        mBaiduMap2.setMyLocationData(locationData);
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

        mBaiduMap2.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                // TODO Auto-generated method stub
                //地图加载完成
            }
        });
        mBaiduMap2.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

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
                LatLng latlng = mBaiduMap2.getMapStatus().target;
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
                center = mBaiduMap2.getMapStatus().target;
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
     * @param point Marker坐标点 小车
     */
    private void addMarker(LatLng point) {
        if (marker != null) {
//            marker.remove();
            marker.setPosition(point);
        } else {
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(xiaoche);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            marker = (Marker) mBaiduMap.addOverlay(option);
            marker = (Marker) mBaiduMap2.addOverlay(option);
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
                    Toast.makeText(mContext, "未找到结果", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(mContext, "未找到结果", Toast.LENGTH_SHORT).show();
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            //重写返回键
//            if (keyword.trim().length() > 0) {//如果输入框还有字，就返回到地图界面并清空输入框
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
////                        if(!myNetworkReceiver.getNetwork().equals("无网络连接")){
////
////                        }
//                        lv_searchAddress.setVisibility(View.VISIBLE);
//                    }else {
//                        Toast.makeText(mContext, "横屏不建议拓展.", Toast.LENGTH_SHORT).show();
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
//                        //销毁
                    fragmentManager2 = getFragmentManager();
                    Fragment fragment = fragmentManager2.findFragmentByTag("shipin");
                    fragmentTransaction2 = fragmentManager2.beginTransaction();
                    fragmentTransaction2.remove(fragment);
                    fragmentTransaction2.commit();
                    //创建
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
////        // 当不需要定位图层时关闭定位图层
////        mBaiduMap.setMyLocationEnabled(false);
////        // 取消监听函数
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
                // 处理下载进度更新提示
                if (update != null) {
//                    stateView.setText();
                    //updateView();
                    Toast.makeText(mContext, "" + String.format("%s : %d%%", update.cityName,
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
                gpslatitude = location1.getLatitude(); // 经度
                gpslatitude = location1.getLongitude(); // 纬度
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
        Log.e("pianyi gps偏移前", String.valueOf(gpslongitude) + "---" + String.valueOf(gpslatitude));
        Log.e("pianyi gps偏移后", String.valueOf(b[0]) + "---" + String.valueOf(b[1]));
        Toast.makeText(mContext, "GPS定位:" + '\n' + "纬度：" + gpslatitude + "\n" + "经度：" + gpslongitude, Toast.LENGTH_SHORT).show();

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
     * 缩放地图，使所有Overlay都在合适的视野内
     * <p>
     * 注： 该方法只对Marker类型的overlay有效
     * </p>
     */

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
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

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + "内部存储/pictures")));

    }

//    // 移动 缩放的监听事件
//    BaiduMap.OnMapStatusChangeListener listent = new BaiduMap.OnMapStatusChangeListener() {
//
//        @Override
//        public void onMapStatusChangeStart(MapStatus arg0) {
//            Log.e("开始", "onMapStatusChangeStart");
//        }
//
//        @Override
//        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
//
//        }
//
//        @Override
//        public void onMapStatusChangeFinish(MapStatus map) {
//            Log.e("改变结束", "map" + map.zoom);
//
//        }
//
//        @Override
//        public void onMapStatusChange(MapStatus status) {
//            Log.e("改变中", "onMapStatusChange");
//            if (status.zoom < 14) {
//                try {
//                    float zoomLevel = Float.parseFloat("14");
//                    Log.e("s", "1" + zoomLevel);
//                    MapStatusUpdate u = MapStatusUpdateFactory
//                            .zoomTo(zoomLevel);
//                    mBaiduMap.animateMapStatus(u);
//                } catch (NumberFormatException e) {
//                    // Toast.makeText(this, "请输入正确的缩放级别",
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
     * 计算两个Marker之间的距离
     */
    private double calculateDistance() {
        //   pppa.add(new LatLng(39.33333,122.32135));
        //   pppa.add(new LatLng(39.33335,122.32136));
        double distance = GeoHasher.GetDistance(39.3333333, 122.3213332, 39.3333533, 122.3213532);
        return distance;
    }

    //todo 蓝牙-----------------------------------------------------------------------------------------------------------

    private void initBluetooth() {
        deviceLv = (ListView) view.findViewById(android.R.id.list);
        scanCountTv = (TextView) view.findViewById(R.id.scan_device_count);

        adapter = new DeviceAdapter(getActivity());
        deviceLv.setAdapter(adapter);

        deviceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //点击某个扫描到的设备进入设备详细信息界面
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
     * 菜单栏的显示
     *
     * @param menu 菜单
     * @return 返回是否拦截操作
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
     * 点击菜单栏的处理
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan://开始扫描
                startScan();
                break;
            case R.id.menu_stop://停止扫描
                stopScan();
                break;
        }
        return true;
    }

    /**
     * 开始扫描
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
     * 停止扫描
     */
    private void stopScan() {
        ViseBle.getInstance().stopScan(periodScanCallback);
        // invalidateOptionsMenu();
    }

    /**
     * 更新扫描到的设备个数
     *
     * @param count
     */
    private void updateItemCount(final int count) {
        scanCountTv.setText(getString(R.string.formatter_item_count, String.valueOf(count)));
    }


    //todo ----------------------------------------------------------------------------------------------------------
    //再次可见
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //TODO 可见操作
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
//            //TODO 不可见操作
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

    //todo wifi局域网发送数据
    public static void sendMessage(String msg) {
        Log.i("TaskCenter", "发送成功");
        Log.e("biaofeng", msg);
        //todo 将十六进制字符数组转换为字节数组
        TaskCenter.sharedCenter().send(HexUtil.decodeHex(msg.toCharArray()));
    }

    //todo 初始化wifi局域网-回调s函数
    public void initWIfi() {
        TaskCenter.sharedCenter().setDisconnectedCallback(new TaskCenter.OnServerDisconnectedCallbackBlock() {
            @Override
            public void callback(IOException e) {
                //textView_receive.setText(textView_receive.getText().toString() + "断开连接" + "\n");

            }
        });

        TaskCenter.sharedCenter().setConnectedCallback(new TaskCenter.OnServerConnectedCallbackBlock() {
            @Override
            public void callback() {
                // textView_receive.setText(textView_receive.getText().toString() + "连接成功" + "\n");
                Log.i("TaskCenter", "连接成功");
            }
        });

        TaskCenter.sharedCenter().setReceivedCallback(new TaskCenter.OnReceiveCallbackBlock() {
            @Override
            public void callback(String receicedMessage) {
                Log.e("sscallback", receicedMessage);
                //TODO 气体浓度
                //浓度最大值
                //TODO 光强 2
                //绿光状态
                //传感器温度
                //TODO 标定调峰状态 5
                //峰值
                //水平角度
                //垂直角度 8
                //激光器通讯状态
                //云台通讯状态
                //定位状态 11
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
                    //通知主线程刷新
                    dataCheckHandler.sendEmptyMessage(50);
                    //todo 一级
                    Log.e("duandian", "3");
                }
                Log.e("duandian", "4");
                Log.e("duandian", receicedMessage);
                //todo 一键标定
                char bfchar[] = receicedMessage.toCharArray();
                Log.e("duandian", "5");
                if (bfchar.length >= 2) {
                    Log.e("duandian", "6");
                    if (bfchar.length < 3 && bfchar[0] == '0' && bfchar[1] == '0') {
                        bfm = "成功";
                    } else if (bfchar.length < 3 && bfchar[0] == '0' && bfchar[1] == '2') {
                        bfm = "失败";
                    }
                }
                Log.e("duandian", "7");
                if (receicedMessage.equals("00000000000601060D66000C")) {
                    Log.i("TaskCenter2", "标峰中...");
                }


                // textView_receive.setText(textView_receive.getText().toString() +"返回数据"+ receicedMessage + "\n");
            }
        });
    }


    //todo 连接
    public void connect() {
        // TaskCenter.sharedCenter().connect("192.168.1.195",503);
        TaskCenter.sharedCenter().connect(getpre("e6"), 503);
        Log.e("sconnect", getpre("e6"));

    }

    //todo 断开连接
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
                        //      Toast.makeText(mContext, "发送", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        sendMessage(StopController);
                        //       Toast.makeText(mContext, "停", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;//true说明事件已经完成了，不会再被其他事件监听器调用
            }
        });

        down.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sendMessage(DownController);
                        //    Toast.makeText(mContext, "发送", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        sendMessage(StopController);
                        // Toast.makeText(mContext, "停", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;//true说明事件已经完成了，不会再被其他事件监听器调用
            }
        });

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sendMessage(LeftController);
                        //     Toast.makeText(mContext, "发送", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        sendMessage(StopController);
                        //    Toast.makeText(mContext, "停", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;//true说明事件已经完成了，不会再被其他事件监听器调用
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        sendMessage(RightController);
                        //     Toast.makeText(mContext, "发送", Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        sendMessage(StopController);
                        //    Toast.makeText(mContext, "停", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;//true说明事件已经完成了，不会再被其他事件监听器调用
            }
        });
    }


    //todo

    /**
     * 初始化曲线图表
     *
     * @param list 数据集
     */
    public void setData2(final List<Integer> list) {

        //显示边界
        lineChart.setDrawBorders(false);
        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            entries.add(new Entry(i, (float) list.get(i)));
        }
        //一个LineDataSet就是一条线
        lineDataSet = new LineDataSet(entries, "");
        //线颜色
        lineDataSet.setColor(Color.parseColor("#F15A4A"));
        //线宽度
        lineDataSet.setLineWidth(1.6f);
        //不显示圆点
        lineDataSet.setDrawCircles(true);
        //线条平滑
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillAlpha(50);
        lineDataSet.setFillColor(Color.RED);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(5f);// 显示的圆形大小
        lineDataSet.setDrawCircles(false);// 是否有圆点
        lineDataSet.setDrawValues(true);//设置是否显示点的坐标值
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);//曲线模式
        lineDataSet.setHighlightEnabled(true); // allow highlighting for DataSet
        lineDataSet.setHighLightColor(R.color.huangse2);
        lineDataSet.setHighlightLineWidth(2);

        ChartHighlighter highlight = new ChartHighlighter(lineChart);

        lineChart.setHighlighter(highlight);

        // set this to false to disable the drawing of highlight indicator (lines)
        lineDataSet.setDrawHighlightIndicators(true);
        LineData data = new LineData(lineDataSet);
        //无数据时显示的文字
        lineChart.setNoDataText("等待浓度数据");
        //折线图不显示数值
        data.setDrawValues(false);
        //得到X轴
        XAxis xAxis = lineChart.getXAxis();
        //设置X轴的位置（默认在上方)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴坐标之间的最小间隔
        xAxis.setGranularity(1f);
        //不显示网格线
        xAxis.setDrawGridLines(false);
        Legend mLegend = lineChart.getLegend(); //设置标示，就是那个一组y的value的
        mLegend.setForm(Legend.LegendForm.LINE); //横线的样式
        mLegend.setFormSize(6f); //字体
//        mLegend.setXEntrySpace(6f); //设置在水平轴上 legend-entries 的间隙
        mLegend.setYEntrySpace(6f); //设置在垂直轴上 legend-entries 的间隙
        mLegend.setFormToTextSpace(6f); //设置 legend-form 和 legend-label 之间的空间
        mLegend.setWordWrapEnabled(true); //设置 Legend 是否自动换行？ 目前仅支持BelowChartLeft，BelowChartRight
        mLegend.setTextColor(Color.RED); //设置字体颜色
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT); //设置显示下位置在左侧
        //mLegend.setTypeface(Typeface.DEFAULT); //设置图例标签的字体
        mLegend.setEnabled(true);
        //得到Y轴
        YAxis yAxis = lineChart.getAxisLeft();
        YAxis rightYAxis = lineChart.getAxisRight();
        YAxis left = lineChart.getAxisLeft();
        left.setEnabled(true); //右侧Y轴不显示
        //设置Y轴是否显示
        rightYAxis.setEnabled(false); //右侧Y轴不显示
        //设置y轴坐标之间的最小间隔
        //不显示网格线
        yAxis.setDrawGridLines(false);
        //设置Y轴坐标之间的最小间隔
        yAxis.setGranularity(1);
//        lineChart.setHighlightPerTapEnabled(false);
//        lineChart.setHighlightPerDragEnabled(false);

        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        //设置数据
        lineChart.setData(data);
        //图标刷新
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
            biaoti.setText("手动取证");
        } else {
            biaoti.setText("报警取证");
        }
        VisDialog = 1;
        dialogFlag = 1;
        String usetime = disposeTime();
        info1.setText(" " + getpre("username"));
        info2.setText(" " + "车载");

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

        info9.setText("  起始时间   " + getpre("starttime") + " - " + usetime);

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
                //    String type;//设备类型-车载
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
                //    String endtime; //结束时间 = 事件发生时间
                //    String endtime2;//比较时间
                //    String beizhu;
                //    String ImagePath;
                //    String grade;//1 2 3级别
                //    String flag;//1手动 2自动
                //    int uid;

                int nd = 0;
                if (nowifidata13.size() == 12) {
                    nd = Integer.parseInt(nowifidata13.get(0));
                }
                ShouDongQuZheng mShouDongQuZheng = new ShouDongQuZheng();
                mShouDongQuZheng.setShebeiname(getpre("SHEBEIWEIHAO"));
                mShouDongQuZheng.setUsername(getpre("username"));
                mShouDongQuZheng.setType("车载");
                mShouDongQuZheng.setDidian("暂不显示");
                //todo 经纬度
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
                //自动
                mShouDongQuZheng.setFlag(getpre("setflag"));
                Random random = new Random();
                mShouDongQuZheng.setUid(disposeTime3() + String.valueOf(random.nextInt(90000000) + 1));
                addCheZaiBaoJingData(myDatebaseHelper, mShouDongQuZheng);
                toastShow("保存成功...");
                //上传
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
                "手动取证" + disposeTime() + ".jpg");
        // Bitmap bitmap = screenShot(getActivity());
        Bitmap bitmap = getViewBitmap(activity.getWindow().getDecorView());
        //Bitmap bitmap = screenShot(activity);

        try {
            if (!file.exists())
                file.createNewFile();

            boolean ret = saveSrc(bitmap, file, Bitmap.CompressFormat.JPEG, true);
            if (ret) {
                path = file.getAbsolutePath();
                //Toast.makeText(getActivity().getApplicationContext(), "截图已保持至 " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //final ImageView imageView = view.findViewById(R.id.imageView4);
        //        Glide.with(this).load(as).into(imageView);
        return path;
    }

    /**
     * 保存图片到文件File。
     *
     * @param src     源图片
     * @param file    要保存到的文件
     * @param format  格式
     * @param recycle 是否回收
     * @return true 成功 false 失败
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
     * Bitmap对象是否为空。
     */
    public static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    public void InitView(int width, int height, int x, int y) {
        //todo 初始化
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
        //录制视频
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
                        //如果API-23编译，使用Android运行时权限
                        AskForPermission();
                    }
                    myShareScreen();
                    Toast.makeText(getActivity(), "屏幕录制服务开始运行", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "屏幕录制服务已经开启，操作无效", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, "下载中...请不要退出app", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "已下载城市地图数量：" + localMapList.size(), Toast.LENGTH_SHORT).show();
            lixianListAdapter adapter = new lixianListAdapter(getActivity(), localMapList) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    final View view = super.getView(position, convertView, parent);
                    final TextView ss4 = (TextView) view.findViewById(R.id.ss4);
                    ss4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOffline.remove(localMapList.get(position).cityID);
                            Toast.makeText(getActivity(), localMapList.get(position).cityName + "删除成功", Toast.LENGTH_SHORT).show();
//                            init();
                        }
                    });

                    return view;
                }
            };
            lixianListview.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "已下载城市地图数量：0", Toast.LENGTH_SHORT).show();
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
                    Log.i(context.getPackageName(), "后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    //todo --------------视频录制-------------------------------------------------------------------------------------


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
        //实例化SharedPreferences对象（第一步）
        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(TAG, MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putBoolean("SC_IS_RUN", SC_IS_RUN);
        editor.putBoolean("SERVICE_HAS_BIND", SERVICE_HAS_BIND);
        editor.putBoolean("SERVICE_IS_START", SERVICE_IS_START);
        //提交当前数据
        editor.commit();
        Log.v("SaveRunState", "SERVICE_IS_START = " + SERVICE_IS_START
                + ";SERVICE_HAS_BIND = " + SERVICE_HAS_BIND + ";SC_IS_RUN = " + SC_IS_RUN);
    }

    private void ReadRunState() {
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(TAG, MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
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


    //todo 阿里云上传
    public void aliyunShangchuan(String path, final ShouDongQuZheng mShouDongQuZheng) {
        ALiUploadManager.getInstance().uploadFile(path, new ALiUploadManager.ALiCallBack() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result, String url) {
                Log.e("上传阿里云成功:", url);
                Log.e("上传阿里云成功:", result.toString());
                String url2 = "https://" + ACCESS_BUCKET_NAME + "." + ACCESS_ENDPOINT + "/" + "aaa";
                Log.e("上传阿里云成功:", url2);
                // putImage(url);  //路径回调至服务端
                Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                Log.e("上传阿里云失败clientExcepion:",
                        clientExcepion.getMessage() + ",serviceException:" + serviceException);
//                ToastUtil.showShortToast(ImageSubmitActivity.this, "上传失败");
                Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void process(long currentSize, long totalSize) {
                Log.e("上传中:", (currentSize * 100) / totalSize + "%");

            }
        });
    }

    //todo 上传记录 http://123.56.230.79:6799/user/JikongSave/SaveAlert?
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
        Log.e("aass22", "上传");
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SaveAlert, hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                if (result.contains("成功")) {
                    toastShow("上传成功");
                    lessUid(myDatebaseHelper, uid, id);
                    Log.e("aass22", "上传成功");
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
        //        ////TODO 气体浓度
        //        //                //浓度最大值
        //        //                //TODO 光强 2
        //        //                //绿光状态
        //        //                //传感器温度
        //        //                //TODO 标定调峰状态 5
        //        //                //峰值
        //        //                //水平角度
        //        //                //垂直角度 8
        //        //                //激光器通讯状态
        //        //                //云台通讯状态
        //        //                //定位状态 11
        dataCheckHandler.sendEmptyMessage(1002);
        hs.put("latitude", String.valueOf(sendlocation.getLatitude()));
        hs.put("longitude", String.valueOf(sendlocation.getLongitude()));
        hs.put("placeName", "待定");
        //todo 未开始巡检
        if (!isStart) {
            hs.put("deviceStatus", "离线");
        } else {
            hs.put("deviceStatus", "在线");
        }
        hs.put("userName", getpre("username"));
        hs.put("deviceName", getpre("androidIds"));
        hs.put("alerts", "待定");
        hs.put("fault", "待定");
        hs.put("CH4", ssmShouDongQuZheng.getNongdu());
        hs.put("nospeed", ssmShouDongQuZheng.getChesu());
        hs.put("video", "待定");
        hs.put("uid", getpre("eventId"));
        hs.put("temperature", ssmShouDongQuZheng.getWendu());
        hs.put("light", ssmShouDongQuZheng.getGuangqiang());
        hs.put("IntervalData", "待定");
        hs.put("placeTime", disposeTime());
        Log.e("send--post2", "1");
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SavePlaceUrl, hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                if (result.contains("成功")) {
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
        mShouDongQuZheng.setType("车载");
        mShouDongQuZheng.setDidian("暂不显示");
        //todo 经纬度
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
        //自动
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
        hs.put("endtime", "巡检中");
        hs.put("statusId", "巡检中");
        hs.put("deviceName", getpre("androidIds"));
        String eventId = disposeTime3() + createMima() + String.valueOf(random.nextInt(900000) + 1250);
        hs.put("eventId", eventId);
        pre("eventId", eventId);
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SaveEventurl, hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                if (result.contains("成功")) {

                } else {
                    toastShow("上传信息失败！");
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
        hs.put("statusId", "巡检结束");
        hs.put("deviceName", getpre("androidIds"));
        hs.put("eventId", getpre("eventId"));
        pre("eventId", getpre("eventId") + "end");
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SaveEventurl, hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                if (result.contains("成功")) {

                } else {
                    toastShow("上传信息失败！");
                }
            }
        });
    }
}