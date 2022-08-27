package com.vise.bledemo.shouchi.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.vise.bledemo.Base.BaseFragment;
import com.vise.bledemo.R;
import com.vise.bledemo.adapter.jingBaoAdapter;
import com.vise.bledemo.baiduMap.baiduMap.view.WelcomeActivity;
import com.vise.bledemo.model.GetJingbao;
import com.vise.bledemo.sqlite.MyDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.vise.bledemo.sqlite.sqliteMethod.GetJingbaoData;
import static com.vise.bledemo.sqlite.sqliteMethod.getData;
import static com.vise.bledemo.utils.url.SQLITE_NAME;


public class SthreeFragment extends BaseFragment {
    View view;
    List<GetJingbao> lst = new ArrayList<GetJingbao>();
    jingBaoAdapter adapter;
    ListView listView;
    private ImageView back;
    private EditText starttime,endtime,sids,names;
    private LinearLayout daochu;
    String start,end,sid,name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            view = inflater.inflate(R.layout.fragment3, container, false);
        }else {
            view = inflater.inflate(R.layout.fragment33, container, false);
        }
        myDatebaseHelper = new MyDatabase(getActivity(), SQLITE_NAME, null, 1);


        init();


        return view;
    }
    private void init() {
        listView = (ListView) view.findViewById(R.id.listview1);
        back = (ImageView) view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        starttime = (EditText) view.findViewById(R.id.starttime3);
        sids = (EditText) view.findViewById(R.id.sids);
        names = (EditText) view.findViewById(R.id.names);
        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               gettimes();
            }
        });

        endtime = (EditText) view.findViewById(R.id.endtime);
        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getendtimes();
            }
        });


        daochu = f(view,R.id.daochu);
        daochu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 导出pdf
               // intent(MainPdfActivity.class);
                //TODO 查询操作
                if(names.getText().toString().equals("")||sids.getText().toString().equals("")||
                    starttime.getText().toString().equals("")||endtime.getText().toString().equals("")){
                    toastShow("查询信息不完整");
                }else {
                    lst.clear();
                    lst = GetJingbaoData(myDatebaseHelper,start,end,sids.getText().toString(),
                            names.getText().toString());

                    adapter = new jingBaoAdapter(getActivity(),lst);
                    listView.setAdapter(adapter);
                }

            }
        });


    }

    public void gettimes(){
        Calendar calendar=Calendar.getInstance();
        new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String months = String.valueOf(month);
                String dayOfMonths = String.valueOf(dayOfMonth);
                if((month + 1)<10){
                    months="0"+String.valueOf(month+1);
                }
                if(dayOfMonth<10){
                    dayOfMonths="0"+String.valueOf(dayOfMonth);
                }
                starttime.setText(year+"/"+months+"/"+dayOfMonths);
                start= String.valueOf(year + months + dayOfMonths);
                String text = "你选择了：" + year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                toastShow(start);
            }
        }
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void getendtimes(){
        Calendar calendar=Calendar.getInstance();
        new DatePickerDialog( getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String months = String.valueOf(month);
                String dayOfMonths = String.valueOf(dayOfMonth);
                if((month + 1)<10){
                    months="0"+String.valueOf(month+1);
                }
                if(dayOfMonth<10){
                    dayOfMonths="0"+String.valueOf(dayOfMonth);
                }
                endtime.setText(year+"/"+months+"/"+dayOfMonths);
                end= String.valueOf(year + months + dayOfMonths);
                String text = "你选择了：" + year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                toastShow(end);
            }
        }
                ,calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            lst.clear();
            //TODO 可见操作
            lst = getData(myDatebaseHelper);
            adapter = new jingBaoAdapter(getActivity(),lst);
            listView.setAdapter(adapter);
        } else {
            //TODO 不可见操作
        }
    }


}
