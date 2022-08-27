package com.vise.bledemo.Base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.vise.bledemo.R;
import com.vise.bledemo.baiduMap.baiduMap.view.MainActivity2;
import com.vise.bledemo.baiduMap.utils.PrefStore;
import com.vise.bledemo.sqlite.MyDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import static com.vise.bledemo.utils.ViewBean.mAllBaiduMap;
import static com.vise.bledemo.utils.url.SQLITE_NAME;

public abstract class BaseFragment extends Fragment {
    View view;
    public MyDatabase myDatebaseHelper;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment2, container, false);
        myDatebaseHelper = new MyDatabase(getActivity(), SQLITE_NAME, null, 1);
        ToastUtil.init(getActivity());
        return view;
    }
    @SuppressWarnings("uncheked")
    protected <T extends View> T f(int resId) {
        return (T) view.findViewById(resId);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T f(View fview,int resId) {
        return (T) fview.findViewById(resId);
    }

    protected <T extends View> void back(T view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity2.class);
                startActivity(intent);
                System.exit(0);
                getActivity().finish();
            }
        });
    }


    public <T> void intent(Class<T> cls){
        Intent intent = new Intent(getActivity(),cls);
        startActivity(intent);
    }

    public <T> void intentFinish(Class<T> cls){
        Intent intent = new Intent(getActivity(),cls);
        startActivity(intent);
        getActivity().finish();
    }

    public <T> void intentExit(Class<T> cls){
        Intent intent = new Intent(getActivity(),cls);
        startActivity(intent);
        System.exit(0);
        getActivity().finish();
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
        PrefStore pref = PrefStore.getInstance(getActivity());
        //键值对不为空，他的值也不为空
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pref.savePref(entry.getKey(), entry.getValue());
            }
        }
    }

    public void pre(String key,String value){
        PrefStore pref = PrefStore.getInstance(getActivity());
        pref.savePref(key, value);
    }

    public String getpre(String a){
        PrefStore pref = PrefStore.getInstance(getActivity());
        return pref.getPref(a,"1");
    }

    //Toast
    public void toastShow(String msg){
        BaseFragmentActivity.ToastUtil.show(msg);
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

    public void close(View view){
        view.setVisibility(View.GONE);
    }


    public String getBaidujietu(final String sspath){
        // 指定区域截图
        Rect rect = new Rect(0, 0, 8000, 8000);// 左xy 右xy
        mAllBaiduMap.snapshotScope(rect, new BaiduMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                File file = new File(sspath);
                FileOutputStream out;
                try {
                    out = new FileOutputStream(file);
                    if (snapshot.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                        out.flush();
                        out.close();
                    }
                    Log.e("baidujietu",file.toString());
//                    Toast.makeText(getActivity(), "屏幕截图成功，图片存在: " + file.toString(),
//                            Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        return sspath;
    }
}



