package com.example.surface.smartbutler.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.surface.smartbutler.MainActivity;
import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.entity.MyUser;
import com.example.surface.smartbutler.utils.ShareUtils;
import com.example.surface.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //注册按钮
    private Button btn_register;
    private Button btn_login;
    private EditText loginName;
    private EditText loginPassword;
    private CheckBox remember_pass;
    private TextView tv_forget;
    private Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initview();
    }

    private void initview() {
        btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_login=findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        loginName=findViewById(R.id.username);
        loginPassword=findViewById(R.id.password);
        remember_pass=findViewById(R.id.remember_pass);
        tv_forget=findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);
        //设置“记住密码”选中状态
        boolean isChecked=ShareUtils.getBoolean(this,"remember_pass",false);
        remember_pass.setChecked(isChecked);
        if (isChecked) {
            String username = ShareUtils.getString(this, "username", "");
            String password = ShareUtils.getString(this, "password", "");
            loginName.setText(username);
            loginPassword.setText(password);
        }
//初始化dialog
       dialog=new CustomDialog(this,100,100,R.layout.dialogloading,R.style.Theme_Dialog,Gravity.CENTER,R.style.pop_anim_style);
    //屏幕外点击无效
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_forget:
                startActivity(new Intent(this,ForgetPasswordActivity.class));
                break;
            case R.id.btn_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.btn_login:
                //获取输入框文本   账号和密码
                String username=loginName.getText().toString().trim();
                String password=loginPassword.getText().toString();
                //判断是否为空
                if (!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)) {
                    dialog.show();
                    final MyUser myUser = new MyUser();
                    myUser.setUsername(username);
                    myUser.setPassword(password);
                    myUser.login(new SaveListener<Object>() {
                        @Override
                        public void done(Object o, BmobException e) {
                            dialog.dismiss();
                            if (e == null) {
                                if (myUser.getEmailVerified()) {
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    //跳转
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                    finish();
                                }else {
                                    Toast.makeText(LoginActivity.this, "请验证邮箱", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "登录失败"+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(LoginActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        ShareUtils.putBoolean(this,"remember_pass",remember_pass.isChecked());
       //判断状态,是否记住密码
        if (remember_pass.isChecked()){
            //记住用户名密码
            ShareUtils.putString(this,"username",loginName.getText().toString().trim());
            ShareUtils.putString(this,"password",loginPassword.getText().toString().trim());
        }else {
           ShareUtils.deleteShare(this,"username");
            ShareUtils.deleteShare(this,"password");
        }

    }
}
