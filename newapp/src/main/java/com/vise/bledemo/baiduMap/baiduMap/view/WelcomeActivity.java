package com.vise.bledemo.baiduMap.baiduMap.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.bledemo.Base.BaseFragmentActivity;
import com.vise.bledemo.R;
import com.vise.bledemo.chezai.view.chezaiHome;
import com.vise.bledemo.kailu.view.kailuHome;
import com.vise.bledemo.model.User;
import com.vise.bledemo.otherView.bannerView.bannerFragment1;
import com.vise.bledemo.otherView.bannerView.bannerFragment2;
import com.vise.bledemo.otherView.bannerView.bannerFragment3;
import com.vise.bledemo.shouchi.activity.Home;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import static com.vise.bledemo.sqlite.sqliteMethod.IScheck;
import static com.vise.bledemo.sqlite.sqliteMethod.addUser;
import static com.vise.bledemo.utils.Jiexi.jiexi2;
import static com.vise.bledemo.utils.getTime.isTabletDevice;

public class WelcomeActivity extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private Button jinru;
    private Banner banner;
    //ViewPager
    private ViewPager main_viewPager;
    //RadioGroup
    private RadioGroup main_tab_RadioGroup;
    //RadioButton
    private RadioButton radio_1,radio_2,radio_3;
    //
    private ArrayList<Fragment> fragmentList;
    int current = 0;
    int homeId=0;//0车 1开 2 手持
    EditText username,password;
    SharedPreferences setting;
    EditText s1,s2;
    Button submit;
    LinearLayout ba1,ba2;
    public static String SHARE_APP_TAG="LOGIN_TAG";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //平板
        if(isTabletDevice(WelcomeActivity.this)){
            //todo  android:background="#F5F6FA"
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖竖屏
        }
      //  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制横屏
        setContentView(R.layout.welcome);
        //设备唯一标示
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if(androidId==null || androidId.equals("")){
            pre("androidIds","未知设备");
        } else {
            pre("androidIds",androidId);
        }

        Log.e("aabiaoshi",androidId);
        setting = getSharedPreferences(SHARE_APP_TAG, 0);
        ba1 = findViewById(R.id.home15);
        ba2 = findViewById(R.id.home16);
        ba1.setBackgroundResource(R.drawable.wel1);
        ba2.setBackgroundResource(R.drawable.wel2);

        if(isTabletDevice(WelcomeActivity.this)){
            ba1.setBackgroundResource(R.drawable.wel1);
            ba2.setBackgroundResource(R.drawable.wel1);
        }
        first();
        init();
        InitView();

        initPreData();
        jiexi2("0");


        isBlue();
        if(isTabletDevice(WelcomeActivity.this)){
            main_tab_RadioGroup.setVisibility(View.GONE);
            InitViewPager2();
        }else {
            InitViewPager();
        }
    }

    private void initPreData() {
        pre("starttime","1");

    }


    private void first() {
        Boolean user_first = setting.getBoolean("FIRST178",true);
        if(user_first){
            setting.edit().putBoolean("FIRST178", false).commit();
            addUser(myDatebaseHelper,new User("test","XLJ2020","test"));
            //todo 车速 初始化 10
            pre("setchesu","10");
            pre("kailudanwei","PPM.m");
            pre("TESTTYPE_SHOUCHI","PPM.M");

            //开路
            pre("dibao","15000");
            pre("gaobao","25000");

            //手持
            pre("GAOJINGZHI1_SHOUCHI","15000");
            pre("GAOJINGZHI2_SHOUCHI","25000");
            pre("GAOJINGZHI3_SHOUCHI","30000");

        }else{

        }
    }



    private void init() {
        if(ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(WelcomeActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},200);
        }else{
            Toast.makeText(WelcomeActivity.this,"已开启定位权限", Toast.LENGTH_LONG).show();
            //存储权限

        }

      //  doStartApplicationWithPackageName("com.edu.neusoft.shy.shangchuantup");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                verifyStoragePermissions(WelcomeActivity.this);
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//                ComponentName cn = new ComponentName("com.edu.neusoft.shy.shangchuantup",className);
//                intent.setComponent(cn);
//                startActivity(intent);

                //doStartApplicationWithPackageName("com.edu.neusoft.shy.shangchuantup");
            }
        },1500);
        s1=f(R.id.username);
        s2=f(R.id.password);
        if(!getpre("username").equals("1")){
            s1.setText(getpre("username"));
        }
        if(!getpre("password").equals("1")){
            s2.setText(getpre("password"));
        }
        submit = f(R.id.button2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!s1.getText().toString().equals("")){
                    pre("Caozuoyuan",s1.getText().toString());
                    if(IScheck(myDatebaseHelper,s2.getText().toString())){
                        pre("username",s1.getText().toString());
                        pre("password",s2.getText().toString());
                        tips();
                    }else {
                        toastShow("集控码错误.");
                    }
                }else {
                    toastShow("请输入操作员");
                }

            }
        });


    }

    //打开外部app，嵌入旧窗口中
    @SuppressLint("NewApi")
    private void doStartApplicationWithPackageName(String packagename) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
           // BDToast.showToast(getText(R.string.app_not_found).toString());
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            startActivity(intent);
        }
    }
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void InitView() {
        main_tab_RadioGroup = (RadioGroup) findViewById(R.id.main_tab_RadioGroup);
        radio_1 = (RadioButton) findViewById(R.id.radio_1);
        radio_2 = (RadioButton) findViewById(R.id.radio_2);
        radio_3 = (RadioButton) findViewById(R.id.radio_3);
        main_tab_RadioGroup.setOnCheckedChangeListener(this);
        radio_1.setTextSize(18);
        radio_2.setTextSize(12);
        radio_3.setTextSize(12);
    }


    public void InitViewPager() {
        main_viewPager = (ViewPager) findViewById(R.id.main_ViewPager);
        fragmentList = new ArrayList<Fragment>();
        Fragment fragment1 = new bannerFragment1();
        Fragment fragment2 = new bannerFragment2();
        Fragment fragment3 = new bannerFragment3();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        main_viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragmentList));
        main_viewPager.setCurrentItem(0);
        main_viewPager.addOnPageChangeListener(new MyListner());
    }

    public void InitViewPager2() {
        main_viewPager = (ViewPager) findViewById(R.id.main_ViewPager);
        fragmentList = new ArrayList<Fragment>();
        Fragment fragment1 = new bannerFragment1();
        fragmentList.add(fragment1);
        main_viewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), fragmentList));
        main_viewPager.setCurrentItem(0);
        main_viewPager.addOnPageChangeListener(new MyListner());
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

    public class MyListner implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            int current = main_viewPager.getCurrentItem();
            switch (current) {
                case 0:
                    main_tab_RadioGroup.check(R.id.radio_1);
                    homeId=0;
                    radio_1.setTextSize(18);
                    radio_2.setTextSize(12);
                    radio_3.setTextSize(12);
                    break;
                case 1:
                    main_tab_RadioGroup.check(R.id.radio_2);
                    homeId=1;
                    radio_1.setTextSize(12);
                    radio_2.setTextSize(18);
                    radio_3.setTextSize(12);
                    break;
                case 2:
                    main_tab_RadioGroup.check(R.id.radio_3);
                    homeId=2;
                    radio_1.setTextSize(12);
                    radio_2.setTextSize(12);
                    radio_3.setTextSize(18);
                    break;
            }
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {

        switch (checkId) {
            case R.id.radio_1:
                current = 0;
                homeId=0;
                radio_1.setTextSize(18);
                radio_2.setTextSize(12);
                radio_3.setTextSize(12);
                break;
            case R.id.radio_2:
                current = 1;
                homeId=1;
                radio_1.setTextSize(12);
                radio_2.setTextSize(18);
                radio_3.setTextSize(12);
                break;
            case R.id.radio_3:
                current = 2;
                homeId=2;
                radio_1.setTextSize(12);
                radio_2.setTextSize(12);
                radio_3.setTextSize(18);
                break;
        }
        if (main_viewPager.getCurrentItem() != current) {
            main_viewPager.setCurrentItem(current);
        }
    }


    //todo
    private void tips() {
        LayoutInflater inflater = LayoutInflater.from(WelcomeActivity.this);
        View view = inflater.inflate(R.layout.tips, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(WelcomeActivity.this);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        Button jinru = (Button) view.findViewById(R.id.b1);
        Button quxiao = (Button) view.findViewById(R.id.b2);
        TextView info = (TextView) view.findViewById(R.id.textView9);
        if(homeId==0){
            info.setText("确认进入车载模式");
        }else if(homeId==1){
            info.setText("确认进入开路模式");
        }else if(homeId==2){
            info.setText("确认进入手持模式");
        }
        jinru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   intentExit(chezaiHome.class);
//                pre("status","0");
//                intentExit(chezaiHome.class);
                if(homeId==0){
                    if(isTabletDevice(WelcomeActivity.this)){
                        pre("status","0");
                        intentExit(chezaiHome.class);
                    }else {
                        Toast.makeText(WelcomeActivity.this, "该模式仅支持平板设备.", Toast.LENGTH_LONG).show();
                    }
                   // intentExit(chezaiHome.class);
                }else if(homeId==1){
                    pre("status","1");
                    intentExit(kailuHome.class);
                }else if(homeId==2){
                    pre("status","2");
                    intentExit(Home.class);
                }
                dialog.dismiss();

            }
        });

        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

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
                                android.os.Process.killProcess(android.os.Process.myPid());
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