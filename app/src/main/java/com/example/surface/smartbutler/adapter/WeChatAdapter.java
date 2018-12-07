package com.example.surface.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.entity.WeChatData;
import com.example.surface.smartbutler.utils.PicassoUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WeChatAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<WeChatData>mList;
    private WeChatData data;

    public  WeChatAdapter(Context mContext,List<WeChatData>mList){
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
        ViewHolder holder=null;
        if (convertView==null){
             holder=new ViewHolder();
             convertView=inflater.inflate(R.layout.wechat_item,null);
             holder.iv_img=convertView.findViewById(R.id.iv_img);
             holder.tv_title=convertView.findViewById(R.id.tv_title);
             holder.tv_sourse=convertView.findViewById(R.id.tv_sourse);
             convertView.setTag(holder);

        }else {
             holder= (ViewHolder) convertView.getTag();
        }
        data=mList.get(position);
        holder.tv_title.setText(data.getTitle());
        holder.tv_sourse.setText(data.getSource());

        //加载图片
//        PicassoUtils.loadImageViewHolder(data.getImgUrl(),R.mipmap.ic_launcher,R.mipmap.ic_launcher,holder.iv_img);
        return convertView;
    }

    class ViewHolder{
        private ImageView iv_img;
        private TextView tv_sourse;
        private TextView tv_title;
    }
}
