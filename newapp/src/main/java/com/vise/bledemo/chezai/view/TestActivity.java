package com.vise.bledemo.chezai.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vise.bledemo.R;
import com.vise.bledemo.utils.OkHttpUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.vise.bledemo.utils.getTime.disposeTime;
import static com.vise.bledemo.utils.url.SavePlaceUrl;


public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendPost();
            }
        });

    }
    public void SendPost(){
        Map<String,String> hs=new HashMap<>();
        //http://localhost:8080/user/saveShops?latitude=33&longitude=23&placeTime=dsdsad&placeName=909
        hs.put("latitude", String.valueOf("3.3213"));
        hs.put("longitude", String.valueOf("3.3213"));
//        Log.e("sspost getLatitude ", String.valueOf(sendlocation.getLatitude()));
//        Log.e("sspost getLongitude ", String.valueOf(sendlocation.getLongitude()));
        hs.put("placeTime", disposeTime());
        hs.put("placeName", "111");
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SavePlaceUrl,hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                if(result.contains("成功")){

                }else {

                }
            }
        });
    }

}