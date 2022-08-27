package com.vise.bledemo.baiduMap.baiduMap.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.vise.bledemo.R;

import java.util.List;


public class lixiancityAdapter extends BaseAdapter {
    private List<MKOLSearchRecord> mList;
    private Context mContext;
    public lixiancityAdapter(Context mContext, List<MKOLSearchRecord> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.title2, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mList.get(position).cityName);
        return convertView;
    }




    private class ViewHolder{
        TextView name,status,num;
        public ViewHolder(View view) {
            if(view==null)
                return;
            name = (TextView) view.findViewById(R.id.ss1);
        }
    }
}