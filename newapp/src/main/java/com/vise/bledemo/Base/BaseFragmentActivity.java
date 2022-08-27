package com.vise.bledemo.Base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.vise.bledemo.baiduMap.baiduMap.view.MainActivity2;
import com.vise.bledemo.baiduMap.utils.PrefStore;
import com.vise.bledemo.sqlite.MyDatabase;

import java.util.Map;

import static com.vise.bledemo.utils.url.SQLITE_NAME;

public class BaseFragmentActivity extends FragmentActivity {
    public MyDatabase myDatebaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            int ui = decorView.getSystemUiVisibility();
            ui |=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //设置状态栏中字体的颜色为白色
            decorView.setSystemUiVisibility(ui);
        }

        ToastUtil.init(getBaseContext());
        myDatebaseHelper = new MyDatabase(this, SQLITE_NAME, null, 1);
    }

    @SuppressWarnings("uncheked")
    protected <T extends View> T f(int resId) {
        return (T) findViewById(resId);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T f(View view,int resId) {
        return (T) view.findViewById(resId);
    }

    protected <T extends View> void back(T view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity2.class);
                startActivity(intent);
                System.exit(0);
                finish();
            }
        });
    }

    public <T> void intent(Class<T> cls){
        Intent intent = new Intent(getBaseContext(),cls);
        startActivity(intent);
    }

    public <T> void intentFinish(Class<T> cls){
        Intent intent = new Intent(getBaseContext(),cls);
        startActivity(intent);
        finish();
    }

    public <T> void intentExit(Class<T> cls){
        Intent intent = new Intent(getBaseContext(),cls);
        startActivity(intent);
        System.exit(0);
        finish();
    }

    //todo 获取屏幕高度调用此函数
    public static int getAndroiodScreenHeigh(WindowManager wm) {
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;       // 屏幕高度（像素）
        return height;
    }

    //todo 获取屏幕宽度调用此函数
    public static int getAndroiodScreenWidth(WindowManager wm) {
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        return width;
    }

    public void pre(Map<String,String> params){
        PrefStore pref = PrefStore.getInstance(getBaseContext());
        //键值对不为空，他的值也不为空
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pref.savePref(entry.getKey(), entry.getValue());
            }
        }
    }

    public void pre(String key,String value){
        PrefStore pref = PrefStore.getInstance(getBaseContext());
        pref.savePref(key, value);
    }

    public String getpre(String a){
        PrefStore pref = PrefStore.getInstance(getBaseContext());
        return pref.getPref(a,"1");
    }

    //Toast
    public void toastShow(String msg){
        ToastUtil.show(msg);
    }

    public static class ToastUtil {

        @SuppressLint("StaticFieldLeak")
        private static Context mContext;

        public static void init(Context context) {
            mContext = context.getApplicationContext();
        }

        public static void show(String content) {
            Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public void isBlue() {
        //获取蓝牙适配器
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean enabled = bluetoothAdapter.isEnabled();
        //判断如果为true就是开启状态
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
    }
}



