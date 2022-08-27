package com.vise.bledemo.baiduMap.baiduMap.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.vise.bledemo.R;
import com.vise.bledemo.baiduMap.baiduMap.adapter.lixianListAdapter;

import java.util.ArrayList;


public class lixianList extends AppCompatActivity implements MKOfflineMapListener {
    MKOfflineMap mOffline;
    ListView listView;
    TextView xiazai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lixianlist);
        init();
    }

    private void init() {
        //TODO 设置状态栏透明
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            int ui = decorView.getSystemUiVisibility();
            ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //设置状态栏中字体的颜色为黑色
            decorView.setSystemUiVisibility(ui);
        }
//        ActionBar actionBar=getSupportActionBar();
//        actionBar.hide();
        listView = (ListView) findViewById(R.id.lixianListview);
        xiazai = (TextView) findViewById(R.id.xiazai);

        xiazai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mOffline = new MKOfflineMap();
        mOffline.init(this);
        final ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo();
        if(localMapList!=null){
            Toast.makeText(this, "已下载城市地图数量：" + localMapList.size(), Toast.LENGTH_SHORT).show();
            lixianListAdapter adapter = new lixianListAdapter(this,localMapList){
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    final View view = super.getView(position, convertView, parent);
                    final TextView ss4 = (TextView) view.findViewById(R.id.ss4);
                    ss4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOffline.remove(localMapList.get(position).cityID);
                            Toast.makeText(lixianList.this, localMapList.get(position).cityName+"删除成功", Toast.LENGTH_SHORT).show();
                            init();
                        }
                    });

                    return view;
                }
            };
            listView.setAdapter(adapter);
        }else {
            Toast.makeText(this, "已下载城市地图数量：0" , Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onGetOfflineMapState(int i, int i1) {

    }


}