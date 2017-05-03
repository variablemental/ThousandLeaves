package com.example.coder_z.thousandleaves;

import android.app.Activity;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MainActivity extends AppCompatActivity implements Imageable{

    public static String TAG="MainActivity";
    private static String IMG_TAG="image";
    public static final int REQUEST_PHOTO=1;
    private static final int REQUST_ALUBM=2;


    private Button mPhotoButton;
    private Button mPhotoManage;
    private Button mNetTest;
    private Button mUpLoad;
    private ImageView mImageView;

    private Uri imageUri;

    private String temp_filename="";


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
                String url="http://www.baidu.com/";
                StringRequest request=new StrRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG);
                        System.out.print(s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG,volleyError.toString());
                    }
                });
                LocationApplication.getRequestQueue().add(request);
            }
        });
        mUpLoad=(Button)findViewById(R.id.image_upload);
        mUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUpLoad upLoad=new ImageUpLoad(getFileStreamPath(temp_filename).getAbsolutePath(),temp_filename,);
                LocationApplication.getRequestQueue().add(upLoad);
                LocationApplication.getRequestQueue().start();
            }
        });

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


}
