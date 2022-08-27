package com.vise.bledemo.utils;

import android.os.Environment;
import android.os.Handler;
import android.view.View;

import com.vise.bledemo.chezai.bean.ShouDongQuZheng;
import com.vise.bledemo.model.IntentB_Q;
import com.vise.bledemo.model.send;

import java.util.ArrayList;
import java.util.List;

public class url {
    //todo SQLITE数据库名称
    public static String SQLITE_NAME="JIKONGDATA13822";

    //TODO app向遥测仪发出请求读取探头程序版本号0x20
    public static String Blue0x20 = "CCAC2000CC";

    //TODO app向遥测仪发出请求读取当前设备型号0x21
    public static String Blue0x21 = "CCAC2100CC";

    //TODO app向遥测仪发出请求读取当前设备ID 0x22
    public static String Blue0x22 = "CCAC2100CC";

    //TODO app向遥测仪发出请求读取当前显示控制版本 0x23
    public static String Blue0x23 = "CCAC2100CC";

    //TODO app向遥测仪发出请求读取当前探头编号 0x24
    public static String Blue0x24 = "CCAC2100CC";

    //todo 读取浓度/温度/光强
    public static String readConcentrationAgreement = "CCAC2500CCD1";
    public static String readConcentrationAgreement2 = "CCAC2600CCD2";
    public static String readConcentrationAgreement3 = "CCAC2700CCD3";


    public static String adata="";
    public static boolean isStart=true;

    //TODO app向遥测仪发出请求调整绿光开关0x28 00关
    public static String Blue0x2800 = "CCAC280100CCD5";

    //TODO app向遥测仪发出请求调整绿光开关0x28 01开
    public static String Blue0x2801 = "CCAC280101CCD6";

    //TODO app向遥测仪发出请求开始标峰0x29 01
    public static String Blue0x29 = "CCAC29020102CCDA";

    //todo app向遥测仪发出请求调整当前显示检测单位0x2A
    public static String sendPpm = "CCAC2A0100CCD7";
    public static String sendLel = "CCAC2A0101CCD8";
    public static String sendVol = "CCAC2A0102CCD9";

    //todo 读取标峰指令（一键标定）
    public static String getBiaogeng = "CCAC3600CCE2";

    //todo 数据更新获取解析多线程
    public static Handler intenhandler =new Handler();

    //todo 蓝牙发送数据
    public static List<send> bluesenddata = new ArrayList<>();

    //todo 从蓝牙 - > 手动取证的浓度-温度-光强
    public static IntentB_Q intentB_q =new IntentB_Q();

    public static returnAgreement allreturnAgreement =null;




    //todo Wifi modbustcp
    //todo 气体浓度
    public static String nongduurl= "00000000000601030B140001";
    //todo 收到 00 00 00 00 00 05 01 03 02 00 00


    //todo 获取数据 连续13个
    public static String getWifiData = "00000000000601030B14000C";

    //todo 获取数据 连续10个(车速 经纬度)
    public static String getWifiData2 = "00000000000601030D420006";


    //todo 控制
    public static String StopController = "00000000000601060D660000";
    public static String UpController = "00000000000601060D660001";
    public static String DownController = "00000000000601060D660002";
    public static String LeftController = "00000000000601060D660003";
    public static String RightController = "00000000000601060D660004";

    //todo 手动标峰
    public static String ShouDongBiaoFeng = "00000000000601060D66000C";
    //todo 手动标峰数值
    public static String bfm = "超时";

    //todo 绿光开启/关闭
    public static String lvguang = "00000000000601060D66000E";

    //todo 控制方向（角度执行）
    public static String KongZhiFangXiang = "00000000000601060D66000F";

    //todo 云台速度 +两位速度 0-64
    public static String SuduController = "00000000000601060D6700";


    public static View viewsa;
    public static List<Integer> delList = new ArrayList<>();
    public static List<ShouDongQuZheng> pdfList =new ArrayList<>();
    public static String imaurl;


    //todo 手持 -主页手动取证 0不 1 取证 且不重复弹出
    public static int ShoudongFlag = 0;

    //todo 手持 开始标定
    public static int startbiaoding = 0;

   // public static String localhost="123.56.230.79:8080";

   // public static String localhost="http://123.56.230.79:6799";
    //todo 服务器
    public static String localhost="http://39.106.144.53:6060";
    //  public static String localhost="http://192.168.24.231:6060";

    //todo 网络请求-保存地址 http://localhost:8080/user/SavePlace?latitude=33&longitude=23&placeTime=dsdsad&placeName=909

    public static String SavePlaceUrl = localhost+"/user/SavePlace?";

    //todo 上传记录 http://localhost:6799/user/JikongSave/SaveAlert?deviceName=321321&userName=321&type=32
    // &longitude=321&latitude=321&gpslongitude=5125&gpslatitude=21424&concentration=321&temperature=3213
    // &lightIntensity=4214&nospeed=5215&starttime=3123&endtime=321312&grade=5215&automatic=312321&uid=3213

    public static String SaveAlert = localhost+"/user/JikongSave/SaveAlert?";

    //turl thttp://localhost:6060/user/SaveEvent?starttime=1111&endtime=2222&statusId=3333&deviceName=3232&eventId=5555
    public static String SaveEventurl = localhost+"/user/SaveEvent?";

    public static String SAVE_PATH  = Environment.getExternalStorageDirectory().getPath()+"/ScreenCapture11/";
    public static String SAVE_PATH2  = Environment.getExternalStorageDirectory().getPath()+"/ScreenCapture22/";
    public static String SAVE_PATH3  = Environment.getExternalStorageDirectory().getPath()+"/ScreenCapture33/";
    public static String SAVE_PATH4  = Environment.getExternalStorageDirectory().getPath()+"/ScreenCapture44/";


    public static String alyimaPath = "https://lingjiedian.oss-cn-beijing.aliyuncs.com/";

    //阿里云图片
    public static String alyima1 = "create";
    public static String alyima2 = "create";



}
