package com.vise.bledemo.shouchi.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.vise.baseble.ViseBle;
import com.vise.baseble.common.PropertyType;
import com.vise.baseble.core.DeviceMirror;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.resolver.GattAttributeResolver;
import com.vise.baseble.utils.HexUtil;
import com.vise.bledemo.Base.BaseFragment;
import com.vise.bledemo.R;
import com.vise.bledemo.aliyun.ALiUploadManager;
import com.vise.bledemo.baiduMap.baiduMap.adapter.Adapter_SearchAddress2;
import com.vise.bledemo.bluetooth.common.BluetoothDeviceManager;
import com.vise.bledemo.bluetooth.event.CallbackDataEvent;
import com.vise.bledemo.bluetooth.event.ConnectEvent;
import com.vise.bledemo.bluetooth.event.NotifyDataEvent;
import com.vise.bledemo.chezai.bean.ShouDongQuZheng;
import com.vise.bledemo.model.send;
import com.vise.bledemo.pdf.PdfUtils;
import com.vise.bledemo.shouchi.activity.paizhao;
import com.vise.bledemo.sqlite.MyDatabase;
import com.vise.bledemo.utils.OkHttpUtils;
import com.vise.bledemo.utils.returnAgreement;
import com.vise.bledemo.utils.screen.ScreenShotActivity;
import com.vise.bledemo.utils.screen.Shooter;
import com.vise.bledemo.utils.screen.luxiang.MainActivity;
import com.vise.bledemo.utils.screen.luxiang.ScreenCaptureService;
import com.vise.bledemo.utils.screen.luxiang.ScreenCaptureService.MyBinder;
import com.vise.xsnow.event.BusManager;
import com.vise.xsnow.event.Subscribe;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static com.vise.bledemo.aliyun.ALiUploadManager.initAlyun;
import static com.vise.bledemo.bluetooth.common.ToastUtil.showToast;
import static com.vise.bledemo.shouchi.data.Data_all.devices;
import static com.vise.bledemo.shouchi.fragment.SoneFragment.Shouchisendlocation;
import static com.vise.bledemo.sqlite.sqliteMethod.addCheZaiBaoJingData;
import static com.vise.bledemo.sqlite.sqliteMethod.lessUid;
import static com.vise.bledemo.utils.Jiexi.jiexi2;
import static com.vise.bledemo.utils.Jiexi.ssmShouDongQuZheng;
import static com.vise.bledemo.utils.Jiexi.ssmShouDongQuZheng2;
import static com.vise.bledemo.utils.UseFngfa.closejianpan2;
import static com.vise.bledemo.utils.ViewBean.closeblueview2;
import static com.vise.bledemo.utils.ViewBean.currentLatLng2;
import static com.vise.bledemo.utils.ViewBean.lanyaClose2;
import static com.vise.bledemo.utils.ViewBean.mAllBaiduMap;
import static com.vise.bledemo.utils.ViewBean.poiInfoListForGeoCoder2;
import static com.vise.bledemo.utils.ViewBean.startBiaodingKailu;
import static com.vise.bledemo.utils.ViewBean.stopUpdate;
import static com.vise.bledemo.utils.ViewBean.tname;
import static com.vise.bledemo.utils.getTime.disposeTime;
import static com.vise.bledemo.utils.getTime.disposeTime2;
import static com.vise.bledemo.utils.url.Blue0x29;
import static com.vise.bledemo.utils.url.SQLITE_NAME;
import static com.vise.bledemo.utils.url.SaveAlert;
import static com.vise.bledemo.utils.url.SavePlaceUrl;
import static com.vise.bledemo.utils.url.ShoudongFlag;
import static com.vise.bledemo.utils.url.alyima1;
import static com.vise.bledemo.utils.url.alyima2;
import static com.vise.bledemo.utils.url.bluesenddata;
import static com.vise.bledemo.utils.url.getBiaogeng;
import static com.vise.bledemo.utils.url.intenhandler;
import static com.vise.bledemo.utils.url.isStart;

import static com.vise.bledemo.utils.url.readConcentrationAgreement;
import static com.vise.bledemo.utils.url.readConcentrationAgreement2;
import static com.vise.bledemo.utils.url.readConcentrationAgreement3;


public class blueToothDataFragment extends BaseFragment {
    //todo  阿里云
//    public static final String ACCESS_ID = "LTAI5tPg3MjRLkfUJHpjWc9B";                                  //阿里云ID
//    public static final String ACCESS_KEY = "4fOW348MFfa2nig0Qb6ZCE6qPbc7sh";                           //阿里云KEY
//    public static final String ACCESS_BUCKET_NAME = "lingjiedian";
//    public static final String ACCESS_ENDPOINT = "http://oss-cn-beijing.aliyuncs.com/";
//    public static final String ACCESS_DOMAINNAME = "http:xxxxx";

    public static final String ACCESS_ID = "LTAI5t5nHEUsjSY5EnZTjzBD";                                  //阿里云ID
    public static final String ACCESS_KEY = "L3r0Fnd2VbVTNYcXAAIJ1N6YI2AEmI";                           //阿里云KEY
    public static final String ACCESS_BUCKET_NAME = "lingjiedian";
    public static final String ACCESS_ENDPOINT = "http://oss-cn-beijing.aliyuncs.com/";
    public static final String ACCESS_DOMAINNAME = "http:xxxxx";
    //todo 录像 ----------------------------------------------------------------------------------------
    private MediaProjectionManager mMediaProjectionManager;
    public static MediaProjection mMediaProjection;
    ToggleButton tbtScreenCaptureService;

    private ScreenCaptureService.MyBinder mBinder;
    private boolean SERVICE_HAS_BIND = false;
    private boolean SERVICE_IS_START = false;
    private boolean SC_IS_RUN = false;

    private static final int CAPTURE_CODE = 115;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 123;
    //todo 录像 ----------------------------------------------------------------------------------------

    private static final String LIST_NAME = "NAME";
    private static final String LIST_UUID = "UUID";
    public static final String WRITE_CHARACTERISTI_UUID_KEY = "write_uuid_key";
    public static final String NOTIFY_CHARACTERISTIC_UUID_KEY = "notify_uuid_key";
    public static final String WRITE_DATA_KEY = "write_data_key";
    private Adapter_SearchAddress2 adapter_searchAddress;
    private SimpleExpandableListAdapter simpleExpandableListAdapter;
    TextView info3;
    // 调用相机拍照的请求码
    public static final int REQUEST_TAKE_PHOTO_CODE = 1;
    public static final int REQUEST_TAKE_PHOTO_CODE2 = 2;
    // 拍照后显示图片的ImageView
    private ImageView paizhaoimageView;
    // 保存图片的文件
    private File imageFile;

    //设备信息
    private BluetoothLeDevice mDevice;
    //输出数据展示
    private StringBuilder mOutputInfo = new StringBuilder();
    private List<BluetoothGattService> mGattServices = new ArrayList<>();
    //设备特征值集合
    private List<List<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<>();
    private TextView nd,gq,wd,textlianjie,text_biaoding;

    private static final String TAG = "SHOUCHI_BLUE";
    //浓度
    returnAgreement returnAgreement;
    private static final int REQ_CODE_PER = 0x2304;
    private static final int REQ_CODE_ACT = 0x2305;
    //todo 折线图
    ArrayList<PieEntry> pieEntryList = new ArrayList();//数据列表
    ArrayList<Integer> colors = new ArrayList();//颜色列表
    private LineChart lineChart;
    float datas[] = {14f,15f,16f,17f,16f,16f};
    float datas2[] = {17f,16f,15f,14f,17f,14f};
    //在MPAndroidChart一般都是通过List<Entry>对象来装数据的
    final List<Entry> entries = new ArrayList<Entry>();
    final List<Entry> entries2 = new ArrayList<Entry>();
    final List<String> yRightList = new ArrayList<String>();
    List<Integer> mylista = new ArrayList<>();
    LineDataSet lineDataSet;

    private ProgressBar pro_guangqiang;
    int DialogTime = 0;//0可以 1不可以
    int islanyaTime = 0;
    int biaodingDialog = 0;
    int startBiaodingDialog = 1;

    //todo 定时发送定位
    private int sendPlaceFalg = 0;

    String saveweizhi="暂无数据";
    String snongdu="0",swendu="0",sguangqiang="0";
    //todo 数据更新获取解析多线程
    public Thread dataCheckThread;
    //todo 数据更新获取解析多线程
    public Handler dataCheckHandler=new Handler(){
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==10){
                info3.setText(saveweizhi);
            }
            if(msg.what==32){
                tips();
                ShoudongFlag = 0;
            }
            if(msg.what==33){
                if(startBiaodingKailu==1){
                    tips_biaoding();
                }
            }
            if(msg.what==34){
                if(stopUpdate==1){
                    stopUpdate = 0;
                    text_biaoding.setText("标定失败");
                }

            }
            if(msg.what==35){
                if(stopUpdate==1){
                    stopUpdate = 0;
                    text_biaoding.setText("标定成功");
                }

            }
            if(msg.what==1001){
                if (isStart) {
                    tname.setBackground(getActivity().getDrawable(R.drawable.shuangchuan2));
                }
            }
            if(msg.what==1002){
                if (isStart) {
                    tname.setBackground(getActivity().getDrawable(R.drawable.shangchuan));
                }
            }
            if(msg.what==1003){
                tname.setBackground(null);
            }
            if(msg.what==100){
                //浓度命令
                if(returnAgreement.getCaozuo().equals("5")){
                    //浓度信息正确
                    if(returnAgreement.getMsg().equals("1") && returnAgreement.getData1()!=null){
                        Log.e("ajiexi","设置浓度");
                        if (getpre("TESTTYPE_SHOUCHI").equals("PPM.M")) {
                            nd.setText(returnAgreement.getData1()+" "+getpre("TESTTYPE_SHOUCHI"));
                        } else if (getpre("TESTTYPE_SHOUCHI").equals("LEL.M")) {
                            nd.setText(String.valueOf(Integer.valueOf(returnAgreement.getData1())/500)+" "+getpre("TESTTYPE_SHOUCHI"));
                        }else {
                            nd.setText(String.valueOf(Integer.valueOf(returnAgreement.getData1())/10000)+" "+getpre("TESTTYPE_SHOUCHI"));
                        }

                        ssmShouDongQuZheng.setNongdu(returnAgreement.getData1());
                    }
                    //光强
                }else if(returnAgreement.getCaozuo().equals("6")){
                    gq.setText(returnAgreement.getData1());
                    pro_guangqiang.setProgress(Integer.parseInt(returnAgreement.getData1()));
                    ssmShouDongQuZheng.setGuangqiang(returnAgreement.getData1());

                    //温度
                }else if(returnAgreement.getCaozuo().equals("7")){
                    wd.setText(returnAgreement.getData1());
                    ssmShouDongQuZheng.setWendu(returnAgreement.getData1());

                }else if(returnAgreement.getCaozuo().equals("-1111")){

                    //todo 一键标定
                }else if(returnAgreement.getCaozuo().equals("9")){
                    yijianbiaoding = returnAgreement.getData1();
                    if (biaodingDialog == 1) {
                        //完成
                        if (yijianbiaoding.contains("0")){
                            if (startBiaodingKailu == 1){
                                dataCheckHandler.sendEmptyMessage(35);
                                startBiaodingKailu = 0;
                            }
                            //标定中
                        }else if(yijianbiaoding.contains("1")){
                            if(stopUpdate==1){
                                text_biaoding.setText("标定中");
                            }
                            //失败
                        }else if(yijianbiaoding.contains("2")){
                            if (startBiaodingKailu == 1){
                                dataCheckHandler.sendEmptyMessage(34);
                                startBiaodingKailu = 0;
                            }
                        }
                    }


                    Log.e("biaodings",yijianbiaoding);
                }


                Log.e("ssDialogTime1" ,String.valueOf(DialogTime));
                Log.e("ssDialogTime2aa" ,String.valueOf(snongdu));
                Log.e("ssDialogTime3" ,String.valueOf(IsDialogVisible));
                Log.e("ssDialogTime4" ,String.valueOf(ssmShouDongQuZheng2.getNongdu()));
                Log.e("tiaojian"," DialogTime "+DialogTime+" isStart "+isStart +" IsDialogVisible "+IsDialogVisible);
                //GAOJINGZHI1_SHOUCHI
                if (DialogTime == 0 && isStart) {
                    Log.e("ssDialogTimeaaaaa" ,String.valueOf(ssmShouDongQuZheng2.getNongdu()));
                    ssmShouDongQuZheng2 = ssmShouDongQuZheng;
                    if ((Integer.valueOf(ssmShouDongQuZheng.getNongdu()) >= Integer.valueOf(getpre("GAOJINGZHI1_SHOUCHI")))&&
                            (Integer.valueOf(ssmShouDongQuZheng.getNongdu()) < Integer.valueOf(getpre("GAOJINGZHI2_SHOUCHI")))) {
                        if (IsDialogVisible == 0) {
                           // onClickReqPermission();
                            snongdu = ssmShouDongQuZheng.getNongdu();
                            Log.e("snongdu","更新1");
                            swendu = ssmShouDongQuZheng.getWendu();
                            sguangqiang = ssmShouDongQuZheng.getGuangqiang();
                          //  IsDialogVisible = 1;
                            DialogTime = 35000;
                            Log.e("daying111","报警1");
                            pre("setflag", "2");
                            //         dataList.add(ssmShouDongQuZheng);
                            tips();
                        }
                    } else if ((Integer.valueOf(ssmShouDongQuZheng.getNongdu()) >= Integer.valueOf(getpre("GAOJINGZHI2_SHOUCHI"))) &&
                            (Integer.valueOf(ssmShouDongQuZheng.getNongdu()) < Integer.valueOf(getpre("GAOJINGZHI3_SHOUCHI")))) {
                        if (IsDialogVisible == 0) {
                         //   onClickReqPermission();
                            snongdu = ssmShouDongQuZheng.getNongdu();
                            Log.e("snongdu","更新2");
                            swendu = ssmShouDongQuZheng.getWendu();
                            sguangqiang = ssmShouDongQuZheng.getGuangqiang();
                          //  IsDialogVisible = 1;
                            DialogTime = 35000;
                            // ssmShouDongQuZheng2 = ssmShouDongQuZheng;
                            Log.e("daying111","报警2");
                            pre("setflag", "2");
                            // dataList.add(ssmShouDongQuZheng);
                            tips();
                        }
                    } else if (Integer.valueOf(ssmShouDongQuZheng.getNongdu()) >= Integer.valueOf(getpre("GAOJINGZHI3_SHOUCHI"))) {
                        if (IsDialogVisible == 0) {
                          //  onClickReqPermission();
                            snongdu = ssmShouDongQuZheng.getNongdu();
                            Log.e("snongdu","更新3");
                            swendu = ssmShouDongQuZheng.getWendu();
                            sguangqiang = ssmShouDongQuZheng.getGuangqiang();
                         //   IsDialogVisible = 1;
                            DialogTime = 35000;
//                            ssmShouDongQuZheng2 = ssmShouDongQuZheng;
//                            ssmShouDongQuZheng3 = ssmShouDongQuZheng;
                            Log.e("daying111","报警3");
                            pre("setflag", "2");
                            tips();
                        }
                    }
                }
                mylista.add(Integer.valueOf(ssmShouDongQuZheng.getNongdu()));
//                //todo　如果低于报警又高于报警  java.lang.IndexOutOfBoundsException: Index: 24, Size: 24
                if (mylista.size() >= 25 && isStart) {
                    if (mylista.get(23) < Integer.valueOf(getpre("GAOJINGZHI1_SHOUCHI")) &&
                            mylista.get(24) > Integer.valueOf(getpre("GAOJINGZHI1_SHOUCHI")) && IsDialogVisible == 0) {
                      //  onClickReqPermission();
                        snongdu = String.valueOf(mylista.get(24));
                        Log.e("snongdu","更新4");
                        swendu = ssmShouDongQuZheng.getWendu();
                        sguangqiang = ssmShouDongQuZheng.getGuangqiang();
                        DialogTime = 35000;
                        ssmShouDongQuZheng2 = ssmShouDongQuZheng;
                        pre("setflag", "2");
                        tips();
                      //  IsDialogVisible = 1;
                        Log.e("ssDialogTime777","11111");
                    }
                }

                setData2(mylista);
                if(mylista.size()>25){
                    mylista.remove(0);
                }
                Log.e("aaaaass",""+mylista.size());

                if (islanyaTime < 0){
                    lanyaClose2 = "连接中";
                }
            }

            if(msg.what == 101){
                toastShow("未开启巡检无法手动取证");
            }
        }

    };

    int index=0;
    View view;
    int IsDialogVisible=0;
    int isfirst = 100;
    String yijianbiaoding="chaoshi";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(MainActivity.this,"您申请了动态权限",Toast.LENGTH_SHORT).show();
        }else{
            //否则去请求相机权限
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},100);
        }
        view = inflater.inflate(R.layout.activity_device_control2, container, false);
        initZhexian();
        initAlyun(getActivity());
        //录像
//        mMediaProjectionManager = (MediaProjectionManager) getActivity().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//        ReadRunState();
//        if (!SC_IS_RUN) {
//            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), CAPTURE_CODE);
//        }
//        myBindService();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // tips();
            }
        },10000);

        return view;
    }

    //todo 阿里云上传
    public void aliyunShangchuan(String path, final ShouDongQuZheng mShouDongQuZheng){
        ALiUploadManager.getInstance().uploadFile(path, new ALiUploadManager.ALiCallBack() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result, String url) {
                Log.e("上传阿里云成功:", url);
                Log.e("上传阿里云成功:", result.toString());
                String url2 = "https://"+ ACCESS_BUCKET_NAME + "." + ACCESS_ENDPOINT + "/" + "aaa";
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startLuxiang(){
        if (SC_IS_RUN) {
            ReadRunState();
            if (SC_IS_RUN) {
                mBinder.StopScreenCapture();
                SC_IS_RUN = false;
                SaveRunState(SERVICE_IS_START, SERVICE_HAS_BIND, SC_IS_RUN);
             //   Toast.makeText(getActivity(), "屏幕录制服务停止运行", Toast.LENGTH_SHORT).show();
            } else {
               // Toast.makeText(getActivity(), "屏幕录制服务并没有开启，操作无效", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (!SC_IS_RUN) {
                startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), CAPTURE_CODE);
            }
            ReadRunState();
            if (!SC_IS_RUN) {
                if (Build.VERSION.SDK_INT >= 23) {
                    //如果API-23编译，使用Android运行时权限
                    AskForPermission();
                }
                myShareScreen();
               // Toast.makeText(getActivity(), "屏幕录制服务开始运行", Toast.LENGTH_SHORT).show();
            } else {
               // Toast.makeText(getActivity(),"屏幕录制服务已经开启，操作无效", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void ssint() {
        initData();
        Log.e("aaaacccc","1111111");
        init();
        myDatebaseHelper = new MyDatabase(getActivity(), SQLITE_NAME, null, 1);

        createInit();
        updateDataByBuleTooth();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void createInit() {
        isStart = false;
        Log.e("aaxianchen","--------------------重启");
        if (devices!=null){
            // Toast.makeText(getActivity(), "111111111111111111", Toast.LENGTH_SHORT).show();
            BusManager.getBus().unregister(this);
            BusManager.getBus().register(this);
            init();
            if (BluetoothDeviceManager.getInstance().isConnected(mDevice)) {
                DeviceMirror deviceMirror = ViseBle.getInstance().getDeviceMirror(mDevice);
                if (deviceMirror != null) {
                    simpleExpandableListAdapter = displayGattServices(deviceMirror.getBluetoothGatt().getServices());
                }
                showDefaultInfo();
            } else {
                clearUI();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    if (!BluetoothDeviceManager.getInstance().isConnected(mDevice)) {
                        BluetoothDeviceManager.getInstance().connect(mDevice);
                        getActivity().invalidateOptionsMenu();
                    }
                }
            },50);
        }
        bluesenddata.clear();
        bluesenddata.add(new send(0,readConcentrationAgreement));
        bluesenddata.add(new send(0,readConcentrationAgreement2));
        bluesenddata.add(new send(0,readConcentrationAgreement));
        bluesenddata.add(new send(0,readConcentrationAgreement3));
    }

    private void initZhexian() {
        lineChart = f(view,R.id.lineChart);
        mylista.clear();
        for (int i = 0; i < 23; i++) {
            mylista.add(0);
        }

        setData2(mylista);
    }

    public void setData2(final List<Integer> list){
        //显示边界
        lineChart.setDrawBorders(false);
        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
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
        lineDataSet.setDrawValues(false);//设置是否显示点的坐标值
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
        yAxis.setDrawGridLines(true);
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


    private void updateDataByBuleTooth() {
        if(dataCheckThread==null){
            dataCheckThread=new Thread(){
                @Override
                public void run() {
                    super.run();
                    while (true){
                        try {
                            sleep(120);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //添加 执行的数据
                        addList();

                        if(biaodingDialog == 0 && startBiaodingKailu == 1){
                            dataCheckHandler.sendEmptyMessage(33);
                        }

                        Log.e("sendPlaceFalg","执行中"+sendPlaceFalg+"---"+Shouchisendlocation.getLatitude());
                        if(sendPlaceFalg > 50 && Shouchisendlocation!=null){
                            sendPlaceFalg = 0;
                            SendPost();
                        }else {
                            sendPlaceFalg++;
                        }

                        //todo 停止巡检
                        if(!isStart){
                            Log.e("aaxianchen","执行中");
                            //判断是否是暂停
                            //TODO 暂停
                            Log.e("aaxianchen","绘图");
                            if(ShoudongFlag == 1){
                                ShoudongFlag = 0;
                                dataCheckHandler.sendEmptyMessage(101);
                            }
                            dataCheckHandler.sendEmptyMessage(1003);
                            continue;
                        }

                        if(DialogTime > 0){
                            DialogTime = DialogTime - 120;
                        }else {
                            DialogTime = 0;
                        }
                        Log.e("aaDialogTime",""+DialogTime);
                        Log.e("shoudongquzhengsa",""+ShoudongFlag+"---"+IsDialogVisible);
                        if(ShoudongFlag == 1 && IsDialogVisible == 0){
                            ssmShouDongQuZheng2 = ssmShouDongQuZheng;
                            snongdu = ssmShouDongQuZheng.getNongdu();
                            swendu = ssmShouDongQuZheng.getWendu();
                            sguangqiang = ssmShouDongQuZheng.getGuangqiang();
                            Log.e("snongdu","更新5");
                            pre("setflag", "1");
                          //  onClickReqPermission();
                            dataCheckHandler.sendEmptyMessage(32);

                            Log.e("ssDialogTime5" ,String.valueOf(ssmShouDongQuZheng2.getNongdu()));
                        }

                        islanyaTime -= 120;
                    }
                }
            };
            //线程优先级 //标准后台优先级，优先级略低于正常优先级，它对用户界面的影响非常小
            dataCheckThread.setPriority(THREAD_PRIORITY_BACKGROUND);
            dataCheckThread.start();
        }
    }

    private void addList() {
        if(index > bluesenddata.size() - 1){
            index=0;
        }else {
            if(bluesenddata.get(index).getFlag()==0){
                SendData(bluesenddata.get(index).getData());
            }else if(bluesenddata.get(index).getFlag()==1){
                yijianbiaoding = "chaoshi";
                SendData(bluesenddata.get(index).getData());
                bluesenddata.remove(index);
            }
            index++;
        }
    }

    private void init() {
        nd = (TextView) view.findViewById(R.id.nd);
        wd = (TextView) view.findViewById(R.id.wd);
        gq = (TextView) view.findViewById(R.id.text_guangqiang);
        pro_guangqiang = view.findViewById(R.id.pro_guangqiang);
        pro_guangqiang.setMax(20);
        textlianjie = (TextView) view.findViewById(R.id.textlianjie);
        textlianjie.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "重新连接...", Toast.LENGTH_SHORT).show();
               // init();
                if (BluetoothDeviceManager.getInstance().isConnected(mDevice)) {
                    DeviceMirror deviceMirror = ViseBle.getInstance().getDeviceMirror(mDevice);
                    if (deviceMirror != null) {
                        simpleExpandableListAdapter = displayGattServices(deviceMirror.getBluetoothGatt().getServices());
                    }
                    showDefaultInfo();
                } else {
                    clearUI();
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @SuppressLint("NewApi")
                    @Override
                    public void run() {
                        if (!BluetoothDeviceManager.getInstance().isConnected(mDevice)) {
                            BluetoothDeviceManager.getInstance().connect(mDevice);
                            getActivity().invalidateOptionsMenu();
                        }
                    }
                },50);
            }
        });
        //mDevice = getActivity().getIntent().getParcelableExtra(DeviceDetailActivity.EXTRA_DEVICE);
        mDevice = devices;

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Subscribe
    public void showConnectedDevice(ConnectEvent event) {
        if (event != null) {
            if (event.isSuccess()) {
                textlianjie.setText("• 连接成功");
                showToast(getActivity(), "连接成功!");
                getActivity().invalidateOptionsMenu();
                if (event.getDeviceMirror() != null && event.getDeviceMirror().getBluetoothGatt() != null) {
                    simpleExpandableListAdapter = displayGattServices(event.getDeviceMirror().getBluetoothGatt().getServices());
                }
                //add
                zidongadd();


            } else {
                if (event.isDisconnected()) {
                    textlianjie.setText("• 断开");
                   // ToastUtil.showToast(getActivity(), "Disconnect!");
                } else {
                    textlianjie.setText("• 连接失败");
                 //   ToastUtil.showToast(getActivity(), "Connect Failure!");
                }
                getActivity().invalidateOptionsMenu();
                clearUI();
            }
        }
    }

    private void zidongadd() {
        Handler handler =new Handler();
        handler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {
                //showGattServices();
                final BluetoothGattService service = mGattServices.get(0);
                final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(0).get(1);
                final int charaProp = characteristic.getProperties();
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_WRITE, service.getUuid(), characteristic.getUuid(), null);
                } else if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_READ, service.getUuid(), characteristic.getUuid(), null);
                    BluetoothDeviceManager.getInstance().read(mDevice);
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_NOTIFY, service.getUuid(), characteristic.getUuid(), null);
                    BluetoothDeviceManager.getInstance().registerNotify(mDevice, false);
                } else if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_INDICATE, service.getUuid(), characteristic.getUuid(), null);
                    BluetoothDeviceManager.getInstance().registerNotify(mDevice, true);
                }
            }
        },50);
        handler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {
                //showGattServices();
                final BluetoothGattService service = mGattServices.get(0);
                final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(0).get(0);
                final int charaProp = characteristic.getProperties();
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_WRITE, service.getUuid(), characteristic.getUuid(), null);
                } else if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_READ, service.getUuid(), characteristic.getUuid(), null);
                    BluetoothDeviceManager.getInstance().read(mDevice);
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_NOTIFY, service.getUuid(), characteristic.getUuid(), null);
                    BluetoothDeviceManager.getInstance().registerNotify(mDevice, false);
                } else if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_INDICATE, service.getUuid(), characteristic.getUuid(), null);
                    BluetoothDeviceManager.getInstance().registerNotify(mDevice, true);
                }
            }
        },50);
    }

    @Subscribe
    public void showDeviceCallbackData(CallbackDataEvent event) {
        if (event != null) {
            if (event.isSuccess()) {
                if (event.getBluetoothGattChannel() != null && event.getBluetoothGattChannel().getCharacteristic() != null
                        && event.getBluetoothGattChannel().getPropertyType() == PropertyType.PROPERTY_READ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        showReadInfo(event.getBluetoothGattChannel().getCharacteristic().getUuid().toString(), event.getData());
                    }
                }
            } else {
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onResume() {
        getActivity().invalidateOptionsMenu();
        super.onResume();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDestroy() {
      //  startLuxiang();
        BusManager.getBus().unregister(this);
        myUnbindService();
        myStopService();
        super.onDestroy();
    }

    /**
     * 根据GATT服务显示该服务下的所有特征值
     *
     * @param gattServices GATT服务
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private SimpleExpandableListAdapter displayGattServices(final List<BluetoothGattService> gattServices) {
        if (gattServices == null) return null;
        String uuid;
        final String unknownServiceString = getResources().getString(R.string.unknown_service);
        final String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        final List<Map<String, String>> gattServiceData = new ArrayList<>();
        final List<List<Map<String, String>>> gattCharacteristicData = new ArrayList<>();

        mGattServices = new ArrayList<>();
        mGattCharacteristics = new ArrayList<>();

        // Loops through available GATT Services.
        for (final BluetoothGattService gattService : gattServices) {
            final Map<String, String> currentServiceData = new HashMap<>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(LIST_NAME, GattAttributeResolver.getAttributeName(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            final List<Map<String, String>> gattCharacteristicGroupData = new ArrayList<>();
            final List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            final List<BluetoothGattCharacteristic> charas = new ArrayList<>();

            // Loops through available Characteristics.
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                final Map<String, String> currentCharaData = new HashMap<>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(LIST_NAME, GattAttributeResolver.getAttributeName(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }

            mGattServices.add(gattService);
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }

        final SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(getActivity(), gattServiceData, android.R.layout
                .simple_expandable_list_item_2, new String[]{LIST_NAME, LIST_UUID}, new int[]{android.R.id.text1, android.R.id.text2},
                gattCharacteristicData, android.R.layout.simple_expandable_list_item_2, new String[]{LIST_NAME, LIST_UUID}, new
                int[]{android.R.id.text1, android.R.id.text2});
        return gattServiceAdapter;
    }

    private void showReadInfo(String uuid, byte[] dataArr) {
    }

    private void showDefaultInfo() {
        mOutputInfo = new StringBuilder();
    }

    private void clearUI() {
        mOutputInfo = new StringBuilder();
        simpleExpandableListAdapter = null;;
    }


    private boolean isHexData(String str) {
        if (str == null) {
            return false;
        }
        char[] chars = str.toCharArray();
        if ((chars.length & 1) != 0) {//个数为奇数，直接返回false
            return false;
        }
        for (char ch : chars) {
            if (ch >= '0' && ch <= '9') continue;
            if (ch >= 'A' && ch <= 'F') continue;
            if (ch >= 'a' && ch <= 'f') continue;
            return false;
        }
        return true;
    }

    public void SendData(String msg){
        BluetoothDeviceManager.getInstance().write(mDevice, HexUtil.decodeHex(msg.toCharArray()));
    }


    @Subscribe
    public void showDeviceNotifyData(NotifyDataEvent event) {
        if (event != null && event.getData() != null && event.getBluetoothLeDevice() != null
                && event.getBluetoothLeDevice().getAddress().equals(mDevice.getAddress())) {
            Log.e("ssssssaaaa", String.valueOf(mOutputInfo));
            mOutputInfo.delete(0, mOutputInfo.length());
            mOutputInfo.append(HexUtil.encodeHexStr(event.getData())).append("--");
            String a[]=mOutputInfo.toString().split("--");
            returnAgreement = jiexi2(a[a.length-1]);
            islanyaTime = 10000;
            dataCheckHandler.sendEmptyMessage(100);
        }
    }


    private void tips() {
        IsDialogVisible = 1;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.tips_che_shou3, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.setCancelable(false);
        final TextView biaoti = view.findViewById(R.id.textView12);
        final TextView info1 = view.findViewById(R.id.info1);
        final TextView info2 = view.findViewById(R.id.info2);
        info3 = view.findViewById(R.id.info3);
        final TextView info4 = view.findViewById(R.id.info4);
        final TextView info5 = view.findViewById(R.id.info5);
        final TextView info6 = view.findViewById(R.id.info6);
        final TextView info7 = view.findViewById(R.id.info7);
        final TextView info9 = view.findViewById(R.id.info9);
        final ImageView exit = view.findViewById(R.id.exit);
        final EditText beizhu = view.findViewById(R.id.beizhu);
        final Button submit = view.findViewById(R.id.button3);

        if(poiInfoListForGeoCoder2 != null &&poiInfoListForGeoCoder2.size()>0){
            info3.setText(poiInfoListForGeoCoder2.get(0).getName()+"   "+poiInfoListForGeoCoder2.get(0).getAddress());
        } else {
            info3.setText("暂无数据");
        }

        info3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tips_weizhixuanze();
            }
        });

        paizhaoimageView = view.findViewById(R.id.imageView12);
        LinearLayout ssaa = view.findViewById(R.id.ssaa);
        if(getpre("setflag").equals("1")){
            biaoti.setText("手动取证");
        }else {
            biaoti.setText("报警取证");
        }
        //拍照图片归 0
        ssmShouDongQuZheng2.setImagePath2("111");
        String usetime = disposeTime();
        info1.setText(" "+getpre("username"));
        info2.setText(" "+"手持");
        paizhaoimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStart = false;
                btnclick();
            }
        });
        info4.setText(" "+usetime);
        info5.setText(snongdu);
        info6.setText(swendu);
        info7.setText(sguangqiang);
        info9.setText("  起始时间   "+getpre("starttime")+" - "+usetime);
        Log.e("aaaaaaaaa",devices.getName());
        Log.e("aaaaaaaaa",getpre("username"));
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

                final ShouDongQuZheng mShouDongQuZheng = new ShouDongQuZheng();
                mShouDongQuZheng.setShebeiname(devices.getName());
                mShouDongQuZheng.setUsername(getpre("username"));
                mShouDongQuZheng.setType("手持");
                if(poiInfoListForGeoCoder2 != null &&poiInfoListForGeoCoder2.size()>0){
                    mShouDongQuZheng.setDidian(info3.getText().toString());
                } else {
                    mShouDongQuZheng.setDidian("暂无");
                }
                mShouDongQuZheng.setJingdu(ssmShouDongQuZheng2.getJingdu());
                mShouDongQuZheng.setWeidu(ssmShouDongQuZheng2.getWeidu());
                mShouDongQuZheng.setPianyijingdu("--");
                mShouDongQuZheng.setPianyiweidu("--");
                mShouDongQuZheng.setNongdu(snongdu);
                mShouDongQuZheng.setWendu(swendu);
                mShouDongQuZheng.setGuangqiang(sguangqiang);
                mShouDongQuZheng.setChesu("--");
                mShouDongQuZheng.setStarttime(disposeTime());
                mShouDongQuZheng.setEndtime(disposeTime());
                mShouDongQuZheng.setEndtime2(disposeTime2());
                if(beizhu.getText().toString().equals("")){
                    mShouDongQuZheng.setBeizhu("无");
                }else {
                    mShouDongQuZheng.setBeizhu(beizhu.getText().toString());
                }

              //  mShouDongQuZheng.setImagePath(imaPath);
                //todo 图片
                alyima1 ="create";
                alyima2 ="create";
                mShouDongQuZheng.setImagePath(getBaidujietu(getSavedPath3()));
                mShouDongQuZheng.setImagePath2(ssmShouDongQuZheng2.getImagePath2());
                aliyunShangchuan(mShouDongQuZheng.getImagePath(),mShouDongQuZheng);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ssmShouDongQuZheng2.getImagePath2().equals("111")){
                            alyima2 ="create-null";
                        } else {
                            aliyunShangchuan(ssmShouDongQuZheng2.getImagePath2(),mShouDongQuZheng);
                        }
                    }
                },100);


                Log.e("aashangchuan1",mShouDongQuZheng.getImagePath());
                Log.e("aashangchuan2",mShouDongQuZheng.getImagePath2());
               // aliyunShangchuan("/storage/emulated/0/DCIM/Screenshots/Screenshot_20220806_160104.jpg");
                Log.e("aashangchuan3",alyima1+"--"+alyima2);
              //  toastShow(mShouDongQuZheng.getImagePath2());

                if(Integer.valueOf(ssmShouDongQuZheng2.getNongdu())>=Integer.valueOf(getpre("GAOJINGZHI1_SHOUCHI"))
                    &&Integer.valueOf(ssmShouDongQuZheng2.getNongdu())<Integer.valueOf(getpre("GAOJINGZHI2_SHOUCHI"))){
                    mShouDongQuZheng.setGrade("1");
                }else if(Integer.valueOf(ssmShouDongQuZheng2.getNongdu())>=Integer.valueOf(getpre("GAOJINGZHI2_SHOUCHI"))
                        &&Integer.valueOf(ssmShouDongQuZheng2.getNongdu())<Integer.valueOf(getpre("GAOJINGZHI3_SHOUCHI"))){
                    mShouDongQuZheng.setGrade("2");
                }else if(Integer.valueOf(ssmShouDongQuZheng2.getNongdu())>=Integer.valueOf(getpre("GAOJINGZHI3_SHOUCHI"))){
                    mShouDongQuZheng.setGrade("3");
                }else {
                    mShouDongQuZheng.setGrade("0");
                }

                mShouDongQuZheng.setFlag(getpre("setflag"));
                mShouDongQuZheng.setUid("1");
                addCheZaiBaoJingData(myDatebaseHelper,mShouDongQuZheng);
                toastShow("保存成功...");

                IsDialogVisible = 0;
                beizhu.clearFocus();
                beizhu.setVisibility(View.GONE);
                closejianpan2(getActivity(),beizhu);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SendPost2(mShouDongQuZheng);
                    }
                },150);

                dialog.dismiss();
            }
        });


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IsDialogVisible = 0;
                beizhu.clearFocus();
                beizhu.setVisibility(View.GONE);
                closejianpan2(getActivity(),beizhu);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void onClickReqPermission() {
        Log.e("ajietu","2");
        if (Build.VERSION.SDK_INT >= 21) {
            startActivityForResult(createScreenCaptureIntent(), REQ_CODE_PER);
            Log.e("daying111","启动截图权限");
        }
        Log.e("ajietu","4");
    }
    @SuppressLint("WrongConstant")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Intent createScreenCaptureIntent() {
        //Here using media_projection instead of Context.MEDIA_PROJECTION_SERVICE to  make it successfully build on low api.
        Log.e("ajietu","3");
        return ((MediaProjectionManager) getActivity().getSystemService("media_projection")).createScreenCaptureIntent();
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
     * @param view
     */
    public void onClickShot(View view) {
        startActivityForResult(ScreenShotActivity.createIntent(getActivity(), null,0), REQ_CODE_ACT);
        // toast("Press home key,open another app.");//if you want to take screenshot on another app.
    }




    @SuppressLint("NewApi")
    private String getSavedPath() {
        //REQUEST_TAKE_PHOTO_CODE2
        //filePath = PdfUtils.ADDRESS + File.separator + "集控PDF_"
        //                    + disposeTime() + ".png";
        return Environment.getExternalStorageDirectory().getPath()+"/ScreenCaptureShouchi/jk" +
                SystemClock.currentThreadTimeMillis() + disposeTime()+".png";
//        return getActivity().getExternalFilesDir("screenshot").getAbsoluteFile() + "/"
//                + SystemClock.currentThreadTimeMillis() + disposeTime()+".png";
    }
    @SuppressLint("NewApi")
    private String getSavedPath3() {
        //REQUEST_TAKE_PHOTO_CODE2
        //filePath = PdfUtils.ADDRESS + File.separator + "集控PDF_"
        //                    + disposeTime() + ".png";
        return Environment.getExternalStorageDirectory().getPath()+"/ScreenCaptureShouchi/";
//        return getActivity().getExternalFilesDir("screenshot").getAbsoluteFile() + "/"
//                + SystemClock.currentThreadTimeMillis() + disposeTime()+".png";
    }
    @SuppressLint("NewApi")
    private String getSavedPath2() {
        //REQUEST_TAKE_PHOTO_CODE2
        //filePath = PdfUtils.ADDRESS + File.separator + "集控PDF_"
        //                    + disposeTime() + ".png";
        return getActivity().getExternalFilesDir("screenshot").getAbsoluteFile() + "/"
                + SystemClock.currentThreadTimeMillis() + disposeTime()+".jpg";
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v("onActivityResult", "onActivityResult");
        if (requestCode == CAPTURE_CODE) {
            if (resultCode != RESULT_OK) {
                return;
            } else {
                mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            }
        }
        // 拍照后获取返回值，这里获取到的是原始图片
        if (requestCode == REQUEST_TAKE_PHOTO_CODE
                && resultCode == Activity.RESULT_OK) {
            // 获取到了拍照后的图片文件，从文件解码出Bitmap对象
            if (imageFile.exists()) {
                // 这里直接decode了图片，没有判断图片大小，没有对可能出现的OOM做处理
                Bitmap bm = BitmapFactory.decodeFile(imageFile
                        .getAbsolutePath());
                // 显示图片
                paizhaoimageView.setImageBitmap(bm);
                toastShow("1111"+getSavedPath());
                ssmShouDongQuZheng2.setImagePath2(getSavedPath());
            } else {
                Toast.makeText(getActivity(), "图片文件不存在", Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == REQUEST_TAKE_PHOTO_CODE2
                && resultCode == Activity.RESULT_OK){
            //这里获取到的是缩放后的图片，不是原始图片
            Bundle b = data.getExtras();
            if(b != null){
                final Bitmap bm = (Bitmap) b.get("data");
                toastShow("222"+getSavedPath());
                if(bm != null){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isStart = true;
                            saveIma(bm);
                        }
                    },100);
                }
            }else{
                Toast.makeText(getActivity(), "没有获取数据", Toast.LENGTH_SHORT).show();
            }
        }
        switch (requestCode) {

            case REQ_CODE_ACT: {
                if (resultCode == RESULT_OK && data != null) {
                    // toast("Screenshot saved at " + data.getData().toString());

                }
                else{
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
                                   // toastShow(path);
                                    Log.e("daying111","截图获得地址");
                                   // tips(path);

                                }

                                @Override
                                public void onError() {
                                    // toast("You got wrong.");
                                }
                            }
                    );
                } else if (resultCode == RESULT_CANCELED) {
                    //user canceled.
                    Log.e("quanxians","quxiao");
                    IsDialogVisible = 0;
                } else {

                }
            }
        }
    }

    public void initData(){
        ssmShouDongQuZheng.setShebeiname(devices.getName());
        ssmShouDongQuZheng.setUsername(getpre("username"));
        ssmShouDongQuZheng.setType("手持");
        ssmShouDongQuZheng.setDidian("dsadsa");
        ssmShouDongQuZheng.setJingdu("0");
        ssmShouDongQuZheng.setWeidu("0");
        ssmShouDongQuZheng.setPianyijingdu("0");
        ssmShouDongQuZheng.setPianyiweidu("0");
        ssmShouDongQuZheng.setNongdu("0");
        ssmShouDongQuZheng.setWendu("0");
        ssmShouDongQuZheng.setGuangqiang("0");
        ssmShouDongQuZheng.setChesu("0");
        ssmShouDongQuZheng.setStarttime(disposeTime());
        ssmShouDongQuZheng.setEndtime(disposeTime());
        ssmShouDongQuZheng.setEndtime2(disposeTime2());
        ssmShouDongQuZheng.setBeizhu("aaa");
        ssmShouDongQuZheng.setImagePath("111");
        ssmShouDongQuZheng.setImagePath2("111");
        ssmShouDongQuZheng.setGrade("0");
        ssmShouDongQuZheng.setFlag("0");
        ssmShouDongQuZheng.setUid("1");
        if(isfirst == 100){
            ssmShouDongQuZheng2 = ssmShouDongQuZheng;
            isfirst++;
        }
        isfirst++;
        Log.e("isfirst",String.valueOf(isfirst));

    }

    //todo 拍照 -----------------------------------------------------------------------------------------


    /**
     * 判断是否有SD卡
     *
     * @return 有SD卡返回true，否则false
     */
    private boolean hasSDCard() {
        // 获取外部存储的状态
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // 有SD卡
            return true;
        }
        return false;
    }

    /**
     * 初始化存储图片的文件
     *
     * @return 初始化成功返回true，否则false
     */
    private boolean initImageFile() {
        // 有SD卡时才初始化文件
        if (hasSDCard()) {
            // 构造存储图片的文件的路径，文件名为当前时间
            String filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + "/"
                    + System.currentTimeMillis()
                    + ".png";
//            filePath = getSavedPath();
//            filePath = PdfUtils.ADDRESS + File.separator + "集控PDF_"
//                    + disposeTime() + ".png";
            imageFile = new File(filePath);
            if (!imageFile.exists()) {// 如果文件不存在，就创建文件
                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public String saveIma(Bitmap bitmap) {
        String path = "";

//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                "aaa" + disposeTime() + ".jpg");
        File file = new File(getSavedPath2());

        Log.e("aaazzz", "2");
        try {
            Log.e("aaazzz", "3"+file.exists());
            if (!file.exists()){
                file.createNewFile();
            }
            Log.e("aaazzz", "3");
            boolean ret = saveSrc(bitmap, file, Bitmap.CompressFormat.JPEG, true);
            if (ret) {
                path = file.getAbsolutePath();
                ssmShouDongQuZheng.setImagePath2(path);
                ssmShouDongQuZheng2.setImagePath2(path);
                Glide.with(getActivity()).load(path).into(paizhaoimageView);
                Log.e("aaazzz111111", path);
                Toast.makeText(getActivity().getApplicationContext(), "截图已保持至 " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
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

    /**
     * 跳转到系统相机拍照
     */
    private void startTakePhoto() {
        // 启动系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 设置拍照后保存的图片存储在文件中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        // 启动activity并获取返回数据
        startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE);
    }

    private void startTakePhoto2() {
        // 启动系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 启动activity并获取返回数据
        startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE2);
    }

    // 点击按钮后调用相机拍照
    public void btnclick() {
        // 如果初始化文件成功，则调用相机
        if (initImageFile()) {
            //下面用第一种方式获取图片，还可以调用startTakePhoto2()方法获取图片
            //       startTakePhoto();
            startTakePhoto2();
        } else {
            Toast.makeText(getActivity(), "初始化文件失败，无法调用相机拍照！", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    //标定 是否
    private void tips_biaoding() {
        if (biaodingDialog == 1) {
           return;
        }
        biaodingDialog = 1;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_isbiaoding, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final Button yes = view.findViewById(R.id.button5);
        final Button no = view.findViewById(R.id.button9);
        text_biaoding = view.findViewById(R.id.textView29);
        text_biaoding.setText("等待标定状态");
        startBiaodingDialog = 0;
        final Runnable mRunable = new Runnable() {
            @Override
            public void run() {
                //失败弹窗
                if(startBiaodingKailu == 1) {
                    stopUpdate = 1;
                    dataCheckHandler.sendEmptyMessage(34);
                    startBiaodingKailu = 0;
                }
            }
        };
        intenhandler.postDelayed(mRunable,305000);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biaodingDialog = 0;
                startBiaodingKailu = 0;
                intenhandler.removeCallbacks(mRunable);
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biaodingDialog = 0;
                startBiaodingKailu = 0;
                intenhandler.removeCallbacks(mRunable);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void initGeoCoderListView(final ListView listView) {
        if(poiInfoListForGeoCoder2.size()>0){
            saveweizhi=poiInfoListForGeoCoder2.get(0).getName()+"  "+poiInfoListForGeoCoder2.get(0).getAddress();
        }else {
            saveweizhi="暂无数据";
        }
        adapter_searchAddress = new Adapter_SearchAddress2(poiInfoListForGeoCoder2, getActivity(), currentLatLng2) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                final LinearLayout t1 = (LinearLayout) view.findViewById(R.id.t1);
                t1.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onClick(View v) {
                        saveweizhi=poiInfoListForGeoCoder2.get(position).getName()+"  "+poiInfoListForGeoCoder2.get(position).getAddress();
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

    }
    //todo
    private void tips_weizhixuanze() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.tips223, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();

        final ListView getdizhiListview = (ListView) view.findViewById(R.id.alistview1);
        Button ok = view.findViewById(R.id.button3);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCheckHandler.sendEmptyMessage(10);
                dialog.dismiss();
            }
        });
        initGeoCoderListView(getdizhiListview);
        dialog.show();

    }


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
            mBinder = (MyBinder) service;
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
        Log.v("SaveRunState", "SERVICE_IS_START = " + SERVICE_IS_START + ";SERVICE_HAS_BIND = " + SERVICE_HAS_BIND + ";SC_IS_RUN = " + SC_IS_RUN);
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

    //todo 上传记录 http://123.56.230.79:6799/user/JikongSave/SaveAlert?
    // deviceName=321321&userName=321&type=32&longitude=321&latitude=321&
    // gpslongitude=5125&gpslatitude=21424&concentration=321&temperature=3213&
    // lightIntensity=4214&nospeed=5215&starttime=3123&endtime=321312&grade=5215&automatic=312321&uid=3213
    public void SendPost2(ShouDongQuZheng mShouDongQuZheng){
        final String uid = mShouDongQuZheng.getUid()+"AA";
        final int id = mShouDongQuZheng.getId();
        Log.e("sssendpost2","1111");
        Map<String,String> hs=new HashMap<>();
        //http://localhost:8080/user/saveShops?latitude=33&longitude=23&placeTime=dsdsad&placeName=909
        //INSERT INTO alerts (deviceName, userName, type, longitude, latitude
        //        , gpslongitude, gpslatitude, CH4, temperature, lightIntensity
        //        , nospeed, starttime, endtime, grade, automatic, uid,screenshotImaPath,
        //        photoImaPath, remarks, resolve, implementer, exe_time)
        hs.put("deviceName",getpre("androidIds"));
        hs.put("userName", getpre("username"));
        hs.put("type", mShouDongQuZheng.getType());
        hs.put("longitude", mShouDongQuZheng.getJingdu());
        hs.put("latitude",mShouDongQuZheng.getWeidu());
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
        hs.put("resolve","0");
        Log.e("aass22","上传");
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SaveAlert,hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                if(result.contains("成功")){
                    toastShow("上传成功");
                    lessUid(myDatebaseHelper,uid,id);
                    Log.e("aass22","上传成功");
                }else {

                }
            }
        });
    }

    public void SendPost(){
        Map<String,String> hs=new HashMap<>();
        //http://localhost:8080/user/saveShops?latitude=33&longitude=23&placeTime=dsdsad&placeName=909
        //latitude, longitude, placeTime, placeName, deviceStatus,
        //        userName,deviceName)
        //(#{latitude},#{longitude},
        //                      #{placeTime},#{placeName},#{deviceStatus},#{userName}
        //                      ,#{deviceName},#{alerts},#{fault},#{CH4},#{nospeed},#{video}
        //                      ,#{uid},#{temperature},#{light},#{IntervalData});
        hs.put("latitude", String.valueOf(Shouchisendlocation.getLatitude()));
        hs.put("longitude", String.valueOf(Shouchisendlocation.getLongitude()));
        Log.e("sspost getLatitude ", String.valueOf(Shouchisendlocation.getLatitude()));
        Log.e("sspost getLongitude ", String.valueOf(Shouchisendlocation.getLongitude()));
        Log.e("send-placeTime",disposeTime());
        hs.put("placeName", "待定");
        //todo 未开始巡检
        if(!isStart){
            hs.put("deviceStatus", "离线");
        }else {
            hs.put("deviceStatus", "在线");
        }
        hs.put("userName", getpre("username"));
        hs.put("deviceName",getpre("androidIds"));
        hs.put("alerts", "待定");
        hs.put("fault", "待定");
        hs.put("CH4", ssmShouDongQuZheng.getNongdu());
        hs.put("nospeed", "无");
        hs.put("video", "待定");
        hs.put("uid",getpre("eventId"));
        hs.put("temperature", ssmShouDongQuZheng.getWendu());
        hs.put("light", ssmShouDongQuZheng.getGuangqiang());
        hs.put("IntervalData", "待定");
        hs.put("placeTime", disposeTime());
        Log.e("send--post2","1");
        dataCheckHandler.sendEmptyMessage(1002);
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SavePlaceUrl,hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                if(result.contains("成功")){
                    Log.e("send--post2","2");
                    dataCheckHandler.sendEmptyMessage(1001);
                }else {
                    Log.e("send--post2","3");
                }
            }
        });
    }
}
