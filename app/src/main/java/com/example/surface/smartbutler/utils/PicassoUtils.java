package com.example.surface.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.example.surface.smartbutler.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;
import com.squareup.picasso.Transformation;

public class PicassoUtils {
    //加载图片
    public static void loadImageView(String url, ImageView imageView){
        Picasso.get().load(url).into(imageView);
    }

    //默认加载图片（指定大小）
    public static void loadImageViewSize(String url,int width,int height,ImageView imageView){
        Picasso.get()
                .load(url)
                .config(Bitmap.Config.RGB_565)
                .resize(width, height)
                .centerCrop()
                .into(imageView);
    }
    //加载图片（有默认图片）
     public static void loadImageViewHolder(String url,int loadImg,int errorImg,ImageView imageView){
         Picasso.get()
                 .load(url)
                 .placeholder(loadImg)
                 .error(errorImg)
                 .into(imageView);
     }

     //裁剪
    public static void loadImgViewCrop(String url,ImageView imageView){
        Picasso.get().load(url).transform(new CropSquareTransformation()).into(imageView);
    }
     //按比例裁剪图片(矩形)
     public static class CropSquareTransformation implements Transformation {
         @Override public Bitmap transform(Bitmap source) {
             int size = Math.min(source.getWidth(), source.getHeight());
             int x = (source.getWidth() - size) / 2;
             int y = (source.getHeight() - size) / 2;
             Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
             if (result != source) {
                 source.recycle();
             }
             return result;
         }

         @Override public String key() {
             return "square()";
         }
     }
}
