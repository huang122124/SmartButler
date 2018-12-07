package com.example.surface.smartbutler.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.service.SmsService;
import com.example.surface.smartbutler.utils.L;
import com.example.surface.smartbutler.utils.ShareUtils;
import com.example.surface.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;


import org.json.JSONException;
import org.json.JSONObject;

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private Switch sw_sms;
    private LinearLayout ll_update;
    private TextView tv_version;
    private int versionCode;
    private String versionName;
    private String url;
    private static final int REQUEST_CODE_SCAN=10000;
    //扫一扫
    private LinearLayout ll_scan;
    //扫描结果
    private TextView tv_scan_result;

    //生成二维码
    private LinearLayout ll_qr_code;
    //我的位置
    private LinearLayout ll_my_location;
    //关于软件
    private  LinearLayout ll_about;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();

    }

    private void initView() {
        sw_sms=findViewById(R.id.sw_sms);
        sw_sms.setOnClickListener(this);
        ll_update=findViewById(R.id.ll_update);
        tv_version=findViewById(R.id.tv_version);
        ll_update.setOnClickListener(this);

        ll_scan=findViewById(R.id.ll_scan);
        ll_scan.setOnClickListener(this);

        tv_scan_result=findViewById(R.id.tv_scan_result);

        ll_qr_code=findViewById(R.id.ll_qr_code);
        ll_qr_code.setOnClickListener(this);

        ll_my_location=findViewById(R.id.ll_my_location);
        ll_my_location.setOnClickListener(this);

        ll_about=findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);

        boolean isSms=ShareUtils.getBoolean(this,"isSms",false);
        sw_sms.setSelected(isSms);
        try {
            getVersionNamecode();
            tv_version.setText("检测版本:"+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            tv_version.setText("检测版本");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sw_sms:
                //切换相反
                sw_sms.setSelected(!sw_sms.isSelected());
                //保存状态
                ShareUtils.putBoolean(this,"isSms",sw_sms.isChecked());
                if (sw_sms.isChecked()){

                    startService(new Intent(this,SmsService.class));
                }else {
                   stopService(new Intent(this,SmsService.class));
                }
                break;
            case R.id.ll_update:
                //请求服务器的配置文件，拿到code
                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        L.i("Json: "+t);
                        ParsingJson(t);

                    }
                });
                break;
            case R.id.ll_scan:
                //打开扫描界面扫描条形码或二维码
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent,REQUEST_CODE_SCAN);
                break;
            case R.id.ll_qr_code:
                 startActivity(new Intent(SettingActivity.this,QRCodeActivity.class));
                break;
            case R.id.ll_my_location:
                startActivity(new Intent(this,LocationActivity.class));

                break;
            case R.id.ll_about:
                startActivity(new Intent(this,AboutActivity.class));
                break;
        }
    }

    private void ParsingJson(String t) {
        try {
            JSONObject object=new JSONObject(t);
            int code=object.getInt("versionCode");
            url=object.getString("url");
            if (code>versionCode){
                showUpdateDialog(object.getString("content"));
            }else {
                Toast.makeText(this, "已是最新版本", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
//弹出升级提示
    private void showUpdateDialog(String content) {
            new AlertDialog.Builder(this)
                    .setTitle("有新版本啦！").setMessage(content)
                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(SettingActivity.this,UpdateActivity.class);
                            intent.putExtra("url",url);
                             startActivity(intent);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
    }

    private void getVersionNamecode() throws PackageManager.NameNotFoundException {
        PackageManager pm=getPackageManager();
        PackageInfo info=pm.getPackageInfo(getPackageName(),0);
        versionName=info.versionName;
        versionCode=info.versionCode;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                tv_scan_result.setText("扫描结果为：" + content);
            }

        }
    }
}