package com.vise.bledemo.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vise.bledemo.R;
import com.vise.bledemo.model.GetJingbao;
import com.vise.bledemo.model.getCaozuo;

import java.util.List;


public class CaoZuoAdapter extends BaseAdapter {
    private List<getCaozuo> mList;
    private Context mContext;
    public CaoZuoAdapter(Context mContext, List<getCaozuo> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_caozuo, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.s1.setText("设备位号:"+mList.get(position).getSbName());
        holder.s2.setText(""+mList.get(position).getNumvalues());
        holder.s3.setText("设备ID:"+mList.get(position).getsId());
        holder.s4.setText("操作员:"+mList.get(position).getName());
        holder.s5.setText("操作时间:"+mList.get(position).getTime());
        holder.s6.setText("操作项:"+mList.get(position).getInfo());
        return convertView;
    }




    private class ViewHolder{
        TextView s1,s2,s3,s4,s5,s6;
        public ViewHolder(View view) {
            if(view==null)
                return;
            s1 = (TextView) view.findViewById(R.id.s1);
            s2 = (TextView) view.findViewById(R.id.s2);
            s3 = (TextView) view.findViewById(R.id.s3);
            s4 = (TextView) view.findViewById(R.id.s4);
            s5 = (TextView) view.findViewById(R.id.s5);
            s6 = (TextView) view.findViewById(R.id.s6);
        }
    }
}