package com.vise.bledemo.kailu.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.vise.bledemo.R;
import com.vise.bledemo.adapter.CaoZuoAdapter;
import com.vise.bledemo.baiduMap.baiduMap.view.WelcomeActivity;
import com.vise.bledemo.model.getCaozuo;
import com.vise.bledemo.sqlite.MyDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.vise.bledemo.utils.getTime.disposeTime;
import static com.vise.bledemo.utils.url.SQLITE_NAME;


public class caozuojiluFragment extends Fragment {
    View view;
    private ImageView back;
    ListView listView;
    private MyDatabase myDatebaseHelper;
    List<getCaozuo> ls = new ArrayList<>();
    CaoZuoAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmentcaozuo, container, false);
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
                System.exit(0);
            }
        });

        //sqlite数据库
        myDatebaseHelper = new MyDatabase(getActivity(), SQLITE_NAME, null, 1);
       // ls = getData(myDatebaseHelper);
        ls.add(new getCaozuo(1,"GBB79-II","10000/20000","123121255","张继科",disposeTime(),"一级警报值"));
        ls.add(new getCaozuo(1,"GBB79-II","10000/20000","521512555","张继科",disposeTime(),"一级警报值"));
        ls.add(new getCaozuo(1,"GBB79-II","10000/20000","521515555","张继科",disposeTime(),"二级警报值"));
        ls.add(new getCaozuo(1,"GBB79-II","10000/20000","311121321","张继科",disposeTime(),"一级警报值"));
        ls.add(new getCaozuo(1,"GBB79-II","10000/20000","325152555","张继科",disposeTime(),"一级警报值"));
        ls.add(new getCaozuo(1,"GBB79-II","10000/20000","123121255","张继科",disposeTime(),"一级警报值"));
        ls.add(new getCaozuo(1,"GBB79-II","10000/20000","521512555","张继科",disposeTime(),"一级警报值"));
        ls.add(new getCaozuo(1,"GBB79-II","10000/20000","521515555","张继科",disposeTime(),"二级警报值"));
        ls.add(new getCaozuo(1,"GBB79-II","10000/20000","311121321","张继科",disposeTime(),"一级警报值"));
        ls.add(new getCaozuo(1,"GBB79-II","10000/20000","325152555","张继科",disposeTime(),"一级警报值"));
        adapter = new CaoZuoAdapter(getActivity(),ls);
        listView = (ListView) view.findViewById(R.id.listview1);
        listView.setAdapter(adapter);
    }
}
