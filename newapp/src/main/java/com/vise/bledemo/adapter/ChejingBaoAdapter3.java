package com.vise.bledemo.adapter;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vise.bledemo.R;
import com.vise.bledemo.chezai.bean.ShouDongQuZheng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vise.bledemo.utils.url.delList;
import static com.vise.bledemo.utils.url.pdfList;


public class ChejingBaoAdapter3 extends BaseAdapter {
    private List<ShouDongQuZheng> mList;
    private Context mContext;
    private int Del;
    //步骤1
    private Map<Integer, Boolean> cbStateA;// 存放 CheckBox 的选中状态
    public ChejingBaoAdapter3(Context mContext, List<ShouDongQuZheng> mList, int Del) {
        this.mContext=mContext;
        this.mList=mList;
        this.Del=Del;
        cbStateA=new HashMap<Integer, Boolean>();
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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_baojing3, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (Del==1){
            holder.checkBox.setChecked(true);
            cbStateA.put(position, true);
            delList.add(mList.get(position).getId());
            pdfList.add(mList.get(position));
            Log.e("shifouz","1");
        }else {
            holder.checkBox.setChecked(false);
            cbStateA.remove(position);
            delList.clear();
            pdfList.clear();
            Log.e("shifouz","0");

        }
        Log.e("shifouz",""+Del);
        holder.s1.setText("设备名称:"+mList.get(position).getShebeiname());


        holder.s2.setText("操作人员:"+mList.get(position).getUsername());
        holder.s3.setText("浓度:"+mList.get(position).getNongdu()+"ppm.m");
        holder.s4.setText("时间:"+mList.get(position).getEndtime());


        if(mList.get(position).getFlag().equals("1")){
            holder.s5.setText("手动");
            holder.s5.setTextColor(mContext.getColor(R.color.huangse2));
            holder.s5.setBackgroundResource(R.drawable.bb0cs);
        }else if(mList.get(position).getFlag().equals("2")){
            holder.s5.setText("自动");
            holder.s5.setBackgroundResource(R.drawable.bbhuang);
            holder.s5.setTextColor(mContext.getColor(R.color.black));

        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cbStateA.put(position, true);
                    delList.add(mList.get(position).getId());
                    pdfList.add(mList.get(position));
                    Log.e("aaset","1");
                }else {
                    for(int i =0;i<delList.size();i++){
                        if(delList.get(i)==mList.get(position).getId()){
                            delList.remove(i);
                        }
                        if(pdfList.get(i).getId()==mList.get(position).getId()){
                            pdfList.remove(i);
                        }
                    }

                    Log.e("aaset","2");
                }
            }
        });

        if (cbStateA != null && cbStateA.containsKey(position)) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        return convertView;
    }




    private class ViewHolder{
        TextView s1,s2,s3,s4,s5;
        CheckBox checkBox;
        public ViewHolder(View view) {
            if(view==null)
                return;
            s1 = (TextView) view.findViewById(R.id.s1);
            s2 = (TextView) view.findViewById(R.id.s2);
            s3 = (TextView) view.findViewById(R.id.s3);
            s4 = (TextView) view.findViewById(R.id.s4);
            s5 = (TextView) view.findViewById(R.id.s5a);
            checkBox = view.findViewById(R.id.checkBox2);
        }
    }
}