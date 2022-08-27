package com.vise.bledemo.chezai.service;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.vise.bledemo.R;

public class start extends Activity implements View.OnClickListener {
    Button btn_1,btn_2;
    Button btn_floatView;//悬浮按钮
    Boolean isAdded=false;//是否添加
    WindowManager wm;
    WindowManager.LayoutParams params;
    public static boolean isStarted = false;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View displayView;
    private FragmentManager fragmentManager2 = null;
    private FragmentTransaction fragmentTransaction2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startservice);

        btn_1= (Button) findViewById(R.id.b1);
        btn_floatView=new Button(this);//用按钮表示悬浮窗
        btn_1.setOnClickListener(this);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 500;
        layoutParams.height = 500;
        layoutParams.x = 300;
        layoutParams.y = 300;
        initFloatView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }

    private void initFloatView() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        displayView = layoutInflater.inflate(R.layout.image_display, null);
        displayView.setOnTouchListener(new FloatingOnTouchListener());

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.b1:
                if(!isAdded){
                    wm.addView(btn_floatView,params);
                    isAdded=true;
                }
                break;
//            case R.id.btn_2:
//                if(isAdded){
//                    wm.removeView(btn_floatView);
//                    isAdded=false;
//                }else{
//                    Toast.makeText(this, "请open先", Toast.LENGTH_SHORT).show();
//                }
//                break;
        }
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }
}