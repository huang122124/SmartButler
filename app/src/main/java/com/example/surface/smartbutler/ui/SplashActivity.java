package com.example.surface.smartbutler.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.surface.smartbutler.MainActivity;
import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.utils.ShareUtils;
import com.example.surface.smartbutler.utils.StaticClass;
import com.example.surface.smartbutler.utils.UtilTools;

//闪屏页
public class SplashActivity extends AppCompatActivity {
    private TextView tv_splash;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StaticClass.HANDLER_SPLASH:
                         //判断程序是否第一次运行
                    if (isFirst()){
                        startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                    }else {
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        //延时2000ms
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH,2000);
        tv_splash=findViewById(R.id.tv_splash);
        //设置字体
        UtilTools.setFonts(this,tv_splash);

    }

    //判断程序是否第一次运行
    private boolean isFirst() {
        Boolean isFirst=ShareUtils.getBoolean(this,StaticClass.SHEARE_IS_FIRST,true);
        if (isFirst){
            ShareUtils.putBoolean(this,StaticClass.SHEARE_IS_FIRST,false);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
