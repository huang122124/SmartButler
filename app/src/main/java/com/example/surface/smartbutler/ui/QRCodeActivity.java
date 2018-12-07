package com.example.surface.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.surface.smartbutler.R;
import com.google.zxing.WriterException;
import com.yzq.zxinglibrary.encode.CodeCreator;

public  class QRCodeActivity extends BaseActivity{
    private ImageView iv_qr_code;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        initView();
    }

    private void initView() {
        iv_qr_code=findViewById(R.id.iv_qr_code);
        Bitmap bitmap = null;
        try {
            /*
             * contentEtString：字符串内容
             * w：图片的宽
             * h：图片的高
             * logo：不需要logo的话直接传null
             * */
             int width=getResources().getDisplayMetrics().widthPixels;
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.grizzlies);
            bitmap = CodeCreator.createQRCode("我是智能管家", width/2, width/2, logo);
            iv_qr_code.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
