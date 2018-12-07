package com.example.surface.smartbutler.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.TextView;

public class UtilTools {
    public static void setFonts(Context mContext, TextView textView){
        Typeface typeface=Typeface.createFromAsset(mContext.getAssets(),"fonts/Font.ttf");
       textView.setTypeface(typeface);
    }

    //获取版本号
    public static String getVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "未知";
        }
    }
}
