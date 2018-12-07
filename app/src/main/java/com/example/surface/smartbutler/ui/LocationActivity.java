package com.example.surface.smartbutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.example.surface.smartbutler.R;
import com.example.surface.smartbutler.utils.L;
public class LocationActivity extends BaseActivity{

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private BDLocation location;
    public LocationClient mLocationClient = null;
    private boolean isFirstLocate=true;
    private MyLocationListener myListener = new MyLocationListener();
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
//原有BDLocationListener接口暂时同步保留。具体介绍请参考后文第四步的说明
    private MyLocationConfiguration.LocationMode mode=MyLocationConfiguration.LocationMode.FOLLOWING;
    BitmapDescriptor mCurrentMarker;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mLocationClient = new LocationClient(getApplicationContext());//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);//注册监听函数
        mMapView=findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//普通地图 ,mBaiduMap是地图控制器对象
        mBaiduMap.setMyLocationEnabled(true);
        // 设置定位数据
        UiSettings mUiSettings = mBaiduMap.getUiSettings();
//实例化UiSettings类对象 mUiSettings.setCompassEnabled(enable);
        mUiSettings.setScrollGesturesEnabled(true);
        //地图缩放
        mUiSettings.setZoomGesturesEnabled(true);

        initLocation();
        mLocationClient.start();
//mLocationClient为第二步初始化过的LocationClient对象
//调用LocationClient的start()方法，便可发起定位请求
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        mLocationClient.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mLocationClient.stop();
        // 当不需要定位图层时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }


   private void initLocation(){
       LocationClientOption option = new LocationClientOption();

       option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，设置定位模式，默认高精度
//LocationMode.Hight_Accuracy：高精度；
//LocationMode. Battery_Saving：低功耗；
//LocationMode. Device_Sensors：仅使用设备；

       option.setCoorType("GCJ02");
//可选，设置返回经纬度坐标类型，默认GCJ02
//GCJ02：国测局坐标；
//BD09ll：百度经纬度坐标；
//BD09：百度墨卡托坐标；
//海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

       option.setScanSpan(1000);
//可选，设置发起定位请求的间隔，int类型，单位ms
//如果设置为0，则代表单次定位，即仅定位一次，默认为0
//如果设置非0，需设置1000ms以上才有效

       option.setOpenGps(true);
//可选，设置是否使用gps，默认false
//使用高精度和仅用设备两种定位模式的，参数必须设置为true

       option.setLocationNotify(true);
//可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

       option.setIgnoreKillProcess(false);
//可选，定位SDK内部是一个service，并放到了独立进程。
//设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

       option.SetIgnoreCacheException(false);
//可选，设置是否收集Crash信息，默认收集，即参数为false

       option.setWifiCacheTimeOut(5*60*1000);
//可选，V7.2版本新增能力
//如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位
       option.setIsNeedLocationDescribe(true);
//可选，是否需要位置描述信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的位置信息，此处必须为true
       option.setEnableSimulateGps(false);
//可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
       option.setIsNeedAddress(true);
       option.setIsNeedLocationPoiList(true);
       mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
   }
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            double latitude = location.getLatitude();    //获取纬度信息
            Toast.makeText(LocationActivity.this, "纬度："+latitude, Toast.LENGTH_SHORT).show();
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f
            String coorType = location.getCoorType();//获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            int errorCode = location.getLocType();//获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            String locationDescribe = location.getLocationDescribe();    //获取位置描述信息
            StringBuilder builder=new StringBuilder();
            builder.append(latitude).append(longitude).append(locationDescribe);
            L.i(builder.toString());

                LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
                MapStatusUpdate update=MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(update);
                update=MapStatusUpdateFactory.zoomTo(16f);
                mBaiduMap.animateMapStatus(update);

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

// 设置定位数据
            mBaiduMap.setMyLocationData(locData);

// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_geo);
            MyLocationConfiguration config = new MyLocationConfiguration(mode, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfiguration(config);
//            navigateTo(location);
            mLocationClient.stop();

        }
    }
    private void navigateTo(BDLocation location){
        if (isFirstLocate){
            LatLng ll=new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update=MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            update=MapStatusUpdateFactory.zoomTo(8f);
            mBaiduMap.animateMapStatus(update);
            isFirstLocate=false;
        }
        MyLocationData.Builder locationBuilder=new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData=locationBuilder.build();
        mBaiduMap.setMyLocationData( locationData);
    }

}
