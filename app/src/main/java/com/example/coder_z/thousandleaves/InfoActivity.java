package com.example.coder_z.thousandleaves;

import android.app.Activity;
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
    BDLocationListener mListener=new MyLocationListener();
    LocationService mLocationService=null;


    @Override
    public void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.location_activity);
        mapView=(MapView)findViewById(R.id.mapView);
        mBaiduMap=mapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mLocationService=((LocationApplication)getApplication()).service;
        mLocationService.registerLocationListener(mListener);
        mLocationService.start();
        //addMarkedPoint(locationPoint);
    }

    private Handler handler=new Handler(){

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


    public class MyLocationListener implements BDLocationListener{

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




}
