package com.example.surface.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.entity.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public  class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener{
    private EditText et_email;
    private Button btn_confirm;

    private EditText pwd_old;
    private EditText pwd_new;
    private EditText pwd_again;
    private Button change_pass;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        initView();
    }

    private void initView() {
        et_email=findViewById(R.id.et_email);
        btn_confirm=findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);

        pwd_old=findViewById(R.id.psw_old);
        pwd_new=findViewById(R.id.psw_new);
        pwd_again=findViewById(R.id.psw_again);
        change_pass=findViewById(R.id.change_pass);
        change_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_pass:
              String psw_old=pwd_old.getText().toString();
              String psw_new=pwd_new.getText().toString();
              String psw_again=pwd_again.getText().toString();
              if (!TextUtils.isEmpty(psw_old)&&!TextUtils.isEmpty(psw_new)&&!TextUtils.isEmpty(psw_again)){
                  if (psw_new.equals(psw_again)){
                      MyUser.updateCurrentUserPassword(psw_old, psw_new, new UpdateListener() {
                          @Override
                          public void done(BmobException e) {
                              if (e==null){
                                  Toast.makeText(ForgetPasswordActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                                  finish();
                              }else {
                                  Toast.makeText(ForgetPasswordActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                              }
                          }
                      });
                  } else {
                      Toast.makeText(ForgetPasswordActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                  }
              }else {
                  Toast.makeText(ForgetPasswordActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
              }

                break;
            case  R.id.btn_confirm:
               final String email=et_email.getText().toString();
               if (!TextUtils.isEmpty(email)){
                   MyUser.resetPasswordByEmail(email, new UpdateListener() {
                       @Override
                       public void done(BmobException e) {
                           if(e==null){
                               Toast.makeText(ForgetPasswordActivity.this,"重置密码请求成功，请到" +email +
                                       "邮箱进行密码重置操作",Toast.LENGTH_SHORT).show();
                               finish();
                           }else{
                               Toast.makeText(ForgetPasswordActivity.this,"邮件发送失败",Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }else {
                   Toast.makeText(this,"邮箱不能为空",Toast.LENGTH_SHORT).show();
               }
                break;
        }
    }
}
