package com.example.surface.smartbutler.entity;
import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
    private  boolean sex;

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
