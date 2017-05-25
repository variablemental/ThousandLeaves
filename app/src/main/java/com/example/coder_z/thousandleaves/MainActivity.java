package com.example.coder_z.thousandleaves;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.example.coder_z.thousandleaves.Service.LocationService;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements Imageable{

    public static String TAG="MainActivity";
    public static String LOC_MSG="LOC_MSG";
    private static String IMG_TAG="image";
    public static final int REQUEST_PHOTO=1;
    private static final int REQUST_ALUBM=2;
    private static final String POST_SERVER_URL="http://119.23.210.134:8080/wb/process";
    private static final String PARAMS_IMG_NAME="name";
    private static final String PARAMS_IMG_CONTENT="image";

    private boolean IsShowingResult=false;
    private LeafDao dao=new LeafDao(this);
    private LocationService mService;
    private BDLocationListener mListener;
    private String loc_buf="";


    private Button mPhotoButton;
    private Button mPhotoManage;
    private Button mNetTest;
    private Button mUpLoad;
    private Button mUpLoads;
    private ImageView mImageView;
    private TextView mTestText;

    private Uri imageUri;

    private String temp_filename="";

    private Handler handler=new Handler(){
      @Override
      public void handleMessage(Message msg) {
          super.handleMessage(msg);
          BDLocation location=msg.getData().getParcelable(LOC_MSG);
          LatLng point=new LatLng(location.getLatitude(),location.getLongitude());
          loc_buf=new String(point.latitude+","+point.longitude);
      }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhotoButton=(Button)findViewById(R.id.photo_button);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PhotoFragmentActivity.class);
                startActivityForResult(intent,REQUEST_PHOTO);
            }
        });
        mPhotoManage=(Button)findViewById(R.id.photo_manage);
        mPhotoManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new AlertDialog.Builder(MainActivity.this)
                        .setTitle("从相册中选取").setMessage("要从相册选取照片吗？").setPositiveButton("是",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent=new Intent(Intent.ACTION_PICK,null);
                                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                        startActivityForResult(intent,REQUST_ALUBM);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                            }
                        }).create();
                dialog.show();
            }
        });
        Log.d(TAG,TAG+"start");
        mTestText=(TextView)findViewById(R.id.text_net_test);
        //Toast.makeText(this,"THis is Main activity",Toast.LENGTH_LONG).show();
        mImageView=(ImageView) findViewById(R.id.image_button);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path=getFileStreamPath(temp_filename).getAbsolutePath();
                ImageFragment.newInstance(path).show(getFragmentManager(),IMG_TAG);
            }
        });
        mNetTest=(Button)findViewById(R.id.net_test);
        mNetTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="http://119.23.210.134:8080/wb/process";
                StringRequest request=new StrRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(MainActivity.this,"访问成功",Toast.LENGTH_LONG).show();
                        Log.d(TAG,s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity.this,"访问失败",Toast.LENGTH_LONG).show();
                        Log.e(TAG,volleyError.toString());
                    }
                });
                request.setRetryPolicy(new DefaultRetryPolicy(50000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)); // 设置超时时间
                LocationApplication.getRequestQueue().add(request);
            }
        });
        mUpLoad=(Button)findViewById(R.id.image_upload);
        mUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog=ProgressDialog.show(MainActivity.this,"正在上传图片","请稍候",false,false);
                StrRequest request=new StrRequest(Request.Method.POST, POST_SERVER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        dialog.dismiss();
                        mTestText.setText(s);
                        Leaf leaf=new Leaf(39,"sss","",getFileStreamPath(temp_filename).getAbsolutePath());
                        if(!loc_buf.equals(""))
                            leaf.setDescription(leaf.getDescription()+"["+loc_buf+"]");
                        dao.insert(leaf);
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_LONG).show();
                    }

                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this,"上传失败",Toast.LENGTH_LONG).show();
                                //Toast.makeText(MainActivity.this,volleyError.getLocalizedMessage()+"",Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Bitmap bitmap=BitmapFactory.decodeFile(getFileStreamPath(temp_filename).getAbsolutePath());
                        String img=getStringImg(bitmap);
                        Map<String,String> params=new HashMap<String, String>();
                        params.put(PARAMS_IMG_NAME,temp_filename);
                        params.put(PARAMS_IMG_CONTENT,img);
                        //params.put("name","coder_z");
                        return params;
                    }
                };
                RequestQueue queue=LocationApplication.getRequestQueue();
                request.setRetryPolicy(new DefaultRetryPolicy(80000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(request);
            }
        });
        dao.open();
        mListener=new MyLocationListener(handler,LOC_MSG);
        mService=((LocationApplication) getApplication()).service;
        mService.registerLocationListener(mListener);
        mService.start();

    }


    @Override
    public void onActivityResult(int RequestCode,int ResultCode,Intent data){
        if(ResultCode!= Activity.RESULT_OK) return ;
        else {
            switch (RequestCode){
                case REQUEST_PHOTO:
                        String path=data.getStringExtra(PhotoFragment.FILENAME);
                        if(path!=null&&!path.equals("")) {
                            showPhoto(path);
                            temp_filename=path;
                        }
                        break;
                case REQUST_ALUBM:
/*                        Uri image=data.getData();
                        String[] column={MediaStore.Images.Media.DATA};                             //打开媒体库
                        Cursor cursor=getContentResolver().query(image,column,null,null,null);      //通过Uri搜寻指定照片
                        cursor.moveToFirst();
                        String image_path=cursor.getString(cursor.getColumnIndex(column[0]));                               //取得字段
                        showPhoto(image_path);*/
                    try {
                        BitmapDrawable b=PictureUtil.getScaledDrawable(this,imageUri);
                        mImageView.setImageDrawable(b);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG,"can`t find the file");
                        e.printStackTrace();
                    }
                    break;

            }
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,TAG+"start");
    }

    public void showPhoto(String filename) {
        String path=this.getFileStreamPath(filename).getAbsolutePath();
        showPhoto(path,false);
    }


    public void showPhoto(String path,boolean nonesense) {
        BitmapDrawable b= PictureUtil.getScaledDrawable(this,path);
        mImageView.setImageDrawable(b);
    }

    private String getAbsolutePath(String filename){
        return this.getFileStreamPath(filename).getAbsolutePath();
    }

    //把图片压缩成字符串格式
    private String getStringImg(Bitmap bitmap){
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
        byte[] bytes=bos.toByteArray();
        String strImg= Base64.encodeToString(bytes,Base64.DEFAULT);
        return strImg;
    }




}
