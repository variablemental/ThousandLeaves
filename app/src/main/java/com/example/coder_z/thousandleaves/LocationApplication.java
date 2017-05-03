package com.example.coder_z.thousandleaves;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.example.coder_z.thousandleaves.Service.LocationService;

/**
 * Created by coder-z on 17-4-29.
 */

public class LocationApplication extends Application {

    public static RequestQueue queue;
    public LocationService service;
    public Vibrator mVibrator;

    @Override
    public void onCreate(){
        service=new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        queue= Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getRequestQueue(){
        return queue;
    }

}
