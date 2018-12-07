package com.example.surface.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.entity.MyUser;
import com.example.surface.smartbutler.utils.L;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public  class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_user;
    private EditText et_password;
    private EditText et_passagain;
    private RadioGroup mRadioGroup;
    private EditText    et_email;
    private Button btnRegister;
    private boolean isMale=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        et_user=findViewById(R.id.et_user);
        et_password=findViewById(R.id.et_password);
        et_passagain=findViewById(R.id.et_passagain);
        mRadioGroup=findViewById(R.id.mRadioGroup);
        et_email=findViewById(R.id.et_email);
        btnRegister=findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                //获取到输入框的值
                final String name = et_user.getText().toString();
                final String password = et_password.getText().toString().trim();
                String passagain = et_passagain.getText().toString().trim();
                final String email = et_email.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(passagain) &&
                        !TextUtils.isEmpty(email)) {
                    //判断两次密码是否输入一致
                    if (password.equals(passagain)) {
                        //判断性别
                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (checkedId == R.id.rb_boy) {
                                    isMale = true;
                                    } else if (checkedId == R.id.rb_girl) {
                                    isMale = false;
                                }
                            }
                        });
                                //注册
                                MyUser user = new MyUser();
                                user.setUsername(name);
                                user.setPassword(password);
                                user.setEmail(email);
                                user.setSex(isMale);
                                user.signUp(new SaveListener<Object>() {
                                    @Override
                                    public void done(Object o, BmobException e) {
                                        if(e==null){
                                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }else{
                                            Toast.makeText(RegisterActivity.this,"注册失败"+e.toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
