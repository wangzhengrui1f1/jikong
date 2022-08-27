package com.vise.bledemo.kailu.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.vise.baseble.ViseBle;
import com.vise.baseble.common.PropertyType;
import com.vise.baseble.core.DeviceMirror;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.resolver.GattAttributeResolver;
import com.vise.baseble.utils.HexUtil;
import com.vise.bledemo.Base.BaseFragment;
import com.vise.bledemo.R;
import com.vise.bledemo.bluetooth.common.BluetoothDeviceManager;
import com.vise.bledemo.bluetooth.event.CallbackDataEvent;
import com.vise.bledemo.bluetooth.event.ConnectEvent;
import com.vise.bledemo.bluetooth.event.NotifyDataEvent;
import com.vise.bledemo.model.send;
import com.vise.bledemo.utils.returnAgreement;
import com.vise.xsnow.event.BusManager;
import com.vise.xsnow.event.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static com.vise.bledemo.bluetooth.common.ToastUtil.showToast;
import static com.vise.bledemo.shouchi.data.Data_all.devices;
import static com.vise.bledemo.utils.Jiexi.jiexi2;
import static com.vise.bledemo.utils.ViewBean.closeblueview;
import static com.vise.bledemo.utils.url.Blue0x2800;
import static com.vise.bledemo.utils.url.Blue0x2801;
import static com.vise.bledemo.utils.url.Blue0x29;
import static com.vise.bledemo.utils.url.adata;
import static com.vise.bledemo.utils.url.allreturnAgreement;
import static com.vise.bledemo.utils.url.bluesenddata;

import static com.vise.bledemo.utils.url.getBiaogeng;
import static com.vise.bledemo.utils.url.intenhandler;
import static com.vise.bledemo.utils.url.readConcentrationAgreement;
import static com.vise.bledemo.utils.url.readConcentrationAgreement2;
import static com.vise.bledemo.utils.url.readConcentrationAgreement3;


public class blueToothDataFragment2 extends BaseFragment {
    private static final String LIST_NAME = "NAME";
    private static final String LIST_UUID = "UUID";
    public static final String WRITE_CHARACTERISTI_UUID_KEY = "write_uuid_key";
    public static final String NOTIFY_CHARACTERISTIC_UUID_KEY = "notify_uuid_key";
    public static final String WRITE_DATA_KEY = "write_data_key";

    private SimpleExpandableListAdapter simpleExpandableListAdapter;

    //开始标定
    int startBiaoding = 0;
    int indexBiaoding = 0;
    int startBiaodingDialog = 1;
    //设备信息
    private BluetoothLeDevice mDevice;
    //输出数据展示
    private StringBuilder mOutputInfo = new StringBuilder();
    private List<BluetoothGattService> mGattServices = new ArrayList<>();
    //设备特征值集合
    private List<List<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<>();
    private TextView nongdu,gq,wd,textlianjie;
    private LinearLayout baojing;
    private static final String TAG = "SHOUCHI_BLUE";
    //浓度
    returnAgreement returnAgreement;
    //低报警
    int dibao = 0,dibaoflag = 0;

    Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            //失败弹窗
            if(startBiaoding == 1) {
                startBiaoding = 0;
                mTipSuccessOrFaild(false);
            }
        }
    };

    //todo 数据更新获取解析多线程
    public Thread dataCheckThread;
    //todo 数据更新获取解析多线程
    public Handler dataCheckHandler=new Handler(){
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==99){
                status.setText("工作中");
            }
            if(msg.what==100){
                status.setText("标定中");
            }
            // 定时更新
            if(msg.what==101){
                //todo 低报逻辑
                if(dibao == 1){
                    dibaoflag++;
                    if(dibaoflag%3==0){
                        baojing.setBackgroundResource(R.drawable.bbhong2);
                    }else {
                        baojing.setBackgroundResource(R.drawable.bbhong3);
                        nongdu.setTextColor(getResources().getColor(R.color.hong3));
                    }
                    if(dibaoflag == 9000000){
                        dibaoflag = 0;
                    }
                }
                //todo 更新 低报高报
                updatebao();
                //todo 更新蓝牙
                if (blueStatusTime <= 0){
                    blueIma.setBackground(getResources().getDrawable(R.drawable.lanya));
                } else {
                    blueIma.setBackground(getResources().getDrawable(R.drawable.lanya2));
                }
            }

        }
    };
    public static boolean isStart = true;
    int index=0;
    View view;
    int s=0;
    EditText shebeixinxi,shebeimc;
    String yijianbiaoding="no";
    TextView textdibao,textgaobao,textbiaoding,danwei,status;
    Switch zhishidengSwitch;
    int blueStatus = 0; //未连接
    int blueStatusTime = 1000;
    ImageView blueIma;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_device_control3, container, false);

        return view;
    }

    public void rootinit() {
        sinit();
        createInit();
        updateDataByBuleTooth();
    }


    private void sinit() {
        textdibao = view.findViewById(R.id.textView42);
        textgaobao = view.findViewById(R.id.textView43);
        textbiaoding = view.findViewById(R.id.button10);
        danwei = view.findViewById(R.id.textView40);
        blueIma = view.findViewById(R.id.imageView10);
        textdibao.setText("低报:"+getpre("dibao")+getpre("kailudanwei"));
        textgaobao.setText("高报:"+getpre("gaobao")+getpre("kailudanwei"));
        zhishidengSwitch = view.findViewById(R.id.switch2);

        zhishidengSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    bluesenddata.add(new send(1,Blue0x2801));
                    bluesenddata.add(new send(1,readConcentrationAgreement));
                    toastShow("打开");
                }else {
                    bluesenddata.add(new send(1,Blue0x2800));
                    bluesenddata.add(new send(1,readConcentrationAgreement));
                    toastShow("关闭");
                }
            }
        });

        //todo 一键标定
        textbiaoding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tips_biaoding();
            }
        });
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void createInit() {
        isStart = false;
        Log.e("aaxianchen","--------------------重启");
        if (devices!=null){
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
        //SendData();
        bluesenddata.clear();
        bluesenddata.add(new send(0,readConcentrationAgreement));
        bluesenddata.add(new send(0,readConcentrationAgreement2));
        //bluesenddata.add(new send(0,readConcentrationAgreement3));
        bluesenddata.add(new send(0,readConcentrationAgreement3));

        isStart = true;
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
                        //判断是否处于结束、暂停
                        if(!isStart)
                            continue;

                        blueStatusTime -= 120;
                        //添加 执行的数据
                        addList();
                        Log.e("aaxianchen","执行中");
                        //判断是否是暂停
                        //TODO 暂停
                        Log.e("aaxianchen","绘图");

                        //发送读取标定请求 5分钟 3.6s读取一次
                        if (startBiaoding == 1 && indexBiaoding%30 == 0) {
                            bluesenddata.add(new send(1,getBiaogeng));
                        }
                        indexBiaoding++;
                        if (indexBiaoding > 9999999) {
                            indexBiaoding = 0;
                        }

                        dataCheckHandler.sendEmptyMessage(101);
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
                Log.e("aazzaa","sned1111111111111");
                SendData(bluesenddata.get(index).getData());
                bluesenddata.remove(index);

            }
            index++;
        }
    }

    private void init() {
        nongdu = (TextView) view.findViewById(R.id.textView39);
        baojing = view.findViewById(R.id.baojing);
        wd = (TextView) view.findViewById(R.id.textView35);
        gq = (TextView) view.findViewById(R.id.text_gq);
        status = (TextView) view.findViewById(R.id.textView41);

        textlianjie = (TextView) view.findViewById(R.id.textlianjie);
        textlianjie.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "重新连接...", Toast.LENGTH_SHORT).show();
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
                dataCheckHandler.sendEmptyMessage(99);
                blueStatusTime = 1000;
                close(closeblueview);
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

    @Override
    public void onDestroy() {
        BusManager.getBus().unregister(this);
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
            mOutputInfo.append(HexUtil.encodeHexStr(event.getData())).append("--");
           // Toast.makeText(getActivity(), "\n"+mOutputInfo.toString(), Toast.LENGTH_LONG).show();
            String a[]=mOutputInfo.toString().split("--");
            adata+=a[a.length-1]+'\n';
           // Toast.makeText(getActivity(), ""+adata, Toast.LENGTH_SHORT).show();
            Log.e("aaas11223",a[a.length-1]+'\n');
            returnAgreement = jiexi2(a[a.length-1]);
            allreturnAgreement = returnAgreement;
            blueStatus = 1;
            blueStatusTime = 1000;
            if(isStart){
                setData();
            }

        }
    }

    private void setData() {
        //浓度命令
        if(returnAgreement.getCaozuo().equals("5")){
            //浓度信息正确
            if(returnAgreement.getMsg().equals("1") && returnAgreement.getData1()!=null){
                Log.e("ajiexi","设置浓度");

                if (getpre("kailudanwei").equals("PPM.m")) {
                    nongdu.setText(returnAgreement.getData1()+".00");
                    danwei.setText("PPM.m");
                } else if (getpre("kailudanwei").equals("LEL.m")) {
                    float a = Float.valueOf(returnAgreement.getData1())/500;
                    nongdu.setText(String.valueOf((float)(Math.round(a*100))/100));
                    danwei.setText("LEL.m");
                }else {
                    float a = Float.valueOf(returnAgreement.getData1())/10000;
                    nongdu.setText(String.valueOf((float)(Math.round(a*100))/100));
                    danwei.setText("VOL.m");
                }
                //todo 低报 闪烁
                if(Integer.valueOf(returnAgreement.getData1())>Integer.valueOf(getpre("dibao"))
                        &&Integer.valueOf(returnAgreement.getData1())<Integer.valueOf(getpre("gaobao"))){
                   // toastShow("低报模式");
                    Log.e(TAG,"一级浓度超标");
                    dibao = 1;
                //todo 高报 长亮
                }else if(Integer.valueOf(returnAgreement.getData1())>=Integer.valueOf(getpre("gaobao"))){
                 //   toastShow("高报模式");
                    Log.e(TAG,"二级浓度超标");
                    dibao = 0;
                    baojing.setBackgroundResource(R.drawable.bbhong3);
                    nongdu.setTextColor(getResources().getColor(R.color.hong3));
                }else {
                    baojing.setBackgroundResource(R.drawable.bbhong2);
                    nongdu.setTextColor(getResources().getColor(R.color.white));
                    dibao = 0;
                }
                //dataCheckHandler.sendEmptyMessage(100);
            }
            //光强
        }else if(returnAgreement.getCaozuo().equals("6")){
            if(returnAgreement.getData1().length()==1){
                gq.setText("光强:0"+returnAgreement.getData1());
            } else {
                gq.setText("光强:"+returnAgreement.getData1());
            }


            //温度
        }else if(returnAgreement.getCaozuo().equals("7")){
            wd.setText(returnAgreement.getData1()+"℃");

        }else if(returnAgreement.getCaozuo().equals("-1111")){
            //nd.setText("正确");
            //todo 一键标定
        }else if(returnAgreement.getCaozuo().equals("9")){
            yijianbiaoding = returnAgreement.getData1();
            //完成
            if (yijianbiaoding.contains("0")){
                if (startBiaodingDialog == 0){
                    status.setText("标定完成");
                }
                startBiaoding = 0;
                mTipSuccessOrFaild(true);
                //标定中
            }else if(yijianbiaoding.contains("1")){
                if (startBiaodingDialog == 0){
                    status.setText("标定中");
                }
                //失败
            }else if(yijianbiaoding.contains("2")){
                if (startBiaodingDialog == 0){
                    status.setText("标定失败");
                }
                startBiaoding = 0;
                mTipSuccessOrFaild(false);
            }

            Log.e("biaodings",yijianbiaoding);
        }

        Log.e("biaodings2",returnAgreement.getCaozuo());
    }


    public void updatebao(){
        if(getpre("kailudanwei").equals("PPM.m")){
            textdibao.setText("低报:"+getpre("dibao")+getpre("kailudanwei"));
            textgaobao.setText("高报:"+getpre("gaobao")+getpre("kailudanwei"));
        }else if(getpre("kailudanwei").equals("LEL.m")){
            textdibao.setText("低报:"+String.valueOf(Float.valueOf(getpre("dibao"))/500)+getpre("kailudanwei"));
            textgaobao.setText("高报:"+String.valueOf(Float.valueOf(getpre("gaobao"))/500)+getpre("kailudanwei"));
        }else {
            textdibao.setText("低报:"+String.valueOf(Float.valueOf(getpre("dibao"))/10000)+getpre("kailudanwei"));
            textgaobao.setText("高报:"+String.valueOf(Float.valueOf(getpre("gaobao"))/10000)+getpre("kailudanwei"));
        }

    }

    private void tips_biaoding() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_isbiaoding, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final Button yes = view.findViewById(R.id.button5);
        final Button no = view.findViewById(R.id.button9);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intenhandler.removeCallbacks(mRunable);
                bluesenddata.add(new send(1,Blue0x29));
                bluesenddata.add(new send(1,getBiaogeng));
                dataCheckHandler.sendEmptyMessage(100);
                startBiaoding = 1;
                startBiaodingDialog = 0;
                intenhandler.postDelayed(mRunable,300000);
                toastShow("开始标定");
                dialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCheckHandler.sendEmptyMessage(99);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void mTipSuccessOrFaild(boolean success) {
        if (startBiaodingDialog == 1) {
            return;
        }
        startBiaodingDialog = 1;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_chengg, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final TextView yes = view.findViewById(R.id.textView29);
        final Button no = view.findViewById(R.id.button5);

        if(success){
            yes.setText("标定成功");
        }else {
            yes.setText("标定失败");
        }
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCheckHandler.sendEmptyMessage(99);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
