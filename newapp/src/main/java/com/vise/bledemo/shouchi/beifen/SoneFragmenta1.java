package com.vise.bledemo.shouchi.beifen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.vise.bledemo.bluetooth.adapter.DeviceAdapter;
import com.vise.bledemo.bluetooth.common.BluetoothDeviceManager;
import com.vise.bledemo.model.GetJingbao;
import com.vise.bledemo.model.Jingbao;
import com.vise.bledemo.shouchi.fragment.blueToothDataFragment;
import com.vise.bledemo.sqlite.MyDatabase;
import com.vise.bledemo.utils.GeoHasher;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;
import com.vise.xsnow.event.BusManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.vise.bledemo.R.mipmap.location;
import static com.vise.bledemo.baiduMap.baiduMap.adapter.helps.ScreenOrient;
import static com.vise.bledemo.shouchi.data.Data_all.devices;
import static com.vise.bledemo.shouchi.data.Data_all.pppa;
import static com.vise.bledemo.sqlite.sqliteMethod.addData;
import static com.vise.bledemo.sqlite.sqliteMethod.getData;
import static com.vise.bledemo.utils.getTime.disposeTime;
import static com.vise.bledemo.utils.getTime.disposeTime2;
import static com.vise.bledemo.utils.getTime.getdidian;
import static com.vise.bledemo.utils.getTime.guangbaojin;
import static com.vise.bledemo.utils.getTime.xianchen;
import static com.vise.bledemo.utils.url.SQLITE_NAME;
import static com.vise.bledemo.utils.url.intentB_q;
import static com.vise.bledemo.utils.url.isStart;
import static com.vise.utils.handler.HandlerUtil.runOnUiThread;

public class SoneFragmenta1 extends BaseFragment implements OnGetPoiSearchResultListener
        , MKOfflineMapListener {
    View view;
    private static String TAG = "MainActivity";
    private Context mContext;
    private TextView tv_city,jieshu,kaishi,text_caozuoyuan;
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
    private Button shoudongjilu;
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
    private Adapter_SearchAddress2 adapter_searchAddress;
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
    private TextView xiazai, textview_lixianditu, text5;
    boolean isFrist =true;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private int settinsflag = 0;
    private MyNetworkReceiver myNetworkReceiver;
    private int myNetwork = 0;
    //todo 运动轨迹点 录入时间间隔
    private int runtime = 10;
    private int daka = 0;
    private BDLocation dakalocation;
    private LinearLayout ls1, ls2, ls6, ls7, ls8, ls11, ls12, ls15;
    private ImageView ima1,ima2,back,ima3;
    private int shensuo = 0;//3最长 2中间 1最短
    List<LatLng> list= new ArrayList<>();
    private TextView text3,text_shebei;
    Boolean lineStop = false;
    private ListView getdizhiListview;

    //sqilte sqlite
    private MyDatabase myDatebaseHelper;
    //todo 蓝牙------------------------------------------------------------------
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;
    //浓度 光强---
    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;
    private ListView deviceLv;
    private TextView scanCountTv;
    //设备扫描结果展示适配器
    private DeviceAdapter adapter;
    private BluetoothLeDeviceStore bluetoothLeDeviceStore = new BluetoothLeDeviceStore();
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
    private ViewPager main_viewPager;
    //todo 数据更新获取解析多线程
    public Thread dataCheckThread;
    //todo 数据更新获取解析多线程
    public Handler dataCheckHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //刷新重绘view
            if(msg.what==50){
                Log.e("aaxianchen","tips1");
                tips();
                clean();
                Log.e("aaxianchen","tips2");
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        view = inflater.inflate(R.layout.activity_mainbaidushouchibeifen, container, false);
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            Toast.makeText(getActivity(), "已开启定位权限", Toast.LENGTH_LONG).show();
            //存储权限

        }
        //sqlite数据库
        myDatebaseHelper = new MyDatabase(getActivity(), SQLITE_NAME, null, 1);
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
        monitorMap();

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

        //TODO 打卡功能
        View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.dakaitem, null);
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
                Toast.makeText(mContext, ""+calculateDistance(), Toast.LENGTH_SHORT).show();
                if(calculateDistance()>0){
                    mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(16).build()));
                }
            }
        });
       // pppa.add(new LatLng(39.3333333,122.3213533));
       // pppa.add(new LatLng(39.3333332,122.3213532));
        //更新数据多线程类
        if (xianchen==-1){
            updateDataByBuleTooth();
            xianchen=100;
            Log.e("aaxianchen","线程被创建");
        }
        //updateDataByBuleTooth();

        Handler hs = new Handler();
        hs.postDelayed(new Runnable() {
            @Override
            public void run() {
                pppa.clear();
            }
        },15000);
        checknetwork();
        return view;
    }

    private void updateDataByBuleTooth() {
        if(dataCheckThread==null){
                dataCheckThread=new Thread("jiancheThread"){
                    @Override
                    public void run() {
                        super.run();
                        while (true){
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //判断是否处于结束
                            //isOver=checkOver();
                            if(isStart==false){
                                continue;
                            }
                            Log.e("axianchenSone",""+guangbaojin);
                            Log.e("aaxianchen","执行中");
                            //判断是否是暂停
                            //TODO
                            if(guangbaojin==1){
                                guangbaojin = 2;
                                Log.e("axianchenSone","准备执行clean");
                                Log.e("aaxianchen","异常");
                                dataCheckHandler.sendEmptyMessage(50);
                               // isStart=false;

                            }
                            //通知主线程刷新
                        }
                    }
                };
                dataCheckThread.start();
            }
    }

    private void clean(){
        Log.e("axianchenSone","执行clean");
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                guangbaojin = 0;
                Log.e("axianchenSone","可以自动报警");
            }
        },60000);
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
        ima3 = (ImageView) view.findViewById(R.id.ima3);
        back = (ImageView) view.findViewById(R.id.back);
        text5 = (TextView) view.findViewById(R.id.text5);
        text5.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        ls6.setVisibility(View.GONE);
        ls7.setVisibility(View.GONE);
        Log.e("ls6ls7", "gone");
        text3 = (TextView)view.findViewById(R.id.text3);
        text_shebei = (TextView) view.findViewById(R.id.text_shebei);
        shoudongjilu = (Button) view.findViewById(R.id.shoudongjilu);
        jieshu = (TextView) view.findViewById(R.id.jieshu);
        kaishi = f(view,R.id.kaishi);
        text_caozuoyuan = f(view,R.id.text_caozuoyuan);
        text_caozuoyuan.setText(getpre("username"));
    }

    public void InitViewPager() {
//        getActivity().getSupportFragmentManager()    //
//                .beginTransaction()
//                .add(R.id.ssa2,new blueToothDataFragment())   // 此处的R.id.fragment_container是要盛放fragment的父容器
//                .commit();
        if(fragmentManager == null){
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.ssa2, new blueToothDataFragment(),"one");
            fragmentTransaction.commit();
            Handler handler =new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    blueToothDataFragment id = (blueToothDataFragment) fragmentManager.findFragmentByTag("one");
                   // id.toa();
                }
            },15000);
        }else {
            //销毁
            fragmentManager=getFragmentManager();
            Fragment fragment=fragmentManager.findFragmentByTag("one");
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
            //创建
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.ssa2, new blueToothDataFragment(),"one");
            fragmentTransaction.commit();
        }

        //销毁


    }


    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;
        public MyAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }
        @Override
        public int getCount() {
            return list.size();
        }
    }

    private void initOnclick() {
        jieshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = false;
                toastShow("结束检测");
            }
        });
        kaishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStart = true;
                toastShow("开始检测");
                //intent(Test1Activity.class);
            }
        });
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
                mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        ima1.setBackground(null);
                        ima1.setImageBitmap(bitmap);
                        saveImageToGallery(getActivity(),bitmap);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ima1.setBackground(getActivity().getDrawable(R.drawable.xx2));
                                ima1.setImageBitmap(null);
                            }
                        },1500);
                    }
                });
                // 指定区域截图
                Rect rect = new Rect(0, 0, 8000, 8000);// 左xy 右xy
                mBaiduMap.snapshotScope(rect, new BaiduMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap snapshot) {
                        File file = new File("/mnt/sdcard/testal"+disposeTime()+".png");
                        FileOutputStream out;
                        try {
                            out = new FileOutputStream(file);
                            if (snapshot.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                                out.flush();
                                out.close();
                            }
                            Toast.makeText(getActivity(), "屏幕截图成功，图片存在: " + file.toString(),
                                    Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
                Toast.makeText(getActivity(), "正在截取屏幕图片...", Toast.LENGTH_SHORT).show();

                // 返回当前位置
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(pppa.get(pppa.size() - 1));
                mBaiduMap.animateMapStatus(mapStatusUpdate);

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
                        saveImageToGallery(getActivity(),bitmap);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ima2.setBackground(getActivity().getDrawable(R.drawable.qiandao));
                                ima2.setImageBitmap(null);
                            }
                        },1500);
                    }
                });
                return false;
            }
        });

        text_shebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // tips2();
                startScan();
                if(deviceLv.getVisibility()==View.GONE){
                    deviceLv.setVisibility(View.VISIBLE);
                }else {
                    deviceLv.setVisibility(View.GONE);
                }
            }
        });

        shoudongjilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tips();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("aaxianchen","结束线程");
                Intent intent = new Intent(getActivity(),WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });


    }

    private void checknetwork() {
//        //动态注册广播
        myNetworkReceiver = new MyNetworkReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(myNetworkReceiver, intentFilter);
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
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                //离线地图
                settinsflag++;
                if (settinsflag % 2 == 0) {
                    h1.setVisibility(View.GONE);
                    lv_searchAddress.setVisibility(View.GONE);
                } else {
                    if (ScreenOrient(getActivity()) == 1) {
                        h1.setVisibility(View.VISIBLE);
                        lv_searchAddress.setVisibility(View.VISIBLE);
                    } else {
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
      //  option.setScanSpan(10000);

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
                //路径
                if(lineStop==false){
                   // onReceiveLocation2(location);
                }

//                if (location.getLatitude() > 37.8330 && location.getLatitude() > 49.9377) {
//                    Toast.makeText(mContext, "打卡成功！", Toast.LENGTH_SHORT).show();
//                }
                //  Toast.makeText(mContext, ""+location.getLatitude(), Toast.LENGTH_SHORT).show();
                if(myNetworkReceiver.getNetwork().equals("无网络连接")){
                    //模拟定位

                }
                location.setLatitude(gpslatitude+0.008774687519+0.00074331248099);
                location.setLongitude(gpslongitude+0.00374531687912+0.00960631645);
                Log.e("aaasss", (gpslatitude+0.008774687519+0.00074331248099)+"GPS模拟定位---"+(gpslongitude+0.00374531687912+0.00960631645));
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
//                    Toast.makeText(mContext, "打卡成功！", Toast.LENGTH_SHORT).show();
//                }
                //当前位置 蓝色
                navigateTo(location);
                cityName = location.getCity();
                tv_city.setText(cityName);
                Log.i("baidu_location_result", "location type = " + location.getLocType());
                Log.i("baidu_location_result1", "location type = " + location.getLatitude());
                Log.i("baidu_location_result2", "location type = " + location.getLongitude());
                // baiduMap.setMyLocationEnabled(true);
                //Toast.makeText(MainActivity.this,"点击",Toast.LENGTH_SHORT).show();
                //返回当前位置
                final BDLocation Backloction = location;
                ima3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MapStatusUpdate mapStatusUpdate  = MapStatusUpdateFactory.newLatLng(new LatLng(Backloction.getLatitude(), Backloction.getLongitude()));
                        mBaiduMap.animateMapStatus(mapStatusUpdate);
                    }
                });
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
                    .fromResource(location);
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

    //todo -----------------------------------------反向地理解析，获取周边poi列表--------------------------------------------------

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
               // initGeoCoderListView();
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
                        listView.smoothScrollToPosition(position);
                    }
                });
                return view;
            }
        };
        listView.setAdapter(adapter_searchAddress);
        adapter_searchAddress.setSelectedPosition(0);
        adapter_searchAddress.notifyDataSetInvalidated();
        Log.e("wwwzr",poiInfoListForGeoCoder.get(0).toString());
        Log.e("wwwzr2",poiInfoListForGeoCoder.toString());

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        showMapView();
        Log.e("生命周期","onResume");
       // toastShow("生命周期-onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        //Log.e("生命周期","onPause");
        //toastShow("生命周期-onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("生命周期","onDestroy");
        toastShow("生命周期-onDestroy");
//        mMapView.onDestroy();
//        // 当不需要定位图层时关闭定位图层
//        mBaiduMap.setMyLocationEnabled(false);
//        // 取消监听函数
//        if (mLocationClient != null) {
//            mLocationClient.unRegisterLocationListener(myListener);
//        }
//        mPoiSearch.destroy();
//        mSuggestionSearch.destroy();
//        mGeoCoder.destroy();
        Log.e("aaxianchen",String.valueOf(xianchen));
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
        LatLng latLng = new LatLng(bdLocation.getLatitude(),
                bdLocation.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
        //回去
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
     * 缩放地图，使所有Overlay都在合适的视野内
     * <p>
     * 注： 该方法只对Marker类型的overlay有效
     * </p>
     */

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "jikong"+disposeTime());
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
        double distance = GeoHasher.GetDistance(39.3333333,122.3213332,39.3333533,122.3213532);
        return distance;
    }

    //todo 蓝牙-----------------------------------------------------------------------------------------------------------

    private void initBluetooth() {
        deviceLv = (ListView) view.findViewById(R.id.blueList);
        scanCountTv = (TextView) view.findViewById(R.id.scan_device_count);

        adapter = new DeviceAdapter(getActivity());
        deviceLv.setAdapter(adapter);

        deviceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //点击某个扫描到的设备进入设备详细信息界面
                BluetoothLeDevice device = (BluetoothLeDevice) adapter.getItem(position);
                if (device == null){
                    return;
                }else {
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


    //tips

    //todo
    private void tips() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.tips2, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        final Button tijiao = (Button) view.findViewById(R.id.button3);
        final TextView xinghao = (TextView) view.findViewById(R.id.textView14);
        final TextView caozuoyuan = (TextView) view.findViewById(R.id.textView16);
        final TextView textdidian = (TextView) view.findViewById(R.id.textdidian);
        final LinearLayout shuju = (LinearLayout) view.findViewById(R.id.shuju);
        final LinearLayout didian = (LinearLayout) view.findViewById(R.id.didian);
        final EditText add_content = (EditText) view.findViewById(R.id.add_content);

        final TextView nongdu = (TextView) view.findViewById(R.id.textView17);
        final TextView guangqiang = (TextView) view.findViewById(R.id.textView18);
        final TextView wendu = (TextView) view.findViewById(R.id.textView19);
        nongdu.setText("浓度 "+intentB_q.getNd());
        guangqiang.setText("光强 "+intentB_q.getWd());
        wendu.setText("温度 "+intentB_q.getGq());
        textdidian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuju.setVisibility(View.GONE);
                didian.setVisibility(View.VISIBLE);
                tijiao.setText("选择");
            }
        });
        if(!getpre("WEIHAOSHOUCHI").equals("1")){
            xinghao.setText("设备型号:"+getpre("WEIHAOSHOUCHI"));
        }else {
            xinghao.setText("设备型号 ");
        }
        caozuoyuan.setText("操作员 "+getpre("username"));
        tijiao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if(tijiao.getText().equals("选择")){
                        shuju.setVisibility(View.VISIBLE);
                        didian.setVisibility(View.GONE);
                        textdidian.setText("地点:"+getdidian);
                        tijiao.setText("提交");
                    }else {
                    if(!textdidian.getText().toString().equals("地点:未选择")){
//提交数据
                        //String title;
                        //    String info;
                        //    String name;
                        //    String time;
                        //    int grade;//1 2 3级别
                        //    String locate;
                        //    String jingdu;
                        //    String weidu;
                        isStart=true;
                        dialog.dismiss();
                        Log.e("aaaaaasa","1");
                        //todo 设备型号 getpre("WEIHAOSHOUCHI")
                        Jingbao jingbao = new Jingbao(getpre("WEIHAOSHOUCHI"),
                                add_content.getText().toString(),
                                getpre("username"),disposeTime(), intentB_q.getDengji(),
                                textdidian.getText().toString(),"321","123",intentB_q.getNd(),
                                intentB_q.getWd(),intentB_q.getGq(),disposeTime2(),getpre("WEIHAOSHOUCHI"));
                        //getpre("WEIHAOSHOUCHI")
                        Log.e("aaaaaasa","2");
                        addData(myDatebaseHelper,jingbao);
                        toastShow("记录已添加");
                        Log.e("aaaaaasa","3");
                        List<GetJingbao> ls = new ArrayList<>();
                        ls = getData(myDatebaseHelper);
                        //todo 地点未选择
                    }else {
                        textdidian.setTextColor(getResources().getColor(R.color.colorAccent));
                        toastShow("地点未选择");
                    }

                }

            }
        });
        getdizhiListview = (ListView) view.findViewById(R.id.alistview1);
        initGeoCoderListView(getdizhiListview);
        dialog.show();

    }

}