package com.example.surface.smartbutler.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.adapter.WeChatAdapter;
import com.example.surface.smartbutler.entity.WeChatData;
import com.example.surface.smartbutler.ui.WebViewActivity;
import com.example.surface.smartbutler.utils.L;
import com.example.surface.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WechatFragment extends Fragment {

             private ListView mListView;
             private List<WeChatData>mList=new ArrayList<>();
             private List<String>mListTitle=new ArrayList<>();
             private List<String>mListUrl=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_wehat,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mListView=view.findViewById(R.id.mListView);
        //解析接口
        String url=" http://v.juhe.cn/weixin/query?key="+StaticClass.WECHAT_KEY+"&ps=100";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //解析JSON
                parsingJson(t);

            }
        });
        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                L.d("position:  "+position);
                Intent intent=new Intent(getActivity(),WebViewActivity.class);
                 intent.putExtra("title",mListTitle.get(position));
                 intent.putExtra("url",mListUrl.get(position));
                 startActivity(intent);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
           JSONObject jsonResult= jsonObject.getJSONObject("result");
           JSONArray jsonArray=jsonResult.getJSONArray("list");
           for (int i=0;i<jsonArray.length();i++){
               JSONObject jsonItem= (JSONObject) jsonArray.get(i);
               WeChatData data=new WeChatData();
               String title=jsonItem.getString("title");
               String url=jsonItem.getString("url");
               data.setTitle(title);
               data.setSource(jsonItem.getString("source"));
               data.setImgUrl(jsonItem.getString("firstImg"));

              mList.add(data);
              mListTitle.add(title);
              mListUrl.add(url);
           }
            WeChatAdapter adapter=new WeChatAdapter(getActivity(),mList);
           mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
