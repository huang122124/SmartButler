package com.example.surface.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.entity.ChatListData;

import java.util.List;

public class ChatListAdapter extends BaseAdapter {
    public static final int VALUE_LEFT_TEXT=1;
    public static final int VALUE_RIGHT_TEXT=2;

    private Context mContext;
    private LayoutInflater inflater;
    private ChatListData data;
    private List<ChatListData>mList;
    public ChatListAdapter(Context mContext,List<ChatListData>mList){
        this.mContext=mContext;
        this.mList=mList;
        //获取系统服务
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
        ViewHolderLeftText viewHolderLeftText=null;
        ViewHolderRightText viewHolderRightText=null;
        //获取当前要显示的Type
        int type=getItemViewType(position);
        //判断是否第一次加载
        if (convertView==null){
            switch (type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText=new ViewHolderLeftText();
                    convertView=inflater.inflate(R.layout.left_item,null);
                    viewHolderLeftText.tv_left_text=convertView.findViewById(R.id.tv_left_text);
                    convertView.setTag(viewHolderLeftText);

                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText=new ViewHolderRightText();
                    convertView=inflater.inflate(R.layout.right_item,null);
                    viewHolderRightText.tv_right_text=convertView.findViewById(R.id.tv_right_text);
                    convertView.setTag(viewHolderRightText);

                    break;
            }

        }else {
            switch (type){
                case VALUE_LEFT_TEXT:
                    viewHolderLeftText= (ViewHolderLeftText) convertView.getTag();
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRightText= (ViewHolderRightText) convertView.getTag();
                    break;
            }
        }


        //赋值
        ChatListData data=mList.get(position);
        switch (type){
            case VALUE_LEFT_TEXT:
                viewHolderLeftText.tv_left_text.setText(data.getText());
                break;
            case VALUE_RIGHT_TEXT:
               viewHolderRightText.tv_right_text.setText(data.getText());
                break;
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        ChatListData data=mList.get(position);
        int type=data.getType();
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    class ViewHolderLeftText{
        private TextView tv_left_text;
    }

    class ViewHolderRightText{
        private TextView tv_right_text;
    }
}
