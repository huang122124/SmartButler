package com.example.surface.smartbutler.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.adapter.GrilAdapter;
import com.example.surface.smartbutler.entity.GirlData;
import com.example.surface.smartbutler.utils.L;
import com.example.surface.smartbutler.utils.StaticClass;
import com.google.gson.JsonObject;
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
public class GirlFragment extends Fragment {


private GridView mGridView;
private List<GirlData>mList=new ArrayList<>();
private List<String>mListUrl=new ArrayList<>();
private GrilAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_girl,null);
        findView(view);
        return view;
    }

    private void findView( View view) {
        mGridView=view.findViewById(R.id.mGridView);
        //解析
        RxVolley.get(StaticClass.GIRL_URL, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
//                L.i("Json: "+t);
                parsingJson(t);
            }
        });

    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
           JSONArray jsonArray= jsonObject.getJSONArray("data");
           for (int i=0;i<jsonArray.length();i++){
               JSONObject object= (JSONObject) jsonArray.get(i);
               String url=object.getString("download_url");
               mListUrl.add(url);
               GirlData data=new GirlData();
               data.setImgUrl(url);
               mList.add(data);
           }
           adapter=new GrilAdapter(getActivity(),mList);
           //设置适配器
           mGridView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
