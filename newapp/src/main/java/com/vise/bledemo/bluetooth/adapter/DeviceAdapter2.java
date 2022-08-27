package com.vise.bledemo.bluetooth.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.utils.HexUtil;
import com.vise.bledemo.R;
import com.vise.bledemo.baiduMap.utils.PrefStore;
import com.vise.xsnow.ui.adapter.helper.HelperAdapter;
import com.vise.xsnow.ui.adapter.helper.HelperViewHolder;

public class DeviceAdapter2 extends HelperAdapter<BluetoothLeDevice> {

    public DeviceAdapter2(Context context) {
        super(context, R.layout.item_scan_layout_home2);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void HelpConvert(HelperViewHolder viewHolder, int position, BluetoothLeDevice bluetoothLeDevice) {
        TextView deviceNameTv = viewHolder.getView(R.id.device_name);
        TextView deviceMacTv = viewHolder.getView(R.id.device_mac);
        TextView deviceRssiTv = viewHolder.getView(R.id.device_rssi);
        TextView deviceScanRecordTv = viewHolder.getView(R.id.device_scanRecord);
        LinearLayout ssar = viewHolder.getView(R.id.ssar);
        if (bluetoothLeDevice != null && bluetoothLeDevice.getDevice() != null) {
            String deviceName = bluetoothLeDevice.getDevice().getName();
            if (deviceName != null && !deviceName.isEmpty()) {
                PrefStore prefStore = new PrefStore(mContext);
                if(prefStore.getPref("kailushebeiname","1").equals(deviceName)){
                    deviceNameTv.setText(deviceName+" (上次连接)");
                    ssar.setBackgroundResource(R.drawable.bbhuang);
                }else {
                    deviceNameTv.setText(deviceName);
                    ssar.setBackgroundResource(R.drawable.bbhui);
                }

            } else {
                deviceNameTv.setText(mContext.getString(R.string.unknown_device));
                ssar.setBackgroundResource(R.drawable.bbhui);
            }
            deviceMacTv.setText(bluetoothLeDevice.getDevice().getAddress());
            deviceRssiTv.setText(mContext.getString(R.string.label_rssi) + bluetoothLeDevice.getRssi() + "dB");
            deviceScanRecordTv.setText(mContext.getString(R.string.header_scan_record) + ":"
                    + HexUtil.encodeHexStr(bluetoothLeDevice.getScanRecord()));
        }
    }
}
