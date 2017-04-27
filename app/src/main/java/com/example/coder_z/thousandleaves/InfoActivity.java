package com.example.coder_z.thousandleaves;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;


/**
 * Created by coder-z on 17-3-19.
 */

public class InfoActivity extends Activity {
    MapView  mapView=null;
    BaiduMap mBaiduMap=null;
    LocationClient mLocationClient=null;
    BDLocationListener mListener=new MyLocationListener();
    Pair locationPoint=null;

    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        mapView=(MapView)findViewById(R.id.mapView);
        mBaiduMap=mapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        initLocation();
        addMarkedPoint(locationPoint);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause(){
        super.onStop();
        mapView.onPause();
    }

    private void initLocation(){
        LocationClientOption option=new LocationClientOption();
        option.setCoorType("b0911");
        int span=5000;
        option.setScanSpan(span);
        option.setOpenGps(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    private void addMarkedPoint(Pair location){
        OverlayOptions options=new MarkerOptions().position(location.toLatLng());
        mBaiduMap.addOverlay(options);
    }

    public class MyLocationListener implements BDLocationListener{


        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if(locationPoint==null){
                locationPoint=new Pair(bdLocation.getLatitude(),bdLocation.getLongitude());
            }else{
                locationPoint.setX(bdLocation.getLatitude());
                locationPoint.setY(bdLocation.getLongitude());
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private class Pair{
        private double x;
        private double y;

        Pair(double x,double y){
            this.x=x;
            this.y=y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }

        public LatLng toLatLng(){
            return new LatLng(x,y);
        }
    }

}
