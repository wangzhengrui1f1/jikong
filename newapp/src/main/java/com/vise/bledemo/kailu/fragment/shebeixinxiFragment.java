package com.vise.bledemo.kailu.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vise.bledemo.Base.BaseFragment;
import com.vise.bledemo.R;
import com.vise.bledemo.baiduMap.baiduMap.view.WelcomeActivity;
import com.vise.bledemo.model.send;

import static com.vise.bledemo.utils.url.Blue0x2801;
import static com.vise.bledemo.utils.url.bluesenddata;
import static com.vise.bledemo.utils.url.sendLel;
import static com.vise.bledemo.utils.url.sendPpm;
import static com.vise.bledemo.utils.url.sendVol;


public class shebeixinxiFragment extends BaseFragment {
    View view;
    private ImageView back;
    Button save, b1, b2, b3, huifu;
    private TextView textdi, textgao, shebeixinxi;
    int saveDw = 0;
    EditText shebeimc;
    // 16进制数字：大小写不影响
    private final static char[] HEXDIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentshebei2, container, false);
        init();

        return view;
    }

    private void init() {
        //  back = (ImageView) view.findViewById(R.id.back);
        huifu = view.findViewById(R.id.huifu);
        save = (Button) view.findViewById(R.id.save);
        textdi = (TextView) view.findViewById(R.id.textdi);
        textgao = (TextView) view.findViewById(R.id.textgao);
        shebeimc = (EditText) view.findViewById(R.id.shebeimc);
        shebeixinxi = view.findViewById(R.id.shebeixinxi);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
//                startActivity(intent);
//                System.exit(0);
//            }
//        });

        b1 = view.findViewById(R.id.e1);
        b2 = view.findViewById(R.id.e2);
        b3 = view.findViewById(R.id.e3);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textdi.getText().toString().length() > 8 || textgao.getText().toString().length() > 8) {
                    toastShow("超过最大范围,保存失败");
                    return;
                }
                if (Integer.valueOf(textdi.getText().toString()) >= Integer.valueOf(textgao.getText().toString())) {
                    toastShow("低报大于高报，保存失败");
                    return;
                }

                Log.e("aazzzhuanhua--开路", sendString());
                tipssave();
            }
        });

        if (getpre("kailudanwei").equals("PPM.m")) {
            b1.setBackgroundResource(R.drawable.bb01);
            b2.setBackgroundResource(R.drawable.bbhui);
            b3.setBackgroundResource(R.drawable.bbhui);
            b1.setTextColor(getResources().getColor(R.color.huangse2));
            b2.setTextColor(getResources().getColor(R.color.black));
            b3.setTextColor(getResources().getColor(R.color.black));
        } else if (getpre("kailudanwei").equals("VOL.m")) {
            b2.setBackgroundResource(R.drawable.bb01);
            b1.setBackgroundResource(R.drawable.bbhui);
            b3.setBackgroundResource(R.drawable.bbhui);
            b1.setTextColor(getResources().getColor(R.color.black));
            b2.setTextColor(getResources().getColor(R.color.huangse2));
            b3.setTextColor(getResources().getColor(R.color.black));
        } else if (getpre("kailudanwei").equals("LEL.m")) {
            b3.setBackgroundResource(R.drawable.bb01);
            b2.setBackgroundResource(R.drawable.bbhui);
            b1.setBackgroundResource(R.drawable.bbhui);
            b1.setTextColor(getResources().getColor(R.color.black));
            b2.setTextColor(getResources().getColor(R.color.black));
            b3.setTextColor(getResources().getColor(R.color.huangse2));
        } else {
            pre("kailudanwei", "ppm");
        }

        if (Integer.valueOf(getpre("dibao")) > 1) {
            textdi.setText(getpre("dibao"));
        } else {
            pre("dibao", "15000");
            textdi.setText(getpre("dibao"));
        }

        if (Integer.valueOf(getpre("gaobao")) > 1) {
            textgao.setText(getpre("gaobao"));
        } else {
            pre("gaobao", "25000");
            textgao.setText(getpre("gaobao"));
        }

        shebeimc.setText(getpre("kailushebeiname"));

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
                b1.setTextColor(getResources().getColor(R.color.black));
                b2.setTextColor(getResources().getColor(R.color.black));
                b3.setTextColor(getResources().getColor(R.color.huangse2));
            }
        });


        huifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tips_huifu();


            }
        });


        shebeixinxi = view.findViewById(R.id.shebeixinxi);
        shebeixinxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipsInfo();
            }
        });
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
                pre("kailudanwei", "PPM.m");
                saveDw = 0;
                b1.setBackgroundResource(R.drawable.bb01);
                b2.setBackgroundResource(R.drawable.bbhui);
                b3.setBackgroundResource(R.drawable.bbhui);
                b1.setTextColor(getResources().getColor(R.color.huangse2));
                b2.setTextColor(getResources().getColor(R.color.black));
                b3.setTextColor(getResources().getColor(R.color.black));

                pre("dibao", "15000");
                pre("gaobao", "25000");
                textdi.setText(getpre("dibao"));
                textgao.setText(getpre("gaobao"));

                pre("kailushebeiname", "SA0001");
                shebeimc.setText(getpre("kailushebeiname"));
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
    private void tipssave() {
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
                if (saveDw == 0) {
                    pre("kailudanwei", "PPM.m");
                    bluesenddata.add(new send(1, sendPpm));
                } else if (saveDw == 1) {
                    pre("kailudanwei", "VOL.m");
                    bluesenddata.add(new send(1, sendVol));
                } else {
                    pre("kailudanwei", "LEL.m");
                    bluesenddata.add(new send(1, sendLel));
                }

                bluesenddata.add(new send(1, sendString()));
                //低高保存
                pre("dibao", textdi.getText().toString());
                pre("gaobao", textgao.getText().toString());
                pre("kailushebeiname", shebeimc.getText().toString());
                toastShow("保存成功!");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //todo
    private void tipsInfo() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.item_shebeixinxi2, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        // dialog.setCancelable(true);

        dialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        shebeimc.setText(getpre("kailushebeiname"));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //TODO 可见操作
            shebeimc.setText(getpre("kailushebeiname"));
        } else {
//            //TODO 不可见操作
        }
    }


    private String sendString() {
        String result = "CCAC2B0C";
        int yi = Integer.valueOf(textdi.getText().toString());
        int er = Integer.valueOf(textgao.getText().toString());
        int San = er + 1000;
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
        String ssan = "";
        String san = toHex(San);
        for (int i = 0; i < 8 - san.length(); i++) {
            ssan += "0";
        }
        ssan = ssan + san;

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
        Log.e("ajisuana -最后计算", String.valueOf(result));
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
}
