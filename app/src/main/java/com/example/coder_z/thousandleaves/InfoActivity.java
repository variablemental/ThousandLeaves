package com.example.coder_z.thousandleaves;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.coder_z.thousandleaves.Service.LocationService;


/**
 * Created by coder-z on 17-3-19.
 */

public class InfoActivity extends Activity {
    public static final String LOC_MSG="LOC_MSG";
    public static final String Tag="INFOACTIVITY";
    MapView  mapView=null;
    BaiduMap mBaiduMap=null;
    BDLocationListener mListener;/*=new MyLocationListener();*/
    LocationService mLocationService=null;
    LeafDao dao=new LeafDao(this);

    public Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            BDLocation location=msg.getData().getParcelable(LOC_MSG);
            LatLng point=new LatLng(location.getLatitude(),location.getLongitude());
            BitmapDescriptor mark;
            mark= BitmapDescriptorFactory.fromResource(R.drawable.huaji);
            OverlayOptions options=new MarkerOptions().position(point).icon(mark);
            mBaiduMap.addOverlay(options);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
            Log.d(Tag,point.latitude+","+point.longitude);
        }
    };


    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.location_activity);
        mapView=(MapView)findViewById(R.id.mapView);
        mBaiduMap=mapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mLocationService=((LocationApplication)getApplication()).service;
        mListener=new MyLocationListener(handler,LOC_MSG);
        mLocationService.registerLocationListener(mListener);
        mLocationService.start();
        //addMarkedPoint(locationPoint);
        dao.open();
    }

    private void setOverlay(LatLng point,String path) {
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        BitmapDescriptor mark=BitmapDescriptorFactory.fromBitmap(bitmap);
        OverlayOptions options=new MarkerOptions().position(point).icon(mark);
        mBaiduMap.addOverlay(options);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(point));
    }




/*    public class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Message loc_msg=handler.obtainMessage();
            if(bdLocation!=null){
                Bundle data=new Bundle();
                data.putParcelable(LOC_MSG,bdLocation);
                loc_msg.setData(data);
                handler.sendMessage(loc_msg);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if(dao!=null){
            Leaf[] leafs=dao.queryAll();
            for(int i=0;i<leafs.length;i++){
                String location=leafs[i].getLocation();                                                                //还有点问题：没办法过滤非法数据
                if(location!=null) {
                    double x = Double.valueOf(location.substring(1, location.indexOf(",")));                           //去掉第一个"["
                    double y = Double.valueOf(location.substring(location.indexOf(",") + 1, location.length() - 1));  //去掉","和最后一个“]”
                    setOverlay(new LatLng(x, y), leafs[i].getImgUrl());
                }
            }
        }

    }

    @Override
    public void onPause(){
        super.onStop();
        mapView.onPause();
    }




}
