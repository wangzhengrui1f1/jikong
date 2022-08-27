package com.vise.bledemo.sendPost;

import android.text.StaticLayout;
import android.util.Log;

import com.vise.bledemo.baiduMap.utils.PrefStore;
import com.vise.bledemo.chezai.bean.ShouDongQuZheng;
import com.vise.bledemo.utils.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import static com.vise.bledemo.sqlite.sqliteMethod.lessUid;
import static com.vise.bledemo.utils.url.SaveAlert;

public class SendPost {

    static String Staticresult = "create";
    //todo 上传记录 http://123.56.230.79:6799/user/JikongSave/SaveAlert?
    // deviceName=321321&userName=321&type=32&longitude=321&latitude=321&
    // gpslongitude=5125&gpslatitude=21424&concentration=321&temperature=3213&
    // lightIntensity=4214&nospeed=5215&starttime=3123&endtime=321312&grade=5215&automatic=312321&uid=3213
    public static String SendPost2222(ShouDongQuZheng mShouDongQuZheng){
        Staticresult = "create";
        final String resulea;
        final String uid = mShouDongQuZheng.getUid()+"AA";
        final int id = mShouDongQuZheng.getId();
        Log.e("sssendpost2","1111");
        Map<String,String> hs=new HashMap<>();
        //http://localhost:8080/user/saveShops?latitude=33&longitude=23&placeTime=dsdsad&placeName=909
//        hs.put("deviceName",getpre("SHEBEIWEIHAO"));
//        hs.put("userName", getpre("username"));
        hs.put("type", mShouDongQuZheng.getType());
        hs.put("longitude", mShouDongQuZheng.getJingdu());
        hs.put("latitude",mShouDongQuZheng.getWeidu());
        hs.put("gpslongitude", String.valueOf(mShouDongQuZheng.getJingdu()));
        hs.put("gpslatitude", String.valueOf(mShouDongQuZheng.getWeidu()));
        hs.put("concentration", mShouDongQuZheng.getNongdu());
        hs.put("temperature", mShouDongQuZheng.getWendu());
        hs.put("lightIntensity", mShouDongQuZheng.getGuangqiang());
        hs.put("nospeed", mShouDongQuZheng.getChesu());
        hs.put("starttime", mShouDongQuZheng.getStarttime());
        hs.put("endtime", mShouDongQuZheng.getEndtime());
        hs.put("grade", mShouDongQuZheng.getGrade());
        hs.put("automatic", mShouDongQuZheng.getFlag());
        hs.put("uid", mShouDongQuZheng.getUid());
        OkHttpUtils okHttp = OkHttpUtils.getInstance();
        okHttp.sendDatafForClicent2(SaveAlert,hs, new OkHttpUtils.FuncJsonString() {
            @Override
            public void onResponse(String result) {
                Staticresult = result;
            }
        });
        return Staticresult;
    }

}
