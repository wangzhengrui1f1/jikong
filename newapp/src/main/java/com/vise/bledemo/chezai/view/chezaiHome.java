package com.vise.bledemo.chezai.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.vise.bledemo.Base.BaseFragmentActivity;
import com.vise.bledemo.R;
import com.vise.bledemo.chezai.fragment.SoneFragment1;
import com.vise.bledemo.chezai.fragment.SthreeFragmentchezai;
import com.vise.bledemo.chezai.fragment.xinxiFragment;
import com.vise.bledemo.kailu.view.kailuHome;
import com.vise.bledemo.utils.NoScrollViewPager;

import java.util.ArrayList;

import static com.vise.bledemo.utils.Shengyin.play1;
import static com.vise.bledemo.utils.Shengyin.register;
import static com.vise.bledemo.utils.UseFngfa.closejianpan;
import static com.vise.bledemo.utils.getTime.isTabletDevice;

public class chezaiHome extends BaseFragmentActivity implements OnCheckedChangeListener {
    //todo 监听网络变化
    //ViewPager
    private NoScrollViewPager main_viewPager;
    //RadioGroup
    private RadioGroup main_tab_RadioGroup;
    //RadioButton
    private RadioButton radio_1,radio_2,radio_3;
    //
    private ArrayList<Fragment> fragmentList;
    int current = 0;
    //todo 悬浮窗
    Button btn_1,btn_2;
    Button btn_floatView;//悬浮按钮
    Boolean isAdded=false;//是否添加
    WindowManager wm;
    WindowManager.LayoutParams params;
    public static boolean isStarted = false;
    ImageView shezhi,xj,jl;
    int exitflag = 0;
//    private WindowManager windowManager;
//    private WindowManager.LayoutParams layoutParams;
//
//    private View displayView;
//    private Boolean winflag=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0)
        {
            finish();
            return;
        }
        if(isTabletDevice(chezaiHome.this)){
            //todo  android:background="#F5F6FA"
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏

        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖竖屏
           // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
            //todo  android:background="#F5F6FA"
//            setContentView(R.layout.activity_homecheshouji);
        }
        setContentView(R.layout.activity_homeche);
        // 界面初始函数，用来获取定义的各控件对应的ID
//        //TODO 设置状态栏透明
//        if (Build.VERSION.SDK_INT >= 21){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//            int ui = decorView.getSystemUiVisibility();
//            ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //设置状态栏中字体的颜色为黑色
//            decorView.setSystemUiVisibility(ui);
//        }
       // tips();
        InitView();
        //todo
        InitViewPager();
        register(chezaiHome.this);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void InitView() {
        main_tab_RadioGroup = (RadioGroup) findViewById(R.id.main_tab_RadioGroup);
        radio_1 = (RadioButton) findViewById(R.id.radio_1);
        radio_2 = (RadioButton) findViewById(R.id.radio_2);
        radio_3 = (RadioButton) findViewById(R.id.radio_3);
        shezhi = (ImageView) findViewById(R.id.shezhi);
        xj = (ImageView) findViewById(R.id.xj);
        jl = (ImageView) findViewById(R.id.jl);
        main_tab_RadioGroup.setOnCheckedChangeListener(this);
//        //todo 初始化
//        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
//        layoutParams = new WindowManager.LayoutParams();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//        } else {
//            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//        }
//        layoutParams.format = PixelFormat.RGBA_8888;
//        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        layoutParams.width = 500;
//        layoutParams.height = 500;
//        layoutParams.x = 50;
//        layoutParams.y = 120;
        Handler hsa = new Handler();
        hsa.postDelayed(new Runnable() {
            @Override
            public void run() {
             //   initFloatView();
            }
        },300);

        shezhi.setBackground(getDrawable(R.drawable.shezhi));
        xj.setBackground(getDrawable(R.drawable.xj2));
        jl.setBackground(getDrawable(R.drawable.jl));
        radio_1.setBackground(getDrawable(R.drawable.sbutton2));
        radio_2.setBackground(getDrawable(R.drawable.sbutton));
        radio_3.setBackground(getDrawable(R.drawable.sbutton));

        radio_1.setTextColor(getColor(R.color.e3));
        radio_2.setTextColor(getColor(R.color.white));
        radio_3.setTextColor(getColor(R.color.white));

        isAdded=true;
    }


    public void InitViewPager() {
        main_viewPager = (NoScrollViewPager) findViewById(R.id.main_ViewPager);
        fragmentList = new ArrayList<Fragment>();
        Fragment fragment1 = new SoneFragment1();
        Fragment fragment2 = new xinxiFragment();
        Fragment fragment3 = new SthreeFragmentchezai();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        main_viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragmentList));
        main_viewPager.setCurrentItem(0);
        main_viewPager.setOffscreenPageLimit(3);
       // main_viewPager.addOnPageChangeListener(new MyListner());
    }


    public class MyAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> list;
        public MyAdapter(FragmentManager fm, ArrayList<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }
        @Override
        public int getCount() {
            return list.size();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {

        switch (checkId) {
            case R.id.radio_1:
                current = 0;
                Window_white();
//                if(displayView.getVisibility()==View.GONE){
//                    displayView.setVisibility(View.VISIBLE);
////                    layoutParams.x = 50;
////                    layoutParams.y = 80;
////                    windowManager.updateViewLayout(displayView, layoutParams);
//                }
               // tips();
                shezhi.setBackground(getDrawable(R.drawable.shezhi));
                xj.setBackground(getDrawable(R.drawable.xj2));
                jl.setBackground(getDrawable(R.drawable.jl));
                radio_1.setBackground(getDrawable(R.drawable.sbutton2));
                radio_2.setBackground(getDrawable(R.drawable.sbutton));
                radio_3.setBackground(getDrawable(R.drawable.sbutton));

                radio_1.setTextColor(getColor(R.color.e3));
                radio_2.setTextColor(getColor(R.color.white));
                radio_3.setTextColor(getColor(R.color.white));
                closejianpan(chezaiHome.this,radio_1);
                pre("chezaiOneFragment","11");
                play1();
                break;
            case R.id.radio_2:
                current = 1;
//                if(displayView.getVisibility()==View.VISIBLE){
//                    displayView.setVisibility(View.GONE);
//                }
                Window_white();
                shezhi.setBackground(getDrawable(R.drawable.shezhi2));
                xj.setBackground(getDrawable(R.drawable.xj));
                jl.setBackground(getDrawable(R.drawable.jl));


                radio_2.setBackground(getDrawable(R.drawable.sbutton2));
                radio_1.setBackground(getDrawable(R.drawable.sbutton));
                radio_3.setBackground(getDrawable(R.drawable.sbutton));

                radio_1.setTextColor(getColor(R.color.white));
                radio_2.setTextColor(getColor(R.color.e3));
                radio_3.setTextColor(getColor(R.color.white));
                closejianpan(chezaiHome.this,radio_2);
                pre("chezaiOneFragment","22");
                play1();
                break;
            case R.id.radio_3:
                current = 2;
//                if(displayView.getVisibility()==View.VISIBLE){
//                    displayView.setVisibility(View.GONE);
//                }
                Window_white();
                shezhi.setBackground(getDrawable(R.drawable.shezhi));
                xj.setBackground(getDrawable(R.drawable.xj));
                jl.setBackground(getDrawable(R.drawable.jl2));


                radio_1.setBackground(getDrawable(R.drawable.sbutton));
                radio_2.setBackground(getDrawable(R.drawable.sbutton));
                radio_3.setBackground(getDrawable(R.drawable.sbutton2));

                radio_1.setTextColor(getColor(R.color.white));
                radio_2.setTextColor(getColor(R.color.white));
                radio_3.setTextColor(getColor(R.color.e3));
                closejianpan(chezaiHome.this,radio_3);
                pre("chezaiOneFragment","33");
                play1();
                break;
        }
        if (main_viewPager.getCurrentItem() != current) {
            main_viewPager.setCurrentItem(current);
        }
    }
    private void Window_black() {
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.BLACK);
            int ui = decorView.getSystemUiVisibility();
            ui |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN; //设置状态栏中字体的颜色为黑色
            decorView.setSystemUiVisibility(ui);
        }
    }
    private void Window_white() {
//        if (Build.VERSION.SDK_INT >= 21){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//            int ui = decorView.getSystemUiVisibility();
//            ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //设置状态栏中字体的颜色为黑色
//            decorView.setSystemUiVisibility(ui);
//        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
//                android.support.v7.app.AlertDialog.Builder build = new android.support.v7.app.AlertDialog.Builder(this);
//                build.setTitle("集控")
//                        .setMessage("确定要退出吗？")
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                android.os.Process.killProcess(android.os.Process.myPid());
//                            }
//                        })
//                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // TODO Auto-generated method stub
//                            }
//                        }).show();
                if(exitflag<=2){
                    toastShow("再次滑动退出...");
                    exitflag++;
                }else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exitflag = 0;
                    }
                },3500);
                break;
            default:
                break;
        }
        return false;
    }

//    private void initFloatView() {
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        displayView = layoutInflater.inflate(R.layout.image_display, null);
//        displayView.setOnTouchListener(new FloatingOnTouchListener());
//        windowManager.addView(displayView, layoutParams);
//    }
//
//
//    private class FloatingOnTouchListener implements View.OnTouchListener {
//        private int x;
//        private int y;
//
//        @Override
//        public boolean onTouch(View view, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    x = (int) event.getRawX();
//                    y = (int) event.getRawY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    int nowX = (int) event.getRawX();
//                    int nowY = (int) event.getRawY();
//                    int movedX = nowX - x;
//                    int movedY = nowY - y;
//                    x = nowX;
//                    y = nowY;
//                    layoutParams.x = layoutParams.x + movedX;
//                    layoutParams.y = layoutParams.y + movedY;
//                    windowManager.updateViewLayout(view, layoutParams);
//                    break;
//                default:
//                    break;
//            }
//            return false;
//        }
//    }


    //todo
    private void tips() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_jiazai, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.setCancelable(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        },5000);
        dialog.show();

    }






}
