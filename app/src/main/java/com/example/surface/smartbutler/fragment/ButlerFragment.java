package com.example.surface.smartbutler.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.adapter.ChatListAdapter;
import com.example.surface.smartbutler.entity.ChatListData;
import com.example.surface.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class ButlerFragment extends Fragment implements View.OnClickListener {
private EditText et_text;
private Button btn_send;
 private ListView mChatListView;
 private List<ChatListData>mList=new ArrayList<>();
 private ChatListAdapter adapter;
    public ButlerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_butler,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mChatListView=view.findViewById(R.id.mChatListView);
        et_text=view.findViewById(R.id.et_text);
        btn_send=view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        //设置适配器
        adapter=new ChatListAdapter(getActivity(),mList);
        mChatListView.setAdapter(adapter);
        addLeftItem("你好，我是小管家");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                //获取输入框内容
                String text=et_text.getText().toString();
                String answer="我不知道";
                if (!TextUtils.isEmpty(text)){
                     if (text.length()<=120){
                         //清空输入框
                         et_text.setText("");
                         //添加输入内容到right item
                         addRightItem(text);
                            //发送机器人请求
                         String url = "http://www.tuling123.com/openapi/api?key=" + StaticClass.CHAT_LIST_KEY
                                 + "&info=" +text;
                         RxVolley.get(url, new HttpCallback() {
                             @Override
                             public void onSuccess(String t) {
                                 ParsingJson(t);
                             }
                         });


                     }else {
                         Toast.makeText(getActivity(), "输入长度超出限制", Toast.LENGTH_SHORT).show();
                     }
                }else {
                    Toast.makeText(getActivity(), "输入框不能为空", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    private void ParsingJson(String t) {
        try {
            JSONObject object=new JSONObject(t);
            String answer=object.getString("text");
            //拿到机器人返回值后添加在leftItem
            addLeftItem(answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //添加左边文本
    private void addLeftItem(String text){
        ChatListData data=new ChatListData();
        data.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        data.setText(text);
        mList.add(data);
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }
    //添加右边文本
    private void addRightItem(String text){
        ChatListData data=new ChatListData();
        data.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        data.setText(text);
        mList.add(data);
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }

}
