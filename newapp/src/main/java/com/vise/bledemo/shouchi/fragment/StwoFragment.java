package com.vise.bledemo.shouchi.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;

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
import com.vise.bledemo.model.send;

import static com.vise.bledemo.utils.ViewBean.startBiaodingKailu;
import static com.vise.bledemo.utils.ViewBean.stopUpdate;
import static com.vise.bledemo.utils.url.Blue0x2800;
import static com.vise.bledemo.utils.url.Blue0x2801;
import static com.vise.bledemo.utils.url.Blue0x29;
import static com.vise.bledemo.utils.url.bluesenddata;
import static com.vise.bledemo.utils.url.getBiaogeng;
import static com.vise.bledemo.utils.url.intenhandler;
import static com.vise.bledemo.utils.url.readConcentrationAgreement;
import static com.vise.bledemo.utils.url.sendLel;
import static com.vise.bledemo.utils.url.sendPpm;
import static com.vise.bledemo.utils.url.sendVol;


public class StwoFragment extends BaseFragment {
    View view;
    private ImageView back;
    private EditText shebeiname;
    private TextView xinghao, shebeixinxi, aat1, aat2, aat3;
    private Button save, b1, b2, b3, biaoding, huifu;
    private EditText b4, b5, b6;
    Switch zhishidengSwitch;
    int saveDw;

    //TODO  测试单元 Csdy Gjz   SHOUCHIOTHERNONGDU
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment2, container, false);

        init1();
        initleft();


        return view;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void init1() {
        aat1 = f(view, R.id.aat1);
        aat2 = f(view, R.id.aat2);
        aat3 = f(view, R.id.aat3);
        save = f(view, R.id.save);
        biaoding = f(view, R.id.biaoding);
        huifu = f(view, R.id.button6);
        shebeiname = view.findViewById(R.id.text_weihao);
        shebeixinxi = f(view, R.id.shebeixinxi);
        zhishidengSwitch = (Switch) view.findViewById(R.id.switch2);

        shebeixinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipsInfo();
            }
        });

        zhishidengSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    bluesenddata.add(new send(1, Blue0x2801));
                    bluesenddata.add(new send(1, readConcentrationAgreement));
                    toastShow("打开");
                } else {
                    bluesenddata.add(new send(1, Blue0x2800));
                    bluesenddata.add(new send(1, readConcentrationAgreement));
                    toastShow("关闭");
                }
            }
        });

        biaoding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startBiaodingKailu == 1) {
                    toastShow("标定作业进行中");
                } else {
                    tips_biaoding();
                }
            }
        });

    }

    //TODO ppm/500=LEL ppm/10000=VOL
    //TODO TESTTYPE_CHEZAI 测试单位-车载
    //TODO GAOJINGZHI1_CHEZAI 告警值一级-车载
    //TODO GAOJINGZHI2_CHEZAI 告警值二级-车载
    //TODO GAOJINGZHI3_CHEZAI 告警其他-车载  pre("GAOJINGZHI3_CHEZAI",s.toString());
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initleft() {
        b1 = f(view, R.id.b1);
        b2 = f(view, R.id.b2);
        b3 = f(view, R.id.b3);
        b4 = f(view, R.id.b4);
        b5 = f(view, R.id.b5);
        b6 = f(view, R.id.b6);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDw = 0;
                b1.setBackgroundResource(R.drawable.bb01);
                b2.setBackgroundResource(R.drawable.bbhui);
                b3.setBackgroundResource(R.drawable.bbhui);
                b1.setTextColor(getResources().getColor(R.color.huangse2));
                b2.setTextColor(getResources().getColor(R.color.black));
                b3.setTextColor(getResources().getColor(R.color.black));
//                b4.setText(getpre("GAOJINGZHI1_SHOUCHI"));
//                b5.setText(getpre("GAOJINGZHI2_SHOUCHI"));
//                b6.setText(getpre("GAOJINGZHI3_SHOUCHI"));
//                aat1.setText("ppm.m");
//                aat2.setText("ppm.m");
//                aat3.setText("ppm.m");
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
                b1.setTextColor(getResources().getColor(R.color.black));
                b2.setTextColor(getResources().getColor(R.color.huangse2));
                b3.setTextColor(getResources().getColor(R.color.black));
//                b4.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI1_SHOUCHI"))/500));
//                b5.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI2_SHOUCHI"))/500));
//                b6.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI3_SHOUCHI"))/500));
//                aat1.setText("vol.m");
//                aat2.setText("vol.m");
//                aat3.setText("vol.m");
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDw = 2;
                b3.setBackgroundResource(R.drawable.bb01);
                b2.setBackgroundResource(R.drawable.bbhui);
                b1.setBackgroundResource(R.drawable.bbhui);
                b1.setTextColor(getResources().getColor(R.color.black));
                b2.setTextColor(getResources().getColor(R.color.black));
                b3.setTextColor(getResources().getColor(R.color.huangse2));
//                b4.setText(String.valueOf(Float.valueOf(getpre("GAOJINGZHI1_SHOUCHI"))/10000));
//                b5.setText(String.valueOf(Float.valueOf(getpre("GAOJINGZHI2_SHOUCHI"))/10000));
//                b6.setText(String.valueOf(Float.valueOf(getpre("GAOJINGZHI3_SHOUCHI"))/10000));
//                aat1.setText("lel.m");
//                aat2.setText("lel.m");
//                aat3.setText("lel.m");
            }
        });

        if (getpre("TESTTYPE_SHOUCHI").equals("PPM.M")) {
            saveDw = 0;
            b1.setBackgroundResource(R.drawable.bb01);
            b2.setBackgroundResource(R.drawable.bbhui);
            b3.setBackgroundResource(R.drawable.bbhui);
            b1.setTextColor(getResources().getColor(R.color.huangse2));
            b2.setTextColor(getResources().getColor(R.color.black));
            b3.setTextColor(getResources().getColor(R.color.black));

        } else if (getpre("TESTTYPE_SHOUCHI").equals("VOL.M")) {
            saveDw = 1;
            b2.setBackgroundResource(R.drawable.bb01);
            b1.setBackgroundResource(R.drawable.bbhui);
            b3.setBackgroundResource(R.drawable.bbhui);
            b2.setTextColor(getResources().getColor(R.color.huangse2));
            b1.setTextColor(getResources().getColor(R.color.black));
            b3.setTextColor(getResources().getColor(R.color.black));
            //获取告警值
//            b4.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI1_SHOUCHI"))/500));
//            b5.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI2_SHOUCHI"))/500));
//            b6.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI3_SHOUCHI"))/500));
//            aat1.setText("vol.m");
//            aat2.setText("vol.m");
//            aat3.setText("vol.m");
        } else if (getpre("TESTTYPE_SHOUCHI").equals("LEL.M")) {
            saveDw = 2;
            b3.setTextColor(getResources().getColor(R.color.huangse2));
            b2.setTextColor(getResources().getColor(R.color.black));
            b1.setTextColor(getResources().getColor(R.color.black));
            b3.setBackgroundResource(R.drawable.bb01);
            b2.setBackgroundResource(R.drawable.bbhui);
            b1.setBackgroundResource(R.drawable.bbhui);
//            b4.setText(String.valueOf(Float.valueOf(getpre("GAOJINGZHI1_SHOUCHI"))/10000));
//            b5.setText(String.valueOf(Float.valueOf(getpre("GAOJINGZHI2_SHOUCHI"))/10000));
//            b6.setText(String.valueOf(Integer.valueOf(getpre("GAOJINGZHI3_SHOUCHI"))/10000));
//            aat1.setText("lel.m");
//            aat2.setText("lel.m");
//            aat3.setText("lel.m");
            //首次
        } else {
            saveDw = 0;
            pre("TESTTYPE_CHEZAI", "PPM.M");
            pre("GAOJINGZHI1_SHOUCHI", "15000");
            pre("GAOJINGZHI2_SHOUCHI", "25000");
            pre("GAOJINGZHI3_SHOUCHI", "30000");
            b1.setBackgroundResource(R.drawable.bb01);
            b2.setBackgroundResource(R.drawable.bbhui);
            b3.setBackgroundResource(R.drawable.bbhui);
            //获取告警值
            b4.setText(getpre("GAOJINGZHI1_SHOUCHI"));
            b5.setText(getpre("GAOJINGZHI2_SHOUCHI"));
            b6.setText(getpre("GAOJINGZHI3_SHOUCHI"));
        }

        //获取告警值
        b4.setText(getpre("GAOJINGZHI1_SHOUCHI"));
        b5.setText(getpre("GAOJINGZHI2_SHOUCHI"));
        b6.setText(getpre("GAOJINGZHI3_SHOUCHI"));
        aat1.setText("ppm.m");
        aat2.setText("ppm.m");
        aat3.setText("ppm.m");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b4.getText().toString().length() > 8 || b5.getText().toString().length() > 8 || b6.getText().toString().length() > 8) {
                    toastShow("报警值错误");
                    return;
                }
                if (Integer.valueOf(b4.getText().toString()) > Integer.valueOf(b4.getText().toString()) ||
                        Integer.valueOf(b5.getText().toString()) > Integer.valueOf(b6.getText().toString())) {
                    toastShow("报警值错误");
                    return;
                }
                //保存 报警值
                pre("GAOJINGZHI1_SHOUCHI", b4.getText().toString());
                pre("GAOJINGZHI2_SHOUCHI", b5.getText().toString());
                pre("GAOJINGZHI3_SHOUCHI", b6.getText().toString());

                if (saveDw == 0) {
                    pre("TESTTYPE_SHOUCHI", "PPM.M");
                    bluesenddata.add(new send(1, sendPpm));
                } else if (saveDw == 1) {
                    pre("TESTTYPE_SHOUCHI", "VOL.M");
                    bluesenddata.add(new send(1, sendVol));
                } else if (saveDw == 2) {
                    pre("TESTTYPE_SHOUCHI", "LEL.M");
                    bluesenddata.add(new send(1, sendLel));
                }
                bluesenddata.add(new send(1, sendString()));
                Log.e("aazzzhuanhua--手持", sendString());
                tips();

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
    private void tipsInfo() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_shebeixinxi3, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        // dialog.setCancelable(true);

        dialog.show();

    }


    private void tips_huifu() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_huifu, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final Button ok = view.findViewById(R.id.button5);
        final Button quxiao = view.findViewById(R.id.button9);

        ok.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {


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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //TODO 可见操作
            shebeiname.setText(getpre("SHEBEIWEIHAOSHOUCHI"));
        } else {
//            //TODO 不可见操作
        }
    }


    private String sendString() {
        String result = "CCAC2B0C";
        int yi = Integer.valueOf(b4.getText().toString());
        int er = Integer.valueOf(b5.getText().toString());
        int san = Integer.valueOf(b6.getText().toString());
        //todo 转化凑足8位
        String ssyi = "";
        String syi = toHex(yi);
        for (int i = 0; i < 8 - syi.length(); i++) {
            ssyi += "0";
        }
        ssyi = ssyi + syi;
        //todo 转化凑足8位
        String sser = "";
        String ser = toHex(er);
        for (int i = 0; i < 8 - ser.length(); i++) {
            sser += "0";
        }
        sser = sser + ser;
        //todo 转化凑足8位
        String sssan = "";
        String ssan = toHex(san);
        for (int i = 0; i < 8 - ssan.length(); i++) {
            sssan += "0";
        }
        ssan = sssan + ssan;
        result = result + ssyi + sser + ssan;
        Log.e("ajisuana-result ", String.valueOf(result));

        char aa[] = result.toCharArray();
        for (int i = 0; i < aa.length; i++) {
            Log.e("ajisuana-char-aa ", String.valueOf(aa[i]));
        }
        //todo 校验值
        int a1 = Integer.parseInt(String.valueOf("ccac"), 16);
        Log.e("ajisuana-ccac ", String.valueOf(a1));
        int a2 = Integer.parseInt(String.valueOf("2b"), 16);
        Log.e("ajisuana-2b ", String.valueOf(a2));
        int a3 = Integer.parseInt(String.valueOf("0c"), 16);
        Log.e("ajisuana-0c ", String.valueOf(a3));
        int num = 0;
        int index = 8;
        for (int i = 0; i < 12; i++) {
            num += Integer.parseInt(String.valueOf("" + aa[index] + aa[index + 1]), 16);
            Log.e("ajisuana-计算的数字", String.valueOf(aa[index] + aa[index + 1]));
            Log.e("ajisuana", String.valueOf(num));
            index += 2;
        }
        num = num + a1 + a2 + a3;
        Log.e("ajisuana -最后计算", String.valueOf(num));
        result = result + toHex(num);
        return result;
    }


    public String toHex(int num) {
        StringBuilder s = new StringBuilder();
        if (num > 0) {
            while (num != 0) {
                int a = num % 16;
                s.append((a > 9) ? (toStr(a)) : a);
                num = num / 16;
            }
            s.reverse();
            return s.toString();
        } else if (num < 0) {
            long n = (long) Math.pow(2, 32) + num;
            while (n != 0) {
                long a = n % 16;
                s.append((a > 9) ? (toStr((int) a)) : a);
                n = n / 16;
            }
            s.reverse();
            return s.toString();
        }
        return "0";
    }

    public String toStr(int a) {
        if (a == 10) return "a";
        else if (a == 11) return "b";
        else if (a == 12) return "c";
        else if (a == 13) return "d";
        else if (a == 14) return "e";
        return "f";
    }

    private void tips_biaoding() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_isbiaoding, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        final Button yes = view.findViewById(R.id.button5);
        final Button no = view.findViewById(R.id.button9);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluesenddata.add(new send(1, Blue0x29));
                bluesenddata.add(new send(1, getBiaogeng));
                startBiaodingKailu = 1;
                stopUpdate = 1;
                dialog.dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
