package com.example.surface.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.adapter.CourierAdapter;
import com.example.surface.smartbutler.entity.CourierData;
import com.example.surface.smartbutler.utils.L;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.surface.smartbutler.utils.StaticClass.COURIER_KEY;

//快递查询

public class CourierActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_name;
    private EditText et_number;
    private Button btn_get_courier;
    private ListView mListView;
    private List<CourierData> mList=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        initView();
    }

    private void initView() {
        et_name=findViewById(R.id.et_name);
        et_number=findViewById(R.id.et_number);
        btn_get_courier=findViewById(R.id.btn_get_courier);
        mListView=findViewById(R.id.mListView);
        btn_get_courier.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_courier:
                //获取输入框内容
                String name=et_name.getText().toString().trim();
                String number=et_number.getText().toString().trim();
                //判断非空
                if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(number)){
                    //使用 RxVolley 做网络请求
                     String url="http://v.juhe.cn/exp/index?key="+COURIER_KEY+"&com="+name+"&no="+number;
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
//                            Toast.makeText(CourierActivity.this, t, Toast.LENGTH_SHORT).show();
                            L.i("Json:"+t);
                            //解析Json
                            ParsingJson(t);
                        }

                        @Override
                        public void onFailure(VolleyError error) {
                            Toast.makeText(CourierActivity.this, "", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(CourierActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
//解析数据
    private void ParsingJson(String t) {
        try {
            JSONObject object=new JSONObject(t);
           JSONObject jsonResult=object.getJSONObject("result");
           JSONArray jsonArray=jsonResult.getJSONArray("list");
           for (int i=0;i<jsonArray.length();i++){
               JSONObject json= (JSONObject) jsonArray.get(i);
               CourierData data=new CourierData();
               data.setRemark(json.getString("remark"));
               data.setDatetime(json.getString("datetime"));
               mList.add(data);
           }
            Collections.reverse(mList);       //倒序显示
            CourierAdapter adapter=new CourierAdapter(this,mList);
           mListView.setAdapter(adapter);
            L.d("Parsing success");
        } catch (JSONException e) {
            L.e(e.toString());
            e.printStackTrace();
        }
    }
}
