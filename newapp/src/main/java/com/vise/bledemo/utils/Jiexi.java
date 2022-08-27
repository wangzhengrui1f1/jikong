package com.vise.bledemo.utils;

import android.util.Log;

import com.vise.bledemo.chezai.bean.ShouDongQuZheng;

import java.util.ArrayList;
import java.util.List;

import static com.vise.bledemo.utils.Hex2Float.bytes2Float;
import static com.vise.bledemo.utils.Hex2Float.bytes2float;
import static com.vise.bledemo.utils.LongitudelatitudeUtil.GCJ02_To_BD09;
import static com.vise.bledemo.utils.LongitudelatitudeUtil.WGS84_To_GCJ02;

public class Jiexi {

    public static returnAgreement jiexi(String msg) {
        char ss[] = msg.toCharArray();
        //返回类型 // 前导
        if (ss[0] == 'C' && ss[1] == 'C' && ss[2] == 'A' && ss[3] == 'B') {
            Log.e("ajiexi", "" + ss[4] + ss[5]);
            Log.e("ajiexi", "aa" + Integer.valueOf(ss[4] + ss[5]));
            //根据命令判断类型
            switch (ss[4] + ss[5]) {
                case 00://探头程序版本号0x00
                    Log.e("ajiexi", "00");
                    int datax10_yingjian = 0;
                    int datax10_ruanjian = 0;
                    //需要校验的数据 data1+前导  校验值 =前导+命令+数据长度+数据
                    int data1 = Integer.parseInt("CCAB", 16);
                    //需要校验的数据 data1+命令
                    data1 += 0;
                    //数据长度 8位 8-23为数据
                    if (ss[6] == '0' && ss[7] == '8') {
                        //data1+数据长度
                        data1 += Integer.parseInt(String.valueOf(ss[6] + ss[7]), 16);
                        //硬件版本号 8-15硬件版本号
                        String datax16_yingjian = null;
                        for (int i = 0; i < 8; i++) {
                            datax16_yingjian += ss[8 + i];
                        }
                        //软件版本号 16-23软件版本号
                        String datax16_ruanjian = null;
                        for (int i = 0; i < 8; i++) {
                            datax16_yingjian += ss[16 + i];
                        }
                        //数据转化 16进制 - 10进制
                        datax10_yingjian = Integer.parseInt(datax16_yingjian, 16);
                        datax10_ruanjian = Integer.parseInt(datax16_ruanjian, 16);
                        //需要校验的数据 data1+数据
                        data1 += datax10_yingjian;
                        data1 += datax10_ruanjian;
                    }
                    Log.e("ajiexi", "丢弃数据" + data1 + "--" + Integer.parseInt(String.valueOf(ss[24] + ss[25] + ss[26] + ss[27]), 16));
                    //校验和正确-有效 / 丢弃数据
                    if (data1 - 1035 == Integer.parseInt(String.valueOf(ss[24] + ss[25] + ss[26] + ss[27]), 16)) {
                        Log.e("ajiexi", "校验和正确");
                        //  return new returnAgreement(00, String.valueOf(datax10_yingjian), String.valueOf(datax10_ruanjian));
                    }
                    Log.e("ajiexi", "丢弃数据" + data1 + "--" + Integer.parseInt(String.valueOf(ss[24] + ss[25] + ss[26] + ss[27]), 16));
                    break;
                case 01://当前设备型号0x01
                    break;

                case 02://设备ID 0x02
                    break;

                case 03://当前显示控制版本 0x03
                    break;
                default:
                    break;
            }

        }
        Log.e("ajiexi", "丢弃数据");
        return null;
    }


    public static returnAgreement jiexi2(String msg) {
        Log.e("ajiexidata", msg);
        returnAgreement re = new returnAgreement();
        char ss[] = msg.toCharArray();
        //判断数据正确性

        //数据长度
        String returnlength = "";
        int endlength;
        //数据
        String data1 = "";
        String data2 = "";
        String data3 = "";
        String data4 = "";
        re.setMsg("-1111");
        re.setData1("-1111");
        re.setCaozuo("-1111");
        re.setData2("-1111");
        re.setData3("-1111");
        re.setData4("-1111");
        if (msg.equals("0") || msg.equals("00")) {
            return re;
        }
        int jisuan = Integer.parseInt(String.valueOf("CCAB"), 16);
        int jiaoyanzhi = Integer.parseInt(String.valueOf("" + ss[ss.length - 4] + ss[ss.length - 3] + ss[ss.length - 2] + ss[ss.length - 1]), 16);
        Log.e("ajiexi", "原-校验值:" + String.valueOf(jiaoyanzhi));
        //返回类型 // 前导 CC AB 05 04 00 00 03 E8 CD 9F
        if ((ss[0] == 'C' && ss[1] == 'C' && ss[2] == 'A' && ss[3] == 'B') ||
                (ss[0] == 'c' && ss[1] == 'c' && ss[2] == 'a' && ss[3] == 'b')) {
            //首先判断是否是正确数据
            //确认返回命令类型
            re.setCaozuo(String.valueOf(Integer.parseInt(String.valueOf("" + ss[4] + ss[5]), 16)));
            Log.e("ajiexi", "命令类型:" + re.getCaozuo());
            jisuan += Integer.parseInt(String.valueOf("" + ss[4] + ss[5]), 16);
            Log.e("ajiexi", "前导+命令:" + String.valueOf(jisuan));
            //确认 返回数据长度
            returnlength = "" + ss[6] + ss[7];
            returnlength = String.valueOf(Integer.parseInt("" + returnlength, 16));
            Log.e("ajiexi", "数据长度" + returnlength);
            Log.e("ajiexi", "jisuan长度" + jisuan);
            jisuan += Integer.valueOf(returnlength);
            Log.e("ajiexi", "前导+命令+数据长度:" + String.valueOf(jisuan));
            //取数据长度的数据  jiexi2("CC AB 05 08 00 00 03 E8 99 99 99 99 CD 9F");
            Log.e("ajiexi", "数据长度Integer" + Integer.valueOf(returnlength));
            // CC AB 05 01 00 CD 9F 1
            // CC AB 05 02 00 00 CD 9F 2
            // CC AB 05 04 00 00 00 00 CD 9F 4
            // CC AB 05 08 00 00 00 00 00 00 03 E8 CD 9F 8
            // 取校验值
            if (Integer.valueOf(returnlength) == 1) {
                jiaoyanzhi = Integer.parseInt(String.valueOf("" + ss[10] + ss[11] + ss[12] + ss[13]), 16);
            } else if (Integer.valueOf(returnlength) == 2) {
                jiaoyanzhi = Integer.parseInt(String.valueOf("" + ss[12] + ss[13] + ss[14] + ss[15]), 16);
            } else if (Integer.valueOf(returnlength) == 4) {
                //todo java.lang.RuntimeException: Could not dispatch event: class com.vise.bledemo.
                // bluetooth.event.NotifyDataEvent to subscriber com.vise.xsnow.event.inner.EventSubscriber@6e944a1: length=18; index=18
                jiaoyanzhi = Integer.parseInt(String.valueOf("" + ss[16] + ss[17] + ss[18] + ss[19]), 16);
            } else if (Integer.valueOf(returnlength) == 8) {
                jiaoyanzhi = Integer.parseInt(String.valueOf("" + ss[24] + ss[25] + ss[26] + ss[27]), 16);
            } else if (Integer.valueOf(returnlength) == 12) {
                jiaoyanzhi = Integer.parseInt(String.valueOf("" + ss[32] + ss[33] + ss[34] + ss[35]), 16);
            }
            Log.e("ajiexi", "校验值:" + jiaoyanzhi);
            //取数据 8-15
            int qulength = 0;
            if (Integer.valueOf(returnlength) <= 4) {
                qulength = 7 + Integer.valueOf(returnlength) * 2 + 1;
                for (int is = 8; is < qulength; is++) {
                    data1 += "" + ss[is];
                }
                //todo CC AB 05 04 00 00 02 E8 CD 9e       长度 0- 19
                //todo java.lang.RuntimeException: Could not dispatch event: class com.vise.bledemo.bluetooth.event.NotifyDataEvent
                // to subscriber com.vise.xsnow.event.inner.EventSubscriber@ca03abd: For input string: "808080f4"
                Log.e("data1aaaa", data1);
                re.setData1(String.valueOf(Long.parseLong(String.valueOf("" + data1), 16)));
                Log.e("ajiexi", "取的数据data1:" + re.getData1());
                String ls = "";
                int temp = 0;
                for (int is = 8; is < qulength; is++) {
                    temp++;
                    ls += "" + ss[is];
                    if (temp == 2) {
                        Log.e("ajiexi", "add:" + ls);
                        jisuan += Integer.parseInt(String.valueOf("" + ls), 16);
                        ls = "";
                        temp = 0;
                    }
                }
                if (jisuan == jiaoyanzhi) {
                    re.setMsg("1");
                    Log.e("ajiexi", "总和=校验码,数据正确:" + String.valueOf(jisuan));
                } else {
                    re.setMsg("-1");
                    Log.e("ajiexi", "数据错误:" + re.getMsg());
                }

                //todo 两个数据
            } else if (Integer.valueOf(returnlength) == 8) {
                Log.e("ajiexi", "两个数据");
                for (int is = 8; is < 16; is++) {
                    data1 += "" + ss[is];
                }
                re.setData1(String.valueOf(Integer.parseInt(String.valueOf("" + data1), 16)));
                Log.e("ajiexi", "取的数据data1:" + re.getData1());
                String ls = "";
                int temp = 0;
                for (int is = 8; is < 16; is++) {
                    temp++;
                    ls += "" + ss[is];
                    if (temp == 2) {
                        Log.e("ajiexi", "add:" + ls);
                        jisuan += Integer.parseInt(String.valueOf("" + ls), 16);
                        ls = "";
                        temp = 0;
                    }
                }
                for (int is = 16; is < 24; is++) {
                    data2 += "" + ss[is];
                }
                re.setData2(String.valueOf(Integer.parseInt(String.valueOf("" + data2), 16)));
                Log.e("ajiexi", "取的数据data2:" + re.getData2());
                ls = "";
                temp = 0;
                for (int is = 16; is < 24; is++) {
                    temp++;
                    ls += "" + ss[is];
                    if (temp == 2) {
                        Log.e("ajiexi", "add:" + ls);
                        jisuan += Integer.parseInt(String.valueOf("" + ls), 16);
                        ls = "";
                        temp = 0;
                    }
                }
                if (jisuan == jiaoyanzhi) {
                    re.setMsg("1");
                    Log.e("ajiexi", "总和=校验码,数据正确:" + String.valueOf(jisuan));
                } else {
                    re.setMsg("-1");
                    Log.e("ajiexi", "总和=:" + String.valueOf(jisuan));
                    Log.e("ajiexi", "数据错误:" + re.getMsg());
                }
            } else if (Integer.valueOf(returnlength) == 12) {
                Log.e("ajiexi", "三个数据");
                for (int is = 8; is < 16; is++) {
                    data1 += "" + ss[is];
                }
                String ls = "";
                int temp = 0;
                for (int is = 8; is < 16; is++) {
                    temp++;
                    ls += "" + ss[is];
                    if (temp == 2) {
                        Log.e("ajiexi", "add:" + ls);
                        jisuan += Integer.parseInt(String.valueOf("" + ls), 16);
                        ls = "";
                        temp = 0;
                    }
                }
                Log.e("ajiexi", "总和=" + String.valueOf(jisuan));
                re.setData1(String.valueOf(Integer.parseInt(String.valueOf("" + data1), 16)));
                Log.e("ajiexi", "取的数据data1:" + re.getData1());
                for (int is = 16; is < 24; is++) {
                    data2 += "" + ss[is];
                }
                ls = "";
                temp = 0;
                for (int is = 16; is < 24; is++) {
                    temp++;
                    ls += "" + ss[is];
                    if (temp == 2) {
                        Log.e("ajiexi", "add:" + ls);
                        jisuan += Integer.parseInt(String.valueOf("" + ls), 16);
                        ls = "";
                        temp = 0;
                    }
                }
                Log.e("ajiexi", "取的数据data2:" + data2);
                Log.e("ajiexi", "总和=" + String.valueOf(jisuan));
                re.setData2(String.valueOf(Integer.parseInt(String.valueOf("" + data2), 16)));
                Log.e("ajiexi", "取的数据data2:" + re.getData2());
                for (int is = 24; is < 32; is++) {
                    data3 += "" + ss[is];
                }
                re.setData3(String.valueOf(Integer.parseInt(String.valueOf("" + data3), 16)));
                Log.e("ajiexi", "取的数据data3:" + re.getData3());
                ls = "";
                temp = 0;
                for (int is = 24; is < 32; is++) {
                    temp++;
                    ls += "" + ss[is];
                    if (temp == 2) {
                        Log.e("ajiexi", "add:" + ls);
                        jisuan += Integer.parseInt(String.valueOf("" + ls), 16);
                        ls = "";
                        temp = 0;
                    }
                }
                if (jisuan == jiaoyanzhi) {
                    re.setMsg("1");
                    Log.e("ajiexi", "总和=校验码,数据正确:" + String.valueOf(jisuan));
                } else {
                    re.setMsg("-1");
                    Log.e("ajiexi", "总和=" + String.valueOf(jisuan));
                    Log.e("ajiexi", "数据错误:" + re.getMsg());
                }
            } else {

            }
        }

        Log.e("ajiexi", "------------------");
        return re;
    }


    //todo 解析wifi连续13数据
    public static List<String> jiexiwifi13(String msg) {
        List<String> dataList = new ArrayList<>();
        char ss[] = msg.toCharArray();
        //设备号正确 设备号 03 dsasdad 00000000001b010318000000000000000000000000000000000000000100010000
        if (ss[14] == '0' && ss[15] == '3' && ss[16] == '1') {
            Log.e("jeixiwifi13", "wifi13 开始解析");
            // 17 18为 数据有效长度 18 ,12个数据 12*4
            int index = 18;
            int flag = 0;
            int i = 0;
            String data = "";
            for (i = 0; i < 12; i++) {
                data = "";
                data = String.valueOf(Integer.parseInt(String.valueOf("" + ss[index] + ss[index + 1] + ss[index + 2] + ss[index + 3]), 16));
                index += 4;
                dataList.add(data);
            }

        }
        return dataList;
    }

    public static List<String> wifidata13 = new ArrayList<>();

    public static List<String> nowifidata13 = new ArrayList<>();
    public static List<String> wifidata10 = new ArrayList<>();
    public static List<String> nowifidata10 = new ArrayList<>();

    //手持
    public static ShouDongQuZheng ssmShouDongQuZheng = new ShouDongQuZheng();
    public static ShouDongQuZheng ssmShouDongQuZheng2 = new ShouDongQuZheng();

    //todo 解析wifi连续10数据
    //todo         9           天线 gis  经度26-41          纬度42-57         车速58-73              点式1 点式2
    //todo 0000000000 23010320 0000 0001 00000000 00000000 00000000 00000000 00000000 00000000 0000  0000
    //todo                    18-25    26-33    34-41
    //todo 00000000000F01030C 42F2FE18 421B649D 00000000
    //todo 00000000000f01030c 00000000 00000000 00000000
    public static List<String> jiexiwifi10(String msg) {
        List<String> dataList = new ArrayList<>();
        char ss[] = msg.toCharArray();
        Log.e("aazzzzza",String.valueOf(ss.length));
        //设备号正确 设备号 03
        if (ss[14] == '0' && ss[15] == '3' && ss[16] == '0' && ss.length>=42) {
            Log.e("jeixiwifi10", "wifi10 开始解析");
            String jingdu = ""+ss[18]+ss[19]+ss[20]+ss[21]+ss[22]+ss[23]+ss[24]+ss[25];
            String weidu = ""+ss[26]+ss[27]+ss[28]+ss[29]+ss[30]+ss[31]+ss[32]+ss[33];
            String chesu =""+ss[34]+ss[35]+ss[36]+ss[37]+ss[38]+ss[39]+ss[40]+ss[41];
            Log.e("jeixiwifi10--1 ", String.valueOf(IEEE754(weidu)));
            Log.e("jeixiwifi10--1 ", String.valueOf(IEEE754(jingdu)));
            double a[]=WGS84_To_GCJ02(IEEE754(jingdu),IEEE754(weidu));
            Log.e("jeixiwifi10--2 ", String.valueOf(a[1]));
            Log.e("jeixiwifi10--2 ", String.valueOf(a[0]));
            double b[]=GCJ02_To_BD09(a[0],a[1]);
            Log.e("jeixiwifi10--2 ", String.valueOf(b[1]));
            Log.e("jeixiwifi10--2 ", String.valueOf(a[0]));
            dataList.add(String.valueOf(b[1]));
            dataList.add(String.valueOf(b[0]));
            dataList.add(String.valueOf(IEEE754(chesu)));
            //42f2fe34
            Log.e("jeixiwifi10--size ", String.valueOf(dataList.toString()));
        }
        return dataList;
    }
    //todo Java 16进制字符串转IEEE754浮点数
    public static float IEEE754(String str) {
        int ieee754Int = Integer.parseInt(str, 16);
        float realValue = Float.intBitsToFloat(ieee754Int);
        System.out.println(realValue);
        return realValue;
    }

    public static String a1(){
        return "dsa";

    }

    public static String a2(){
        return "dsa";
    }

    public static String a3(){
        return "dsa";
    }


}
