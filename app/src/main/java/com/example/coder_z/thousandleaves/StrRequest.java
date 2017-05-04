package com.example.coder_z.thousandleaves;

import android.app.ProgressDialog;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by coder-z on 17-5-1.
 */

public class StrRequest extends StringRequest {
    public Map<String,String> headers=new HashMap<>();

    public StrRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Map<String,String> getHeaders(){
        return headers;
    }

}
