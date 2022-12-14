package com.vise.bledemo.kailu.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.baseble.ViseBle;
import com.vise.baseble.callback.scan.IScanCallback;
import com.vise.baseble.callback.scan.ScanCallback;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.BluetoothLeDeviceStore;
import com.vise.bledemo.Base.BaseFragment;
import com.vise.bledemo.R;
import com.vise.bledemo.bluetooth.adapter.DeviceAdapter2;
import com.vise.bledemo.bluetooth.common.BluetoothDeviceManager;
import com.vise.bledemo.model.GetJingbao;
import com.vise.bledemo.model.Jingbao;
import com.vise.bledemo.sqlite.MyDatabase;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;
import com.vise.xsnow.event.BusManager;

import java.util.ArrayList;
import java.util.List;

import static com.vise.bledemo.shouchi.data.Data_all.devices;
import static com.vise.bledemo.sqlite.sqliteMethod.addData;
import static com.vise.bledemo.sqlite.sqliteMethod.getData;
import static com.vise.bledemo.utils.ViewBean.closeblueview;
import static com.vise.bledemo.utils.getTime.disposeTime;
import static com.vise.bledemo.utils.getTime.disposeTime2;
import static com.vise.bledemo.utils.getTime.getdidian;
import static com.vise.bledemo.utils.getTime.guangbaojin;
import static com.vise.bledemo.utils.url.SQLITE_NAME;
import static com.vise.bledemo.utils.url.intentB_q;
import static com.vise.bledemo.utils.url.isStart;
import static com.vise.utils.handler.HandlerUtil.runOnUiThread;


public class Sone extends BaseFragment {
    View view;
    private ImageView back;
    //sqilte sqlite
    private MyDatabase myDatebaseHelper;
    //todo ??????------------------------------------------------------------------
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;
    //?????? ??????---
    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;
    private ListView deviceLv;
    private TextView scanCountTv;
    int blueone = 0;
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

    private TextView nongdu;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    ImageView lanyaima;
    Button buttonlanya;
    LinearLayout lanyalist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        view = inflater.inflate(R.layout.kailusone1, container, false);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//?????????????????????
            //??????????????????,200????????????
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            Toast.makeText(getActivity(), "?????????????????????", Toast.LENGTH_LONG).show();
            //????????????
        }
        Log.e("aazzaa", "1");
        //sqlite?????????
        myDatebaseHelper = new MyDatabase(getActivity(), SQLITE_NAME, null, 1);

        //???????????????
        ViseLog.getLogConfig().configAllowLog(true);//??????????????????
        ViseLog.plant(new LogcatTree());//??????Logcat????????????
        BluetoothDeviceManager.getInstance().init(getActivity());
        BusManager.getBus().register(this);

        initBluetooth();
        startScan();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                verifyStoragePermissions(getActivity());
            }
        }, 3000);


        init();
        return view;
    }

    private void init() {
        nongdu = view.findViewById(R.id.textView39);
        lanyaima = view.findViewById(R.id.imageView10);
        buttonlanya = view.findViewById(R.id.button14);
        lanyalist = view.findViewById(R.id.lanyalist);

        buttonlanya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lanyalist.getVisibility() == View.GONE) {
                    lanyalist.setVisibility(View.VISIBLE);
                } else {
                    lanyalist.setVisibility(View.GONE);
                }
            }
        });

        closeblueview = lanyalist;

    }

    //todo
    private void tips() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.tips2, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
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
        nongdu.setText("?????? " + intentB_q.getNd());
        guangqiang.setText("?????? " + intentB_q.getWd());
        wendu.setText("?????? " + intentB_q.getGq());
        textdidian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuju.setVisibility(View.GONE);
                didian.setVisibility(View.VISIBLE);
                tijiao.setText("??????");
            }
        });
        if (!getpre("WEIHAOSHOUCHI").equals("1")) {
            xinghao.setText("????????????:" + getpre("WEIHAOSHOUCHI"));
        } else {
            xinghao.setText("???????????? ");
        }
        caozuoyuan.setText("????????? " + getpre("username"));
        tijiao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (tijiao.getText().equals("??????")) {
                    shuju.setVisibility(View.VISIBLE);
                    didian.setVisibility(View.GONE);
                    textdidian.setText("??????:" + getdidian);
                    tijiao.setText("??????");
                } else {
                    if (!textdidian.getText().toString().equals("??????:?????????")) {
//????????????
                        //String title;
                        //    String info;
                        //    String name;
                        //    String time;
                        //    int grade;//1 2 3??????
                        //    String locate;
                        //    String jingdu;
                        //    String weidu;
                        isStart = true;
                        dialog.dismiss();
                        Log.e("aaaaaasa", "1");
                        //todo ???????????? getpre("WEIHAOSHOUCHI")
                        Jingbao jingbao = new Jingbao(getpre("WEIHAOSHOUCHI"),
                                add_content.getText().toString(),
                                getpre("username"), disposeTime(), intentB_q.getDengji(),
                                textdidian.getText().toString(), "321", "123", intentB_q.getNd(),
                                intentB_q.getWd(), intentB_q.getGq(), disposeTime2(), getpre("WEIHAOSHOUCHI"));
                        //getpre("WEIHAOSHOUCHI")
                        Log.e("aaaaaasa", "2");
                        addData(myDatebaseHelper, jingbao);
                        toastShow("???????????????");
                        Log.e("aaaaaasa", "3");
                        List<GetJingbao> ls = new ArrayList<>();
                        ls = getData(myDatebaseHelper);
                        //todo ???????????????
                    } else {
                        textdidian.setTextColor(getResources().getColor(R.color.colorAccent));
                        toastShow("???????????????");
                    }

                }


            }
        });
//        getdizhiListview = (ListView) view.findViewById(R.id.alistview1);
//        initGeoCoderListView(getdizhiListview);
        dialog.show();

    }

    private void clean() {
        Log.e("axianchenSone", "??????clean");
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                guangbaojin = 0;
                Log.e("axianchenSone", "??????????????????");
            }
        }, 60000);
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

    //todo ??????-----------------------------------------------------------------------------------------------------------

    private void initBluetooth() {
        deviceLv = (ListView) view.findViewById(R.id.blueList);


        adapter = new DeviceAdapter2(getActivity());
        deviceLv.setAdapter(adapter);
        InitViewPager();
        deviceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //????????????????????????????????????????????????????????????
                BluetoothLeDevice device = (BluetoothLeDevice) adapter.getItem(position);
                if (device == null) {
                    return;
                } else {
                    devices = device;
                    //???-???
                    pre("kailushebeiname", device.getName());
                    pre("kailushebeinameold", device.getName());
                    InitViewPager();
                }
            }
        });
    }

    public void InitViewPager() {
        if (blueone == 0) {
            if (fragmentManager == null) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.rraa, new blueToothDataFragment2(), "scone3");
                fragmentTransaction.commit();
                blueone = 1;
                Log.e("blueone", "1");
            }
        } else if (blueone == 1) {
            blueToothDataFragment2 id = (blueToothDataFragment2) fragmentManager.findFragmentByTag("scone3");
            id.rootinit();
            Log.e("blueone", "2");
        } else {
            blueToothDataFragment2 id = (blueToothDataFragment2) fragmentManager.findFragmentByTag("scone3");
            id.createInit();
            Log.e("blueone", "3");
        }
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
        //scanCountTv.setText(getString(R.string.formatter_item_count, String.valueOf(count)));
    }

}
