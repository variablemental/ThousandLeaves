package com.example.coder_z.thousandleaves;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;


/**
 * Created by coder-z on 17-5-14.
 */

public class MyLocationListener implements BDLocationListener{

    private Handler handler;
    private String LOC_MSG;
    public MyLocationListener(Handler handler,String msg){
        this.handler=handler;
        this.LOC_MSG=msg;
    }

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

    public Handler getHandler() {
        return handler;
    }
}