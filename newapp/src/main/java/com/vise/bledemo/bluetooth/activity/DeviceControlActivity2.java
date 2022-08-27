package com.vise.bledemo.bluetooth.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.vise.baseble.ViseBle;
import com.vise.baseble.common.ConnectState;
import com.vise.baseble.common.PropertyType;
import com.vise.baseble.core.DeviceMirror;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.resolver.GattAttributeResolver;
import com.vise.baseble.utils.HexUtil;
import com.vise.bledemo.R;
import com.vise.bledemo.bluetooth.common.BluetoothDeviceManager;
import com.vise.bledemo.bluetooth.common.ToastUtil;
import com.vise.bledemo.bluetooth.event.CallbackDataEvent;
import com.vise.bledemo.bluetooth.event.ConnectEvent;
import com.vise.bledemo.bluetooth.event.NotifyDataEvent;
import com.vise.xsnow.cache.SpCache;
import com.vise.xsnow.event.BusManager;
import com.vise.xsnow.event.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备数据操作相关展示界面
 */
public class DeviceControlActivity2 extends AppCompatActivity {

    private static final String LIST_NAME = "NAME";
    private static final String LIST_UUID = "UUID";
    public static final String WRITE_CHARACTERISTI_UUID_KEY = "write_uuid_key";
    public static final String NOTIFY_CHARACTERISTIC_UUID_KEY = "notify_uuid_key";
    public static final String WRITE_DATA_KEY = "write_data_key";

    private SimpleExpandableListAdapter simpleExpandableListAdapter;
    private EditText mInput;
    private EditText mOutput;

    private SpCache mSpCache;
    //设备信息
    private BluetoothLeDevice mDevice;
    //输出数据展示
    private StringBuilder mOutputInfo = new StringBuilder();
    private List<BluetoothGattService> mGattServices = new ArrayList<>();
    //设备特征值集合
    private List<List<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<>();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control2);
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
                    invalidateOptionsMenu();
                }
            }
        },300);
    }

    private void init() {
        mInput = (EditText) findViewById(R.id.input);
        mOutput = (EditText) findViewById(R.id.output);

        mDevice = getIntent().getParcelableExtra(DeviceDetailActivity.EXTRA_DEVICE);

        mSpCache = new SpCache(this);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInput.getText() == null || mInput.getText().toString() == null) {
                    ToastUtil.showToast(DeviceControlActivity2.this, "Please input command!");
                    return;
                }
                if (!isHexData(mInput.getText().toString())) {
                    ToastUtil.showToast(DeviceControlActivity2.this, "Please input hex data command!");
                    return;
                }
                mSpCache.put(WRITE_DATA_KEY + mDevice.getAddress(), mInput.getText().toString());
                BluetoothDeviceManager.getInstance().write(mDevice, HexUtil.decodeHex(mInput.getText().toString().toCharArray()));
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Subscribe
    public void showConnectedDevice(ConnectEvent event) {
        if (event != null) {
            if (event.isSuccess()) {
                ToastUtil.showToast(DeviceControlActivity2.this, "Connect Success!");
                invalidateOptionsMenu();
                if (event.getDeviceMirror() != null && event.getDeviceMirror().getBluetoothGatt() != null) {
                    simpleExpandableListAdapter = displayGattServices(event.getDeviceMirror().getBluetoothGatt().getServices());
                }
                //add
                zidongadd();


            } else {
                if (event.isDisconnected()) {
                    ToastUtil.showToast(DeviceControlActivity2.this, "Disconnect!");
                } else {
                    ToastUtil.showToast(DeviceControlActivity2.this, "Connect Failure!");
                }
                invalidateOptionsMenu();
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
                    mSpCache.put(WRITE_CHARACTERISTI_UUID_KEY + mDevice.getAddress(), characteristic.getUuid().toString());
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_WRITE, service.getUuid(), characteristic.getUuid(), null);
                } else if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_READ, service.getUuid(), characteristic.getUuid(), null);
                    BluetoothDeviceManager.getInstance().read(mDevice);
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        mSpCache.put(NOTIFY_CHARACTERISTIC_UUID_KEY + mDevice.getAddress(), characteristic.getUuid().toString());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    }
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_NOTIFY, service.getUuid(), characteristic.getUuid(), null);
                    BluetoothDeviceManager.getInstance().registerNotify(mDevice, false);
                } else if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                    mSpCache.put(NOTIFY_CHARACTERISTIC_UUID_KEY + mDevice.getAddress(), characteristic.getUuid().toString());
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_INDICATE, service.getUuid(), characteristic.getUuid(), null);
                    BluetoothDeviceManager.getInstance().registerNotify(mDevice, true);
                }
            }
        },100);
        handler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void run() {
                //showGattServices();
                final BluetoothGattService service = mGattServices.get(0);
                final BluetoothGattCharacteristic characteristic = mGattCharacteristics.get(0).get(0);
                final int charaProp = characteristic.getProperties();
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                    mSpCache.put(WRITE_CHARACTERISTI_UUID_KEY + mDevice.getAddress(), characteristic.getUuid().toString());
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_WRITE, service.getUuid(), characteristic.getUuid(), null);
                } else if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_READ, service.getUuid(), characteristic.getUuid(), null);
                    BluetoothDeviceManager.getInstance().read(mDevice);
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        mSpCache.put(NOTIFY_CHARACTERISTIC_UUID_KEY + mDevice.getAddress(), characteristic.getUuid().toString());
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    }
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_NOTIFY, service.getUuid(), characteristic.getUuid(), null);
                    BluetoothDeviceManager.getInstance().registerNotify(mDevice, false);
                } else if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
                    mSpCache.put(NOTIFY_CHARACTERISTIC_UUID_KEY + mDevice.getAddress(), characteristic.getUuid().toString());
                    BluetoothDeviceManager.getInstance().bindChannel(mDevice, PropertyType.PROPERTY_INDICATE, service.getUuid(), characteristic.getUuid(), null);
                    BluetoothDeviceManager.getInstance().registerNotify(mDevice, true);
                }
            }
        },100);
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

    @Subscribe
    public void showDeviceNotifyData(NotifyDataEvent event) {
        if (event != null && event.getData() != null && event.getBluetoothLeDevice() != null
                && event.getBluetoothLeDevice().getAddress().equals(mDevice.getAddress())) {
            mOutputInfo.append(HexUtil.encodeHexStr(event.getData())).append("\n");
            mOutput.setText(mOutputInfo.toString());
        }
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
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

        final SimpleExpandableListAdapter gattServiceAdapter = new SimpleExpandableListAdapter(this, gattServiceData, android.R.layout
                .simple_expandable_list_item_2, new String[]{LIST_NAME, LIST_UUID}, new int[]{android.R.id.text1, android.R.id.text2},
                gattCharacteristicData, android.R.layout.simple_expandable_list_item_2, new String[]{LIST_NAME, LIST_UUID}, new
                int[]{android.R.id.text1, android.R.id.text2});
        return gattServiceAdapter;
    }

    private void showReadInfo(String uuid, byte[] dataArr) {
    }

    private void showDefaultInfo() {
        mInput.setText(mSpCache.get(WRITE_DATA_KEY + mDevice.getAddress(), ""));
        mOutput.setText("");
        mOutputInfo = new StringBuilder();
    }

    private void clearUI() {
        mInput.setText("");
        mOutput.setText("");
        mOutputInfo = new StringBuilder();
        simpleExpandableListAdapter = null;
        mSpCache.remove(WRITE_CHARACTERISTI_UUID_KEY + mDevice.getAddress());
        mSpCache.remove(NOTIFY_CHARACTERISTIC_UUID_KEY + mDevice.getAddress());
        mSpCache.remove(WRITE_DATA_KEY + mDevice.getAddress());
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

}
