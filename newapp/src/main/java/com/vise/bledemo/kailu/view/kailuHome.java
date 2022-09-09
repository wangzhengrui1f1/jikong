package com.vise.bledemo.kailu.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.vise.bledemo.Base.BaseFragmentActivity;
import com.vise.bledemo.R;
import com.vise.bledemo.kailu.fragment.Sone;
import com.vise.bledemo.kailu.fragment.caozuojiluFragment;
import com.vise.bledemo.kailu.fragment.shebeixinxiFragment;
import com.vise.bledemo.shouchi.activity.Home;
import com.vise.bledemo.shouchi.fragment.SoneFragment;
import com.vise.bledemo.shouchi.fragment.SthreeFragment;
import com.vise.bledemo.shouchi.fragment.StwoFragment;
import com.vise.bledemo.utils.NoScrollViewPager;

import java.util.ArrayList;

import static com.vise.bledemo.utils.Shengyin.play1;
import static com.vise.bledemo.utils.Shengyin.play2;
import static com.vise.bledemo.utils.Shengyin.register;
import static com.vise.bledemo.utils.UseFngfa.closejianpan;

public class kailuHome extends BaseFragmentActivity implements OnCheckedChangeListener {
    //todo 监听网络变化
    //ViewPager
    private NoScrollViewPager main_viewPager;
    //RadioGroup
    private RadioGroup main_tab_RadioGroup;
    //RadioButton
    private RadioButton radio_1, radio_2, radio_3;
    //
    private ArrayList<Fragment> fragmentList;
    int current = 0;
    ImageView shezhi, xj;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        //todo  android:background="#F5F6FA"
        setContentView(R.layout.activity_homekailu);

        InitView();
        //todo
        InitViewPager();
        register(kailuHome.this);
    }

    @SuppressLint("NewApi")
    public void InitView() {
        main_tab_RadioGroup = (RadioGroup) findViewById(R.id.main_tab_RadioGroup);
        radio_1 = (RadioButton) findViewById(R.id.radio_1);
        radio_2 = (RadioButton) findViewById(R.id.radio_2);
        // radio_3 = (RadioButton) findViewById(R.id.radio_3);
        main_tab_RadioGroup.setOnCheckedChangeListener(this);
        shezhi = (ImageView) findViewById(R.id.shezhi);
        xj = (ImageView) findViewById(R.id.xj);

        shezhi.setBackground(getDrawable(R.drawable.shezhi));
        xj.setBackground(getDrawable(R.drawable.xj2));

        radio_1.setBackground(getDrawable(R.drawable.sbutton2));
        radio_2.setBackground(getDrawable(R.drawable.sbutton));


        radio_1.setTextColor(getColor(R.color.e3));
        radio_2.setTextColor(getColor(R.color.white));

    }


    public void InitViewPager() {
        main_viewPager = (NoScrollViewPager) findViewById(R.id.main_ViewPager);
        fragmentList = new ArrayList<Fragment>();
        Fragment fragment1 = new Sone();
        Fragment fragment2 = new shebeixinxiFragment();
        //  Fragment fragment3 = new caozuojiluFragment();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        //todo 学习中心
        // fragmentList.add(fragment3);
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

//    public class MyListner implements ViewPager.OnPageChangeListener {
//
//        @Override
//        public void onPageScrollStateChanged(int arg0) {
//
//        }
//
//        @Override
//        public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//        }
//
//        @Override
//        public void onPageSelected(int arg0) {
//            int current = main_viewPager.getCurrentItem();
//            switch (current) {
//                case 0:
//                    main_tab_RadioGroup.check(R.id.radio_1);
//                    break;
//                case 1:
//                    main_tab_RadioGroup.check(R.id.radio_2);
//                    break;
//                case 2:
//                    main_tab_RadioGroup.check(R.id.radio_3);
//                    break;
//            }
//        }
//    }

    @SuppressLint("NewApi")
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {

        switch (checkId) {
            case R.id.radio_1:
                current = 0;
                shezhi.setBackground(getDrawable(R.drawable.shezhi));
                xj.setBackground(getDrawable(R.drawable.xj2));

                radio_1.setBackground(getDrawable(R.drawable.sbutton2));
                radio_2.setBackground(getDrawable(R.drawable.sbutton));


                radio_1.setTextColor(getColor(R.color.e3));
                radio_2.setTextColor(getColor(R.color.white));

                closejianpan(kailuHome.this, radio_1);
                Window_white();
                play1();
                break;
            case R.id.radio_2:
                current = 1;

                shezhi.setBackground(getDrawable(R.drawable.shezhi2));
                xj.setBackground(getDrawable(R.drawable.xj));

                radio_2.setBackground(getDrawable(R.drawable.sbutton2));
                radio_1.setBackground(getDrawable(R.drawable.sbutton));


                radio_1.setTextColor(getColor(R.color.white));
                radio_2.setTextColor(getColor(R.color.e3));

                closejianpan(kailuHome.this, radio_2);
                Window_white();
                play2();
                break;
//            case R.id.radio_3:
//                current = 2;
//                Window_white();
//                break;
        }
        if (main_viewPager.getCurrentItem() != current) {
            main_viewPager.setCurrentItem(current);
        }
    }

    private void Window_black() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.BLACK);
            int ui = decorView.getSystemUiVisibility();
            ui |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN; //设置状态栏中字体的颜色为黑色
            decorView.setSystemUiVisibility(ui);
        }
    }

    private void Window_white() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            int ui = decorView.getSystemUiVisibility();
            ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; //设置状态栏中字体的颜色为黑色
            decorView.setSystemUiVisibility(ui);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                android.support.v7.app.AlertDialog.Builder build = new android.support.v7.app.AlertDialog.Builder(this);
                build.setTitle("集控")
                        .setMessage("确定要退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).show();
                break;
            default:
                break;
        }
        return false;
    }

}
