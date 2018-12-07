package com.example.surface.smartbutler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.surface.smartbutler.fragment.ButlerFragment;
import com.example.surface.smartbutler.fragment.GirlFragment;
import com.example.surface.smartbutler.fragment.UserFragment;
import com.example.surface.smartbutler.fragment.WechatFragment;
import com.example.surface.smartbutler.ui.SettingActivity;
import com.example.surface.smartbutler.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
      private TabLayout mTabLyout;
      private ViewPager mViewPager;
      private List<String>mTitle;
      private List<Fragment>mFragment;
      private FloatingActionButton fab_setting;
      
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);
        initData();
        initView();

    }
    //初始化数据
    private void initData() {
        mTitle=new ArrayList<>();
        mTitle.add("服务管家");
        mTitle.add("微信精选");
        mTitle.add("美女社区");
        mTitle.add("个人中心");

       mFragment=new ArrayList<>();
       mFragment.add(new ButlerFragment());
        mFragment.add(new WechatFragment());
        mFragment.add(new GirlFragment());
        mFragment.add(new UserFragment());


    }

    //初始化View
    @SuppressLint("RestrictedApi")
    private void initView(){
             mTabLyout=findViewById(R.id.mTabLayout);
             mViewPager=findViewById(R.id.mViewPager);
             fab_setting=findViewById(R.id.fab_setting);
             fab_setting.setVisibility(View.GONE);
             fab_setting.setOnClickListener(this);
             //预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());

        //ViewPager滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onPageSelected(int i) {
                if (i==0) {
                    fab_setting.setVisibility(View.GONE);
                }else {
                    fab_setting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        //绑定
         mTabLyout.setupWithViewPager(mViewPager);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_setting:
                startActivity(new Intent(this,SettingActivity.class));
                break;
        }
    }
}
