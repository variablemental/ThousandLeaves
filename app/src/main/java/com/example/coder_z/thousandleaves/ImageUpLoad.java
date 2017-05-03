package com.example.coder_z.thousandleaves;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by coder-z on 17-5-3.
 */

public class ImageUpLoad extends Request<String> {

    private MultipartEntity entity=new MultipartEntity();
    private File mFile;
    private Response.Listener mListener=null;
    private String mFileName;


    public ImageUpLoad(String url,Response.ErrorListener errorListener,String filename,File file,Response.Listener listener){
        super(Method.POST,url,errorListener);
        mFile=file;
        mFileName=filename;
        mListener=listener;
        if(mFile!=null)
            entity.addPart(mFileName,new FileBody(mFile));
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse networkResponse) {
        String parse;
        try {
            parse=new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
        } catch (UnsupportedEncodingException e) {
            parse=new String(networkResponse.data);
            e.printStackTrace();
        }
        return Response.success(parse,HttpHeaderParser.parseCacheHeaders(networkResponse));
    }

    @Override
    protected void deliverResponse(String s) {

    }

    @Override
    public byte[] getBody(){
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        try {
            entity.writeTo(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os.toByteArray();
    }




}
