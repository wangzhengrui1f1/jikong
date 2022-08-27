package com.vise.bledemo.bluetooth.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.vise.baseble.ViseBle;
import com.vise.baseble.callback.scan.IScanCallback;
import com.vise.baseble.callback.scan.ScanCallback;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.BluetoothLeDeviceStore;
import com.vise.bledemo.R;
import com.vise.bledemo.bluetooth.adapter.DeviceAdapter;
import com.vise.bledemo.bluetooth.common.BluetoothDeviceManager;
import com.vise.bledemo.shouchi.activity.Home;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;
import com.vise.xsnow.event.BusManager;

import java.util.ArrayList;

import static com.vise.bledemo.shouchi.data.Data_all.devices;

/**
 * 设备扫描展示界面
 */
public class DeviceScanActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;

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
                        updateItemCount(adapter.getCount());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_scan);
        ViseLog.getLogConfig().configAllowLog(true);//配置日志信息
        ViseLog.plant(new LogcatTree());//添加Logcat打印信息
        BluetoothDeviceManager.getInstance().init(this);
        BusManager.getBus().register(this);
        init();
    }

    private void init() {
        deviceLv = (ListView) findViewById(android.R.id.list);
        scanCountTv = (TextView) findViewById(R.id.scan_device_count);

        adapter = new DeviceAdapter(this);
        deviceLv.setAdapter(adapter);

        deviceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //点击某个扫描到的设备进入设备详细信息界面
                BluetoothLeDevice device = (BluetoothLeDevice) adapter.getItem(position);
                if (device == null) return;
                devices=device;
                Intent intent = new Intent(DeviceScanActivity.this, Home.class);
                intent.putExtra(DeviceDetailActivity.EXTRA_DEVICE, device);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startScan();
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScan();
        invalidateOptionsMenu();
        bluetoothLeDeviceStore.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 菜单栏的显示
     *
     * @param menu 菜单
     * @return 返回是否拦截操作
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.scan, menu);
        if (periodScanCallback != null && !periodScanCallback.isScanning()) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(R.layout.actionbar_progress_indeterminate);
        }
        return true;
    }

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
        invalidateOptionsMenu();
    }

    /**
     * 停止扫描
     */
    private void stopScan() {
        ViseBle.getInstance().stopScan(periodScanCallback);
        invalidateOptionsMenu();
    }

    /**
     * 更新扫描到的设备个数
     *
     * @param count
     */
    private void updateItemCount(final int count) {
        scanCountTv.setText(getString(R.string.formatter_item_count, String.valueOf(count)));
    }

}
