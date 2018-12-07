package com.example.surface.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.surface.smartbutler.MainActivity;
import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.utils.L;

import java.util.ArrayList;
import java.util.List;

public  class GuideActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<View>mList=new ArrayList<>();
    private View view1,view2,view3;
    //小圆点
    private ImageView point1,point2,point3;
    //跳过按钮
    private ImageView iv_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        point1=findViewById(R.id.point1);
        point2=findViewById(R.id.point2);
        point3=findViewById(R.id.point3);
        setPointImage(true,false,false);

      initView();
    }
//初始化View
    private void initView() {
        iv_back=(ImageView)findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,LoginActivity.class));
                finish();
            }
        });
        mViewPager=findViewById(R.id.mViewPager);
        view1=View.inflate(this,R.layout.pager_item_1,null);
        view2=View.inflate(this,R.layout.pager_item_2,null);
        view3=View.inflate(this,R.layout.pager_item_3,null);

        view3.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                finish();
            }
        });
        mList.add(view1);
        mList.add(view2);
        mList.add(view3);
        mViewPager.setAdapter(new GuideAdaper());
        //监听Viewpager滑动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                L.i("Position: "+i);
                switch (i){
                    case 0:
                        setPointImage(true,false,false);
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setPointImage(false,true,false);
                        iv_back.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setPointImage(false,false,true);
                        iv_back.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private class GuideAdaper extends PagerAdapter {
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ((ViewPager)container).addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ( (ViewPager)container).removeView(mList.get(position));
        }
    }
//设置小圆点选中效果
    private void setPointImage(boolean isChecked1,boolean isChecked2,boolean isChecked3){
        if (isChecked1){
            point1.setBackgroundResource(R.drawable.point_on);
        }else {
            point1.setBackgroundResource(R.drawable.point_off);
        }

        if (isChecked2){
            point2.setBackgroundResource(R.drawable.point_on);
        }else {
            point2.setBackgroundResource(R.drawable.point_off);
        }

        if (isChecked3){
            point3.setBackgroundResource(R.drawable.point_on);
        }else {
            point3.setBackgroundResource(R.drawable.point_off);
        }

    }
}
