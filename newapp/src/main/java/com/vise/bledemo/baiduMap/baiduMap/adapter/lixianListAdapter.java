package com.vise.bledemo.baiduMap.baiduMap.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.vise.bledemo.R;

import java.util.List;


public class lixianListAdapter extends BaseAdapter {
    private List<MKOLUpdateElement> mList;
    private Context mContext;
    public lixianListAdapter(Context mContext, List<MKOLUpdateElement> mList) {
        this.mContext=mContext;
        this.mList=mList;
    }

    @Override
    public int getCount() {
        return mList!=null?mList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return mList!=null?mList.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_lixian_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ss1.setText(mList.get(position).cityName);
        if(mList.get(position).update==false){
            holder.ss2.setText(String.valueOf("最新"));
        }else {
            holder.ss2.setText(String.valueOf("需要更新"));
        }

        holder.ss3.setText("地图大小:"+ String.valueOf(mList.get(position).serversize));

        return convertView;
    }




    private class ViewHolder{
        TextView ss1,ss2,ss3,ss4;
        public ViewHolder(View view) {
            if(view==null)
                return;
            ss1 = (TextView) view.findViewById(R.id.ss1);
            ss2 = (TextView)view.findViewById(R.id.ss2);
            ss3 = (TextView)view.findViewById(R.id.ss3);
            ss4 = (TextView)view.findViewById(R.id.ss4);
        }
    }
}