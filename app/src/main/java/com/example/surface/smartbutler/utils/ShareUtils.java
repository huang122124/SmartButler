package com.example.surface.smartbutler.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ShareUtils {
    public static final String NAME="config";
      //String型数据
      public static void putString(Context mContext,String key,String value){
          SharedPreferences sp=mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
          sp.edit().putString(key,value).commit();
      }
      public static String getString(Context mContext,String key,String defaultValue){
          SharedPreferences sp=mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
          return sp.getString(key,defaultValue);
      }
       //Int型数据
    public static void putInt(Context mContext,String key,int value){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }
    public static int getInt(Context mContext,String key,int defaultValue){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getInt(key,defaultValue);
    }
    //Boolean数据
    public static void putBoolean(Context mContext,String key,Boolean value){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }
    public static Boolean getBoolean(Context mContext,String key,Boolean defaultValue){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key,defaultValue);
    }

    //删除单个数据
    public static void deleteShare(Context mContext,String key){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }
    //删除全部
    public static void deleteAll(Context mContext){
        SharedPreferences sp=mContext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}
