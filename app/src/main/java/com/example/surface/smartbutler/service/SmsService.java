package com.example.surface.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.example.surface.smartbutler.utils.L;
import com.example.surface.smartbutler.utils.StaticClass;


public class SmsService extends Service {

    private SmsReceiver smsReceiver;
    private String smsPhone;
    private String smsContent;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }
   //初始化
    private void init() {
        L.i("init service");
        //动态注册
        smsReceiver=new SmsReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(StaticClass.SMS_ACTION);
        //设置权重
        intentFilter.setPriority(Integer.MAX_VALUE);
        //注册
        registerReceiver(smsReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("stop service");

        //注销
        unregisterReceiver(smsReceiver);
    }
//短信广播
    public class SmsReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
           String action=intent.getAction();
           if (StaticClass.SMS_ACTION==action){
               L.i("来短信了");
               //获取短信内容
              Object[] objs= (Object[]) intent.getExtras().get("pdus");
              //遍历数组
               for (Object obj:objs){
                   SmsMessage sms=SmsMessage.createFromPdu((byte[]) obj,null);
                   smsPhone=sms.getOriginatingAddress();
                   smsContent=sms.getMessageBody();
                   L.i("短信的内容："+smsPhone+":"+smsContent);
               }
           }
        }
    }
}
