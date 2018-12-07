package com.example.surface.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.entity.CourierData;

import java.util.List;

public class CourierAdapter extends BaseAdapter {
    private Context mContext;
    private List<CourierData>mList;
    private CourierData data;
    //布局加载器
    private LayoutInflater inflater;

    public CourierAdapter(Context mContext,List<CourierData>mList){
                this.mContext=mContext;
                this.mList=mList;
                inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder viewHolder=null;
           //第一次加载
        if (convertView==null){
          viewHolder=new ViewHolder();
          convertView=inflater.inflate(R.layout.layout_courier_item,null);
          viewHolder.tv_remark=convertView.findViewById(R.id.tv_remark);
          viewHolder.tv_datatime=convertView.findViewById(R.id.tv_datatime);
          //设置缓存
           convertView.setTag(viewHolder);

        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
          //设置数据
          data=mList.get(position);
        viewHolder.tv_remark.setText(data.getRemark());
        viewHolder.tv_datatime.setText(data.getDatetime());
        return convertView;
    }
    class ViewHolder{
        private TextView tv_remark;
        private TextView tv_datatime;

    }
}
