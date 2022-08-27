package com.vise.bledemo.chezai.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.vise.bledemo.Base.BaseFragment;
import com.vise.bledemo.R;
import com.vise.bledemo.baiduMap.baiduMap.view.WelcomeActivity;
import com.vise.bledemo.chezai.bean.ShouDongQuZheng;
import com.vise.bledemo.tcp.TaskCenter;
import com.vise.bledemo.utils.OkHttpUtils;

import java.util.HashMap;
import java.util.Map;

import static com.vise.bledemo.chezai.fragment.SoneFragment1.sendMessage;
import static com.vise.bledemo.utils.url.SaveAlert;
import static com.vise.bledemo.utils.url.ShouDongBiaoFeng;
import static com.vise.bledemo.utils.url.bfm;
import static com.vise.bledemo.utils.url.intenhandler;
import static com.vise.bledemo.utils.url.lvguang;


public class xinxiFragment extends BaseFragment {
    View view;
    private ImageView back;
    private EditText e1,e2,e3,e4,e5,e6,e7,e8,text_weihao;
    private TextView xinghao,shebeixinxi,aat1,aat2,aat3;
    private Button save,b1,b2,b3,biaoding,huifu;
    private EditText b4,b5,b6;
    Switch switch1;
    int saveDw;
    //todo 对比视频信息是否进行了修改
    String spIP,spYanma,spWangGuan,spZhanghao,spMima;
    // 16进制数字：大小写不影响
    private final static char[] HEXDIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chezaifragment2, container, false);
        init();

        initleft();

        initduibi();
        return view;
    }


    private void initduibi() {
        spIP = getpre("e1");
        spYanma = getpre("e2");
        spWangGuan = getpre("e3");
        spZhanghao = getpre("e4");
        spMima = getpre("e5");
    }

    private void duibi() {
        if(spIP.equals(getpre("e1"))&&spYanma.equals(getpre("e2"))&&spWangGuan.equals(getpre("e3"))&&spZhanghao.equals(getpre("e4"))
                &&spMima.equals(getpre("e5"))){
        }else {
            FragmentManager fragmentManager = getFragmentManager();
            final shexiangtouFragment id = (shexiangtouFragment) fragmentManager.findFragmentByTag("shipin");
            id.Zhuce();
            id.Bofang();
            initduibi();
        }
    }

    //TODO ppm/500=LEL ppm/10000=VOL
    //TODO TESTTYPE_CHEZAI 测试单位-车载
    //TODO GAOJINGZHI1_CHEZAI 告警值一级-车载
    //TODO GAOJINGZHI2_CHEZAI 告警值二级-车载
    //TODO GAOJINGZHI3_CHEZAI 告警其他-车载  pre("GAOJINGZHI3_CHEZAI",s.toString());
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initleft() {
        b1 = f(view,R.id.b1);
        b2 = f(view,R.id.b2);
        b3 = f(view,R.id.b3);
        b4 = f(view,R.id.b4);
        b5 = f(view,R.id.b5);
        b6 = f(view,R.id.b6);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDw = 0;
                b1.setBackgroundResource(R.drawable.bb01);
                b2.setBackgroundResource(R.drawable.bbhui);
                b3.setBackgroundResource(R.drawable.bbhui);
                b4.setText(getpre("GAOJINGZHI1_CHEZAI"));
                b5.setText(getpre("GAOJINGZHI2_CHEZAI"));
                b6.setText(getpre("GAOJINGZHI3_CHEZAI"));
                b1.setTextColor(getResources().getColor(R.color.huangse2));
                b2.setTextColor(getResources().getColor(R.color.black));
                b3.setTextColor(getResources().getColor(R.color.black));
                aat1.setText("ppm.m");
                aat2.setText("ppm.m");
                aat3.setText("ppm.m");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDw = 1;
                //pre("TESTTYPE_CHEZAI","VOL.M");
                b2.setBackgroundResource(R.drawable.bb01);
                b1.setBackgroundResource(R.drawable.bbhui);
                b3.setBackgroundResource(R.drawable.bbhui);
//                b4.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI1_CHEZAI"))/500));
//                b5.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI2_CHEZAI"))/500));
//                b6.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI3_CHEZAI"))/500));
                b1.setTextColor(getResources().getColor(R.color.black));
                b2.setTextColor(getResources().getColor(R.color.huangse2));
                b3.setTextColor(getResources().getColor(R.color.black));
//                aat1.setText("vol.m");
//                aat2.setText("vol.m");
//                aat3.setText("vol.m");
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDw = 2;
                //pre("TESTTYPE_CHEZAI","LEL.M");
                b3.setBackgroundResource(R.drawable.bb01);
                b2.setBackgroundResource(R.drawable.bbhui);
                b1.setBackgroundResource(R.drawable.bbhui);
//                b4.setText(String.valueOf(Float.valueOf(getpre("GAOJINGZHI1_CHEZAI"))/10000));
//                b5.setText(String.valueOf(Float.valueOf(getpre("GAOJINGZHI2_CHEZAI"))/10000));
//                b6.setText(String.valueOf(Float.valueOf(getpre("GAOJINGZHI3_CHEZAI"))/10000));
                b1.setTextColor(getResources().getColor(R.color.black));
                b2.setTextColor(getResources().getColor(R.color.black));
                b3.setTextColor(getResources().getColor(R.color.huangse2));
//                aat1.setText("lel.m");
//                aat2.setText("lel.m");
//                aat3.setText("lel.m");
            }
        });

        if(getpre("TESTTYPE_CHEZAI").equals("PPM.M")){
            saveDw = 0;
            b1.setBackgroundResource(R.drawable.bb01);
            b2.setBackgroundResource(R.drawable.bbhui);
            b3.setBackgroundResource(R.drawable.bbhui);
            b1.setTextColor(getResources().getColor(R.color.huangse2));
            b2.setTextColor(getResources().getColor(R.color.black));
            b3.setTextColor(getResources().getColor(R.color.black));
            //获取告警值
            b4.setText(getpre("GAOJINGZHI1_CHEZAI"));
            b5.setText(getpre("GAOJINGZHI2_CHEZAI"));
            b6.setText(getpre("GAOJINGZHI3_CHEZAI"));
            aat1.setText("ppm.m");
            aat2.setText("ppm.m");
            aat3.setText("ppm.m");
        }else if(getpre("TESTTYPE_CHEZAI").equals("VOL.M")){
            saveDw = 1;
            b2.setBackgroundResource(R.drawable.bb01);
            b1.setBackgroundResource(R.drawable.bbhui);
            b3.setBackgroundResource(R.drawable.bbhui);
            b2.setTextColor(getResources().getColor(R.color.huangse2));
            b1.setTextColor(getResources().getColor(R.color.black));
            b3.setTextColor(getResources().getColor(R.color.black));
            //获取告警值
            b4.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI1_CHEZAI"))/500));
            b5.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI2_CHEZAI"))/500));
            b6.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI3_CHEZAI"))/500));
            aat1.setText("vol.m");
            aat2.setText("vol.m");
            aat3.setText("vol.m");
        }else if(getpre("TESTTYPE_CHEZAI").equals("LEL.M")){
            saveDw = 2;
            b3.setTextColor(getResources().getColor(R.color.huangse2));
            b2.setTextColor(getResources().getColor(R.color.black));
            b1.setTextColor(getResources().getColor(R.color.black));
            b3.setBackgroundResource(R.drawable.bb01);
            b2.setBackgroundResource(R.drawable.bbhui);
            b1.setBackgroundResource(R.drawable.bbhui);
            b4.setText(String.valueOf(Float.valueOf(getpre("GAOJINGZHI1_CHEZAI"))/10000));
            b5.setText(String.valueOf(Float.valueOf(getpre("GAOJINGZHI2_CHEZAI"))/10000));
            b6.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI3_CHEZAI"))/10000));
            aat1.setText("lel.m");
            aat2.setText("lel.m");
            aat3.setText("lel.m");
        //首次
        }else {
            saveDw = 0;
            pre("TESTTYPE_CHEZAI","PPM.M");
            pre("GAOJINGZHI1_CHEZAI","15000");
            pre("GAOJINGZHI2_CHEZAI","25000");
            pre("GAOJINGZHI3_CHEZAI","30000");
            b1.setBackgroundResource(R.drawable.bb01);
            b2.setBackgroundResource(R.drawable.bbhui);
            b3.setBackgroundResource(R.drawable.bbhui);
            //获取告警值
            b4.setText(getpre("GAOJINGZHI1_CHEZAI"));
            b5.setText(getpre("GAOJINGZHI2_CHEZAI"));
            b6.setText(getpre("GAOJINGZHI3_CHEZAI"));
        }
        if(!getpre("GAOJINGZHI3_CHEZAI").equals("1")){
           // text_qita.setText(getpre("GAOJINGZHI3_CHEZAI"));
        }

        if(!getpre("SHEBEIWEIHAO").equals("1")){
            text_weihao.setText(getpre("SHEBEIWEIHAO"));
           // xinghao.setText("设备型号:"+getpre("SHEBEIWEIHAO"));
        }

//        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if(isChecked){
//                    switch1.setText("On");
//                    sendMessage(lvguang);
//                }else {
//                    switch1.setText("Off");
//                    sendMessage(lvguang);
//                }
//
//            }
//        });
    }

    private void init() {
        back = (ImageView) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        e1 = f(view,R.id.e1);
        e2 = f(view,R.id.e2);
        e3 = f(view,R.id.e3);
        e4 = f(view,R.id.e4);
        e5 = f(view,R.id.e5);
        e6 = f(view,R.id.e6);
        e7 = f(view,R.id.e7);
        e8 = f(view,R.id.e8);
        huifu = f(view,R.id.button6);
        aat1 = f(view,R.id.aat1);
        aat2 = f(view,R.id.aat2);
        aat3 = f(view,R.id.aat3);
        text_weihao = f(view,R.id.text_weihao);
        xinghao = f(view,R.id.xinghao);
        save = f(view,R.id.save);
        shebeixinxi = f(view,R.id.shebeixinxi);
        if(!getpre("e1").equals("1")){
            e1.setText(getpre("e1"));
            e2.setText(getpre("e2"));
            e3.setText(getpre("e3"));
            e4.setText(getpre("e4"));
            e5.setText(getpre("e5"));
            e6.setText(getpre("e6"));
            e7.setText(getpre("e7"));
            e8.setText(getpre("e8"));
        }

        shebeixinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipsInfo();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pre("e1",e1.getText().toString());
                pre("e2",e2.getText().toString());
                pre("e3",e3.getText().toString());
                pre("e4",e4.getText().toString());
                pre("e5",e5.getText().toString());
                pre("e6",e6.getText().toString());
                pre("e7",e7.getText().toString());
                pre("e8",e8.getText().toString());
                Double data1 = Double.valueOf(String.format("%.0f",Double.valueOf(b4.getText().toString())));
                Double data2 = Double.valueOf(String.format("%.0f",Double.valueOf(b5.getText().toString())));
                Double data3 = Double.valueOf(String.format("%.0f",Double.valueOf(b6.getText().toString())));
                if(data1>=0 && data2>=0 && data3>=0 && data1<data2 && data2<data3){
                    if(getpre("TESTTYPE_CHEZAI").equals("PPM.M")){
                        //报警值
                        pre("GAOJINGZHI1_CHEZAI",b4.getText().toString());
                        pre("GAOJINGZHI2_CHEZAI",b5.getText().toString());
                        pre("GAOJINGZHI3_CHEZAI",b6.getText().toString());
                    }else if(getpre("TESTTYPE_CHEZAI").equals("VOL.M")){
                        data1 *= 500;
                        data2 *= 500;
                        data3 *= 500;
                        //报警值
                        pre("GAOJINGZHI1_CHEZAI",String.valueOf(data1.intValue()));
                        pre("GAOJINGZHI2_CHEZAI",String.valueOf(data2.intValue()));
                        pre("GAOJINGZHI3_CHEZAI",String.valueOf(data3.intValue()));
                    }else if(getpre("TESTTYPE_CHEZAI").equals("LEL.M")){
                        data1 *= 10000;
                        data2 *= 10000;
                        data3 *= 10000;
                        //报警值
                        pre("GAOJINGZHI1_CHEZAI",String.valueOf(data1.intValue()));
                        pre("GAOJINGZHI2_CHEZAI",String.valueOf(data2.intValue()));
                        pre("GAOJINGZHI3_CHEZAI",String.valueOf(data3.intValue()));
                    }
                    TaskCenter.sharedCenter().connect(getpre("e6"), Integer.parseInt(getpre("e8")));
                    duibi();
                    tips();
                }else {
                    toastShow("输入的报警值信息错误.");
                }
                if(saveDw == 0){
                    pre("TESTTYPE_CHEZAI","PPM.M");
                }else if (saveDw ==1){
                    pre("TESTTYPE_CHEZAI","VOL.M");
                }else if (saveDw ==2){
                    pre("TESTTYPE_CHEZAI","LEL.M");
                }



            }
        });


        TextWatcher watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pre("SHEBEIWEIHAO",s.toString());
                toastShow("设备型号更新完成.");
               // xinghao.setText("设备型号:"+getpre("SHEBEIWEIHAO"));
            }
        };
        text_weihao.addTextChangedListener(watcher2);

        //todo 一键标定
        biaoding = view.findViewById(R.id.biaoding);
        biaoding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bfm = "超时";
                tips_biaoding();
                sendMessage(ShouDongBiaoFeng);

            }
        });

        huifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tips_huifu();
            }
        });
    }


    //todo
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void tips() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_save, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final Button save = view.findViewById(R.id.button5);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //todo
    private void tips_biaoding() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_biaoding, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final Button save = view.findViewById(R.id.button5);
        final TextView text_bd = view.findViewById(R.id.text_bd);

        intenhandler.postDelayed(new Runnable() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run() {
                if(bfm.equals("成功")){
                    text_bd.setText("标定成功");
                    text_bd.setTextColor(R.color.huangse);
                }else if(bfm.equals("失败")){
                    text_bd.setText("标定失败");
                }else if(bfm.equals("超时")){
                    text_bd.setText("标峰超时");
                }else if(bfm.equals("关闭")){

                }
            }
        },20000);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bfm = "关闭";
                dialog.dismiss();

            }
        });
        dialog.show();
    }


    //todo
    private void tipsInfo() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_shebeixinxi, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();
       // dialog.setCancelable(true);

        dialog.show();

    }


    private void tips_huifu() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_huifu, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.setCancelable(false);
        final Button ok =view.findViewById(R.id.button5);
        final Button quxiao =view.findViewById(R.id.button9);

        ok.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

            e1.setText("192.168.1.64");
            e2.setText("255.255.255.0");
            e3.setText("8000");
            e4.setText("admin");
            e5.setText("abcd1234");
            e6.setText("192.168.1.195");
            e7.setText("255.255.255.0");
            e8.setText("503");
            text_weihao.setText("xlj0001");

                b1.setBackgroundResource(R.drawable.bb01);
                b1.setTextColor(getResources().getColor(R.color.huangse2));

                b2.setTextColor(getResources().getColor(R.color.black));
                b2.setBackgroundResource(R.drawable.bbhui);
                b3.setTextColor(getResources().getColor(R.color.black));
                b3.setBackgroundResource(R.drawable.bbhui);
                b4.setText("15000");
                b5.setText("25000");
                b6.setText("30000");
                dialog.dismiss();
            }
        });

        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }




}
