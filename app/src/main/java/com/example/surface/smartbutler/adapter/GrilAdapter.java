package com.example.surface.smartbutler.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.entity.GirlData;
import com.example.surface.smartbutler.utils.L;
import com.example.surface.smartbutler.utils.PicassoUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GrilAdapter extends BaseAdapter {
    private Context mContext;
    private List<GirlData>mList;
    private LayoutInflater inflater;
    private GirlData girlData;
    private WindowManager wm;
    private int width;
    private DisplayMetrics displayMetrics;
    public GrilAdapter(Context mContext,List<GirlData>mList){
        this.mContext=mContext;
        this.mList=mList;
        inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm= (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        width=displayMetrics.widthPixels;

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
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.girl_item,null);
            viewHolder.imageView=convertView.findViewById(R.id.image_view);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
         girlData=mList.get(position);
        //解析图片
        String url=girlData.getImgUrl();
        L.i(url);
        if (!TextUtils.isEmpty(url)) {
            PicassoUtils.loadImageViewSize(url,width/2,width/2,viewHolder.imageView);
        }
        return convertView;
    }
    private Integer[] image={
            R.drawable.robot,R.drawable.setting_icon,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher

    };

    class ViewHolder{
        private ImageView imageView;
    }
}
