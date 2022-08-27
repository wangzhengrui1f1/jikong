package com.vise.bledemo.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vise.bledemo.R;
import com.vise.bledemo.model.GetJingbao;

import java.util.List;


public class jingBaoAdapter extends BaseAdapter {
    private List<GetJingbao> mList;
    private Context mContext;
    public jingBaoAdapter(Context mContext, List<GetJingbao> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_baojing, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.s1.setText(mList.get(position).getTitle());
        if(mList.get(position).getGrade()==1){
            holder.s2.setBackgroundResource(R.drawable.bb1);
            holder.s2.setText("一级");
        }else if(mList.get(position).getGrade() == 2){
            holder.s2.setBackgroundResource(R.drawable.bb2);
            holder.s2.setText("二级");
        }else if(mList.get(position).getGrade() == 3){
            holder.s2.setBackgroundResource(R.drawable.bb3);
            holder.s2.setText("三级");
        }

        holder.s3.setText(mList.get(position).getInfo());
        holder.s4.setText(mList.get(position).getName());
        holder.s5.setText(mList.get(position).getTime());

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

        }
    }
}